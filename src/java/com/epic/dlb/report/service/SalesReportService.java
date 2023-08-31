/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.PaginatedDataDto;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.dto.SalesTicket;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbTicket;
import com.epic.dlb.report.model.PaginatedPageData;
import com.epic.dlb.report.model.SalesSummery;
import com.epic.dlb.report.model.TicketSearchVM;
import com.epic.dlb.report.repository.TicketReportRepository;
import com.epic.dlb.repository.CustomRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("salesReportService")
public class SalesReportService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private TicketReportRepository ticketReportRepository;

    @Autowired
    private CustomRepository customRepository;

    @Transactional(rollbackFor = Exception.class)
    public List getSalesByDate(String fromDate, String toDate) {

        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement status = new WhereStatement("status", SystemVarList.CHECKOUT, SystemVarList.EQUAL);

        List listWithQuery = genericRepository.listWithQuery(DlbWbTicket.class, searchCriteriaFromDate, searchCriteriaToDate, status);

        Iterator<DlbWbTicket> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbWbTicket next = iterator.next();
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(product.getDescription());
        }

        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getSalesByDate(String fromDate, String toDate, String lotteryCode) {

        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement searchLottery = new WhereStatement("productCode", lotteryCode, SystemVarList.EQUAL, SystemVarList.AND);
        WhereStatement status = new WhereStatement("status", SystemVarList.CHECKOUT, SystemVarList.EQUAL);

        List listWithQuery = genericRepository.listWithQuery(DlbWbTicket.class, searchCriteriaFromDate, searchCriteriaToDate, searchLottery, status);

        Iterator<DlbWbTicket> iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {
            DlbWbTicket next = iterator.next();
            Hibernate.initialize(next.getDlbSwtStWallet());
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(product.getDescription());
        }

        return listWithQuery;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getSalesSummeryByDate(String fromDate, String toDate) {

        //search using criteria       
        WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
        WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
        WhereStatement status = new WhereStatement("status", SystemVarList.CHECKOUT, SystemVarList.EQUAL);

        //set projection list
        List projectionList = new ArrayList();
        projectionList.add("productCode as productCode,");
        projectionList.add("cast(count(id) as int) as count");

        List listWithQuery = genericRepository.listWithQueryNGroup(DlbWbTicket.class, projectionList, "productCode", searchCriteriaFromDate, searchCriteriaToDate, status);

        List list = new ArrayList();
        Iterator iterator = listWithQuery.iterator();

        while (iterator.hasNext()) {

            Object object[] = (Object[]) iterator.next();

            SalesSummery salesSummery = new SalesSummery();
            salesSummery.setProductCode((String) object[0]);
            salesSummery.setCount((Integer) object[1]);
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(salesSummery.getProductCode(), DlbWbProduct.class);
            salesSummery.setDescription(product.getDescription());

            list.add(salesSummery);
        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTicketFile(List<WhereStatement> whereStatements, int start, int end) {
        return ticketReportRepository.getTicketFile(whereStatements, start, end);
    }

    @Transactional(rollbackFor = Exception.class)
    public List getAllTicketFile(int id, int start, int end) {
        return ticketReportRepository.getAllTicketByTicketFile(id, start, end);
    }

    @Transactional(rollbackFor = Exception.class)
    public List getSalesTicketFile(int id, int start, int end) {
        return ticketReportRepository.getSalesTicketByTicketFile(id, start, end);
    }

    @Transactional(rollbackFor = Exception.class)
    public List getTicketByDateUsingTicket(String fromDate, String toDate, String drawNo, String lottery) throws Exception {

        fromDate = fromDate + " 00:00:00";
        toDate = toDate + " 23:59:59";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String query = "SELECT "
                + "ticf.PRODUCT_DESCRIPTION, "
                +//0
                "ticf.DRAW_NO, "
                +//1
                "ticf.DATE, "
                +//2
                "ticf.NO_OF_TICKET, "
                +//3
                "ticf.LAST_UPDATED_USER, "
                +//4
                "ticf.CREATED_TIME, "
                +//5
                "sum(IF(tic.STATUS=17,1,0)) ALLS, "
                +//6 
                "sum(IF(tic.STATUS=19,1,0)) SALES, "
                +//7
                "sum(IF(tic.STATUS=36,1,0)) RT, "
                +//8                       
                "ticf.ID, "
                + //9
                "ticf.PENDING_APPROVAL  status, "
                +//10         
                "sum(IF(tic.STATUS=18,1,0)) CART "
                + "FROM "
                + "DLB_WB_TICKET_FILE AS ticf "
                + "INNER JOIN DLB_WB_TICKET AS tic ON tic.TICKET_FILE_ID = ticf.ID "
                + "WHERE ticf.DATE >= '" + fromDate + "' "
                + "AND ticf.DATE <= '" + toDate + "' ";
        if (drawNo != null && !drawNo.equals("")) {
            query += "AND ticf.DRAW_NO = \"" + drawNo + "\" ";
        }

        if (!lottery.equals("0")) {
            query += "AND ticf.PRODUCT_CODE = \"" + lottery + "\" ";
        }

        query += "GROUP BY ticf.ID ORDER BY ticf.DATE ASC "
                + ";";

        //System.out.println("-----------------------------------------------query" + query );
        List<Object[]> result = customRepository.queryExecuter(query);

        List<TicketSearchVM> list = new ArrayList<TicketSearchVM>();

        for (Object[] row : result) {
            TicketSearchVM tsVM = new TicketSearchVM();

            tsVM.setProductDescription(row[0].toString());
            tsVM.setDrawNo(row[1].toString());
            tsVM.setDrawDate(row[2].toString());
            tsVM.setNoOfTickets(row[3].toString());
            tsVM.setAll(row[6].toString());
            tsVM.setRt(row[8].toString());
            tsVM.setSales(row[7].toString());
            String date = row[5].toString();
            tsVM.setCreateTime(simpleDateFormat.format(simpleDateFormat.parse(date)));
            tsVM.setLastUpdateUser(row[4].toString());
            tsVM.setFileID(row[9].toString());
            tsVM.setStatus(row[10].toString());
            tsVM.setCart(row[11].toString());

            list.add(tsVM);
        }

        return list;
    }

    public JSONArray buildTicketSearchReportData(List<TicketSearchVM> list) throws ParseException {

        JSONArray array = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            TicketSearchVM a = (TicketSearchVM) list.get(i);
            JSONObject jSONObject = new JSONObject();

            jSONObject.put("productDescription", a.getProductDescription());
            jSONObject.put("drawNo", a.getDrawNo());
            jSONObject.put("drawDate", a.getDrawDate());
            jSONObject.put("noOfTickets", a.getNoOfTickets());
            jSONObject.put("lastUpdateUser", a.getLastUpdateUser());
            jSONObject.put("createTime", a.getCreateTime());
            jSONObject.put("all", a.getAll());
            jSONObject.put("sales", a.getSales());
            jSONObject.put("cart", a.getCart());
            jSONObject.put("rt", Integer.parseInt(a.getAll()) - Integer.parseInt(a.getRt()));
            jSONObject.put("fileid", a.getFileID());
            jSONObject.put("status", a.getStatus());
            array.add(jSONObject);
        }
        return array;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Object[]> getFileDetailsById(String fileid) throws Exception {
        String query = "SELECT PRODUCT_DESCRIPTION,DRAW_NO,DATE,UPLOAD_BY,APPROVED_BY FROM DLB_WB_TICKET_FILE WHERE ID = \"" + fileid + "\" ;";
        List<Object[]> result = customRepository.queryExecuter(query);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Object[]> getTicketDetailsByFileId(String fileid, String status) throws Exception {
        String query = "SELECT "
                + "tic.SERIAL_NUMBER, "
                + //0
                "tic.LOTTERY_NUMBERS, "
                + //1
                "wall.MOBILE_NO, "
                + //2
                "wall.NIC, "
                + //3
                "ph.TXN_ID "
                + //4
                "FROM DLB_WB_TICKET tic "
                + "INNER JOIN DLB_SWT_ST_WALLET wall ON tic.WALLET_ID = wall.ID "
                + "INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ph ON ph.DRAW_NO = tic.DRAW_NO AND ph.PRODUCT_CODE = tic.PRODUCT_CODE "
                + "WHERE tic.TICKET_FILE_ID = \"" + fileid + "\" "
                + "AND tic.STATUS = \"" + status + "\" "
                + "; ";

        List<Object[]> result = customRepository.queryExecuter(query);
        return result;
    }

    public JSONArray buildTicketDetailsByFileId(List<Object[]> result) {
        JSONArray array = new JSONArray();
        for (Object[] row : result) {
            JSONObject jSONObject = new JSONObject();

            jSONObject.put("serialno", row[0].toString());
            jSONObject.put("lotterynumber", row[1].toString());
            jSONObject.put("mobileno", row[2].toString());
            jSONObject.put("nic", row[3].toString());
            jSONObject.put("txnid", row[4].toString());
            array.add(jSONObject);
        }
        return array;
    }

    @Transactional(rollbackFor = Exception.class)
    public PaginatedPageData getTicketDetailsByFileIdPagination(String start, String length, String fileid, String status) throws Exception {
        Integer i_start = Integer.parseInt(start);
        Integer i_length = Integer.parseInt(length);

        String query = "SELECT "
                + "tic.SERIAL_NUMBER, "
                + //0
                "tic.LOTTERY_NUMBERS, "
                + //1
                "wall.MOBILE_NO, "
                + //2
                "wall.NIC, "
                + //3
                "ph.TXN_ID "
                + //4
                "FROM DLB_WB_TICKET tic "
                + "INNER JOIN DLB_SWT_ST_WALLET wall ON tic.WALLET_ID = wall.ID "
                + "INNER JOIN DLB_SWT_ST_PURCHASE_HISTORY ph ON ph.DRAW_NO = tic.DRAW_NO AND ph.PRODUCT_CODE = tic.PRODUCT_CODE "
                + "WHERE tic.TICKET_FILE_ID = \"" + fileid + "\" "
                + "AND tic.STATUS = \"" + status + "\" "
                + "; ";

        List<Object[]> result = customRepository.queryExecuter(query);
        List<Object[]> paginatedResult = new ArrayList<Object[]>();

        Integer i = 0;
        Integer j = 0;
        for (Object[] row : result) {
            if (i >= i_start) {
                if (j < i_length) {
                    paginatedResult.add(row);
                    j++;
                } else {
                    break;
                }
            }
            i++;
        }

        PaginatedPageData pd = new PaginatedPageData();
        pd.setTotal_records(result.size());
        pd.setPaginatedData(paginatedResult);
        return pd;
    }

    @Transactional
    public List getTicketSalesReport(Date fromDate, Date toDate, String lottery) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        String query = "SELECT\n"
                + "sales_transaction.CREATED_DATE,\n"
                + "sales_transaction.SERIAL_NO,\n"
                + "sales_transaction.DRAW_NO,\n"
                + "sales_transaction.DRAW_DATE,\n"
                + "sales_transaction.FIRST_NAME,\n"
                + "sales_transaction.LAST_NAME,\n"
                + "sales_transaction.TRACENO,\n"
                + "sales_transaction.PAY_METHOD,\n"
                + "sales_transaction.AMOUNT,\n"
                + "sales_transaction.TXNID,\n"
                + "sales_transaction.PRODUCT_CODE,\n"
                + "sales_transaction.PRODUCT_DESCRIPTION,\n"
                + "sales_transaction.TICKET_PRICE \n"
                + "FROM\n"
                + "sales_transaction\n"
                + "WHERE\n"
                + "sales_transaction.CREATED_DATE BETWEEN '" + dateFormat.format(fromDate) + "' AND '" + dateFormat.format(toDate) + "' ";

        String optionalWhere = "";
        if (lottery != null && !"0".equals(lottery)) {
            optionalWhere = " AND sales_transaction.PRODUCT_CODE ='" + lottery + "' ";
        }

        Iterator<Object[]> list = customRepository.queryExecuter(query + optionalWhere).iterator();
        List<SalesTicket> salesTicket = new ArrayList<SalesTicket>();

        while (list.hasNext()) {
            Object[] next = list.next();

            SalesTicket ticket = new SalesTicket();

            Timestamp createdDate = (Timestamp) next[0];
            Timestamp drawDate = (Timestamp) next[3];

            ticket.setCreatedDate(dateFormat.format(createdDate));
            ticket.setSerialNo((String) next[1]);
            ticket.setDrawNo((String) next[2]);
            ticket.setDrawDate(dateFormat2.format(drawDate));
            ticket.setFirstName((String) next[4]);
            ticket.setLastName((String) next[5]);
            ticket.setTraceNo((String) next[6]);
            ticket.setPayMethod((String) next[7]);
            ticket.setAmount((String) next[8]);
            ticket.setTxId((String) next[9]);
            ticket.setProductCode((String) next[10]);
            ticket.setProductDescription((String) next[11]);
            ticket.setTicketPrice((String) next[12]);

            salesTicket.add(ticket);

        }

        return salesTicket;

    }

    @Transactional
    public List getPaginatedTicketSalesReport(Date fromDate, Date toDate,
            String lottery, ReportSearchCriteriaDto dto) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        String query = "SELECT\n"
                + "sales_transaction.CREATED_DATE,\n"
                + "sales_transaction.SERIAL_NO,\n"
                + "sales_transaction.DRAW_NO,\n"
                + "sales_transaction.DRAW_DATE,\n"
                + "sales_transaction.FIRST_NAME,\n"
                + "sales_transaction.LAST_NAME,\n"
                + "sales_transaction.TRACENO,\n"
                + "sales_transaction.PAY_METHOD,\n"
                + "sales_transaction.AMOUNT,\n"
                + "sales_transaction.TXNID,\n"
                + "sales_transaction.PRODUCT_CODE,\n"
                + "sales_transaction.PRODUCT_DESCRIPTION,\n"
                + "sales_transaction.TICKET_PRICE \n"
                + "FROM\n"
                + "sales_transaction\n"
                + "WHERE\n"
                + "sales_transaction.CREATED_DATE BETWEEN '"
                + dateFormat.format(fromDate) + "' AND '" + dateFormat.format(toDate) + "' ";

        String optionalWhere = "";
        if (lottery != null && !"0".equals(lottery)) {
            optionalWhere = " AND sales_transaction.PRODUCT_CODE ='" + lottery + "' ";
        }

        if (dto.getMode().equals("REP")) {
            int limit = 0;
            int length = 10;

            if (dto.getLength() != null) {
                length = Integer.parseInt(dto.getLength());
            }

            if (!dto.getStart().equals("0")) {
                limit = Integer.parseInt(dto.getStart());
                optionalWhere = optionalWhere + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                optionalWhere = optionalWhere + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }

        Iterator<Object[]> list = customRepository.queryExecuter(query + optionalWhere).iterator();
        List<SalesTicket> salesTicket = new ArrayList<SalesTicket>();

        while (list.hasNext()) {
            Object[] next = list.next();

            SalesTicket ticket = new SalesTicket();

            Timestamp createdDate = (Timestamp) next[0];
            Timestamp drawDate = (Timestamp) next[3];

            ticket.setCreatedDate(dateFormat.format(createdDate));
            ticket.setSerialNo((String) next[1]);
            ticket.setDrawNo((String) next[2]);
            ticket.setDrawDate(dateFormat2.format(drawDate));
            ticket.setFirstName((String) next[4]);
            ticket.setLastName((String) next[5]);
            ticket.setTraceNo((String) next[6]);
            ticket.setPayMethod((String) next[7]);
            ticket.setAmount((String) next[8]);
            ticket.setTxId((String) next[9]);
            ticket.setProductCode((String) next[10]);
            ticket.setProductDescription((String) next[11]);
            ticket.setTicketPrice((String) next[12]);

            salesTicket.add(ticket);

        }

        return salesTicket;

    }

    @Transactional
    public Integer getTicketSalesCount(Date fromDate, Date toDate,
            String lottery, ReportSearchCriteriaDto dto) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        String query = "SELECT\n"
                + "COUNT(*)\n"
                + "FROM\n"
                + "sales_transaction\n"
                + "WHERE\n"
                + "sales_transaction.CREATED_DATE BETWEEN '"
                + dateFormat.format(fromDate) + "' AND '" + dateFormat.format(toDate) + "' ";

        String optionalWhere = "";
        if (lottery != null && !"0".equals(lottery)) {
            optionalWhere = " AND sales_transaction.PRODUCT_CODE ='" + lottery + "' ";
        }

        List<Object[]> result = customRepository.queryExecuter(query + optionalWhere);
        return Integer.parseInt(result.toString().replace("[", "").replace("]", ""));

    }

    @Transactional
    public Double getTicketSalesTotalAmount(Date fromDate, Date toDate,
            String lottery, ReportSearchCriteriaDto dto) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        String query = "SELECT\n"
                + "SUM(sales_transaction.AMOUNT)\n"
                + "FROM\n"
                + "sales_transaction\n"
                + "WHERE\n"
                + "sales_transaction.CREATED_DATE BETWEEN '"
                + dateFormat.format(fromDate) + "' AND '" + dateFormat.format(toDate) + "' ";

        String optionalWhere = "";
        if (lottery != null && !"0".equals(lottery)) {
            optionalWhere = " AND sales_transaction.PRODUCT_CODE ='" + lottery + "' ";
        }

        List<Object[]> result = customRepository.queryExecuter(query + optionalWhere);
        return Double.parseDouble(result.toString().replace("[", "").replace("]", ""));

    }

    @Transactional
    public List getTicketNotSalesReport(String fromDate, String toDate, String product, String status) {

        List whers = new ArrayList();
        if (fromDate != null) {
            fromDate = fromDate + " 00:00:00";
            WhereStatement searchCriteriaFromDate = new WhereStatement("createdDate", fromDate, SystemVarList.GREATER_THAN, SystemVarList.AND);
            whers.add(searchCriteriaFromDate);

        }

        if (toDate != null) {
            toDate = toDate + " 23:59:59";
            WhereStatement searchCriteriaToDate = new WhereStatement("createdDate", toDate, SystemVarList.LESS_THAN, SystemVarList.AND);
            whers.add(searchCriteriaToDate);

        }

        if (product != null && !product.equals("0")) {
            WhereStatement statusWh = new WhereStatement("status", status, SystemVarList.EQUAL, SystemVarList.AND);
            WhereStatement productCode = new WhereStatement("productCode", product, SystemVarList.EQUAL);

            whers.add(statusWh);
            whers.add(productCode);
        } else {
            WhereStatement statusWh = new WhereStatement("status", status, SystemVarList.EQUAL);
            whers.add(statusWh);
        }

        List<DlbWbTicket> dlbSwtStPurchaseHistorys = genericRepository
                .listWithQuery(DlbWbTicket.class, whers);

        Iterator<DlbWbTicket> iterator = dlbSwtStPurchaseHistorys.iterator();

        while (iterator.hasNext()) {
            DlbWbTicket next = iterator.next();
            DlbWbProduct dlbWbProduct = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(dlbWbProduct.getDescription());

        }

        return dlbSwtStPurchaseHistorys;

    }

    @Transactional
    public PaginatedDataDto getReturnedTicketsReport(String fromDate, String toDate, String product,
            String status, Integer start, Integer length) {

        List whers = new ArrayList();
        fromDate = fromDate + " 00:00:00";
        toDate = toDate + " 23:59:59";

        String whereQuery = "WHERE drawDate BETWEEN '" + fromDate + "' AND '" + toDate + "' AND ";

        if (product != null && !product.equals("0")) {
            whereQuery = whereQuery + "status = '" + status + "'  and productCode = '" + product + "' ";

        } else {
            whereQuery = whereQuery + "status = '" + status + "' ";
        }
        List<DlbWbTicket> dlbSwtStPurchaseHistorys = new ArrayList<>();
        System.out.println(start);
        if (start != null) {
            dlbSwtStPurchaseHistorys = genericRepository
                    .randomListWithLimit(DlbWbTicket.class, whereQuery, start, length);
        } else {
            dlbSwtStPurchaseHistorys = genericRepository.randomList(DlbWbTicket.class, whereQuery);
        }
        Long count = genericRepository.randomListCount(DlbWbTicket.class, whereQuery);
        Iterator<DlbWbTicket> iterator = dlbSwtStPurchaseHistorys.iterator();

        while (iterator.hasNext()) {
            DlbWbTicket next = iterator.next();
            DlbWbProduct dlbWbProduct = (DlbWbProduct) genericRepository.get(next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(dlbWbProduct.getDescription());

        }

        return new PaginatedDataDto(dlbSwtStPurchaseHistorys, count.intValue());

    }

    @Transactional
    public Date getPurchaseDate(String serialNo) {
        WhereStatement searchCriteriaFromDate = new WhereStatement("serialNo", serialNo, SystemVarList.EQUAL);
        DlbSwtStPurchaseHistory dlbSwtStPurchaseHistory = (DlbSwtStPurchaseHistory) genericRepository.get(DlbSwtStPurchaseHistory.class, searchCriteriaFromDate);
        return dlbSwtStPurchaseHistory.getCreatedDate();
    }

}
