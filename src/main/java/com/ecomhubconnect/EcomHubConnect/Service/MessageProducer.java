// MessageProducer.java
package com.ecomhubconnect.EcomHubConnect.Service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    public void sendMessage(String message) {
        logger.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend(message);
    }
}
