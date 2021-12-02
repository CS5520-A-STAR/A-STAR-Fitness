package com.project.a_star_fitness;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOUser {

    private DatabaseReference databaseReference;

    public DAOUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        //databaseReference=db.getReference(GlobalClass.class.getSimpleName());
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public Task<Void> add(User user, String email) {
        return databaseReference.child("Users").child(email).setValue(user);
        //return databaseReference.push().setValue(user);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child("Users").child(key).updateChildren(hashMap);
    }

    public Query get() {
        return databaseReference.orderByKey();
    }


}
