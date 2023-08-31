package com.epic.dlb.model;
// Generated Jun 15, 2018 2:03:02 PM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DlbSwtMtLogLevel generated by hbm2java
 */
@Entity
@Table(name="DLB_SWT_MT_LOG_LEVEL"
    
)
public class DlbSwtMtLogLevel  implements java.io.Serializable {


     private int id;
     private String description;

    public DlbSwtMtLogLevel() {
    }

	
    public DlbSwtMtLogLevel(int id) {
        this.id = id;
    }
    public DlbSwtMtLogLevel(int id, String description) {
       this.id = id;
       this.description = description;
    }
   
     @Id 

    
    @Column(name="ID", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    @Column(name="DESCRIPTION", length=45)
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }




}


