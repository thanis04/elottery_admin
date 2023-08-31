package com.epic.dlb.model;
// Generated Oct 1, 2018 7:23:18 PM by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * DlbEzCashCheck generated by hbm2java
 */
@Entity
@Table(name="DLB_EZ_CASH_CHECK"
   
)
public class DlbEzCashCheck  implements java.io.Serializable {


     private String txnId;
     private DlbStatus dlbStatus;
     private DlbSwtStTransaction dlbSwtStTransaction;
     private Integer count;
     private String desc;
     private Date createDate;

    public DlbEzCashCheck() {
    }

	
    public DlbEzCashCheck(DlbSwtStTransaction dlbSwtStTransaction) {
        this.dlbSwtStTransaction = dlbSwtStTransaction;
    }
    public DlbEzCashCheck(DlbStatus dlbStatus, DlbSwtStTransaction dlbSwtStTransaction, Integer count, String desc, Date createDate) {
       this.dlbStatus = dlbStatus;
       this.dlbSwtStTransaction = dlbSwtStTransaction;
       this.count = count;
       this.desc = desc;
       this.createDate = createDate;
    }
   
     @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="dlbSwtStTransaction"))@Id @GeneratedValue(generator="generator")

    
    @Column(name="TxnId", unique=true, nullable=false, length=32)
    public String getTxnId() {
        return this.txnId;
    }
    
    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="status")
    public DlbStatus getDlbStatus() {
        return this.dlbStatus;
    }
    
    public void setDlbStatus(DlbStatus dlbStatus) {
        this.dlbStatus = dlbStatus;
    }

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public DlbSwtStTransaction getDlbSwtStTransaction() {
        return this.dlbSwtStTransaction;
    }
    
    public void setDlbSwtStTransaction(DlbSwtStTransaction dlbSwtStTransaction) {
        this.dlbSwtStTransaction = dlbSwtStTransaction;
    }

    
    @Column(name="count")
    public Integer getCount() {
        return this.count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }

    
    @Column(name="desc")
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="createDate", length=19)
    public Date getCreateDate() {
        return this.createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }




}

