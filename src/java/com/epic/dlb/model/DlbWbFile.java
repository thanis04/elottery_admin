/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author nipuna_k
 */
@Entity
@Table(name="DLB_WB_FILE")
public class DlbWbFile {
    
    private Integer id;
    private Blob file;
    private String type;

    public DlbWbFile() {
    }

    public DlbWbFile(Integer id, Blob file, String type) {
        this.id = id;
        this.file = file;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "FILE")
    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    @Column(name = "FILE_TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
