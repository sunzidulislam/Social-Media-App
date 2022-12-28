package com.example.instragram.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instragram.Adapter.PostAdapter;
import com.example.instragram.Adapter.PostAdapterProfile;
import com.example.instragram.LoginActivity;
import com.example.instragram.Model.Post;
import com.example.instragram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private TextView tvUsername, tvName, tvEmail;
    private CircleImageView ivProfile;
    private ImageView ivLogout;
    private RecyclerView recyclerViewPosts;
    private PostAdapterProfile adapter;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.username);
        tvName = view.findViewById(R.id.name);
        tvEmail = view.findViewById(R.id.email);

        ivProfile = view.findViewById(R.id.ivProfile);
        ivLogout = view.findViewById(R.id.ivLogout);




        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);


        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });


        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) return;


        String userId = user.getUid();

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

        readPosts();

    }



    private void readPosts() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) return;

        String userId = user.getUid();

        FirebaseDatabase.getInstance().getReference().child("savedPost").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Post> postList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    postList.add(post);
                }

                adapter = new PostAdapterProfile(getContext(), postList);
                recyclerViewPosts.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}