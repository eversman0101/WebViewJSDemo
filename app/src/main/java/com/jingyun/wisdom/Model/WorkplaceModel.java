package com.jingyun.wisdom.Model;

import java.io.Serializable;

/**
 * Created by jingyun on 2018/7/4.
 * 工位
 */

public class WorkplaceModel implements Serializable{
    private int ID;
    private String Name;
    private double Temperature;
    private double TemperatureDiff;
    public String Data;
    public int WarningState;
    public int RobbingState;
    public int WarningMethod;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getTemperature() {
        return Temperature;
    }

    public void setTemperature(double temperature) {
        Temperature = temperature;
    }

    public double getTemperatureDiff() {
        return TemperatureDiff;
    }

    public void setTemperatureDiff(double temperatureDiff) {
        TemperatureDiff = temperatureDiff;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public int getWarningState() {
        return WarningState;
    }

    public void setWarningState(int warningState) {
        WarningState = warningState;
    }

    public int getRobbingState() {
        return RobbingState;
    }

    public void setRobbingState(int robbingState) {
        RobbingState = robbingState;
    }

    public int getWarningMethod() {
        return WarningMethod;
    }

    public void setWarningMethod(int warningMethod) {
        WarningMethod = warningMethod;
    }
}
