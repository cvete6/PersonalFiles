package com.example.demo.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

/**
 * Configure email messages structure for mail that is sent to new employees and mail that is sent to workers in
 * Human Capital Management that need to be notified that some passport will not be valid for the next month
 */
@ConfigurationProperties("email")
public class EmailPropertiesConfiguration {

    @Value("from")
    private String from;

    @Value("to")
    private String to;

    @Value("subjectForNewEmployee")
    private String subjectNewEmployee;

    @Value("subjectForExpiredPassport")
    private String subjectForExpiredPassport;

    @Value("messageContentForNewEmployee")
    private String messageContentForNewEmployee;

    @Value("messageContentForExpiredPassport")
    private String messageContentForExpiredPassport;

    @Value("employeesListWithInvalidPassport")
    private String employeesListWithInvalidPassport;

    private File emptyPDFFile = new File("PersonalFile.pdf");

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubjectNewEmployee() {
        return subjectNewEmployee;
    }

    public void setSubjectNewEmployee(String subjectNewEmployee) {
        this.subjectNewEmployee = subjectNewEmployee;
    }

    public String getSubjectForExpiredPassport() {
        return subjectForExpiredPassport;
    }

    public void setSubjectForExpiredPassport(String subjectForExpiredPassport) {
        this.subjectForExpiredPassport = subjectForExpiredPassport;
    }

    public String getMessageContentForNewEmployee() {
        return messageContentForNewEmployee;
    }

    public void setMessageContentForNewEmployee(String messageContentForNewEmployee) {
        this.messageContentForNewEmployee = messageContentForNewEmployee;
    }

    public String getMessageContentForExpiredPassport() {
        return messageContentForExpiredPassport;
    }

    public void setMessageContentForExpiredPassport(String messageContentForExpiredPassport) {
        this.messageContentForExpiredPassport = messageContentForExpiredPassport;
    }

    public FileSystemResource getAttachmentFile() {
        return new FileSystemResource(emptyPDFFile);
    }


    public String getEmployeesListWithInvalidPassport() {
        return employeesListWithInvalidPassport;
    }

    public void setEmployeesListWithInvalidPassport(String employeesListWithInvalidPassport) {
        this.employeesListWithInvalidPassport = employeesListWithInvalidPassport;
    }

}