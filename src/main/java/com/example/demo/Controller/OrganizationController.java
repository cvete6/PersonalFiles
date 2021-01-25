package com.example.demo.Controller;

import com.example.demo.DomainModel.Organization;
import com.example.demo.DomainModel.Person;
import com.example.demo.Service.OrganizationServiceImpl.OrganizationServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Manipulate with organizations in application (add and edit only by admin)
 */
@Controller
@RequestMapping("/organizations")
public class OrganizationController {

    private OrganizationServiceImpl organizationService;

    public OrganizationController(OrganizationServiceImpl organizationService) {
        this.organizationService = organizationService;
    }

    @RequestMapping(value = "/organization-list/page/{page}")
    public String getAllPersons(@PathVariable("page") int page, Model model) {
        organizationService.getPaginatedOrganizations(page, model);
        return "allOrganizations";
    }

    @PostMapping("/edit")
    public String editOrganization(@ModelAttribute Organization organization, Model model) throws IOException {
        organizationService.editOrganization(organization);
        Integer organizationID=organization.getId();
        return "redirect:/organizations/showFormForUpdate?organizationId="+organizationID;
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("organizationId") Integer organizationId, Model model) {
        organizationService.sendDataToEditModelView(organizationId, model);
        return "editOrganization";
    }

    /**
     * Show form for adding new Person
     *
     * @param model is used to add model attributes to a view
     * @return thymeleaf template for insert new Person
     */
    @GetMapping("/showFormForAddOrganization")
    public String organizations(Model model) {
        model.addAttribute("organization", new Organization());
        return "addOrganization";
    }

    /**
     * Save new Person to database
     *
     * @param organization   Organization that is save to model in AddOrganization form
     * @param model         is used to get model attributes from view
     * @return redirect to thymeleaf template for all Organizations
     */
    @PostMapping("/addOrganization")
    public String addNewPerson(@ModelAttribute Organization organization, Model model) {
        organizationService.addNewOrganization(organization);
        return "redirect:/organizations/organization-list/page/1";
    }
}
