package com.example.dimot_bekalot.connectActivities;
/**
 *
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dimot_bekalot.R;
import com.example.dimot_bekalot.dataObjects.Costumer_Details_Institute;
import com.example.dimot_bekalot.entryActivities.Main_Activity;
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
    private static final int REQUEST_CALL = 1;

    private ImageButton logOut;
    Context context = this;

    private FirebaseDatabase dataBase;
    private DatabaseReference myDataBase;
    private Spinner chooseInstituteSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_to_institute_activity);

        InstitutesPhoneList = new ArrayList<>();
        instituteToCall = new Costumer_Details_Institute();
        /*create a drop sown menu*/
        chooseInstituteSpinner = findViewById(R.id.dropdown_menu_instituties);

        /*FireBase_connection*/
        dataBase = FirebaseDatabase.getInstance();
        myDataBase = dataBase.getReference(INSTITUTES);
        /*end_FireBase_connection*/

        /*Bottun_log-out*/
        logOut = (ImageButton) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(context, com.example.dimot_bekalot.entryActivities.Main_Activity.class);
                startActivity(logOutIntent);
            }
        });
        /*end_Bottun_log-out*/

//        /*asking for permission to use the dialer of the user*/
//        if(ContextCompat.checkSelfPermission(Call_To_Institute_Activity.this,
//                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(Call_To_Institute_Activity.this,
//                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
//        }
//        /*end asking permission*/

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
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        chooseInstituteSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String instituteNameAndPhone = parent.getItemAtPosition(position).toString();
        ActivityCompat.requestPermissions(Call_To_Institute_Activity.this,
                new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        if (!instituteNameAndPhone.equals("בחר מכון רצוי בבקשה")) {
            String phone = creteOnltPhoneNumber(instituteNameAndPhone);
            if (ContextCompat.checkSelfPermission(Call_To_Institute_Activity.this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent open_dialer = new Intent(Intent.ACTION_DIAL);
                open_dialer.setData(Uri.parse("tel:" + phone));
                startActivity(open_dialer);
            }
        } else
            Toast.makeText(parent.getContext(), "אנא בחר מכון מן הרשימה לצורך התקשרות", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onBackPressed() {
        Intent open_login = new Intent(this, Main_Activity.class);
        open_login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(open_login);
    }

    /************private function************/
    /*split the string that contains the institute name and it's phone number, to use only the phone number*/
    private String creteOnltPhoneNumber(String instituteNameAndPhone) {
        String[] name_phone = instituteNameAndPhone.split("  :  ");
        return name_phone[1];
    }

    /*asking for permission from the user to use the dialer to make a call*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseInstituteSpinner.setOnItemSelectedListener(this);
            } else {
                Toast.makeText(this, "לא אישרת לבצע את השיחה", Toast.LENGTH_SHORT).show();
            }
        }
    }
}