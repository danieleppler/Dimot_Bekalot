package com.example.dimot_bekalot.Firebase;

import com.example.dimot_bekalot.dataObjects.Costumer_Details;
import com.example.dimot_bekalot.dataObjects.Costumer_Details_Institute;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DB_WriteToDB {

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;



//    private void activate_Firebase_Connections(){
//        /*FireBase_connection*/
//        dataBase = FirebaseDatabase.getInstance();
//        myDataBase = dataBase.getReference(PATIENTS);
//        /*end_FireBase_connection*/
//    }
//
//    /*Adding patient to our Firebase DataBase*/
//    public static  void registerPatientToRealDB(Costumer_Details costumer_details,String User) {
//        if (User.equals("Patients")) {
//
//        } else if(User.equals("Institutes")) {
//            myDataBase.child(costumer_details_institute.getInstituteID()).setValue(this.costumer_details_institute);
//        }
//    }

}
