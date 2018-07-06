package com.jingyun.wisdom.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jingyun.wisdom.Model.AreaListModel;
import com.jingyun.wisdom.Model.JsonAllAreaResult;
import com.jingyun.wisdom.Model.JsonAreaResult;
import com.jingyun.wisdom.Model.JsonBaseResult;
import com.jingyun.wisdom.Model.SharePreferenceUtil;
import com.jingyun.wisdom.Model.WorkplaceModel;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import net.sf.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by jingyun on 2018/7/4.
 * 处理网络请求和判断报警的业务逻辑
 */

public class NetWorkUtil {
    Context mContext;
    public NetWorkUtil(Context context){
        this.mContext=context;
    }
    //
    private JsonBaseResult fnGetModel(int id)
    {
        try {
            String url;
            if(id==-1)
                url=Web.allArea;
            else
                url=Web.area+id;
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url(url)//请求接口。如果需要传参拼接到接口后面。
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.d("okhttp","response.code()=="+response.code());
                Log.d("okhttp","response.message()=="+response.message());

                String result=response.body().string();
                Log.d("okhttp","res=="+result);
                String sub_result=result.substring(1,result.length()-1);
                sub_result=sub_result.replace("\\\"","\"");
                sub_result=sub_result.replace("\\\\","\\");
                Log.d("okhttp","sub_result:"+sub_result);
                JSONObject object;
                if(id==-1)
                {
                    JsonAllAreaResult model=fnGetJsonAllArea(sub_result);
                    return model;
                }else
                {
                    JsonAreaResult model=fnGetJsonAreaResult(sub_result);
                    return model;
                }
            }
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //获取报警标志位，0为正常，1则报警。-1为异常（请求失败）
    public int fnGetWarnFlag()
    {
        //从sp中拿状态
        int id= SharePreferenceUtil.getRoutingType(mContext);
        //请求并获取json
        if(id==-1) {
            //全部片区
            JsonAllAreaResult result = (JsonAllAreaResult) fnGetModel(id);
            if(result.getErrorCode()!=0)
                return -1;
            AreaListModel model=result.getResult();
            if(model==null)
                return -1;
            //遍历片区
            //遍历工位，找出报警标志位为1的，加起来，与sp中的比较，小于则赋值并返回0，大于则赋值并返回1
            int warn_count=0;
            for(int i=0;i<model.getSize();i++)
            {
                for(int j=0;j<=model.getList().get(i).getList().size();j++)
                {
                    WorkplaceModel workplaceModel=model.getList().get(i).getList().get(j);
                    if(workplaceModel.getWarningState()==1)
                        warn_count++;
                }
            }
            int sp_warn_count=SharePreferenceUtil.getAllAreaCount(mContext);
            if(warn_count<sp_warn_count)
            {
                SharePreferenceUtil.setAllAreaCount(mContext,warn_count);
            }
        }
        else
        {

        }

        //解析出model
        //解析出报警标识位，有新增则1
        return 0;
    }
    //解析，得到全部片区全部工位温度数据
    private JsonAllAreaResult fnGetJsonAllArea(String jsonData) {
        Gson gson = new Gson();
        try
        {
            Type type=new TypeToken<JsonAllAreaResult>(){}.getType();
            JsonAllAreaResult model = gson.fromJson(jsonData, type);
            return model;
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
    //解析，得到某片区全部工位温度数据
    private JsonAreaResult fnGetJsonAreaResult(String jsonData)
    {
        Gson gson = new Gson();
        try
        {
            Type type=new TypeToken<JsonAreaResult>(){}.getType();
            JsonAreaResult model = gson.fromJson(jsonData, type);
            return model;
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
