package loginpage.pradeep.loginpage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by pradeepbalakrishnan on 4/5/17.
 */

public class FireBaseMsgService extends FirebaseMessagingService {

    String end_time = "";
    String start_time = "";
    private static final String TAG = "MyFirebaseMsgService";
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//            displayNotification(remoteMessage.getData().get("data"), remoteMessage.getData().get("notification"));
//
//        //}
//    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: "+remoteMessage.getFrom());
          Log.d(TAG,"UserName"+remoteMessage.getData().get("username"));
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            start_time = remoteMessage.getData().get("start_time");
             end_time = remoteMessage.getData().get("end_time");

            Log.d(TAG, "End:Time " + end_time);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        displayNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle(),end_time,start_time);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void displayNotification(String contentText, String title,String start_time, String end_time){

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_fb_happyhours);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        //incase we need to expand the notification
//        String[] events = new String[6];
//// Sets a title for the Inbox in expanded layout
//        inboxStyle.setBigContentTitle("Event tracker details:");
//
//// Moves events into the expanded layout
//        for (int i=0; i < events.length; i++) {
//
//            inboxStyle.addLine(events[i]);
//        }
//// Moves the expanded layout object into the notification object.
//        builder.setStyle(inboxStyle);

        Intent intent = new Intent(this,NotificationActivity.class);
        intent.putExtra("notificationTitle",title);
        intent.putExtra("body",contentText);
        intent.putExtra("start_time",start_time);
        intent.putExtra("end_time",end_time);
        // The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pending = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pending);


        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(3, notification);

    }
}
