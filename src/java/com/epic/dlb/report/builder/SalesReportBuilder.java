package com.epic.dlb.report.builder;


import com.epic.dlb.model.DlbWbTicket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kasun_n
 */
@Component("salesReportBuilder")
public class SalesReportBuilder {

    private static SimpleDateFormat dateFormat;

    public JSONArray buildReportData(List<DlbWbTicket> list) throws ParseException {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");        
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        Iterator<DlbWbTicket> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbWbTicket transaction = iterator.next();

            //put to JSON Object
            JSONObject row = new JSONObject();

            Date dateTime=transaction.getCreatedDate();
            row.put("datetime", dateFormat.format(dateTime));                    
            row.put("serial_no",transaction.getSerialNumber());                    
            row.put("lottery_name",transaction.getProductDescription());                    
            row.put("draw_date",dateFormat2.format(transaction.getDrawDate()));                    
            row.put("draw_no",transaction.getDrawNo());                    
          
            table.add(row);

        }

        return table;
    }

}
