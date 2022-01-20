package io.xqs.bt.smspush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class PowerService extends Service {

    final private PowerInfoReceiver powerInfoReceiver = new PowerInfoReceiver();

    public PowerService() {
    }

    @Override
    public void onCreate() {
        String ID = "io.xqs.bt.smspush"; //这里的id里面输入自己的项目的包的路径
        String NAME = "Foreground Service";
        Intent intent = new Intent(PowerService.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder notificationBuilder; //创建服务对象
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(ID, NAME, manager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
            notificationBuilder = new NotificationCompat.Builder(PowerService.this).setChannelId(ID);
        } else {
            notificationBuilder = new NotificationCompat.Builder(PowerService.this);
        }
        notificationBuilder.setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("运行中...")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        Notification notification = notificationBuilder.build();
        startForeground(1,notification);

        registerReceiver(powerInfoReceiver, new IntentFilter(Intent.ACTION_POWER_CONNECTED));
        registerReceiver(powerInfoReceiver, new IntentFilter(Intent.ACTION_POWER_DISCONNECTED));
        registerReceiver(powerInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));
        registerReceiver(powerInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_OKAY));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(powerInfoReceiver);
    }

    private LocalBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {

        public PowerService getService(){
            return PowerService.this;
        }
    }


}