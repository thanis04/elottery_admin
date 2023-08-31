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
public class ResultPublishDto implements java.io.Serializable {

    private String drawDate;
    private String gameSeries;
    private String gameDescription;
    private String drawNo;
    private String resultStatus;
    private String publishedTime;
    private String nextDrawDate;
    private DrawResultDto drawResult;

    public ResultPublishDto(String drawDate, String gameSeries, String gameDescription, String drawNo, String resultStatus, String publishedTime, String nextDrawDate, DrawResultDto drawResult) {
        this.drawDate = drawDate;
        this.gameSeries = gameSeries;
        this.gameDescription = gameDescription;
        this.drawNo = drawNo;
        this.resultStatus = resultStatus;
        this.publishedTime = publishedTime;
        this.nextDrawDate = nextDrawDate;
        this.drawResult = drawResult;
    }

    public ResultPublishDto() {
    }

    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
    }

    public String getGameSeries() {
        return gameSeries;
    }

    public void setGameSeries(String gameSeries) {
        this.gameSeries = gameSeries;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public String getDrawNo() {
        return drawNo;
    }

    public void setDrawNo(String drawNo) {
        this.drawNo = drawNo;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getNextDrawDate() {
        return nextDrawDate;
    }

    public void setNextDrawDate(String nextDrawDate) {
        this.nextDrawDate = nextDrawDate;
    }

    public DrawResultDto getDrawResult() {
        return drawResult;
    }

    public void setDrawResult(DrawResultDto drawResult) {
        this.drawResult = drawResult;
    }

}
