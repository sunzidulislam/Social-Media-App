package com.example.instragram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity {


    private TextView tvUsername, tvName, tvEmail;
    private CircleImageView ivProfile;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String userId = getIntent().getStringExtra("publisherId");

        tvUsername = findViewById(R.id.username);
        tvName = findViewById(R.id.name);
        tvEmail = findViewById(R.id.email);

        ivProfile = findViewById(R.id.ivProfile);
        buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get()
                        .load(String.valueOf(snapshot.child("imageurl").getValue()))
                        .placeholder(R.mipmap.ic_launcher).into(ivProfile);

                tvUsername.setText(
                        String.valueOf(snapshot.child("username").getValue())
                );

                tvName.setText(
                        String.valueOf(snapshot.child("name").getValue())
                );

                tvEmail.setText(
                        String.valueOf(snapshot.child("email").getValue())
                );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
