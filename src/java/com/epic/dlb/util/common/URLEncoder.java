/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.util.common;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author admin
 */
public class URLEncoder {
    
    public String getEncodedString(String str) throws UnsupportedEncodingException {
        
        Base64 base64 = new Base64(true);
        String encString = base64.encodeAsString(str.getBytes("UTF8"));
        return encString;
        
    }
    
    public String getDecodedString(String str) throws UnsupportedEncodingException {
        
        Base64 base64 = new Base64(true);
        byte[] tmpBytes = base64.decode(str);
        return new String(tmpBytes, "UTF8");
        
    }

}
