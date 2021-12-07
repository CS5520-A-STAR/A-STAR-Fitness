package com.project.a_star_fitness.posts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.project.a_star_fitness.R;
import com.project.a_star_fitness.posts.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class Post_creation extends AppCompatActivity {
    private ImageView add_post_image;
    private EditText edit_title,post_content;
    private Button post_create_btn;
    private DatabaseReference root;
    private ProgressBar progressBar;
    private Uri pickedImage=null;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creation);
        init();
        uploadImage();
        createPost();
    }
    private void init(){


//        FirebaseAuth auth1=FirebaseAuth.getInstance();
//        auth1.signInWithEmailAndPassword("a@abc.com","123456");
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        root= FirebaseDatabase.getInstance().getReference().child("post");
        add_post_image=findViewById(R.id.add_post_image);
        edit_title=findViewById(R.id.edit_title);
        post_content=findViewById(R.id.post_content);
        post_create_btn=findViewById(R.id.post_create_btn);
        progressBar=findViewById(R.id.progressBar_addPost);


    }
    private void createPost(){
        post_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_create_btn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                if(!edit_title.getText().toString().isEmpty()){
                    // StorageReference
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("blog_image");
                    final StorageReference path=storageRef.child(pickedImage.getLastPathSegment());
                    path.putFile(pickedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String link=uri.toString();

                                    // Map<String ,Object> map=new HashMap<>();
//
//                                    String temp=root.push().getKey();
//                                    DatabaseReference m1=root.child(temp);
//                                    Post newPost=new Post("a","aaa","sds","ss");
//                                    m1.setValue(newPost);
                                    Post newPost=new Post(edit_title.getText().toString()
                                            ,post_content.getText().toString()
                                            ,link,currentUser.getEmail(), Calendar.getInstance().getTime().toString());
                                    addPost(newPost);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Post_creation.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                    post_create_btn.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });



                }else{
                    Toast.makeText(Post_creation.this,"Invalid Input",Toast.LENGTH_SHORT).show();
                    post_create_btn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }


            }
        });
    }

    private void addPost(Post post){
        String key=root.push().getKey();
        DatabaseReference ref=root.child(key);
        post.setId(key);
        ref.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                post_create_btn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Post_creation.this,"Post added",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Post_creation.this, Post_list.class));

            }
        });
    }
    private void uploadImage(){
        add_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                // gallery();
            }
        });
    }
    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            System.out.println("a");
            if(ActivityCompat.shouldShowRequestPermissionRationale(Post_creation.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(Post_creation.this,"Please give permission",Toast.LENGTH_SHORT).show();;
            }else {

                ActivityCompat.requestPermissions(Post_creation.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }else{
            gallery();
        }
    }
    private void gallery(){
        Intent gallery=new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");

        startActivityForResult(gallery,1);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK&& requestCode==1&&data!=null){
            pickedImage=data.getData();
            add_post_image.setImageURI(pickedImage);
        }
    }
}