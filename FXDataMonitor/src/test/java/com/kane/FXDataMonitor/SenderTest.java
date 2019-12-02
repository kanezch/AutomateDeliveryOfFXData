package com.kane.FXDataMonitor;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.Assert.*;

/**
 * Created by
 *
 * @author kane
 * @date 1/12/19
 */
@RunWith(MockitoJUnitRunner.class)
public class SenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private Sender sender;

    @Test
    public void When_SendMessageSuccessful_Expect_True() {
        boolean result = sender.sendFXDataMessage("xxx");
        Assert.assertTrue(result);
    }

    @Test
    public void When_SendMessageAmqpException_Expect_False() {

        Mockito.doThrow(AmqpException.class).when(rabbitTemplate).convertAndSend("FXDataExchange", "FXData", "xxx");
        boolean result = sender.sendFXDataMessage("xxx");
        Assert.assertFalse(result);
    }
}