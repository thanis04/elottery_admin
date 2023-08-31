/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

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
 *
 * @author nipuna_k
 */
@Entity
@Table(name = "DLB_WB_DISTRICT")
public class DlbWbDistrict {

    private Integer id;
    private String name;
    private Boolean active;
    private DlbWbProvince dlbWbProvince;

    public DlbWbDistrict() {
    }

    public DlbWbDistrict(Integer id, String name, Boolean active, DlbWbProvince dlbWbProvince) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.dlbWbProvince = dlbWbProvince;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "ACTIVE")
    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="PROVINCE")
    public DlbWbProvince getDlbWbProvince() {
        return dlbWbProvince;
    }

    public void setDlbWbProvince(DlbWbProvince dlbWbProvince) {
        this.dlbWbProvince = dlbWbProvince;
    }

}
