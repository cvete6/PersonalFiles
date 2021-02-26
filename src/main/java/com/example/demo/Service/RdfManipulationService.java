package com.example.demo.Service;

import com.example.demo.DomainModel.Person;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public interface RdfManipulationService {

    void createRdfFromPersonProfile(Person person) throws IOException;

    byte[] createRdfFileInRDFXMLFormat(Person person) throws IOException;

    byte[] createRdfFileInTURTLEFormat(Person person) throws IOException;

    byte[] createRdfFileInNTriplesFormat(Person person) throws IOException;

    Person validateAndCreatePerson(MultipartFile uploadedMultipartRDFFile, String uploadFormat, org.springframework.ui.Model model) throws IOException, ParseException;

    File convertMultipartFileToFile(MultipartFile multipartPdfFile) throws IOException;

}
