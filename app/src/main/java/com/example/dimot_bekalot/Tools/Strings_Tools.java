package com.example.dimot_bekalot.Tools;

public class Strings_Tools {
    /**
     * This class contains help tools String for the project
     */
    /*add to the string that contains the user name , the ":"*/
    public static String createNOTCleanUserName(String originalString, int position, String putInOriginal) {
        StringBuilder newUserName = new StringBuilder(originalString);
        newUserName.insert(position, putInOriginal);
        return newUserName.toString();
    }

    /*clean the string and remain only the ID with 9 digits*/
    public static String only_number_at_ID(String UserName) {
        StringBuffer clean_ID = new StringBuffer(UserName);
        clean_ID.deleteCharAt(0);
        return clean_ID.toString();
    }
}
