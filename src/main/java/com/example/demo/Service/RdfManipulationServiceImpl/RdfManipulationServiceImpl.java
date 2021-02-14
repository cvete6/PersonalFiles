package com.example.demo.Service.RdfManipulationServiceImpl;

import com.example.demo.DomainModel.Organization;
import com.example.demo.DomainModel.Person;
import com.example.demo.Repository.PersonJpaRepository;
import com.example.demo.Service.RdfManipulationService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.*;



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
        Resource o = model.createResource(object);

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
    public void createRdfFromPersonProfile(Person person) throws IOException {

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
       addStatement(personSubject, personSchema+ "birthPlace",personSchema+person.getBirthPlace());

        if(person.getBirthDate() != null){
           String birthDateString = dateFormat.format(person.getBirthDate());
           addStatement(personSubject, personSchema+ "birthDate",personSchema+birthDateString);
       }

       if(person.getDeathDate() != null){
           String deathDateString = dateFormat.format(person.getDeathDate());
           addStatement(personSubject, personSchema+ "deathDate",personSchema+deathDateString);
       }

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

       if(person.getDateOfIssuePassport() != null){
           String dateOfIssuePassportInString = dateFormat.format(person.getDateOfIssuePassport());
           addStatement(personSubject, personSchema+ "dateOfIssuePassport",personSchema+dateOfIssuePassportInString);
       }

       if(person.getDateOfExpiryPassport() != null){
           String dateOfExpiryPassportString = dateFormat.format(person.getDateOfExpiryPassport());
           addStatement(personSubject, personSchema+ "dateOfExpiryPassport",personSchema+dateOfExpiryPassportString);
       }

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
    }

    @Override
    public byte[] createRdfFileInRDFXMLFormat(Person person) throws IOException {
        String localPath = "C:\\Users\\Cvete\\Documents\\DIPLOMSKA\\PersonalFiles\\profiles\\";
        createRdfFromPersonProfile(person);
        //Add unique name for all profile
        int length = 10;
        boolean useNumbers = false;
        boolean useLetters = true;
        String fileName = RandomStringUtils.random(length, useLetters, useNumbers);
        FileWriter out = new FileWriter(localPath + fileName+".xml");
        try {
            model.write(out, "RDF/XML");
        } finally {
            try {
                out.close();
            } catch (IOException closeException) {
            }
        }
        InputStream in =  FileManager.get().open(localPath);
        byte[] data = Files.readAllBytes(Paths.get(localPath + fileName + ".xml"));
        return data;
    }

    @Override
    public byte[] createRdfFileInNTriplesFormat(Person person) throws IOException {
        String localPath = "C:\\Users\\Cvete\\Documents\\DIPLOMSKA\\PersonalFiles\\profiles\\";
        createRdfFromPersonProfile(person);
        //Add unique name for all profile
        int length = 10;
        boolean useNumbers = false;
        boolean useLetters = true;
        String fileName = RandomStringUtils.random(length, useLetters, useNumbers);
        FileWriter out = new FileWriter(localPath + fileName+".rdf");
        try {
            model.write(out, "N-TRIPLES");
        } finally {
            try {
                out.close();
            } catch (IOException closeException) {
            }
        }
        InputStream in =  FileManager.get().open(localPath);
        byte[] data = Files.readAllBytes(Paths.get(localPath + fileName + ".rdf"));
        return data;
    }

    @Override
    public byte[] createRdfFileInTURTLEFormat(Person person) throws IOException {
        String localPath = "C:\\Users\\Cvete\\Documents\\DIPLOMSKA\\PersonalFiles\\profiles\\";

        createRdfFromPersonProfile(person);

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

        //model read from ttl file
        Person person = mapPersonFromRDFFile(model,uploadedRDFFile.getAbsolutePath());

        return person;
    }

    private Person mapPersonFromRDFFile(Model model, String path) throws ParseException {
        String personSchema = "http://schema.org/Person#";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Person person = new Person();

      //  Model m = FileManager.get().loadModel(path);
        //Property socialNumberProperty = model.getProperty(personSchema+"socialNumber");
        //Property givenNameProperty = model.getProperty(personSchema+"givenName");

     /*   Property socialNumberPropertyM = m.getProperty(personSchema+"socialNumber");
        Property givenNamePropertyM = m.getProperty(personSchema+"givenName");*/

   /*     ResIterator iter = model.listSubjectsWithProperty(givenNamePropertyM);
        Resource r = iter.nextResource();
        String socNum = r.getProperty(socialNumberPropertyM).getString();
        System.out.println("maIN NEW SOCNUM: " + socNum);*/

        //Proba//

        StmtIterator iter = model.listStatements();
        Resource personResource = null;
        while(iter.hasNext()){
            Statement stmt = iter.nextStatement();
            Resource subject = stmt.getSubject();
            Property predicate = stmt.getPredicate();
            RDFNode object = stmt.getObject();

            System.out.print("SUBEJCT: "+subject.toString());
            System.out.print(" " + predicate.toString() + " " );
            if(object instanceof Resource) {
                System.out.print(object.toString());
            }
            else {
                //object as a literal
                System.out.print(" \" " + object.toString() +" \"");
            }

            System.out.println(" .");
            if(subject.toString().equals(object.toString())){
                System.out.println("IME NA SUBJECTO GLAVEN NA RDF FILE: "+subject.toString() + " object imeee:"+ object.toString());
                personResource = subject;
            }

        }

        Property socialNumberProperty = model.getProperty(personSchema+"socialNumber");
        String uploadRdfSocialNumber = personResource.getProperty(socialNumberProperty).getResource().toString();
        String[] socialNumber = uploadRdfSocialNumber.split("#");
        if(socialNumber.length != 1 && !socialNumber[1].equals("null")){
            System.out.println("social number main man: "+socialNumber[1]);
            String sn = socialNumber[1];
            person.setSocialNumber(sn);
        }

        Property givenNameProperty = model.getProperty(personSchema+"givenName");
        String uploadRdfGivenName = personResource.getProperty(givenNameProperty).getResource().toString();
        String[] givenName = uploadRdfGivenName.split("#");
        if(givenName.length != 1 && !givenName[1].equals("null")){
            String gn = givenName[1];
            person.setGivenName(gn);
        }

        Property familyNameProperty = model.getProperty(personSchema+"familyName");
        String uploadRdffamilyName = personResource.getProperty(familyNameProperty).getResource().toString();
        String[] familyName = uploadRdffamilyName.split("#");
        if(familyName.length != 1 && !familyName[1].equals("null") ){
            String fn = familyName[1];
            person.setFamilyName(fn);
        }

        Property givenAdditionalNameProperty = model.getProperty(personSchema+"additionalName");
        NodeIterator iterAdditionalName = model.listObjectsOfProperty(givenAdditionalNameProperty);
        String uploadRdfadditionalName = iterAdditionalName.nextNode().toString();
        String[] additionalName = uploadRdfadditionalName.split("#");
        if(additionalName.length != 1 && !additionalName[1].equals("null")){
            String an = additionalName[1];
            person.setAdditionalName(an);
        }

        Property givenAwardProperty = model.getProperty(personSchema+"award");
        NodeIterator iterAward = model.listObjectsOfProperty(givenAwardProperty);
        String uploadRdfAward = iterAward.nextNode().toString();
        String[] award = uploadRdfAward.split("#");
        if(award.length != 1 && !award[1].equals("null")){
            String awardValue = award[1];
            person.setAward(awardValue);
        }

        Property givenCallSignProperty = model.getProperty(personSchema+"callSign");
        NodeIterator iterCallSign = model.listObjectsOfProperty(givenCallSignProperty);
        String uploadRdfCallSign = iterCallSign.nextNode().toString();
        String[] callSign = uploadRdfAward.split("#");
        if(callSign.length != 1 && !callSign[1].equals("null")){
            String callSignValue = callSign[1];
            person.setCallSign(callSignValue);
        }

        Property addressProperty = model.getProperty(personSchema+"address");
        NodeIterator iteraddress = model.listObjectsOfProperty(addressProperty);
        String uploadRdfaddress = iteraddress.nextNode().toString();
        String[] address = uploadRdfaddress.split("#");
        if(address.length != 1 && !address[1].equals("null")){
            String a = address[1];
            person.setAddress(a);
        }

        Property givenContactPointProperty = model.getProperty(personSchema+"contactPoint");
        NodeIterator iterContactPoint = model.listObjectsOfProperty(givenContactPointProperty);
        String uploadRdfContactPoint = iterContactPoint.nextNode().toString();
        String[] contactPoint = uploadRdfContactPoint.split("#");
        if(contactPoint.length != 1 && !contactPoint[1].equals("null")){
            String contactPointValue = contactPoint[1];
            person.setContactPoint(contactPointValue);
        }

        Property birthDateProperty = model.getProperty(personSchema+"birthDate");
        NodeIterator iterbirthDate = model.listObjectsOfProperty(birthDateProperty);
        //this condition is if birthData do not exist like a property in rdf file that we upload
        if(iterbirthDate.hasNext()){
            String uploadRdfbirthDate = iterbirthDate.nextNode().toString();
            String[] birthDate = uploadRdfbirthDate.split("#");
            if(birthDate.length != 1 && !birthDate[1].equals("null")){
                String bd = birthDate[1];
                Date parseDateOfBirth = simpleDateFormat.parse(bd);
                person.setBirthDate(parseDateOfBirth);
            }
        }

        Property birthPlaceProperty = model.getProperty(personSchema+"birthPlace");
        NodeIterator iterbirthPlace = model.listObjectsOfProperty(birthPlaceProperty);
        String uploadRdfbirthPlace = iterbirthPlace.nextNode().toString();
        String[] birthPlace = uploadRdfbirthPlace.split("#");
        if(birthPlace.length != 1 && !birthPlace[1].equals("null")){
            String bp = birthPlace[1];
            person.setBirthPlace(bp);
        }

        Property emailProperty = model.getProperty(personSchema+"email");
        //NodeIterator iteremail = model.listObjectsOfProperty(emailProperty);
        String uploadRdfemail = personResource.getProperty(emailProperty).getResource().toString();
        String[] email = uploadRdfemail.split("#");
        if(email.length != 1 && !email[1].equals("null")){
            System.out.println("email main: "+ email[1]);
            String e = email[1];
            person.setEmail(e);
        }

        Property passportNumberProperty = model.getProperty(personSchema+"passportNumber");
        NodeIterator iterpassportNumber = model.listObjectsOfProperty(passportNumberProperty);
        String uploadRdfpassportNumber = iterpassportNumber.nextNode().toString();
        String[] passportNumber = uploadRdfpassportNumber.split("#");
        if(passportNumber.length != 1 && !passportNumber[1].equals("null")){
            String pn = passportNumber[1];
            person.setPassportNumber(pn);
        }

        Property dateOfIssuePassportProperty = model.getProperty(personSchema+"dateOfIssuePassport");
        NodeIterator iterdateOfIssuePassport = model.listObjectsOfProperty(dateOfIssuePassportProperty);
        if(iterdateOfIssuePassport.hasNext()){
            String uploadRdfdateOfIssuePassport = iterdateOfIssuePassport.nextNode().toString();
            String[] dateOfIssuePassport = uploadRdfdateOfIssuePassport.split("#");
            if(dateOfIssuePassport.length != 1 && !dateOfIssuePassport[1].equals("null")){
                String ip = dateOfIssuePassport[1];
                Date parsedateOfIssuePassport = simpleDateFormat.parse(ip);
                person.setDateOfIssuePassport(parsedateOfIssuePassport);
            }
        }

        Property dateOfExpiryPassportProperty = model.getProperty(personSchema+"dateOfExpiryPassport");
        NodeIterator iterdateOfExpiryPassport = model.listObjectsOfProperty(dateOfExpiryPassportProperty);
        if(iterdateOfExpiryPassport.hasNext()){
            String uploadRdfdateOfExpiryPassport = iterdateOfExpiryPassport.nextNode().toString();
            String[] dateOfExpiryPassport = uploadRdfdateOfExpiryPassport.split("#");
            if(dateOfExpiryPassport.length != 1 && !dateOfExpiryPassport[1].equals("null")){
                String ep = dateOfExpiryPassport[1];
                Date parsedateOfExpiryPassport = simpleDateFormat.parse(ep);
                person.setDateOfExpiryPassport(parsedateOfExpiryPassport);
            }
        }

        Property givenDeathPlaceProperty = model.getProperty(personSchema+"deathPlace");
        NodeIterator iterDeathPlace = model.listObjectsOfProperty(givenDeathPlaceProperty);
        String uploadRdfDeathPlace = iterDeathPlace.nextNode().toString();
        String[] deathPlace = uploadRdfDeathPlace.split("#");
        if(deathPlace.length != 1 && !deathPlace[1].equals("null")){
            String deathPlaceValue = deathPlace[1];
            person.setDeathPlace(deathPlaceValue);
        }

        Property deathDateProperty = model.getProperty(personSchema+"deathDate");
        NodeIterator iterDeathDate = model.listObjectsOfProperty(deathDateProperty);
        if(iterDeathDate.hasNext()){
            String uploadRdfDeathDate = iterDeathDate.nextNode().toString();
            String[] deathDate = uploadRdfDeathDate.split("#");
            if(deathDate.length != 1 && !deathDate[1].equals("null")){
                String dd = deathDate[1];
                Date parseDeathDate = simpleDateFormat.parse(dd);
                person.setDeathDate(parseDeathDate);
            }
        }

        Property givenFaxNumberProperty = model.getProperty(personSchema+"faxNumber");
        NodeIterator iterFaxNumber = model.listObjectsOfProperty(givenFaxNumberProperty);
        String uploadRdfFaxNumber = iterFaxNumber.nextNode().toString();
        String[] faxNumber = uploadRdfFaxNumber.split("#");
        if(faxNumber.length != 1 && !faxNumber[1].equals("null")){
            String faxNumbereValue = faxNumber[1];
            person.setFaxNumber(faxNumbereValue);
        }

        Property givenGenderProperty = model.getProperty(personSchema+"gender");
        NodeIterator iterGender = model.listObjectsOfProperty(givenGenderProperty);
        String uploadRdfGender = iterGender.nextNode().toString();
        String[] gender = uploadRdfGender.split("#");
        if(gender.length != 1 && !gender[1].equals("null")){
            String genderValue = gender[1];
            person.setGender(genderValue);
        }

        Property givenGlobalLocationNumberProperty = model.getProperty(personSchema+"globalLocationNumber");
        NodeIterator iterGlobalLocationNumber = model.listObjectsOfProperty(givenGlobalLocationNumberProperty);
        String uploadRdfGlobalLocationNumber = iterGlobalLocationNumber.nextNode().toString();
        String[] globalLocationNumber = uploadRdfGlobalLocationNumber.split("#");
        if(globalLocationNumber.length != 1 && !globalLocationNumber[1].equals("null")){
            String globalLocationNumberValue = globalLocationNumber[1];
            person.setGlobalLocationNumber(globalLocationNumberValue);
        }

        Property givenHeightProperty = model.getProperty(personSchema+"height");
        NodeIterator iterHeight = model.listObjectsOfProperty(givenHeightProperty);
        String uploadRdfHeight = iterHeight.nextNode().toString();
        String[] height = uploadRdfHeight.split("#");
        if(height.length != 1 && !height[1].equals("null")){
            Integer heightValue = Integer.valueOf(height[1]);
            person.setHeight(heightValue);
        }

        Property givenWeightProperty = model.getProperty(personSchema+"weight");
        NodeIterator iterWeight = model.listObjectsOfProperty(givenWeightProperty);
        String uploadRdfWeight = iterWeight.nextNode().toString();
        String[] weight = uploadRdfWeight.split("#");
        if(weight.length != 1 && !weight[1].equals("null")){
            Integer weightValue = Integer.valueOf(weight[1]);
            person.setWeight(weightValue);
        }

        Property givenHomeLocationProperty = model.getProperty(personSchema+"homeLocation");
        NodeIterator iterHomeLocation = model.listObjectsOfProperty(givenHomeLocationProperty);
        String uploadRdfHomeLocation = iterHomeLocation.nextNode().toString();
        String[] homeLocation = uploadRdfHomeLocation.split("#");
        if(homeLocation.length != 1 && !homeLocation[1].equals("null")){
            String homeLocationValue = homeLocation[1];
            person.setHomeLocation(homeLocationValue);
        }

        Property givenHonorificPrefixProperty = model.getProperty(personSchema+"honorifixPrefix");
        NodeIterator iterHonorificPrefix = model.listObjectsOfProperty(givenHonorificPrefixProperty);
        String uploadRdfHonorificPrefix = iterHonorificPrefix.nextNode().toString();
        String[] honorificPrefix = uploadRdfHonorificPrefix.split("#");
        if(honorificPrefix.length != 1 && !honorificPrefix[1].equals("null")){
            String honorificPrefixValue = honorificPrefix[1];
            person.setHonorificPrefix(honorificPrefixValue);
        }

        Property givenHonorificSuffixProperty = model.getProperty(personSchema+"honorifixSuffix");
        NodeIterator iterHonorificSuffix = model.listObjectsOfProperty(givenHonorificSuffixProperty);
        String uploadRdfHonorificSuffix = iterHonorificSuffix.nextNode().toString();
        String[] honorificSuffix = uploadRdfHonorificSuffix.split("#");
        if(honorificSuffix.length != 1 && !honorificSuffix[1].equals("null")){
            String honorificSuffixValue = honorificSuffix[1];
            person.setHonorificSuffix(honorificSuffixValue);
        }

        Property givenJobTitleProperty = model.getProperty(personSchema+"jobTitle");
        NodeIterator iterJobTitle = model.listObjectsOfProperty(givenJobTitleProperty);
        String uploadRdfJobTitle = iterJobTitle.nextNode().toString();
        String[] jobTitle = uploadRdfJobTitle.split("#");
        if(jobTitle.length != 1 && !jobTitle[1].equals("null")){
            String jobTitleValue = homeLocation[1];
            person.setJobTitle(jobTitleValue);
        }

        Property givenKnowsAboutProperty = model.getProperty(personSchema+"knowsAbout");
        NodeIterator iterKnowsAbout = model.listObjectsOfProperty(givenKnowsAboutProperty);
        String uploadRdfKnowsAbout = iterKnowsAbout.nextNode().toString();
        String[] knowsAbout = uploadRdfKnowsAbout.split("#");
        if(knowsAbout.length != 1 && !knowsAbout[1].equals("null")){
            String knowsAboutValue = knowsAbout[1];
            person.setKnowsAbout(knowsAboutValue);
        }

        Property givenKnowsLanguageProperty = model.getProperty(personSchema+"knowsLanguage");
        NodeIterator iterKnowsLanguage = model.listObjectsOfProperty(givenKnowsLanguageProperty);
        String uploadRdfKnowsLanguage = iterKnowsLanguage.nextNode().toString();
        String[] knowsLanguage = uploadRdfKnowsLanguage.split("#");
        if(knowsLanguage.length != 1 && !knowsLanguage[1].equals("null")){
            String knowsLanguageValue = knowsLanguage[1];
            person.setKnowsLanguage(knowsLanguageValue);
        }

        Property givenNationalityProperty = model.getProperty(personSchema+"nationality");
        NodeIterator iterNationality = model.listObjectsOfProperty(givenNationalityProperty);
        String uploadRdfNationality = iterNationality.nextNode().toString();
        String[] Nationality = uploadRdfNationality.split("#");
        if(Nationality.length != 1 && !Nationality[1].equals("null")){
            String NationalityValue = Nationality[1];
            person.setNationality(NationalityValue);
        }

        Property givenPerformInProperty = model.getProperty(personSchema+"performerIn");
        NodeIterator iterPerformIn = model.listObjectsOfProperty(givenPerformInProperty);
        String uploadRdfPerformIn = iterPerformIn.nextNode().toString();
        String[] PerformIn = uploadRdfPerformIn.split("#");
        if(PerformIn.length != 1 && !PerformIn[1].equals("null")){
            String PerformInValue = PerformIn[1];
            person.setPerformerIn(PerformInValue);
        }

        Property givenPublishingPrinciplesProperty = model.getProperty(personSchema+"publishingPrinciples");
        NodeIterator iterPublishingPrinciples = model.listObjectsOfProperty(givenPublishingPrinciplesProperty);
        String uploadRdfPublishingPrinciples = iterPublishingPrinciples.nextNode().toString();
        String[] PublishingPrinciples = uploadRdfPublishingPrinciples.split("#");
        if(PublishingPrinciples.length != 1 &&!PublishingPrinciples[1].equals("null")){
            String PublishingPrinciplesValue = PublishingPrinciples[1];
            person.setPublishingPrinciples(PublishingPrinciplesValue);
        }

        Property givenSeekProperty = model.getProperty(personSchema+"seek");
        NodeIterator iterSeek = model.listObjectsOfProperty(givenSeekProperty);
        String uploadRdfSeek = iterSeek.nextNode().toString();
        String[] Seek = uploadRdfSeek.split("#");
        if(Seek.length != 1 && !Seek[1].equals("null")){
            String SeekValue = Seek[1];
            person.setSeeks(SeekValue);
        }

        Property givenTaxIDProperty = model.getProperty(personSchema+"taxID");
        NodeIterator iterTaxID = model.listObjectsOfProperty(givenTaxIDProperty);
        String uploadRdfTaxID = iterTaxID.nextNode().toString();
        String[] TaxID = uploadRdfTaxID.split("#");
        if(TaxID.length != 1 && !TaxID[1].equals("null")){
            String TaxIDValue = TaxID[1];
            person.setTaxID(TaxIDValue);
        }

        Property givenTelephoneProperty = model.getProperty(personSchema+"telephone");
        NodeIterator iterTelephone = model.listObjectsOfProperty(givenTelephoneProperty);
        String uploadRdfTelephone = iterTelephone.nextNode().toString();
        String[] Telephone = uploadRdfTelephone.split("#");
        if(Telephone.length != 1 && !Telephone[1].equals("null")){
            String TelephoneValue = Telephone[1];
            person.setTelephone(TelephoneValue);
        }

        Property givenWorkLocationProperty = model.getProperty(personSchema+"workLocation");
        NodeIterator iterWorkLocation = model.listObjectsOfProperty(givenWorkLocationProperty);
        String uploadRdfWorkLocation = iterWorkLocation.nextNode().toString();
        String[] WorkLocation = uploadRdfWorkLocation.split("#");
        if(WorkLocation.length != 1 && !WorkLocation[1].equals("null")){
            String WorkLocationValue = WorkLocation[1];
            person.setWorkLocation(WorkLocationValue);
        }

        Property childrenProperty = model.getProperty(personSchema+"children");
        List<Person> children = addConnectedPerson(model,childrenProperty);
        if(!children.isEmpty()){
            person.setChildren(children);
        }

        Property spouseProperty = model.getProperty(personSchema+"spouse");
        List<Person> spouse = addConnectedPerson(model, spouseProperty);
        if (!spouse.isEmpty()){
            person.setSpouse(spouse.get(0));
        }

        Property colleagueProperty = model.getProperty(personSchema+"colleague");
        List<Person> colleague = addConnectedPerson(model,colleagueProperty);
        if(!colleague.isEmpty()){
            person.setColleague(colleague);
        }

        Property parentProperty = model.getProperty(personSchema+"parent");
        List<Person> parent = addConnectedPerson(model,parentProperty);
        if(!parent.isEmpty()){
            person.setParent(parent);
        }



        //!!! NE E DOBRO RESENO VIDI PAK
        Property followsProperty = model.getProperty(personSchema+"follows");
        List<Person> follows = addConnectedPerson(model,followsProperty);
        if(!follows.isEmpty()){
            person.setFollows(follows);
            //Add this main person to be followed from person follows (make bidirectional relationship)
            for(Person f : follows){
                List<Person> fFollowers = f.getFollows();
                    if(!fFollowers.contains(person)){
                        fFollowers.add(person);
                    }
                f.setFollows(fFollowers);
                personJpaRepository.save(f);
            }
        }

        return person;

    }

    /**
     * For specific properties for main person in rdf file add children, spouse, colleague... (add other person = other person in object from statement)
     *
     * @param model rdf model
     * @param property resource has property that have object like a new resource
     * @return list od persons that is added to that property frm main person
     */

    private List<Person> addConnectedPerson(Model model, Property property ){
        NodeIterator iterator = model.listObjectsOfProperty(property);
        List<Person> persons = new ArrayList<>();
        while(iterator.hasNext()){
            Resource resource = (Resource) iterator.nextNode();
            Property socialNumberProperty = model.createProperty(personSchema+"socialNumber");
            Property emailProperty = model.createProperty(personSchema+"email");
            Property givenNameProperty = model.createProperty(personSchema+"givenName");
            Property familyNameProperty = model.createProperty(personSchema+"familyName");

            //Child Social Number
            String givenSocialNumber = String.valueOf(resource.getProperty(socialNumberProperty).getObject());
            String [] socialNumber = givenSocialNumber.split("#");
            String socialNum = socialNumber[1];

            Optional<Person> existPersonChild = personJpaRepository.findBySocialNumber(socialNum);

            if(existPersonChild.isPresent()){
                //if person already exist in database add this child as child to main person
                persons.add(existPersonChild.get());
            }
            else{
                Person unExistPerson = new Person();
                unExistPerson.setSocialNumber(socialNumber[1]);
                //GivenName
                String givenName = String.valueOf(resource.getProperty(givenNameProperty).getObject());
                String[] name = givenName.split("#");
                unExistPerson.setGivenName(name[1]);

                //Email
                String givenEmail = String.valueOf(resource.getProperty(emailProperty).getObject());
                String[] email = givenEmail.split("#");
                unExistPerson.setEmail(email[1]);

                //FamilyName
                String givenFamilyName = String.valueOf(resource.getProperty(familyNameProperty).getObject());
                String[] familyName = givenFamilyName.split("#");
                unExistPerson.setFamilyName(familyName[1]);

                personJpaRepository.save(unExistPerson);
                persons.add(unExistPerson);
            }
        }
        return persons;
    }


}