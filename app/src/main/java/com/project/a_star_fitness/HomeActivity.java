package com.project.a_star_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.project.a_star_fitness.posts.Post_list;
import com.project.a_star_fitness.profile.ProfileUpdateActivity;
import com.project.a_star_fitness.record.RecordActivity;
import com.project.a_star_fitness.register.LoginActivity;

public class HomeActivity extends AppCompatActivity {
    private Button logout;
    Button buttonUpdate;
    private Button goToRecord;
    private Button post_list_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.signOut);
        post_list_btn=findViewById(R.id.post_list_btn);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        goToRecord = findViewById(R.id.go_to_record);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileUpdateActivity.class);
                startActivity(intent);
            }
        });

        goToRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, RecordActivity.class));
            }
        });
        post_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Post_list.class));
            }
        });
    }
}