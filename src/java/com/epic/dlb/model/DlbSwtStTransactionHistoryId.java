package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * DlbSwtStTransactionHistoryId generated by hbm2java
 */
@Embeddable
public class DlbSwtStTransactionHistoryId  implements java.io.Serializable {


     private String txnid;
     private int status;

    public DlbSwtStTransactionHistoryId() {
    }

    public DlbSwtStTransactionHistoryId(String txnid, int status) {
       this.txnid = txnid;
       this.status = status;
    }
   


    @Column(name="TXNID", nullable=false, length=32)
    public String getTxnid() {
        return this.txnid;
    }
    
    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }


    @Column(name="STATUS", nullable=false)
    public int getStatus() {
        return this.status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof DlbSwtStTransactionHistoryId) ) return false;
		 DlbSwtStTransactionHistoryId castOther = ( DlbSwtStTransactionHistoryId ) other; 
         
		 return ( (this.getTxnid()==castOther.getTxnid()) || ( this.getTxnid()!=null && castOther.getTxnid()!=null && this.getTxnid().equals(castOther.getTxnid()) ) )
 && (this.getStatus()==castOther.getStatus());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getTxnid() == null ? 0 : this.getTxnid().hashCode() );
         result = 37 * result + this.getStatus();
         return result;
   }   


}


