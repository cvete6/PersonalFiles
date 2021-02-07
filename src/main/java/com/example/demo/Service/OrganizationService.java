package com.example.demo.Service;

import com.example.demo.DomainModel.Organization;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

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

    }
