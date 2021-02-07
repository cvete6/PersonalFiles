package com.example.demo.Service.OrganizationServiceImpl;

import com.example.demo.DomainModel.Organization;
import com.example.demo.Exceptions.InvalidOrganizationIdException;
import com.example.demo.Repository.OrganizationJpaRepository;
import com.example.demo.Service.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.demo.DataMapper.OrganizationMapper.oldOrganizationMapToNewOrganization;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationJpaRepository organizationJpaRepository;

    public OrganizationServiceImpl(OrganizationJpaRepository organizationJpaRepository) {
        this.organizationJpaRepository = organizationJpaRepository;
    }

    @Override
    public Page<Organization> getPaginatedOrganizations(int page,Model model) {
        PageRequest pageable = PageRequest.of(page - 1, 10);

        Page<Organization> organizationPage = calculateNumberOfPages(model, pageable);
        int totalPages = organizationPage.getTotalPages();
        long totalItems = organizationPage.getTotalElements();
        List<Organization> organizationList = organizationPage.getContent();

        model.addAttribute("currentPage", page);
        model.addAttribute("organizationsList", organizationList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);

        return organizationJpaRepository.findAll(pageable);
    }

    @Override
    public Organization getOrganizationById(Integer id) {
        Optional<Organization> result = organizationJpaRepository.findById(id);
        Organization organization = new Organization();
        if (result.isPresent()) {
            organization = result.get();
        }
        return organization;
    }

    private Page<Organization> calculateNumberOfPages(Model model, PageRequest pageable) {
        Page<Organization> organizationPage;
        int totalPages;
        organizationPage = organizationJpaRepository.findAll(pageable);
        totalPages = organizationPage.getTotalPages();

            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
        return organizationPage;
    }

    @Override
    public Organization editOrganization(Organization organization) {
        Organization oldOrganization = organizationJpaRepository.findById(organization.getId()).orElseThrow(
                InvalidOrganizationIdException::new);
        Organization modifiedOrganization = oldOrganizationMapToNewOrganization(organization, oldOrganization);
        return organizationJpaRepository.save(modifiedOrganization);
    }
    @Override
    public void sendDataToEditModelView(Integer organizationId, Model model) {
        Organization organization = getOrganizationById(organizationId);
        List<Organization> departmentList = organization.getDepartments();
        List<Organization> memberOfOrganizationList = organization.getMemberOf_organization();
        List<Organization> subOrganizationOrganizationList = organization.getSubOrganization();
        Organization parentIfOrganization = organization.getParentOrganization();

        model.addAttribute("organization", organization);
        model.addAttribute("departmentList", departmentList);
        model.addAttribute("memberOfOrganizationList", memberOfOrganizationList);
        model.addAttribute("subOfOrganizationList", subOrganizationOrganizationList);
        model.addAttribute("parentOfOrganizationExisted", parentIfOrganization);

    }

    @Override
    public Organization addNewOrganization(Organization organization){
        return organizationJpaRepository.save(organization);
    }

    @Override
    public void deleteOrganization(Integer id) {
        organizationJpaRepository.deleteById(id);
    }

    @Override
    public String addDepartment(Integer organizationId, Organization departmentOrganization) {
        Optional<Organization> existsOrganization = organizationJpaRepository.findById(organizationId);
        String departmentEmail = departmentOrganization.getEmail();
        Optional<Organization> existDepartmentOrganization = organizationJpaRepository.findByEmail(departmentEmail);
        //this organization always exist because we add departments for that organization
        if(existsOrganization.isPresent()){
            Organization organization = existsOrganization.get();
            List<Organization> departments = organization.getDepartments();
            //if department do NOT exist
            if (!existDepartmentOrganization.isPresent()) {
                //create department like a organization
                Organization newDepartment = addNewOrganization(departmentOrganization);
                //check if department is already in organization departments
                if(!departments.contains(newDepartment)){
                    departments.add(newDepartment);
                }
                organization.setDepartments(departments);
                organizationJpaRepository.save(organization);
                return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
            } else {
                //if department that we want to add to organization already exists in database we check is in departments of organization if not we add to organizations_collageue
                //check if department is already in organization departments
                Organization newDepartment = existDepartmentOrganization.get();
                if(!departments.contains(newDepartment)){
                    departments.add(newDepartment);
                }
                organization.setDepartments(departments);
                organizationJpaRepository.save(organization);
                return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
            }
        }
        else {
            return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
        }
    }

    @Override
    public String addSubOrganizationOrganization(Integer organizationId, Organization subOrganizationOrganization) {
        Optional<Organization> existsOrganization = organizationJpaRepository.findById(organizationId);
        String subOrganizationEmail = subOrganizationOrganization.getEmail();
        Optional<Organization> existSubOrganizationOrganization = organizationJpaRepository.findByEmail(subOrganizationEmail);
        //this organization always exist because we add subOrganizations for that organization
        if(existsOrganization.isPresent()){
            Organization organization = existsOrganization.get();
            List<Organization> subOrganizations = organization.getSubOrganization();
            //if subOrganization do NOT exist
            if (!existSubOrganizationOrganization.isPresent()) {
                //create subOrganization like a organization
                Organization newSubOrganization = addNewOrganization(subOrganizationOrganization);
                //check if subOrganization is already in organization subOrganizations
                if(!subOrganizations.contains(newSubOrganization)){
                    subOrganizations.add(newSubOrganization);
                }
                organization.setSubOrganization(subOrganizations);
                organizationJpaRepository.save(organization);
                return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
            } else {
                //if subOrganization that we want to add to organization already exists in database we check is in subOrganizations of organization if not we add to organizations_collageue
                //check if subOrganization is already in organization subOrganizations
                Organization newSubOrganization = existSubOrganizationOrganization.get();
                if(!subOrganizations.contains(newSubOrganization)){
                    subOrganizations.add(newSubOrganization);
                }
                organization.setSubOrganization(subOrganizations);
                organizationJpaRepository.save(organization);
                return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
            }
        }
        else {
            return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
        }
    }

    @Override
    public String addMemberOfOrganization(Integer organizationId, Organization memberOfOrganization) {
        Optional<Organization> existsOrganization = organizationJpaRepository.findById(organizationId);
        String memberOfOrganizationEmail = memberOfOrganization.getEmail();
        Optional<Organization> existMemberOfOrganizationOrganization = organizationJpaRepository.findByEmail(memberOfOrganizationEmail);
        //this organization always exist because we add memberOfOrganizations for that organization
        if(existsOrganization.isPresent()){
            Organization organization = existsOrganization.get();
            List<Organization> memberOfOrganizations = organization.getMemberOf_organization();
            //if memberOfOrganization do NOT exist
            if (!existMemberOfOrganizationOrganization.isPresent()) {
                //create memberOfOrganization like a organization
                Organization newMemberOfOrganization = addNewOrganization(memberOfOrganization);
                //check if memberOfOrganization is already in organization memberOfOrganizations
                if(!memberOfOrganizations.contains(newMemberOfOrganization)){
                    memberOfOrganizations.add(newMemberOfOrganization);
                }
                organization.setMemberOf_organization(memberOfOrganizations);
                organizationJpaRepository.save(organization);
                return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
            } else {
                //if memberOfOrganization that we want to add to organization already exists in database we check is in memberOfOrganizations of organization if not we add to organizations_collageue
                //check if memberOfOrganization is already in organization memberOfOrganizations
                Organization newMemberOfOrganization = existMemberOfOrganizationOrganization.get();
                if(!memberOfOrganizations.contains(newMemberOfOrganization)){
                    memberOfOrganizations.add(newMemberOfOrganization);
                }
                organization.setMemberOf_organization(memberOfOrganizations);
                organizationJpaRepository.save(organization);
                return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
            }
        }
        else {
            return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
        }
    }

    @Override
    public String addParentOfOrganization(Integer organizationId, Organization parentOfOrganization) {
        Optional<Organization> existsOrganization = organizationJpaRepository.findById(organizationId);
        String parentOfOrganizationEmail = parentOfOrganization.getEmail();
        Optional<Organization> existParentOfOrganization = organizationJpaRepository.findByEmail(parentOfOrganizationEmail);
        //this organization always exist because we add parentOfOrganizations for that organization
        if(existsOrganization.isPresent()){
            Organization organization = existsOrganization.get();
            //if parentOfOrganization do NOT exist
            if (!existParentOfOrganization.isPresent()) {
                //create parentOfOrganization like a organization
                Organization newParentOfOrganization = addNewOrganization(parentOfOrganization);
                organization.setParentOrganization(newParentOfOrganization);

                organizationJpaRepository.save(organization);
                return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
            } else {
                //if parentOfOrganization that we want to add to organization already exists in database we check is in parentOfOrganizations of organization if not we add to organizations_collageue
                Organization newParentOfOrganization = existParentOfOrganization.get();
                organization.setParentOrganization(newParentOfOrganization);
                organizationJpaRepository.save(organization);
                return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
            }
        }
        else {
            return "redirect:/organizations/showFormForUpdate?organizationId=" + organizationId;
        }
    }
}
