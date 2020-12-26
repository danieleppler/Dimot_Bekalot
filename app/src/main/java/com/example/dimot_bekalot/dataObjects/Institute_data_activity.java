package com.example.dimot_bekalot.dataObjects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.dimot_bekalot.Tools.validationTools.isForgetPasswordInputValid;

import com.example.dimot_bekalot.Firebase.DB_ChangePassword;
import com.example.dimot_bekalot.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class Institute_data_activity extends AppCompatActivity implements View.OnClickListener {
    private TextView email, phone_number, password,
            institute_name, institute_id, address;
    private String out_institute_id;
    private FirebaseDatabase dataBase;
    private DatabaseReference ref_institute;
    private final String INSTITUTES = "Institutes";
    private FirebaseAuth passwordAuth;

    // for popup of address
    Dialog addressDialog;
    private EditText data_change_city, data_change_street, data_change_number;
    private Button addressDialog_yes;
    private String addressDialog_new_data;

    // for popup of address
    Dialog passwordDialog;
    private EditText firstTimePassword, secondTimePassword;
    private Button passwordDialog_yes;

    // for popup
    Dialog dialog;
    private EditText data_to_change;
    private Button yes;
    private String new_data, choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_data);

        Intent intent = getIntent();
        out_institute_id = intent.getExtras().getString("instituteID");

        email = (TextView) findViewById(R.id.data_email);
        phone_number = (TextView) findViewById(R.id.data_phone_number);
        password = (TextView) findViewById(R.id.data_password);
        institute_name = (TextView) findViewById(R.id.data_name_institute);
        institute_id = (TextView) findViewById(R.id.data_id);
        address = (TextView) findViewById(R.id.data_location);

        dataBase = FirebaseDatabase.getInstance();
        ref_institute = dataBase.getReference(INSTITUTES);
        passwordAuth = FirebaseAuth.getInstance();

        ref_institute.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                email.setText(snapshot.child(out_institute_id).child("email").getValue().toString());
                password.setText(snapshot.child(out_institute_id).child("password").getValue().toString());
                phone_number.setText(snapshot.child(out_institute_id).child("phone_number").getValue().toString());
                institute_name.setText(snapshot.child(out_institute_id).child("institute_name").getValue().toString());

                String city = snapshot.child(out_institute_id).child("address").child("city_Name").getValue().toString();
                String street = snapshot.child(out_institute_id).child("address").child("street_Name").getValue().toString();
                String number_house = snapshot.child(out_institute_id).child("address").child("house_Number").getValue().toString();
                address.setText("Street " + street + " number " + number_house + ", " + city);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

        String id = out_institute_id.substring(2); // id without "i:"
        institute_id.setText(id);
        password.setOnClickListener(this);
        phone_number.setOnClickListener(this);
        institute_name.setOnClickListener(this);
        address.setOnClickListener(this);
        email.setOnClickListener(this);
        institute_id.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (password == v) {
            choice = "password";

            CreatePopupPassword();
        } else if (passwordDialog_yes == v) {
            String password_1 = firstTimePassword.getText().toString().trim();
            String password_2 = secondTimePassword.getText().toString().trim();
            if (!isForgetPasswordInputValid(password_1, firstTimePassword)) {
                return;
            }
            if (password_1.equals(password_2)) {
                updateInDatabase(choice);
                Toast.makeText(this, "הסיסמא שונתה בהצלחה", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "אינכם יכולים להמשיך, הסיסמאות חייבות להיות זהות", Toast.LENGTH_LONG).show();
                return;
            }
            passwordDialog.dismiss();
        } else if (address == v) {
            choice = "address";

            CreatePopupAddress();
        } else if (addressDialog_yes == v) {
            String city = data_change_city.getText().toString().trim();
            String street = data_change_street.getText().toString().trim();
            String numberHouse = data_change_number.getText().toString().trim();
            addressDialog_new_data = city + "#" + street + "#" + numberHouse;

            updateInDatabase(choice);

            Toast.makeText(this, "הכתובת שונתה בהצלחה", Toast.LENGTH_LONG).show();
            addressDialog.dismiss();
        } else if (phone_number == v ||
                institute_name == v) {
            if (phone_number == v) {
                choice = "phone_number";
            } else {
                choice = "institute_name";
            }

            CreatePopup();
        } else if (yes == v) {
            new_data = data_to_change.getText().toString().trim();
            updateInDatabase(choice);
            Toast.makeText(this, choice + " שונה בהצלחה", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        } else if (email == v || institute_id == v) {
            Toast.makeText(this, "שדה זה אינו בר שינוי, עימכם הסליחה", Toast.LENGTH_LONG).show();
        }
    }

    private void CreatePopupPassword() {
        passwordDialog = new Dialog(this);
        passwordDialog.setContentView(R.layout.popup_password_change_institute);

        passwordDialog.setTitle("Update password");

        firstTimePassword = (EditText) passwordDialog.findViewById(R.id.password1);
        secondTimePassword = (EditText) passwordDialog.findViewById(R.id.password2);

        passwordDialog_yes = (Button) passwordDialog.findViewById(R.id.yes_update_password);
        passwordDialog_yes.setOnClickListener(this);

        passwordDialog.show();
    }

    private void CreatePopupAddress() {
        addressDialog = new Dialog(this);
        addressDialog.setContentView(R.layout.popup_address_change_institute);

        addressDialog.setTitle("Update address");

        data_change_city = (EditText) addressDialog.findViewById(R.id.data_city);
        data_change_street = (EditText) addressDialog.findViewById(R.id.data_street);
        data_change_number = (EditText) addressDialog.findViewById(R.id.data_number_house);

        addressDialog_yes = (Button) addressDialog.findViewById(R.id.yes_update_address);
        addressDialog_yes.setOnClickListener(this);

        addressDialog.show();
    }

    private void CreatePopup() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_data_change_institute);

        dialog.setTitle("Update");

        data_to_change = (EditText) dialog.findViewById(R.id.input_data_to_change);

        yes = (Button) dialog.findViewById(R.id.yes_update);
        yes.setOnClickListener(this);

        dialog.show();
    }

    private void updateInDatabase(String choice) {
        ref_institute.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (choice.equals("address")) {
                    String address_new_data[] = addressDialog_new_data.split("#");
                    ref_institute.child(out_institute_id).child(choice).child("city_Name").setValue(address_new_data[0]);
                    ref_institute.child(out_institute_id).child(choice).child("street_Name").setValue(address_new_data[1]);
                    ref_institute.child(out_institute_id).child(choice).child("house_Number").setValue(address_new_data[2]);
                } else if (choice.equals("password")) {
                    String newPassword = firstTimePassword.getText().toString().trim();
                    String emailInDB = email.getText().toString();
                    String originalPassword = password.getText().toString();
                    Login_Input_Data thisInstitute = new Login_Input_Data(out_institute_id, originalPassword, emailInDB);
                    updatePassword(newPassword, thisInstitute);
                } else {
                    ref_institute.child(out_institute_id).child(choice).setValue(new_data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    private void updatePassword(String newPassword, Login_Input_Data data_institute) {
        DB_ChangePassword.changePasswordAtAuthentication(INSTITUTES, data_institute, newPassword);
        /*wait until the Firebase Authentication will update the password*/
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DB_ChangePassword.changePasswordAtRealDB(INSTITUTES, data_institute, newPassword);
    }


}