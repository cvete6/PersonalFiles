package com.example.demo.DataMapper;

import com.example.demo.DomainModel.Person;
import com.example.demo.Repository.PersonJpaRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

///!!!!!!!NE GO KORISTAM OVAJ MAPER GO IMAM VO SERVISO ZA RDF
public class RdfMapper {

    private PersonJpaRepository personJpaRepository;

    public Person mapPersonFromRDFFile(Model model) throws ParseException {
        String personSchema = "http://schema.org/Person#";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Person person = new Person();

        Property socialNumberProperty = model.getProperty(personSchema+"socialNumber");
        NodeIterator iterSocialNumber = model.listObjectsOfProperty(socialNumberProperty);
        String uploadRdfSocialNumber = iterSocialNumber.nextNode().toString();//this return complet personschema with value
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

        Property givenAdditionalNameProperty = model.getProperty(personSchema+"additionalName");
        NodeIterator iterAdditionalName = model.listObjectsOfProperty(givenAdditionalNameProperty);
        String uploadRdfadditionalName = iterAdditionalName.nextNode().toString();
        String[] additionalName = uploadRdfadditionalName.split("#");
        String an = additionalName[1];
        person.setAdditionalName(an);

        Property givenAwardProperty = model.getProperty(personSchema+"award");
        NodeIterator iterAward = model.listObjectsOfProperty(givenAwardProperty);
        String uploadRdfAward = iterAward.nextNode().toString();
        String[] award = uploadRdfAward.split("#");
        String awardValue = award[1];
        person.setAward(awardValue);

        Property givenCallSignProperty = model.getProperty(personSchema+"callSign");
        NodeIterator iterCallSign = model.listObjectsOfProperty(givenCallSignProperty);
        String uploadRdfCallSign = iterCallSign.nextNode().toString();
        String[] callSign = uploadRdfAward.split("#");
        String callSignValue = callSign[1];
        person.setCallSign(callSignValue);

        Property addressProperty = model.getProperty(personSchema+"address");
        NodeIterator iteraddress = model.listObjectsOfProperty(addressProperty);
        String uploadRdfaddress = iteraddress.nextNode().toString();
        String[] address = uploadRdfaddress.split("#");
        String a = address[1];
        person.setAddress(a);

        Property givenContactPointProperty = model.getProperty(personSchema+"contactPoint");
        NodeIterator iterContactPoint = model.listObjectsOfProperty(givenContactPointProperty);
        String uploadRdfContactPoint = iterContactPoint.nextNode().toString();
        String[] contactPoint = uploadRdfContactPoint.split("#");
        String contactPointValue = contactPoint[1];
        person.setContactPoint(contactPointValue);

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

        Property givenDeathPlaceProperty = model.getProperty(personSchema+"deathPlace");
        NodeIterator iterDeathPlace = model.listObjectsOfProperty(givenDeathPlaceProperty);
        String uploadRdfDeathPlace = iterDeathPlace.nextNode().toString();
        String[] deathPlace = uploadRdfDeathPlace.split("#");
        String deathPlaceValue = deathPlace[1];
        person.setDeathPlace(deathPlaceValue);

        Property deathDateProperty = model.getProperty(personSchema+"deathDate");
        NodeIterator iterDeathDate = model.listObjectsOfProperty(deathDateProperty);
        String uploadRdfDeathDate = iterDeathDate.nextNode().toString();
        String[] deathDate = uploadRdfDeathDate.split("#");
        String dd = deathDate[1];
        Date parseDeathDate = simpleDateFormat.parse(dd);
        person.setDeathDate(parseDeathDate);

        Property givenFaxNumberProperty = model.getProperty(personSchema+"faxNumber");
        NodeIterator iterFaxNumber = model.listObjectsOfProperty(givenFaxNumberProperty);
        String uploadRdfFaxNumber = iterFaxNumber.nextNode().toString();
        String[] faxNumber = uploadRdfFaxNumber.split("#");
        String faxNumbereValue = faxNumber[1];
        person.setFaxNumber(faxNumbereValue);

        Property givenGenderProperty = model.getProperty(personSchema+"gender");
        NodeIterator iterGender = model.listObjectsOfProperty(givenGenderProperty);
        String uploadRdfGender = iterGender.nextNode().toString();
        String[] gender = uploadRdfGender.split("#");
        String genderValue = gender[1];
        person.setGender(genderValue);

        Property givenGlobalLocationNumberProperty = model.getProperty(personSchema+"globalLocationNumber");
        NodeIterator iterGlobalLocationNumber = model.listObjectsOfProperty(givenGlobalLocationNumberProperty);
        String uploadRdfGlobalLocationNumber = iterGlobalLocationNumber.nextNode().toString();
        String[] globalLocationNumber = uploadRdfGlobalLocationNumber.split("#");
        String globalLocationNumberValue = globalLocationNumber[1];
        person.setGlobalLocationNumber(globalLocationNumberValue);

        Property givenHeightProperty = model.getProperty(personSchema+"height");
        NodeIterator iterHeight = model.listObjectsOfProperty(givenHeightProperty);
        String uploadRdfHeight = iterHeight.nextNode().toString();
        String[] height = uploadRdfHeight.split("#");
        Integer heightValue = Integer.valueOf(height[1]);
        person.setHeight(heightValue);

        Property givenWeightProperty = model.getProperty(personSchema+"weight");
        NodeIterator iterWeight = model.listObjectsOfProperty(givenWeightProperty);
        String uploadRdfWeight = iterWeight.nextNode().toString();
        String[] weight = uploadRdfWeight.split("#");
        Integer weightValue = Integer.valueOf(weight[1]);
        person.setWeight(weightValue);

        Property givenHomeLocationProperty = model.getProperty(personSchema+"homeLocation");
        NodeIterator iterHomeLocation = model.listObjectsOfProperty(givenHomeLocationProperty);
        String uploadRdfHomeLocation = iterHomeLocation.nextNode().toString();
        String[] homeLocation = uploadRdfHomeLocation.split("#");
        String homeLocationValue = homeLocation[1];
        person.setHomeLocation(homeLocationValue);

        Property givenHonorificPrefixProperty = model.getProperty(personSchema+"honorifixPrefix");
        NodeIterator iterHonorificPrefix = model.listObjectsOfProperty(givenHonorificPrefixProperty);
        String uploadRdfHonorificPrefix = iterHonorificPrefix.nextNode().toString();
        String[] honorificPrefix = uploadRdfHonorificPrefix.split("#");
        String honorificPrefixValue = honorificPrefix[1];
        person.setHonorificPrefix(honorificPrefixValue);

        Property givenHonorificSuffixProperty = model.getProperty(personSchema+"honorifixSuffix");
        NodeIterator iterHonorificSuffix = model.listObjectsOfProperty(givenHonorificSuffixProperty);
        String uploadRdfHonorificSuffix = iterHonorificSuffix.nextNode().toString();
        String[] honorificSuffix = uploadRdfHonorificSuffix.split("#");
        String honorificSuffixValue = honorificSuffix[1];
        person.setHonorificSuffix(honorificSuffixValue);

        Property givenJobTitleProperty = model.getProperty(personSchema+"jobTitle");
        NodeIterator iterJobTitle = model.listObjectsOfProperty(givenJobTitleProperty);
        String uploadRdfJobTitle = iterJobTitle.nextNode().toString();
        String[] jobTitle = uploadRdfJobTitle.split("#");
        String jobTitleValue = homeLocation[1];
        person.setJobTitle(jobTitleValue);

        Property givenKnowsAboutProperty = model.getProperty(personSchema+"knowsAbout");
        NodeIterator iterKnowsAbout = model.listObjectsOfProperty(givenKnowsAboutProperty);
        String uploadRdfKnowsAbout = iterKnowsAbout.nextNode().toString();
        String[] knowsAbout = uploadRdfKnowsAbout.split("#");
        String knowsAboutValue = knowsAbout[1];
        person.setKnowsAbout(knowsAboutValue);

        Property givenKnowsLanguageProperty = model.getProperty(personSchema+"knowsLanguage");
        NodeIterator iterKnowsLanguage = model.listObjectsOfProperty(givenKnowsLanguageProperty);
        String uploadRdfKnowsLanguage = iterKnowsLanguage.nextNode().toString();
        String[] knowsLanguage = uploadRdfKnowsLanguage.split("#");
        String knowsLanguageValue = knowsLanguage[1];
        person.setKnowsLanguage(knowsLanguageValue);


        Property givenNationalityProperty = model.getProperty(personSchema+"nationality");
        NodeIterator iterNationality = model.listObjectsOfProperty(givenNationalityProperty);
        String uploadRdfNationality = iterNationality.nextNode().toString();
        String[] Nationality = uploadRdfNationality.split("#");
        String NationalityValue = Nationality[1];
        person.setNationality(NationalityValue);

        Property givenPerformInProperty = model.getProperty(personSchema+"performerIn");
        NodeIterator iterPerformIn = model.listObjectsOfProperty(givenPerformInProperty);
        String uploadRdfPerformIn = iterPerformIn.nextNode().toString();
        String[] PerformIn = uploadRdfPerformIn.split("#");
        String PerformInValue = PerformIn[1];
        person.setPerformerIn(PerformInValue);

        Property givenPublishingPrinciplesProperty = model.getProperty(personSchema+"publishingPrinciples");
        NodeIterator iterPublishingPrinciples = model.listObjectsOfProperty(givenPublishingPrinciplesProperty);
        String uploadRdfPublishingPrinciples = iterPublishingPrinciples.nextNode().toString();
        String[] PublishingPrinciples = uploadRdfPublishingPrinciples.split("#");
        String PublishingPrinciplesValue = PublishingPrinciples[1];
        person.setPublishingPrinciples(PublishingPrinciplesValue);

        Property givenSeekProperty = model.getProperty(personSchema+"seek");
        NodeIterator iterSeek = model.listObjectsOfProperty(givenSeekProperty);
        String uploadRdfSeek = iterSeek.nextNode().toString();
        String[] Seek = uploadRdfSeek.split("#");
        String SeekValue = Seek[1];
        person.setSeeks(SeekValue);

        Property givenTaxIDProperty = model.getProperty(personSchema+"taxID");
        NodeIterator iterTaxID = model.listObjectsOfProperty(givenTaxIDProperty);
        String uploadRdfTaxID = iterTaxID.nextNode().toString();
        String[] TaxID = uploadRdfTaxID.split("#");
        String TaxIDValue = TaxID[1];
        person.setTaxID(TaxIDValue);

        Property givenTelephoneProperty = model.getProperty(personSchema+"telephone");
        NodeIterator iterTelephone = model.listObjectsOfProperty(givenTelephoneProperty);
        String uploadRdfTelephone = iterTelephone.nextNode().toString();
        String[] Telephone = uploadRdfTelephone.split("#");
        String TelephoneValue = Telephone[1];
        person.setTelephone(TelephoneValue);

        Property givenWorkLocationProperty = model.getProperty(personSchema+"workLocation");
        NodeIterator iterWorkLocation = model.listObjectsOfProperty(givenWorkLocationProperty);
        String uploadRdfWorkLocation = iterWorkLocation.nextNode().toString();
        String[] WorkLocation = uploadRdfWorkLocation.split("#");
        String WorkLocationValue = WorkLocation[1];
        person.setWorkLocation(WorkLocationValue);

        /*Property givenSpouseProperty = model.getProperty(personSchema+"spouse");
        NodeIterator iterSpouse = model.listObjectsOfProperty(givenSpouseProperty);
        String uploadRdfSpouse = iterSpouse.nextNode().toString();*/

        Property childrenProperty = model.getProperty(personSchema+"children");
        NodeIterator iterChildren = model.listObjectsOfProperty(childrenProperty);
        List<Person> childrens = new ArrayList<>();
        while(iterChildren.hasNext()){
            Person child = new Person();
            Resource resourceChild = (Resource) iterChildren.nextNode();
            /*String givenNameChild = String.valueOf(resourceChild.getProperty(givenNameProperty).getObject());
            System.out.println("ime child " + givenNameChild);
            String[] nameChild = givenNameChild.split("#");
            System.out.println("ime " + nameChild[1]);*/

            String givenSocialNumberChild = String.valueOf(resourceChild.getProperty(socialNumberProperty).getObject());
            String [] socialNumberChild = givenSocialNumberChild.split("#");
            System.out.println("socialNumber " + socialNumberChild[1]);
            String snn =socialNumberChild[1];
            Optional<Person> existPersonChild = personJpaRepository.findBySocialNumber(snn);
            if(existPersonChild.isPresent()){
                //if this child already exist in database add this child as child to main person
                childrens.add(existPersonChild.get());
            }

            //child.setSocialNumber(socialNumberChild[1]);
        }
        person.setChildren(childrens);
        return person;

    }
}
