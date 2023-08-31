package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * DlbSwtMtPaymentMethod generated by hbm2java
 */
@Entity
@Table(name="DLB_SWT_MT_PAYMENT_METHOD"
    
)
public class DlbSwtMtPaymentMethod  implements java.io.Serializable {


     private int code;
     private String description;
     private Set<DlbSwtStTransaction> dlbSwtStTransactions = new HashSet<DlbSwtStTransaction>(0);

    public DlbSwtMtPaymentMethod() {
    }

	
    public DlbSwtMtPaymentMethod(int code) {
        this.code = code;
    }
    public DlbSwtMtPaymentMethod(int code, String description, Set<DlbSwtStTransaction> dlbSwtStTransactions) {
       this.code = code;
       this.description = description;
       this.dlbSwtStTransactions = dlbSwtStTransactions;
    }
   
     @Id 

    
    @Column(name="CODE", unique=true, nullable=false)
    public int getCode() {
        return this.code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }

    
    @Column(name="DESCRIPTION", length=45)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbSwtMtPaymentMethod")
    public Set<DlbSwtStTransaction> getDlbSwtStTransactions() {
        return this.dlbSwtStTransactions;
    }
    
    public void setDlbSwtStTransactions(Set<DlbSwtStTransaction> dlbSwtStTransactions) {
        this.dlbSwtStTransactions = dlbSwtStTransactions;
    }




}

