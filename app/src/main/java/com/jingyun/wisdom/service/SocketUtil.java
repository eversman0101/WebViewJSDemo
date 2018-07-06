package com.jingyun.wisdom.service;

import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.jingyun.wisdom.webviewjsdemo.Alert;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Jinjingyun on 2018/2/9.
 * Modified by Jinjingyun on 2018/7/6.
 * 用于后台服务推送
 * 启动顺序：init-> ->reload
 * 流程：初始化，重连接，连接上后开始阻塞读取，异常断开时停止读取
 */

public class SocketUtil {
    Alert alert;
    private Context mContext;
    private UnpackFrame unpackFrame;
    private Socket socket=null;
    private String ip;
    private int port;
    private OutputStream outputStream;
    //
    private BufferedInputStream bis;
    private DataInputStream dis;
    private static SocketUtil instance=null;
    //判断断线重发的循环的标志位
    private boolean resendTag=true;
    //读取数据标志位
    private boolean readTag=true;
    /**
     * 连接状态：正常
     */
    public static final int STATUS_CONNECT = 0;
    /**
     * 连接状态：断开连接
     */
    public static final int STATUS_DISCONNECT = 1;
    /**
     * 连接状态：连接失败
     */
    public static final int STATUS_PAIRED_ERROR = 2;
    /**
     * 连接状态：还未建立连接
     */
    public static final int STATUS_UN_REQUEST = 3;
    /**
     * 状态
     */
    private final AtomicInteger fStatus = new AtomicInteger(STATUS_UN_REQUEST);

    public static SocketUtil getInstance()
    {
        if(instance==null)
            instance=new SocketUtil();
        return instance;
    }
    public void init(Context context,String ip,int port)
    {
        this.ip=ip;
        this.port=port;
        this.mContext=context;
        alert=new Alert(mContext);
        unpackFrame=new UnpackFrame();
    }
    public void connect(final String ip, final int port)
    {
        this.ip=ip;
        this.port=port;

        if(socket!=null)
            disconnect();
        new Thread(){
            @Override
            public void run() {
                try
                {
                    socket = new Socket(ip, port);
                    //socket.setSoTimeout(3000);
                    fStatus.set(STATUS_CONNECT);
                    resendTag=true;
                    readTag=true;
                    outputStream = socket.getOutputStream();
                    Log.i("SocketUtil","已连接");
                    readAuto();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
    @WorkerThread
    public synchronized void write(byte[] data)
    {
        try
        {
            if(socket==null)
                return;

            outputStream.write(data);
            outputStream.flush();
            Log.i("SocketUtil","发："+ByteUtil.BinaryToHexString(data));
            //outputStream.close();
        }catch (Exception ex)
        {
            fStatus.set(STATUS_DISCONNECT);
            ex.printStackTrace();
        }
    }
    @WorkerThread
    public synchronized byte[] read()
    {
        try{
            if(socket==null)
            {
                Log.i("SocketUtil","socket已关闭");
                return null;
            }
            bis=new BufferedInputStream(socket.getInputStream());
            dis=new DataInputStream(bis);

            byte[] bFrame=new byte[1024*10];
            int length=0,iRet=0;
            byte[] bframe;
            length=dis.read(bFrame);
            if(length!=-1)
            {
                bframe=new byte[length];
                System.arraycopy(bFrame,0,bframe,0,length);
                Log.d("SocketUtil","收:"+ByteUtil.BinaryToHexString(bframe));
                return bframe;
            }
        }catch (Exception ex)
        {
            fStatus.set(STATUS_DISCONNECT);
            ex.printStackTrace();
        }
        return null;
    }
    /*
   * 循环读取、阻塞
   * 读到报警命令(0x11,0x01,0x10)后报警
   * */
    public void readAuto()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(socket==null)
                        return;
                    //输入流
                    bis=new BufferedInputStream(socket.getInputStream());
                    dis=new DataInputStream(bis);
                    while(readTag)
                    {
                        try{
                            Thread.sleep(200);
                            byte[] bFrame=new byte[64];
                            int length=0;
                            byte[] bframe;
                            length=dis.read(bFrame);
                            if(length!=-1)
                            {
                                bframe=new byte[length];
                                System.arraycopy(bFrame,0,bframe,0,length);
                                Log.d("SocketUtil","收:"+ByteUtil.BinaryToHexString(bframe));
                                if(unpackFrame.fnGetWarnFlag(bframe)==1)
                                    alert.BeepAndShake(1000);
                                else
                                    continue;
                            }
                        }catch (Exception ex)
                        {
                            ex.printStackTrace();
                            Log.d("SocketUtil","读取异常,断开先");
                            readTag=false;
                            disconnectNoTag();
                        }
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

            }
        }).start();
    }


    public void disconnect()
    {
        try
        {
            if(dis!=null)
            dis.close();
            if(bis!=null)
            bis.close();
            if(outputStream!=null)
            outputStream.close();
            if(socket!=null)
            socket.close();

            socket=null;
            outputStream=null;
            dis=null;
            bis=null;

            fStatus.set(STATUS_DISCONNECT);
            //结束断线重连线程
            resendTag=false;
            readTag=false;
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //单纯的断线
    public void disconnectNoTag()
    {
        try
        {
            if(dis!=null)
                dis.close();
            if(bis!=null)
                bis.close();
            if(outputStream!=null)
                outputStream.close();
            if(socket!=null)
                socket.close();

            socket=null;
            outputStream=null;
            dis=null;
            bis=null;

            readTag=false;
            fStatus.set(STATUS_DISCONNECT);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    /*
    * 断线重连机制
    * */
    public void reload()
    {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(resendTag)
                    {
                        try{
                            Thread.sleep(3000);
                            if(socket!=null)
                            {
                                try{
                                    Thread.sleep(1000);
                                    socket.sendUrgentData(0xFF);
                                }catch (Exception ex)
                                {
                                    disconnectNoTag();
                                }
                            }
                            else
                            {
                                Log.i("SocketUtil","重新连接中...");
                                try
                                {
                                    socket = new Socket(ip, port);
                                    //socket.setSoTimeout(3000);
                                    fStatus.set(STATUS_CONNECT);
                                    resendTag=true;
                                    readTag=true;
                                    outputStream = socket.getOutputStream();
                                    Log.i("SocketUtil","已连接");
                                    readAuto();
                                }
                                catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                    socket=null;
                                    fStatus.set(STATUS_DISCONNECT);
                                    Log.i("SocketUtil","连接失败，10秒后尝试重连...");
                                    Thread.sleep(7000);
                                    continue;
                                }
                            }
                        }catch (Exception ex)
                        {
                            ex.printStackTrace();
                            try{
                                Log.i("SocketUtil","连接又失败，10秒后尝试重连...");
                                Thread.sleep(10*1000);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
    }
}
