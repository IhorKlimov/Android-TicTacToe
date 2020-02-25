package com.myhexaville.tictactoe.push_notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import static com.myhexaville.tictactoe.Util.savePushToken;


public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MyFirebaseInstanceId";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.d(LOG_TAG, "onTokenRefresh: ");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            return;
        }

        savePushToken(refreshedToken, currentUser.getUid());
    }
}
