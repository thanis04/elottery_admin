/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.repository;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.dto.BrandwiseSearchReportDto;
import com.epic.dlb.repository.CustomRepository;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository("brandWiseSearchReportRepository")
public class BrandWiseSearchReportRepository {

    @Autowired
    private CustomRepository customRepository;

    private SimpleDateFormat dateFormat;

    public List<BrandwiseSearchReportDto> findPrizeClaimedOrUnclaimed(ReportSearchCriteriaDto dto) {
        List<BrandwiseSearchReportDto> reportDtos = new ArrayList<>();
        return reportDtos;
    }

    public List<BrandwiseSearchReportDto> findTicketExpired(ReportSearchCriteriaDto dto) throws Exception {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryStr = "SELECT "
                + "`P`.DESCRIPTION, "
                + "`PH`.`DRAW_NO`, "
                + "`PH`.`DRAW_DATE`, "
                + "`PH`.`SERIAL_NO`, "
                + "`PH`.`CREATED_DATE`, "
                + "`WL`.`NIC`, "
                + "`WL`.`FIRST_NAME`, "
                + "`WL`.`LAST_NAME`, "
                + "`TR`.`AMOUNT`, "
                + "`PH`.`WINNING_PRIZE`, "
                + "`TR`.`TOKEN`, "
                + "DATE_ADD(`PH`.`DRAW_DATE`, INTERVAL 180 DAY) AS `EXP_DATE` "
                + "FROM `DLB_SWT_ST_PURCHASE_HISTORY` `PH` "
                + "INNER JOIN `DLB_WB_PRODUCT` `P` ON `PH`.`PRODUCT_CODE` = `P`.`PRODUCT_CODE` "
                + "INNER JOIN `DLB_SWT_ST_TRANSACTION` `TR` ON `PH`.`TXN_ID` = `TR`.`TXNID` "
                + "LEFT JOIN `DLB_SWT_ST_TOKEN` `TK` ON `TR`.`TOKEN` = `TK`.`TOKEN` "
                + "INNER JOIN `DLB_SWT_ST_WALLET` `WL` ON `PH`.`WALLET_ID` = `WL`.`ID` "
                + "WHERE `PH`.`STATUS` = '" + 28 + "' AND `P`.`STATUS` = '" + 1 + "' ";

        if (!(dto.getFromDate() == null || dto.getToDate() == null)) {
            queryStr = queryStr + "AND `PH`.`DRAW_DATE` "
                    + "BETWEEN '" + dto.getFromDate() + "' AND '" + dto.getToDate() + "' ";
        }

        if (!dto.getGameType().equals("0") && !dto.getDrawNumber().equals("")) {
            queryStr = queryStr + "AND `PH`.`PRODUCT_CODE` = '" + dto.getGameType() + "' "
                    + "AND `PH`.`DRAW_NO` = '" + dto.getDrawNumber() + "' ";
        } else {
            if (!dto.getGameType().equals("0")) {
                queryStr = queryStr + "AND `PH`.`PRODUCT_CODE` = '" + dto.getGameType() + "' ";
            } else if (!dto.getDrawNumber().equals("")) {
                queryStr = queryStr + "AND `PH`.`DRAW_NO` = '" + dto.getDrawNumber() + "' ";
            }
        }
        if (dto.getMode().equals("REP")) {
            int limit = 0;
            int length = 10;

            if (dto.getLength() != null) {
                length = Integer.parseInt(dto.getLength());
            }

            if (!dto.getStart().equals("0")) {
                limit = Integer.parseInt(dto.getStart());
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }
        List<Object[]> resultList = customRepository.queryExecuter(queryStr);
        List<BrandwiseSearchReportDto> list = new ArrayList<>();
        resultList.forEach(row -> {
            BrandwiseSearchReportDto result = new BrandwiseSearchReportDto();
            result.setLotteryType(row[0].toString());
            result.setDrawNo(row[1].toString());
            result.setDrawDate(row[2] == null ? "-" : dateFormat.format((Date) row[2]));
            result.setTicketReference(row[3] == null ? "-" : row[3].toString());
            result.setPurchasedDate(row[4] == null ? "-" : dateFormat.format((Date) row[4]));
            result.setNicNumber(row[5] == null ? "-" : row[5].toString());
            result.setCustomerReference(row[6].toString()
                    + " " + row[7].toString());
            result.setTicketValue(row[8] == null ? "-" : row[8].toString());
            result.setPrizeValue(row[9] == null ? "-" : row[9].toString());
            result.setExpiredDate(row[11] == null ? "-" : dateFormat.format((Date) row[11]));
            list.add(result);
        });

        return list;
    }

    public List<BrandwiseSearchReportDto> findDrawSummary(
            ReportSearchCriteriaDto dto) throws Exception {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryStr = "SELECT "
                + "`BDV`.`PRODUCT_DESCRIPTION` AS `PRODUCT_DESCRIPTION`, "
                + "`BDV`.`DRAW_NO` AS `DRAW_NO`, "
                + "`BDV`.`DATE` AS `DRAW_DATE`, "
                + "`BDV`.`ALLOCATED` AS `ALLOCATED`, "
                + "`BDV`.`SOLD` AS `SOLD`, "
                + "`BDV`.`RETURNED` AS `RETURNED`, "
                + "`BDV`.`WINNING_COUNT` AS `WINNING_COUNT`, "
                + "`BDV`.`WINNING_TOTAL` AS `WINNING_TOTAL`, "
                + "`BDV`.`PRODUCT_CODE` AS `PRODUCT_CODE` "
                + "FROM "
                + "`BRANDWISE_DRAW_VIEW` `BDV` "
                + "WHERE `BDV`.`DATE` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        if (!dto.getGameType().equals("0") && !dto.getDrawNumber().equals("")) {
            queryStr = queryStr + "AND `BDV`.`PRODUCT_CODE` = '" + dto.getGameType() + "' "
                    + "AND `BDV`.`DRAW_NO` = '" + dto.getDrawNumber() + "' ";
        } else {
            if (!dto.getGameType().equals("0")) {
                queryStr = queryStr + "AND `BDV`.`PRODUCT_CODE` = '" + dto.getGameType() + "' ";
            } else if (!dto.getDrawNumber().equals("")) {
                queryStr = queryStr + "AND `BDV`.`DRAW_NO` = '" + dto.getDrawNumber() + "' ";
            }
        }

//        queryStr = queryStr + "GROUP BY `BDV`.`PRODUCT_CODE` AND `BDV`.`DRAW_NO` ";
        if (dto.getMode().equals("REP")) {
            int limit = 0;
            int length = 10;

            if (dto.getLength() != null) {
                length = Integer.parseInt(dto.getLength());
            }

            if (!dto.getStart().equals("0")) {
                limit = Integer.parseInt(dto.getStart());
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            } else {
                queryStr = queryStr + "LIMIT " + limit + " , " + length + " ".replace("'", "");
            }
        }

        List<Object[]> resultList = customRepository.queryExecuter(queryStr);
        List<BrandwiseSearchReportDto> list = new ArrayList<>();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        resultList.forEach(row -> {
            BrandwiseSearchReportDto result = new BrandwiseSearchReportDto();
            result.setLotteryType(row[0] == null ? "-" : row[0].toString());
            result.setDrawNo(row[1] == null ? "-" : row[1].toString());
            result.setDrawDate(row[2] == null ? "-" : dateFormat.format((Date) row[2]));
            result.setAllocatedTickets(row[3] == null ? "-" : row[3].toString());
            result.setSoldTickets(row[4] == null ? "-" : row[4].toString());
            result.setReturnedTickets(row[5] == null ? "-" : row[5].toString());
            result.setProductCode(row[8].toString());
            setStatusByCountPerDraw(result);
            list.add(result);
        });

        return list;
    }

    public BrandwiseSearchReportDto setStatusByCountPerDraw(BrandwiseSearchReportDto result) {

        result.setWinningTicketCount(getPrizeTotalPerDraw(
                "SELECT COUNT( * ) "
                + "FROM `DLB_SWT_ST_PURCHASE_HISTORY` `PH` "
                + "WHERE "
                + "`PH`.`DRAW_NO` = " + result.getDrawNo() + " "
                + "AND `PH`.`PRODUCT_CODE` = '" + result.getProductCode() + "' ").
                replace(".00", ""));

        String winningTotal = getPrizeTotalPerDraw(
                "SELECT SUM( `PH`.`WINNING_PRIZE` ) "
                + "FROM `DLB_SWT_ST_PURCHASE_HISTORY` `PH` "
                + "WHERE "
                + "`PH`.`DRAW_NO` = " + result.getDrawNo() + " "
                + "AND `PH`.`PRODUCT_CODE` = '" + result.getProductCode() + "' ");

        result.setWinningTicketTotal(winningTotal);

        result.setClaimed(getStatusByCountPerDraw(
                "`DSS`.`DRAW_NO` = " + result.getDrawNo() + " "
                + "AND `DSS`.`PRODUCT_CODE` = '" + result.getProductCode() + "' "
                + "AND ( `DSS`.`STATUS` = 25 OR `DSS`.`STATUS` = 60 )"));
        result.setProcessing(getStatusByCountPerDraw(
                "`DSS`.`DRAW_NO` = " + result.getDrawNo() + " "
                + "AND `DSS`.`PRODUCT_CODE` = '" + result.getProductCode() + "' "
                + "AND ( `DSS`.`STATUS` = 26 )"));
        result.setExpired(getStatusByCountPerDraw(
                "`DSS`.`DRAW_NO` = " + result.getDrawNo() + " "
                + "AND `DSS`.`PRODUCT_CODE` = '" + result.getProductCode() + "' "
                + "AND ( `DSS`.`STATUS` = 28 )"));
        result.setUnclaimed(getStatusByCountPerDraw(
                "`DSS`.`DRAW_NO` = " + result.getDrawNo() + " "
                + "AND `DSS`.`PRODUCT_CODE` = '" + result.getProductCode() + "' "
                + "AND ( `DSS`.`STATUS` = 20 OR `DSS`.`STATUS` = 21 )"));

        return result;
    }

    public String getStatusByCountPerDraw(String statusQuery) {
        String queryStr = "SELECT "
                + "SUM( `DSS`.`COUNT` ) "
                + "FROM `DRAW_SUMMARY_BY_STATUS` `DSS` "
                + "WHERE ";
        queryStr = queryStr + statusQuery;
        try {
            List<Object[]> resultList = customRepository.queryExecuter(queryStr);
            if (resultList.get(0) == null) {
                return "-";
            } else {
                Object value = resultList.get(0);
                return value.toString();
            }

        } catch (Exception ex) {
            Logger.getLogger(BrandWiseSearchReportRepository.class.getName()).log(Level.SEVERE, null, ex);
            return "-";
        }
    }

    public String getPrizeTotalPerDraw(String statusQuery) {
        String queryStr = "AND `PH`.`WINNING_PRIZE` > 0 ";
        queryStr = statusQuery + queryStr;
        String format = "-";
        try {
            List<Object[]> resultList = customRepository.queryExecuter(queryStr);
            if (resultList.get(0) == null) {
                return "-";
            } else {

                DecimalFormat df = new DecimalFormat("#.##");
                format = df.format(resultList.get(0));
                format = format + ".00";

//                Object value = resultList.get(0);
                return format;
            }

        } catch (Exception ex) {
            Logger.getLogger(BrandWiseSearchReportRepository.class.getName()).log(Level.SEVERE, null, ex);
            return "-";
        }
    }

    public Integer findTicketExpiredCount(
            ReportSearchCriteriaDto dto) throws Exception {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryStr = "SELECT "
                + " COUNT(*) "
                + "FROM `DLB_SWT_ST_PURCHASE_HISTORY` `PH` "
                + "INNER JOIN `DLB_WB_PRODUCT` `P` ON `PH`.`PRODUCT_CODE` = `P`.`PRODUCT_CODE` "
                + "INNER JOIN `DLB_SWT_ST_TRANSACTION` `TR` ON `PH`.`TXN_ID` = `TR`.`TXNID` "
                + "LEFT JOIN `DLB_SWT_ST_TOKEN` `TK` ON `TR`.`TOKEN` = `TK`.`TOKEN` "
                + "INNER JOIN `DLB_SWT_ST_WALLET` `WL` ON `PH`.`WALLET_ID` = `WL`.`ID` "
                + "WHERE `PH`.`STATUS` = '" + 28 + "' AND `P`.`STATUS` = '" + 1 + "' ";

        if (!(dto.getFromDate() == null || dto.getToDate() == null)) {
            queryStr = queryStr + "AND `PH`.`DRAW_DATE` "
                    + "BETWEEN '" + dto.getFromDate() + "' AND '" + dto.getToDate() + "' ";
        }

        if (!dto.getGameType().equals("0") && !dto.getDrawNumber().equals("")) {
            queryStr = queryStr + "AND `PH`.`PRODUCT_CODE` = '" + dto.getGameType() + "' "
                    + "AND `PH`.`DRAW_NO` = '" + dto.getDrawNumber() + "' ";
        } else {
            if (!dto.getGameType().equals("0")) {
                queryStr = queryStr + "AND `PH`.`PRODUCT_CODE` = '" + dto.getGameType() + "' ";
            } else if (!dto.getDrawNumber().equals("")) {
                queryStr = queryStr + "AND `PH`.`DRAW_NO` = '" + dto.getDrawNumber() + "' ";
            }
        }
        List<Object[]> resultList = customRepository.queryExecuter(queryStr);

        return Integer.parseInt(resultList.toString().replace("[", "").replace("]", "").trim());
    }

    public Integer findDrawSummaryCount(
            ReportSearchCriteriaDto dto) throws Exception {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String queryStr = "SELECT "
                + " COUNT(*) "
                + "FROM "
                + "`BRANDWISE_DRAW_VIEW` `BDV` "
                + "WHERE `BDV`.`DATE` "
                + "BETWEEN '" + dto.getFromDate() + "' "
                + "AND '" + dto.getToDate() + "' ";

        if (!dto.getGameType().equals("0") && !dto.getDrawNumber().equals("")) {
            queryStr = queryStr + "AND `BDV`.`PRODUCT_CODE` = '" + dto.getGameType() + "' "
                    + "AND `BDV`.`DRAW_NO` = '" + dto.getDrawNumber() + "' ";
        } else {
            if (!dto.getGameType().equals("0")) {
                queryStr = queryStr + "AND `BDV`.`PRODUCT_CODE` = '" + dto.getGameType() + "' ";
            } else if (!dto.getDrawNumber().equals("")) {
                queryStr = queryStr + "AND `BDV`.`DRAW_NO` = '" + dto.getDrawNumber() + "' ";
            }
        }

        queryStr = queryStr + "GROUP BY `BDV`.`DRAW_NO` ";

        List<Object[]> resultList = customRepository.queryExecuter(queryStr);

        return resultList.size();
    }
}
