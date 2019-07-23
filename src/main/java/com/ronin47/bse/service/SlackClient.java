package com.ronin47.bse.service;

import com.ronin47.bse.domain.BseApiResponse;
import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class SlackClient {
    private final Logger logger = LoggerFactory.getLogger(SlackClient.class);
    private SlackSession slackSession;

    public SlackClient() {
        String slackToken = System.getenv("SLACK_TOKEN");
        System.out.println("Slack token " + slackToken);
        this.slackSession = SlackSessionFactory.createWebSocketSlackSession(slackToken);
    }

    public void sendMessage(BseApiResponse response) {
        try {
            slackSession.connect();
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
            slackSession.sendMessage(slackChannel, sb.toString());
        } catch (Exception e) {
            logger.error("Error while sending message from slack {}", e.getMessage());
        }
    }

    public void sendExceptionMessage(String exception){
        try{
            slackSession.connect();
            SlackChannel slackChannel = slackSession.findChannelByName("bsealertsexception");
            slackSession.sendMessage(slackChannel, exception);
        }catch (Exception e){
            logger.error("Error while sending exception from slack {}", e.getMessage());
        }
    }
}
