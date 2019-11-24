package com.kane.FXDataMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Sender {
    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    private final RabbitTemplate rabbitTemplate;

    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendFXDataMessage(String message)  {

        try{
            rabbitTemplate.convertAndSend("FXDataExchange", "FXData", message);
        }catch (AmqpException e){
            logger.error("Sender error: {}", e.getMessage());
        }
    }
}

