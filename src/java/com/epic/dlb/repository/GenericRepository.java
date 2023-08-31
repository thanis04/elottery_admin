/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.repository;

import com.epic.dlb.util.common.SystemVarList;
import com.epic.dlb.util.common.WhereStatement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author kasun_n
 */
@Repository("genericRepository")
public class GenericRepository {

    /*-----------------------------
    Dependancy Injection 
    -----------------------------*/
    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    //save method
    public Serializable save(Object object) throws Exception {
        Serializable res = 0l;//define method response varible

        //get current session using session factory       
        Session session = sessionFactory.getCurrentSession();

        //save object using session
        res = (Serializable) session.save(object);

        //return response
        return res;

    }

    //update method
    public Object update(Object object) throws Exception {
        int res = 0;//define method response varible
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();

        //update object using session
        session.update(object);

        res = 1;
        //return response
        return res;

    }

    //get method
    public Object get(int id, Class cls) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();

        //get obejct using session
        Object object = session.get(cls, id);

        //return object
        return object;
    }

    //get method
    public Object get(Serializable id, Class cls) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();

        //get obejct using session
        Object object = session.get(cls, id);

        //return object
        return object;
    }

    //get method by where
    public Object get(Class cls, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //Add where clause
        for (WhereStatement where : wheres) {
            criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
        }

        //return list
        List list = criteria.list();
        if (list.size() > 0) {
            return list.get(0);
        }

        return null;
    }

    //delete method
    public int delete(Object object) throws Exception {
        int res = 0;//define method response varible
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();

        //delete object using session
        session.delete(object);
        res = 1;

        //return response
        return res;

    }

    public List list(Class cls, String orderProperty, String orderType) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //set order by
        if (orderType.equals(SystemVarList.ASC)) {
            criteria.addOrder(Order.asc(orderProperty));
        } else {
            criteria.addOrder(Order.desc(orderProperty));
        }

        //return list
        return criteria.list();

    }

    public List list(Class cls) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //return list
        return criteria.list();

    }

    //delete Header detals item lines
    public int deleteRecords(Class c, WhereStatement... whereStatements) {
        Session s = sessionFactory.getCurrentSession();
        String whereString = "";
        String hql = "DELETE FROM " + c.getSimpleName();
        //build where statment
        for (int i = 0; i < whereStatements.length; i++) {
            WhereStatement whereStatement = whereStatements[i];
            if (i == 0) {
                whereString = whereString + " WHERE " + whereStatement.getProperty() + "='" + whereStatement.getValue() + "'";
            } else {
                whereString = whereString + " AND " + whereStatement.getProperty() + "='" + whereStatement.getValue() + "'";
            }

        }
        Query query = s.createQuery(hql + whereString);
        return query.executeUpdate();
    }

    //list by multiple wheres using AND
    public List list(Class cls, String orderProperty, String orderType, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //Add where clause
        for (WhereStatement where : wheres) {
            //set operator
            switch (where.getOperator()) {
                case "=":
                    criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
                    break;

                case "like":
                    criteria.add(Restrictions.like(where.getProperty(), where.getValue()));
                    break;

                case "<>":
                    criteria.add(Restrictions.ne(where.getProperty(), where.getValue()));
                    break;

                case "IS NULL":
                    criteria.add(Restrictions.isNull(where.getProperty()));
                    break;

                case "IS NOT NULL":
                    criteria.add(Restrictions.isNotNull(where.getProperty()));
                    break;

                case "less_than":
                    criteria.add(Restrictions.lt(where.getProperty(), where.getValue()));
                    break;

                case "greater_than":
                    criteria.add(Restrictions.gt(where.getProperty(), where.getValue()));
                    break;

            }
        }

        //set order by
        if (orderType.equals(SystemVarList.ASC)) {
            criteria.addOrder(Order.asc(orderProperty));
        } else {
            criteria.addOrder(Order.desc(orderProperty));
        }

        //return list
        return criteria.list();

    }

    //list by multiple wheres using AND
    public List list(Class cls, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //Add where clause
        for (WhereStatement where : wheres) {
            //set operator
            switch (where.getOperator()) {
                case "=":
                    criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
                    break;

                case "like":
                    criteria.add(Restrictions.like(where.getProperty(), where.getValue()));
                    break;

                case "<>":
                    criteria.add(Restrictions.ne(where.getProperty(), where.getValue()));
                    break;

                case "IS NULL":
                    criteria.add(Restrictions.isNull(where.getProperty()));
                    break;

                case "IS NOT NULL":
                    criteria.add(Restrictions.isNotNull(where.getProperty()));
                    break;

                case "less_than":
                    criteria.add(Restrictions.lt(where.getProperty(), where.getValue()));
                    break;

                case "greater_than":
                    criteria.add(Restrictions.gt(where.getProperty(), where.getValue()));
                    break;

            }
        }

        //return list
        return criteria.list();

    }

    //list with or function
    public List listOR(Class cls, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //Add where clause
        for (WhereStatement where : wheres) {
            //set operator
            switch (where.getOperator()) {
                case "=":
                    criteria.add(Restrictions.or(Restrictions.eq(where.getProperty(), where.getValue())));
                    break;
                case "like":
                    criteria.add(Restrictions.or(Restrictions.eq(where.getProperty(), where.getValue())));
                    break;
                case "<>":
                    criteria.add(Restrictions.or(Restrictions.eq(where.getProperty(), where.getValue())));
                    break;
                case "IS NULL":
                    criteria.add(Restrictions.or(Restrictions.eq(where.getProperty(), where.getValue())));
                    break;
                case "IS NOT NULL":
                    criteria.add(Restrictions.or(Restrictions.eq(where.getProperty(), where.getValue())));
                    break;

            }
        }

        //return list
        return criteria.list();

    }

    public List listWithGrouping(Class cls, String groupProperty, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //Add where clause
        for (WhereStatement where : wheres) {
            //set operator
            switch (where.getOperator()) {
                case "=":
                    criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
                    break;
                case "like":
                    criteria.add(Restrictions.like(where.getProperty(), where.getValue()));
                    break;
                case "<>":
                    criteria.add(Restrictions.ne(where.getProperty(), where.getValue()));
                    break;
                case "IS NULL":
                    criteria.add(Restrictions.isNull(where.getProperty()));
                    break;
                case "IS NOT NULL":
                    criteria.add(Restrictions.isNotNull(where.getProperty()));
                    break;
                case "less_than":
                    criteria.add(Restrictions.lt(where.getProperty(), where.getValue()));
                    break;

                case "greater_than":
                    criteria.add(Restrictions.gt(where.getProperty(), where.getValue()));
                    break;

            }
        }

        //add group property       
        criteria.setProjection(Projections.groupProperty(groupProperty));

        //return list
        List list = criteria.list();
        return list;

    }

    //list by multiple wheres using AND
    public List search(Class cls, List<WhereStatement> wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //Add where clause
        for (WhereStatement where : wheres) {
            //set operator
            switch (where.getOperator()) {
                case "=":
                    criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
                    break;

                case "like":
                    criteria.add(Restrictions.like(where.getProperty(), where.getValue()));
                    break;

                case "<>":
                    criteria.add(Restrictions.ne(where.getProperty(), where.getValue()));
                    break;

                case "IS NULL":
                    criteria.add(Restrictions.isNull(where.getProperty()));
                    break;

                case "IS NOT NULL":
                    criteria.add(Restrictions.isNotNull(where.getProperty()));
                    break;

                case "less_than":
                    criteria.add(Restrictions.lt(where.getProperty(), where.getValue()));
                    break;

                case "greater_than":
                    criteria.add(Restrictions.gt(where.getProperty(), where.getValue()));
                    break;

            }
        }

        //return list
        return criteria.list();

    }

    //list by multiple wheres using AND
    public List search(Class cls, List<WhereStatement> wheres, String orderProperty, String orderType) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //Add where clause
        for (WhereStatement where : wheres) {
            //set operator
            switch (where.getOperator()) {
                case "=":
                    criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
                    break;
                case "like":
                    criteria.add(Restrictions.like(where.getProperty(), where.getValue()));
                    break;
                case "<>":
                    criteria.add(Restrictions.ne(where.getProperty(), where.getValue()));
                    break;
                case "IS NULL":
                    criteria.add(Restrictions.isNull(where.getProperty()));
                    break;
                case "IS NOT NULL":
                    criteria.add(Restrictions.isNotNull(where.getProperty()));
                    break;
                case "less_than":
                    criteria.add(Restrictions.lt(where.getProperty(), where.getValue()));
                    break;

                case "greater_than":
                    criteria.add(Restrictions.gt(where.getProperty(), where.getValue()));
                    break;

            }
        }
        //set order by
        if (orderType.equals(SystemVarList.ASC)) {
            criteria.addOrder(Order.asc(orderProperty));
        } else {
            criteria.addOrder(Order.desc(orderProperty));
        }

        //return list
        return criteria.list();

    }

//    //list for select box
//    public List<SelectBox> loadSelectBox(Class cls, String codeProperty, String textProperty, String status) {
//        //get current session using session factory
//        List list;
//        Session session = sessionFactory.getCurrentSession();
//      
//        Criteria criteria = session.createCriteria(cls); 
//         if (!status.equals(SystemVarList.ALL)) {//check is select all records
//            //check only status match records
//            criteria.add(Restrictions.eq("dlbStatus.statusCode", status));
//         }
//        list=criteria.list();
//        return list;
//
//    }
    //list for select box
    public List loadSelectBox(Class cls, String codeProperty, String textProperty, WhereStatement... wheres) {
        //get current session using session factory
        List list;
        Session session = sessionFactory.getCurrentSession();

        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //Add where clause
        for (WhereStatement where : wheres) {
            criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
        }
        list = criteria.list();
        return list;

    }

    //list by multiple wheres using AND
    public List listWithQuery(Class cls, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create query
        String queryStr = "FROM " + cls.getName() + " ";
        String whereString = "";
        String whereNextOperator = "";

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

        }

        //create criteria
        queryStr = queryStr + whereString;
        Query query = session.createQuery(queryStr);

        //return list
        return query.list();
    }

    //list by multiple wheres using AND
    public List listWithQuery(Class cls, List<WhereStatement> wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create query
        String queryStr = "FROM " + cls.getName() + " ";
        String whereString = "";
        String whereNextOperator = "";

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

        }

        //create criteria
        queryStr = queryStr + whereString;
        System.out.println(queryStr);
        Query query = session.createQuery(queryStr);
        //return list
        return query.list();
    }

    //list by multiple wheres using AND
    public List listWithQuery(Class cls, List<WhereStatement> wheres, String orderProperty, String orderType) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create query
        String queryStr = "FROM " + cls.getName() + " ";
        String whereString = "";
        String whereNextOperator = "";

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

        }

        //create criteria
        queryStr = queryStr + whereString + " order by " + orderProperty + " " + orderType;
        Query query = session.createQuery(queryStr);
        //return list
        return query.list();
    }

    //list by multiple wheres using AND
    public List listWithQueryNGroup(Class cls, String groupProperty, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create query
        String queryStr = "FROM " + cls.getName() + " ";
        String whereString = "";
        String whereNextOperator = "";

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

        }

        //create criteria
        queryStr = queryStr + whereString + " GROUP BY " + groupProperty;
        Query query = session.createQuery(queryStr);

        //return list
        return query.list();
    }

    //list by multiple wheres using AND with grouping and projectionList
    public List listWithQueryNGroup(Class cls, List<String> projectionList, String groupProperty, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create query
        String projections = "select ";
        String queryStr = "FROM " + cls.getName() + " ";
        String whereString = "";
        String whereNextOperator = "";

        //set projection list
        for (int i = 0; i < projectionList.size(); i++) {
            String select = projectionList.get(i);
            projections = projections + select + "";
        }

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

        }

        //create criteria
        queryStr = projections + " " + queryStr + whereString + " GROUP BY " + groupProperty;
        Query query = session.createQuery(queryStr);

        //return list
        List list = query.list();
        return list;
    }

    public int getCount(Class cls, List<WhereStatement> wheres) {
        Long count = 0l;
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();

        String queryStr = "select count(*) FROM " + cls.getSimpleName();
        String whereString = "";

        String whereNextOperator = "";

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {

            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

            index++;

        }

        //create criteria
        Query query = session.createQuery(queryStr + whereString);
        List list = query.list();
        if (list.size() > 0) {
            count = (Long) list.get(0);
        }
        return count.intValue();

    }

    public List listWithQueryPaginated(Class cls, List<WhereStatement> wheres, Integer start, Integer length) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create query
        String queryStr = "FROM " + cls.getName() + " ";
        String whereString = "";
        String whereNextOperator = "";

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

        }

        //create criteria
        queryStr = queryStr + whereString;
        System.out.println("------------------------------------------- queryStr " + queryStr);
        Query query = session.createQuery(queryStr);
        query.setMaxResults(length);
        query.setFirstResult(start);
        //return list
        return query.list();
    }

    public List listWithQueryPaginated(Class cls, List<WhereStatement> wheres, Integer start, Integer length, String orderProperty, String orderType) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create query
        String queryStr = "FROM " + cls.getName() + " ";
        String whereString = "";
        String whereNextOperator = "";

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

        }

        //create criteria
        queryStr = queryStr + whereString + " ORDER BY " + orderProperty + " " + orderType;
        Query query = session.createQuery(queryStr);
        query.setMaxResults(length);
        query.setFirstResult(start);
        //return list
        return query.list();
    }

    public Long CountWithQuery(Class cls, List<WhereStatement> wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create query
        String queryStr = "select count(*) FROM " + cls.getName() + " ";
        String whereString = "";
        String whereNextOperator = "";

        //Add where clause
        int index = 1;
        for (WhereStatement where : wheres) {
            if (index == 1) {
                //for first
                whereString += " WHERE ";
            }

            if (where.getNextOperator() != null) {
                whereNextOperator = where.getNextOperator();
            }

            switch (where.getOperator()) {
                case "=":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "like":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "<>":
                    whereString += where.getProperty() + " " + where.getOperator() + "'" + where.getValue() + "' " + whereNextOperator;
                    break;
                case "IS NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "IS NOT NULL":
                    whereString += where.getProperty() + " " + where.getOperator() + "  " + whereNextOperator;
                    break;
                case "less_than":
                    whereString += where.getProperty() + " < '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than":
                    whereString += where.getProperty() + " > '" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "less_than_or_equal":
                    whereString += where.getProperty() + " <='" + where.getValue() + "' " + whereNextOperator;
                    break;

                case "greater_than_or_equal":
                    whereString += where.getProperty() + " >='" + where.getValue() + "' " + whereNextOperator;
                    break;

            }

            index++;

            whereNextOperator = "";

        }

        //create criteria
        queryStr = queryStr + whereString;
        //System.out.println("------------------------------------------- queryStr " + queryStr);
        Long totalRecords = (long) session.createQuery(queryStr).uniqueResult();

        return totalRecords;
    }

    public List list(Class cls, String orderProperty, String orderType, int limit, WhereStatement... wheres) {
        //get current session using session factory
        Session session = sessionFactory.getCurrentSession();
        //create criteria
        Criteria criteria = session.createCriteria(cls);

        //set order by
        if (orderType.equals(SystemVarList.ASC)) {
            criteria.addOrder(Order.asc(orderProperty));
        } else {
            criteria.addOrder(Order.desc(orderProperty));
        }

        criteria.setMaxResults(limit);

        //Add where clause
        for (WhereStatement where : wheres) {
            //set operator
            switch (where.getOperator()) {
                case "=":
                    criteria.add(Restrictions.eq(where.getProperty(), where.getValue()));
                    break;

                case "like":
                    criteria.add(Restrictions.like(where.getProperty(), where.getValue()));
                    break;

                case "<>":
                    criteria.add(Restrictions.ne(where.getProperty(), where.getValue()));
                    break;

                case "IS NULL":
                    criteria.add(Restrictions.isNull(where.getProperty()));
                    break;

                case "IS NOT NULL":
                    criteria.add(Restrictions.isNotNull(where.getProperty()));
                    break;

                case "less_than":
                    criteria.add(Restrictions.lt(where.getProperty(), where.getValue()));
                    break;

                case "greater_than":
                    criteria.add(Restrictions.gt(where.getProperty(), where.getValue()));
                    break;

            }
        }

        //return list
        return criteria.list();

    }

    public List randomList(Class cls, String queryStr) {
        queryStr = "FROM " + cls.getName() + " " + queryStr;
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryStr);
        //return list
        return query.list();
    }

    public Long randomListCount(Class cls, String queryStr) {
        queryStr = "select count(*) FROM " + cls.getName() + " " + queryStr;
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryStr);

        Long totalRecords = (long) session.createQuery(queryStr).uniqueResult();

        return totalRecords;
    }

    public List randomListWithLimit(Class cls, String queryStr, int start, int length) {
        queryStr = "FROM " + cls.getName() + " " + queryStr;
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(queryStr);
        query.setMaxResults(length);
        query.setFirstResult(start);
        //return list
        return query.list();
    }
}
