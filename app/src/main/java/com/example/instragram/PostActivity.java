package com.example.instragram;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instragram.Model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PostActivity extends AppCompatActivity {
    private ImageView close, imageAdded;
    private TextView post;
    private EditText description;
    private ActivityResultLauncher<String> mGetContent;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close = findViewById(R.id.close);
        imageAdded = findViewById(R.id.image_added);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if(result != null){
                    imageUri = result;
                    imageAdded.setImageURI(imageUri);
                }
            }
        });

        imageAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = System.currentTimeMillis()+"";
                StorageReference ref = FirebaseStorage.getInstance().getReference().child("post").child(name);

                ref.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){
                                        String url = String.valueOf(task.getResult());
                                        Toast.makeText(PostActivity.this, url, Toast.LENGTH_SHORT).show();
                                        String desc = description.getText().toString();


                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        if(user == null) return;

                                        String uid = user.getUid();


                                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("imageurl");
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String proUrl = "https://firebasestorage.googleapis.com/v0/b/instragramsunzid.appspot.com/o/post%2F1672071025523?alt=media&token=da0b1b7d-2bf6-4ae9-8740-5f584470d411";
                                                if(snapshot.exists())
                                                {
                                                    proUrl = String.valueOf(snapshot.getValue());
                                                }


                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                                                        .child("post");
                                                String key = ref.push().getKey();

                                                assert key != null;

                                                Post post = new Post(desc,key,url,proUrl,uid);

                                                ref.child(key).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(PostActivity.this, "Post added", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                        else{
                                                            Toast.makeText(PostActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                    else{
                                        task.getException().printStackTrace();
                                    }
                                }
                            });
                        }
                        else{
                            task.getException().printStackTrace();
                        }
                    }
                });

            }
        });
    }

}