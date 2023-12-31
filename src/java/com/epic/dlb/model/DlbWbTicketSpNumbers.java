package com.epic.dlb.model;
// Generated Mar 27, 2020 3:49:15 PM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * DlbWbTicketSpNumbers generated by hbm2java
 */
@Entity
@Table(name="DLB_WB_TICKET_SP_NUMBERS"
   
)
public class DlbWbTicketSpNumbers  implements java.io.Serializable {


     private Integer id;
     private DlbWbTicket dlbWbTicket;
     private String spNumbers;

    public DlbWbTicketSpNumbers() {
    }

    public DlbWbTicketSpNumbers(DlbWbTicket dlbWbTicket, String spNumbers) {
       this.dlbWbTicket = dlbWbTicket;
       this.spNumbers = spNumbers;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)

    
    @Column(name="ID", unique=true, nullable=false)
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="TICKET_ID")
    public DlbWbTicket getDlbWbTicket() {
        return this.dlbWbTicket;
    }
    
    public void setDlbWbTicket(DlbWbTicket dlbWbTicket) {
        this.dlbWbTicket = dlbWbTicket;
    }

    
    @Column(name="SP_NUMBERS")
    public String getSpNumbers() {
        return this.spNumbers;
    }
    
    public void setSpNumbers(String spNumbers) {
        this.spNumbers = spNumbers;
    }




}


