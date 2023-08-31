/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.BrandwisePaginatedPageData;
import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.dto.BrandwiseSearchReportDto;
import com.epic.dlb.model.DlbSwtStPurchaseHistory;
import com.epic.dlb.model.DlbSwtStToken;
import com.epic.dlb.model.DlbSwtStTransaction;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.report.repository.BrandWiseSearchReportRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("brandWiseSearchReportService")
public class BrandWiseSearchReportService {

    @Autowired
    private BrandWiseSearchReportRepository brandWiseSearchReportRepository;

    private static SimpleDateFormat dateFormat;

    @Autowired
    private GenericRepository genericRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<BrandwiseSearchReportDto> getPrizeClaimedOrUnclaimed(ReportSearchCriteriaDto dto) {
        List<BrandwiseSearchReportDto> reportDtos = brandWiseSearchReportRepository.
                findPrizeClaimedOrUnclaimed(dto);
        return reportDtos;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<BrandwiseSearchReportDto> getTicketExpired(ReportSearchCriteriaDto dto) throws Exception {
        List<BrandwiseSearchReportDto> resultList = brandWiseSearchReportRepository.
                findTicketExpired(dto);
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public BrandwisePaginatedPageData getTicketExpiredPaginated(
            ReportSearchCriteriaDto dto) throws Exception {
        List<BrandwiseSearchReportDto> resultList = brandWiseSearchReportRepository.
                findTicketExpired(dto);
        return getTicketExpiredPaginatedData(resultList, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<BrandwiseSearchReportDto> getDrawSummary(ReportSearchCriteriaDto dto) throws Exception {
        List<BrandwiseSearchReportDto> resultList = brandWiseSearchReportRepository.
                findDrawSummary(dto);
        return resultList;
    }

    @Transactional(rollbackFor = Exception.class)
    public BrandwisePaginatedPageData getDrawSummaryPaginated(ReportSearchCriteriaDto dto) throws Exception {
        List<BrandwiseSearchReportDto> reportDtos = brandWiseSearchReportRepository.
                findDrawSummary(dto);
        return getDrawWisePaginatedData(reportDtos, dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<DlbSwtStPurchaseHistory> viewPurchaseHistory(String lotteryType, String drawNo) {

        List<DlbSwtStPurchaseHistory> listWithQuery = new ArrayList<>();

        if (lotteryType == null && drawNo == null) {
            WhereStatement status = new WhereStatement("status", SystemVarList.WINNING_EXPIRED, SystemVarList.EQUAL);
            listWithQuery = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, status);
        } else if (lotteryType != null && drawNo != null) {
            WhereStatement productCode = new WhereStatement("productCode", lotteryType, SystemVarList.EQUAL, SystemVarList.AND);
            WhereStatement draw = new WhereStatement("drawNo", drawNo, SystemVarList.EQUAL, SystemVarList.AND);
            WhereStatement status = new WhereStatement("status", SystemVarList.WINNING_EXPIRED, SystemVarList.EQUAL);
            listWithQuery = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, status, productCode, draw);
        } else {
            if (lotteryType != null) {
                WhereStatement productCode = new WhereStatement("productCode", lotteryType, SystemVarList.EQUAL, SystemVarList.AND);
                WhereStatement status = new WhereStatement("status", SystemVarList.WINNING_EXPIRED, SystemVarList.EQUAL);
                listWithQuery = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, status, productCode);
            } else if (drawNo != null) {
                WhereStatement draw = new WhereStatement("drawNo", drawNo, SystemVarList.EQUAL, SystemVarList.AND);
                WhereStatement status = new WhereStatement("status", SystemVarList.WINNING_EXPIRED, SystemVarList.EQUAL);
                listWithQuery = genericRepository.listWithQuery(DlbSwtStPurchaseHistory.class, status, draw);
            }
        }

        Iterator<DlbSwtStPurchaseHistory> iterator = listWithQuery.iterator();
        while (iterator.hasNext()) {
            DlbSwtStPurchaseHistory next = iterator.next();
            DlbSwtStTransaction transaction = (DlbSwtStTransaction) genericRepository.get(
                    next.getTxnId(), DlbSwtStTransaction.class);
            Hibernate.initialize(next.getDlbSwtStWallet());
            DlbWbProduct product = (DlbWbProduct) genericRepository.get(
                    next.getProductCode(), DlbWbProduct.class);
            next.setProductDescription(product.getDescription());
            if (transaction != null) {
                Hibernate.initialize(transaction.getDlbSwtMtPaymentMethod());
                next.setPaymentMethod(transaction.getDlbSwtMtPaymentMethod().getDescription());
                WhereStatement statement = new WhereStatement(
                        "token", transaction.getToken(), SystemVarList.EQUAL);
                DlbSwtStToken stToken = (DlbSwtStToken) genericRepository.get(
                        DlbSwtStToken.class, statement);
                next.setReferenceNo(stToken.getMaskedNumber());

            }
        }

        return listWithQuery;
    }

    public BrandwisePaginatedPageData getTicketExpiredPaginatedData(
            List<BrandwiseSearchReportDto> resultList,
            ReportSearchCriteriaDto dto) throws Exception {

        BrandwisePaginatedPageData pageData = new BrandwisePaginatedPageData();
        pageData.setPaginatedData(resultList);
        pageData.setTotalRecords(brandWiseSearchReportRepository.findTicketExpiredCount(dto));

        return pageData;
    }

    public BrandwisePaginatedPageData getDrawWisePaginatedData(
            List<BrandwiseSearchReportDto> resultList,
            ReportSearchCriteriaDto dto) throws Exception {

        BrandwisePaginatedPageData pageData = new BrandwisePaginatedPageData();
        pageData.setPaginatedData(resultList);
        pageData.setTotalRecords(brandWiseSearchReportRepository.findDrawSummaryCount(dto));

        return pageData;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getAllRecordCount(ReportSearchCriteriaDto dto) throws Exception {
        return brandWiseSearchReportRepository.findTicketExpiredCount(dto);
    }

    public List<String> getFormattedExpiredDate(String fromDate, String toDate) throws ParseException {
        List<String> list = new ArrayList<>();
        Date fDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
        Date tDate = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);

        Calendar fcal = Calendar.getInstance();
        fcal.setTime(fDate);
        fcal.add(Calendar.DATE, -180);
        fDate = fcal.getTime();

        Calendar tcal = Calendar.getInstance();
        tcal.setTime(tDate);
        tcal.add(Calendar.DAY_OF_MONTH, -180);
        tDate = tcal.getTime();
        list.add(new SimpleDateFormat("yyyy-MM-dd").format(fDate) + " 00:00:00");
        list.add(new SimpleDateFormat("yyyy-MM-dd").format(tDate) + " 23:59:59");
        return list;
    }
}
