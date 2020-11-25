package com.example.dimot_bekalot.tools;
/**
 *
 */

import android.text.TextUtils;
import android.widget.EditText;

/**
 *
 */
public class validationTools {

    /**
     * @param firstName
     * @param lastName
     * @return
     */
    public static boolean isPatientNamesIsValid(String firstName, String lastName, EditText first_nameInput,
                                                EditText last_nameInput) {
        /*checking if the inputs is valid inputs*/
        if (TextUtils.isEmpty(firstName)) {
            first_nameInput.setError("שם פרטי הוא שדה חובה");
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            last_nameInput.setError("שם משפחה הוא שדה חובה");
            return false;
        }
        return true;
    }

    public static boolean isInstituteNamesIsValid(String InstituteName, EditText institute_nameInput){
        /*checking if the inputs is valid inputs*/
        if (TextUtils.isEmpty(InstituteName)) {
            institute_nameInput.setError("שם פרטי הוא שדה חובה");
            return false;
        }
        return true;
    }
    /**
     * @return
     */
    public static boolean isAllCostumersNeedfulInputIsValid(String email, String password, String phone,
                                                           EditText emailInput, EditText passwordInput,
                                                           EditText phone_numberInput ) {
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

        if (password.length() < 9) {
            passwordInput.setError("סיסמא חייבת להיות באורך של מינימום 8 תוים");
            return false;
        }
        return true;
    }
}
