/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbEmployee;
import com.epic.dlb.model.DlbWbUserRole;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.WhereStatement;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service("userRoleService")
public class UserRoleService {
    @Autowired
    private GenericRepository genericRepository;
    
    //save method
    @Transactional(rollbackFor =Exception.class )
    public Object save(DlbWbUserRole userRole) throws Exception{
        return  genericRepository.save(userRole);
    }
    
    //update method
    @Transactional(rollbackFor =Exception.class )
    public int update(DlbWbUserRole userRole) throws Exception{
        return  (int) genericRepository.update(userRole);
    }
    
    //get method
    @Transactional(rollbackFor =Exception.class )
    public DlbWbUserRole get(String id) throws Exception{
        return  (DlbWbUserRole) genericRepository.get(id, DlbWbUserRole.class);
    }
    
    //list all method
   @Transactional(rollbackFor =Exception.class )
    public List listAll() throws Exception{
        return genericRepository.list(DlbWbUserRole.class);
    }
    
    //lis all by WhereStatement method
   @Transactional(rollbackFor =Exception.class )
    public List listAll(WhereStatement ... whereStatements) throws Exception{
        return genericRepository.list(DlbWbUserRole.class, whereStatements);
    }
    
    //delete method
    @Transactional(rollbackFor =Exception.class )
    public int delete(DlbWbUserRole userRole) throws Exception{
        return   genericRepository.delete(userRole);
    }
    
     //lis all by WhereStatement method
    @Transactional(rollbackFor = Exception.class)
    public List search(List<WhereStatement> whereStatements) throws Exception {
        List list = new ArrayList();
        List tmpList = genericRepository.search(DlbWbUserRole.class, whereStatements);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbUserRole userRole = (DlbWbUserRole) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(userRole.getDlbStatus());
            list.add(userRole);

        }
        return list;
    }
    
}
