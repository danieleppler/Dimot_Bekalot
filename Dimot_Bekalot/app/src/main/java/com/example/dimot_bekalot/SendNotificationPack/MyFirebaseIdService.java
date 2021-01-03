package com.example.dimot_bekalot.SendNotificationPack;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        Log.d("check new tkn","entered this class for some reason");
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        if(firebaseUser!=null){
            updateToken(refreshToken);
        }
    }

    private void updateToken(String refreshToken){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Token token= new Token(refreshToken);
        DatabaseReference db_ref=FirebaseDatabase.getInstance().getReference("Patients");
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data:snapshot.getChildren()
                     ) {
                    if (data.child("Uid").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                       db_ref.child(data.getKey()).child("token").setValue(token);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
