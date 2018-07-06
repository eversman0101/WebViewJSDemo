package com.jingyun.wisdom.Model;

import java.io.Serializable;

/**
 * Created by jingyun on 2018/7/5.
 */

public class JsonBaseResult implements Serializable{
    private String Reason;
    private int ErrorCode;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }
}
