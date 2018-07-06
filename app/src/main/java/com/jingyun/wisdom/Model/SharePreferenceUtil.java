package com.jingyun.wisdom.Model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jinjingyun on 2018/3/27.
 */

public class SharePreferenceUtil {
    /**
     * 温度轮训方式
     * */
    private static final String ROUTING = "routing";
    /*
    * -1为所有片区
    * 否则为根据ID查询单个片区
    * */
    private static final String ROUTING_TYPE="routing_type";


    /*
    * 记录报警次数
    * */
    private static final String WARN_COUNT="warn_count";
    /*
    * 所有片区的报警次数
    * */
    private static final String ALL_AREA_COUNT="all_area_count";
    /*
    * 单个片区的报警次数
    * */
    private static final String AREA_COUNT="area_count";


    public static int getRoutingType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                ROUTING, 0);
        if (preferences.contains(ROUTING_TYPE)) {
            return preferences.getInt(ROUTING_TYPE,-1);
        } else {
            return -1;
        }
    }
    public static void setRoutingType(Context context, int id)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                ROUTING, 0).edit();
        editor.putInt(ROUTING_TYPE, id);
        editor.commit();
    }

    public static int getAllAreaCount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                WARN_COUNT, 0);
        if (preferences.contains(ALL_AREA_COUNT)) {
            return preferences.getInt(ALL_AREA_COUNT,0);
        } else {
            return 0;
        }
    }
    public static void setAllAreaCount(Context context, int id)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                WARN_COUNT, 0).edit();
        editor.putInt(ALL_AREA_COUNT, id);
        editor.commit();
    }

    public static int getAreaCount(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                WARN_COUNT, 0);
        if (preferences.contains(AREA_COUNT)) {
            return preferences.getInt(AREA_COUNT,0);
        } else {
            return 0;
        }
    }
    public static void setAreaCount(Context context, int id)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                WARN_COUNT, 0).edit();
        editor.putInt(AREA_COUNT, id);
        editor.commit();
    }
}
