package com.teclan.desktop.client.utils;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

public class Objects {

    public static JSONArray list2JSONArray(List<String> list){

        JSONArray array = new JSONArray();
        for(String o:list){
            array.add(o);
        }
        return array;
    }
}
