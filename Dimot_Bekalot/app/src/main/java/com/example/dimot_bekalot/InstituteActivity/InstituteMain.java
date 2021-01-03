package com.example.dimot_bekalot.InstituteActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InstituteMain extends AppCompatActivity {
    private Button add, watching, institute_data;
    private TextView institute_name;
    private static final String INSTITUTE = "Institutes";
    private String institute_id = "";
    private String nameInstitute = "";

    private FirebaseDatabase dataBase;
    private DatabaseReference dbRef;

    private ImageButton logOut;
    Context context = this;

    /* Image */
    private ImageView image;
    private Uri imageUri;
    private ImageButton get_picture;
    private static final int PICK_IMAGE = 100;

    private FirebaseStorage storage;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_main);

//       Intent institute_details = getIntent();
//       institute_id = institute_details.getExtras().getString("instituteID"); //Real-time
        institute_id = "i:123456781"; //debuging
        dataBase = FirebaseDatabase.getInstance();
        dbRef = dataBase.getReference(INSTITUTE);

        /* name of institute */
        institute_name = (TextView) findViewById(R.id.name_institute);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nameInstitute = snapshot.child(institute_id).child("institute_name").getValue().toString();
                institute_name.setText(nameInstitute);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        image = (ImageView) findViewById(R.id.institute_image);

        /*accessing the firebase storage*/
        storage = FirebaseStorage.getInstance();
        /*creates a storage reference*/
        storageRef = storage.getReference();

        get_picture = (ImageButton) findViewById(R.id.get_image_form_app);
        get_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

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

        logOut = (ImageButton) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logOutIntent = new Intent(context, com.example.dimot_bekalot.entryActivities.Main_Activity.class);
                startActivity(logOutIntent);
            }
        });

    }

    private void open_addQueueActivity(){
        Intent add_in = new Intent(this, AddQueueActivity.class);
        add_in.putExtra("instituteID", institute_id);
        startActivity(add_in);
    }
    private void open_watchingQueueActivity(){
        Intent watching_in = new Intent(this, WatchingQueueActivity.class);
        watching_in.putExtra("instituteID", institute_id);
        startActivity(watching_in);
    }
    private void open_dateActivity() {
        Intent personal_data = new Intent(this, com.example.dimot_bekalot.dataObjects.Institute_data_activity.class);
        personal_data.putExtra("instituteID", institute_id);
        startActivity(personal_data);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }
}
