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
 * DlbSwtMtCardAssociationType generated by hbm2java
 */
@Entity
@Table(name="DLB_SWT_MT_CARD_ASSOCIATION_TYPE"
    
)
public class DlbSwtMtCardAssociationType  implements java.io.Serializable {


     private int id;
     private String type;
     private String imgurl;
     private Set<DlbSwtStToken> dlbSwtStTokens = new HashSet<DlbSwtStToken>(0);

    public DlbSwtMtCardAssociationType() {
    }

	
    public DlbSwtMtCardAssociationType(int id) {
        this.id = id;
    }
    public DlbSwtMtCardAssociationType(int id, String type, String imgurl, Set<DlbSwtStToken> dlbSwtStTokens) {
       this.id = id;
       this.type = type;
       this.imgurl = imgurl;
       this.dlbSwtStTokens = dlbSwtStTokens;
    }
   
     @Id 

    
    @Column(name="ID", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    
    @Column(name="TYPE", length=50)
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    
    @Column(name="IMGURL")
    public String getImgurl() {
        return this.imgurl;
    }
    
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

@OneToMany(fetch=FetchType.LAZY, mappedBy="dlbSwtMtCardAssociationType")
    public Set<DlbSwtStToken> getDlbSwtStTokens() {
        return this.dlbSwtStTokens;
    }
    
    public void setDlbSwtStTokens(Set<DlbSwtStToken> dlbSwtStTokens) {
        this.dlbSwtStTokens = dlbSwtStTokens;
    }




}


