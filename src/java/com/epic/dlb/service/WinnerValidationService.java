/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.repository.CustomRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.Configuration;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import com.epic.dlb.viewmodel.WinnerValidateSubViewVM;
import com.epic.dlb.viewmodel.WinnerValidateVM;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.xml.bind.DatatypeConverter;
import org.hibernate.Hibernate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.krysalis.barcode4j.impl.datamatrix.DataMatrix;
import org.krysalis.barcode4j.impl.upcean.EAN13Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yasitha_b
 */
@Service
public class WinnerValidationService {

    @Autowired
    CustomRepository customRepository;

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getWinnersData(String nic, String mobile) throws Exception {
        Calendar date = Calendar.getInstance();
        date.setTime(new Date());
        //Date nowdate = date.getTime();
        Integer numberOfMonthsToReduce = -6;
        date.add(Calendar.MONTH, numberOfMonthsToReduce);
        Date fromDate = date.getTime();

        String strFromDate = new SimpleDateFormat("yyyy/MM/dd").format(fromDate);
        //date.setMonth((date.getMonth() - 1 + 6) % 12 + 1);
        //System.out.println("-------------------------------------------------------------------------strFromDate" + strFromDate);
        String strQueryNonExpired = "SELECT wall.NIC,wall.MOBILE_NO,wall.FIRST_NAME,wall.LAST_NAME,ph.SERIAL_NO,"
                + "ph.WINNING_PRIZE,status.DESCRIPTION,wall.ID,ph.ID as phid,DLB_WB_PRODUCT.DESCRIPTION As lotteryName, wall.Profile AS profile, ph.STATUS "
                + "FROM DLB_SWT_ST_WALLET wall "
                + "INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ph ON ph.WALLET_ID = wall.ID "
                + "INNER JOIN DLB_WB_TICKET tic ON ph.PRODUCT_CODE = tic.PRODUCT_CODE AND ph.SERIAL_NO = tic.SERIAL_NUMBER "
                + "INNER JOIN DLB_WB_PRODUCT ON ph.PRODUCT_CODE = DLB_WB_PRODUCT.PRODUCT_CODE  "
                + "INNER JOIN DLB_STATUS status ON ph.STATUS = status.STATUS_CODE  "
                + "AND wall.NIC = \"" + nic + "\" "
                + "AND wall.MOBILE_NO = \"" + mobile + "\" "
                + "AND tic.DRAW_DATE >= \"" + strFromDate + "\" "
                + "AND (ph.STATUS = \"21\" or ph.STATUS = \"4\" or ph.STATUS = \"5\" or ph.STATUS = \"3\"  or ph.STATUS = \"44\")"
                + ";";
        List<Object[]> resultForNonExpired = customRepository.queryExecuter(strQueryNonExpired);
        //System.out.println("-------------------------------------------------------------------------resultForNonExpired" + resultForNonExpired.size());

        List<WinnerValidateVM> winnerValidateVMListNonExpired = new ArrayList<WinnerValidateVM>();

        for (Object[] row : resultForNonExpired) {
            WinnerValidateVM vm = new WinnerValidateVM();

            vm.setNic(row[0].toString());
            vm.setMobile(row[1].toString());
            vm.setName(row[2].toString() + row[3].toString());
            vm.setSerialNo(row[4].toString());
            vm.setWinningAmount(row[5].toString());
            vm.setStatus(row[6].toString());
            vm.setStatusFlag("1");
            vm.setWalletId(row[7].toString());
            vm.setPurchesHistoryId(row[8].toString());
            vm.setLotteryName(row[9].toString());
            vm.setProfile(row[10].toString());
            vm.setStatusCode((Integer) row[11]);

            winnerValidateVMListNonExpired.add(vm);
        }

        String strQueryExpired = "SELECT wall.NIC,wall.MOBILE_NO,wall.FIRST_NAME,wall.LAST_NAME,ph.SERIAL_NO,"
                + "ph.WINNING_PRIZE,status.DESCRIPTION,wall.ID,ph.ID as phid,DLB_WB_PRODUCT.DESCRIPTION As lotteryName, ph.STATUS  "
                + "FROM DLB_SWT_ST_WALLET wall "
                + "INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ph ON ph.WALLET_ID = wall.ID "
                + "INNER JOIN DLB_WB_TICKET tic ON ph.PRODUCT_CODE = tic.PRODUCT_CODE AND ph.SERIAL_NO = tic.SERIAL_NUMBER "
                + "INNER JOIN DLB_WB_PRODUCT ON ph.PRODUCT_CODE = DLB_WB_PRODUCT.PRODUCT_CODE  "
                + "INNER JOIN DLB_STATUS status ON ph.STATUS = status.STATUS_CODE  "
                + "AND wall.NIC = \"" + nic + "\" "
                + "AND wall.MOBILE_NO = \"" + mobile + "\" "
                + "AND tic.DRAW_DATE < \"" + strFromDate + "\" "
                + "AND (ph.STATUS = \"43\" or ph.STATUS = \"4\" or ph.STATUS = \"5\" or ph.STATUS = \"3\"  or ph.STATUS = \"44\")"
                + ";";

        List<Object[]> resultExpired = customRepository.queryExecuter(strQueryExpired);
        //System.out.println("-------------------------------------------------------------------------strQueryExpired" + strQueryExpired);
        //System.out.println("-------------------------------------------------------------------------resultExpired" + resultExpired.size());

        List<WinnerValidateVM> winnerValidateVMListExpired = new ArrayList<WinnerValidateVM>();

        for (Object[] row : resultExpired) {
            WinnerValidateVM vm = new WinnerValidateVM();

            vm.setNic(row[0].toString());
            vm.setMobile(row[1].toString());
            vm.setName(row[2].toString() + row[3].toString());
            vm.setSerialNo(row[4].toString());
            vm.setWinningAmount(row[5].toString());
            vm.setStatus("Expired");
            vm.setStatusFlag("0");
            vm.setWalletId(row[7].toString());
            vm.setPurchesHistoryId(row[8].toString());
            vm.setLotteryName(row[9].toString());
            vm.setStatusCode((Integer) row[10]);

            winnerValidateVMListExpired.add(vm);
        }

        JSONArray searchResult = this.buildSearchList(winnerValidateVMListNonExpired, winnerValidateVMListExpired);
        return searchResult;
    }

    public JSONArray buildSearchList(List nonExpList, List expList) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < nonExpList.size(); i++) {
            WinnerValidateVM a = (WinnerValidateVM) nonExpList.get(i);

            JSONObject jSONObject = new JSONObject();
            jSONObject.put("nic", a.getNic());
            jSONObject.put("mobile", a.getMobile());
            jSONObject.put("name", a.getName());
            jSONObject.put("serialno", a.getSerialNo());
            if (a.getStatusCode().equals(SystemVarList.PRIZE_LARGE_THAN_OR_EQ_100000)) {
                jSONObject.put("status", "Winner Claim Pending");
            } else {
                jSONObject.put("status", a.getStatus());
            }

            jSONObject.put("winningamount", a.getWinningAmount());
            jSONObject.put("statusFlag", a.getStatusFlag());
            jSONObject.put("walletId", a.getWalletId());
            jSONObject.put("lotteryname", a.getLotteryName());
            jSONObject.put("phid", a.getPurchesHistoryId());

            array.add(jSONObject);
        }
        for (int i = 0; i < expList.size(); i++) {
            WinnerValidateVM a = (WinnerValidateVM) expList.get(i);

            JSONObject jSONObject = new JSONObject();
            jSONObject.put("nic", a.getNic());
            jSONObject.put("mobile", a.getMobile());
            jSONObject.put("name", a.getName());
            jSONObject.put("serialno", a.getSerialNo());
            if (a.getStatus().equals(SystemVarList.PRIZE_LARGE_THAN_OR_EQ_100000)) {
                jSONObject.put("status", "Claim Pending");
            } else {
                jSONObject.put("status", a.getStatus());
            }
            jSONObject.put("winningamount", a.getWinningAmount());
            jSONObject.put("statusFlag", a.getStatusFlag());
            jSONObject.put("walletId", a.getWalletId());
            jSONObject.put("phid", a.getPurchesHistoryId());

            array.add(jSONObject);
        }
        return array;
    }

    public String createBase64Image(byte[] image) throws IOException {
        //String base64String = null;
        //base64String = DatatypeConverter.printBase64Binary(image);
        String s = new String(image);
        //System.out.println("--------------------------------------Text Decryted : " + s);
        //String encoded = Base64.getEncoder().encodeToString(image);
        return s;
    }

    @Transactional(rollbackFor = Exception.class)
    public WinnerValidateSubViewVM getWalletIdRecord(String walletId, String phid) throws Exception {
        WhereStatement searchFromId = new WhereStatement("id", walletId, SystemVarList.EQUAL);
        List listWithQuery = genericRepository.listWithQuery(DlbSwtStWallet.class, searchFromId);

        Iterator<DlbSwtStWallet> iterator = listWithQuery.iterator();

        WinnerValidateSubViewVM vm = new WinnerValidateSubViewVM();
        while (iterator.hasNext()) {
            DlbSwtStWallet wallet = iterator.next();
            vm.setNic(wallet.getNic());
            vm.setName(wallet.getFirstName() + " " + wallet.getLastName());
            vm.setMobile(wallet.getMobileNo());
            vm.setUserName(wallet.getUsername());
            vm.setProfile(wallet.getProfile());
            if (wallet.getNicfront() != null) {

                String nicimgFront = new String(wallet.getNicfront(), "UTF-8");
                vm.setNicPage1(nicimgFront);
            }

            if (wallet.getNicback() != null) {

                String nicBack = new String(wallet.getNicback(), "UTF-8");
                vm.setNicPage2(nicBack);
            }
        }

        String query = "SELECT\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY.ID,\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY.`STATUS`,\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY.SERIAL_NO,\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY.LOTTERY_NUMBERS,\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY.WINNING_PRIZE,\n"               
                + "DLB_WB_PRODUCT.DESCRIPTION,"
                + "DLB_WB_PRODUCT.PRODUCT_ICON,"
                 + "DLB_SWT_ST_PURCHASE_HISTORY.DRAW_NO,\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY.DRAW_DATE,\n"
                + "DLB_WB_WEEK_DAY.DESCRIPTION AS DAY\n"
                + "FROM\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY\n"
                + "INNER JOIN DLB_WB_PRODUCT ON DLB_SWT_ST_PURCHASE_HISTORY.PRODUCT_CODE = DLB_WB_PRODUCT.PRODUCT_CODE\n"
                + "INNER JOIN DLB_WB_TICKET_FILE ON DLB_SWT_ST_PURCHASE_HISTORY.DRAW_NO = DLB_WB_TICKET_FILE.DRAW_NO\n"
                + "INNER JOIN DLB_WB_PRODUCT_PROFILE ON DLB_WB_TICKET_FILE.PROFILE_ID = DLB_WB_PRODUCT_PROFILE.ID\n"
                + "INNER JOIN DLB_WB_WEEK_DAY ON DLB_WB_PRODUCT_PROFILE.DAY = DLB_WB_WEEK_DAY.DAY_CODE\n"
                + "WHERE\n"
                + "DLB_SWT_ST_PURCHASE_HISTORY.ID = \"" + phid + "\";";

        List<Object[]> result = customRepository.queryExecuter(query.toUpperCase());

        List<WinnerValidateVM> winnerValidateVMListExpired = new ArrayList<WinnerValidateVM>();

        for (Object[] row : result) {
            vm.setPurchesHistoryStatus(row[1].toString());
            vm.setSerialNumber(row[2].toString());
            vm.setLotteryNumbers(row[3].toString());
            vm.setWinnningPrize(row[4].toString());
            vm.setLotteryName(row[5].toString());
            vm.setLotteryTemplate(row[6].toString());
            vm.setDrawNo(row[7].toString());
            vm.setDrawDate((Date) row[8]);
            vm.setDay((String) row[9]);
        }
        return vm;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean getverified(String walletid, String phid) throws Exception {
        boolean updatestatus = false;
        String query = "SELECT ID,STATUS,TXN_ID,CREATED_DATE FROM DLB_SWT_ST_PURCHASE_HISTORY WHERE ID = \"" + phid + "\";";

        List<Object[]> result = customRepository.queryExecuter(query);

        List<WinnerValidateVM> winnerValidateVMListExpired = new ArrayList<WinnerValidateVM>();
        String strStatus = null;
        String strTxnId = null;
        String strTxnDate = null;
        for (Object[] row : result) {
            strStatus = row[1].toString();
            strTxnId = row[2].toString();
            strTxnDate = row[3].toString();
        }
        Integer intStatus = 0;

        intStatus = Integer.parseInt(strStatus);
        if (intStatus == 21 || intStatus == 5) {
            query = "UPDATE DLB_SWT_ST_PURCHASE_HISTORY "
                    + "SET `STATUS` = \"3\" "
                    + "WHERE ID = \"" + phid + "\" "
                    + ";";

            customRepository.UpdateQueryExecuter(query);
            updatestatus = true;
        }

        try {
            WhereStatement searchFromId = new WhereStatement("id", walletid, SystemVarList.EQUAL);
            List listWithQuery = genericRepository.listWithQuery(DlbSwtStWallet.class, searchFromId);

            Iterator<DlbSwtStWallet> iterator = listWithQuery.iterator();

            //WinnerValidateSubViewVM vm =new WinnerValidateSubViewVM ();
            String pushId = "";
            String userName = "";

            while (iterator.hasNext()) {
                DlbSwtStWallet wallet = iterator.next();
                pushId = wallet.getPushId();
                userName = wallet.getUsername();
            }

//            String strJson = this.generateOTPRequest(walletid, pushId, userName, strTxnId, strTxnDate);
//            String response = null;
//
//            if (null != strJson) {
//
////                response = this.sendData(strJson);
//                if (null != response) {
//                    System.out.println("Response from Switch : " + response);
//                }
//            }

        } catch (Exception e) {

        }
        return updatestatus;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updatePaymentState(String phid) throws Exception {
        String query = "UPDATE DLB_SWT_ST_PURCHASE_HISTORY "
                + "SET `STATUS` = \"44\" "
                + "WHERE ID = \"" + phid + "\" "
                + ";";

        customRepository.UpdateQueryExecuter(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer checkPurchesHistoryStatusById(String phid) throws Exception {
        String query = "SELECT ID,STATUS FROM DLB_SWT_ST_PURCHASE_HISTORY WHERE ID = \"" + phid + "\";";

        List<Object[]> result = customRepository.queryExecuter(query);

        List<WinnerValidateVM> winnerValidateVMListExpired = new ArrayList<WinnerValidateVM>();
        String strStatus = null;
        for (Object[] row : result) {
            strStatus = row[1].toString();
        }
        Integer intStatus = 0;
        try {
            intStatus = Integer.parseInt(strStatus);
        } catch (Exception e) {

        }
        //Integer status = 5;
        return intStatus;
    }

    private String generateOTPRequest(String walletId, String pushId, String userName, String txnId, String txnDate) {
        String str_json = "";

        try {
            Date d_Date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(txnDate);
            String str_formattedTxnDate = new SimpleDateFormat("yyyyMMddHHmmss").format(d_Date);
            Integer traceNo = Integer.parseInt(walletId);
            traceNo++;
            str_json = "{\"associationType\":0,\"messageVersion\":\"001\",\"txnType\":\"03\",\"txnDate\":\"" + str_formattedTxnDate + "\",\"traceNo\":\"" + traceNo + "\",\"pushId\":\"" + pushId + "\",\"userName\":\"" + userName + "\",\"traceNo\":\"" + traceNo + "\",\"txnId\":\"" + txnId + "\",\"clientType\":\"2\"}";
        } catch (Exception e) {

        }
        //System.out.println("-------------------------------------------json" + str_json);
        return str_json;
    }

    private String sendData(String jsonStringRequest) throws ISOException {
        Socket socket = null;
        DataOutputStream dataOut = null;
        DataInputStream dataIn = null;
        byte[] request = null;
        int headerLength = 0;
        byte[] header = null;
        byte[] buffer = null;

        try {
            InetAddress anetAdd = InetAddress.getByName(Configuration.getConfiguration("SERVER_IP"));
            String str_serverPort = Configuration.getConfiguration("SERVER_PORT");
            Integer i_serverPort = Integer.parseInt(str_serverPort);
            InetSocketAddress sockAddress = new InetSocketAddress(anetAdd, i_serverPort);

            socket = new Socket();
            socket.setKeepAlive(true);

            String str_serverTimeOut = Configuration.getConfiguration("SERVER_TIMEOUT");
            Integer i_serverTimeOut = Integer.parseInt(str_serverTimeOut);

            socket.connect(sockAddress, i_serverTimeOut);
            socket.setSoTimeout(i_serverTimeOut);

            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());

            System.out.println("Server Request : \n" + jsonStringRequest);

            // ==== Disable Encryption ========
            request = jsonStringRequest.getBytes();
            byte[] mHeader = (ISOUtil.zeropad((request.length + 7) + "", 8) + "0000000").getBytes();

            byte out_buf[] = ISOUtil.concat(mHeader, request);

            System.out.println("Server Request : \n" + new String(out_buf));

            dataOut.write(out_buf);
            dataOut.flush();

            header = new byte[8];
            dataIn.readFully(header, 0, header.length);
            headerLength = Integer.parseInt(new String(header));

            buffer = new byte[headerLength];

            if (headerLength > 0) {
                dataIn.readFully(buffer, 0, headerLength);
            }

            return new String(buffer);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ISOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (null != dataOut) {
                    dataOut.close();
                }

                if (null != dataIn) {
                    dataIn.close();
                }

                if (null != socket) {
                    socket.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return null;
    }

    public static BufferedImage generateQR(String product, String sn, String combination, String winAmount, String mobileNo) {

        String barcodeText = product + ", " + sn + ", " + combination + ", " + winAmount + ", " + mobileNo;

        DataMatrix barcodeGenerator = new DataMatrix();
        BitmapCanvasProvider canvas
                = new BitmapCanvasProvider(160, BufferedImage.BITMASK, false, 0);

        barcodeGenerator.generateBarcode(canvas, barcodeText);
        return canvas.getBufferedImage();

    }

    @Transactional
    public boolean checkOtp(Integer walletId, String otp, Integer pid) throws Exception {
        WhereStatement statement1 = new WhereStatement("otp", otp, SystemVarList.EQUAL);
        WhereStatement statement2 = new WhereStatement("id", walletId, SystemVarList.EQUAL);

        DlbSwtStWallet wallet = (DlbSwtStWallet) genericRepository.get(DlbSwtStWallet.class, statement1, statement2);

        if (wallet != null) {
            DlbSwtStPurchaseHistory purchaseHistory
                    = (DlbSwtStPurchaseHistory) genericRepository.get(pid, DlbSwtStPurchaseHistory.class);

            purchaseHistory.setDlbStatusByStatus(new DlbStatus(SystemVarList.OTP_SUCESS, null, null));

            genericRepository.update(purchaseHistory);

            return true;
        } else {
            DlbSwtStPurchaseHistory purchaseHistory
                    = (DlbSwtStPurchaseHistory) genericRepository.get(pid, DlbSwtStPurchaseHistory.class);

            purchaseHistory.setDlbStatusByStatus(new DlbStatus(SystemVarList.OTP_FAIL, null, null));

            genericRepository.update(purchaseHistory);
        }
        return false;
    }
}
