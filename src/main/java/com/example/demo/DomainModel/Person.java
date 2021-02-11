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

import javax.validation.constraints.Size;
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
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 50)
    private String givenName;

    @NotEmpty(message = "Last Name cannot be empty")
    @Size(max = 50)
    private String familyName;

    @Size(max = 50)
    private String additionalName;

    //@NotEmpty(message = "Address cannot be empty")
    private String address;

    private String award;

    @Column(unique = true)
    @NotEmpty(message = "Social number cannot be empty")
   // @Size(min = 9)
    private String socialNumber;

    private String callSign;

    private String contactPoint;

    //@NotNull(message = "Date of birth cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    //@NotEmpty(message = "Place of birth cannot be empty")
    private String birthPlace;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deathDate;

    private String deathPlace;

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    private String faxNumber;

    private String gender;

    private String globalLocationNumber;

    private Integer height;

    private String homeLocation;

    //(Dr/Mrs/Mr.)
    private String honorificPrefix;

    //( M.D. /PhD/MSCSW.)
    private String honorificSuffix;

    private String jobTitle;

    //(a topic that i known about , job description)
    private String knowsAbout;

    private String knowsLanguage;

    private String nationality;

    //event or participant in
    private String performerIn;

    //blog for a person url
    private String publishingPrinciples;

    private String seeks;

    private String taxID;

    private String telephone;

    private Integer weight;

    private String workLocation;

    //@NotEmpty(message = "Passport Number cannot be empty")
    private String passportNumber;

  //  @NotNull(message = "Date of issue passport cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfIssuePassport;

//    @NotNull(message = "Date of expiry passport cannot be empty")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfExpiryPassport;

    @Lob
    private byte[] image;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                          CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "children_id")
    private List<Person> children;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                          CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "colleague_id")
    private List<Person> colleague;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                          CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "parent_id")
    private List<Person> parent;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                         CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "spouse_id")
    private Person spouse;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                          CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "follows_id")
    private List<Person> follows;

    @JsonIgnore
    @OneToMany( cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                           CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "knows_id")
    private List<Person> knows;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                          CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "organization_sponsor_id")
    private Organization organization_sponsor;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                          CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "organization_worksIn_id")
    private Organization worksFor;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                           CascadeType.MERGE,CascadeType.DETACH})
    @JoinTable(name="person_organization",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private List<Organization> memberOf;

}
