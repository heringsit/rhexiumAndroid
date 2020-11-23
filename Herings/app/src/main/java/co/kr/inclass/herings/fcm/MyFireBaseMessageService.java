package co.kr.inclass.herings.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.Iterator;
import java.util.Map;

import co.kr.inclass.herings.HeringsApplication;
import co.kr.inclass.herings.MainActivity;
import co.kr.inclass.herings.R;
import co.kr.inclass.herings.model.PushData;

/**
 * Created by genius on 2017. 8. 9..
 */

public class MyFireBaseMessageService extends FirebaseMessagingService
{
    static final String TAG = "PUSH";

    private Gson gson = new Gson();

    @Override
    public void onNewToken(String s) {
        HeringsApplication application = (HeringsApplication) getApplicationContext();
        application.setPushToken(s);
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
//        Log.d(TAG, "noti title : " + remoteMessage.getNotification().getTitle());
        Log.d(TAG, "from : " + remoteMessage.getFrom());
        Map<String, String> data =  remoteMessage.getData();
        Log.d(TAG, "data is null? " + (data == null) );
        Log.d(TAG, "data : " + data.toString());
        Iterator<String> keyIt = data.keySet().iterator();
        PushData pushData = new PushData();
        while(keyIt.hasNext())
        {
            String key = keyIt.next();
            String value = data.get(key);

            if(key.equals("MsgTitle")) {
                pushData.title = value;
            }
            if(key.equals("MsgBody")) {
                pushData.content = value;
            }

            if(key.equals("DeviceToken")) {
                pushData.token = value;
            }

            if(key.equals("MsgLink")) {
                pushData.link = value;
            }
        }
//        parseBody(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        // for test
//        if (pushData.title.isEmpty()) {
//            pushData.title = "테스트";
//            pushData.content = "테스트 메시지입니다.";
//        }
//        if (pushData.link.isEmpty()) {
//            pushData.link = "/community/detail.asp?board_id=1";
//        }
        sendNotify(this, pushData);
    }

    private void parseBody(String title, String body)
    {
//        FileLog.d(TAG, "title : " + title + ", body : " + body);
    }

    private void sendNotify(Context context, PushData pushData)
    {
        //type변수를 추가하여 처음으로 이동할 페이지 설정
        /*
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(msg);

        PendingIntent intent = null;

        Intent resultIntent = new Intent(context, SplashActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(intent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.

        Notification noti = mBuilder.build();
        noti.defaults |= Notification.DEFAULT_SOUND;
        noti.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager.notify(msgID, noti);
        */

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("link", pushData.link);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        PendingIntent pendingIntent= PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "100";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_icon)
                .setContentTitle(pushData.title)
                .setContentText(pushData.content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification 채널을 설정합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channelId, pushData.title, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
