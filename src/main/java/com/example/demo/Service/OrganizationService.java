package com.example.demo.Service;

import com.example.demo.DomainModel.Organization;
import com.example.demo.DomainModel.Person;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

/**
 * All method that are used to manipulate with organizations
 */
public interface OrganizationService {

    Page<Organization> getPaginatedOrganizations(int page, Model model);

    Organization getOrganizationById(Integer id);

    Organization editOrganization(Organization organization);

    void sendDataToEditModelView(Integer organizationId, Model model);

    Organization addNewOrganization(Organization organization);

    void deleteOrganization(Integer id);

    String addDepartment(Integer organizationId, Organization departmentOrganization);

    String addSubOrganizationOrganization(Integer organizationId, Organization subOrganizationOrganization);

    String addMemberOfOrganization(Integer organizationId, Organization memberOfOrganization);

    String addParentOfOrganization(Integer organizationId, Organization parentOfOrganization);

    String addSponsorInOrganization(Integer organizationId, Person sponsorInOrganization);

    String addEmployeeInOrganization(Integer organizationId, Person employee);

    void addMemberInOrganization(Integer organizationId, Person member);
}
