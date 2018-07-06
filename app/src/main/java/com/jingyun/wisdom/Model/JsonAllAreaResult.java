package com.jingyun.wisdom.Model;

import java.io.Serializable;

/**
 * Created by jingyun on 2018/7/4.
 * 所有片区的所有工位的温度数据
 */

public class JsonAllAreaResult extends JsonBaseResult implements Serializable{

    private AreaListModel Result;

    public AreaListModel getResult() {
        return Result;
    }

    public void setResult(AreaListModel result) {
        Result = result;
    }

}
