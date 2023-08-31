<%-- 
    Document   : result_approval
    Created on : Oct 17, 2017, 10:54:46 AM
    Author     : methsiri_h
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
                                <h4>Ticket File List</h4>
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
                                            <th>#</th>
                                            <th>Lottery</th>                                                          
                                            <th>Draw Date</th>
                                            <th>Draw No</th>                                               
                                            <th>No of Tickets</th>                                               
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="row" items="${pending_list}">
                                            <tr>
                                                <td>${row.id}</td>
                                                <td>${row.productDescription}</td>
                                                <td>${row.date}</td>
                                                <td>${row.drawNo}</td>
                                                <td>${row.noOfTicket}</td>
                                                <td><span class="badge bg-blue-sky">${row.dlbStatus.description}</span></td>
                                                <td>
                                                    <c:if test="${row.dlbStatus.statusCode ne '37'}">
                                                        <a onclick="location.reload()" href="generate_sales_file.htm?id=${row.id}"><button  class="btn btn-warning"><i class="fa fa-file"></i> Generate Sales File</button></a>

                                                    </c:if>
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



        function viewRecord(id) {
            $('#viewModal').modal().show();

            var dataString = "id=" + id;
            $('#view_id').val(id);
            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#viewModal').modal().show();

                        //show item details to form
                        var record = response.record;

                        $('#view_product').html(record.lottery);
                        $('#view_draw_no').html(record.draw_no);
                        $('#view_draw_date').html(record.draw_date);
                        $('#view_no_of_ticket').html(record.no_of_tk);
                        $('#view_statusCode').html(record.status);

                        if (record.status === 'Processing') {
                            $('#btn_approve').hide();
                            $('#progessDisplay').show();
                            showProgress(record.id);
                        }

                        if (record.filename_check) {
                            $('#view_filname_check').addClass('fa-check');
                            $('#view_filname_check').addClass('text-success');
                            $('#view_filname_check').removeClass('fa-remove');
                            $('#view_filname_check').removeClass('text-danger');
                        } else {
                            $('#view_filname_check').addClass('fa-remove')
                            $('#view_filname_check').addClass('text-danger')
                            $('#view_filname_check').removeClass('fa-check');
                            $('#view_filname_check').removeClass('text-success');
                        }

                        if (record.mac_check) {
                            $('#mac_address_check').addClass('fa-check');
                            $('#mac_address_check').addClass('text-success');
                            $('#mac_address_check').removeClass('fa-remove');
                            $('#mac_address_check').removeClass('text-danger');
                        } else {
                            $('#mac_address_check').addClass('fa-remove')
                            $('#mac_address_check').addClass('text-danger')
                            $('#mac_address_check').removeClass('fa-check');
                            $('#mac_address_check').removeClass('text-success');
                        }

                        if (record.hash_check) {
                            $('#hash_code_check').addClass('text-success');
                            $('#hash_code_check').addClass('fa-check');
                            $('#hash_code_check').removeClass('fa-remove');
                            $('#hash_code_check').removeClass('text-danger');
                        } else {
                            $('#hash_code_check').addClass('text-danger');
                            $('#hash_code_check').addClass('fa-remove');
                            $('#hash_code_check').removeClass('fa-check');
                            $('#hash_code_check').removeClass('text-success');
                        }


                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function genarateRecord(id) {

            showConfirmMsg('Approve', 'Are you sure to generate sales file ?', fnGenarateRecord);

            function fnGenarateRecord() {

                var dataString = "id=" + id;

                $.ajax({
                    data: dataString,
                    type: 'POST',
                    url: "generate_sales_file.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);
                        $('#btn_approve').prop('disabled', true);
                        $('#progessDisplay').show();
                        // showNotification(response.status,response.msg);
                        showProgress(response.id);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });



            }

        }



        function refreshPage() {
            location.reload();
        }




    </script>
</html>

