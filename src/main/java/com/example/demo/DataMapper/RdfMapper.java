package com.example.demo.DataMapper;

import com.example.demo.DomainModel.Person;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RdfMapper {

    public static Person mapPersonFromRDFFile(Model model) throws ParseException {
        String personSchema = "http://schema.org/Person#";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Person person = new Person();

        Property socialNumberProperty = model.getProperty(personSchema+"socialNumber");
        NodeIterator iterSocialNumber = model.listObjectsOfProperty(socialNumberProperty);
        String uploadRdfSocialNumber = iterSocialNumber.nextNode().toString();
        String[] socialNumber = uploadRdfSocialNumber.split("#");
        String sn = socialNumber[1];
        person.setSocialNumber(sn);

        Property givenNameProperty = model.getProperty(personSchema+"givenName");
        NodeIterator itergivenName = model.listObjectsOfProperty(givenNameProperty);
        String uploadRdfgivenName = itergivenName.nextNode().toString();
        String[] givenName = uploadRdfgivenName.split("#");
        String gn = givenName[1];
        person.setGivenName(gn);

        Property familyNameProperty = model.getProperty(personSchema+"familyName");
        NodeIterator iterfamilyName = model.listObjectsOfProperty(familyNameProperty);
        String uploadRdffamilyName = iterfamilyName.nextNode().toString();
        String[] familyName = uploadRdffamilyName.split("#");
        String fn = familyName[1];
        person.setFamilyName(fn);

        Property addressProperty = model.getProperty(personSchema+"address");
        NodeIterator iteraddress = model.listObjectsOfProperty(addressProperty);
        String uploadRdfaddress = iteraddress.nextNode().toString();
        String[] address = uploadRdfaddress.split("#");
        String a = address[1];
        person.setAddress(a);

        Property birthDateProperty = model.getProperty(personSchema+"birthDate");
        NodeIterator iterbirthDate = model.listObjectsOfProperty(birthDateProperty);
        String uploadRdfbirthDate = iterbirthDate.nextNode().toString();
        String[] birthDate = uploadRdfbirthDate.split("#");
        String bd = birthDate[1];
        Date parseDateOfBirth = simpleDateFormat.parse("1997-04-17 00:00:00");
        person.setBirthDate(parseDateOfBirth);

        Property birthPlaceProperty = model.getProperty(personSchema+"birthPlace");
        NodeIterator iterbirthPlace = model.listObjectsOfProperty(birthPlaceProperty);
        String uploadRdfbirthPlace = iterbirthPlace.nextNode().toString();
        String[] birthPlace = uploadRdfbirthPlace.split("#");
        String bp = birthPlace[1];
        person.setBirthPlace(bp);

        Property emailProperty = model.getProperty(personSchema+"email");
        NodeIterator iteremail = model.listObjectsOfProperty(emailProperty);
        String uploadRdfemail = iteremail.nextNode().toString();
        String[] email = uploadRdfemail.split("#");
        String e = email[1];
        person.setEmail(e);

        Property passportNumberProperty = model.getProperty(personSchema+"passportNumber");
        NodeIterator iterpassportNumber = model.listObjectsOfProperty(passportNumberProperty);
        String uploadRdfpassportNumber = iterpassportNumber.nextNode().toString();
        String[] passportNumber = uploadRdfpassportNumber.split("#");
        String pn = passportNumber[1];
        person.setPassportNumber(pn);

        Property dateOfIssuePassportProperty = model.getProperty(personSchema+"dateOfIssuePassport");
        NodeIterator iterdateOfIssuePassport = model.listObjectsOfProperty(dateOfIssuePassportProperty);
        String uploadRdfdateOfIssuePassport = iterdateOfIssuePassport.nextNode().toString();
        String[] dateOfIssuePassport = uploadRdfdateOfIssuePassport.split("#");
        String ip = dateOfIssuePassport[1];
        Date parsedateOfIssuePassport = simpleDateFormat.parse("1997-04-17 00:00:00");
        person.setDateOfIssuePassport(parsedateOfIssuePassport);

        Property dateOfExpiryPassportProperty = model.getProperty(personSchema+"dateOfExpiryPassport");
        NodeIterator iterdateOfExpiryPassport = model.listObjectsOfProperty(dateOfExpiryPassportProperty);
        String uploadRdfdateOfExpiryPassport = iterdateOfExpiryPassport.nextNode().toString();
        String[] dateOfExpiryPassport = uploadRdfdateOfExpiryPassport.split("#");
        String ep = dateOfExpiryPassport[1];
        Date parsedateOfExpiryPassport = simpleDateFormat.parse("1997-04-17 00:00:00");
        person.setDateOfExpiryPassport(parsedateOfExpiryPassport);

        return person;

    }
}
