package com.jingyun.wisdom.webviewjsdemo;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by jingyun on 2018/7/3.
 * 唤醒屏幕
 */

public class WakeAndLock {
    Context mContext;
    PowerManager pm;
    PowerManager.WakeLock wakeLock;
    final int WAKE_LOCK_TIMEOUT=5000;

    public WakeAndLock(Context context) {
        mContext = context;
        pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        wakeLock=pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Gank");
    }

    /**
     * 唤醒屏幕
     */
    public void screenOn() {
        if(!pm.isScreenOn()) {
            wakeLock.acquire(WAKE_LOCK_TIMEOUT);
            android.util.Log.i("WakeAndLock", "screen off->on");
        }else
            Log.i("WakeAndLock", "screen already on");
    }
}
