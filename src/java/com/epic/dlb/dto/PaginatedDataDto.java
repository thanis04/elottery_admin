/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import java.util.List;
import org.json.simple.JSONArray;

/**
 *
 * @author nipuna_k
 */
public class PaginatedDataDto {

    private List list;
    private Integer count;
    private JSONArray array;

    public PaginatedDataDto(List list, Integer count) {
        this.list = list;
        this.count = count;
    }
    
    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public JSONArray getArray() {
        return array;
    }

    public void setArray(JSONArray array) {
        this.array = array;
    }
    
    
}
