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

public class BodyActivity extends AppCompatActivity {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users");
    private TextView bmiView;
    private TextView heightView;
    private TextView weightView;
    private TextView ageView;

    private String height;
    private String weight;
    private String age;

    private Button bmiButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        bmiView = findViewById(R.id.bmiView);
        ageView = findViewById(R.id.ageView);
        heightView = findViewById(R.id.heightView);
        weightView = findViewById(R.id.weightView);
        bmiButton = findViewById(R.id.bmiButton);

        bmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetBMITask bmiTask = new GetBMITask();
                bmiTask.execute();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {

            String id = user.getUid();

            Log.d("This is userId: ", id);

            //DAOUser dao = new DAOUser();

            root.child(id).child("age").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    } else {
                        final String defaultAge = String.valueOf(task.getResult().getValue());
                        age = defaultAge;
                        ageView.setText(defaultAge);
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
                        heightView.setText(defaultHeight);
                        //Log.d("This is user height: ", defaultHeight);
                        //bmiView.setText(defaultHeight);
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
                        weightView.setText(defaultWeight);
                    }
                }
            });

//            root.child(id).child("gender").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DataSnapshot> task) {
//                    if (!task.isSuccessful()) {
//                        Log.e("firebase", "Error getting data", task.getException());
//                    } else {
//                        final String defaultGender = String.valueOf(task.getResult().getValue());
////                        if (defaultGender.equals("Male")) {
////                            gender.check(R.id.male);
////                        } else if (defaultGender.equals("Female")) {
////                            gender.check(R.id.female);
////                        }
//                    }
//                }
//            });

//            GetBMITask bmiTask = new GetBMITask();
//            bmiTask.execute();

        }
        else{
            // user is null
        }
    }

    private class GetBMITask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            String result = null;

            try {
                Log.d("Start BMI","Start BMI");

                OkHttpClient client = new OkHttpClient();

                int weightInKg = (int) (Integer.parseInt(weightView.getText().toString()) / 2.2);

                Request request = new Request.Builder()
                        .url("https://fitness-calculator.p.rapidapi.com/bmi?age=" +
                                ageView.getText().toString() + "&weight=" + weightInKg
                                + "&height=" + heightView.getText().toString())
                        .get()
                        .addHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "e3da6c4171mshb00bfe19ec86053p195a81jsn82f7c1f971ba")
                        .build();

                Log.d("Params","age is " + ageView.getText().toString() + " weight is "
                        + weightView.getText().toString() + " height is " + heightView.getText().toString());

                Response response = client.newCall(request).execute();

                Log.d("Got Response","got response");

                //Log.i("BMI result",response.body().string());

                //String responseString = response.body().string();
                result = response.body().string();

            } catch (Exception e) {
                Log.d("Exception", "Caught Exception: " + e.getMessage());
            }

            return result;
        }

        protected void onPostExecute(String result){
            Log.i("BMI result on post execute", result);

            try {
                JSONObject myObject = new JSONObject(result);
                JSONObject userData = myObject.getJSONObject("data");
                String bmiValue = userData.getDouble("bmi") + "";
                Log.d("JSON data", userData.getDouble("bmi") + "");
                bmiView.setText(bmiValue);
            }catch (Exception e) {
                Log.d("JSON Error", "");
                bmiView.setText("000");
            }

//            String data = result.substring(63);
//            Log.i("data", data);
        }
    }
}