package com.project.a_star_fitness.posts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.a_star_fitness.R;
import com.project.a_star_fitness.posts.adapter.CommentAdapter;
import com.project.a_star_fitness.posts.model.Comment;
import com.project.a_star_fitness.posts.model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class Post_detail extends AppCompatActivity {
    private ImageView detail_user_image;
    private TextView detail_username;
    private TextView detail_time;
    private ImageView detail_image;
    private TextView detail_content;
    private TextView detail_title;
    private EditText edit_comment;
    private Button comment_btn;
    private ProgressBar detail_progressbar;
    private RecyclerView recyclerView;
    private Post curPost;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> comment_list =new ArrayList<>();
    private DatabaseReference root ;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        init();
        initRecyclerView();
        curPost=(Post) getIntent().getSerializableExtra("post");
        postData();
        initialCommentData(savedInstanceState);
        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createComment();
            }
        });
    }
    private void createComment(){
        comment_btn.setVisibility(View.INVISIBLE);
        detail_progressbar.setVisibility(View.VISIBLE);

        if(!edit_comment.getText().toString().isEmpty()){


//                            Post newPost=new Post(edit_title.getText().toString()
//                                    ,post_content.getText().toString()
//                                    ,link,currentUser.getEmail(), Calendar.getInstance().getTime().toString());
//                            addPost(newPost);
            Comment newComment=new Comment(detail_content.getText().toString(),currentUser.getEmail(),Calendar.getInstance().getTime().toString());
            addComment(newComment);



        }else{
            Toast.makeText(Post_detail.this,"Invalid Input",Toast.LENGTH_SHORT).show();
            comment_btn.setVisibility(View.VISIBLE);
            detail_progressbar.setVisibility(View.INVISIBLE);
        }
    }
    private void addComment(Comment comment){
        String key=root.push().getKey();
        DatabaseReference ref=root.child(curPost.getId()+"/"+key);
        comment.setId(key);
        ref.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

               comment_btn.setVisibility(View.VISIBLE);
                detail_progressbar.setVisibility(View.INVISIBLE);
                Toast.makeText(Post_detail.this,"comment added",Toast.LENGTH_SHORT).show();


            }
        });
    }
    private void init(){
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        root= FirebaseDatabase.getInstance().getReference().child("comment");
        detail_progressbar=findViewById(R.id.detail_progressbar);
        detail_user_image=findViewById(R.id.detail_user_image);
        detail_username=findViewById(R.id.detail_username);
        detail_time=findViewById(R.id.detail_time);
        detail_image=findViewById(R.id.detail_image);
        detail_content=findViewById(R.id.detail_content);
        detail_title=findViewById(R.id.detail_title);
        edit_comment=findViewById(R.id.edit_comment);
        comment_btn=findViewById(R.id.comment_btn);

    }
    private void postData(){

        detail_username.setText(curPost.getAuthor());
        detail_time.setText(curPost.getTime());
        Picasso.get().load(curPost.getPicture()).into(detail_image);
        detail_content.setText(curPost.getContent());
        detail_title.setText(curPost.getTitle());
    }
    private void initRecyclerView(){
        recyclerLayoutManager=new LinearLayoutManager(this);
        recyclerView=findViewById(R.id.detail_recyclerView);
        recyclerView.setHasFixedSize(true);
        commentAdapter=new CommentAdapter(this,comment_list);

        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(recyclerLayoutManager);
    }
    private void initialCommentData(Bundle savedInstanceState){
        DatabaseReference root1=root.child(curPost.getId());
        root1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clearList();

                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Comment c=dataSnapshot.getValue(Comment.class);


                    comment_list.add(c);


                    commentAdapter.notifyItemInserted(comment_list.size());
                    recyclerView.scrollToPosition(comment_list.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void clearList(){
        int size = comment_list.size();
        comment_list.clear();
        commentAdapter.notifyItemRangeRemoved(0, size);
    }
}