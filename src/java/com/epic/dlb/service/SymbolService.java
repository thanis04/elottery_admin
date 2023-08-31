/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbProductItem;
import com.epic.dlb.model.DlbWbSymbol;
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
 * @author Kasun
 */
@Service("symbolService")
public class SymbolService {
    
    @Autowired
    private GenericRepository genericRepository;
    
     //list symbol by symbol type(ProductItem)
   @Transactional(rollbackFor =Exception.class )
    public List listByType(DlbWbProductItem productItem) throws Exception,SQLException{
        WhereStatement whereStatement=new WhereStatement("dlbWbProductItem", productItem, SystemVarList.EQUAL);
        return genericRepository.list(DlbWbSymbol.class,whereStatement);
    }
    
}
