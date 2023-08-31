/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.support;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author Kasun
 */
public class DateTimeConvertor implements Converter<String, Date>  {

    @Override
    public Date convert(String s) {
      
        
        Date tmpDate=null;
        
        try {
            tmpDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(s);
        } catch (ParseException ex) {
            Logger.getLogger(DateTimeConvertor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tmpDate;
    }

    
    
    
}
