package com.example.demo.DomainModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    @Size(min = 9)
    private String socialNumber;

    private String callSign;

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

    @JsonIgnore
    @ManyToMany
    private List<Person> follows;

    private String gender;

    private String globalLocationNumber;

    private Integer height;

    private String homeLocation;

    //(Dr/Mrs/Mr.)
    private String honorificPrefix;

    //( M.D. /PhD/MSCSW.)
    private String honorificSuffix;

    private String jobTitle;

    @JsonIgnore
    @ManyToMany
    private List<Person> knows;

    //(a topic that i known about , job description)
    private String knowsAbout;

    private String knowsLanguage;

    private String nationality;

    @ManyToOne
    private Organization funder_organization;

    @JsonIgnore
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.DETACH})
    @JoinColumn(name = "parent_id")
    private List<Person> parent;

    //event or participant in
    private String performerIn;

    //blog for a person url
    private String publishingPrinciples;

    private String seeks;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization_sponzor;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
            CascadeType.MERGE,CascadeType.DETACH})
    private Person spouse;

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

    @ManyToMany
    @JoinTable(name="organization_person",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id"))
    private List<Organization> organization_members;

}
