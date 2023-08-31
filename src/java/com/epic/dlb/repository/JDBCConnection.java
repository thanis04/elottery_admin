/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kasun_n
 */
@Repository("jDBCConnection")
public class JDBCConnection {

    @Autowired 
    private SessionFactory sessionFactory;

    public Connection getConnection() throws SQLException {
        SessionFactoryImpl factoryImpl = (SessionFactoryImpl) sessionFactory;
        ConnectionProvider connectionProvider = factoryImpl.getConnectionProvider();
        Connection connection = connectionProvider.getConnection();
        return connection;
    }

}
