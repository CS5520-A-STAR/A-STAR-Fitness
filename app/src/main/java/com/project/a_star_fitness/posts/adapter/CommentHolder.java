package com.project.a_star_fitness.posts.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.a_star_fitness.R;

public class CommentHolder extends RecyclerView.ViewHolder{

    public ImageView comment_user_image;
    public TextView comment_username;
    public TextView comment_time;

    public TextView comment_content;

    public CommentHolder(View itemView, final ItemClickListener listener){
        super(itemView);

        comment_user_image=itemView.findViewById(R.id.comment_user_image);
        comment_username=itemView.findViewById(R.id.comment_username);
        comment_time=itemView.findViewById(R.id.comment_time);
        comment_content=itemView.findViewById(R.id.comment_content);

//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null) {
//                    int position = getLayoutPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//
//                        listener.onItemClick(position);
//                    }
//                }
//            }
//        });


    }
}
