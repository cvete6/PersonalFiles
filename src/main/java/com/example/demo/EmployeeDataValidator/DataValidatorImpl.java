package com.example.demo.EmployeeDataValidator;

import com.itextpdf.forms.fields.PdfFormField;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

@Component
public class DataValidatorImpl implements DataValidator {

    @Override
    public ArrayList<String> validatePdfForm(Map<String, PdfFormField> fields) {
        String name = fields.get("givenName").getValueAsString();
        String lastName = fields.get("familyName").getValueAsString();
        String address = fields.get("address").getValueAsString();
        String socialNumber = fields.get("socialNumber").getValueAsString();
        String dateOfBirth = fields.get("dateOfBirth").getValueAsString();
        String placeOfBirth = fields.get("placeOfBirth").getValueAsString();
        String email = fields.get("email").getValueAsString();
        String jobTitle = fields.get("jobTitle").getValueAsString();
        String dateOfExpiryPassport = fields.get("dateOfExpiryPassport").getValueAsString();
        String dateOfIssuePassport = fields.get("dateOfIssuePassport").getValueAsString();
        String passportNumber = fields.get("passportNumber").getValueAsString();

        //check if some fields are empty and if they are add them to empty list to know which one is empty
        ArrayList<String> emptyFieldsList = new ArrayList<>();

        if (name.isEmpty()) {
            emptyFieldsList.add("Name");
        }
        if (lastName.isEmpty()) {
            emptyFieldsList.add("Last Name");
        }
        if (address.isEmpty()) {
            emptyFieldsList.add("Address");
        }
        if (placeOfBirth.isEmpty()) {
            emptyFieldsList.add("Place Of Birth");
        }
        if (email.isEmpty()) {
            emptyFieldsList.add("Email");
        }
        if (jobTitle.isEmpty()) {
            emptyFieldsList.add("Job Title");
        }
        if (passportNumber.isEmpty()) {
            emptyFieldsList.add("Passport number");
        }
        if (socialNumber.isEmpty()) {
            emptyFieldsList.add("Social number");
        }
        if (dateOfBirth.isEmpty() || !isDateValid(dateOfBirth)) {
            emptyFieldsList.add("Date of birth");
        }
        if (dateOfExpiryPassport.isEmpty() || !isDateValid(dateOfExpiryPassport)) {
            emptyFieldsList.add("Date of expiry passport");
        }

        if (dateOfIssuePassport.isEmpty() || !isDateValid(dateOfIssuePassport)) {
            emptyFieldsList.add("Date of issue passport");
        }
        return emptyFieldsList;
    }

    @Override
    public boolean isDateValid(String dateString) {
        String dateFormat = "dd-MM-yyyy";
        DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateString);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
