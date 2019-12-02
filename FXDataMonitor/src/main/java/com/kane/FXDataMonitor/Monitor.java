package com.kane.FXDataMonitor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Monitor implements CommandLineRunner{

    private static final Logger logger = LoggerFactory.getLogger(Monitor.class);
    private static final String urlprefix ="https://eodhistoricaldata.com/api/real-time/";
    private static final String urlpostfix =".FOREX?api_token=";
    private static final String urltail ="&fmt=json";
    private static final String[] currency = {"AUDUSD", "AUDNZD", "AUDHKD", "AUDKRW", "AUDJPY"};
    private static final List<String> urls = new ArrayList<>();
    private static final String ApiToken = System.getenv().get("APITOKEN");


    private Sender sender;
    private RestTemplate restTemplate;

    @Autowired
    public Monitor(Sender sender, RestTemplate restTemplate) {
        this.sender = sender;
        this.restTemplate = restTemplate;}

    private void initURLs(){
        for (int i = 0; i < currency.length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(urlprefix);
            stringBuilder.append(currency[i]);
            stringBuilder.append(urlpostfix);
            stringBuilder.append(ApiToken);
            stringBuilder.append(urltail);
            urls.add(stringBuilder.toString());

            logger.info("Init url:{}", urls.toString());
        }
    }

    private String fetchChangedFXData(){

        JSONObject changedData = new JSONObject();
        String result = null;

        for (String url : urls) {
            try {
                RealExDataResp realExDataResp  = restTemplate.getForObject(url, RealExDataResp.class);
                if (realExDataResp != null && realExDataResp.getChange() != 0){
                    changedData.put(realExDataResp.getCode(), realExDataResp.getClose());
                }
            }catch (RestClientException e){
                logger.error("RestTemplate error:{}", e.getMessage());
            }
        }

        String changedDataString = changedData.toString();
        logger.info("Changed FXData:{}", changedDataString);

        if (changedDataString.length() > 2){
            result = changedDataString;
        }

        return result;
    }

    @Override
    public void run(String... args){
        initURLs();

        for (;;){

            String message = fetchChangedFXData();
            if (message != null){
                sender.sendFXDataMessage(message);
            }

            /* Get Data once per minute */
            try {
                Thread.sleep(60000);
            }
            catch (InterruptedException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
