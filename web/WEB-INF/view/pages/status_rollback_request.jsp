<%-- 
    Document   : status_rollback_request
    Created on : Apr 8, 2021, 10:50:35 AM
    Author     : nipuna_k
--%>

<%@page import="com.epic.dlb.util.common.SystemVarList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>

        <style>
            .dark-area {
                background-color: #666;
                padding: 40px;
                margin: 0 -40px 20px -40px;
                clear: both;
            }

            .clearfix:before,.clearfix:after {content: " "; display: table;}
            .clearfix:after {clear: both;}
            .clearfix {*zoom: 1;}
        </style>

        <link href="<c:url value="/resources/circle-percentage/css/circle.css"/>" rel="stylesheet">
    </head>

    <body class="nav-md">
        <div class="container body">
            <div class="main_container">
                <!-- include side bar  -->
                <jsp:include page="../common/sidebar.jsp"/>
                <!-- top navigation -->
                <jsp:include page="../common/header.jsp"/>
                <!-- /top navigation -->

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">   
                        <div class="page-title">
                            <div class="title_left">
                                <h4>Request Status Rollback</h4>
                            </div>

                        </div>

                        <br>
                        <br>
                        <br>
                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">                               
                                <!-- table -->
                                <table class="table table-striped table-bordered bulk_action" id="result_table">
                                    <thead>
                                        <tr>
                                            <th>Lottery ID</th>                                                          
                                            <th>Lottery name</th>
                                            <th>Draw number</th>                                               
                                            <th>Draw date</th>
                                            <th>Customer name</th>
                                            <th>Customer NIC</th>
                                            <th>Winning prize</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="row" items="${list}">
                                            <tr>
                                                <td>${row.lotteryId}</td>
                                                <td>${row.lotteryName}</td>
                                                <td>${row.drawNo}</td>
                                                <td>${row.drawDate}</td>
                                                <td>${row.name}</td>
                                                <td>${row.nic}</td>
                                                <td>${row.winningPrize}</td>
                                                <td align="center">
                                                    <c:choose>
                                                        <c:when test="${row.status eq '-'}"> 
                                                            <span onclick="approve('${row.id}')" class="fa fa-undo btn btn-danger" title='View' ></span>
                                                        </c:when>
                                                        <c:when test="${row.status eq 'Pending'}"> 
                                                            <span class="badge badge-warning">${row.status}</span>
                                                        </c:when>
                                                    </c:choose>
                                                </td>

                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- footer content -->
                <jsp:include page="../common/footer.jsp"/>
                <!-- /footer content -->
            </div>
        </div>
    </body>

    <script>

        $(document).ready(function () {
            $('#progessDisplay').hide();


        });

        function approve(id) {
            showConfirmMsg('Confirmation', 'Are you sure to request a rollback ?', ok);
            function ok() {
                var formData = {
                    "purchaseHistoryId": id
                }

                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "request_rollback.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);
                        showNotification(response.status, response.msg);
                        location.reload();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }
        }
    </script>
</html>

