package com.project.a_star_fitness.posts.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.project.a_star_fitness.R;

public class PostHolder  extends RecyclerView.ViewHolder{

    public ImageView post_image;
    public ImageView user_image;
    public TextView user_username;
    public TextView post_time;
    public TextView post_title;
    public TextView post_content;

    public  PostHolder(View itemView, final ItemClickListener listener){
        super(itemView);

        post_image=itemView.findViewById(R.id.post_image);
        user_image=itemView.findViewById(R.id.comment_user_image);
        user_username=itemView.findViewById(R.id.comment_username);
        post_time=itemView.findViewById(R.id.comment_time);
        post_title=itemView.findViewById(R.id.post_title);
        post_content=itemView.findViewById(R.id.post_content_1);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        listener.onItemClick(position);
                    }
                }
            }
        });


    }
}
