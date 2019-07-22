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
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class GetAlerts {
    private final Logger logger = LoggerFactory.getLogger(GetAlerts.class);
    private final String baseApiPath = "https://api.bseindia.com/BseIndiaAPI/api/AnnGetData/w";

    public List<BseApiResponse> getDetailsFromApi() {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(baseApiPath);
            List<NameValuePair> params = new ArrayList<>();

            String todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            params.add(new BasicNameValuePair("strCat", "-1"));
            params.add(new BasicNameValuePair("strType", "C"));
            params.add(new BasicNameValuePair("strScrip", ""));
            params.add(new BasicNameValuePair("strPrevDate", todayDate));
            params.add(new BasicNameValuePair("strSearch", "P"));
            params.add(new BasicNameValuePair("strToDate", todayDate));


            URI uri = new URIBuilder(httpGet.getURI()).addParameters(params).build();
            httpGet.setURI(uri);
            HttpResponse response = httpClient.execute(httpGet);
            ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            BseTableResponse apiResponse = objectMapper.readValue(response.getEntity().getContent(), BseTableResponse.class);
            return apiResponse.getTable();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
