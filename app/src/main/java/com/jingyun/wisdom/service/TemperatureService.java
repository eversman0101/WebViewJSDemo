package com.jingyun.wisdom.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.jingyun.wisdom.webviewjsdemo.Alert;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.sf.json.JSONObject;

import java.net.Socket;

/**
 * Created by jingyun on 2018/7/4.
 * 温度轮训
 */

public class TemperatureService extends Service{
    NetWorkUtil netWorkUtil;
    Alert alert;
    boolean state=false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /**
     * 服务创建的时候调用
     */
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        alert=new Alert(getApplicationContext());
        netWorkUtil=new NetWorkUtil(getApplicationContext());
        Log.i("TemperatureService","=========Service onCreate======");
        //startLoop();
        initSocket();
    }
    /**
     * 服务启动的时候调用
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("TemperatureService","=========Service onStartCommand======");
        return super.onStartCommand(intent, flags, startId);
    }
    /**
     * 服务销毁的时候调用
     */
    @Override
    public void onDestroy() {
        Log.i("TemperatureService","=========Service onDestroy======");
        super.onDestroy();
        state = false;
        SocketUtil.getInstance().disconnect();
    }
    //温度轮训
    //2018-07-06 不方便，放弃此方案
    /*
    private void startLoop()
    {
        try{
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            while(state){
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                int iFlag=netWorkUtil.fnGetWarnFlag();
                                if(iFlag==1)
                                {
                                //没区分报警方式，待优化
                                alert.BeepAndShake(1000);
                                }
                            }
                        }
                    }
            ).start();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    */
    private void initSocket()
    {
        SocketUtil.getInstance().init(getApplicationContext(),Web.socketIP,Web.socketPort);
        SocketUtil.getInstance().reload();
    }
}
