package com.project.a_star_fitness.posts.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.project.a_star_fitness.R;
import com.project.a_star_fitness.posts.model.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {
    private Context context;
    private final ArrayList<Comment> itemList;
    private ItemClickListener listener;

    public CommentAdapter(Context context, ArrayList<Comment> itemList) {
        this.context=context;
        this.itemList = itemList;
    }
    public void setOnItemClickListener(ItemClickListener listener) {

        this.listener = listener;
    }
    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_format, parent, false);
        return new CommentHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment currentItem = itemList.get(position);
        holder.comment_user_image.setImageResource(R.drawable.ic_baseline_account_box);
        holder.comment_content.setText(currentItem.getContent());
        holder.comment_time.setText(currentItem.getTime());
        holder.comment_username.setText(currentItem.getAuthor());
       // holder.comment_user_image




    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }
}
