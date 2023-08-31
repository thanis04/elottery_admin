/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto;

/**
 *
 * @author nipuna_k
 */
public class AgentSalesDto {
    
    private String gameType;
    private String noOfTickets;

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getNoOfTickets() {
        return noOfTickets;
    }

    public void setNoOfTickets(String noOfTickets) {
        this.noOfTickets = noOfTickets;
    }
}
