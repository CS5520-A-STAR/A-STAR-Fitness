package com.project.a_star_fitness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Locale;

public class BodyActivity extends AppCompatActivity {
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Users");
    private TextView bmiView;
    private TextView heightView;
    private TextView weightView;
//    private TextView ageView;

    private TextView neckInput;
    private TextView waistInput;
    private TextView hipInput;
    private TextView bodyFatView;

    private String gender;
    private String age;

    private Button bmiButton;
    private Button bodyFatButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body);

        bmiView = findViewById(R.id.bmiView);
//        ageView = findViewById(R.id.ageView);
        heightView = findViewById(R.id.heightView);
        weightView = findViewById(R.id.weightView);
        bmiButton = findViewById(R.id.bmiButton);

        neckInput = findViewById(R.id.neckInput);
        waistInput = findViewById(R.id.waistInput);
        hipInput = findViewById(R.id.hipInput);
        bodyFatView = findViewById(R.id.bodyFatView);
        bodyFatButton = findViewById(R.id.bodyFatButton);

        bmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetBMITask bmiTask = new GetBMITask();
                bmiTask.execute();
            }
        });

        bodyFatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetBodyFatTask bodyFatTask = new GetBodyFatTask();
                bodyFatTask.execute();
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
//                        ageView.setText(defaultAge);
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
                        heightView.setText(defaultHeight);
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
                        weightView.setText(defaultWeight);
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
                                age + "&weight=" + weightInKg
                                + "&height=" + heightView.getText().toString())
                        .get()
                        .addHeader("x-rapidapi-host", "fitness-calculator.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "e3da6c4171mshb00bfe19ec86053p195a81jsn82f7c1f971ba")
                        .build();

                Log.d("Params","age is " + age + " weight is "
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

    private class GetBodyFatTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            String result = null;

            try {
                OkHttpClient client = new OkHttpClient();

                int weightInKg = (int) (Integer.parseInt(weightView.getText().toString()) / 2.2);

                Request request = new Request.Builder()
                        .url("https://fitness-calculator.p.rapidapi.com/bodyfat?age=" + age
                                + "&gender=" + gender + "&weight=" + weightInKg + "&height="
                                + heightView.getText().toString() + "&neck="
                                + neckInput.getText().toString() + "&waist="
                                + waistInput.getText().toString() + "&hip=" + hipInput.getText().toString())
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

                String bodyFatValue = userData.getDouble("Body Fat (U.S. Navy Method)") + "";

                Log.d("JSON data", bodyFatValue);

                bodyFatView.setText(bodyFatValue);
            }catch (Exception e) {
                Log.d("JSON Error", "");
                Toast.makeText(getApplicationContext(), "Invalid body data.", Toast.LENGTH_SHORT).show();
                bodyFatView.setText("000");
            }
        }
    }
}