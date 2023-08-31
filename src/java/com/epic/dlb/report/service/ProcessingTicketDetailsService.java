/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.ReportSearchCriteriaDto;
import com.epic.dlb.report.repository.ProcessingTicketDetailsRepository;
import com.epic.dlb.util.common.SystemVarList;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("processingTicketDetailsService")
public class ProcessingTicketDetailsService {

    @Autowired
    private ProcessingTicketDetailsRepository processingTicketDetailsRepository;

    @Transactional(rollbackFor = Exception.class)
    public JSONObject getSearch(HttpServletRequest request) throws Exception {
        JSONObject response = new JSONObject();
        JSONArray data = this.getDetails(request, "REP");
        Integer count = this.getCount(request, "REP");
        response.put("search_result", data);
        response.put("data", data);
        response.put("recordsTotal", count);
        response.put("recordsFiltered", count);
        response.put("msg", "Fetching Successfull");
        response.put("status", SystemVarList.SUCCESS);
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReportSearchCriteriaDto buildCriteria(HttpServletRequest httpServletRequest, String mode) {
        String start = httpServletRequest.getParameter("start");
        String end = httpServletRequest.getParameter("end");
        String length = httpServletRequest.getParameter("length");
        String fromDate = httpServletRequest.getParameter("fromDate") + " 00:00:00";
        String toDate = httpServletRequest.getParameter("toDate") + " 23:59:59";

        ReportSearchCriteriaDto dto = new ReportSearchCriteriaDto();
        dto.setStart(start);
        dto.setEnd(end);
        dto.setMode(mode);
        dto.setFromDate(fromDate);
        dto.setToDate(toDate);
        dto.setLength(length);
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONArray getDetails(HttpServletRequest httpServletRequest, String mode) throws Exception {
        return processingTicketDetailsRepository.getData(buildCriteria(httpServletRequest, mode));
    }
    
    @Transactional(rollbackFor = Exception.class)
    public JSONArray getDetailsForExcel(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        return processingTicketDetailsRepository.getData(reportSearchCriteriaDto);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public JSONArray getDetailsForExcel2(ReportSearchCriteriaDto reportSearchCriteriaDto) throws Exception {
        return processingTicketDetailsRepository.getDataSet2(reportSearchCriteriaDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getCount(HttpServletRequest httpServletRequest, String mode) throws Exception {
        return processingTicketDetailsRepository.getCount(buildCriteria(httpServletRequest, mode));
    }

}
