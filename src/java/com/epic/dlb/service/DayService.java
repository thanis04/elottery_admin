/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.model.DlbWbWeekDay;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("dayService")
public class DayService {
    @Autowired
    private GenericRepository genericRepository;
    
    //save method
    @Transactional(rollbackFor =Exception.class )
    public String save(DlbWbWeekDay dlbWbWeekDay) throws Exception,SQLException{
        return  (String) genericRepository.save(dlbWbWeekDay);
    }
    
    //update method
    @Transactional(rollbackFor =Exception.class )
    public int update(DlbWbWeekDay product) throws Exception,SQLException{
        return  (int) genericRepository.update(product);
    }
    
    //get method
    @Transactional(rollbackFor =Exception.class )
    public DlbWbWeekDay get(String id) throws Exception{
        return  (DlbWbWeekDay) genericRepository.get(id, DlbWbProduct.class);
    }
    
    //get method
    @Transactional(rollbackFor =Exception.class )
    public int delete(DlbWbWeekDay weekDay) throws Exception,SQLException{
        return   genericRepository.delete(weekDay);
    }
    
    //list all method
   @Transactional(rollbackFor =Exception.class )
    public List listAll() throws Exception,SQLException{
        return genericRepository.list(DlbWbWeekDay.class);
    }
    
    //list all by WhereStatement method
   @Transactional(rollbackFor =Exception.class )
    public List listAll(WhereStatement ... whereStatements) throws Exception,SQLException{
        return genericRepository.list(DlbWbWeekDay.class, whereStatements);
    }
    
    //search method
   @Transactional(rollbackFor =Exception.class )
    public List search(WhereStatement ... whereStatements) throws Exception{
        return genericRepository.list(DlbWbWeekDay.class, whereStatements);
    }
    
     //list for select boxes
   @Transactional(rollbackFor =Exception.class )
    public List loadSelectBox(int status) throws Exception{
        DlbStatus dlbStatus = new DlbStatus(status, null, null);
        
        WhereStatement whereStatement=new WhereStatement("dlbStatus",dlbStatus, SystemVarList.EQUAL);
        return genericRepository.loadSelectBox(DlbWbWeekDay.class,"dayCode","description",whereStatement);
    }
    
}
