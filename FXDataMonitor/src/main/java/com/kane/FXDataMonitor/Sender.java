package com.kane.FXDataMonitor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by
 *
 * @author kane
 * @date 22/11/19
 */
@Component
public class Sender {
    private final RabbitTemplate rabbitTemplate;

    public Sender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendFXDataMessage(String message) throws Exception {
        rabbitTemplate.convertAndSend("FXDataExchange", "FXData", message);
    }
}

