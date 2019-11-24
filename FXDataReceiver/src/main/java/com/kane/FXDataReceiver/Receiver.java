package com.kane.FXDataReceiver;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class Receiver {

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    private static final String EMAIL_ADDRESS = System.getenv().get("EMAIL_ADDRESS");

    @Autowired
    EmailService emailService;

    public void receiveMessage(String message){
        logger.info("Receive message from rabbitmq:{}", message);

        JSONObject jsonObject = new JSONObject(message);
        String csvFilePath = CsvUtil.assembleCSVFilePath();

        try{
            CsvUtil.createCSV(jsonObject, csvFilePath);
        }catch (IOException e){
            logger.error(e.getMessage());
        }

        logger.info("Send Email to {} with attachment.", EMAIL_ADDRESS);
        emailService.sendMessageWithAttachment(EMAIL_ADDRESS,
                                               "FXData change",
                                               "The real-time changed FXData has been attached to this email.",
                                                csvFilePath);
    }
}
