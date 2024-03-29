package com.kane.FXDataReceiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService{

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender emailSender;

    public boolean sendSimpleMessage(String to,
                                  String subject,
                                  String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
            return true;
        }catch (MailException e){
            logger.error(e.getMessage());
        }
        return false;
    }

    public boolean sendMessageWithAttachment(String to,
                                          String subject,
                                          String text,
                                          String pathToAttachment) {

        try {

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment(file.getFilename(), file);

            emailSender.send(message);

            logger.info("Send email with attachment successfully.");
            return true;

        } catch (MessagingException e) {
            logger.error(e.getMessage());
        }catch (MailSendException e){
            logger.error(e.getMessage());
        }
        return false;
    }
}
