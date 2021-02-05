package com.example.demo.Service;

import com.example.demo.DomainModel.Person;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public interface RdfManipulationService {

    byte[] createRdfFromPersonProfile(Person person) throws IOException;

    Person validateAndCreatePerson(MultipartFile uploadedMultipartRDFFile, org.springframework.ui.Model model) throws IOException, ParseException;

    File convertMultipartFileToFile(MultipartFile multipartPdfFile) throws IOException;

    //String redirectToPersonDetailsView(Person person, org.springframework.ui.Model model);
    }
