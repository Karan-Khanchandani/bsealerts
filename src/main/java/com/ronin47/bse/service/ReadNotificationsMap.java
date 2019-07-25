package com.ronin47.bse.service;

import com.ronin47.bse.domain.BseApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
            if (readMap == null) {
                readMap = new HashMap<>();
            }
            String resourceName = "stocks.txt"; // could also be a constant
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Properties props = new Properties();
            InputStream resourceStream = loader.getResourceAsStream(resourceName);
            props.load(resourceStream);

            Long stockId;
            for (Object stk : props.values()) {
                String stock = String.valueOf(stk);
                stockId = Long.valueOf(stock);
                if (!readMap.containsKey(stockId)) {
                    readMap.put(stockId, null);
                }
            }

            scheduledExecutorService = Executors.newScheduledThreadPool(1);
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                logger.info("Calling API");
                List<BseApiResponse> responses = getAlertsService.getDetailsFromApi();
                if (responses != null) {
                    for (BseApiResponse response : responses) {
                        if (readMap.containsKey(response.getScriptId())) {
                            BseApiResponse mapResponse = readMap.get(response.getScriptId());
                            if (mapResponse == null || response.getNewsDateTime().isAfter(mapResponse.getNewsDateTime())) {
                                boolean isMessageSent = slackClient.sendMessage(response);
                                if (isMessageSent) {
                                    readMap.put(response.getScriptId(), response);
                                }
                            }
                        }
                    }
                }
            }, 0, 5, TimeUnit.SECONDS);
        } catch (Exception e) {
            slackClient.sendExceptionMessage(e.getMessage());
            logger.error("Error while fetching API {}", e);
        }
    }

}
