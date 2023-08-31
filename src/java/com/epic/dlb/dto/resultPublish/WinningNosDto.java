/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto.resultPublish;

/**
 *
 * @author nipuna_k
 */
public class WinningNosDto implements java.io.Serializable {
    private String id;
    private String value;

    public WinningNosDto() {
    } 
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
}
