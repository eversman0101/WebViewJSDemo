package com.jingyun.wisdom.service;

/**
 * Created by jingyun on 2018/7/6.
 * 字节码解包
 */

public class UnpackFrame {
    public int fnGetWarnFlag(byte[] data)
    {
        if(data[0]==(byte)0x11&&data[1]==(byte)0x01&&data[2]==(byte)0x10)
        {
            return 1;
        }
        else
            return 0;
    }
}
