<%-- 
    Document   : daily_ticket_sales_report
    Created on : Feb 27, 2020, 1:46:36 PM
    Author     : nipuna_k
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
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left" >
                                <h4>Daily Ticket Sales Report</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="show_report_bydateAndCategory.htm"  class="form-horizontal" id="search_form" role="form">
                                            <div class="form-group">
                                                <label>Lottery Type</label>
                                                <div>
                                                    <select  type="text" class="form-control" name="lottery" id="lottery"  >
                                                        <option selected value="0">--Select--</option>
                                                        <c:forEach var="item" items="${product_select_box}">
                                                            <option value="${item.productCode}">${item.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="">From Date (Purchase Date)<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date (Purchase Date) <span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>

                                            <div class="form-group" align="right">
                                                <button type="reset" class="btn btn-secondary" >Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">View</button>
                                                <button type="button" class="btn print" onclick="print();">Print Report</button>
                                                <button type="button" class="btn btn-info" onclick="generateFile()">Download As Excel</button>
                                                <!--                                                <button type="button" class="btn print" onclick="printSummery();">Print Summary Report</button>-->
                                            </div>

                                            <br>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="search_result">
                            <div class="x_title" align="center">
                                <h4>Report Data</h4>                                        
                                <div class="clearfix"></div>
                            </div>

                            <div class="row">
                                <div  class="col-md-2 "><h5>From Date (Purchase Date)</h5></div>
                                <div class="col-md-4">
                                    <h5 class="text-primary" id="from_dateD"></h5>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-2"><h5>To Date (Purchase Date)</h5></div>                                   
                                <div class="col-md-4">
                                    <h5 class="text-primary"  id="to_dateD"></h5>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-2"><h5>Total Sales</h5></div>

                                <div class="col-md-4">
                                    <h5 class="text-primary"  id="tot_sales"></h5>
                                </div>
                            </div>

                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Lottery Name</th>
                                        <th>Draw No</th>
                                        <th>Draw Date</th>
                                        <th>Ticket Reference No</th>
                                        <th>Purchase Date</th>
                                        <th>Customer's Reference No</th>
                                        <th>Ticket Value</th>
                                        <th>Transaction Reference</th>
                                        <th>Mode of Purchase</th>
                                        <th>Currency</th>
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <jsp:include page="../common/footer.jsp"/>
            </div>
        </div>
    </body>

    <script>

        var resultTable = "";
        $(document).ready(function () {

            //datatable
            $('#search_result').hide();
//            resultTable = $('#result_table').dataTable({
//                "searching": false
//            });
            resultTable = null;

            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });

        });

        function search1() {
            var formData = $('#search_form').serialize();
            var fromDate = $('#from_date').val();
            var toDate = $('#to_date').val();

            var tot_sales = 0.00;

            $('#from_dateD').html($('#from_date').val());
            $('#to_dateD').html($('#to_date').val());
            $.ajax({
                data: formData,
                url: "show_report.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    //clear table
                    var response = JSON.parse(data);
                    if (response.status === true) {


                    } else {
                        //show msg
                        showNotification('error', response.msg);
                    }

                    $('#tot_sales').html("LKR " + tot_sales.toFixed(2));


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }



            });



        }

        function search() {

            if (validateForm($('#search_form'))) {
                var formData = $('#search_form').serialize();
                var fromDate = $('#from_date').val();
                var toDate = $('#to_date').val();

                var tot_sales = 0.00;

                $('#from_dateD').html($('#from_date').val());
                $('#to_dateD').html($('#to_date').val());


                if (checkFromDateNToDate(fromDate, toDate)) {

                    if (resultTable !== null) {
                        resultTable.destroy();
                        resultTable = null;
                    }
                    $('#search_result').show();

                    var toDate = $('#to_date').val();
                    var fromDate = $('#from_date').val();
                    var gameType = $('#lottery').val();
                    resultTable = $('#result_table').DataTable
                            ({

                                "ajax":
                                        {
                                            url: "show_paginated_report.htm",
                                            data: {
                                                "fromDate": fromDate,
                                                "toDate": toDate,
                                                "gameType": gameType
                                            },
                                            type: "GET",
                                            error: function (xhr, status, error)
                                            {
                                                alert(status);
                                            },

                                        },
                                "paging": true,
                                "serverSide": true,
                                "processing": true,
                                "order": [],
                                "search": false,
                                "header": true,
                                "columns":
                                        [
                                            {"title": "Lottery Name", "data": "lottery_name", "defaultContent": "-"},
                                            {"title": "Draw No", "data": "draw_no", "defaultContent": "-"},
                                            {"title": "Draw Date", "data": "draw_date", "defaultContent": "-"},
                                            {"title": "Ticket Reference No", "data": "tkt_reff", "defaultContent": "-"},
                                            {"title": "Purchase Date", "data": "purchase_date", "defaultContent": "-"},
                                            {"title": "Customer's Reference No", "data": "cus_reff", "defaultContent": "-"},
                                            {"title": "Ticket Value", "data": "tkt_value", "defaultContent": "-"},
                                            {"title": "Transaction Reference", "data": "tr_id", "defaultContent": "-"},
                                            {"title": "Mode of Purchase", "data": "mode_purchase", "defaultContent": "-"},
                                            {"title": "Currency", "data": "currency", "defaultContent": "-"}

                                        ],
                            });

                }
            }
            $('#tot_sales').html("Loading ...");
            window.onload = setTimeout(generateTotal, 9000);
        }



        function generateTotal() {
            var tot_sales = 0.00;
            var res = resultTable.context[0];
            tot_sales = parseFloat(res.json.tktValue);
            $('#tot_sales').html("LKR " + tot_sales.toFixed(2));
        }

        function generateFile() {

            var fromDate = $('#from_date').val();
            var toDate = $('#to_date').val();
            var lottery = $('#lottery').val();

            window.open("download_excel.htm?from_date=" + fromDate + "&to_date=" + toDate + "&lottery=" + lottery);

        }
    </script>

</html>
