package com.myhexaville.tictactoe.wifi;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myhexaville.tictactoe.R;
import com.myhexaville.tictactoe.Util;
import com.myhexaville.tictactoe.databinding.ActivityUserListBinding;
import com.myhexaville.tictactoe.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {
    private static final String LOG_TAG = "UserListActivity";
    private List<User> users = new ArrayList<>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_list);

        adapter = new Adapter(this, users);
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new LinearLayoutManager(this));

        fetchUsers();
    }

    private void fetchUsers() {
        FirebaseDatabase.getInstance().getReference().child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            if (!snapshot.getKey().equals(Util.getCurrentUserId())) {
                                users.add(user);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
