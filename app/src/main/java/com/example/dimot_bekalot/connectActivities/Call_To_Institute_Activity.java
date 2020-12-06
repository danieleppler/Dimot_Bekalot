package com.example.dimot_bekalot.connectActivities;
/**
 *
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Costumer_Details_Institute;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Call_To_Institute_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String INSTITUTES = "Institutes";
    private ArrayList<String> InstitutesPhoneList;
    private ArrayAdapter<String> InstitutesPhoneListAdapter;
    private Costumer_Details_Institute instituteToCall;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_to_institute_activity);

        InstitutesPhoneList = new ArrayList<>();
        instituteToCall = new Costumer_Details_Institute();
        /*create a drop sown menu*/
        Spinner chooseInstituteSpinner = findViewById(R.id.dropdown_menu_instituties);

        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference(INSTITUTES);
        /*end_FireBase_connection*/

        /*create a adapter to show the institutes names and phone numbers */
        InstitutesPhoneListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, InstitutesPhoneList);

        myDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                InstitutesPhoneList.add("בחר מכון רצוי בבקשה");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    instituteToCall = dataSnapshot.getValue(Costumer_Details_Institute.class);
                    InstitutesPhoneList.add(instituteToCall.getInstitute_name() + "  :  " + instituteToCall.getPhone_number());
                }
                InstitutesPhoneListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                chooseInstituteSpinner.setAdapter(InstitutesPhoneListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
        chooseInstituteSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String instituteNameAndPhone = parent.getItemAtPosition(position).toString();
        if (!instituteNameAndPhone.equals("בחר מכון רצוי בבקשה"))
            openInstituteMenu_Activity("tel:" + creteOnltPhoneNumber(instituteNameAndPhone));
        else
            Toast.makeText(parent.getContext(), "אנא בחר מכון מן הרשימה לצורך התקשרות", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    /************private function************/
    /**/
    private String creteOnltPhoneNumber(String instituteNameAndPhone) {
        String[] name_phone = instituteNameAndPhone.split("  :  ");
        return name_phone[1];
    }

    /*Open the dialer*/
    private void openInstituteMenu_Activity(String Institute_phoneNumber) {
        Intent open_dialer = new Intent(Intent.ACTION_DIAL);
        open_dialer.setData(Uri.parse(Institute_phoneNumber));
        startActivity(open_dialer);
    }
}