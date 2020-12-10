package com.example.dimot_bekalot.InstituteActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InstituteMain extends AppCompatActivity implements View.OnClickListener {
    private Button add, watching, institute_data;
    private TextView institute_name;
    private static final String INSTITUTE = "institutes";
    private String institute_id = "";
    private String nameInstitute = "";

    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    // for pop up
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button mri, ct, bone_mapping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_main);

        Intent institute_details = getIntent();
        institute_id = institute_details.getExtras().getString("instituteID");
        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference(INSTITUTE);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameInstitute = snapshot.child(institute_id).child("institute_name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /* name of institute */
        institute_name = (TextView)findViewById(R.id.name_institute);
        institute_name.setText(nameInstitute);

        add = (Button)findViewById(R.id.add_queue);
        watching = (Button)findViewById(R.id.watch_queue);
        institute_data = (Button)findViewById(R.id.instData);

    }

    @Override
    public void onClick(View v) {
        if(add == v){ // go to AddQueueActivity
            Intent add_in = new Intent(this, AddQueueActivity.class);
            add_in.putExtra("instituteID", institute_id);
            startActivity(add_in);
        }
        else if (watching == v) { // go to WatchingQueueActivity
            String type = createPopupWatching();
            Intent watching_in = new Intent(this, com.example.dimot_bekalot.InstituteActivity.WatchingQueueActivity.class);
            watching_in.putExtra("instituteID", institute_id);
            watching_in.putExtra("Treatment_type", type);
            startActivity(watching_in);
        }
        else if(institute_data == v){ // go to page of data
            Intent personal_data = new Intent(this, com.example.dimot_bekalot.dataObjects.Institute_data_activity.class);
            startActivity(personal_data);
        }
    }

    private String createPopupWatching(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_queue, null);
        mri = (Button)findViewById(R.id.choice_mri);
        ct = (Button)findViewById(R.id.choice_ct);
        bone_mapping = (Button)findViewById(R.id.choice_bone_mapping);

        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();

        final String[] theChoiceThatWeWant = new String[1];

        mri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theChoiceThatWeWant[0] = "MRI";
            }
        });

        ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theChoiceThatWeWant[0] = "CT";
            }
        });

        bone_mapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theChoiceThatWeWant[0] = "BONE MAPPING";
            }
        });

        return theChoiceThatWeWant[0];
    }

}
