package com.project.a_star_fitness;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileUpdateActivity extends AppCompatActivity {

    private static final String TAG = "";

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users");

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String id = user.getUid();

            Log.d("This is userId: ", id);


            DAOUser dao = new DAOUser();


            root.child(id).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultName = String.valueOf(task.getResult().getValue());
                        name.setText(defaultName);
                    }
                }
            });

            root.child(id).child("age").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultAge = String.valueOf(task.getResult().getValue());
                        age.setText(defaultAge);
                    }
                }
            });

            root.child(id).child("expectation").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultExpectation = String.valueOf(task.getResult().getValue());
                        if (defaultExpectation.equals("Athletic")) {
                            expectation.check(R.id.athletic);
                        } else if (defaultExpectation.equals("Attractive")) {
                            expectation.check(R.id.attractive);
                        } else if (defaultExpectation.equals("Healthy")) {
                            expectation.check(R.id.healthy);
                        }

                    }
                }
            });

            root.child(id).child("gender").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultGender = String.valueOf(task.getResult().getValue());
                        if (defaultGender.equals("Male")) {
                            gender.check(R.id.male);
                        } else if (defaultGender.equals("Female")) {
                            gender.check(R.id.female);
                        }
                    }
                }
            });

            root.child(id).child("height").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultHeight = String.valueOf(task.getResult().getValue());
                        height.setText(defaultHeight);
                    }
                }
            });

            root.child(id).child("weight").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultWeight = String.valueOf(task.getResult().getValue());
                        weight.setText(defaultWeight);
                    }
                }
            });

            root.child(id).child("work").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultWork = String.valueOf(task.getResult().getValue());
                        if (defaultWork.equals("Sit Work")) {
                            work.check(R.id.sitWork);
                        } else if (defaultWork.equals("Move Work")) {
                            work.check(R.id.moveWork);
                        } else if (defaultWork.equals("Balance Work")) {
                            work.check(R.id.balanceWork);
                        }
                    }
                }
            });


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


                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", name.getText().toString());
                    hashMap.put("expectation", expectationButton.getText().toString());
                    hashMap.put("gender", genderButton.getText().toString());
                    hashMap.put("work", workButton.getText().toString());
                    hashMap.put("age", Integer.parseInt(age.getText().toString()));
                    hashMap.put("height", Integer.parseInt(height.getText().toString()));
                    hashMap.put("weight", Integer.parseInt(weight.getText().toString()));

                    hashMap.put("score", score);

                    if (TextUtils.isEmpty(name.getText().toString())) {
                        Toast.makeText(ProfileUpdateActivity.this,
                                "Empty name not allowed!",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(expectationButton.getText().toString())) {
                        Toast.makeText(ProfileUpdateActivity.this,
                                "Empty expectation not allowed!",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(genderButton.getText().toString())) {
                        Toast.makeText(ProfileUpdateActivity.this,
                                "Empty gender not allowed!",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(workButton.getText().toString())) {
                        Toast.makeText(ProfileUpdateActivity.this,
                                "Empty work type not allowed!",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(age.getText().toString())) {
                        Toast.makeText(ProfileUpdateActivity.this,
                                "Empty age not allowed!",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(height.getText().toString())) {
                        Toast.makeText(ProfileUpdateActivity.this,
                                "Empty height not allowed!",
                                Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(weight.getText().toString())) {
                        Toast.makeText(ProfileUpdateActivity.this,
                                "Empty weight not allowed!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        dao.update(id, hashMap).addOnSuccessListener(suc -> {
                            Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ProfileUpdateActivity.this, MainActivity.class);
                            startActivity(intent);

                        }).addOnFailureListener(er -> {
                            Toast.makeText(getApplicationContext(), "Update Fail", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });

        } else {
            // No user is signed in
        }
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