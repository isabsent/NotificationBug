package com.example.notificationbug;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.RemoteInput;

import static com.example.notificationbug.FirstFragment.ACTION_REPLY;
import static com.example.notificationbug.FirstFragment.EXTRA_TEXT_REPLY;
import static com.example.notificationbug.FirstFragment.NOTIFICATION_REQUEST_CODE;
import static com.example.notificationbug.FirstFragment.NOTIFICATION_TAG;

public class InlineReplyService extends IntentService {

    public InlineReplyService() {
        super(InlineReplyService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ACTION_REPLY.equals(intent.getAction())) {
            CharSequence replyText = RemoteInput.getResultsFromIntent(intent).getCharSequence(EXTRA_TEXT_REPLY);
            System.out.println(replyText);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null)
                notificationManager.cancel(NOTIFICATION_TAG, NOTIFICATION_REQUEST_CODE);
        }
    }
}
