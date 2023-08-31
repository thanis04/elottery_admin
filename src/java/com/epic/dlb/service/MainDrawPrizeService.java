/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbMainDrawPrizeCode;
import com.epic.dlb.model.DlbWbMainDrawPrizeCodeId;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("drawPrizeService")
public class MainDrawPrizeService {

    @Autowired
    private GenericRepository genericRepository;

    //ge
    @Transactional
    public DlbWbMainDrawPrizeCode getMainPrizeCode(String productCode, String prizeCode) {
        DlbWbMainDrawPrizeCodeId drawPrizeCodeId = new DlbWbMainDrawPrizeCodeId(productCode, prizeCode);
       
        return (DlbWbMainDrawPrizeCode) genericRepository.get(drawPrizeCodeId,DlbWbMainDrawPrizeCode.class);
    }

}