package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * DlbWbMainDrawPrizeCode generated by hbm2java
 */
@Entity
@Table(name="DLB_WB_MAIN_DRAW_PRIZE_CODE"
    
)
public class DlbWbMainDrawPrizeCode  implements java.io.Serializable {


     private DlbWbMainDrawPrizeCodeId id;
     private String drawPrize;
     private String statusCode;
     private String lastUpdatedUser;
     private Date lastUpdatedTime;
     private Date createdTime;

    public DlbWbMainDrawPrizeCode() {
    }

	
    public DlbWbMainDrawPrizeCode(DlbWbMainDrawPrizeCodeId id, String drawPrize, String statusCode) {
        this.id = id;
        this.drawPrize = drawPrize;
        this.statusCode = statusCode;
    }
    public DlbWbMainDrawPrizeCode(DlbWbMainDrawPrizeCodeId id, String drawPrize, String statusCode, String lastUpdatedUser, Date lastUpdatedTime, Date createdTime) {
       this.id = id;
       this.drawPrize = drawPrize;
       this.statusCode = statusCode;
       this.lastUpdatedUser = lastUpdatedUser;
       this.lastUpdatedTime = lastUpdatedTime;
       this.createdTime = createdTime;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="productCode", column=@Column(name="PRODUCT_CODE", nullable=false, length=16) ), 
        @AttributeOverride(name="prizeCode", column=@Column(name="PRIZE_CODE", nullable=false, length=16) ) } )
    public DlbWbMainDrawPrizeCodeId getId() {
        return this.id;
    }
    
    public void setId(DlbWbMainDrawPrizeCodeId id) {
        this.id = id;
    }

    
    @Column(name="DRAW_PRIZE", nullable=false, length=64)
    public String getDrawPrize() {
        return this.drawPrize;
    }
    
    public void setDrawPrize(String drawPrize) {
        this.drawPrize = drawPrize;
    }

    
    @Column(name="STATUS_CODE", nullable=false, length=16)
    public String getStatusCode() {
        return this.statusCode;
    }
    
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    
    @Column(name="LAST_UPDATED_USER", length=128)
    public String getLastUpdatedUser() {
        return this.lastUpdatedUser;
    }
    
    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="LAST_UPDATED_TIME", length=19)
    public Date getLastUpdatedTime() {
        return this.lastUpdatedTime;
    }
    
    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_TIME", length=19)
    public Date getCreatedTime() {
        return this.createdTime;
    }
    
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

}


