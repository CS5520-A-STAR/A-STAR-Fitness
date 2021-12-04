package com.project.a_star_fitness.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.a_star_fitness.DAOUser;
import com.project.a_star_fitness.register.LoginActivity;
import com.project.a_star_fitness.R;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "";

    private EditText name;
    private EditText age;
    private EditText height;
    private EditText weight;

    private RadioGroup gender;
    private RadioGroup work;
    private RadioGroup expectation;

    private Button male;
    private Button female;

    private Button sit;
    private Button move;
    private Button balance;

    private Button athletic;
    private Button attractive;
    private Button healthy;

    private Button submit;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        gender = findViewById(R.id.gender);
        work = findViewById(R.id.work);
        expectation = findViewById(R.id.expectation);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        sit = findViewById(R.id.sitWork);
        move = findViewById(R.id.moveWork);
        balance = findViewById(R.id.balanceWork);
        athletic = findViewById(R.id.athletic);
        attractive = findViewById(R.id.attractive);
        healthy = findViewById(R.id.healthy);
        submit = findViewById(R.id.submit);


        DAOUser dao = new DAOUser();

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                double bmi=getBMI(Integer.parseInt(height.getText().toString()),Integer.parseInt(weight.getText().toString()));
                String score=BMItoScore(bmi);




                int expectationSelectedId = expectation.getCheckedRadioButtonId();

                Button expectationButton = findViewById(expectationSelectedId);

                int genderSelectedId = gender.getCheckedRadioButtonId();

                Button genderButton = findViewById(genderSelectedId);

                int workSelectedId = work.getCheckedRadioButtonId();

                Button workButton = findViewById(workSelectedId);


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    String id = user.getUid();

                    submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            int expectationSelectedId = expectation.getCheckedRadioButtonId();

                            Button expectationButton = findViewById(expectationSelectedId);

                            int genderSelectedId = gender.getCheckedRadioButtonId();

                            Button genderButton = findViewById(genderSelectedId);

                            int workSelectedId = work.getCheckedRadioButtonId();

                            Button workButton = findViewById(workSelectedId);


                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", name.getText().toString());
                            hashMap.put("expectation", expectationButton.getText().toString());
                            hashMap.put("gender", genderButton.getText().toString());
                            hashMap.put("work", workButton.getText().toString());
                            hashMap.put("age", Integer.parseInt(age.getText().toString()));
                            hashMap.put("height", Integer.parseInt(height.getText().toString()));
                            hashMap.put("weight", Integer.parseInt(weight.getText().toString()));

                            //
                            hashMap.put("score", score);


                            if (TextUtils.isEmpty(name.getText().toString())) {
                                Toast.makeText(ProfileActivity.this,
                                        "Empty name not allowed!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(expectationButton.getText().toString())) {
                                Toast.makeText(ProfileActivity.this,
                                        "Empty expectation not allowed!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(genderButton.getText().toString())) {
                                Toast.makeText(ProfileActivity.this,
                                        "Empty gender not allowed!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(workButton.getText().toString())) {
                                Toast.makeText(ProfileActivity.this,
                                        "Empty work type not allowed!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(age.getText().toString())) {
                                Toast.makeText(ProfileActivity.this,
                                        "Empty age not allowed!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(height.getText().toString())) {
                                Toast.makeText(ProfileActivity.this,
                                        "Empty height not allowed!",
                                        Toast.LENGTH_SHORT).show();
                            } else if (TextUtils.isEmpty(weight.getText().toString())) {
                                Toast.makeText(ProfileActivity.this,
                                        "Empty weight not allowed!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                dao.update(id, hashMap).addOnSuccessListener(suc -> {
                                    Toast.makeText(getApplicationContext(), "Submit Successful", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                }).addOnFailureListener(er -> {
                                    Toast.makeText(getApplicationContext(), "Submit Fail", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }
                    });
                } else {
                    // No user is signed in
                }

            }

        });

    }

    public double getBMI(int height,int weight){

        double bmi=703*weight/(height/2.54)/(height/2.54);

        return bmi;
    }

    public String BMItoScore(double bmi){

        if(bmi>=18.5 && bmi<=24.9){
            return "B";
        }

        if(bmi>=30){
            return "D";
        }

        return "C";
    }
}