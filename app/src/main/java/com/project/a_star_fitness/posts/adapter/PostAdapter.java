package com.project.a_star_fitness.posts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.project.a_star_fitness.R;
import com.project.a_star_fitness.posts.model.Post;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostHolder> {
    private Context context;
    private final ArrayList<Post> itemList;
    private ItemClickListener listener;


    public PostAdapter(Context context, ArrayList<Post> itemList) {
        this.context=context;
        this.itemList = itemList;
    }
    public void setOnItemClickListener(ItemClickListener listener) {

        this.listener = listener;
    }
    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_format, parent, false);
        return new PostHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        Post currentItem = itemList.get(position);

        holder.post_content.setText(currentItem.getContent());
        holder.post_title.setText(currentItem.getTitle());
        holder.post_time.setText(currentItem.getTime().toString());
        holder.user_username.setText(currentItem.getAuthor());
        if(currentItem.getPicture()!=null){
                    Picasso.get()
               .load(currentItem.getPicture())

                .into(holder.post_image);

        }else{
            holder.post_image.setImageResource(R.drawable.ic_launcher_background);
        }




    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }
}
