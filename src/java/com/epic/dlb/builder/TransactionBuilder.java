/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.builder;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.service.TransactionService;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author methsiri_h
 */
@Component("transactionBuilder")
public class TransactionBuilder {

    @Autowired
    private TransactionService transactionService;

    private static SimpleDateFormat dateFormat;
    private static SimpleDateFormat dateFormat2;
    private static SimpleDateFormat dateFormat3;

    //build json object from model object
    public JSONObject buildJSONObject(DlbSwtStTransaction transaction) throws ParseException {
        dateFormat = new SimpleDateFormat("yyyyMMddhmmss");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        //put to JSON Object
        JSONObject object = new JSONObject();
        object.put("id", transaction.getTid());
        object.put("datetime", dateFormat2.format(dateFormat.parse(transaction.getDateTime())));
        object.put("fanme", transaction.getDlbSwtStWallet().getFirstName());
        object.put("lanme", transaction.getDlbSwtStWallet().getLastName());
        object.put("nic", transaction.getDlbSwtStWallet().getNic());
        object.put("mobile", transaction.getDlbSwtStWallet().getMobileNo());
        object.put("description", transaction.getDlbSwtMtTxnType().getDescription());
        object.put("brand", transaction.getDlbSwtStWallet().getBrand());

        return object;

    }

    //create json array  from model object
    public JSONArray buildSearchResult(List<DlbSwtStTransaction> transactionLists) {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
        JSONArray table = new JSONArray();
        //loop lists
        for (int i = 0; i < transactionLists.size(); i++) {
            DlbSwtStTransaction transaction = transactionLists.get(i);

            //put to JSON Object
            JSONObject row = new JSONObject();

            row.put("id", transaction.getTxnid());
            row.put("datetime", dateFormat.format(transaction.getDateTime()));
            row.put("fanme", transaction.getDlbSwtStWallet().getFirstName());
            row.put("lanme", transaction.getDlbSwtStWallet().getLastName());
            row.put("nic", transaction.getDlbSwtStWallet().getNic());
            row.put("mobile", transaction.getDlbSwtStWallet().getMobileNo());
            row.put("description", transaction.getDlbSwtMtTxnType().getDescription());

            table.add(row);

        }

        return table;

    }

    @Transactional(rollbackFor = Exception.class)
    public List generateFile(List<DlbSwtStTransaction> transactions, Date fromdate, Date toDate) throws FileNotFoundException, UnsupportedEncodingException, ParseException, Exception {
        Iterator<DlbSwtStTransaction> iterator = transactions.iterator();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat2 = new SimpleDateFormat("YYMMDD");
        dateFormat3 = new SimpleDateFormat("yyyyMMddhmmss");
//           2018-09-11 11:29:08

        List result = new ArrayList();

        File file = null;
        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "CEFT_TXT_" + dateFormat.format(fromdate) + "_" + dateFormat.format(toDate) + ".txt";

        //create file
        PrintWriter writer = new PrintWriter(tmpFilepath, "UTF-8");
        Double totalAmt = 0.00;
        int noOfTrans = 0;

        while (iterator.hasNext()) {
            DlbSwtStTransaction next = iterator.next();

            String txdID = next.getTxnid() != null ? next.getTxnid().substring(0, 14) : ""; 
            String fromAcount = next.getFromAccount() != null ? next.getFromAccount() : "";
            String toAccHolderName = next.getHolderName() != null ? next.getHolderName() : "";
            String toAcc = next.getToAccount() != null ? next.getToAccount() : "";
            String bankCode = next.getBankcode() != null ? next.getBankcode() : "";
            String branchCode = "";
            String transCode = "";
            String amount = next.getAmount() != null ? next.getAmount() : "";
            Date trDate = dateFormat3.parse(next.getDateTime());
            String date = next.getDateTime() != null ? dateFormat2.format(trDate) : "";
            String remark=next.getTxnid() != null ? next.getTxnid(): "";
            String mobile = next.getDlbSwtStWallet().getMobileNo() != null ? next.getDlbSwtStWallet().getMobileNo() : "";
            String email = "info@epiclanka.net";
            String toRefNo = "";
            String type = "CEFT";
            String swftCode = "RTG";
            String transPropuse = "RTG";

            String txtLine = txdID + "," + fromAcount + "," + toAccHolderName + "," + toAcc + ","
                    + bankCode + "," + branchCode + "," + transCode + "," + amount + "," + date + ","+remark+"," + mobile + "," + email + "," + toRefNo + "," + type + "," + swftCode + "," + transPropuse;

            writer.println(txtLine);

            totalAmt = totalAmt + Double.parseDouble(amount);
            noOfTrans++;

            //update status
            next.setDlbStatus(new DlbStatus(SystemVarList.OTHER_BNK_TXT_FILE_GENERATED, null, null));
            transactionService.update(next);

        }

        writer.close();
        //assign created file to return varible
        file = new File(tmpFilepath);
        result.add(totalAmt);
        result.add(noOfTrans);
        result.add(file);

        return result;
 
    }
}
