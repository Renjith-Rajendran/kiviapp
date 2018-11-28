package firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import kivi.ugran.com.KiviApplication;
import kivi.ugran.com.launcher.R;
import kivi.ugran.com.launcher.TutorialActivity;

public class KiviFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.e("kivi", "firebase"+":onMessageReceived body "+ message.getNotification().getBody());
        Log.e("kivi", "firebase"+":onMessageReceived title "+ message.getNotification().getTitle());
        sendMyNotification(message.getNotification().getBody());
    }

    @Override public void onNewToken(String s) {
        super.onNewToken(s);
        KiviApplication.setDeviceToken(s);
        Log.e("kivi", "firebase:onNewToken"+":" +s);
    }


    //http://developine.com/firebase-cloud-messaging-push-notification-android-tutorial/
    private void sendMyNotification(String message) {

        //On click of notification it redirect to this Activity
        Intent intent = new Intent(this, TutorialActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My Firebase Push notification")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
