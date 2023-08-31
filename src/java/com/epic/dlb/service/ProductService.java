/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbStatus;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service
public class ProductService {

    @Autowired
    private GenericRepository genericRepository;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public String save(DlbWbProduct product) throws Exception, SQLException {
        return (String) genericRepository.save(product);
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbProduct product) throws Exception, SQLException {
        return (int) genericRepository.update(product);
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbWbProduct get(String id) throws Exception {
        DlbWbProduct product = (DlbWbProduct) genericRepository.get(id, DlbWbProduct.class);
        if(product!=null){
            Hibernate.initialize(product.getDlbStatus());
        }
        return product;
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public int delete(DlbWbProduct product) throws Exception, SQLException {
        return genericRepository.delete(product);
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception, SQLException {
        List list = genericRepository.list(DlbWbProduct.class);
        Iterator<DlbWbProduct> iterator = list.iterator();
        while (iterator.hasNext()) {
            DlbWbProduct next = iterator.next();
            Hibernate.initialize(next.getDlbStatus());
        }
        return list;
    }

    //list all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List listAll(WhereStatement... whereStatements) throws Exception, SQLException {
        return genericRepository.list(DlbWbProduct.class, whereStatements);
    }

    //search method
    @Transactional(rollbackFor = Exception.class)
    public List search(WhereStatement... whereStatements) throws Exception {
        List list = genericRepository.list(DlbWbProduct.class, whereStatements);
        Iterator it =list.iterator();
        
        while (it.hasNext()) {
            DlbWbProduct product = (DlbWbProduct) it.next();
            Hibernate.initialize(product.getDlbStatus());
            
        }
        
        return list;  
    }

    //list for select boxes
    @Transactional(rollbackFor = Exception.class)
    public List loadSelectBox(int status) throws Exception {

        DlbStatus dlbStatus = new DlbStatus(status, null, null);
        WhereStatement whereStatement = new WhereStatement("dlbStatus", dlbStatus, SystemVarList.EQUAL);
        
//        return genericRepository.listWithQuery(DlbWbProduct.class, whereStatement);
        return genericRepository.loadSelectBox(DlbWbProduct.class, null, null, whereStatement);
    }

}
