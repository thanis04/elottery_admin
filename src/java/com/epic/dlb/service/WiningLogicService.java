/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbSwtStWinningLogic;
import com.epic.dlb.repository.GenericRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kasun_n
 */
@Service
public class WiningLogicService {

    @Autowired
    private GenericRepository genericRepository;

    @Transactional
    public List<DlbSwtStWinningLogic> listAll() {
        List<DlbSwtStWinningLogic> dlbSwtStWinningLogics = null;

        dlbSwtStWinningLogics = genericRepository.list(DlbSwtStWinningLogic.class);

        return dlbSwtStWinningLogics;
    }

    @Transactional
    public DlbSwtStWinningLogic getById(int id) {
        return (DlbSwtStWinningLogic) genericRepository.get(id, DlbSwtStWinningLogic.class);
    }

}
