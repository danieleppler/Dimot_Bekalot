package com.example.dimot_bekalot.tools;
/**
 * This class contains help tools validation
 * functions to the registers aka Patient or Institute
 */

import android.text.TextUtils;
import android.widget.EditText;

public class validationTools {

    /**
     * This function check if the input at Login Activity is valid and correct
     *
     * @param ID
     * @param password
     * @param ID_Input
     * @param password_input
     * @return
     */
    public static boolean isLoginInputValid(String ID, String password, EditText ID_Input,
                                            EditText password_input) {

        if (TextUtils.isEmpty(ID)) {
            ID_Input.setError("ת.ז או מספר רישוי הוא שדה חובה");
            return false;
        }

        if(ID.length() != 9){
            ID_Input.setError("ת.ז או מספר רישוי אמור להיות באורך 9 ספרות");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            password_input.setError("סיסמא הוא שדה חובה");
            return false;
        }

        if (password.length() != 10) {
            password_input.setError("אורך הסיסמא חייב להיות 10 תווים");
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

        if (Integer.valueOf(age) > 120) {
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
            passwordInput.setError("הכנס 9 תווים, התו הראשון כבר הוכנס אוטומטי");
            return false;
        }

        return true;
    }

    /**
     * Cheking if input string is a long Or integer or not
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