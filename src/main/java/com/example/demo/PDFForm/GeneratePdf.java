package com.example.demo.PDFForm;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.IOException;

import static com.itextpdf.kernel.colors.ColorConstants.LIGHT_GRAY;
import static com.itextpdf.kernel.colors.ColorConstants.RED;
import static com.itextpdf.kernel.pdf.action.PdfAction.createJavaScript;

/**
 * Generate PDF document
 */
public class GeneratePdf {

    public static final String destinationFile = "PdfFormForEmployment.pdf";

    /**
     * Initialize new pdf document
     *
     * @throws IOException File not found Exception
     */
    public static void generatePDF() throws IOException {

        //Initialize PDF document
        PdfWriter writer = new PdfWriter(destinationFile);
        PdfDocument pdf = new PdfDocument(writer);
        PageSize ps = PageSize.A4;
        pdf.setDefaultPageSize(ps);

        // Initialize document
        Document document = new Document(pdf);

        GeneratePdf.addAcroForm(document);
        //Close document
        document.close();
    }

    /**
     * Creating all elements of the document
     *
     * @param document blank created document
     */
    private static void addAcroForm(Document document) {

        //Add acroform
        PdfAcroForm form = PdfAcroForm.getAcroForm(document.getPdfDocument(), true);

        Paragraph title = new Paragraph("Personal Profile\n")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18);
        document.add(title);

        Text textInfoMessage = new Text("(Please enter a value)");
        textInfoMessage.setFontSize(6);
        textInfoMessage.setTextAlignment(TextAlignment.LEFT);
        textInfoMessage.setRelativePosition(0, 7, 0, 10);
        textInfoMessage.setFontColor(LIGHT_GRAY);

        Text dateInfoMessage = new Text("(Please enter the date in the format dd-mm-yyyy)");
        dateInfoMessage.setFontSize(6);
        dateInfoMessage.setTextAlignment(TextAlignment.CENTER);
        dateInfoMessage.setRelativePosition(0, 7, 0, 10);
        dateInfoMessage.setFontColor(LIGHT_GRAY);



        //Create javascript
        PdfAction actionValidateDate = createJavaScript(
                "var date = event.value; " +
                        "var dateFormat=/(0[1-9]|[12][0-9]|3[01])[-](0[1-9]|1[012])[-](19|20)\\d\\d/;" +
                        "if(date.match(dateFormat)) {" +
                        "event.value=date;" +
                        "}" +
                        "else {" +
                        "event.value=\"\";" +
                        "}"
        );

        Paragraph nameParagraph = new Paragraph("Given name:");
        addParagraphCharacteristic(nameParagraph, textInfoMessage);
        document.add(nameParagraph);

        //Create text field
        PdfTextFormField nameField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 753, 240, 15), "givenName");
        nameField.setFontSize(10);
        form.addField(nameField);

        Paragraph lastNameParagraph = new Paragraph("Family Name:");
        addParagraphCharacteristic(lastNameParagraph, textInfoMessage);
        document.add(lastNameParagraph);

        PdfTextFormField lastNameField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 727, 240, 15), "familyName");
        lastNameField.setFontSize(10);
        form.addField(lastNameField);

        Paragraph additionalNameParagraph = new Paragraph("Additional name:");
        addParagraphCharacteristic(additionalNameParagraph, textInfoMessage);
        document.add(additionalNameParagraph);

        PdfTextFormField additionalNameField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 701, 240, 15), "additionalName");
        additionalNameField.setFontSize(10);
        form.addField(additionalNameField);

        Paragraph addressParagraph = new Paragraph("Address:");
        addParagraphCharacteristic(addressParagraph, textInfoMessage);
        document.add(addressParagraph);

        PdfTextFormField addressField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 675, 240, 15), "address");
        addressField.setFontSize(10);
        form.addField(addressField);


        Paragraph awardParagraph = new Paragraph("Award:");
        addParagraphCharacteristic(awardParagraph, textInfoMessage);
        document.add(awardParagraph);

        PdfTextFormField awardField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 649, 240, 15), "award");
        awardField.setFontSize(10);
        form.addField(awardField);

        Paragraph socialNumberParagraph = new Paragraph("Social number:");
        addParagraphCharacteristic(socialNumberParagraph, textInfoMessage);
        document.add(socialNumberParagraph);

        PdfTextFormField socialNumberField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 623, 240, 15), "socialNumber");
        socialNumberField.setFontSize(10);
        form.addField(socialNumberField);

        Paragraph callSignParagraph = new Paragraph("Call sign:");
        addParagraphCharacteristic(callSignParagraph, textInfoMessage);
        document.add(callSignParagraph);

        PdfTextFormField callSignField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 597, 240, 15), "callSign");
        callSignField.setFontSize(10);
        form.addField(callSignField);

        Paragraph contactPointParagraph = new Paragraph("Contact point:");
        addParagraphCharacteristic(contactPointParagraph, textInfoMessage);
        document.add(contactPointParagraph);

        PdfTextFormField contactPointField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 571, 240, 15), "contactPoint");
        contactPointField.setFontSize(10);
        form.addField(contactPointField);

        Paragraph dateOfBirthParagraph = new Paragraph("Date of birth:");
        addParagraphCharacteristic(dateOfBirthParagraph, dateInfoMessage);
        document.add(dateOfBirthParagraph);

        PdfTextFormField dateOfBirthField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 545, 240, 15), "dateOfBirth");
        dateOfBirthField.setFontSize(10);
        dateOfBirthField.setAdditionalAction(PdfName.V, actionValidateDate);
        form.addField(dateOfBirthField);

        Paragraph placeOfBirthParagraph = new Paragraph("Place of birth:");
        addParagraphCharacteristic(placeOfBirthParagraph, textInfoMessage);
        document.add(placeOfBirthParagraph);

        PdfTextFormField placeOfBirthField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 519, 240, 15), "placeOfBirth");
        placeOfBirthField.setFontSize(10);
        form.addField(placeOfBirthField);

        Paragraph dateOfDeathParagraph = new Paragraph("Date of death:");
        addParagraphCharacteristic(dateOfDeathParagraph, dateInfoMessage);
        document.add(dateOfDeathParagraph);

        PdfTextFormField dateOfDeathField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 493, 240, 15), "dateOfDeath");
        dateOfDeathField.setFontSize(10);
        dateOfDeathField.setAdditionalAction(PdfName.V, actionValidateDate);
        form.addField(dateOfDeathField);

        Paragraph placeOfDeathParagraph = new Paragraph("Place of birth:");
        addParagraphCharacteristic(placeOfDeathParagraph, textInfoMessage);
        document.add(placeOfDeathParagraph);

        PdfTextFormField placeOfDeathField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 467, 240, 15), "placeOfDeath");
        placeOfDeathField.setFontSize(10);
        form.addField(placeOfDeathField);

        Paragraph emailParagraph = new Paragraph("Email:");
        addParagraphCharacteristic(emailParagraph, textInfoMessage);
        document.add(emailParagraph);

        PdfTextFormField emailField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 441, 240, 15), "email");
        emailField.setFontSize(10);
        form.addField(emailField);

        Paragraph faxNumberParagraph = new Paragraph("Fax number:");
        addParagraphCharacteristic(faxNumberParagraph, textInfoMessage);
        document.add(faxNumberParagraph);

        PdfTextFormField faxNumberField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 415, 240, 15), "faxNumber");
        faxNumberField.setFontSize(10);
        form.addField(faxNumberField);

        Paragraph genderParagraph = new Paragraph("Gender:");
        addParagraphCharacteristic(genderParagraph, textInfoMessage);
        document.add(genderParagraph);

        PdfTextFormField genderField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 389, 240, 15), "gender");
        genderField.setFontSize(10);
        form.addField(genderField);

        Paragraph globalLocationNumberParagraph = new Paragraph("Global Location Number:");
        addParagraphCharacteristic(globalLocationNumberParagraph, textInfoMessage);
        document.add(globalLocationNumberParagraph);

        PdfTextFormField globalLocationNumberField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 363, 240, 15), "globalLocationNumber");
        globalLocationNumberField.setFontSize(10);
        form.addField(globalLocationNumberField);

        Paragraph heightParagraph = new Paragraph("Height:");
        addParagraphCharacteristic(heightParagraph, textInfoMessage);
        document.add(heightParagraph);

        PdfTextFormField heightField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 337, 240, 15), "height");
        heightField.setFontSize(10);
        form.addField(heightField);

        Paragraph homeLocationParagraph = new Paragraph("Home Location:");
        addParagraphCharacteristic(homeLocationParagraph, textInfoMessage);
        document.add(homeLocationParagraph);

        PdfTextFormField homeLocationField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 311, 240, 15), "homeLocation");
        homeLocationField.setFontSize(10);
        form.addField(homeLocationField);

        Paragraph honorificPrefixParagraph = new Paragraph("Honorific Prefix:");
        addParagraphCharacteristic(honorificPrefixParagraph, textInfoMessage);
        document.add(honorificPrefixParagraph);

        PdfTextFormField honorificPrefixField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 285, 240, 15), "honorificPrefix");
        honorificPrefixField.setFontSize(10);
        form.addField(honorificPrefixField);

        Paragraph honorificSuffixParagraph = new Paragraph("Honorific Suffix:");
        addParagraphCharacteristic(honorificSuffixParagraph, textInfoMessage);
        document.add(honorificSuffixParagraph);

        PdfTextFormField honorificSuffixField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 259, 240, 15), "honorificSuffix");
        honorificSuffixField.setFontSize(10);
        form.addField(honorificSuffixField);

        Paragraph jobTitleParagraph = new Paragraph("Job title:");
        addParagraphCharacteristic(jobTitleParagraph, textInfoMessage);
        document.add(jobTitleParagraph);

        PdfTextFormField jobTitleField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 233, 240, 15), "jobTitle");
        jobTitleField.setFontSize(10);
        form.addField(jobTitleField);

        Paragraph knowsAboutParagraph = new Paragraph("Knows About:");
        addParagraphCharacteristic(knowsAboutParagraph, textInfoMessage);
        document.add(knowsAboutParagraph);

        PdfTextFormField knowsAboutField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 207, 240, 15), "knowsAbout");
        knowsAboutField.setFontSize(10);
        form.addField(knowsAboutField);

        Paragraph knowsLanguageParagraph = new Paragraph("Knows Language:");
        addParagraphCharacteristic(knowsLanguageParagraph, textInfoMessage);
        document.add(knowsLanguageParagraph);

        PdfTextFormField knowsLanguageField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 181, 240, 15), "knowsLanguage");
        knowsLanguageField.setFontSize(10);
        form.addField(knowsLanguageField);

        Paragraph nationalityParagraph = new Paragraph("Nationality:");
        addParagraphCharacteristic(nationalityParagraph, textInfoMessage);
        document.add(nationalityParagraph);

        PdfTextFormField nationalityField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 155, 240, 15), "nationality");
        nationalityField.setFontSize(10);
        form.addField(nationalityField);

        Paragraph performInParagraph = new Paragraph("Perform In:");
        addParagraphCharacteristic(performInParagraph, textInfoMessage);
        document.add(performInParagraph);

        PdfTextFormField performInField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 129, 240, 15), "performIn");
        performInField.setFontSize(10);
        form.addField(performInField);

        Paragraph publishingPrincipleParagraph = new Paragraph("Publishing Principles:");
        addParagraphCharacteristic(publishingPrincipleParagraph, textInfoMessage);
        document.add(publishingPrincipleParagraph);

        PdfTextFormField publishingPrinciplesField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 103, 240, 15), "publishingPrinciples");
        publishingPrinciplesField.setFontSize(10);
        form.addField(publishingPrinciplesField);

        Paragraph seekParagraph = new Paragraph("Seek:");
        addParagraphCharacteristic(seekParagraph, textInfoMessage);
        document.add(seekParagraph);

        PdfTextFormField seekField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 77 , 240, 15), "seek");
        seekField.setFontSize(10);
        form.addField(seekField);

        Paragraph taxIdParagraph = new Paragraph("TaxId:");
        addParagraphCharacteristic(taxIdParagraph, textInfoMessage);
        document.add(taxIdParagraph);

        PdfTextFormField taxIDField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 51, 240, 15), "taxID");
        taxIDField.setFontSize(10);
        form.addField(taxIDField);

        Paragraph telephoneParagraph = new Paragraph("Telephone:");
        addParagraphCharacteristic(telephoneParagraph, textInfoMessage);
        document.add(telephoneParagraph);

        PdfTextFormField telephoneField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 779, 240, 15), "telephone");
        telephoneField.setFontSize(10);
        form.addField(telephoneField);

        Paragraph weightParagraph = new Paragraph("Weight:");
        addParagraphCharacteristic(weightParagraph, textInfoMessage);
        document.add(weightParagraph);

        PdfTextFormField weightField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 753, 240, 15), "weight");
        weightField.setFontSize(10);
        form.addField(weightField);


        Paragraph workLocationParagraph = new Paragraph("Work Location:");
        addParagraphCharacteristic(workLocationParagraph, textInfoMessage);
        document.add(workLocationParagraph);

        PdfTextFormField workLocationField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 727, 240, 15), "workLocation");
        workLocationField.setFontSize(10);
        form.addField(workLocationField);

        Paragraph passportNumberParagraph = new Paragraph("Passport number:");
        addParagraphCharacteristic(passportNumberParagraph, textInfoMessage);
        document.add(passportNumberParagraph);

        PdfTextFormField passportNumberField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 701, 240, 15), "passportNumber");
        passportNumberField.setFontSize(10);
        form.addField(passportNumberField);

        Paragraph dateOfIssuePassportParagraph = new Paragraph("Date of issue passport:");
        addParagraphCharacteristic(dateOfIssuePassportParagraph, dateInfoMessage);
        document.add(dateOfIssuePassportParagraph);

        PdfTextFormField dateOfIssuePassportField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 675, 240, 15), "dateOfIssuePassport");
        //"Please enter the date in the format dd-mm-yyyy"
        dateOfIssuePassportField.setFontSize(10);
        dateOfIssuePassportField.setAdditionalAction(PdfName.V, actionValidateDate);
        form.addField(dateOfIssuePassportField);

        Paragraph dateOfExpireParagraph = new Paragraph("Date of expiry passport:");
        addParagraphCharacteristic(dateOfExpireParagraph, dateInfoMessage);
        document.add(dateOfExpireParagraph);

        PdfTextFormField dateOfExpiryPassportField = PdfTextFormField.createText(document.getPdfDocument(),
                new Rectangle(170, 649, 240, 15), "dateOfExpiryPassport");
        dateOfExpiryPassportField.setFontSize(10);
        dateOfExpiryPassportField.setAdditionalAction(PdfName.V, actionValidateDate);
        form.addField(dateOfExpiryPassportField);

        document.add(new Paragraph("").setFontSize(18));
        document.add(new Paragraph("*(This field is required)").setTextAlignment(TextAlignment.RIGHT).setFontSize(6));

        //Create push button field
        PdfButtonFormField button = PdfFormField.createPushButton(document.getPdfDocument(),
                new Rectangle(479, 545, 45, 15), "reset", "RESET");
        button.setAction(PdfAction.createResetForm(
                new String[] {"name", "lastName", "address", "dateOfBirth", "placeOfBirth", "socialNumber",
                        "municipality",
                        "identityCardNumber", "passportNumber",
                        "dateOfIssuePassport", "dateOfExpiryPassport", "education",
                        "transactionAccountNumber",
                        "nameOfBank"}, 0));
        form.addField(button);
    }

    private static void addParagraphCharacteristic(Paragraph paragraph, Text infoMessage) {

        TabStop tabStopInfoMessage = new TabStop(140, TabAlignment.LEFT);
        TabStop starTabStop = new TabStop(385, TabAlignment.CENTER);

        Text star = new Text("*");
        star.setFontColor(RED);
        star.setFontSize(8);
        paragraph.add(new Tab())
                .addTabStops(tabStopInfoMessage)
                .add(infoMessage)
                .add(new Tab())
                .addTabStops(starTabStop)
                .add(star)
                .setFontSize(12);
    }
}
