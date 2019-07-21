package com.ronin47.bse.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ronin47.bse.domain.BseApiResponse;
import com.ronin47.bse.domain.BseTableResponse;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAlerts {
    private final Logger logger = LoggerFactory.getLogger(GetAlerts.class);
    private final String baseApiPath = "https://api.bseindia.com/BseIndiaAPI/api/AnnGetData/w";
    private Map<String, BseApiResponse> readMap;

    @PostConstruct
    public void initializeMap() {
        try {
            if(readMap == null){
                readMap = new HashMap<>();
            }
            URL resource = this.getClass().getClassLoader().getResource("stocks.txt");
            FileReader fileReader = new FileReader(resource.getFile());
            BufferedReader br = new BufferedReader(fileReader);

            String stockId;
            while ((stockId = br.readLine()) != null) {
                if(!readMap.containsKey(stockId)){
                    readMap.put(stockId, null);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public List<BseApiResponse> getDetailsFromApi () {
            try {
                HttpClient httpClient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(baseApiPath);
                List<NameValuePair> params = new ArrayList<>();
                URI uri = new URIBuilder(httpGet.getURI()).addParameters(params).build();
                httpGet.setURI(uri);
                HttpResponse response = httpClient.execute(httpGet);
                ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                BseTableResponse apiResponse = objectMapper.readValue(response.getEntity().getContent(), BseTableResponse.class);
                return apiResponse.getTable();
            } catch (Exception e) {
                return null;
            }
        }
    }
