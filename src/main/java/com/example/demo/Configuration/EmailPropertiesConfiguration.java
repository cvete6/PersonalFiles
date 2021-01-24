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

    @Value("subjectForExpiredInsuranceCard")
    private String subjectForExpiredInsuranceCard;

    @Value("messageContentForNewEmployee")
    private String messageContentForNewEmployee;

    @Value("messageContentForExpiredPassport")
    private String messageContentForExpiredPassport;

    @Value("messageContentForInsuranceCard")
    private String messageContentForInsuranceCard;

    @Value("employeesListWithInvalidPassport")
    private String employeesListWithInvalidPassport;

    @Value("employeesListWithInvalidInsuranceCard")
    private String employeesListWithInvalidInsuranceCard;

    private File emptyPDFFile = new File("PdfFormForEmployment.pdf");

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

    public String getSubjectForExpiredInsuranceCard() {
        return subjectForExpiredInsuranceCard;
    }

    public void setSubjectForExpiredInsuranceCard(String subjectForExpiredInsuranceCard) {
        this.subjectForExpiredInsuranceCard = subjectForExpiredInsuranceCard;
    }

    public String getMessageContentForInsuranceCard() {
        return messageContentForInsuranceCard;
    }

    public void setMessageContentForInsuranceCard(String messageContentForInsuranceCard) {
        this.messageContentForInsuranceCard = messageContentForInsuranceCard;
    }

    public String getEmployeesListWithInvalidPassport() {
        return employeesListWithInvalidPassport;
    }

    public void setEmployeesListWithInvalidPassport(String employeesListWithInvalidPassport) {
        this.employeesListWithInvalidPassport = employeesListWithInvalidPassport;
    }

    public String getEmployeesListWithInvalidInsuranceCard() {
        return employeesListWithInvalidInsuranceCard;
    }

    public void setEmployeesListWithInvalidInsuranceCard(String employeesListWithInvalidInsuranceCard) {
        this.employeesListWithInvalidInsuranceCard = employeesListWithInvalidInsuranceCard;
    }
}