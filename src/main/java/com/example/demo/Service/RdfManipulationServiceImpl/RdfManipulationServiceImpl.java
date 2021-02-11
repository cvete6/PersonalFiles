package com.example.demo.Service.RdfManipulationServiceImpl;

import com.example.demo.DomainModel.Organization;
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
import java.util.Collections;
import java.util.List;
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

    /**
     * Add statement with (subject - main person, property, object-literal)
     *
     * @param subject person
     * @param property person characteristics
     * @param object value of that characteristics-literal
     */
    public void addStatement (String subject, String property, String object){
        Resource s = model.createResource(subject);
        Property p = model.createProperty(property);
        RDFNode o = model.createResource(object);

        Statement statement = model.createStatement(s,p,o);
        model.add(statement);
    }

    /**
     * Create statement in model where object from statement is new resource (new connected person) eg. person has child
     * child is connected person
     *
     * @param personSubject main person
     * @param personProperty property that has object like a resource
     * @param connectPerson new resourced
     */
    public void addComplexStatement (Resource personSubject, Property personProperty, Person connectPerson){

        Property propertyGivenName = model.createProperty(personSchema+"givenName");
        RDFNode objectGivenName = model.createResource(personSchema+connectPerson.getGivenName());

        Property propertyFamilyName = model.createProperty(personSchema+"familyName");
        RDFNode objectFamilyName = model.createResource(personSchema+connectPerson.getFamilyName());

        Property propertySocialNumber = model.createProperty(personSchema+"socialNumber");
        RDFNode objectSocialNumber = model.createResource(personSchema+connectPerson.getSocialNumber());

        Property propertyEmail = model.createProperty(personSchema+"email");
        RDFNode objectEmail = model.createResource(personSchema+connectPerson.getEmail());

        Statement statement = model.createStatement(personSubject,personProperty, model.createResource( )
                .addProperty(propertyGivenName,objectGivenName)
                .addProperty(propertyFamilyName,objectFamilyName)
                .addProperty(propertySocialNumber,objectSocialNumber)
                .addProperty(propertyEmail,objectEmail)
        );
        model.add(statement);
    }

    /**
     * Create statement in model where object from statement is new resource (new connected organization) eg. person is sponsor to some organization
     *
     * @param personSubject main person
     * @param personProperty property that has object like a resource
     * @param connectOrganization new resourced
     */
    public void addComplexStatementOrganization (Resource personSubject, Property personProperty, Organization connectOrganization){

        Property propertyGivenName = model.createProperty(personSchema + "legalName");
        RDFNode objectGivenName = model.createResource(personSchema + connectOrganization.getLegalName());

        Property propertyAddress = model.createProperty(personSchema + "address");
        RDFNode objectAddress = model.createResource(personSchema + connectOrganization.getAddress());

        Property propertyEmail = model.createProperty(personSchema + "email");
        RDFNode objectEmail = model.createResource(personSchema + connectOrganization.getEmail());

        Statement statement = model.createStatement(personSubject,personProperty, model.createResource( )
                .addProperty(propertyGivenName, objectGivenName)
                .addProperty(propertyAddress, objectAddress)
                .addProperty(propertyEmail, objectEmail)
        );
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
       DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

       model = ModelFactory.createDefaultModel();
       String personSchema = "http://schema.org/Person#";
       String personSubject =personSchema+person.getGivenName();
       Resource resourcePerson = model.createResource(personSchema+person.getGivenName());

       addStatement(personSubject, personSchema+ "givenName",personSchema+person.getGivenName());
       addStatement(personSubject, personSchema+ "additionalName", personSchema+person.getAdditionalName());
       addStatement(personSubject, personSchema+ "address",personSchema+person.getAddress());
       addStatement(personSubject, personSchema+ "familyName",personSchema+person.getFamilyName());
       addStatement(personSubject, personSchema+ "award",personSchema+person.getAward());
       addStatement(personSubject, personSchema+ "socialNumber",personSchema+person.getSocialNumber());
       addStatement(personSubject, personSchema+ "callSign",personSchema+person.getCallSign());
       addStatement(personSubject, personSchema+ "contactPoint",personSchema+person.getContactPoint());
       String birthDateString = null;
       if(person.getBirthDate()!=null){
           dateFormat.format(person.getBirthDate());
       }
       addStatement(personSubject, personSchema+ "birthDate",personSchema+birthDateString);
       addStatement(personSubject, personSchema+ "birthPlace",personSchema+person.getBirthPlace());
       String deathDateString = null;
       if(person.getDeathDate()!=null){
           deathDateString = dateFormat.format(person.getDeathDate());
       }
       addStatement(personSubject, personSchema+ "deathDate",personSchema+deathDateString);
       addStatement(personSubject, personSchema+ "deathPlace",personSchema+person.getDeathPlace());
       addStatement(personSubject, personSchema+ "email",personSchema+person.getEmail());
       addStatement(personSubject, personSchema+ "faxNumber",personSchema+person.getFaxNumber());
       addStatement(personSubject, personSchema+ "gender",personSchema+person.getGender());
       addStatement(personSubject, personSchema+ "globalLocationNumber",personSchema+person.getGlobalLocationNumber());
       addStatement(personSubject, personSchema+ "height",personSchema+person.getHeight());
       addStatement(personSubject, personSchema+ "weight", personSchema+person.getWeight());
       addStatement(personSubject, personSchema+ "homeLocation",personSchema+person.getHomeLocation());
       addStatement(personSubject, personSchema+ "workLocation",personSchema+person.getWorkLocation());
       addStatement(personSubject, personSchema+ "honorifixPrefix",personSchema+person.getHonorificPrefix());
       addStatement(personSubject, personSchema+ "honorifixSuffix",personSchema+person.getHonorificSuffix());
       addStatement(personSubject, personSchema+ "jobTitle",personSchema+person.getJobTitle());
       addStatement(personSubject, personSchema+ "knowsAbout",personSchema+person.getKnowsAbout());
       addStatement(personSubject, personSchema+ "knowsLanguage",personSchema+person.getKnowsLanguage());
       addStatement(personSubject, personSchema+ "nationality",personSchema+person.getNationality());
       addStatement(personSubject, personSchema+ "performerIn",personSchema+person.getPerformerIn());
       addStatement(personSubject, personSchema+ "publishingPrinciples",personSchema+person.getPublishingPrinciples());
       addStatement(personSubject, personSchema+ "seek",personSchema+person.getSeeks());
       addStatement(personSubject, personSchema+ "taxID",personSchema+person.getTaxID());
       addStatement(personSubject, personSchema+ "telephone",personSchema+person.getTelephone());
       addStatement(personSubject, personSchema+ "passportNumber",personSchema+person.getPassportNumber());
       String dateOfIssuePassportInString = null;
       if(person.getDateOfIssuePassport()!=null){
           dateFormat.format(person.getDateOfIssuePassport());
       }
       addStatement(personSubject, personSchema+ "dateOfIssuePassport",personSchema+dateOfIssuePassportInString);
       String dateOfExpiryPassportString = null;
       if(person.getDateOfExpiryPassport()!=null){
           dateFormat.format(person.getDateOfExpiryPassport());
       }
       addStatement(personSubject, personSchema+ "dateOfExpiryPassport",personSchema+dateOfExpiryPassportString);


        //Add children property
        List<Person> children = person.getChildren();
        if(!children.isEmpty()){
            Property propertyChildPerson = model.createProperty(personSchema + "children");
            children.forEach(child -> {
                addComplexStatement(resourcePerson, propertyChildPerson, child);
            });
        }

        //Add colleague property
        List<Person> colleagues = person.getColleague();
        if(!colleagues.isEmpty()){
            Property propertyPerson = model.createProperty(personSchema + "colleague");
            colleagues.forEach(colleague ->{
                addComplexStatement(resourcePerson, propertyPerson, colleague );
            });
        }

        //Add parent property
        List<Person> parents = person.getParent();
        if(!parents.isEmpty()){
            Property propertyPerson = model.createProperty(personSchema + "parent");
            parents.forEach(parent ->{
                addComplexStatement(resourcePerson, propertyPerson, parent);
            });
        }

        //Add spouse property
        Person spouse = person.getSpouse();
        if(spouse!=null){
            Property propertySpousePerson = model.createProperty(personSchema + "spouse");
            addComplexStatement(resourcePerson, propertySpousePerson, person.getSpouse());
        }

        //Add follows property
        List<Person> follows = person.getFollows();
        if(!follows.isEmpty()){
            Property propertyPerson = model.createProperty(personSchema + "follows");
            follows.forEach(follow ->{
                addComplexStatement(resourcePerson, propertyPerson, follow);
            });
        }

        //Add follows property
        List<Person> knows = person.getFollows();
        if(!knows.isEmpty()){
            Property propertyPerson = model.createProperty(personSchema + "knows");
            knows.forEach(know ->{
                addComplexStatement(resourcePerson, propertyPerson, know);
            });
        }

        //Add organization sponsor
        Organization sponsorToOrganization = person.getOrganization_sponsor();
        if(sponsorToOrganization!=null){
            Property propertyPerson = model.createProperty(personSchema + "sponsor");
            addComplexStatementOrganization(resourcePerson, propertyPerson, sponsorToOrganization);
        }

        //Add works for organization
        Organization worksForOrganization = person.getOrganization_sponsor();
        if(worksForOrganization!=null){
            Property propertyPerson = model.createProperty(personSchema + "worksFor");
            addComplexStatementOrganization(resourcePerson, propertyPerson, worksForOrganization);
        }

        //Add memberOf organization property
        List<Organization> membersOf = person.getMemberOf();
        if(!membersOf.isEmpty()){
            Property propertyPerson = model.createProperty(personSchema + "memberOf");
            membersOf.forEach(memberOf ->{
                addComplexStatementOrganization(resourcePerson, propertyPerson, memberOf);
            });
        }

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
        return  createPersonFromRDFFile(uploadedMultipartRDFFile, model);
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