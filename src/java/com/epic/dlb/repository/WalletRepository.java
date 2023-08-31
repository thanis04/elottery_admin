/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import com.epic.dlb.dto.DlbSwtStWalletDto;
import com.epic.dlb.model.DlbSwtStWallet;
import com.epic.dlb.model.DlbWbWiningFile;
import com.epic.dlb.service.EncryptionService;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nipuna_k
 */
@Repository("walletRepository")
public class WalletRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private CustomRepository customRepository;

    public List findWalletDataByNicOrMobileNo(String key) throws SQLException {

        Session session = sessionFactory.getCurrentSession();
        session.createCriteria(DlbSwtStWallet.class);
        List rstList = new ArrayList();

        Query query = session.createSQLQuery("SELECT \n"
                + "WT.ID AS id, WT.FIRST_NAME AS firstName, \n"
                + "WT.LAST_NAME AS lastName, WT.MOBILE_NO AS mobileNo, \n"
                + "WT.NIC AS nic FROM DLB_SWT_ST_WALLET WT \n"
                + "WHERE WT.MOBILE_NO = '" + key + "' OR WT.NIC = '" + key + "' ;");

        query.setResultTransformer(Transformers.aliasToBean(DlbSwtStWalletDto.class));
        rstList = query.list();

        return rstList;
    }

    public List<Object[]> findByNicOrMobileNo(String nic, String mobileNo) throws SQLException, Exception {

        Session session = sessionFactory.getCurrentSession();
        session.createCriteria(DlbSwtStWallet.class);
        List rstList = new ArrayList();

        String strQuery = "SELECT "
                + "WT.ID AS id, "
                + "WT.FIRST_NAME AS firstName, "
                + "WT.LAST_NAME AS lastName, "
                + "WT.MOBILE_NO AS mobileNo, "
                + "WT.NIC AS nic, "
                + "WT.LAST_LOGIN_TIME AS last_login, "
                + "WT.USERNAME as username, "
                + "WT.CREATE_TIME as create_time "
                + "FROM DLB_SWT_ST_WALLET WT "
                + "WHERE ";
        
        if ((!nic.equals("-") && !mobileNo.equals("-"))) {
            strQuery = strQuery + "WT.MOBILE_NO = '" + mobileNo + "' AND WT.NIC = '" + nic + "' ;";
        } else if ((nic.equals("-"))) {
            strQuery = strQuery + "WT.MOBILE_NO = '" + mobileNo + "' ;";
        } else if ((mobileNo.equals("-"))) {
            strQuery = strQuery + " WT.NIC = '" + nic + "' ;";
        }

        return customRepository.queryExecuter(strQuery);

    }
}
