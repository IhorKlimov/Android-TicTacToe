package br.com.gorio.jogodavelha.push_notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.gorio.jogodavelha.MainActivity;
import br.com.gorio.jogodavelha.R;
import br.com.gorio.jogodavelha.StartActivity;

import static androidx.core.app.NotificationCompat.PRIORITY_MAX;
import static br.com.gorio.jogodavelha.Util.getCurrentUserId;

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

        switch (type) {
            case "invite":
                handleInviteIntent(fromPushId, fromId, fromName);
                break;
            case "accept":
                startActivity(new Intent(getBaseContext(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("type", "wifi")
                        .putExtra("me", "x")
                        .putExtra("gameId", getCurrentUserId() + "-" + fromId)
                        .putExtra("withId", fromId));
                break;
            case "reject":
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
                break;
        }
    }

    private void handleInviteIntent(String fromPushId, String fromId, String fromName) {
        Intent rejectIntent = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("reject")
                .putExtra("withId", fromId)
                .putExtra("to", fromPushId);

        PendingIntent pendingIntentReject = PendingIntent.getBroadcast(this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String gameId = fromId + "-" + getCurrentUserId();
        FirebaseDatabase.getInstance().getReference().child("games")
                .child(gameId)
                .setValue(null);
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
