/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbRiskProfile;
import com.epic.dlb.model.DlbWbSalesFileBackup;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.model.DlbWbTicketFile;
import com.epic.dlb.model.DlbWbTicketFileErrorDetails;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.repository.JDBCConnection;
import com.epic.dlb.repository.SalesFileRepository;
import com.epic.dlb.repository.TicketRepository;
import com.epic.dlb.util.common.AuditTraceVarList;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Hex;
import org.hibernate.Hibernate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author kasun_n
 */
@Service("ticketService")
public class TicketService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JDBCConnection jDBCConnection;

    @Autowired
    private RiskProfileService riskProfileService;

    @Autowired
    private ProductProfileService productProfileService;

    @Autowired
    private AuditTraceService auditTraceService;

    @Autowired
    private SalesFileRepository salesFileRepository;

    @Transactional(rollbackFor = Exception.class)
    public int saveTicketFile(DlbWbTicketFile ticketFile, MultipartFile file, HttpSession session) throws Exception {
        Integer save = 0;
        //store file

        String folderName = new SimpleDateFormat("yyyy-MM-dd").format(ticketFile.getDate());
        String destincationPath = Configuration.getConfiguration("STORE_DIR_PATH");

        File destinationFile = new File(destincationPath + folderName);
        byte[] fileContent = fileContent = file.getBytes();

        String storeFilePath = destinationFile + File.separator + file.getOriginalFilename();

        if (!destinationFile.exists()) {
            destinationFile.mkdirs();
        }

        WhereStatement whereStatement1 = new WhereStatement("dlbWbProduct.productCode", ticketFile.getDlbWbProduct().getProductCode(), SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("drawNo", ticketFile.getDrawNo(), SystemVarList.EQUAL);

        DlbWbTicketFile getTicketFile = (DlbWbTicketFile) genericRepository.get(DlbWbTicketFile.class, whereStatement1, whereStatement2);

        if (getTicketFile == null) {
            try (FileOutputStream fos = new FileOutputStream(storeFilePath)) {
                fos.write(fileContent);
            }

            //set file path
            ticketFile.setFilePath(storeFilePath);
            BufferedReader br = new BufferedReader(new FileReader(ticketFile.getFilePath()));

            //get total lines of the text file
            int totalLines = 0;
            while (br.readLine() != null) {
                totalLines++;
            }

            ticketFile.setNoOfTicket(totalLines);
            save = (Integer) genericRepository.save(ticketFile);
        }

        return save;

    }

    @Transactional(rollbackFor = Exception.class)
    public boolean checkFileName(String fileName, DlbWbTicketFile ticket) {
//        File Name: DLB_TICKETS_<Brand Code>_<Draw No>_<Order No>
        boolean validateRes = true;

        String productCode = ticket.getDlbWbProduct().getProductCode();
        String drawNo = ticket.getDrawNo() + "";

        String[] array = fileName.split("_");

        try {
            if (!array[2].trim().equals(productCode)) {
                validateRes = false;
            }

            String fileDrawNo = array[3].trim();
            System.out.println("draw checking:" + fileDrawNo);
            if (!fileDrawNo.equals(drawNo)) {
                validateRes = false;
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

        return validateRes;
    }

//    this medthod is help to validate ticket file --> take 20 line of text file and validate 
    public List validateTicketFile(MultipartFile ticketFile, DlbWbTicketFile dlbWbTicketFile) throws FileNotFoundException, IOException, NoSuchAlgorithmException {

        BufferedReader br = null;

        List<String> errorList = new ArrayList<String>();
        List<String> allList = new ArrayList<String>();
        List<String> snList = new ArrayList<String>();

        File file = new File(ticketFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(ticketFile.getBytes());
        fos.close();

        br = null;

        String sCurrentLine;
        br = new BufferedReader(new FileReader(file));

        //check hash 256
        if (!dlbWbTicketFile.getOriginHash().equals(generateCheckSumForFile(ticketFile))) {
            errorList.add("checksum validation failed!");
        } else {

            int index = 0;

            //check first 20 lines
            try {
                while ((sCurrentLine = br.readLine()) != null) { //loop though txt file lines

                    //get indexes of row
                    int idIndex = sCurrentLine.indexOf(",", 0);
                    int snIndex = sCurrentLine.indexOf(",", idIndex + 1);

                    //read text file coulumn by coulumn
                    String serialNo = sCurrentLine.substring(idIndex + 1, snIndex);
                    String items = sCurrentLine.substring(snIndex + 1);

                    //check serial no
                    if ((serialNo == null && serialNo.isEmpty())) {
                        errorList.add("Serial no empty in line no " + index + 1);
                    }

                    //check items     
                    //empty checking
                    if (items == null && items.isEmpty()) {

                        errorList.add("Items empty in line no " + index + 1);
                    } //check item duplicate
                    else {

//                        if (allList.contains(items)) {
//                            errorList.add("Duplicate lottery combinations. serial no: " + serialNo + ", Lottery combination: " + items + " in line no " + index + 1);
//                        }
                        if (snList.contains(serialNo)) {
                            errorList.add("Duplicate  Serial no: " + serialNo + ", Lottery combination: " + items + " in line no " + index + 1);
                        }

                    }

                    //check draw no
                    if ((serialNo != null && !serialNo.isEmpty())) {
                        String lineDrawNo = serialNo.substring(0, 4);

                        if (dlbWbTicketFile.getDrawNo() == null ? lineDrawNo != null : !dlbWbTicketFile.getDrawNo().equals(lineDrawNo)) {
                            errorList.add("Invalid serial no in line no " + index + 1);
                        }

                    }

                    allList.add(items);
                    snList.add(serialNo);
                    index++;

                }

            } catch (IOException | StringIndexOutOfBoundsException e) {
                errorList.add("Invalid line no " + index + 1);;
                return errorList;
            }

        }

        return errorList;

    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbTicketFile> listWiningFileByStatus(int statusCode) {
        WhereStatement whereStatement1 = new WhereStatement("dlbStatus.statusCode", statusCode, SystemVarList.EQUAL);
        List<DlbWbTicketFile> list = genericRepository.list(DlbWbTicketFile.class, whereStatement1);
        Iterator<DlbWbTicketFile> iterator = list.iterator();

        //init proxy objects
        while (iterator.hasNext()) {
            DlbWbTicketFile ticketFile = iterator.next();
            Hibernate.initialize(ticketFile.getDlbWbProduct());
            Hibernate.initialize(ticketFile.getDlbWbEmployeeByApprovedBy());
            Hibernate.initialize(ticketFile.getDlbWbEmployeeByUploadBy());
            Hibernate.initialize(ticketFile.getDlbStatus());

        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbTicketFile> listPendingList(int statusCode) {
        WhereStatement whereStatement1 = new WhereStatement("dlbStatus.statusCode", statusCode, SystemVarList.NOT_EQUAL);
        List<DlbWbTicketFile> list = genericRepository.list(DlbWbTicketFile.class, "id", SystemVarList.DESC, whereStatement1);
        Iterator<DlbWbTicketFile> iterator = list.iterator();

        //init proxy objects
        while (iterator.hasNext()) {
            DlbWbTicketFile ticketFile = iterator.next();
            Hibernate.initialize(ticketFile.getDlbWbProduct());
            Hibernate.initialize(ticketFile.getDlbWbEmployeeByApprovedBy());
            Hibernate.initialize(ticketFile.getDlbWbEmployeeByUploadBy());
            Hibernate.initialize(ticketFile.getDlbStatus());

        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray jsonPendingList(int statusCode) {
        WhereStatement whereStatement1 = new WhereStatement("dlbStatus.statusCode", statusCode, SystemVarList.NOT_EQUAL);
        List<DlbWbTicketFile> list = genericRepository.list(DlbWbTicketFile.class, "id", SystemVarList.DESC, whereStatement1);
//        Collections.reverse(list);
        Iterator<DlbWbTicketFile> iterator = list.iterator();
        JSONArray jSONArray = new JSONArray();
        //init proxy objects
        while (iterator.hasNext()) {
            JSONObject jSONObject = new JSONObject();
            DlbWbTicketFile ticketFile = iterator.next();
            Hibernate.initialize(ticketFile.getDlbWbProduct());
            Hibernate.initialize(ticketFile.getDlbWbEmployeeByApprovedBy());
            Hibernate.initialize(ticketFile.getDlbWbEmployeeByUploadBy());
            Hibernate.initialize(ticketFile.getDlbStatus());
            jSONObject.put("id", ticketFile.getId());
            jSONObject.put("productDescription", ticketFile.getProductDescription());
            jSONObject.put("date", new SimpleDateFormat("yyyy-MM-dd").format(ticketFile.getDate()));
            jSONObject.put("drawNo", ticketFile.getDrawNo());
            jSONObject.put("noOfTicket", ticketFile.getNoOfTicket());
            jSONObject.put("statusDescription", ticketFile.getDlbStatus().getDescription());
            jSONObject.put("statusCode", ticketFile.getDlbStatus().getStatusCode());
            jSONArray.add(jSONObject);
        }

        return jSONArray;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbTicketFile> listApproved(int statusCode) {
        WhereStatement whereStatement1 = new WhereStatement("dlbStatus.statusCode", statusCode, SystemVarList.NOT_EQUAL);
        List<WhereStatement> arrayList = new ArrayList<WhereStatement>();
        arrayList.add(whereStatement1);

        List<DlbWbTicketFile> list = genericRepository.list(DlbWbTicketFile.class, "id", SystemVarList.DESC);
        Iterator<DlbWbTicketFile> iterator = list.iterator();

        //init proxy objects
        while (iterator.hasNext()) {
            DlbWbTicketFile ticketFile = iterator.next();
            Hibernate.initialize(ticketFile.getDlbWbProduct());
            Hibernate.initialize(ticketFile.getDlbWbEmployeeByApprovedBy());
            Hibernate.initialize(ticketFile.getDlbWbEmployeeByUploadBy());
            Hibernate.initialize(ticketFile.getDlbStatus());

        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbTicketFile get(int id) {
        DlbWbTicketFile ticketFile = (DlbWbTicketFile) genericRepository.get(id, DlbWbTicketFile.class);
        if (ticketFile != null) {
            Hibernate.initialize(ticketFile.getDlbStatus());
            Hibernate.initialize(ticketFile.getDlbWbProduct());
        }
        return ticketFile;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(DlbWbTicketFile ticketFile, int status) throws Exception {

        //update status
        DlbWbTicketFile get = get(ticketFile.getId());
        get.setDlbStatus(new DlbStatus(status, null, null));
        try {
            genericRepository.update(get);

        } catch (Exception e) {
            throw e;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void saveTicketData(HttpSession session, DlbWbTicketFile ticketFile) throws Exception {
        Connection connection = jDBCConnection.getConnection();
        connection.setAutoCommit(false);

        DlbWbTicketFile wbTicketFile = (DlbWbTicketFile) genericRepository.get(ticketFile.getId(), DlbWbTicketFile.class);

        DlbWbEmployee dlbWbEmployee = (DlbWbEmployee) session.getAttribute(SystemVarList.EMPLOYEE);
        wbTicketFile.setDlbWbEmployeeByApprovedBy(dlbWbEmployee);
        wbTicketFile.setDlbStatus(new DlbStatus(SystemVarList.PROCESSING, null, null));
        wbTicketFile.setNoOfTicket(ticketFile.getNoOfTicket());

        Hibernate.initialize(wbTicketFile.getDlbWbProductProfile());

        Integer specialPos = wbTicketFile.getDlbWbProductProfile().getSpecialPos();

        genericRepository.update(wbTicketFile);

//        ticketRepository.updateStatus(ticketFile.getId(), SystemVarList.PROCESSING, ticketFile.getNoOfTicket(), connection);
        Thread thread = new Thread(() -> {
            try {
                ticketRepository.saveTicketData(session, ticketFile, connection, specialPos);
                connection.commit();

            } catch (Exception ex) {
                try {
                    ticketRepository.updateAsFail(wbTicketFile.getId());
                    connection.rollback();
                } catch (Exception ex1) {
                    Logger.getLogger(TicketService.class.getName()).log(Level.SEVERE, null, ex1);
                }

                Logger.getLogger(TicketService.class.getName()).log(Level.SEVERE, null, ex);

            }
        });

        thread.start();

    }

    @Transactional(rollbackFor = Exception.class)
    public int getErrorCountByFile(int fileID) {

        WhereStatement whereStatement = new WhereStatement("dlbWbTicketFile.id", fileID, SystemVarList.EQUAL);
        List<WhereStatement> list = new ArrayList<>();
        list.add(whereStatement);
        return genericRepository.getCount(DlbWbTicketFileErrorDetails.class, list);
    }

    @Transactional(rollbackFor = Exception.class)
    public File genarateSalesFile(int fileID, HttpSession session) throws Exception {
        File genarateSalesFile = null;
        Connection connection = jDBCConnection.getConnection();
        DlbWbTicketFile ticketFile = (DlbWbTicketFile) genericRepository.get(fileID, DlbWbTicketFile.class);

        if (ticketFile.getDlbStatus().getStatusCode() == SystemVarList.APPROVED) {
            //generate new file
            int remainCount = 0;
            DlbWbRiskProfile dlbWbRiskProfile = riskProfileService.get(SystemVarList.RECEIVED_TICKET_COUNT);

            if (dlbWbRiskProfile != null) {
                remainCount = Integer.parseInt(dlbWbRiskProfile.getValue());
            }

            genarateSalesFile = ticketRepository.genarateSalesFile(ticketFile, remainCount, connection);
            //update ticket file status
            ticketFile.setDlbStatus(new DlbStatus(SystemVarList.RETURNED, null, null));
            genericRepository.update(ticketFile);

        } else if (ticketFile.getDlbStatus().getStatusCode() == SystemVarList.RETURNED) {

//            return generated file
            String tmpFilepath = Configuration.getConfiguration("TMP_STORE_DIR_PATH") + File.separator
                    + "DLB_SALES_" + ticketFile.getDlbWbProduct().getProductCode() + "_" + ticketFile.getDrawNo() + ".txt";

            genarateSalesFile = new File(tmpFilepath);

        }

        return genarateSalesFile;
    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbTicketFile getTicketFile(String productCode, String drawNo) {
        WhereStatement whereStatement1 = new WhereStatement("dlbWbProduct.productCode", productCode, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("drawNo", drawNo, SystemVarList.EQUAL);

        return (DlbWbTicketFile) genericRepository.get(DlbWbTicketFile.class, whereStatement1, whereStatement2);

    }

    @Transactional(rollbackFor = Exception.class)
    public DlbSwtStWallet getPurchaserDetailsForTicket(String productCode, String drawNo, String serialNo) {

        DlbSwtStWallet dlbSwtStWallet = null;
        WhereStatement whereStatement1 = new WhereStatement("productCode", productCode, SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("drawNo", drawNo, SystemVarList.EQUAL);
        WhereStatement whereStatement3 = new WhereStatement("serialNo", serialNo, SystemVarList.EQUAL);

        DlbSwtStPurchaseHistory purchaseHistory = (DlbSwtStPurchaseHistory) genericRepository.get(DlbSwtStPurchaseHistory.class, whereStatement1, whereStatement2, whereStatement3);

        if (purchaseHistory != null) {
            Hibernate.initialize(purchaseHistory.getDlbSwtStWallet());
            dlbSwtStWallet = purchaseHistory.getDlbSwtStWallet();
        }

        return dlbSwtStWallet;

    }

    public boolean validateDrawDateAndDay(Date drawDate, String day) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated    

        simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely

        return simpleDateformat.format(drawDate).trim().equals(day.trim());
    }

    public String generateCheckSumForFile(MultipartFile ticketFile) throws NoSuchAlgorithmException, IOException {
        String checkSum = "";

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        byte[] bs = digest.digest(ticketFile.getBytes());

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < bs.length; i++) {
            String hex = Integer.toHexString(0xff & bs[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        checkSum = hexString.toString();

        return checkSum;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbSwtStPurchaseHistory> getDuplicatesObjects(Integer fileID) throws Exception {

        DlbWbTicketFile ticketFile = (DlbWbTicketFile) genericRepository.get(fileID, DlbWbTicketFile.class);
        List<DlbSwtStPurchaseHistory> dlbSwtStPurchaseHistorys = new ArrayList<>();
        JSONArray jSONArray = new JSONArray();
        List<String> serialNo = salesFileRepository.getSerialNoList(ticketFile.getDlbWbProduct().getProductCode(), ticketFile.getDrawNo());
        serialNo.forEach(object -> {
            List<WhereStatement> whereStatements = new ArrayList<>();
            WhereStatement criteria1 = new WhereStatement("serialNo", object, SystemVarList.EQUAL);
            whereStatements.add(criteria1);

            List<DlbSwtStPurchaseHistory> dlbSwtStPurchaseHistory = genericRepository.
                    listWithQuery(DlbSwtStPurchaseHistory.class, whereStatements);
            List<DlbSwtStPurchaseHistory> sortedList = dlbSwtStPurchaseHistory.
                    stream().sorted((ph1, ph2) -> {
                        return ph1.getId().compareTo(ph2.getId());
                    }).collect(Collectors.toList());
            sortedList.remove(sortedList.size() - 1);
            dlbSwtStPurchaseHistorys.addAll(sortedList);

        });
        return dlbSwtStPurchaseHistorys;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getDuplicates(Integer fileID) throws Exception {

        JSONArray jSONArray = new JSONArray();
        List<DlbSwtStPurchaseHistory> sortedList = getDuplicatesObjects(fileID);
        sortedList.forEach(sortedObject -> {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("serial_no", sortedObject.getSerialNo());
            jSONObject.put("txn_no", sortedObject.getTxnId());
            jSONObject.put("id", sortedObject.getId());
            jSONObject.put("wallet_id", sortedObject.getDlbSwtStWallet().getId());
            jSONObject.put("nic", sortedObject.getDlbSwtStWallet().getNic());
            jSONObject.put("name", sortedObject.getDlbSwtStWallet().getFirstName() + " " + sortedObject.getDlbSwtStWallet().getLastName());
            jSONArray.add(jSONObject);
        });
        return jSONArray;
    }

    @Transactional(rollbackFor = Exception.class)
    public void doRemovingProcess(Integer fileID) throws Exception {
        List<DlbSwtStPurchaseHistory> dlbSwtStPurchaseHistorys = getDuplicatesObjects(fileID);
        for (DlbSwtStPurchaseHistory purchase : dlbSwtStPurchaseHistorys) {
            DlbWbSalesFileBackup dlbWbSalesFileBackup = new DlbWbSalesFileBackup();

            dlbWbSalesFileBackup.setId(purchase.getId());
            dlbWbSalesFileBackup.setDlbStatusByStatus(
                    purchase.getDlbStatusByStatus().getStatusCode());
            if (purchase.getDlbStatusByNotifyStatus() != null) {
                dlbWbSalesFileBackup.setDlbStatusByNotifyStatus(
                        purchase.getDlbStatusByNotifyStatus().getStatusCode());
            }
            dlbWbSalesFileBackup.setDlbSwtStWallet(purchase.getDlbSwtStWallet().getId());
            dlbWbSalesFileBackup.setSerialNo(purchase.getSerialNo());
            dlbWbSalesFileBackup.setMid(purchase.getMid());
            dlbWbSalesFileBackup.setTxnId(purchase.getTxnId());
            dlbWbSalesFileBackup.setTid(purchase.getTid());
            dlbWbSalesFileBackup.setDrawNo(purchase.getDrawNo());
            dlbWbSalesFileBackup.setDrawDate(purchase.getDrawDate());
            dlbWbSalesFileBackup.setProductCode(purchase.getProductCode());
            dlbWbSalesFileBackup.setLastUpdated(purchase.getLastUpdated());
            dlbWbSalesFileBackup.setCreatedDate(purchase.getCreatedDate());
            dlbWbSalesFileBackup.setNotifiedMesssage(purchase.getNotifiedMesssage());
            dlbWbSalesFileBackup.setLotteryNumbers(purchase.getLotteryNumbers());
            dlbWbSalesFileBackup.setWinningPrize(purchase.getWinningPrize());
            dlbWbSalesFileBackup.setShaKey(purchase.getShaKey());
            dlbWbSalesFileBackup.setRedeemMode(purchase.getRedeemMode());
            dlbWbSalesFileBackup.setRedeemTime(purchase.getRedeemTime());

            genericRepository.save(dlbWbSalesFileBackup);
            salesFileRepository.deleteItem1(purchase.getId());
        }
    }

}
