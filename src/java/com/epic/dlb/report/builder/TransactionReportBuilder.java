package com.epic.dlb.report.builder;


import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.report.model.TransactionSummery;
import java.text.DecimalFormat;
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
@Component("transactionReportBuilder")
public class TransactionReportBuilder {

    private static SimpleDateFormat dateFormat;

    public JSONArray buildReportData(List<DlbSwtStTransaction> list) throws ParseException {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");
        
        DecimalFormat df=new DecimalFormat("0.##");

        Iterator<DlbSwtStTransaction> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbSwtStTransaction transaction = iterator.next();

            //put to JSON Object
            JSONObject row = new JSONObject();

            row.put("id", transaction.getTxnid());
            Date dateTime=dateFormat2.parse(transaction.getDateTime());
            row.put("datetime", dateFormat.format(dateTime));                    
            String fName = "";
            String lName = "";
            String mobile_no = "";
            String nic = "";
            if(transaction.getDlbSwtStWallet() != null)
            {
                if(transaction.getDlbSwtStWallet().getFirstName() != null)
                {
                    fName = transaction.getDlbSwtStWallet().getFirstName();
                }
                if(transaction.getDlbSwtStWallet().getLastName() != null)
                {
                    lName = transaction.getDlbSwtStWallet().getLastName();
                }
                if(transaction.getDlbSwtStWallet().getMobileNo() != null)
                {
                    mobile_no = transaction.getDlbSwtStWallet().getMobileNo();
                }
                if(transaction.getDlbSwtStWallet().getNic() != null)
                {
                    nic = transaction.getDlbSwtStWallet().getNic();
                }
            }

            row.put("name",fName+" "+ lName);   
            row.put("mobile_no",mobile_no);                    
            row.put("nic",nic);                    
            row.put("pay_type",transaction.getDlbSwtMtPaymentMethod().getDescription());                    
            row.put("bank", transaction.getBank());
            row.put("amount", transaction.getAmount()!=null?df.format(Double.parseDouble(transaction.getAmount())):"");

            table.add(row);

        }

        return table;
    }
    public JSONArray buildBankTrnferReportData(List<DlbSwtStTransaction> list) throws ParseException {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMddhmmss");
        
        DecimalFormat df=new DecimalFormat("0.##");

        Iterator<DlbSwtStTransaction> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbSwtStTransaction transaction = iterator.next();

            //put to JSON Object
            JSONObject row = new JSONObject();

            row.put("id", transaction.getTxnid());
            Date dateTime=dateFormat2.parse(transaction.getDateTime());
            row.put("datetime", dateFormat.format(dateTime));
            String fName = "";
            String lName = "";
            if(transaction.getDlbSwtStWallet().getFirstName() != null)
            {
                fName = transaction.getDlbSwtStWallet().getFirstName();
            }
            if(transaction.getDlbSwtStWallet().getLastName() != null)
            {
                lName = transaction.getDlbSwtStWallet().getLastName();
            }
            
            row.put("name",fName+" "+ lName);                     
            row.put("mobile_no",transaction.getDlbSwtStWallet().getMobileNo());                    
            row.put("nic",transaction.getDlbSwtStWallet().getNic());   
            row.put("from_acc", transaction.getFromAccount());
            row.put("to_acc", transaction.getToAccount());
            row.put("holder_name", transaction.getHolderName()!=null?transaction.getHolderName():"-");
            row.put("bank", transaction.getBank());
            row.put("holder_name", transaction.getHolderName());
            row.put("amount", transaction.getAmount()!=null?df.format(Double.parseDouble(transaction.getAmount())):"");

            table.add(row);

        }

        return table;
    }
    public JSONArray buildReportSummaryData(List<TransactionSummery> list) throws ParseException {
        JSONArray table = new JSONArray();
     
        DecimalFormat df=new DecimalFormat("0.##");

        Iterator<TransactionSummery> iterator = list.iterator();

        while (iterator.hasNext()) {
            TransactionSummery transaction = iterator.next();

            //put to JSON Object
            JSONObject row = new JSONObject();

            row.put("description", transaction.getDescription());
            row.put("amount", df.format(transaction.getAmount()));                    
           

            table.add(row);

        }

        return table;
    }

}
