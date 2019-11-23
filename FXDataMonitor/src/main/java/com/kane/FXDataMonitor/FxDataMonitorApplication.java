package com.kane.FXDataMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FxDataMonitorApplication {
    Logger logger = LoggerFactory.getLogger(FxDataMonitorApplication.class);

	@Bean
	Queue queue() {
	    logger.info("Create FxData Queue.");
		return new Queue("FXDataQueue", false);
	}

	@Bean
	TopicExchange exchange() {
        logger.info("Create FxData Exchange.");
		return new TopicExchange("FXDataExchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
        logger.info("Bind FxData Queue and Exchange.");
		return BindingBuilder.bind(queue).to(exchange).with("FXData");
	}

	public static void main(String[] args) {
		SpringApplication.run(FxDataMonitorApplication.class, args);
	}

}
