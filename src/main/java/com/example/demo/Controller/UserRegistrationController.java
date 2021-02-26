package com.example.demo.Controller;


import com.example.demo.Controller.dto.UserRegistrationDto;
import com.example.demo.DomainModel.Person;
import com.example.demo.Service.PersonServiceImpl.PersonServiceImpl;
import com.example.demo.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    private UserService userService;

    private PersonServiceImpl personService;

    public UserRegistrationController(UserService userService, PersonServiceImpl personService) {
        super();
        this.userService = userService;
        this.personService = personService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "authentication/registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        userService.save(registrationDto);
        //when register a new person add this person to database
        Person person = new Person();
        person.setGivenName(registrationDto.getFirstName());
        person.setEmail(registrationDto.getEmail());
        person.setFamilyName("empty");
        person.setSocialNumber("000000000");
        personService.addNewPerson(person);

        return "redirect:/registration?success";
    }
}