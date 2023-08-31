<%-- 
    Document   : product_search2
    Created on : Feb 18, 2019, 12:17:06 PM
    Author     : kasun_n
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<div class="x_title" align="center">
    <h4>Search Result</h4>                                        
    <div class="clearfix"></div>
</div>
<!-- table -->
<table class="table table-striped table-bordered bulk_action" id="result_table">
    <thead>
        <tr>
            <th>Product Code</th>
            <th>Description</th>                                                          
            <th>Status</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${search}" var="row">         
                            
            <tr>
                <th>${row.productCode}</th>
                <th>${row.description}</th>                                                          
                <th>${row.dlbStatus.description}</th>
                <th>
                    <span onclick='viewItem("${row.productCode}")' class='fa fa-eye btn btn-primary' title='View Lottery'></span>
                    <span onclick='editItem("${row.productCode}")' class='fa fa-pencil btn btn-success' title='Edit Lottery'></span>
                    <span onclick='deleteItem("${row.productCode}")' class='fa fa-trash-o btn btn-danger' title='Delete Lottery'></span>
                </th>
            </tr>
        </c:forEach>

    </tbody>
</table>
