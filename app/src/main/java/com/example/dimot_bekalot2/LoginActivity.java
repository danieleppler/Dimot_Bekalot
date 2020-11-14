package com.example.dimot_bekalot2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button Login;
    Button Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Login=(Button)findViewById(R.id.Login);
        Register=(Button)findViewById(R.id.Register);

        Login.setOnClickListener(this);
        Register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == Login) {
            Toast.makeText(this, "unavailable at this moment", Toast.LENGTH_LONG).show();
        }

        if (v == Register)
        {
            Intent intent=new Intent(this, com.example.dimot_bekalot2.RegisterActivity.class);
            startActivity(intent);
        }

    }
}
