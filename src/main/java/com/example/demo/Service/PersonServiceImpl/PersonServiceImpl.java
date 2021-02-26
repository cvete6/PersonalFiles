package com.example.demo.Service.PersonServiceImpl;

import com.example.demo.DataMapper.PersonMapper;
import com.example.demo.DomainModel.AuthenticationProvider;
import com.example.demo.DomainModel.Organization;
import com.example.demo.DomainModel.Person;
import com.example.demo.EmployeeDataValidator.DataValidatorImpl;
import com.example.demo.Exceptions.InvalidPersonIdException;
import com.example.demo.Repository.OrganizationJpaRepository;
import com.example.demo.Repository.PersonJpaRepository;
import com.example.demo.Service.PersonService;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.demo.DataMapper.PersonMapper.oldPersonMapToNewPerson;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonJpaRepository personJpaRepository;
    private PersonMapper personMapper;
    private DataValidatorImpl dataValidatorImpl;
    private OrganizationJpaRepository organizationJpaRepository;

    public PersonServiceImpl(PersonJpaRepository personJpaRepository, DataValidatorImpl dataValidatorImpl, OrganizationJpaRepository organizationJpaRepository) {
        this.personJpaRepository = personJpaRepository;
        this.dataValidatorImpl = dataValidatorImpl;
        this.organizationJpaRepository = organizationJpaRepository;
    }

    @Override
    public List<Person> getAllPersons() {
        return personJpaRepository.findAll();
    }

    @Override
    public Person getPersonByEmail(String email){
        return personJpaRepository.findByEmail(email);
    }

    @Override
    public Person getPersonById(Integer id) {
        Optional<Person> result = personJpaRepository.findById(id);
        Person person = new Person();
        if (result.isPresent()) {
            person = result.get();
        }
        return person;
    }

    @Override
    public Page<Person> getPaginatedPersons(int page, String keyword, Model model) {
        PageRequest pageable = PageRequest.of(page - 1, 10);

        Page<Person> personPage = calculateNumberOfPages(keyword, model, pageable);
        int totalPages = personPage.getTotalPages();
        long totalItems = personPage.getTotalElements();
        List<Person> personList = personPage.getContent();

        model.addAttribute("currentPage", page);
        model.addAttribute("personsList", personList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("keyword", keyword);

        return personJpaRepository.findAll(pageable);
    }

    @Override
    public void deletePerson(Integer id) {
        Optional<Person> existPerson = personJpaRepository.findById(id);
        if (existPerson.isPresent()) {
            Person person = existPerson.get();
            //find all persons in database and delete if somewhere in spouse is this person
            List<Person> personsList = personJpaRepository.findAll();
            for (Person p : personsList) {
                Person spouse = p.getSpouse();
                if (spouse != null) {
                    if (spouse.getSocialNumber().equals(person.getSocialNumber())) {
                        p.setSpouse(null);
                        personJpaRepository.save(p);
                    }
                }
            }
            //delete person but this person is connected with work in organization
            List<Organization> organizationList = organizationJpaRepository.findAll();
            for (Organization o : organizationList) {
                List<Person> employees = o.getEmployee();
                if (!employees.isEmpty()) {
                    if (employees.contains(person)) {
                        employees.remove(person);
                    }
                }
                organizationJpaRepository.save(o);
            }
        }
        personJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Person> findPersonBySocialNumber(Person person) {

        String socialNumber = person.getSocialNumber();
        return personJpaRepository.findBySocialNumber(socialNumber);
    }

    @Override
    public Person addNewPerson(Person person) {
        return personJpaRepository.save(person);
    }

    @Override
    public void addNewPersonAfterOAuthLoginSuccess(String email, String name, AuthenticationProvider authenticationProvider){
        Person person = new Person();
        person.setEmail(email);
        person.setGivenName(name);
        person.setSocialNumber("000000000");
        person.setFamilyName("empty");
        person.setAuthProvider(authenticationProvider);

        personJpaRepository.save(person);
    }
    @Override
    public void updateCustomerAfterOAuthLoginSuccess(Person person, String name, AuthenticationProvider authenticationProvider){
        person.setGivenName(name);
        person.setAuthProvider(authenticationProvider);

        personJpaRepository.save(person);
    }

    @Override
    public Person editPerson(Person person) {
        Person oldPerson = personJpaRepository.findById(person.getId()).orElseThrow(
                InvalidPersonIdException::new);
        Person modifiedPerson = oldPersonMapToNewPerson(person, oldPerson);
        return personJpaRepository.save(modifiedPerson);
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

    @Override
    public String validateAndCreateEmployee(MultipartFile uploadedMultipartPdfFile, Model model) throws IOException, ParseException {
        Person validPerson;
        Optional<Person> person = createPersonFromPdfData(uploadedMultipartPdfFile, model);
        if (person.isPresent()) {
            validPerson = person.get();
            return redirectToPersonDetailsView(validPerson, model);
        }
        return "insertEmail";
    }

    @Override
    public String redirectToPersonDetailsView(Person person, Model model) {
        Optional<Person> existPerson = findPersonBySocialNumber(person);
        if (!existPerson.isPresent()) {
            Person newPerson = addNewPerson(person);
            return "redirect:/persons/showFormForUpdate?personId=" + newPerson.getId();
        } else {
            Person oldPerson = existPerson.get();
            Integer personId = oldPerson.getId();
            model.addAttribute("exists", true);
            model.addAttribute("personId", personId);
            return "insertEmail";
        }
    }

    @Override
    public void sendDataToEditModelView(Integer personId, Model model) {
        Person person = getPersonById(personId);
        Base64.Encoder encoder = Base64.getEncoder();
        String personalImage = "";


        byte[] personalImageByte = person.getImage();

        if (personalImageByte != null) {
            model.addAttribute("personalImageExist", false);
            personalImage = encoder.encodeToString(personalImageByte);
            model.addAttribute("personalImage", personalImage);
        } else {
            model.addAttribute("personalImageExist", true);
        }

        List<Person> personList = personJpaRepository.findAll();
        List<Organization> organizationList = organizationJpaRepository.findAll();
        List<Person> colleaguesList = person.getColleague();
        List<Person> childrenList = person.getChildren();
        List<Person> parentList = person.getParent();
        Person spouse = person.getSpouse();
        List<Person> followsList = person.getFollows();
        List<Person> knowsList = person.getKnows();
        Organization organizationSponsor = person.getOrganization_sponsor();
        Organization worksForOrganization = person.getWorksFor();
        List<Organization> memberOfOrganizationList = person.getMemberOf();

        model.addAttribute("person", person);
        model.addAttribute("personsList", personList);
        model.addAttribute("organizationList", organizationList);
        model.addAttribute("colleaguesList", colleaguesList);
        model.addAttribute("childrenList", childrenList);
        model.addAttribute("parentList", parentList);
        model.addAttribute("spouse", spouse);
        model.addAttribute("followsList", followsList);
        model.addAttribute("knowsList", knowsList);
        model.addAttribute("organizationSponsor", organizationSponsor);
        model.addAttribute("worksFor", worksForOrganization);
        model.addAttribute("memberOfOrganizationList", memberOfOrganizationList);
    }

    private Optional<Person> createPersonFromPdfData(MultipartFile uploadedMultipartPdfFile, Model model)
            throws IOException, ParseException {
        File uploadedPdfFile = convertMultipartFileToFile(uploadedMultipartPdfFile);
        PdfReader reader = new PdfReader(uploadedPdfFile);
        PdfDocument document = new PdfDocument(reader);
        PdfAcroForm acroForm = PdfAcroForm.getAcroForm(document, false);
        Map<String, PdfFormField> fields = acroForm.getFormFields();
        document.close();
        reader.close();
        uploadedPdfFile.delete();

        ArrayList<String> invalidInputFields = dataValidatorImpl.validatePdfForm(fields);
        if (invalidInputFields.isEmpty()) {
            Optional<Person> person = Optional.of(personMapper.dataFromPdfFormMapToClient(fields));
            return person;
        }
        model.addAttribute("invalidUploadPdf", true);
        model.addAttribute("invalidInputFields", invalidInputFields);
        return Optional.empty();

    }

    private List<Person> findPersonByKeywordIgnoreCase(String keyword) {
        List<Person> persons = new ArrayList<>();
        for (Person person : personJpaRepository.findAll()) {

            String lowerKeyword = keyword.toLowerCase();
            String personName = person.getGivenName().toLowerCase();
            String personLastName = person.getFamilyName().toLowerCase();
            String personSocialNumber = person.getSocialNumber();

            if (personName.contains(lowerKeyword) || personLastName.contains(lowerKeyword) ||
                    personSocialNumber.contains(lowerKeyword)) {
                persons.add(person);
            }
        }
        return persons;
    }

    private Page<Person> calculateNumberOfPages(String keyword, Model model, PageRequest pageable) {

        Page<Person> personPage;
        int totalPages;

        if (keyword != null) {
            List<Person> persons = findPersonByKeywordIgnoreCase(keyword);
            int total = persons.size();
            int start = Math.toIntExact(pageable.getOffset());
            int end = Math.min(start + pageable.getPageSize(), total);
            if (start <= end) {
                persons = persons.subList(start, end);
            }
            personPage = new PageImpl<>(persons, pageable, total);
            totalPages = personPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
        } else {
            personPage = personJpaRepository.findAll(pageable);
            totalPages = personPage.getTotalPages();

            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
        }
        return personPage;
    }

    @Override
    public String addColleagues(Integer personId, Person colleaguePerson) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String colleagueSocialNumber = colleaguePerson.getSocialNumber();
        Optional<Person> existColleaguePerson = personJpaRepository.findBySocialNumber(colleagueSocialNumber);

        //this person always exist because we add colleagues for that person
        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            List<Person> colleagues = person.getColleague();
            //if colleague do NOT exist
            if (!existColleaguePerson.isPresent()) {
                //create colleague like a person
                Person newColleague = addNewPerson(colleaguePerson);
                //check if colleague is already in person colleagues
                if (!colleagues.contains(newColleague)) {
                    colleagues.add(newColleague);
                }
                person.setColleague(colleagues);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if colleague that we want to add to person already exists in database we check is in colleagues of person if not we add to persons_collageue
                //check if colleague is already in person colleagues
                Person newColleague = existColleaguePerson.get();
                if (!colleagues.contains(newColleague)) {
                    colleagues.add(newColleague);
                }
                person.setColleague(colleagues);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addChildren(Integer personId, Person childrenPerson) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String childrenSocialNumber = childrenPerson.getSocialNumber();
        Optional<Person> existChildrenPerson = personJpaRepository.findBySocialNumber(childrenSocialNumber);

        //this person always exist because we add childrens for that person
        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            List<Person> children = person.getChildren();
            //if children do NOT exist
            if (!existChildrenPerson.isPresent()) {
                //create children like a person
                Person newChildren = addNewPerson(childrenPerson);
                //check if children is already in person childrens
                if (!children.contains(newChildren)) {
                    children.add(newChildren);
                }
                person.setChildren(children);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if children that we want to add to person already exists in database we check is in childrens of person if not we add to persons_collageue
                //check if children is already in person childrens
                Person newChildren = existChildrenPerson.get();
                if (!children.contains(newChildren)) {
                    children.add(newChildren);
                }
                person.setChildren(children);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addParent(Integer personId, Person parentPerson) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String parentSocialNumber = parentPerson.getSocialNumber();
        Optional<Person> existParentPerson = personJpaRepository.findBySocialNumber(parentSocialNumber);

        //this person always exist because we add parents for that person
        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            List<Person> parents = person.getParent();
            //if parent do NOT exist
            if (!existParentPerson.isPresent()) {
                //create parent like a person
                Person newParent = addNewPerson(parentPerson);
                //check if parent is already in person parents
                if (!parents.contains(newParent)) {
                    parents.add(newParent);
                }
                person.setParent(parents);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if parent that we want to add to person already exists in database we check is in parents of person if not we add to persons_collageue
                //check if parent is already in person parents
                Person newParent = existParentPerson.get();
                if (!parents.contains(newParent)) {
                    parents.add(newParent);
                }
                person.setParent(parents);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addSpouse(Integer personId, Person spousePerson) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String spouseSocialNumber = spousePerson.getSocialNumber();
        Optional<Person> existSpousePerson = personJpaRepository.findBySocialNumber(spouseSocialNumber);

        //this person always exist because we add spouses for that person
        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            //if spouse do NOT exist
            if (!existSpousePerson.isPresent()) {
                //create spouse like a person
                Person newSpouse = addNewPerson(spousePerson);
                //check if spouse is already in person spouses
                person.setSpouse(newSpouse);

                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if spouse that we want to add to person already exists in database we check is in spouses of person if not we add to persons_collageue
                //check if spouse is already in person spouses
                Person newSpouse = existSpousePerson.get();
                person.setSpouse(newSpouse);

                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addFollowPerson(Integer personId, Person followPerson) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String followPersonSocialNumber = followPerson.getSocialNumber();
        Optional<Person> existFollowPerson = personJpaRepository.findBySocialNumber(followPersonSocialNumber);

        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            List<Person> follows = person.getFollows();
            if (!existFollowPerson.isPresent()) {
                Person newFollowPerson = addNewPerson(followPerson);
                if (!follows.contains(newFollowPerson)) {
                    follows.add(newFollowPerson);
                }
                person.setFollows(follows);
                personJpaRepository.save(person);

                List<Person> newFollows = new ArrayList<>();
                newFollows.add(person);
                //Add person like a follow in newFollowPerson
                newFollowPerson.setFollows(newFollows);
                personJpaRepository.save(newFollowPerson);

                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                Person newFollowPerson = existFollowPerson.get();
                if (!follows.contains(newFollowPerson)) {
                    follows.add(newFollowPerson);
                }
                person.setFollows(follows);
                personJpaRepository.save(person);

                //Add person like a knows in newKnowPerson
                List<Person> newFollows = newFollowPerson.getFollows();
                if (!newFollows.contains(person)) {
                    newFollows.add(person);
                }
                newFollowPerson.setFollows(newFollows);
                personJpaRepository.save(newFollowPerson);

                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addKnowPerson(Integer personId, Person knowPerson) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String followPersonSocialNumber = knowPerson.getSocialNumber();
        Optional<Person> existFollowPerson = personJpaRepository.findBySocialNumber(followPersonSocialNumber);

        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            List<Person> knows = person.getKnows();
            if (!existFollowPerson.isPresent()) {
                Person newKnowPerson = addNewPerson(knowPerson);
                if (!knows.contains(newKnowPerson)) {
                    knows.add(newKnowPerson);
                }
                person.setKnows(knows);
                personJpaRepository.save(person);

                List<Person> newKnows = new ArrayList<>();
                newKnows.add(person);
                //Add person like a knows in newKnowPerson
                newKnowPerson.setKnows(newKnows);
                personJpaRepository.save(newKnowPerson);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                Person newKnowPerson = existFollowPerson.get();
                if (!knows.contains(newKnowPerson)) {
                    knows.add(newKnowPerson);
                }
                person.setKnows(knows);
                personJpaRepository.save(person);

                //Add person like a knows in newKnowPerson
                List<Person> newKnows = newKnowPerson.getKnows();
                if (!newKnows.contains(person)) {
                    newKnows.add(person);
                }
                newKnowPerson.setKnows(newKnows);
                personJpaRepository.save(newKnowPerson);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addOrganizationSponsor(Integer personId, Organization organizationSponsor) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String organizationSponsorEmail = organizationSponsor.getEmail();
        Optional<Organization> existOrganizationSponsor = organizationJpaRepository.findByEmail(organizationSponsorEmail);
        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            //if organization do NOT exist
            if (!existOrganizationSponsor.isPresent()) {
                Organization newOrganization = organizationJpaRepository.save(organizationSponsor);
                person.setOrganization_sponsor(newOrganization);
                personJpaRepository.save(person);

                //add bidirectional relationship
                List<Person> sponsorsInOrganization = new ArrayList<>();
                sponsorsInOrganization.add(person);
                newOrganization.setSponsors(sponsorsInOrganization);
                organizationJpaRepository.save(newOrganization);

                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if Organization exist
                Organization organization = existOrganizationSponsor.get();
                person.setOrganization_sponsor(organization);
                personJpaRepository.save(person);

                //add bidirectional relationship
                List<Person> sponsorsInOrganization = organization.getSponsors();
                sponsorsInOrganization.add(person);
                organization.setSponsors(sponsorsInOrganization);
                organizationJpaRepository.save(organization);

                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addWorksForOrganization(Integer personId, Organization worksForOrganization) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String organizationPersonWorksForEmail = worksForOrganization.getEmail();
        Optional<Organization> existOrganization = organizationJpaRepository.findByEmail(organizationPersonWorksForEmail);
        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            //if organization do NOT exist
            if (!existOrganization.isPresent()) {
                Organization newOrganization = organizationJpaRepository.save(worksForOrganization);
                person.setWorksFor(newOrganization);
                personJpaRepository.save(person);

                //add bidirectional relationship
                List<Person> employees = new ArrayList<>();
                employees.add(person);
                newOrganization.setEmployee(employees);
                organizationJpaRepository.save(newOrganization);

                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if Organization exist
                Organization organization = existOrganization.get();
                person.setWorksFor(organization);
                personJpaRepository.save(person);

                //add bidirectional relationship
                List<Person> employees = organization.getEmployee();
                employees.add(person);
                organization.setEmployee(employees);
                organizationJpaRepository.save(organization);

                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    /**
     * Person has property list<Organization> memberOf that means that person can em member of many organization
     * in this function we add add new organization to person and also save old organization that person is member of
     * and plus also add this person to ???????
     * *
     *
     * @param personId
     * @param memberOfOrganization
     * @return
     */
    @Override
    public String addMemberOfOrganization(Integer personId, Organization memberOfOrganization) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String organizationPersonMemberOfEmail = memberOfOrganization.getEmail();
        Optional<Organization> existOrganization = organizationJpaRepository.findByEmail(organizationPersonMemberOfEmail);
        if (existsPerson.isPresent()) {
            Person person = existsPerson.get();
            List<Organization> memberOf = person.getMemberOf();
            //if organization do NOT exist
            if (!existOrganization.isPresent()) {
                Organization newOrganization = organizationJpaRepository.save(memberOfOrganization);
                memberOf.add(newOrganization);
                person.setMemberOf(memberOf);
                personJpaRepository.save(person);

            /*    //add bidirectional relationship
                List<Person> member = new ArrayList<>();
                if(newOrganization.getMember()!=null){
                    member = newOrganization.getMember();
                }
                member.add(person);
                newOrganization.setMember(member);
                organizationJpaRepository.save(newOrganization);*/

                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if Organization exist
                Organization organization = existOrganization.get();
                memberOf.add(organization);
                person.setMemberOf(memberOf);
                personJpaRepository.save(person);

           /*     //add bidirectional relationship
                List<Person> member = new ArrayList<>();
                member = organization.getMember();
                member.add(person);
                organization.setMember(member);
                organizationJpaRepository.save(organization);*/

                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        } else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }
}
