package com.sp.mm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class ReminderBroadcast extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyLemubit")
                .setSmallIcon(R.drawable.ic_time)
                .setContentText("Hey students")
                .setContentTitle("Reminder")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(200,builder.build());
    }
}
