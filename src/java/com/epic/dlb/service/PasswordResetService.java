/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.service;

import com.epic.dlb.model.DlbWbPasswordResetKey;
import com.epic.dlb.model.DlbWbEmailConfiguration;
import com.epic.dlb.repository.CustomRepository;
import com.epic.dlb.repository.GenericRepository;
import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nipuna_k
 */
@Service("passwordResetService")
public class PasswordResetService {

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private CustomRepository customRepository;

    @Transactional(rollbackFor = Exception.class)
    public String generateUrl(String username, String status) {
        String url = "";
        DlbWbEmailConfiguration configuration = (DlbWbEmailConfiguration) genericRepository.get(2, DlbWbEmailConfiguration.class);
        url = configuration.getUrl();
        url = url.replace("{key}", generateKey(username, status));
        return url;
    }

    @Transactional(rollbackFor = Exception.class)
    public String generateKey(String username, String status) {
        String key = "";
        try {
            updateCurrentKeyOfUsername(username);
            Boolean isValid = false;
            while (isValid == false) {
                key = generateRandomKey();
                isValid = isExisting(key);
            }
            createNewKey(username, key, status);
        } catch (Exception ex) {
            Logger.getLogger(PasswordResetService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return key;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean isExisting(String key) throws Exception {

        String query = "SELECT * FROM `DLB_WB_RESET_KEY` WHERE "
                + "(`STATUS_OF_KEY` = 'ACTIVE' OR `STATUS_OF_KEY` = 'STATIC') AND "
                + "`KEY` = '" + key + "' ";
        List<Object[]> result = customRepository.queryExecuter(query);

        return result.isEmpty();
    }
    
    @Transactional(rollbackFor = Exception.class)
    public String getUsernameByKey(String key) throws Exception {

        String query = "SELECT USERNAME FROM `DLB_WB_RESET_KEY` WHERE "
                + "(`STATUS_OF_KEY` = 'ACTIVE' OR `STATUS_OF_KEY` = 'STATIC') AND "
                + "`KEY` = '" + key + "' ";
        List<Object[]> result = customRepository.queryExecuter(query);
        return result.toString().replace("[", "").replace("]", "");
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCurrentKeyOfUsername(String username) throws Exception {

        String query = "UPDATE `DLB_WB_RESET_KEY` SET "
                + "`STATUS_OF_KEY` = 'INACTIVE' WHERE `USERNAME` = '" + username + "' ";
        customRepository.UpdateQueryExecuter(query);

    }

    @Transactional(rollbackFor = Exception.class)
    public void createNewKey(String username, String key, String status) throws Exception {
        String query = "INSERT INTO `DLB_WB_RESET_KEY` "
                + "(`ID`, `USERNAME`, `KEY`, `STATUS_OF_KEY`, `CREATED_TIME`) "
                + "VALUES (0, '" + username + "', '" + key + "', '"+status+"', NOW())";
        customRepository.UpdateQueryExecuter(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public void executeExpiredKeys() throws Exception {
        String query = "UPDATE `DLB_WB_RESET_KEY` SET `STATUS_OF_KEY` = 'INACTIVE' "
                + "WHERE CREATED_TIME < DATE_SUB(NOW(), INTERVAL 24 HOUR) AND `STATUS_OF_KEY` = 'ACTIVE' ";
        customRepository.UpdateQueryExecuter(query);
    }

    @Transactional(rollbackFor = Exception.class)
    public String generateRandomKey() {
        String key = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * key.length());
            salt.append(key.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
