package babiy.planner;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        TaskDataBase dataBase = new TaskDataBase(context);
        ArrayList<Task> tasks = dataBase.getAllTasks("Current");
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String currentDate = df.format(Calendar.getInstance().getTime());
        DateFormat tdf = new SimpleDateFormat("HH:mm");
        String currentTime = tdf.format(Calendar.getInstance().getTime());
        for (Task ts : tasks) {
            String taskDate = ts.getDate();
            String taskTime = ts.getTime();

            if (taskTime.equals("")) {
                if (taskDate.equals(currentDate) && currentTime.equals("09:00")) {
                    showNotificationReminder(context, ts.getTask());
                }
            } else {
                if (taskDate.equals(currentDate) && taskTime.equals(currentTime)) {
                    showNotificationReminder(context, ts.getTask());
                }
            }

        }
    }

    private void showNotificationReminder(Context context, String text){
        Random random = new Random();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        long[] vibrate = new long[] { 1000, 1000, 1000, 1000, 1000 };
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(vibrate)
                .setContentTitle("Reminder")
                .setAutoCancel(true)
                .setContentText(text);
        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(random.nextInt(), notification);
    }
}
