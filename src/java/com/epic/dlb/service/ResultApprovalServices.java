/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbResult;
import com.epic.dlb.model.DlbWbResultDetails;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author methsiri_h
 */

@Service("resultApprovalServices")
public class ResultApprovalServices {
    
     @Autowired
    private GenericRepository genericRepository;
    
     
     //search dlbresults
     @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbWbResult.class, whereStatements); 

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbResult productProfile = (DlbWbResult) tmpList.get(i);
            //initi proxy objects
            Hibernate.initialize(productProfile.getDlbWbProduct());
            Hibernate.initialize(productProfile.getDlbWbWeekDay());
            Hibernate.initialize(productProfile.getDlbStatus());

            list.add(productProfile);

        }
        return list;

    }
     
    //update method dlbresult
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbResult wbResult ) throws Exception {
        return (int) genericRepository.update(wbResult);
    }
    
    
    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbWbResult get(int id) throws Exception {
        DlbWbResult result = (DlbWbResult) genericRepository.get(id, DlbWbResult.class);
        
        //init proxy objects
        Hibernate.initialize(result.getDlbWbProduct());
        Hibernate.initialize(result.getDlbWbWeekDay());
        Hibernate.initialize(result.getDlbStatus());
        Iterator<DlbWbResultDetails> tmpDetails = result.getDlbWbResultDetailses().iterator();
        
        List<DlbWbResultDetails> dlbWbResultDetailses = result.getDlbWbResultDetailses();
        while (tmpDetails.hasNext()) {
            DlbWbResultDetails details = tmpDetails.next();
            Hibernate.initialize(details.getDlbWbProductItem().getDescription());
            //add init object to list
            dlbWbResultDetailses.add(details);
            
            
        }
        //set details to list
        
        result.setDlbWbResultDetailses(dlbWbResultDetailses);
        return result;
    }
}
