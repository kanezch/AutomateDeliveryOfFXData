package com.kane.FXDataReceiver;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class Receiver {

    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    private static final String EMAIL_ADDRESS = System.getenv().get("EMAIL_ADDRESS");

    @Autowired
    EmailService emailService;

    public void receiveMessage(String message){
        logger.info("Receive message from rabbitmq:{}", message);

        JSONObject jsonObject = new JSONObject(message);

        DateTimeFormatter DateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String currentTime = DateFormatter.format(LocalDateTime.now());
        String csvFilePath = CsvUtil.assembleCSVFilePath(currentTime);

        boolean isSuccessful = CsvUtil.createCSV(jsonObject, csvFilePath);
        if (isSuccessful){
            logger.info("Send Email to {} with attachment.", EMAIL_ADDRESS);
            emailService.sendMessageWithAttachment(EMAIL_ADDRESS,
                    "FXData change",
                    "The real-time changed FXData has been attached to this email.",
                    csvFilePath);
        }else{
            logger.error("Create CSV failed.");
        }
    }
}
