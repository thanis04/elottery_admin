/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.dto.OperationRequestDto;
import com.epic.dlb.dto.PurchaseHistoryDto;
import com.epic.dlb.dto.SalesTicket;
import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbOperationRequest;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbSystemUser;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.report.service.CoomonService;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kasun_n
 */
@Service("redeemService")
public class TicketRedeemService {

    @Autowired
    private GenericRepository genericRepository;

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat2;

    @Transactional
    public JSONArray getReportByStatus(int statusCode, String fromdate, String toDate) {

        WhereStatement statusCodeWh = new WhereStatement("dlbStatusByStatus.statusCode", statusCode, SystemVarList.EQUAL);
        WhereStatement searchCriteriaFromDate = new WhereStatement("lastUpdated", fromdate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("lastUpdated", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        List<DlbSwtStPurchaseHistory> historys = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, searchCriteriaFromDate, searchCriteriaToDate, statusCodeWh);

        return buildReeddem(historys);

    }

    @Transactional
    public JSONArray getReportByStatus(int statusCode) {
        WhereStatement statusCodeWh = new WhereStatement("dlbStatusByStatus.statusCode", statusCode, SystemVarList.EQUAL);
        JSONArray array = new JSONArray();

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

        List<DlbSwtStPurchaseHistory> historys = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, statusCodeWh);

        return buildReeddem(historys);

    }

    @Transactional
    private JSONArray buildReeddem(List<DlbSwtStPurchaseHistory> historys) {
        JSONArray array = new JSONArray();
        historys.forEach(obj -> {

            JSONObject row = new JSONObject();

            row.put("id", obj.getId());
            row.put("drawNo", (obj.getDrawNo()));
            row.put("drawDate", dateFormat.format(obj.getDrawDate()));

            DlbWbProduct product = (DlbWbProduct) genericRepository.get(obj.getProductCode(), DlbWbProduct.class);

            row.put("productDescription", product.getDescription());
            row.put("lastUpdated", dateFormat2.format(obj.getLastUpdated()));
            row.put("createdDate", dateFormat2.format(obj.getCreatedDate()));
            row.put("winningPrize", obj.getWinningPrize());
            row.put("username", obj.getDlbSwtStWallet().getUsername());
            row.put("name", obj.getDlbSwtStWallet().getFirstName() + " " + obj.getDlbSwtStWallet().getLastName());
            row.put("mobileNo", obj.getDlbSwtStWallet().getMobileNo());
            row.put("nic", obj.getDlbSwtStWallet().getNic());
            row.put("status_code", obj.getDlbStatusByStatus().getStatusCode());
            row.put("status", obj.getDlbStatusByStatus().getDescription());

            array.add(row);

        });

        return array;
    }

    @Transactional
    public File generateExcel(int statusCode, String fromdate, String toDate) throws Exception {

        List<DlbSwtStPurchaseHistory> historys;

        File file = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("#00.00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhmmss");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

            Date fromDateD = dateFormat2.parse(fromdate);
            Date toDateD = dateFormat2.parse(toDate);

            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "Redemption_Processing_Report_" + dateFormat.format(fromDateD) + "_" + dateFormat.format(toDateD) + sdf2.format(new Date()) + ".xls";

            file = new File(tmpFilepath);

            WritableWorkbook w = Workbook.createWorkbook(file);
            WritableSheet s = w.createSheet("Redemption Processing Report", 0);

            s.addCell(new Label(0, 0, "Lottery ID"));
            s.addCell(new Label(1, 0, "Lottery Name"));
            s.addCell(new Label(2, 0, "Draw No"));
            s.addCell(new Label(3, 0, "Draw Date"));
            s.addCell(new Label(4, 0, "Customer Name"));
            s.addCell(new Label(5, 0, "Customer Username"));
            s.addCell(new Label(6, 0, "Customer NIC"));
            s.addCell(new Label(7, 0, "Winning Prize"));
            s.addCell(new Label(8, 0, "Status"));
            s.addCell(new Label(9, 0, "Last updated on"));

            if (fromdate != null || toDate != null) {
                WhereStatement statusCodeWh = new WhereStatement("dlbStatusByStatus.statusCode", statusCode, SystemVarList.EQUAL);
                WhereStatement searchCriteriaFromDate = new WhereStatement("lastUpdated", fromdate, SystemVarList.GREATER_THAN, SystemVarList.AND);
                WhereStatement searchCriteriaToDate = new WhereStatement("lastUpdated", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
                historys = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, searchCriteriaFromDate, searchCriteriaToDate, statusCodeWh);
            } else {
                WhereStatement statusCodeWh = new WhereStatement("dlbStatusByStatus.statusCode", statusCode, SystemVarList.EQUAL);
                historys = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, statusCodeWh);
            }

            Iterator<DlbSwtStPurchaseHistory> it = historys.iterator();

            int genCount = 1;
            while (it.hasNext()) {

                DlbSwtStPurchaseHistory obj = it.next();

                DlbWbProduct product = (DlbWbProduct) genericRepository.get(obj.getProductCode(), DlbWbProduct.class);

                s.addCell(new Label(0, genCount, "" + obj.getId()));
                s.addCell(new Label(1, genCount, product.getDescription()));
                s.addCell(new Label(2, genCount, obj.getDrawNo()));
                s.addCell(new Label(3, genCount, dateFormat.format(obj.getDrawDate())));
                s.addCell(new Label(4, genCount, obj.getDlbSwtStWallet().getFirstName() + " " + obj.getDlbSwtStWallet().getLastName()));
                s.addCell(new Label(5, genCount, obj.getDlbSwtStWallet().getUsername()));
                s.addCell(new Label(6, genCount, obj.getDlbSwtStWallet().getNic()));
                s.addCell(new Label(7, genCount, obj.getWinningPrize()));
                s.addCell(new Label(8, genCount, obj.getDlbStatusByStatus().getDescription()));
                s.addCell(new Label(9, genCount, dateFormat2.format(obj.getLastUpdated())));
                genCount++;

            }

            w.write();
            w.close();

            return file;

        } catch (Exception e) {
            return null;
        }

    }

    @Transactional
    public File generateExcel(int statusCode) throws Exception {

        List<DlbSwtStPurchaseHistory> historys;

        File file = null;
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("#00.00");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhmmss");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");

            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "Redemption_Processing_Report_" + sdf2.format(new Date()) + ".xls";

            file = new File(tmpFilepath);

            WritableWorkbook w = Workbook.createWorkbook(file);
            WritableSheet s = w.createSheet("Redemption Processing Report", 0);

            s.addCell(new Label(0, 0, "Lottery ID"));
            s.addCell(new Label(1, 0, "Lottery Name"));
            s.addCell(new Label(2, 0, "Draw No"));
            s.addCell(new Label(3, 0, "Draw Date"));
            s.addCell(new Label(4, 0, "Customer Name"));
            s.addCell(new Label(5, 0, "Customer Username"));
            s.addCell(new Label(6, 0, "Customer NIC"));
            s.addCell(new Label(7, 0, "Winning Prize"));
            s.addCell(new Label(8, 0, "Status"));
            s.addCell(new Label(9, 0, "Last updated on"));

            WhereStatement statusCodeWh = new WhereStatement("dlbStatusByStatus.statusCode", statusCode, SystemVarList.EQUAL);
            historys = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, statusCodeWh);

            Iterator<DlbSwtStPurchaseHistory> it = historys.iterator();

            int genCount = 1;
            while (it.hasNext()) {

                DlbSwtStPurchaseHistory obj = it.next();

                DlbWbProduct product = (DlbWbProduct) genericRepository.get(obj.getProductCode(), DlbWbProduct.class);

                s.addCell(new Label(0, genCount, "" + obj.getId()));
                s.addCell(new Label(1, genCount, product.getDescription()));
                s.addCell(new Label(2, genCount, obj.getDrawNo()));
                s.addCell(new Label(3, genCount, dateFormat.format(obj.getDrawDate())));
                s.addCell(new Label(4, genCount, obj.getDlbSwtStWallet().getFirstName() + " " + obj.getDlbSwtStWallet().getLastName()));
                s.addCell(new Label(5, genCount, obj.getDlbSwtStWallet().getUsername()));
                s.addCell(new Label(6, genCount, obj.getDlbSwtStWallet().getNic()));
                s.addCell(new Label(7, genCount, obj.getWinningPrize()));
                s.addCell(new Label(8, genCount, obj.getDlbStatusByStatus().getDescription()));
                s.addCell(new Label(9, genCount, dateFormat2.format(obj.getLastUpdated())));
                genCount++;

            }

            w.write();
            w.close();

        } catch (Exception e) {
            return file;
        }

        return file;

    }

    @Transactional
    public OperationRequestDto saveRequest(OperationRequestDto operationRequestDto, HttpSession httpSession) throws Exception {

        Date date = new Date();
        DlbWbSystemUser user = (DlbWbSystemUser) httpSession.getAttribute(SystemVarList.USER);//get user from session

        DlbWbOperationRequest operationRequest = new DlbWbOperationRequest();
        operationRequest.setCreatedBy(user.getUsername());
        operationRequest.setCreatedDate(date);
        operationRequest.setUpdatedDate(date);
        operationRequest.setUpdatedBy(user.getUsername());
        operationRequest.setType(operationRequestDto.getType());
        operationRequest.setRefNo(operationRequestDto.getRefNo());
        operationRequest.setCurrentStatus(new DlbStatus(operationRequestDto.getCurrentStatusCode()));
        operationRequest.setRequestStatus(new DlbStatus(operationRequestDto.getRequestStatusCode()));
        operationRequest.setComment(operationRequestDto.getComment());
        operationRequest.setRefEvidence(operationRequestDto.getRefEvidence());
        operationRequest.setStatus(new DlbStatus(SystemVarList.SUBMITED));

        Integer savedId = (Integer) genericRepository.save(operationRequest);
        operationRequestDto.setId(savedId);
        return operationRequestDto;

    }

    @Transactional
    public void approveOrRejectRequest(Integer id, Boolean isApproved, HttpSession httpSession) throws Exception {
        Date date = new Date();
        DlbWbSystemUser user = (DlbWbSystemUser) httpSession.getAttribute(SystemVarList.USER);//get user from session
        DlbWbOperationRequest operationRequest = (DlbWbOperationRequest) genericRepository.get(id, DlbWbOperationRequest.class);
        
           operationRequest.setUpdatedDate(date);
            operationRequest.setUpdatedBy(user.getUsername());

        if (isApproved) {
            operationRequest.setStatus(new DlbStatus(SystemVarList.APPROVED));
            
            //do opertaion
            if(operationRequest.getType().equals(SystemVarList.MANUL_REDEEM_REQUEST)) {
                
               DlbSwtStPurchaseHistory purchaseHistory 
                       =  (DlbSwtStPurchaseHistory) genericRepository.get(operationRequest.getRefNo(),DlbSwtStPurchaseHistory.class);
               
               purchaseHistory.setDlbStatusByStatus(operationRequest.getRequestStatus());
               
               genericRepository.update(purchaseHistory);
                
            }


        } else {
            operationRequest.setStatus(new DlbStatus(SystemVarList.FAILED));
          
        }
        
        genericRepository.update(operationRequest);

    }

}
