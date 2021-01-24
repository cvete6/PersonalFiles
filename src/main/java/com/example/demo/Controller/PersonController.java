package com.example.demo.Controller;

import com.example.demo.DomainModel.Person;
import com.example.demo.Service.PersonServiceImpl.PersonServiceImpl;
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

    public PersonController(PersonServiceImpl personService) {
        this.personService = personService;
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

    /**
     * Show form for adding new Person
     *
     * @param model is used to add model attributes to a view
     * @return thymeleaf template for insert new Person
     */
    @GetMapping("/showFormForAddPerson")
    public String persons(Model model) {
        model.addAttribute("person", new Person());
        return "addPerson";
    }

    /**
     * Save new Person to database
     *
     * @param person     Person that is save to model in AddPerson form
     * @param model         is used to get model attributes from view
     * @param personalImage multipartFile for uploaded personal image
     * @return redirect to thymeleaf template for all Persons
     * @throws IOException getBytes() from MultipartFile need not to be null
     */
    @PostMapping("/add")
    public String addNewPerson(@ModelAttribute Person person, Model model,
                               @RequestParam("personalImage") MultipartFile personalImage) throws IOException {
        person.setImage(personalImage.getBytes());
        personService.addNewPerson(person);
        return "redirect:/persons/person-list/page/1";
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
        return "redirect:/persons/person-list/page/1";
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
}
