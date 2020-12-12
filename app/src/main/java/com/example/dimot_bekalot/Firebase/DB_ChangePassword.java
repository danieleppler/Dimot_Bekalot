package com.example.dimot_bekalot.Firebase;
/**
 *
 */

import androidx.annotation.NonNull;
import com.example.dimot_bekalot.dataObjects.Login_Input_Data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DB_ChangePassword {

    private static FirebaseAuth changePassword;
    private static FirebaseDatabase dataBase;
    private static DatabaseReference myDataBase;

    private static final String INSTITUTES = "Institutes";
    private static final String PATIENTS = "Patients";

    //*************************************************************//


    /**write the new password of the user in real DB*/
    public static void changePasswordAtRealDB(String PATIENTSorINSTITUTES, Login_Input_Data InputToChangePassword , String newValidPassword){
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference();
        if (PATIENTSorINSTITUTES.equals(PATIENTS)) {
            myDataBase.child(PATIENTS).child(InputToChangePassword.getID()).child("password").setValue(newValidPassword);
        } else if (PATIENTSorINSTITUTES.equals(INSTITUTES)) {
            myDataBase.child(INSTITUTES).child(InputToChangePassword.getID()).child("password").setValue(newValidPassword);
        }
    }

    /**write the new password of the user FireBase Authentication*/
    public static void changePasswordAtAuthentication(String PATIENTSorINSTITUTES, Login_Input_Data InputToChangePassword, String newValidPassword){
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference();
        changePassword = FirebaseAuth.getInstance();
        myDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String password = snapshot.child(PATIENTSorINSTITUTES).child(InputToChangePassword.getID()).child("password").getValue().toString();
                changePassword.signInWithEmailAndPassword(InputToChangePassword.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = changePassword.getCurrentUser();
                            user.updatePassword(newValidPassword);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
