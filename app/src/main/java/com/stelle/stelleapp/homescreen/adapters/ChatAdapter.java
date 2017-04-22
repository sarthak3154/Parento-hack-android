package com.stelle.stelleapp.homescreen.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.stelle.stelleapp.R;
import com.stelle.stelleapp.homescreen.models.ChatDataModel;
import com.stelle.stelleapp.widgets.CircleTransform;

/**
 * Created by Sarthak on 22-04-2017
 */

public class ChatAdapter extends FirebaseRecyclerAdapter<ChatDataModel, ChatViewHolder> {
    private Context context;
    private int itemsCount = 0;

    /**
     * @param modelClass      Firebase will marshall the data at a location into
     *                        an instance of a class that you provide
     * @param modelLayout     This is the layout used to represent a single item in the list.
     *                        You will be responsible for populating an instance of the corresponding
     *                        view with the data from an instance of modelClass.
     * @param viewHolderClass The class that hold references to all sub-views in an instance modelLayout.
     * @param ref             The Firebase location to watch for data changes. Can also be a slice of a location,
     *                        using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public ChatAdapter(Context context, Class<ChatDataModel> modelClass, int modelLayout, Class<ChatViewHolder> viewHolderClass, Query ref) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
    }

    @Override
    protected void populateViewHolder(ChatViewHolder viewHolder, ChatDataModel model, int position) {
        if (model.getName() != null) {
            viewHolder.tvName.setText(model.getName());
        }
        if (model.getText() != null) {
            viewHolder.tvComment.setText(model.getText());
        }
        if (model.getPhotoUrl() != null) {
            Picasso.with(context)
                    .load(model.getPhotoUrl())
                    .transform(new CircleTransform())
                    .into(viewHolder.ivUserAvatar);
        } else {
            viewHolder.ivUserAvatar.setImageDrawable(ContextCompat.getDrawable(context,
                    R.drawable.bg_chat_avatar));
        }
    }
}
