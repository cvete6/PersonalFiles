package com.example.demo.DomainModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * Person is the main model in the application that store all attributes for one person
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Address cannot be empty")
    private String address;

    private String areaServed;

    private String award;

    private String brand;

    private String contactPoint;

    //vidi ova so e
    private String correctionsPolicy;

    //Odeli od koi e sostavena organnizacijata koi i tie se organizacii
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "department_id")
    private List<Organization> departments;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dissolutionDate;

    @NotEmpty(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    private String faxNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "worksFor")
    private List<Person> employee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date foundingDate;

    private String foundingLocation;

    private String globalLocationNumber;

    private String knowsAbout;

    @NotEmpty(message = "Legal Name cannot be empty")
    private String legalName;

    private String location;

    private String logo;

    private String nonprofitStatus;

    private Integer numberOfEmployees;

    private String ownershipFundingInfo;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.DETACH})
    private Organization parentOrganization;

    //ova e url nekoe koe ne nosi na javnite principi na organizacijata
    private String publishingPrinciples;

    //Slogan ili moto na organizacijata
    private String slogan;

    @OneToMany(mappedBy = "organization_sponsor",
               cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                          CascadeType.MERGE,CascadeType.DETACH})
    private List<Person> sponsors;

    private String telephone;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "subOrganization_id")
    private List<Organization> subOrganization;

    // da bidi clen na nekoja druga organizacija
    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "memberOf_organization_id")
    private List<Organization> memberOf_organization;

    @ManyToMany
    @JoinTable(name="organization_person",
               joinColumns = @JoinColumn(name = "organization_id"),
                inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> person_members;


}
