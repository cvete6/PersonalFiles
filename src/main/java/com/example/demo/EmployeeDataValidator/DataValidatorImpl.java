package com.example.demo.EmployeeDataValidator;

import com.itextpdf.forms.fields.PdfFormField;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class DataValidatorImpl implements DataValidator {

    @Override
    public ArrayList<String> validatePdfForm(Map<String, PdfFormField> fields) {
        String name = fields.get("givenName").getValueAsString();
        String lastName = fields.get("familyName").getValueAsString();
        String socialNumber = fields.get("socialNumber").getValueAsString();
        String email = fields.get("email").getValueAsString();

        //check if some fields are empty and if they are add them to empty list to know which one is empty
        ArrayList<String> emptyFieldsList = new ArrayList<>();

        if (name.isEmpty()) {
            emptyFieldsList.add("Name");
        }
        if (lastName.isEmpty()) {
            emptyFieldsList.add("Last Name");
        }
        if (email.isEmpty()) {
            emptyFieldsList.add("Email");
        }

        if (socialNumber.isEmpty()) {
            emptyFieldsList.add("Social number");
        }

        return emptyFieldsList;
    }
}
