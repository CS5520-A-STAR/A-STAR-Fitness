package com.project.a_star_fitness.record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.project.a_star_fitness.R;

public class RecordActivity extends AppCompatActivity {

    private ImageView imageAddNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        imageAddNote = findViewById(R.id.imageAddNote);
        imageAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordActivity.this, AddActivity.class));
            }
        });


    }
}