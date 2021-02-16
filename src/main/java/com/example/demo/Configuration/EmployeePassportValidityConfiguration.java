package com.example.demo.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class to set values when checking employees passports (example once a day) and the passport
 * expires in the next x numberOfDaysBeforePassport
 */
@ConfigurationProperties("validity")
@Configuration
public class EmployeePassportValidityConfiguration {

    @Value("numberOfDaysBeforePassportExpires")
    private String numberOfDaysBeforePassportExpires;

    public String getNumberOfDaysBeforePassportExpires() {
        return numberOfDaysBeforePassportExpires;
    }

    public void setNumberOfDaysBeforePassportExpires(String numberOfDaysBeforePassportExpires) {
        this.numberOfDaysBeforePassportExpires = numberOfDaysBeforePassportExpires;
    }

}