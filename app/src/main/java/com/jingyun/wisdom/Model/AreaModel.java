package com.jingyun.wisdom.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jingyun on 2018/7/4.
 * 单个片区
 */

public class AreaModel implements Serializable{
    private int ID;
    private String Name;
    private ArrayList<WorkplaceModel> List;

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

    public ArrayList<WorkplaceModel> getList() {
        return List;
    }

    public void setList(ArrayList<WorkplaceModel> list) {
        List = list;
    }
}
