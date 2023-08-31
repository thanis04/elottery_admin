/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.dto.resultPublish;

import java.util.List;

/**
 *
 * @author nipuna_k
 */
public class DrawResultDto implements java.io.Serializable {

    private List<WinningNosDto> winningNos;
    private List<PrizesWonDto> prizesWon;
    private List<PrizesForNextDrawDto> prizesForNextDraw;

    public DrawResultDto() {
    }

    public List<WinningNosDto> getWinningNos() {
        return winningNos;
    }

    public void setWinningNos(List<WinningNosDto> winningNos) {
        this.winningNos = winningNos;
    }

    public List<PrizesWonDto> getPrizesWon() {
        return prizesWon;
    }

    public void setPrizesWon(List<PrizesWonDto> prizesWon) {
        this.prizesWon = prizesWon;
    }

    public List<PrizesForNextDrawDto> getPrizesForNextDraw() {
        return prizesForNextDraw;
    }

    public void setPrizesForNextDraw(List<PrizesForNextDrawDto> prizesForNextDraw) {
        this.prizesForNextDraw = prizesForNextDraw;
    }
    
    
}
