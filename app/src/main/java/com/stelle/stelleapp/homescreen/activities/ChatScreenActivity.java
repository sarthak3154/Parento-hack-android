package com.stelle.stelleapp.homescreen.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.stelle.stelleapp.BaseActivity;
import com.stelle.stelleapp.R;
import com.stelle.stelleapp.dbmodels.UserProfileData;
import com.stelle.stelleapp.homescreen.adapters.ChatAdapter;
import com.stelle.stelleapp.homescreen.adapters.ChatViewHolder;
import com.stelle.stelleapp.homescreen.interfaces.ChatScreenContract;
import com.stelle.stelleapp.homescreen.models.ChatDataModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Sarthak on 22-04-2017
 */

public class ChatScreenActivity extends BaseActivity implements ChatScreenContract.View {

    @Bind(R.id.contentRoot)
    LinearLayout contentRoot;
    @Bind(R.id.rvComments)
    RecyclerView rvComments;
    @Bind(R.id.llAddComment)
    LinearLayout llAddComment;
    @Bind(R.id.etComment)
    EditText etComment;
    @Bind(R.id.btnSendComment)
    AppCompatButton btnSendComment;
    @Bind(R.id.toolbarComments)
    Toolbar toolbar;
    @Bind(R.id.layoutProgressBar)
    LinearLayout progressBar;
    LinearLayoutManager linearLayoutManager;

    public static final int DEFAULT_COMMENTS_LENGTH_LIMIT = 100;

    private static final String KEY_SENDER_NAME = "name";
    private static final String KEY_UID = "uid";
    private static final String KEY_TEXT = "text";
    private static final String KEY_PHOTO_URL = "photoUrl";

    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseRecyclerAdapter<ChatDataModel, ChatViewHolder> chatsAdapter;
    public static final String MESSAGES_CHILD = "messages";

    Query queryRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(false);
            }
        }
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        rvComments.setLayoutManager(linearLayoutManager);
        // Firebase Initializing
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        queryRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        chatsAdapter = new ChatAdapter(this, ChatDataModel.class, R.layout.item_chat, ChatViewHolder.class, queryRef);

        chatsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                progressBar.setVisibility(View.GONE);
                int commentMessageCount = chatsAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.

                if (lastVisiblePosition == -1 ||
                        (positionStart >= (commentMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvComments.scrollToPosition(positionStart);
                }
            }

            @Override
            public void onChanged() {
                super.onChanged();
                progressBar.setVisibility(View.GONE);
            }
        });

        rvComments.setAdapter(chatsAdapter);
        etComment.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_COMMENTS_LENGTH_LIMIT)});
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    btnSendComment.setEnabled(true);
                } else {
                    btnSendComment.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @OnClick(R.id.btnSendComment)
    public void onClickSendComment() {
        UserProfileData userProfileData = UserProfileData.getUserData();
        if (userProfileData != null) {
            Map<String, Object> message = new HashMap<>();
            message.put(KEY_SENDER_NAME, userProfileData.getName());
            message.put(KEY_UID, userProfileData.getuId());
            message.put(KEY_TEXT, etComment.getText().toString());
            message.put(KEY_PHOTO_URL, userProfileData.getImageUrl());
            mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(message);
            etComment.setText("");
        }
    }
}
