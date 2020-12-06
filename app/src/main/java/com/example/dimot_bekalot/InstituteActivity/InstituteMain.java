package com.example.dimot_bekalot.InstituteActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InstituteMain extends AppCompatActivity implements View.OnClickListener {
    private Button update, watching, institute_data;
    private TextView institute_name;
    private static final String INSTITUTE = "institutes";

    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_main);

        Intent institute_details = getIntent();
        String numID = institute_details.getExtras().getString("instituteID");
        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference(INSTITUTE).child(numID).child("institute_name");
        String nameInstitute = dbRef.toString();

        /* name of institute */
        institute_name = (TextView)findViewById(R.id.name_institute);
        institute_name.setText(nameInstitute);

        update = (Button)findViewById(R.id.update_queue);
        watching = (Button)findViewById(R.id.watch_queue);
        institute_data = (Button)findViewById(R.id.instData);

    }

    @Override
    public void onClick(View v) {
        if(update == v){ // go to UpdateQueueActivity
            Intent update_in = new Intent(this, com.example.dimot_bekalot.InstituteActivity.UpdateQueueActivity.class);
            startActivity(update_in);
        }
        else if (watching == v) { // go to WatchingQueueActivity
            Intent watch_in = new Intent(this, com.example.dimot_bekalot.InstituteActivity.WatchingQueueActivity.class);
            startActivity(watch_in);

        }
        else if(institute_data == v){ // go to page of data
//            Intent personal_data = new Intent(this, );
//            startActivity(personal_data);

        }
    }
}
