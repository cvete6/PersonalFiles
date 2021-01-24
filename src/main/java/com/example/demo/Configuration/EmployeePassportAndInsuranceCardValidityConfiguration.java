package com.example.demo.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class to set values when checking employees passports (example once a day) and the passport
 * expires in the next x numberOfDaysBeforePassportAndInsuranceCardExpires
 */
@ConfigurationProperties("validity")
@Configuration
public class EmployeePassportAndInsuranceCardValidityConfiguration {

    @Value("numberOfDaysBeforePassportExpires")
    private String numberOfDaysBeforePassportExpires;

    @Value("numberOfDaysBeforeInsuranceCardExpires")
    private String numberOfDaysBeforeInsuranceCardExpires;

    public String getNumberOfDaysBeforePassportExpires() {
        return numberOfDaysBeforePassportExpires;
    }

    public void setNumberOfDaysBeforePassportExpires(String numberOfDaysBeforePassportExpires) {
        this.numberOfDaysBeforePassportExpires = numberOfDaysBeforePassportExpires;
    }

    public String getNumberOfDaysBeforeInsuranceCardExpires() {
        return numberOfDaysBeforeInsuranceCardExpires;
    }

    public void setNumberOfDaysBeforeInsuranceCardExpires(String numberOfDaysBeforeInsuranceCardExpires) {
        this.numberOfDaysBeforeInsuranceCardExpires = numberOfDaysBeforeInsuranceCardExpires;
    }
}