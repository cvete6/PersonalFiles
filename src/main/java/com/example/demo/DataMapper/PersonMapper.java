package com.example.demo.DataMapper;

import com.example.demo.DomainModel.Person;
import com.itextpdf.forms.fields.PdfFormField;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Class for mapping Client to Person(Data Transfer Object) and Person to Client
 */
public class PersonMapper {

    /**
     * Edit oldClient with data from new person
     *
     * @param person client that we want to edit with new values of data
     * @param oldPerson the same client that exist in database with old values of data
     * @return edited client
     */
    public static Person oldPersonMapToNewPerson(Person person, Person oldPerson) {

        oldPerson.setId(person.getId());
        oldPerson.setFamilyName(person.getFamilyName());
        oldPerson.setGivenName(person.getGivenName());
        oldPerson.setAdditionalName(person.getAdditionalName());
        oldPerson.setAddress(person.getAddress());
        oldPerson.setAward(person.getAward());
        oldPerson.setSocialNumber(person.getSocialNumber());
        oldPerson.setCallSign(person.getCallSign());
        oldPerson.setContactPoint(person.getContactPoint());
        oldPerson.setDeathDate(person.getDeathDate());
        oldPerson.setDeathPlace(person.getDeathPlace());
        oldPerson.setBirthDate(person.getBirthDate());
        oldPerson.setBirthDate(person.getBirthDate());
        oldPerson.setEmail(person.getEmail());
        oldPerson.setFaxNumber(person.getFaxNumber());
        oldPerson.setGender(person.getGender());
        oldPerson.setGlobalLocationNumber(person.getGlobalLocationNumber());
        oldPerson.setHeight(person.getHeight());
        oldPerson.setWeight(person.getWeight());
        oldPerson.setHomeLocation(person.getHomeLocation());
        oldPerson.setHonorificPrefix(person.getHonorificPrefix());
        oldPerson.setHonorificSuffix(person.getHonorificSuffix());
        oldPerson.setJobTitle(person.getJobTitle());
        oldPerson.setKnowsAbout(person.getKnowsAbout());
        oldPerson.setKnowsLanguage(person.getKnowsLanguage());
        oldPerson.setNationality(person.getNationality());
        oldPerson.setPerformerIn(person.getPerformerIn());
        oldPerson.setPublishingPrinciples(person.getPublishingPrinciples());
        oldPerson.setSeeks(person.getSeeks());
        oldPerson.setTaxID(person.getTaxID());
        oldPerson.setTelephone(person.getTelephone());
        oldPerson.setWorkLocation(oldPerson.getHomeLocation());
        oldPerson.setPassportNumber(person.getPassportNumber());
        oldPerson.setDateOfExpiryPassport(person.getDateOfExpiryPassport());
        oldPerson.setDateOfIssuePassport(person.getDateOfIssuePassport());
        if (person.getImage().length != 0) {
            oldPerson.setImage(person.getImage());
        }
        oldPerson.setChildren(person.getChildren());
        oldPerson.setColleague(person.getColleague());
        oldPerson.setParent(person.getParent());
        oldPerson.setSpouse(person.getSpouse());
        oldPerson.setFollows(person.getFollows());
        oldPerson.setKnows(person.getKnows());

        oldPerson.setMemberOf(person.getMemberOf());
        oldPerson.setOrganization_sponsor(person.getOrganization_sponsor());
        oldPerson.setWorksFor(person.getWorksFor());

        return oldPerson;
    }

    /**
     * From the data in the pdf form fields created a new person
     *
     * @param fields input fields in uploaded pdf
     * @return created new person
     * @throws ParseException parsed data cannot be null
     */
    public static Person dataFromPdfFormMapToClient(Map<String, PdfFormField> fields) throws ParseException {
        Person person = new Person();
        person.setGivenName(fields.get("givenName").getValueAsString());
        person.setFamilyName(fields.get("familyName").getValueAsString());
        person.setAdditionalName(fields.get("additionalName").getValueAsString());
        person.setAddress(fields.get("address").getValueAsString());
        person.setAward(fields.get("award").getValueAsString());
        person.setSocialNumber(fields.get("socialNumber").getValueAsString());
        person.setCallSign(fields.get("callSign").getValueAsString());
        person.setContactPoint(fields.get("contactPoint").getValueAsString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateOfBirth = fields.get("dateOfBirth").getValueAsString();
        Date parseDateOfBirth = simpleDateFormat.parse(dateOfBirth);
        person.setBirthDate(parseDateOfBirth);
        person.setBirthPlace(fields.get("placeOfBirth").getValueAsString());

        String dateOfDeath = fields.get("dateOfDeath").getValueAsString();
        Date parseDateOfDeath = simpleDateFormat.parse(dateOfDeath);
        person.setDeathDate(parseDateOfDeath);
        person.setDeathPlace(fields.get("placeOfDeath").getValueAsString());

        person.setEmail(fields.get("email").getValueAsString());
        person.setFaxNumber(fields.get("faxNumber").getValueAsString());
        person.setGender(fields.get("gender").getValueAsString());
        person.setGlobalLocationNumber(fields.get("globalLocationNumber").getValueAsString());
        person.setHeight(20);
        person.setHomeLocation(fields.get("homeLocation").getValueAsString());
        person.setHonorificPrefix(fields.get("honorificPrefix").getValueAsString());
        person.setHonorificSuffix(fields.get("honorificSuffix").getValueAsString());
        person.setJobTitle(fields.get("jobTitle").getValueAsString());
        person.setKnowsAbout(fields.get("knowsAbout").getValueAsString());
        person.setKnowsLanguage(fields.get("knowsLanguage").getValueAsString());
        person.setNationality(fields.get("nationality").getValueAsString());
        person.setPerformerIn(fields.get("performIn").getValueAsString());
        person.setPublishingPrinciples(fields.get("publishingPrinciples").getValueAsString());
        person.setSeeks(fields.get("seek").getValueAsString());
        person.setTaxID(fields.get("taxID").getValueAsString());
        person.setTelephone(fields.get("telephone").getValueAsString());
        person.setWeight(20
        //        Integer.parseInt(fields.get("weight").getValueAsString())
        );
        person.setWorkLocation(fields.get("workLocation").getValueAsString());
        person.setKnowsAbout(fields.get("knowsAbout").getValueAsString());
        person.setPassportNumber(fields.get("passportNumber").getValueAsString());

        String dateOfExpiryPassport = fields.get("dateOfExpiryPassport").getValueAsString();
        Date parseDateOfExpiryPassport = simpleDateFormat.parse(dateOfExpiryPassport);
        person.setDateOfExpiryPassport(parseDateOfExpiryPassport);

        String dateOfIssuePassport = fields.get("dateOfIssuePassport").getValueAsString();
        Date parseDateOfIssuePassport = simpleDateFormat.parse(dateOfIssuePassport);
        person.setDateOfIssuePassport(parseDateOfIssuePassport);

        return person;
    }

}
