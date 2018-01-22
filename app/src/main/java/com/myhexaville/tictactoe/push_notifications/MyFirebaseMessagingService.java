package com.myhexaville.tictactoe.push_notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.myhexaville.tictactoe.MainActivity;
import com.myhexaville.tictactoe.R;
import com.myhexaville.tictactoe.StartActivity;

import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;
import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;
import static com.myhexaville.tictactoe.Util.getCurrentUserId;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String LOG_TAG = "MyFirebaseMessaging";
    public static final String INVITE = "invite";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String fromPushId = remoteMessage.getData().get("fromPushId");
        String fromId = remoteMessage.getData().get("fromId");
        String fromName = remoteMessage.getData().get("fromName");
        String type = remoteMessage.getData().get("type");
        Log.d(LOG_TAG, "onMessageReceived: ");

        if (type.equals("invite")) {
            handleInviteIntent(fromPushId, fromId, fromName);
        } else if (type.equals("accept")) {
            startActivity(new Intent(getBaseContext(), MainActivity.class)
                    .putExtra("type", "wifi")
                    .putExtra("me", "x")
                    .putExtra("gameId", getCurrentUserId() + "-" + fromId)
                    .putExtra("withId", fromId));
        } else if (type.equals("reject")) {
            // todo update to Oreo notifications
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setPriority(PRIORITY_MAX)
                            .setContentTitle(String.format("%s rejected your invite!", fromName));

            Intent resultIntent = new Intent(this, MainActivity.class)
                    .putExtra("type", "wifi");
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(StartActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    private void handleInviteIntent(String fromPushId, String fromId, String fromName) {
        Intent rejectIntent = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("reject")
                .putExtra("withId", fromId)
                .putExtra("to", fromPushId);

        PendingIntent pendingIntentReject = PendingIntent.getBroadcast(this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String gameId = fromId + "-" + getCurrentUserId();
//            FirebaseDatabase.getInstance().getReference().child("games")
//                    .child(gameId)
//                    .setValue(null);
        Intent acceptIntent = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("accept")
                .putExtra("withId", fromId)
                .putExtra("to", fromPushId);
        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 2, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resultIntent = new Intent(this, MainActivity.class)
                .putExtra("type", "wifi")
                .putExtra("withId", fromId)
                .putExtra("to", fromPushId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(StartActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        android.app.Notification build = new NotificationCompat.Builder(this, INVITE)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(PRIORITY_MAX)
                .setContentTitle(String.format("%s invites you to play!", fromName))
                .addAction(R.drawable.accept, "Accept", pendingIntentAccept)
                .setVibrate(new long[3000])
                .setChannelId(INVITE)
                .setContentIntent(resultPendingIntent)
                .addAction(R.drawable.cancel, "Reject", pendingIntentReject)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(INVITE, INVITE, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(1, build);
    }
}
