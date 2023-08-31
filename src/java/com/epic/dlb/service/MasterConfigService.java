/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbSwtMtConfiguration;
import com.epic.dlb.repository.GenericRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author methsiri_h
 */
@Service("masterConfigService")
public class MasterConfigService {

    @Autowired
    private GenericRepository genericRepository;

    //loading the text boxes data
    @Transactional(rollbackFor = Exception.class)
    public List get() throws Exception {
        //using genericRepository list method to get records
        return genericRepository.list(DlbSwtMtConfiguration.class);
    }

    //get id  
    @Transactional(rollbackFor = Exception.class)
    public DlbSwtMtConfiguration get(int id) throws Exception {
        return (DlbSwtMtConfiguration) genericRepository.get(id, DlbSwtMtConfiguration.class);
    }

    //update method
    @Transactional(rollbackFor = Exception.class)
    public int update(DlbSwtMtConfiguration masterConfiguration) throws Exception {
        return (int) genericRepository.update(masterConfiguration);
    }
}
