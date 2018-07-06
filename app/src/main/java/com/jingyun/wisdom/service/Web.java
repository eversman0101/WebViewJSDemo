package com.jingyun.wisdom.service;

/**
 * Created by jingyun on 2018/7/4.
 */

public class Web {
    //页面url
    private static String context="218.201.129.20:8104";
    //推送服务
    public static String socketIP="172.20.10.5";
    public static int socketPort=11111;

    public static String root="http://"+context+"/Basic/";
    public static String allArea="http://"+context+"/api/AllArea/Get";
    public static String area="http://"+context+"/api/Temperature/Get?id=";//后面直接加id
}
