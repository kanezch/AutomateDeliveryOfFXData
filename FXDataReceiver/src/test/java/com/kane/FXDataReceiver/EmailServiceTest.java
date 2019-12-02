package com.kane.FXDataReceiver;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.junit.Assert.*;

/**
 * Created by
 *
 * @author kane
 * @date 1/12/19
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void When_sendSimpleMessageSuccessful_Expect_True() {
        boolean result = emailService.sendSimpleMessage("to", "subject", "text");
        Assert.assertTrue(result);
    }

    @Test
    public void When_sendSimleMessageMailException_Expect_False() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("to");
        message.setSubject("subject");
        message.setText("text");

        Mockito.doThrow(MailSendException.class).when(emailSender).send(message);
        boolean result = emailService.sendSimpleMessage("to", "subject", "text");
        Assert.assertFalse(result);
    }

    @Test
    public void When_sendMessageWithAttachmentSuccessful_Expect_True() {
        MimeMessage message = new MimeMessage((Session)null);

        Mockito.when(emailSender.createMimeMessage()).thenReturn(message);
        boolean result = emailService.sendMessageWithAttachment("to", "subject", "text", "path");
        Assert.assertTrue(result);
    }

    @Test
    public void When_sendMessageWithAttachmentMailSendException_Expect_False() {
        MimeMessage message = new MimeMessage((Session)null);

        Mockito.when(emailSender.createMimeMessage()).thenReturn(message);

        Mockito.doThrow(MailSendException.class).when(emailSender).send(message);
        boolean result = emailService.sendMessageWithAttachment("to", "subject", "text", "path");
        Assert.assertFalse(result);
    }
}