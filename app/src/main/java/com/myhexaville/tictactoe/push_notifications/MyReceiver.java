package com.myhexaville.tictactoe.push_notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myhexaville.tictactoe.MainActivity;
import com.myhexaville.tictactoe.model.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.myhexaville.tictactoe.Constants.FIREBASE_CLOUD_FUNCTIONS_BASE;
import static com.myhexaville.tictactoe.Util.getCurrentUserId;

public class MyReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive: " + intent.getAction());
        FirebaseDatabase.getInstance().getReference().child("users")
                .child(getCurrentUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User me = dataSnapshot.getValue(User.class);

                        OkHttpClient client = new OkHttpClient();

                        String to = intent.getExtras().getString("to");
                        String withId = intent.getExtras().getString("withId");

                        String format = String
                                .format("%s/sendNotification?to=%s&fromPushId=%s&fromId=%s&fromName=%s&type=%s",
                                        FIREBASE_CLOUD_FUNCTIONS_BASE,
                                        to,
                                        me.getPushId(),
                                        getCurrentUserId(),
                                        me.getName(),
                                        intent.getAction());

                        Log.d(LOG_TAG, "onDataChange: " + format);
                        Request request = new Request.Builder()
                                .url(format)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                            }
                        });

                        if (intent.getAction().equals("accept")) {
                            String gameId = withId + "-" + getCurrentUserId();
                            FirebaseDatabase.getInstance().getReference().child("games")
                                    .child(gameId)
                                    .setValue(null);

                            context.startActivity(new Intent(context, MainActivity.class)
                                    .putExtra("type", "wifi")
                                    .putExtra("me", "o")
                                    .putExtra("gameId", gameId)
                                    .putExtra("with", withId));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
