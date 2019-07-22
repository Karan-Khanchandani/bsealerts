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
        String slackToken = System.getProperty("SLACK_TOKEN");
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
            slackSession.sendMessage(slackChannel, sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
