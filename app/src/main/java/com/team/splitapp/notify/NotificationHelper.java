package com.team.splitapp.notify;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.team.splitapp.R;

/**
 * Everything about showing notifications. Owner: Ahmed.
 *
 * Android 8+ needs a notification channel before any notification is shown.
 * Android 13+ also needs the user to allow the POST_NOTIFICATIONS permission.
 */
public class NotificationHelper {

    public static final String CHANNEL_REMINDERS = "reminders";
    public static final int REQUEST_NOTIFICATION_PERMISSION = 100;

    /** Creates the channel. Safe to call many times; call it when the app starts. */
    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_REMINDERS,
                    context.getString(R.string.channel_reminders_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.channel_reminders_description));
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /** True if we are allowed to show notifications (always true before Android 13). */
    public static boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true;
        }
        return ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED;
    }

    /** Shows the system "Allow notifications?" dialog on Android 13+ if not allowed yet. */
    public static void requestPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasPermission(activity)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_NOTIFICATION_PERMISSION);
        }
    }

    /**
     * Shows a notification. Does nothing if the user did not allow notifications.
     *
     * @param id            use a different id for each notification you want to keep separately
     * @param openIntent    what opens when the user taps it (may be null)
     */
    public static void show(Context context, int id, String title, String text, PendingIntent openIntent) {
        if (!hasPermission(context)) {
            return;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_REMINDERS)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        if (openIntent != null) {
            builder.setContentIntent(openIntent);
        }
        try {
            NotificationManagerCompat.from(context).notify(id, builder.build());
        } catch (SecurityException e) {
            // Permission was removed between the check and notify(): nothing to show.
        }
    }
}
