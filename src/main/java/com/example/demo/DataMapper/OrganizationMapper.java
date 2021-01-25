package com.example.demo.DataMapper;

import com.example.demo.DomainModel.Organization;

public class OrganizationMapper {

    public static Organization oldOrganizationMapToNewOrganization (Organization organization, Organization oldOrganization){
        oldOrganization.setId(organization.getId());
        oldOrganization.setAddress(organization.getAddress());
        oldOrganization.setAreaServed(organization.getAreaServed());
        oldOrganization.setAward(organization.getAward());
        oldOrganization.setBrand(organization.getBrand());
        oldOrganization.setContactPoint(organization.getContactPoint());
        oldOrganization.setCorrectionsPolicy(organization.getCorrectionsPolicy());
        oldOrganization.setDissolutionDate(organization.getDissolutionDate());
        oldOrganization.setEmail(organization.getEmail());
        oldOrganization.setFaxNumber(organization.getFaxNumber());
        oldOrganization.setFoundingDate(organization.getFoundingDate());
        oldOrganization.setFoundingLocation(organization.getFoundingLocation());
        oldOrganization.setGlobalLocationNumber(organization.getGlobalLocationNumber());
        oldOrganization.setKnowsAbout(organization.getKnowsAbout());
        oldOrganization.setLegalName(organization.getLegalName());
        oldOrganization.setLocation(organization.getLocation());
        oldOrganization.setLogo(organization.getLogo());
        oldOrganization.setNonprofitStatus(organization.getNonprofitStatus());
        oldOrganization.setNumberOfEmployees(100);
        oldOrganization.setOwnershipFundingInfo(organization.getOwnershipFundingInfo());
        oldOrganization.setPublishingPrinciples(organization.getPublishingPrinciples());
        oldOrganization.setSlogan(organization.getSlogan());
        oldOrganization.setTelephone(organization.getTelephone());

        return oldOrganization;
    }
}
