package com.epic.dlb.model;
// Generated Dec 4, 2019 5:10:31 PM by Hibernate Tools 4.3.1


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * DlbSwtStWinningLogic generated by hbm2java
 */
@Entity
@Table(name="DLB_SWT_ST_WINNING_LOGIC"
   
)
public class DlbSwtStWinningLogic  implements java.io.Serializable {


     private int logicId;
     private String logicDescription;
     private Set<DlbWbResult> dlbWbResults = new HashSet<DlbWbResult>(0);

    public DlbSwtStWinningLogic() {
    }

	
    public DlbSwtStWinningLogic(int logicId, String logicDescription) {
        this.logicId = logicId;
        this.logicDescription = logicDescription;
    }
    public DlbSwtStWinningLogic(int logicId, String logicDescription, Set<DlbWbResult> dlbWbResults) {
       this.logicId = logicId;
       this.logicDescription = logicDescription;
       this.dlbWbResults = dlbWbResults;
    }
   
     @Id 

    
    @Column(name="LOGIC_ID", unique=true, nullable=false)
    public int getLogicId() {
        return this.logicId;
    }
    
    public void setLogicId(int logicId) {
        this.logicId = logicId;
    }

    
    @Column(name="LOGIC_DESCRIPTION", nullable=false, length=45)
    public String getLogicDescription() {
        return this.logicDescription;
    }
    
    public void setLogicDescription(String logicDescription) {
        this.logicDescription = logicDescription;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbSwtStWinningLogic")
    public Set<DlbWbResult> getDlbWbResults() {
        return this.dlbWbResults;
    }
    
    public void setDlbWbResults(Set<DlbWbResult> dlbWbResults) {
        this.dlbWbResults = dlbWbResults;
    }




}


