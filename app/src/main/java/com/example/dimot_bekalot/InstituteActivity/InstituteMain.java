package com.example.dimot_bekalot.InstituteActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InstituteMain extends AppCompatActivity {
    private Button add, watching, institute_data;
    private TextView institute_name;
    private static final String INSTITUTE = "Institutes";
    private String institute_id = "";
    private String nameInstitute = "";

    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_main);

        Intent institute_details = getIntent();

        //institute_id = institute_details.getExtras().getString("instituteID"); //Real-time
        institute_id = "i:123456123"; //DE-BUGING

        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference(INSTITUTE);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameInstitute = snapshot.child(institute_id).child("institute_name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

        });
        /* name of institute */
        institute_name = (TextView)findViewById(R.id.name_institute);
        institute_name.setText(nameInstitute);

        add = (Button)findViewById(R.id.add_queue);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_addQueueActivity();
            }
        });

        watching = (Button)findViewById(R.id.watch_queue);
        watching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_watchingQueueActivity();
            }
        });
        institute_data = (Button)findViewById(R.id.instData);
        institute_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_dateActivity();
            }
        });

    }

    private void open_addQueueActivity(){
        Intent add_in = new Intent(this, AddQueueActivity.class);
        add_in.putExtra("instituteID", institute_id);
        startActivity(add_in);
    }
    private void open_watchingQueueActivity(){
//        String type = createPopupWatching();
        Intent watching_in = new Intent(this, WatchingQueueActivity.class);
        watching_in.putExtra("instituteID", institute_id);
//        watching_in.putExtra("Treatment_type", type);
        startActivity(watching_in);
    }
    private void open_dateActivity() {
        Intent personal_data = new Intent(this, com.example.dimot_bekalot.dataObjects.Institute_data_activity.class);
        startActivity(personal_data);
    }
}
