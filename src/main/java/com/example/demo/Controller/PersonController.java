package com.example.demo.Controller;

import com.example.demo.DomainModel.Person;
import com.example.demo.Service.PersonServiceImpl.PersonServiceImpl;
import com.example.demo.Service.RdfManipulationServiceImpl.RdfManipulationServiceImpl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Controller for person
 */
@Controller
@RequestMapping("/persons")
public class PersonController {

    private PersonServiceImpl personService;
    private RdfManipulationServiceImpl rdfManipulationService;

    public PersonController(PersonServiceImpl personService, RdfManipulationServiceImpl rdfManipulationService) {
        this.personService = personService;
        this.rdfManipulationService = rdfManipulationService;
    }


    /**
     * Show start page
     *
     * @return thymeleaf template for insert new Person
     */
    @GetMapping("/start")
    public String index() {
        return "start";
    }

    /**
     * List all Persons in database
     *
     * @param model is used to add model attributes to a view
     * @return thymeleaf template for Persons
     */
    @RequestMapping(value = "/person-list/page/{page}")
    public String getAllPersons(@PathVariable("page") int page, Model model,
                                @RequestParam(value = "keyword", required = false) String keyword) {
        personService.getPaginatedPersons(page, keyword, model);
        return "allPersons";
    }

    /**
     * Delete Person
     *
     * @param personId request that we send to delete Person with parameter PersonId
     * @return redirect to view all Persons
     */
    @RequestMapping("/delete")
    public String deletePerson(@RequestParam("personId") Integer personId) {
        personService.deletePerson(personId);
        return "redirect:/persons/person-list/page/1";
    }

    //ova ne go koristam !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @GetMapping("/showFormForAddPerson")
    public String showFormForAddPerson(@RequestParam("personColleagueId") Integer personColleagueId, Model model) {
        model.addAttribute("colleaguePerson", new Person());
        return "addColleague";
    }

    /**
     * Save modified person to database
     *
     * @param person     person that is save to model in Edit person form
     * @param personalImage multipartFile for uploaded personal image
     * @param model         is used to get model attributes from view
     * @return redirect to thymeleaf template for all persons
     * @throws IOException getBytes() from MultipartFile need not to be null
     */
    @PostMapping("/edit")
    public String editPerson(@ModelAttribute Person person,
                             @RequestParam("personalImage") MultipartFile personalImage,
                             Model model) throws IOException {
        person.setImage(personalImage.getBytes());
        personService.editPerson(person);
        return "redirect:/persons/showFormForUpdate?personId=" + person.getId();
    }

    /**
     * Show edit form view with data from a person with appropriate personId
     *
     * @param personId id from a person that we want to change
     * @param model    is used to add model attributes to a view
     * @return thymeleaf template for edit person
     */
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("personId") Integer personId, Model model) {
        personService.sendDataToEditModelView(personId, model);
        model.addAttribute("colleaguePerson", new Person());
        model.addAttribute("childrenPerson", new Person());
        model.addAttribute("parentPerson", new Person());
        model.addAttribute("spousePerson", new Person());
        return "editPerson";
    }

    /**
     * Show view where can upload pdf file
     *
     * @return view template where can upload the pdf file
     */
    @GetMapping("/showFormForUploadPdfFile")
    public String showFormForUploadPdfFile() {
        return "uploadPDFFile";
    }

    /**
     * Get data from uploaded pdf file and create new entry in database
     *
     * @param uploadedMultipartPdfFile a file that we upload
     * @param model add attribute to model only if person already exist in database
     * @return redirect to edit view for new person or show details if person already exists in the database
     */
    @PostMapping("/uploadPdfFile")
    public String uploadAndSaveDataFromPdfFile(
            @RequestParam("uploadedMultipartPdfFile") MultipartFile uploadedMultipartPdfFile, Model model)
            throws Exception {
        return personService.validateAndCreateEmployee(uploadedMultipartPdfFile,model);
    }

    @RequestMapping(value = "/exportRDFFile/{personId}")
    public ResponseEntity<byte[]> rdfExport(@PathVariable("personId") Integer personId) throws IOException {

        Person person = personService.getPersonById(personId);
        byte[] documentContent = rdfManipulationService.createRdfFromPersonProfile(person);//call function to create new rdf

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setContentDisposition(ContentDisposition.parse("attachment; filename=RDFProfile.ttl"));
        headers.setContentLength(documentContent.length);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(documentContent.length)
                .contentType(MediaType.parseMediaType("application/rdf+xml"))
                .body(documentContent);
    }

    /**
     * Get data from uploaded file and create new entry in database if person with that rdf does not exist
     *
     * @param uploadedMultipartRDFFile a file that we upload
     * @param model add attribute to model only if person already exist in database
     * @return redirect to edit view for new person or show details if person already exists in the database
     */
    @PostMapping("/uploadRDFFile")
    public String uploadAndSaveDataFromRDFFile(
            @RequestParam("uploadedMultipartRDFFile") MultipartFile uploadedMultipartRDFFile, Model model)
            throws Exception {
        Person person = rdfManipulationService.validateAndCreatePerson(uploadedMultipartRDFFile,model);
        return  personService.redirectToPersonDetailsView(person,model);
    }

    /**
     * Save new Person to database
     *
     * @param model         is used to get model attributes from view
     * @return redirect to thymeleaf template for all Persons
     * @throws IOException getBytes() from MultipartFile need not to be null
     */
    @PostMapping("/addChildren")
    public String addNewChildren(@ModelAttribute Person childrenPerson,
                                 @RequestParam("personId") Integer personId,
                                 Model model) {
        personService.addChildren(personId,childrenPerson);
        return "redirect:/persons/showFormForUpdate?personId=" + personId;

    }

    @PostMapping("/addParent")
    public String addNewParent(@ModelAttribute Person parentPerson,
                               @RequestParam("personId") Integer personId,
                               Model model) {
        personService.addParent(personId,parentPerson);
        return "redirect:/persons/showFormForUpdate?personId=" + personId;

    }

    @PostMapping("/addColleague")
    public String addNewColleague(@ModelAttribute Person colleaguePerson,
                               @RequestParam("personId") Integer personId,
                               Model model) {
        personService.addColleagues(personId,colleaguePerson);
        return "redirect:/persons/showFormForUpdate?personId=" + personId;

    }


    @PostMapping("/addSpouse")
    public String addNewSpouse(@ModelAttribute Person spousePerson,
                               @RequestParam("personId") Integer personId,
                               Model model) {
        personService.addSpouse(personId,spousePerson);
        return "redirect:/persons/showFormForUpdate?personId=" + personId;

    }
}
