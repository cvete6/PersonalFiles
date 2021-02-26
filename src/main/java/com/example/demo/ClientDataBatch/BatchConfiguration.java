package com.example.demo.ClientDataBatch;

import com.example.demo.Configuration.EmployeePassportValidityConfiguration;
import com.example.demo.DomainModel.Person;
import com.example.demo.Service.EmailSenderServiceImpl.EmailSenderServiceImpl;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Check every day if there are employees whose passport expires in a month if there is,
 * sent a message to workers in Human Capital Management with data from that employees
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public EmailSenderServiceImpl emailSenderService;

    @Autowired
    private EmployeePassportValidityConfiguration employeePassportValidityConfiguration;

    /**
     * Create a JdbcTemplate object using a configured dataSource, create a ClientRowMapper object implementing
     * RowMapper interface and use JdbcTemplate object methods to make database operations while using ClientMapper
     * object
     *
     * @return dataSource reader for Client data
     */
    @Bean
    public JdbcCursorItemReader<Person> reader() {
        JdbcCursorItemReader<Person> cursorItemReader = new JdbcCursorItemReader<>();
        cursorItemReader.setDataSource(dataSource);
        cursorItemReader.setSql("select id,given_Name,family_Name,date_Of_Expiry_Passport," +
                "social_Number from Person ");
        cursorItemReader.setRowMapper(new ClientRowMapper());
        return cursorItemReader;
    }

    /**
     * Insert business logic on every client read from database before call passportWriter method
     *
     * @return list od clients
     */
    @Bean
    public PassportItemProcessor passportProcessor() {
        return new PassportItemProcessor(employeePassportValidityConfiguration);
    }

    /**
     * Get a list of clients that has a passport that will not be valid after some period of time and send
     * an email message to notify for that
     *
     * @return ItemWriter<Client>
     */
    @Bean
    public ItemWriter<Person> passportWriter() {
        return clientsListWithAnAlmostExpiredPassport -> emailSenderService
                .sendNotificationMessageForInvalidPassport(clientsListWithAnAlmostExpiredPassport);
    }


    /**
     * Step that calls reader, passportProcessor and passportWriter method to notify for an invalid passport
     *
     * @return Step
     */
    @Bean
    public Step validatePassportStep() {
        return stepBuilderFactory.get("validatePassportStep")
                .<Person, Person>chunk(100)
                .reader(reader())
                .processor(passportProcessor())
                .writer(passportWriter())
                .build();
    }

    /**
     * Implementing a Job that execute step
     *
     * @return Job
     */
    @Bean
    public Job exportClientJob() {
        return jobBuilderFactory.get("exportClientJob")
                .incrementer(new RunIdIncrementer())
                .flow(validatePassportStep())
                .end()
                .build();
    }
}
