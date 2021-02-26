package com.example.demo.SecurityOAuth;

import com.example.demo.DomainModel.AuthenticationProvider;
import com.example.demo.DomainModel.Person;
import com.example.demo.Service.PersonServiceImpl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private PersonServiceImpl personService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String name = oAuth2User.getName();
        Person person = personService.getPersonByEmail(email);
        if( person == null ){
            //register as a new person
            personService.addNewPersonAfterOAuthLoginSuccess(email, name, AuthenticationProvider.GOOGLE );
        }else{
            //update existing person
            personService.updateCustomerAfterOAuthLoginSuccess(person, name, AuthenticationProvider.GOOGLE);
        }

        //System.out.println("Customers email: " + email);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
