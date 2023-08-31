/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.builder;

import com.epic.dlb.dto.DlbWbWiningFileDetails;
import com.epic.dlb.dto.DlbWbWiningFileDto;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.report.model.TicketViewModel;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author nipuna_k
 */
@Component("ticketReportBuilder")
public class TicketReportBuilder {

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

            Date dateTime = transaction.getCreatedDate();
            row.put("datetime", dateFormat.format(dateTime));
            row.put("serial_no", transaction.getSerialNumber());
            row.put("lottery_name", transaction.getProductDescription());
            row.put("draw_date", transaction.getDrawDate() != null ? dateFormat2.format(transaction.getDrawDate()) : "");
            row.put("draw_no", transaction.getDrawNo());

            table.add(row);

        }

        return table;
    }

    public JSONArray buildPurchaseHistoryReportData(List<DlbSwtStPurchaseHistory> list) throws ParseException {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        Iterator<DlbSwtStPurchaseHistory> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbSwtStPurchaseHistory transaction = iterator.next();

            //put to JSON Object
            JSONObject row = new JSONObject();

            Date dateTime = transaction.getCreatedDate();
            row.put("datetime", dateFormat.format(dateTime));
            row.put("serial_no", transaction.getSerialNo());
            row.put("lottery_name", transaction.getProductDescription());
            row.put("draw_date", transaction.getDrawDate() != null ? dateFormat2.format(transaction.getDrawDate()) : "");
            row.put("draw_no", transaction.getDrawNo());

            table.add(row);

        }

        return table;
    }

    //----------Tickets Search Builder-------------
    public JSONArray buildTicketSearchReportData(List<DlbSwtStPurchaseHistory> list) throws ParseException {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        Iterator<DlbSwtStPurchaseHistory> iterator = list.iterator();
        System.out.println(iterator.toString());

        while (iterator.hasNext()) {
            DlbSwtStPurchaseHistory transaction = iterator.next();

            //put to JSON Object
            JSONObject row = new JSONObject();

            Date dateTime = transaction.getCreatedDate();

            row.put("id", "1");
            row.put("lottery", "Ada Kotipathi");
            row.put("draw_no", "1215525252");
            row.put("draw_date", "2018-11-10");
            row.put("no_of_ticket", "6251");
            row.put("sales_ticket", "6251");
            row.put("return_ticket", "5322");
            row.put("upload_ticket", "1255");

            table.add(row);

        }

        return table;
    }

    //----------Winning Tickets Search Builder-------------
    public JSONArray buildWinningTicketReportData(List<DlbWbWiningFileDto> list) throws ParseException {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd"); 

        Iterator<DlbWbWiningFileDto> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbWbWiningFileDto dlbWbWiningFileDto = iterator.next();
            
            //put to JSON Object
            JSONObject row = new JSONObject();

            row.put("no_of_winning", dlbWbWiningFileDto.getRecordCount());
            row.put("draw_date", dateFormat2.format(dlbWbWiningFileDto.getDrawDate()));
            row.put("draw_date_str", dateFormat3.format(dlbWbWiningFileDto.getDrawDate()));
            row.put("total_winnings", dlbWbWiningFileDto.getAmount());
            row.put("epic_amount", dlbWbWiningFileDto.getEpicAmount());
            row.put("user_claimed_amount", dlbWbWiningFileDto.getClaimedAmount());
            row.put("user_claimed_dlb", dlbWbWiningFileDto.getDlbClaimedAmount());
            row.put("file_gen_amount", dlbWbWiningFileDto.getGeneratedAmount());
            row.put("claim_done", dlbWbWiningFileDto.getClaimedDoneAmount());
            row.put("dlb_amount", dlbWbWiningFileDto.getDlbAmount());
            row.put("dlb_claim_amount", dlbWbWiningFileDto.getDlbClaimedAmount());
            
            row.put("total_epic_win", dlbWbWiningFileDto.getEpicAmount()+dlbWbWiningFileDto.getClaimedAmount());
            row.put("total_dlb_win", dlbWbWiningFileDto.getDlbAmount()+dlbWbWiningFileDto.getDlbClaimedAmount());
            

            table.add(row);

        }

        return table;
    }
    

    public File buildPrizeFile(List list, DlbWbWiningFile dlbWbWiningFile) throws FileNotFoundException, UnsupportedEncodingException {

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        File file = null;
//        File Name: DLB_SALES_<Brand Code>_<Draw No>
        String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                + "DLB_PRIZE_" + dlbWbWiningFile.getDlbWbProduct().getProductCode() + "_" + dlbWbWiningFile.getDrawNo() + ".txt";

        //create file
        PrintWriter writer = new PrintWriter(tmpFilepath, "UTF-8");
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            TicketViewModel next =  (TicketViewModel) iterator.next();

            String txtLine = next.getSerialNumber()+ " NIC " + next.getNic();

            writer.println(txtLine);

        }

        writer.close();
        //assign created file to return varible
        file = new File(tmpFilepath);
        return file;
    }

    public JSONArray buildTicketList(List<DlbSwtStPurchaseHistory> purchaseHistorys) {
        Iterator<DlbSwtStPurchaseHistory> iterator = purchaseHistorys.iterator();
        JSONArray array = new JSONArray();

        while (iterator.hasNext()) {
            DlbSwtStPurchaseHistory next = iterator.next();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("serial_no", next.getSerialNo());
            jSONObject.put("numbers", next.getLotteryNumbers());
            jSONObject.put("prize", (next.getWinningPrize()==null || next.getWinningPrize()=="0" )?"-":next.getWinningPrize());
            
            array.add(jSONObject);

        }
        return array;

    }
    
        //----------Winning Tickets Search Builder-------------
    public JSONArray buildWinningTicketReportDataDetails(List<DlbWbWiningFileDetails> list) throws ParseException {
        JSONArray table = new JSONArray();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyyMMdd"); 

        Iterator<DlbWbWiningFileDetails> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbWbWiningFileDetails dlbWbWiningFileDto = iterator.next();
            
            //put to JSON Object
            JSONObject row = new JSONObject();

            row.put("id", dlbWbWiningFileDto.getId());
            row.put("productDescription", dlbWbWiningFileDto.getProductDescription());
            row.put("drawNo", dlbWbWiningFileDto.getDrawNo());
            row.put("date", dateFormat2.format(dlbWbWiningFileDto.getDate()));
            row.put("no_of_winning", dlbWbWiningFileDto.getRecordCount());
            row.put("draw_date", dateFormat2.format(dlbWbWiningFileDto.getDate()));
            row.put("draw_date_str", dateFormat3.format(dlbWbWiningFileDto.getDate()));
            row.put("total_winnings", dlbWbWiningFileDto.getAmount());
            row.put("epic_amount", dlbWbWiningFileDto.getEpicAmount());
            row.put("user_claimed_amount", dlbWbWiningFileDto.getClaimedAmount());
            row.put("user_claimed_dlb", dlbWbWiningFileDto.getDlbClaimedAmount());
            row.put("file_gen_amount", dlbWbWiningFileDto.getGeneratedAmount());
            row.put("claim_done", dlbWbWiningFileDto.getClaimedDoneAmount());
            row.put("dlb_amount", dlbWbWiningFileDto.getDlbAmount());
            row.put("dlb_claim_amount", dlbWbWiningFileDto.getDlbClaimedAmount());
            
            row.put("total_epic_win", dlbWbWiningFileDto.getEpicAmount()+dlbWbWiningFileDto.getClaimedAmount());
            row.put("total_dlb_win", dlbWbWiningFileDto.getDlbAmount()+dlbWbWiningFileDto.getDlbClaimedAmount());
            

            table.add(row);

        }

        return table;
    }

}
