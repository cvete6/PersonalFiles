package com.example.demo.Service.RdfManipulationServiceImpl;

import com.example.demo.DomainModel.Person;
import com.example.demo.Repository.PersonJpaRepository;
import com.example.demo.Service.RdfManipulationService;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Optional;

import static com.example.demo.DataMapper.RdfMapper.mapPersonFromRDFFile;


/**
 * Working with ttl rdf file
 */
@Service
public class RdfManipulationServiceImpl implements RdfManipulationService {

    private PersonJpaRepository personJpaRepository;

    public RdfManipulationServiceImpl(PersonJpaRepository personJpaRepository) {
        this.personJpaRepository = personJpaRepository;
    }

    private Model model;
    private String personSchema = "http://schema.org/Person#";


    public void addStatement (String subject, String property, String object){
        Resource s = model.createResource(subject);
        Property p = model.createProperty(property);
        RDFNode o = model.createResource(object);

        Statement statement = model.createStatement(s,p,o);
        model.add(statement);
    }

    /**
     *Create model from person data
     *
     * @param person that we want to download rdf file
     * @return content of rdf file in byte[]
     * @throws IOException
     */
    public byte[] createRdfFromPersonProfile(Person person) throws IOException {

       String localPath = "C:\\Users\\Cvete\\Documents\\DIPLOMSKA\\PersonalFiles\\profiles\\";
       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

       model = ModelFactory.createDefaultModel();
       String personSchema = "http://schema.org/Person#";

       addStatement(personSchema+person.getGivenName(), personSchema+ "givenName",personSchema+person.getGivenName());
       addStatement(personSchema+person.getGivenName(), personSchema+ "additionalName", personSchema+person.getAdditionalName());
       addStatement(personSchema+person.getGivenName(), personSchema+ "address",personSchema+person.getAddress());
       addStatement(personSchema+person.getGivenName(), personSchema+ "familyName",personSchema+person.getFamilyName());
       addStatement(personSchema+person.getGivenName(), personSchema+ "award",personSchema+person.getAward());
       addStatement(personSchema+person.getGivenName(), personSchema+ "socialNumber",personSchema+person.getSocialNumber());
       addStatement(personSchema+person.getGivenName(), personSchema+ "callSign",personSchema+person.getCallSign());
       addStatement(personSchema+person.getGivenName(), personSchema+ "contactPoint",personSchema+person.getContactPoint());
       String birthDateString = dateFormat.format(person.getBirthDate());
       addStatement(personSchema+person.getGivenName(), personSchema+ "birthDate",personSchema+birthDateString);
       addStatement(personSchema+person.getGivenName(), personSchema+ "birthPlace",personSchema+person.getBirthPlace());
       String deathDateString = dateFormat.format(person.getDeathDate());
       addStatement(personSchema+person.getGivenName(), personSchema+ "deathDate",personSchema+deathDateString);
       addStatement(personSchema+person.getGivenName(), personSchema+ "deathPlace",personSchema+person.getDeathPlace());
       addStatement(personSchema+person.getGivenName(), personSchema+ "email",personSchema+person.getEmail());
       addStatement(personSchema+person.getGivenName(), personSchema+ "faxNumber",personSchema+person.getFaxNumber());
       addStatement(personSchema+person.getGivenName(), personSchema+ "gender",personSchema+person.getGender());
       addStatement(personSchema+person.getGivenName(), personSchema+ "globalLocationNumber",personSchema+person.getGlobalLocationNumber());
       addStatement(personSchema+person.getGivenName(), personSchema+ "height",personSchema+person.getHeight());
       addStatement(personSchema+person.getGivenName(), personSchema+ "weight", personSchema+person.getWeight());
       addStatement(personSchema+person.getGivenName(), personSchema+ "homeLocation",personSchema+person.getHomeLocation());
       addStatement(personSchema+person.getGivenName(), personSchema+ "workLocation",personSchema+person.getWorkLocation());
       addStatement(personSchema+person.getGivenName(), personSchema+ "honorifixPrefix",personSchema+person.getHonorificPrefix());
       addStatement(personSchema+person.getGivenName(), personSchema+ "honorifixSuffix",personSchema+person.getHonorificSuffix());
       addStatement(personSchema+person.getGivenName(), personSchema+ "jobTitle",personSchema+person.getJobTitle());
       addStatement(personSchema+person.getGivenName(), personSchema+ "knowsAbout",personSchema+person.getKnowsAbout());
       addStatement(personSchema+person.getGivenName(), personSchema+ "knowsLanguage",personSchema+person.getKnowsLanguage());
       addStatement(personSchema+person.getGivenName(), personSchema+ "nationality",personSchema+person.getNationality());
       addStatement(personSchema+person.getGivenName(), personSchema+ "performerIn",personSchema+person.getPerformerIn());
       addStatement(personSchema+person.getGivenName(), personSchema+ "publishingPrinciples",personSchema+person.getPublishingPrinciples());
       addStatement(personSchema+person.getGivenName(), personSchema+ "seek",personSchema+person.getSeeks());
       addStatement(personSchema+person.getGivenName(), personSchema+ "taxID",personSchema+person.getTaxID());
       addStatement(personSchema+person.getGivenName(), personSchema+ "telephone",personSchema+person.getTelephone());
       addStatement(personSchema+person.getGivenName(), personSchema+ "passportNumber",personSchema+person.getPassportNumber());
       String dateOfIssuePassportInString = dateFormat.format(person.getDateOfIssuePassport());
       addStatement(personSchema+person.getGivenName(), personSchema+ "dateOfIssuePassport",personSchema+dateOfIssuePassportInString);
       String dateOfExpiryPassportString = dateFormat.format(person.getDateOfExpiryPassport());
       addStatement(personSchema+person.getGivenName(), personSchema+ "dateOfExpiryPassport",personSchema+dateOfExpiryPassportString);

       addStatement(personSchema+person.getGivenName(), personSchema+ "children", personSchema+person.getChildren());
       //DA SE DODADAT OVDE ZA SITE PRI KREIRAJNE NA RDF FILE

        //Add unique name for all profile
        int length = 10;
        boolean useNumbers = false;
        boolean useLetters = true;
        String fileName = RandomStringUtils.random(length, useLetters, useNumbers);
        FileWriter out = new FileWriter(localPath + fileName+".ttl");
        try {
            model.write(out, "TURTLE");
        } finally {
            try {
                out.close();
            } catch (IOException closeException) {
            }
        }
        InputStream in =  FileManager.get().open(localPath);
        byte[] data = Files.readAllBytes(Paths.get(localPath + fileName + ".ttl"));
        return data;

    }

    @Override
    public Person validateAndCreatePerson(MultipartFile uploadedMultipartRDFFile, org.springframework.ui.Model model) throws IOException, ParseException {
        Person person = createPersonFromRDFFile(uploadedMultipartRDFFile, model);
        return  person;
    }

    @Override
    public File convertMultipartFileToFile(MultipartFile multipartPdfFile) throws IOException {
        File file = new File(multipartPdfFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multipartPdfFile.getBytes());
        fos.close();
        return file;
    }

    private Person createPersonFromRDFFile(MultipartFile uploadedMultipartRDFFile, org.springframework.ui.Model m)
            throws IOException, ParseException {

        File uploadedRDFFile = convertMultipartFileToFile(uploadedMultipartRDFFile);
        InputStream in = FileManager.get().open(uploadedRDFFile.getAbsolutePath());
        Model model = ModelFactory.createDefaultModel();
        model.read(in,null,"TURTLE");
        //model.write(System.out,"TURTLE");

        Person person = mapPersonFromRDFFile(model);

        return person;
    }

















/*
// OVDE IMAME KAKO SE CITA OD FILE SO TTL NASTAVKA PODATOCITE KAKO MODEL
/*        // create an empty model
        Model model = ModelFactory.createDefaultModel();

        String path = "src/main/java/com/example/personalassistant/Service/RdfManipulationServiceImpl/foaf.ttl";
        InputStream in =  FileManager.get().open(path);
        model.read(in,null,"TURTLE");

        //  Resource jas = model.getResource("http://www.facebook.com/cvete.trajkovska");
        // System.out.println(jas.getRequiredProperty(VCARD.FN).getObject().toString());*/

 //SE PECATI SPORED SVOJSTVO KOE SVOJSTVO MI TREBA TO GO PECATAM A GO PREBARUVAM NIZ PROCITANIOT TTL FILE
/*        ResIterator iter = model.listResourcesWithProperty(FOAF.family_name);
        ArrayList<String> lista = new ArrayList<>();
        while(iter.hasNext()){
            lista.add(iter.nextResource().getRequiredProperty(FOAF.family_name).toString());
        }
        for(int i=0;i<lista.size();i++){
            System.out.println(lista.get(i));

            ArrayList<String> subList = new ArrayList<>();
            subList.add(String.valueOf(lista.get(i).split(", ")));
            for(int j=0;j<subList.size();j++){
                System.out.println(subList.get(j));
            }
        }

        System.out.println("Turtles:");
        //model.write(System.out,"TURTLE");*/

}