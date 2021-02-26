package com.example.demo.ClientDataBatch;

import com.example.demo.Configuration.EmployeePassportValidityConfiguration;
import com.example.demo.DomainModel.Person;
import org.springframework.batch.item.ItemProcessor;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Check if the person has a passport that expires in 30 days then return details for that person
 */
public class PassportItemProcessor implements ItemProcessor<Person, Person> {


    public EmployeePassportValidityConfiguration employeePassportValidityConfiguration;

    public PassportItemProcessor(
            EmployeePassportValidityConfiguration employeePassportValidityConfiguration) {
        this.employeePassportValidityConfiguration =
                employeePassportValidityConfiguration;
    }

    public PassportItemProcessor() {
    }

    /**
     * Check if person has a passport that expires in 30 days
     *
     * @param person person from the database on which we are going to check the validity of the passport
     * @return person return the person if it meets the conditions if it does not return null
     */
    @Override
    public Person process(Person person) {
        Instant now = Instant.now(); //current date
        String numberOfDaysBeforePassportExpires =
                employeePassportValidityConfiguration.getNumberOfDaysBeforePassportExpires();
        Integer days = Integer.valueOf(numberOfDaysBeforePassportExpires);

        Instant before = now.plus(Duration.ofDays(days));
        Date dateBefore = Date.from(before);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(dateBefore);
        if(person.getDateOfExpiryPassport() != null){
            Date dateOfExpiryPassport = person.getDateOfExpiryPassport();
            String expireDate = sdf.format(dateOfExpiryPassport);

            if (expireDate.equals(currentDate)) {
                return person;
            }
        }
        return null;
    }
}
