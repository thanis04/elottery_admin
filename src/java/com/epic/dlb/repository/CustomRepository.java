/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author yasitha_b
 */

@Repository
public class CustomRepository 
{
   
    @Autowired
    private SessionFactory sessionFactory;
    
    public List<Object[]> queryExecuter(String strQuery)throws Exception
    {
        Session session = sessionFactory.getCurrentSession();            
        SQLQuery query = session.createSQLQuery(strQuery);
        //List<Object[]> result = query.list();
        List<Object[]> result  = query.list();
        
        return result;
    }
    
    public void UpdateQueryExecuter(String strQuery)throws Exception
    {
        Session session = sessionFactory.getCurrentSession();            
        SQLQuery query = session.createSQLQuery(strQuery);
        query.executeUpdate();
    }
}
