package com.ronin47.bse.service;

import com.ronin47.bse.domain.BseApiResponse;
import com.ullink.slack.simpleslackapi.SlackPreparedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ReadNotificationsMap {
    private final Logger logger = LoggerFactory.getLogger(ReadNotificationsMap.class);
    private ScheduledExecutorService scheduledExecutorService;
    private GetAlerts getAlertsService;
    private SlackClient slackClient;
    private Map<Long, BseApiResponse> readMap;

    public ReadNotificationsMap(GetAlerts getAlertsService, SlackClient slackClient) {
        this.getAlertsService = getAlertsService;
        this.slackClient = slackClient;
    }

    @PostConstruct
    public void construct() {
        try {
            if(readMap == null){
                readMap = new HashMap<>();
            }
            URL resource = this.getClass().getClassLoader().getResource("stocks.txt");
            FileReader fileReader = new FileReader(resource.getFile());
            BufferedReader br = new BufferedReader(fileReader);

            Long stockId;
            String stock;
            while ((stock = br.readLine()) != null) {
                stockId = Long.valueOf(stock);
                if(!readMap.containsKey(stockId)){
                    readMap.put(stockId, null);
                }
            }
            scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    logger.info("Calling API");
                    List<BseApiResponse> responses = getAlertsService.getDetailsFromApi();
                    if(responses != null){
                        for(BseApiResponse response : responses){
                            if(readMap.containsKey(response.getScriptId())){
                                BseApiResponse mapResponse = readMap.get(response.getScriptId());
                                if(mapResponse == null || response.getNewsDateTime().isAfter(mapResponse.getNewsDateTime())){
                                    readMap.put(response.getScriptId(), response);

                                    slackClient.sendMessage(response);
                                }
                            }
                        }
                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Error while fetching API {}", e);
        }
    }

}
