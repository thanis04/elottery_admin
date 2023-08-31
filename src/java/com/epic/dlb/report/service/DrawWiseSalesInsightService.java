/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.report.service;

import com.epic.dlb.dto.DrawWiseSalesInsightDto;
import com.epic.dlb.report.repository.DrawWiseSalesInsightRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("drawWiseSalesInsightService")
public class DrawWiseSalesInsightService {

    @Autowired
    private DrawWiseSalesInsightRepository drawWiseSalesInsightRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<Object[]> getInsight(DrawWiseSalesInsightDto dto) throws Exception {
        return drawWiseSalesInsightRepository.getInsight(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer getInsightCount(DrawWiseSalesInsightDto dto) throws Exception {
        return drawWiseSalesInsightRepository.getInsightCount(dto);
    }
}
