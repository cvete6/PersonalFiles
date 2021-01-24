package com.example.demo.Service;

import com.example.demo.DomainModel.Person;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface PersonService {

    /**
     * Get list from persons
     *
     * @return list of all persons
     */
    List<Person> getAllPersons();

    /**
     * Find person by id
     *
     * @param id person identifier
     * @return data for person with that id
     */
    Person getPersonById(Integer id);

    /**
     * Find if there is a search keyword and list all items according to keyword and list
     * persons in different pages with a fixed number of elements in one page
     *
     * @param page    that currently we are located
     * @param keyword word that we want to make filtered on persons
     * @param model   add pages characteristics to model, total pages, total items,
     * @return page with persons
     */
    Page<Person> getPaginatedPersons(int page, String keyword, Model model);

    /**
     * Delete person from database
     *
     * @param id person identifier
     */
    void deletePerson(Integer id);

    /**
     * Return person that has the same Social number as a person that we pass as a parameter
     *
     * @param person the person created from data in the uploaded pdf file
     * @return if the person exists in the database return person
     */
    Optional<Person> findPersonBySocialNumber(Person person);

    /**
     * Add new person
     *
     * @param person person object witch we want to save
     * @return newly created person
     */
    Person addNewPerson(Person person);

    /**
     * Edit existed person
     *
     * @param person person with editable field
     * @return edited person
     */
    Person editPerson(Person person);

    /**
     * Convert MultipartFile to File
     *
     * @param multipartPdfFile file that we upload
     * @return converted file
     * @throws IOException
     */
    File convertMultipartFileToFile(MultipartFile multipartPdfFile) throws IOException;

    /**
     * Get data from pdf form file validate and create new Person or return exist one
     *
     * @param uploadedMultipartPdfFile pdf file
     * @return created person from data in pdf form
     * @throws IOException
     * @throws ParseException
     */
    String validateAndCreateEmployee(MultipartFile uploadedMultipartPdfFile, Model model) throws IOException,
            ParseException;

    /**
     * Add new person in database or return exists and redirect to edit person view
     *
     * @param person the person created from data in the uploaded pdf file
     * @param model  if person already exists in database then add personId like an attribute to the model
     * @return string to redirect to appropriate view
     */
    String redirectToPersonDetailsView(Person person, Model model);


    /**
     * Find person with personId and send model attribute to view
     *
     * @param personId id from the person that we want to send data to view
     * @param model    add person data to model
     */
    void sendDataToEditModelView(Integer personId, Model model);

}
