package com.project.a_star_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class DietActivity extends AppCompatActivity {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users");
    private TextView proteinView;
    private TextView fatView;
    private TextView carbsView;
    private TextView calorieView;

    private String gender;
    private String age;
    private String height;
    private String weight;
    private String activityLevel;
    private String goal;

    private Button macroButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);

        proteinView = findViewById(R.id.proteinView);
        fatView = findViewById(R.id.fatView);
        carbsView = findViewById(R.id.carbsView);
        calorieView = findViewById(R.id.calorieView);


        macroButton = findViewById(R.id.macroButton);

        macroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetMacrosTask macrosTask = new GetMacrosTask();
                macrosTask.execute();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            String id = user.getUid();

            Log.d("This is userId: ", id);

            root.child(id).child("age").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultAge = String.valueOf(task.getResult().getValue());
                        age = defaultAge;
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
                        height = defaultHeight;
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
                        weight = defaultWeight;
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
                        gender = defaultGender.toLowerCase();
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
                            activityLevel = "2";
                        } else if (defaultWork.equals("Move Work")) {
                            activityLevel = "4";
                        } else if (defaultWork.equals("Balance Work")) {
                            activityLevel = "6";
                        }
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
                            goal = "weightgain";
                        } else if (defaultExpectation.equals("Attractive")) {
                            goal = "weightlose";
                        } else if (defaultExpectation.equals("Healthy")) {
                            goal = "maintain";
                        }

                    }
                }
            });
        }
        else{
            // user is null
        }
    }

    private class GetMacrosTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            String result = null;

            try {
                OkHttpClient client = new OkHttpClient();

                int weightInKg = (int) (Integer.parseInt(weight) / 2.2);

                Request request = new Request.Builder()
                        .url("https://fitness-calculator.p.rapidapi.com/macrocalculator?age=" + age
                                + "&gender=" + gender + "&height=" + height + "&weight=" + weightInKg
                                + "&activitylevel=" + activityLevel + "&goal=" + goal)
                        .get()
                        .addHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "e3da6c4171mshb00bfe19ec86053p195a81jsn82f7c1f971ba")
                        .build();

                Response response = client.newCall(request).execute();

                Log.d("Got Response","got response");

                result = response.body().string();

            } catch (Exception e) {
                Log.d("Exception", "Caught Exception: " + e.getMessage());
            }

            return result;
        }

        protected void onPostExecute(String result){
            Log.i("BodyFat result on post execute", result);

            try {
                JSONObject myObject = new JSONObject(result);
                JSONObject userData = myObject.getJSONObject("data");

                String calorie = userData.getDouble("calorie") + "";

                calorieView.setText(calorie);

                JSONObject macrosData = userData.getJSONObject("balanced");

                String protein = macrosData.getDouble("protein") + "";
                String fat = macrosData.getDouble("fat") + "";
                String carbs = macrosData.getDouble("carbs") + "";

                Log.d("JSON data", protein);

                proteinView.setText(protein);
                fatView.setText(fat);
                carbsView.setText(carbs);
            }catch (Exception e) {
                Log.d("JSON Error", "");
//                bodyFatView.setText("000");
            }
        }
    }
}