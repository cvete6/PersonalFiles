package com.example.demo.Service.EmailSenderServiceImpl;


import com.example.demo.Configuration.EmailPropertiesConfiguration;
import com.example.demo.Configuration.EmployeePassportValidityConfiguration;
import com.example.demo.DomainModel.Person;
import com.example.demo.Exceptions.EmailNotSentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@EnableAutoConfiguration
@Service
public class EmailSenderServiceImpl {

    @Autowired
    private JavaMailSender javaMailSender;

    public EmailPropertiesConfiguration configurationEmailProperties;

    public EmployeePassportValidityConfiguration employeePassportValidityConfiguration;

    public EmailSenderServiceImpl(EmailPropertiesConfiguration configurationEmailProperties, EmployeePassportValidityConfiguration employeePassportValidityConfiguration) {
        this.configurationEmailProperties = configurationEmailProperties;
        this.employeePassportValidityConfiguration = employeePassportValidityConfiguration;
    }

    public void sendMessageWithAttachment(String email) throws MessagingException {
        String from = configurationEmailProperties.getFrom();
        String[] to = new String[1];
        to[0] = email;
        String subject = configurationEmailProperties.getSubjectNewEmployee();
        String messageContent = configurationEmailProperties.getMessageContentForNewEmployee();
        FileSystemResource attachmentFile = configurationEmailProperties.getAttachmentFile();
        sendMail(from, to, subject, messageContent, attachmentFile);
    }

    public void sendNotificationMessageForInvalidPassport(List<? extends Person> personList) throws MessagingException {
        String to = configurationEmailProperties.getTo();
        if (!to.isEmpty() && !personList.isEmpty()) {
            String from = configurationEmailProperties.getFrom();
            String[] toList = to.split(",");
            String subject = configurationEmailProperties.getSubjectForExpiredPassport();

            StringBuilder messageContent = new StringBuilder();
            String introMessageContent = configurationEmailProperties.getMessageContentForExpiredPassport();
            String numberOfDaysBeforePassportExpires = employeePassportValidityConfiguration
                    .getNumberOfDaysBeforePassportExpires();
            messageContent =
                    messageContent.append(String.format(introMessageContent, numberOfDaysBeforePassportExpires));

            for (Person c : personList) {
                String messageStructureForEmployeesList =
                        configurationEmailProperties.getEmployeesListWithInvalidPassport();
                messageContent.append(String.format(messageStructureForEmployeesList,
                        c.getGivenName(), c.getFamilyName(), c.getSocialNumber()));
            }
            sendMail(from, toList, subject, messageContent.toString(), null);
        }
    }


    private void sendMail(String from, String[] to, String subject, String body, FileSystemResource attachmentFile)
            throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(body);

            if (attachmentFile != null) {
                helper.addAttachment("Application for employment.pdf", attachmentFile, "application/pdf");
            }
            javaMailSender.send(mimeMessage);
        } catch (RuntimeException e) {
            throw new EmailNotSentException("Email not sent", e);
        }
    }
}