/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbMainDrawPrizeCode;
import com.epic.dlb.model.DlbWbProductProfile;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.model.DlbWbWiningFileApprovalHistory;
import com.epic.dlb.model.DlbWbWiningFileErrorDetails;
import com.epic.dlb.model.DlbWbWiningFileHistory;
import com.epic.dlb.model.DlbWbWorkflow;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.repository.JDBCConnection;
import com.epic.dlb.repository.ResultRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author kasun_n
 */
@Service("winingFileService")
public class WiningFileService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private EncryptionService encryptionService;

    @Autowired
    private JDBCConnection jDBCConnection;

    @Autowired
    private MainDrawPrizeService drawPrizeService;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public Integer save(DlbWbWiningFile winingFile, MultipartFile file, HttpSession session) throws Exception {

        Serializable save = 0;

        //store file       
        String folderName = new SimpleDateFormat("yyyy-MM-dd").format(winingFile.getDate());
        String destincationPath = Configuration.getConfiguration("STORE_DIR_PATH");

        File destinationFile = new File(destincationPath + folderName);
        byte[] fileContent = fileContent = file.getBytes();

        String storeFilePath = destinationFile + File.separator + file.getOriginalFilename();

        if (!destinationFile.exists()) {
            destinationFile.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(storeFilePath)) {
            fos.write(fileContent);
        }

        String genarateHashCode = encryptionService.genarateHashCode(storeFilePath);
        winingFile.setFilePath(storeFilePath);
        winingFile.setHash(genarateHashCode);

        BufferedReader br = new BufferedReader(new FileReader(winingFile.getFilePath()));

        //get total lines of the text file
        int totalLines = 0;
        while (br.readLine() != null) {
            totalLines++;
        }

        WhereStatement whereStatement1 = new WhereStatement("dlbWbProduct.productCode", winingFile.getDlbWbProduct().getProductCode(), SystemVarList.EQUAL);
        WhereStatement whereStatement2 = new WhereStatement("drawNo", winingFile.getDrawNo(), SystemVarList.EQUAL);

        DlbWbWiningFile getWbWiningFile = (DlbWbWiningFile) genericRepository.get(DlbWbWiningFile.class, whereStatement1, whereStatement2);

        if (getWbWiningFile == null) {

            //save to DB
            save = genericRepository.save(winingFile);

            //save history
            DlbWbWiningFileHistory winingFileHistory = new DlbWbWiningFileHistory();

            winingFileHistory.setDlbStatus(winingFile.getDlbStatus());
            winingFileHistory.setDlbWbEmployeeByUploadBy((DlbWbEmployee) session.getAttribute(SystemVarList.EMPLOYEE));
            winingFileHistory.setDlbWbProduct(winingFile.getDlbWbProduct());
            winingFileHistory.setDrawNo(winingFile.getDrawNo());
            winingFileHistory.setDate(winingFile.getDate());
            winingFileHistory.setHash(winingFile.getHash());
            winingFileHistory.setMacAddress(winingFile.getMacAddress());
            winingFileHistory.setProductDescription(winingFile.getProductDescription());
            winingFileHistory.setDlbWbWorkflow(winingFile.getDlbWbWorkflow());
            winingFileHistory.setFilePath(winingFile.getFilePath());
            winingFileHistory.setReason("");
            winingFileHistory.setLastUpdatedUser(winingFile.getLastUpdatedUser());
            winingFileHistory.setLastUpdatedTime(winingFile.getLastUpdatedTime());
            winingFileHistory.setCreatedTime(winingFile.getCreatedTime());

            genericRepository.save(winingFileHistory);

        }

        return (Integer) save;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveResultData(HttpSession session, DlbWbWiningFile winingFile) throws Exception {
        Connection connection = jDBCConnection.getConnection();
        connection.setAutoCommit(false);

        DlbWbWiningFile getWiningFile = (DlbWbWiningFile) genericRepository.get(winingFile.getId(), DlbWbWiningFile.class);
        getWiningFile.setDlbStatus(new DlbStatus(SystemVarList.PROCESSING, null, null));
        genericRepository.update(getWiningFile);

        //put result saving data to Thread
        Thread thread = new Thread(() -> {

            try {
                //read text file and wrtite to table              
                resultRepository.saveResultDataNew(session, winingFile.getFilePath(), winingFile, connection);
                connection.commit();
            } catch (Exception ex) {
                try {

                    getWiningFile.setDlbStatus(new DlbStatus(SystemVarList.SUBMITED, null, null));
//                    genericRepository.update(getWiningFile);
                    connection.rollback();
                } catch (Exception ex1) {
                    Logger.getLogger(TicketService.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        });

        //start process
        thread.start();

        Thread.State state = thread.getState();
//
//        if (state == state.RUNNABLE) {
//            updateStatus(winingFile, SystemVarList.PROCESSING, session);
//        }

    }

    @Transactional(rollbackFor = Exception.class)
    public DlbWbWiningFile get(int id) {
        DlbWbWiningFile winingFile = (DlbWbWiningFile) genericRepository.get(id, DlbWbWiningFile.class);
        if (winingFile != null) {
            Hibernate.initialize(winingFile.getDlbStatus());
        }
        return winingFile;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbWbWiningFile> listWiningFileByStage(String workflowCode, int statusCode1, int statusCode2) {
        WhereStatement whereStatement1 = new WhereStatement("dlbWbWorkflow.processCode", workflowCode, SystemVarList.EQUAL, " AND ");
        WhereStatement whereStatement2 = new WhereStatement("dlbStatus.statusCode", statusCode1, SystemVarList.EQUAL, " OR ");
        WhereStatement whereStatement3 = new WhereStatement("dlbStatus.statusCode", statusCode2, SystemVarList.EQUAL);

        List<WhereStatement> whereStatements = new ArrayList<>();
        whereStatements.add(whereStatement1);
        whereStatements.add(whereStatement2);
        whereStatements.add(whereStatement3);

        List<DlbWbWiningFile> list = genericRepository.listWithQuery(DlbWbWiningFile.class, whereStatements, "id", SystemVarList.DESC);
        Iterator<DlbWbWiningFile> iterator = list.iterator();

        //init proxy objects
        while (iterator.hasNext()) {
            DlbWbWiningFile winingFile = iterator.next();
            Hibernate.initialize(winingFile.getDlbWbProduct());
            Hibernate.initialize(winingFile.getDlbWbEmployeeByApprovedBy());
            Hibernate.initialize(winingFile.getDlbWbEmployeeByUploadBy());
            Hibernate.initialize(winingFile.getDlbStatus());

        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(DlbWbWiningFile winingFile, int status, HttpSession session) throws Exception {

        //update status
        winingFile.setDlbStatus(new DlbStatus(status, null, null));
        try {
            genericRepository.update(winingFile);

            DlbWbWiningFileHistory winingFileHistory = new DlbWbWiningFileHistory();

            winingFileHistory.setDlbStatus(winingFile.getDlbStatus());
            winingFileHistory.setDlbWbEmployeeByUploadBy((DlbWbEmployee) session.getAttribute(SystemVarList.EMPLOYEE));
            winingFileHistory.setDlbWbProduct(winingFile.getDlbWbProduct());
            winingFileHistory.setDrawNo(winingFile.getDrawNo());
            winingFileHistory.setDate(winingFile.getDate());
            winingFileHistory.setHash(winingFile.getHash());
            winingFileHistory.setMacAddress(winingFile.getMacAddress());
            winingFileHistory.setProductDescription(winingFile.getProductDescription());
            winingFileHistory.setDlbWbWorkflow(winingFile.getDlbWbWorkflow());
            winingFileHistory.setFilePath(winingFile.getFilePath());
            winingFileHistory.setReason("");
            winingFileHistory.setLastUpdatedUser(winingFile.getLastUpdatedUser());
            winingFileHistory.setLastUpdatedTime(winingFile.getLastUpdatedTime());
            winingFileHistory.setCreatedTime(winingFile.getCreatedTime());

            if (status == SystemVarList.APPROVED) {
                winingFileHistory.setDlbWbEmployeeByApprovedBy((DlbWbEmployee) session.getAttribute(SystemVarList.EMPLOYEE));
            }

//            genericRepository.save(winingFileHistory);
            //save approval history
            DlbWbWiningFileApprovalHistory approvalHistory = new DlbWbWiningFileApprovalHistory();
            approvalHistory.setDlbStatus(winingFile.getDlbStatus());
            approvalHistory.setDlbWbEmployee(winingFile.getDlbWbEmployeeByApprovedBy());
            approvalHistory.setDlbWbWiningFile(winingFile);
            approvalHistory.setDlbWbWorkflowByInitialStage(new DlbWbWorkflow(SystemVarList.WINING_FILE_APPROVAL, null));
            if (status == SystemVarList.APPROVED) {
                approvalHistory.setDlbWbWorkflowByInitialStage(new DlbWbWorkflow(SystemVarList.COMPLATEDTE_STAGE, null));
            } else {
                approvalHistory.setDlbWbWorkflowByInitialStage(new DlbWbWorkflow(SystemVarList.WINING_FILE_APPROVAL, null));
            }
            approvalHistory.setLastUpdatedUser(winingFile.getLastUpdatedUser());
            approvalHistory.setLastUpdatedTime(winingFile.getLastUpdatedTime());
            approvalHistory.setCreatedTime(winingFile.getCreatedTime());

//            genericRepository.save(approvalHistory);
        } catch (Exception e) {
            throw e;
        }

    }

    public boolean checkFileName(String fileName, DlbWbWiningFile winingFile) {
//        File Name: DLB_WINNER_<Brand Code>_<Draw No>
        boolean validateRes = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        String productCode = winingFile.getDlbWbProduct().getProductCode();
        String drawNo = winingFile.getDrawNo() + "";

        String[] array = fileName.split("_");

        if (!array[2].trim().equals(productCode)) {
            validateRes = false;
        }

        if (!array[3].trim().equals(drawNo)) {
            validateRes = false;
        }

        return validateRes;
    }

    @Transactional(rollbackFor = Exception.class)
    public int getErrorCountByFile(int fileID) {

        WhereStatement whereStatement = new WhereStatement("dlbWbWiningFile.id", fileID, SystemVarList.EQUAL);
        List<WhereStatement> list = new ArrayList<>();
        list.add(whereStatement);
        return genericRepository.getCount(DlbWbWiningFileErrorDetails.class, list);
    }

    //    this medthod is help to validate winner file --> take 20 line of text file and validate 
    public boolean validateTicketFile(MultipartFile ticketFile, String drawNo) throws FileNotFoundException, IOException {
        boolean validateRes = true;
        BufferedReader br = null;

        File file = new File(ticketFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(ticketFile.getBytes());
        fos.close();

        br = null;

        String sCurrentLine;
        br = new BufferedReader(new FileReader(file));

        int index = 0;
        int sampleCheckError = 0;

        //check first 20 lines
        try {
            while ((sCurrentLine = br.readLine()) != null && index < 20) { //loop though txt file lines

                String[] arr = sCurrentLine.split(" ");

                String drawDateStr = arr[0];
                String drawNoNbookNo = arr[1];
                String lineDrawNo = drawNoNbookNo.substring(0, 4);
                String bookNo = drawNoNbookNo.substring(4, drawNoNbookNo.length());

                String tickertNo = arr[2];
                String prizeCode = arr[3];
                String[] arrDisAgent = arr[4].split("/");

                int checkDigit = resultRepository.calculateTotal(drawNoNbookNo);
                String serialNo = lineDrawNo + bookNo + checkDigit + tickertNo;

                if ((serialNo == null && serialNo.isEmpty())) {
                    sampleCheckError++;
                }
                if ((drawDateStr == null && drawDateStr.isEmpty())) {
                    sampleCheckError++;
                }
                if (prizeCode == null && prizeCode.isEmpty()) {
                    sampleCheckError++;
                }

                if (drawNo == null ? lineDrawNo != null : !drawNo.equals(lineDrawNo)) {
                    sampleCheckError++;
                }

                index++;

            }

        } catch (IOException | StringIndexOutOfBoundsException e) {
            validateRes = false;
            return validateRes;
        }

        validateRes = sampleCheckError == 0;

        return validateRes;

    }

    @Transactional
    public List<Object> getTotalWinningByFile(DlbWbWiningFile winingFile) throws FileNotFoundException, IOException {

        BufferedReader br = null;
        List<Object> result =new ArrayList<Object>();
        PreparedStatement prepareStatement1 = null;
        Double amount = 0.0;
        String jackpot = "";

        DlbWbTicket wbTicket = winingFile.getDlbWbTicket();

//        DlbWbProductProfile productProfile =  wbTicket.getDlbWbProductProfile();
        String fileName = winingFile.getFilePath();

        String sCurrentLine;
        br = new BufferedReader(new FileReader(fileName));

        while ((sCurrentLine = br.readLine()) != null) {//loop though txt file lines

            String[] arr = sCurrentLine.split(" ");
            String prizeCode = arr[3];

            if (!prizeCode.equals(SystemVarList.JACKPOT_PRIZE_CODE)) {
                DlbWbMainDrawPrizeCode mainPrizeCode = drawPrizeService
                        .getMainPrizeCode(winingFile.getDlbWbProduct().getProductCode(), prizeCode);

                amount = amount + Double.parseDouble(mainPrizeCode.getDrawPrize());
            }
            else {
                 jackpot = "Jackpot";
            }

        }
        
        result.add(amount);
        result.add(jackpot);

        return result;

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

}