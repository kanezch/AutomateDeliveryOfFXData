package com.kane.FXDataMonitor;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
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

    @Autowired
    private Sender sender;

    public Monitor(Sender sender) { this.sender = sender; }

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
        RestTemplate restTemplate = new RestTemplate();
        JSONObject changedData = new JSONObject();
        String result = null;

/*        for (String url : urls) {
            RealExDataResp realExDataResp  = restTemplate.getForObject(url, RealExDataResp.class);
            if (realExDataResp.getChange() != 0){
                changedData.put(realExDataResp.getCode(), realExDataResp.getClose());
            }
        }*/

        changedData.put("AUDUSD",0.65);
        changedData.put("AUDJPY",73.5);

        String changedDataString = changedData.toString();
        logger.info("Changed FXData:{}", changedDataString);

        if (changedDataString.length() > 2){
            result = changedDataString;
        }

        return result;
    }

    @Override
    public void run(String... args) throws Exception{
        initURLs();

        for (;;){

            String message = fetchChangedFXData();
            if (message != null){
                sender.sendFXDataMessage(message);
            }

            Thread.sleep(60000);
        }
    }
}
