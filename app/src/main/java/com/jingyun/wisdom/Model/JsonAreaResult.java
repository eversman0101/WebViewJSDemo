package com.jingyun.wisdom.Model;

import java.io.Serializable;

/**
 * Created by jingyun on 2018/7/4.
 * 该片区的所有工位的温度数据
 */

public class JsonAreaResult extends JsonBaseResult implements Serializable{

    private AreaModel Result;

    public AreaModel getResult() {
        return Result;
    }

    public void setResult(AreaModel result) {
        Result = result;
    }
}
