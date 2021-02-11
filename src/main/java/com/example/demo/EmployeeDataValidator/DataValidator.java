package com.example.demo.EmployeeDataValidator;

import com.itextpdf.forms.fields.PdfFormField;

import java.util.ArrayList;
import java.util.Map;

/**
 * Validate if all input fields in employment pdf form file is complete
 */
public interface DataValidator {

    /**
     * Check that each field is filled in and that the date is in a valid format
     *
     * @param fields input fields from employmentPdfForm file
     * @return list with the name of all invalid fields or return an empty list if everything is ok
     */
    ArrayList<String> validatePdfForm(Map<String, PdfFormField> fields);

    /**
     *Check if string is in valid date format
     *
     * @param dateString date in string type
     * @return if string is valid date return false else return true
     */
   // boolean isDateValid(String dateString);
}