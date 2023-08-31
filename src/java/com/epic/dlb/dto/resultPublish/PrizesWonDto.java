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
public class PrizesWonDto implements java.io.Serializable {
    private Boolean isPrizeWon;
    private String soldLocation;
    private String prizeDescription;
    private String prizeType;
    private String prizeValue;

    public PrizesWonDto() {
    }

    public Boolean getIsPrizeWon() {
        return isPrizeWon;
    }

    public void setIsPrizeWon(Boolean isPrizeWon) {
        this.isPrizeWon = isPrizeWon;
    }

    public String getSoldLocation() {
        return soldLocation;
    }

    public void setSoldLocation(String soldLocation) {
        this.soldLocation = soldLocation;
    }

    public String getPrizeDescription() {
        return prizeDescription;
    }

    public void setPrizeDescription(String prizeDescription) {
        this.prizeDescription = prizeDescription;
    }

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public String getPrizeValue() {
        return prizeValue;
    }

    public void setPrizeValue(String prizeValue) {
        this.prizeValue = prizeValue;
    }
    
    
}
