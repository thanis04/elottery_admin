/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

/**
 *
 * @author kasun_n
 */
@Service("encryptionService")
public class EncryptionService {

    public String genarateHashCode(String storeFilePath) throws NoSuchAlgorithmException, FileNotFoundException, IOException {

        byte[] buffer = new byte[8192];
        int count;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(storeFilePath));
        while ((count = bis.read(buffer)) > 0) {
            digest.update(buffer, 0, count);
        }
        bis.close();

        byte[] hash = digest.digest();
        String encode = new BASE64Encoder().encode(hash);

        return encode;
    }

    public String genarateHashCode(BufferedInputStream bis) throws NoSuchAlgorithmException, FileNotFoundException, IOException {

        byte[] buffer = new byte[8192];
        int count;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        while ((count = bis.read(buffer)) > 0) {
            digest.update(buffer, 0, count);
        }
        bis.close();

        byte[] hash = digest.digest();
        String encode = new BASE64Encoder().encode(hash);

        return encode;
    }

    public String genarateShaVal(String plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String shaval = "";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        
        //get byte
        digest.reset();
        digest.update(plainText.getBytes("UTF-8"));
        Formatter formatter = new Formatter();
        
        //convert byte to sha
        for (byte b : digest.digest()) {
            formatter.format("%02x", b);
        }
        shaval = formatter.toString();
        formatter.close();
        return shaval;
    }

}
