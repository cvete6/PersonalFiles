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
        model.addAttribute("organization", organization);
    }

    @Override
    public Organization addNewOrganization(Organization organization){
        return organizationJpaRepository.save(organization);
    }
}
