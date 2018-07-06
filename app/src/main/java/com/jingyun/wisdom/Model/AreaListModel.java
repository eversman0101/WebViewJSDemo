package com.jingyun.wisdom.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jingyun on 2018/7/4.
 * 全部片区
 */

public class AreaListModel implements Serializable{
    private int Size;
    private ArrayList<AreaModel> List;

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public ArrayList<AreaModel> getList() {
        return List;
    }

    public void setList(ArrayList<AreaModel> list) {
        List = list;
    }
}
