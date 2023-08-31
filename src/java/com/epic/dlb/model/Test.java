/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.model;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author nipuna_k
 */
public class Test {
    private String description;
    private MultipartFile image;

    public Test(String description, MultipartFile image) {
        this.description = description;
        this.image = image;
    }

    public Test() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
    
    
}
