package com.example.dimot_bekalot.clientActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dimot_bekalot.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_client_det extends AppCompatActivity implements View.OnClickListener {
    String client_id;
    FirebaseDatabase mDatabase;
    DatabaseReference db_ref;
    EditText name1,name2,age,city,street,num;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client_det);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        client_id=getIntent().getStringExtra("client_id");
        mDatabase=FirebaseDatabase.getInstance();
        db_ref=mDatabase.getReference().child("Patients");
        name1=(EditText)findViewById(R.id.name1);
        name2=(EditText)findViewById(R.id.name2);
        age=(EditText)findViewById(R.id.age);
        city=(EditText)findViewById(R.id.city_edit);
        street=(EditText)findViewById(R.id.street);
        num=(EditText)findViewById(R.id.house_number);
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(this);

        db_ref.child(client_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name1.setText(snapshot.child("first_name").getValue().toString());
                name2.setText(snapshot.child("second_name").getValue().toString());
                age.setText(snapshot.child("age").getValue().toString());
                city.setText(snapshot.child("address").child("city_Name").getValue().toString());
                street.setText(snapshot.child("address").child("street_Name").getValue().toString());
               num.setText(snapshot.child("address").child("house_Number").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==save)
        {
            db_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("edit",name1+" "+name2+" "+age+" "+city+" "+street+" "+num);
                    db_ref.child(client_id).child("first_name").setValue(name1);
                    db_ref.child(client_id).child("second_name").setValue(name2);
                    db_ref.child(client_id).child("age").setValue(age);
                    db_ref.child(client_id).child("address").child("city_Name").setValue(city);
                    db_ref.child(client_id).child("address").child("street_Name").setValue(street);
                    db_ref.child(client_id).child("address").child("house_Number").setValue(num);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Intent intent=new Intent(this,com.example.dimot_bekalot.clientActivities.Main_Client_View.class);
            intent.putExtra("client_id",client_id);
            startActivity(intent);
        }
    }
}