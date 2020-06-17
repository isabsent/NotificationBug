package com.example.notificationbug;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {
    public static final String
            ACTION_REPLY = "com.jetico.messenger.action.REPLAY",
            EXTRA_TEXT_REPLY = "com.jetico.messenger.extra.TEXT_REPLY",
            NOTIFICATION_TAG = "com.jetico.messenger.NOTIFICATION_TAG";

    public static final int NOTIFICATION_REQUEST_CODE = 123;

    private static final String CHANNEL_ID = "DEFAULT_CHANNEL";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
    }

    private void sendNotification() {
        NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle("Title")
                .setAutoCancel(true)
                .addAction(getReplyAction(new Intent(getContext(), InlineReplyService.class).setAction(ACTION_REPLY)));


        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {// Since android Oreo notification channel is needed.
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Messages channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
        } else
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH); //To show Heads-up Notifications on pre-Oreo devices

        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_REQUEST_CODE, notificationBuilder.build());
    }

    private NotificationCompat.Action getReplyAction(Intent replayIntent){
        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_TEXT_REPLY)
                .setLabel("Type message")
                .build();

        PendingIntent replyPendingIntent = PendingIntent.getService(getContext(), NOTIFICATION_REQUEST_CODE, replayIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_send, "Reply", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();
    }
}
