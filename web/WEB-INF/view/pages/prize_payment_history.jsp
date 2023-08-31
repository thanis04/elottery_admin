<%-- 
    Document   : audit_trace
    Created on : Oct 19, 2017, 11:22:20 AM
    Author     : salinda_r
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>
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
                            <div class="title_left" >
                                <h4>Prize Payment File History</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank"   class="form-horizontal" id="search_form" role="form">
                                            <div class="form-group">
                                                <label class="">From Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>


                                            <div class="form-group" align="right">
                                                <button type="button" class="btn submit" onclick="search();">Search</button>
                                            </div>

                                            <br>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div>
                            <div class="x_title" align="center">
                                <h4> Files</h4>                                        
                                <div class="clearfix"></div>
                            </div>

                            <!-- table -->
                            <table id="search_result" class="table table-striped table-bordered bulk_action" >
                                <thead>
                                <td>ID</td>
                                <td>Date</td>
                                <td>Count</td>
                                <td>Action</td>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${list}">
                                        <tr>

                                            <td>${item.id}</td>
                                            <td>${item.createdDate}</td>
                                            <td>${item.count}</td>
                                            <td><button onclick="generateFile('${item.id}')" class="btn btn-success">Download File</button></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>


                        </div>

                        <form id="view" name="view" method="GET" target="_blank">
                            <input type="hidden" id="id" name="id">
                        </form>
                    </div>
                </div>


            </div>


    </body>

    <script>

        var resultTable = "";

        $(document).ready(function () {


            resultTable = $('#search_result').dataTable({
                "searching": false
            });

            //set date selecter         
            $(function () {
                $("#from_date").datepicker({dateFormat: 'yy-mm-dd'});
                $("#to_date").datepicker({dateFormat: 'yy-mm-dd'});
            });
        });





        function generateFile(id) {

            $('#view #id').val(id);
            document.view.action = "${pageContext.servletContext.contextPath}/prize_payment_history/download_prize_file.htm";
            document.view.submit();
        }

        //search function
        function search() {
            var formData = $('#search_form').serialize();

            if (validateForm($('#search_form'))) {
                var fromDate = $('#from_date').val();
                var toDate = $('#to_date').val();

                if (checkFromDateNToDate(fromDate, toDate)) {
                    $.ajax({
                        data: formData,
                        url: "search.htm",
                        type: 'GET',
                        success: function (data, textStatus, jqXHR) {
                            //clear table
                            var response = JSON.parse(data);
                            if (response.status === true) {

                                resultTable.fnClearTable();

                                //set data table data
                                var tabledata = response.search_result;
                                $(tabledata).each(function (index) {
                                    var item = tabledata[index];

                                    var action = "<button onclick='generateFile(" + item.id + ")' class='btn btn-success'>Download File</button>";
                                    var row = [item.id, item.createdDate, item.count, action];
                                    resultTable.fnAddData(row);
                                });
                            } else {
                                //show msg
                                showNotification('error', response.msg);
                            }


                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }

                    });
                } else {
                    showNotification('error', 'Invalid date combination');
                }


            }


        }





    </script>

</html>
