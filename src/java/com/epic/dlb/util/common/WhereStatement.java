/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.util.common;

/**
 *
 * @author kasun_n
 */
public class WhereStatement {
    
    private String property;
    private Object value;
    private String operator;
     private String nextOperator;

    public WhereStatement(String property, Object value, String operator) {
        this.property = property;
        this.value = value;
        this.operator = operator;
        
        if(operator.equals(SystemVarList.LIKE)){
            this.value="%"+value+"%";
        }
    }
    
     public WhereStatement(String property, Object value, String operator, String nextOperator) {
        this.property = property;
        this.value = value;
        this.operator = operator;
        this.nextOperator = nextOperator;

        if (operator.equals(SystemVarList.LIKE)) {
            this.value = "%" + value + "%";
        }
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    
   
    public String getNextOperator() {
        return nextOperator;
    }

    public void setNextOperator(String nextOperator) {
        this.nextOperator = nextOperator;
    }

   
    
    
}
