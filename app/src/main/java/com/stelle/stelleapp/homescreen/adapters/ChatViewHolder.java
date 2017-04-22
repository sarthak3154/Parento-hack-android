package com.stelle.stelleapp.homescreen.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.stelle.stelleapp.R;
import com.stelle.stelleapp.widgets.AppTextView;

/**
 * Created by Sarthak on 22-04-2017
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {
    ImageView ivUserAvatar;
    AppTextView tvComment;
    AppTextView tvName;

    public ChatViewHolder(View view) {
        super(view);
        ivUserAvatar = (ImageView) itemView.findViewById(R.id.ivUserAvatar);
        tvComment = (AppTextView) itemView.findViewById(R.id.tvComment);
        tvName = (AppTextView) itemView.findViewById(R.id.tvName);
    }
}
