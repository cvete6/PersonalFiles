package com.example.demo;

import com.example.demo.Configuration.EmailPropertiesConfiguration;
import com.example.demo.Configuration.EmployeePassportValidityConfiguration;
import com.example.demo.PDFForm.GeneratePdf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;

@SpringBootApplication
@EnableConfigurationProperties({EmailPropertiesConfiguration.class, EmployeePassportValidityConfiguration.class})
public class PersonalFilesApplication {

    public static void main(String[] args) {

        SpringApplication.run(PersonalFilesApplication.class, args);

        try {
            GeneratePdf.generatePDF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
