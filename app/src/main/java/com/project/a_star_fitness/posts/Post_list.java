package com.project.a_star_fitness.posts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.a_star_fitness.R;
import com.project.a_star_fitness.posts.adapter.ItemClickListener;
import com.project.a_star_fitness.posts.adapter.PostAdapter;
import com.project.a_star_fitness.posts.model.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Post_list extends AppCompatActivity {

    private FloatingActionButton addPost_btn;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ArrayList<Post> post_list =new ArrayList<>();
    private DatabaseReference root ;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        auth=FirebaseAuth.getInstance();

        addPost_btn=findViewById(R.id.add_post_btn);
        addPost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Post_list.this, Post_creation.class));
            }
        });
        initRecyclerView();
        initialPostData(savedInstanceState);

    }
    private void clearList(){
        int size = post_list.size();
        post_list.clear();
        postAdapter.notifyItemRangeRemoved(0, size);
    }
    private void initRecyclerView(){
        root= FirebaseDatabase.getInstance().getReference().child("post");
        recyclerLayoutManager=new LinearLayoutManager(this);
        recyclerView=findViewById(R.id.post_recycler_view);
        recyclerView.setHasFixedSize(true);
        postAdapter=new PostAdapter(this,post_list);
        ItemClickListener itemClickListener=new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                postAdapter.notifyItemChanged(position);
                Post cur=post_list.get(position);
                Intent detail=new Intent(Post_list.this,Post_detail.class);
                detail.putExtra("post",cur);
                startActivity(detail);
            }

            @Override
            public void onCheckBoxClick(int position) {

            }
        };
        postAdapter.setOnItemClickListener(itemClickListener);
        recyclerView.setAdapter(postAdapter);
        recyclerView.setLayoutManager(recyclerLayoutManager);
    }

    private void initialPostData(Bundle savedInstanceState){

            root.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     clearList();

                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Post p=dataSnapshot.getValue(Post.class);


                        post_list.add(p);


                        postAdapter.notifyItemInserted(post_list.size());
                        recyclerView.scrollToPosition(post_list.size()-1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





    }

}