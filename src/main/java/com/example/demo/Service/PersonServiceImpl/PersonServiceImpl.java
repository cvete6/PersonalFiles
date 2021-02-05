package com.example.demo.Service.PersonServiceImpl;

import com.example.demo.DataMapper.PersonMapper;
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
        /*Optional<Person> existPerson = personJpaRepository.findById(id);
        Person person = existPerson.get();
        List<Person> personsList = personJpaRepository.findAll();
        for( Person p : personsList){
            if(p.getColleague().contains(person)){
                p.getColleague().remove(person);
            }
        }*/
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

        model.addAttribute("person", person);
        model.addAttribute("personsList", personList);
        model.addAttribute("organizationList",organizationList);
        model.addAttribute("colleaguesList",colleaguesList);
        model.addAttribute("childrenList",childrenList);
        model.addAttribute("parentList",parentList);
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
        if(existsPerson.isPresent()){
            Person person = existsPerson.get();
            List<Person> colleagues = person.getColleague();
            //if colleague do NOT exist
            if (!existColleaguePerson.isPresent()) {
                //create colleague like a person
                Person newColleague = addNewPerson(colleaguePerson);
                //check if colleague is already in person colleagues
                    if(!colleagues.contains(newColleague)){
                        colleagues.add(newColleague);
                    }
                person.setColleague(colleagues);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if colleague that we want to add to person already exists in database we check is in colleagues of person if not we add to persons_collageue
                //check if colleague is already in person colleagues
                Person newColleague = existColleaguePerson.get();
                if(!colleagues.contains(newColleague)){
                    colleagues.add(newColleague);
                }
                person.setColleague(colleagues);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        }
        else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addChildren(Integer personId, Person childrenPerson) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String childrenSocialNumber = childrenPerson.getSocialNumber();
        Optional<Person> existChildrenPerson = personJpaRepository.findBySocialNumber(childrenSocialNumber);

        //this person always exist because we add childrens for that person
        if(existsPerson.isPresent()){
            Person person = existsPerson.get();
            List<Person> children = person.getChildren();
            //if children do NOT exist
            if (!existChildrenPerson.isPresent()) {
                //create children like a person
                Person newChildren = addNewPerson(childrenPerson);
                //check if children is already in person childrens
                if(!children.contains(newChildren)){
                    children.add(newChildren);
                }
                person.setChildren(children);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if children that we want to add to person already exists in database we check is in childrens of person if not we add to persons_collageue
                //check if children is already in person childrens
                Person newChildren = existChildrenPerson.get();
                if(!children.contains(newChildren)){
                    children.add(newChildren);
                }
                person.setChildren(children);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        }
        else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

    @Override
    public String addParent(Integer personId, Person parentPerson) {
        Optional<Person> existsPerson = personJpaRepository.findById(personId);
        String parentSocialNumber = parentPerson.getSocialNumber();
        Optional<Person> existParentPerson = personJpaRepository.findBySocialNumber(parentSocialNumber);

        //this person always exist because we add parents for that person
        if(existsPerson.isPresent()){
            Person person = existsPerson.get();
            List<Person> parents = person.getParent();
            //if parent do NOT exist
            if (!existParentPerson.isPresent()) {
                //create parent like a person
                Person newParent = addNewPerson(parentPerson);
                //check if parent is already in person parents
                if(!parents.contains(newParent)){
                    parents.add(newParent);
                }
                person.setParent(parents);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            } else {
                //if parent that we want to add to person already exists in database we check is in parents of person if not we add to persons_collageue
                //check if parent is already in person parents
                Person newParent = existParentPerson.get();
                if(!parents.contains(newParent)){
                    parents.add(newParent);
                }
                person.setParent(parents);
                personJpaRepository.save(person);
                return "redirect:/persons/showFormForUpdate?personId=" + personId;
            }
        }
        else {
            return "redirect:/persons/showFormForUpdate?personId=" + personId;
        }
    }

}
