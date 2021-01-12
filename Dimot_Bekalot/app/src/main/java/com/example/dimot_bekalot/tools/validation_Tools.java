package com.example.dimot_bekalot.tools;
/**
 * This class contains help tools validation
 * functions to the registers aka Patient or Institute
 */

import android.text.TextUtils;
import android.widget.EditText;

public class validation_Tools {

    /**
     * This function check if the input at Login Activity is valid and correct
     *
     * @param userName
     * @param password
     * @param userName_Input
     * @param password_input
     * @return
     */
    public static boolean isLoginInputValid(String userName, String password, EditText userName_Input,
                                            EditText password_input) {

        if (TextUtils.isEmpty(userName)) {
            userName_Input.setError("שם משתמש הוא שדה חובה");
            return false;
        }

        if(userName.length() != 10){
            userName_Input.setError("שם המשתמש אמור להיות באורך 10");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            password_input.setError("סיסמא הוא שדה חובה");
            return false;
        }

        if (password.length() != 9) {
            password_input.setError("אורך הסיסמא חייב להיות 9 תווים");
            return false;
        }
        return true;
    }

    /**
     * This function make sure the patient fill his first and last name and id.
     *
     * @param firstName
     * @param lastName
     * @param patientId
     * @param first_nameInput
     * @param last_nameInput
     * @param patientID_input
     * @return
     */
    public static boolean isPatientNamesIsValid(String firstName, String lastName,
                                                String patientId, String age, EditText first_nameInput,
                                                EditText last_nameInput, EditText patientID_input,
                                                EditText ageInput) {

        if (TextUtils.isEmpty(firstName)) {
            first_nameInput.setError("שם פרטי הוא שדה חובה");
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            last_nameInput.setError("שם משפחה הוא שדה חובה");
            return false;
        }

        if (TextUtils.isEmpty(patientId)) {
            patientID_input.setError("תעודת זהות הוא שדה חובה");
            return false;
        }

        if (Long.valueOf(patientId) > 999999999 || Long.valueOf(patientId) < 100000000) {
            patientID_input.setError("ת.ז לא תקין");
            return false;
        }

        if (Long.valueOf(age) > 120) {
            ageInput.setError("גיל לא תקין");
            return false;
        }


        return true;
    }

    /**
     * This function make sure the patient fill his institute name, instituteID,
     * what city provided the services currently
     *
     * @param InstituteName
     * @param instituteID
     * @param cityLiving
     * @param cityInput
     * @param institute_nameInput
     * @param instituteID_Input
     * @return
     */
    public static boolean isInstituteNamesIsValid(String InstituteName, String instituteID,
                                                  String cityLiving, EditText cityInput,
                                                  EditText institute_nameInput, EditText instituteID_Input) {

        if (TextUtils.isEmpty(InstituteName)) {
            institute_nameInput.setError("שם פרטי המכון הוא שדה חובה");
            return false;
        }

        if (TextUtils.isEmpty(instituteID)) {
            instituteID_Input.setError("רשיון מכון הוא שדה חובה");
            return false;
        }

        if (TextUtils.isEmpty(cityLiving)) {
            cityInput.setError("עיר פעילות המכון הוא שדה חובה");
            return false;
        }

        return true;
    }

    /**
     * This function make sure the patient and the institute fill there email, password and
     * phone number currently
     * This function
     *
     * @param email
     * @param password
     * @param phone
     * @param emailInput
     * @param passwordInput
     * @param phone_numberInput
     * @return
     */
    public static boolean isAllCostumersNeedfulInputIsValid(String email, String password, String phone,
                                                            EditText emailInput, EditText passwordInput,
                                                            EditText phone_numberInput) {
        if (TextUtils.isEmpty(email)) {
            emailInput.setError("אי-מייל הוא שדה חובה");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("הסיסמא היא שדה חובה");
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            phone_numberInput.setError("מספר טלפון להתקשרות הוא שדה חובה");
            return false;
        }

        if (phone.length() != 10) {
            phone_numberInput.setError("מספר טלפון הנייד צריך להיות 10 ספרות");
            return false;
        }

        if (password.length() != 9) {
            passwordInput.setError("הכנס 9 תווים");
            return false;
        }

        return true;
    }

    /**
     * Checking if input of the password is valid or NOT
     * @param password
     * @param password_input
     * @return
     */
    public static boolean isForgetPasswordInputValid(String password, EditText password_input){
        if (TextUtils.isEmpty(password)) {
            password_input.setError("שדה זה הוא חובה");
            return false;
        }

        if (password_input.length() != 9) {
            password_input.setError("הסיסמא חייבת להיות באורך 9 תווים");
            return false;
        }

        return true;
    }

    /**
     * Checking if input email and userName is valid
     * @param userName_ID
     * @param email
     * @param userName_ID_input
     * @param email_input
     * @return
     */
    public static boolean isForgetPasswordInputValid_User_email(String userName_ID, String email,
                                                                EditText userName_ID_input ,EditText email_input){
        if (TextUtils.isEmpty(userName_ID)) {
            userName_ID_input.setError("שדה זה הוא חובה");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            email_input.setError("שדה זה הוא חובה");
            return false;
        }

        return true;
    }

    /**
     * Checking if input string is a long Or integer or not
     * @param Num
     * @param input_Num
     * @return
     */
    public static boolean CheckIfNumber(String Num, EditText input_Num) {
        if (TextUtils.isEmpty(Num)) {
            input_Num.setError("שדה זה הוא חובה");
            return false;
        }

        try {
            Long.parseLong(Num);
        } catch (NumberFormatException ex) {
            input_Num.setError("חייב להית מספר");
            return false;
        }
        return true;
    }
}