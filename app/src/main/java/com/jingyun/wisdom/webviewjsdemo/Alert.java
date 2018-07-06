package com.jingyun.wisdom.webviewjsdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.webkit.JavascriptInterface;

import java.util.Random;

/**
 * Created by jingyun on 2018/6/23.
 * 震动与声音
 */

public class Alert {
    Context mContext;
    WakeAndLock wake;
    public Alert(Context context)
    {
     this.mContext=context;
     wake=new WakeAndLock(mContext);
    }
    @JavascriptInterface
    public void Beep()
    {
        Log.e("Alert","此处应有响铃");
        PlaySound(mContext);
    }
    @JavascriptInterface
    public void Shake(int millseconds)
    {
        Log.e("Alert","此处应有震动，时长："+millseconds);
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(millseconds);
        }
    }
    @JavascriptInterface
    public void BeepAndShake(int millseconds)
    {
        Log.e("Alert","此处应有震动响铃，时长："+millseconds);
        wake.screenOn();
        PlaySound7();
        //Shake(millseconds);
        NotificationUtils notificationUtils=new NotificationUtils(mContext,R.mipmap.ic_main,"提示：","温升报警");
        notificationUtils.notifyed();

    }
    private int PlaySound(final Context context) {
       try
       {
           NotificationManager mgr = (NotificationManager) context
                   .getSystemService(Context.NOTIFICATION_SERVICE);
           Notification nt = new Notification();
           nt.defaults = Notification.DEFAULT_SOUND;
           int soundId = new Random(System.currentTimeMillis())
                   .nextInt(Integer.MAX_VALUE);
           mgr.notify(soundId, nt);
           return soundId;
       }catch (Exception ex)
       {
           ex.printStackTrace();
       }
       return 0;
    }
    private void PlaySound7()
    {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Ringtone rt = RingtoneManager.getRingtone(mContext, uri);

        rt.play();


    }
}
