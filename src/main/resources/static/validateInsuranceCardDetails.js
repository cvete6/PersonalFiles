function validateInsuranceCardDetails(){
var errorMessageForInsuranceCard = document.getElementById('errorMessageForInsuranceCard')
errorMessageForInsuranceCard.hidden=true;
var dateOfExpiryInsuranceCard = document.getElementById("dateOfExpiryInsuranceCard");
var insuranceCardNumber = document.getElementById("insuranceCardNumber");
    if (dateOfExpiryInsuranceCard.value !== '' && insuranceCardNumber.value === ''){
        insuranceCardNumber.required=true;
        insuranceCardNumber.style.borderColor = "red";
        errorMessageForInsuranceCard.hidden=false;
    }
    else if (dateOfExpiryInsuranceCard.value === '' && insuranceCardNumber.value !== ''){
        dateOfExpiryInsuranceCard.required=true;
        dateOfExpiryInsuranceCard.style.borderColor = "red";
        errorMessageForInsuranceCard.hidden=false;
    }
    else if(dateOfExpiryInsuranceCard.value === '' && insuranceCardNumber.value === ''){
        insuranceCardNumber.required=false;
        dateOfExpiryInsuranceCard.required=false;
        errorMessageForInsuranceCard.hidden=true;
    }
};
