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
        model.addAttribute("departmentOrganization", new Organization());
        model.addAttribute("memberOfOrganization", new Organization());
        model.addAttribute("subOrganizationOrganization", new Organization());
        model.addAttribute("parentOfOrganization", new Organization());
        model.addAttribute("sponsorInOrganization", new Person());
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
    /**
     * Delete organization
     *
     * @param organizationId request that we send to delete organization with parameter organizationId
     * @return redirect to view all organizations
     */
    @RequestMapping("/delete")
    public String deleteOrganization(@RequestParam("organizationId") Integer organizationId) {
        organizationService.deleteOrganization(organizationId);
        return "redirect:/organizations/organization-list/page/1";
    }
    @PostMapping("/addDepartment")
    public String addDepartment(@ModelAttribute Organization departmentOrganization,
                                 @RequestParam("organizationId") Integer organizationId,
                                 Model model) {
        organizationService.addDepartment(organizationId,departmentOrganization);
        return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
    }

    @PostMapping("/addSubOrganization")
    public String addSubOrganizationOrganization(@ModelAttribute Organization subOrganizationOrganization,
                                @RequestParam("organizationId") Integer organizationId,
                                Model model) {
        organizationService.addSubOrganizationOrganization(organizationId,subOrganizationOrganization);
        return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;

    }

    @PostMapping("/addMemberOfOrganization")
    public String addMemberOfOrganization(@ModelAttribute Organization memberOfOrganization,
                                 @RequestParam("organizationId") Integer organizationId,
                                 Model model) {
        organizationService.addMemberOfOrganization(organizationId, memberOfOrganization);
        return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
    }

    @PostMapping("/addParentOfOrganization")
    public String addParentOfOrganization(@ModelAttribute Organization parentOfOrganization,
                                          @RequestParam("organizationId") Integer organizationId,
                                          Model model) {
        organizationService.addParentOfOrganization(organizationId, parentOfOrganization);
        return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
    }

    @PostMapping("/addSponsorInOrganization")
    public String addSponsor(@ModelAttribute Person sponsorInOrganization,
                             @RequestParam("organizationId") Integer organizationId,
                             Model model) {
        organizationService.addSponsorInOrganization(organizationId, sponsorInOrganization);
        return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
    }


}
