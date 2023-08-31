/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbProductItem;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("productItemService")
public class ProductItemService {

    @Autowired
    private GenericRepository genericRepository;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public String save(DlbWbProductItem productItem) throws Exception {
        return (String) genericRepository.save(productItem);
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbProductItem productItem) throws Exception {
        return (int) genericRepository.update(productItem);
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbWbProductItem get(String id) {
        return (DlbWbProductItem) genericRepository.get(id, DlbWbProductItem.class);
    }
    
    //list by status
    @Transactional(rollbackFor = Exception.class)
    public List listByStatus() throws Exception {
        
      DlbStatus dlbStatus = new DlbStatus(SystemVarList.ACTIVE, null, null);  
      WhereStatement whereStatement = new WhereStatement("dlbStatus", dlbStatus, SystemVarList.EQUAL);
        
        
        return genericRepository.loadSelectBox(DlbWbProductItem.class, null, null, whereStatement);
        
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
//      WhereStatement whereStatement = new WhereStatement(property, this, operator)
        
        
        List list = genericRepository.list(DlbWbProductItem.class);
        Iterator<DlbWbProductItem> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbWbProductItem next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());

        }
        return list;
    }

    //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List listAll(WhereStatement... whereStatements) throws Exception {
        return genericRepository.list(DlbWbProductItem.class, whereStatements);
    }

    //delete method
    @Transactional(rollbackFor = Exception.class)
    public int delete(DlbWbProductItem productItem) throws Exception {
        return genericRepository.delete(productItem);
    }

    //search method
    @Transactional(rollbackFor = Exception.class)
    public List search(WhereStatement... whereStatements) throws Exception {
        List list = genericRepository.list(DlbWbProductItem.class, whereStatements);

        Iterator<DlbWbProductItem> iterator = list.iterator();

        while (iterator.hasNext()) {
            DlbWbProductItem next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());  

        }
        return list;
    }

}
