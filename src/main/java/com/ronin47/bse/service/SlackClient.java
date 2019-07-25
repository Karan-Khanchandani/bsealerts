package com.ronin47.bse.service;

import com.ronin47.bse.domain.BseApiResponse;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
public class SlackClient {
    private final Logger logger = LoggerFactory.getLogger(SlackClient.class);
    private SlackSession slackSession;

    public SlackClient() {
        String slackToken = System.getenv("SLACK_TOKEN");
        System.out.println("Slack token " + slackToken);
        SlackSessionFactory.SlackSessionFactoryBuilder builder = SlackSessionFactory.getSlackSessionBuilder(slackToken);
        builder.withConnectionHeartbeat(15, TimeUnit.SECONDS);
        builder.withAutoreconnectOnDisconnection(true);
        this.slackSession = builder.build();

    }

    @PostConstruct
    public void connect(){
        try {
            slackSession.connect();
            sendExceptionMessage("rrrr");
        } catch (IOException e) {
            logger.error("Connection to slack failed");
        }
    }

    public boolean sendMessage(BseApiResponse response) {
        try {
            //Rate limit for sending the message is 1s
            Thread.sleep(1000);
            SlackChannel slackChannel = slackSession.findChannelByName("bsealerts");
            StringBuilder sb = new StringBuilder();
            sb.append("*" + response.getNewsSub() + "*");
            sb.append("\n");
            sb.append(response.getNsUrl());
            sb.append("\n");
            sb.append(response.getNewsDateTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy,  hh:mm:ss a")));
            sb.append("\n");
            String directLink = "https://www.bseindia.com/corporates/anndet_new.aspx?newsid="+response.getNewsId();
            sb.append(directLink);
            return slackSession.sendMessage(slackChannel, sb.toString()).getReply().isOk();
        } catch (Exception e) {
            logger.error("Error while sending message from slack {}", e.getMessage());
            return false;
        }
    }

    public boolean sendExceptionMessage(String exception){
        try{
            SlackChannel slackChannel = slackSession.findChannelByName("bsealertsexception");
            return slackSession.sendMessage(slackChannel, exception).getReply().isOk();
        }catch (Exception e){
            logger.error("Error while sending exception from slack {}", e.getMessage());
            return false;
        }
    }
}
