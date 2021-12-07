package com.project.a_star_fitness.record;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.a_star_fitness.R;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private ImageView imageAddNote;
    private ListView list_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        list_notes = findViewById(R.id.list_notes);
        imageAddNote = findViewById(R.id.imageAddNote);
        imageAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecordActivity.this, AddActivity.class));
            }
        });

        ArrayList<Record> list = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<Record>(this, R.layout.list_item, list);

        list_notes.setAdapter(adapter);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Records").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for(DataSnapshot arecord : snapshot.getChildren()){
                    Record record = arecord.getValue(Record.class);
                    list.add(record);
                }


                //Log.d("snapshot", snapshot.getValue().toString());

                // cast to object
                // GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                // List<String> connectionOne = snapshot.getValue(t);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        list_notes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.remove(i);
                adapter.notifyDataSetChanged();
                Toast.makeText(RecordActivity.this, "deleted this record "+String.valueOf(i), Toast.LENGTH_SHORT).show();
            }
        });
    }
}