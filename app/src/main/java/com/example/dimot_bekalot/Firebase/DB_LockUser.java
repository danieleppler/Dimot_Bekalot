package com.example.dimot_bekalot.Firebase;

/**
 *
 */

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DB_LockUser {
    private static FirebaseDatabase dataBase;
    private static DatabaseReference myDataBase;
    //*************************************************************//


    /*checks if the uer account already was tried to enter 3 times without success*/
    public static boolean isUserLocked (DataSnapshot snapshot , String userName_ID, String PATIENTSorINSTITUTES){
        if(snapshot.child(PATIENTSorINSTITUTES).child(userName_ID).child("lockedAccount").child("locked").getValue().toString().equals("true"))
            return true;
        return false;
    }

    /**/
    public static void upTo_3_tries(DataSnapshot snapshot, String userName_ID, String PATIENTSorINSTITUTES){
        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference();
        String tryToLog = snapshot.child(PATIENTSorINSTITUTES).child(userName_ID).child("lockedAccount").child("logintry").getValue().toString();
        int tryToLog_INT = Integer.valueOf(tryToLog);
        tryToLog_INT++;
        myDataBase.child(PATIENTSorINSTITUTES).child(userName_ID).child("lockedAccount").child("logintry").setValue(String.valueOf(tryToLog_INT));
    }

    /**/
    public static String get_num_of_tries_login(DataSnapshot snapshot, String userName_ID, String PATIENTSorINSTITUTES){
        return snapshot.child(PATIENTSorINSTITUTES).child(userName_ID).child("lockedAccount").child("logintry").getValue().toString();
    }

    /**/
    public static void lock_user(String userName_ID, String PATIENTSorINSTITUTES){
        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference();
        myDataBase.child(PATIENTSorINSTITUTES).child(userName_ID).child("lockedAccount").child("logintry").setValue("0");
        myDataBase.child(PATIENTSorINSTITUTES).child(userName_ID).child("lockedAccount").child("locked").setValue("true");
    }

    /**/
    public static void Unlock_user(String userName_ID, String PATIENTSorINSTITUTES){
        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference();
        myDataBase.child(PATIENTSorINSTITUTES).child(userName_ID).child("lockedAccount").child("logintry").setValue("0");
        myDataBase.child(PATIENTSorINSTITUTES).child(userName_ID).child("lockedAccount").child("locked").setValue("false");
    }
}
