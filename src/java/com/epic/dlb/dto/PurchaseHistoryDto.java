/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

import java.util.Date;

/**
 *
 * @author kasun_n
 */
public class PurchaseHistoryDto {

    private Integer id;
    private String drawNo;
    private String drawDate;
    private String productDescription;
    private String lastUpdated;
    private String createdDate;
    private String winningPrize;
    private String username;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private int statusCode;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDrawNo() {
        return drawNo;
    }

    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String  drawDate) {
        this.drawDate = drawDate;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getWinningPrize() {
        return winningPrize;
    }

    public void setWinningPrize(String winningPrize) {
        this.winningPrize = winningPrize;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

 
    
    

}
