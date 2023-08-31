/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbGame;
import com.epic.dlb.model.DlbWbGameProfile;
import com.epic.dlb.model.DlbWbGameResult;
import com.epic.dlb.model.DlbWbProduct;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.sql.SQLException;
import java.util.ArrayList;
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

@Service("gameProfileService")
public class GameProfileService {
    
     @Autowired
    private GenericRepository genericRepository;

    //save method
    @Transactional(rollbackFor = Exception.class)
    public Object save(DlbWbGameProfile dlbWbGameProfile) throws Exception {
        return genericRepository.save(dlbWbGameProfile);
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbWbGameProfile dlbWbGameProfile) throws Exception {
        return (int) genericRepository.update(dlbWbGameProfile);
    }

    //get method
    @Transactional(rollbackFor = Exception.class)
    public DlbWbGameProfile get(int id) throws Exception {
        return (DlbWbGameProfile) genericRepository.get(id, DlbWbGameProfile.class);
    }

    //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAllActive() throws Exception {
        List list = new ArrayList();
        
        WhereStatement statement=new WhereStatement("dlbStatus.statusCode", SystemVarList.ACTIVE, SystemVarList.EQUAL);
        List tmpList = genericRepository.list(DlbWbGameProfile.class,statement);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbGameProfile dlbWbGameProfile = (DlbWbGameProfile) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(dlbWbGameProfile.getDlbStatus());
            list.add(dlbWbGameProfile);

        }
        return list;
    }
    
    @Transactional
    public List<DlbWbGame> listGamesByProfile(Integer gameProfileID){
        
         WhereStatement statement1=new WhereStatement("dlbWbGameProfile.id", gameProfileID, SystemVarList.EQUAL);
         WhereStatement statement2=new WhereStatement("dlbStatus.statusCode", SystemVarList.ACTIVE, SystemVarList.EQUAL);
         
        return genericRepository.list(DlbWbGame.class,statement1,statement2);
         
    }
    
    
     //list all method
    @Transactional(rollbackFor = Exception.class)
    public List listAll() throws Exception {
        List list = new ArrayList();

        List tmpList = genericRepository.list(DlbWbGameProfile.class);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbGameProfile dlbWbGameProfile = (DlbWbGameProfile) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(dlbWbGameProfile.getDlbStatus());
            list.add(dlbWbGameProfile);

        }
        return list;
    }
    
     //list all method
    @Transactional(rollbackFor = Exception.class)
    public List serach(String name) throws Exception {
        List list = new ArrayList();
        
           WhereStatement statement1=new WhereStatement("name", name, SystemVarList.LIKE);

        List tmpList = genericRepository.list(DlbWbGameProfile.class,statement1);

        for (int i = 0; i < tmpList.size(); i++) {
            DlbWbGameProfile dlbWbGameProfile = (DlbWbGameProfile) tmpList.get(i);
            //init proxy objects
            Hibernate.initialize(dlbWbGameProfile.getDlbStatus());
            list.add(dlbWbGameProfile); 

        }
        return list;
    }
    
     @Transactional(rollbackFor = Exception.class)
    public void saveGameResult(List<DlbWbGameResult> dlbWbGameResults) throws Exception{
    
         Iterator<DlbWbGameResult> iterator = dlbWbGameResults.iterator();
         
         while (iterator.hasNext()) {
            DlbWbGameResult next = iterator.next();
            
             DlbWbGame  dlbWbGame = (DlbWbGame) genericRepository.get(next.getDlbWbGame().getId(), DlbWbGame.class);
             next.setDlbWbGame(dlbWbGame);
            
            genericRepository.save(next);
            
        }
    }
    
     @Transactional(rollbackFor = Exception.class)
     public List<DlbWbGameResult> getResultByResultId(Integer rstId){
         WhereStatement statement=new WhereStatement("dlbWbResult.id", rstId, SystemVarList.EQUAL);
         
         List list= genericRepository.list(DlbWbGameResult.class, statement);
         
         Iterator iterator = list.iterator();
         
         while (iterator.hasNext()) {
             DlbWbGameResult next = (DlbWbGameResult) iterator.next();
             
             Hibernate.initialize(next.getDlbWbGame());
             Hibernate.initialize(next.getDlbWbResult());
             
         }
         
         
         return list;
     }
    
    


    //get method
    @Transactional(rollbackFor = Exception.class)
    public int delete(DlbWbGameProfile dlbWbGameProfile) throws Exception, SQLException {
        return genericRepository.delete(dlbWbGameProfile);
    }
    
    
}
