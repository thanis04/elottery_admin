<%-- 
    Document   : rpt_reconciliation
    Created on : Mar 29, 2021, 11:27:45 AM
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
                                <h4>Reconciliation report</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="show_report_bydate.htm"  class="form-horizontal" id="search_form" role="form">
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
                                                <label class="">From Date<span class="text-red"> *</span></label>
                                                <input  type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date<span class="text-red"> *</span></label>
                                                <input  type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>

                                            <div class="form-group" align="right">
                                                <button  type="button" class="btn btn-secondary" onclick="reset();">Reset</button>
                                                <button type="button" class="btn submit" onclick="loadTable();">Search</button>
                                                <!--<button type="button" class="btn print" onclick="print();">Print Report</button>-->
                                                <button type="button" class="btn btn-info" onclick="generateFile()">Download As Excel</button>
                                                <!--                                               <button type="button" class="btn print" onclick="printSummery();">Print Summary Report</button>-->
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
                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>

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

        function reset() {
            $('#search_result').hide();
            var form = $('#search_form');
            resetFrom(form);
            resultTable.fnClearTable();
        }

        function loadTable() {

            if ($('#from_date').val() === "" || $('#to_date').val() === "") {
                showNotification('error', "Please Fill From Date And To Date");
            } else {
//            alert(page);
                if (resultTable !== null) {
                    resultTable.destroy();
                    resultTable = null;
                }

                $('#search_result').show();

                resultTable = $('#result_table').DataTable
                        ({

                            "ajax":
                                    {
                                        url: "show_paginated_data.htm",
                                        data: {
                                            "gameType": $('#lottery').val(),
                                            "fromDate": $('#from_date').val(),
                                            "toDate": $('#to_date').val()
                                        },
                                        type: "POST",
                                        error: function (xhr, status, error)
                                        {
                                            alert(status);
                                        }
                                    },
                            "paging": true,
                            "serverSide": true,
                            "processing": true,
                            "order": [],
                            "search": false,
                            "header": true,
                            "columns":
                                    [
                                        {"title": "Lottery name", "data": "lotteryType", "defaultContent": "-"},
                                        {"title": "Draw number", "data": "drawNo", "defaultContent": "-"},
                                        {"title": "Draw date", "data": "drawDate", "defaultContent": "-"},
                                        {"title": "Redemption date", "data": "redemptionDate", "defaultContent": "-"},
                                        {"title": "Redemption mode", "data": "redemptionMode", "defaultContent": "-"},
                                        {"title": "Reference ID", "data": "referenceID", "defaultContent": "-"},
                                        {"title": "Winning prize", "data": "winningPrize", "defaultContent": "-"},
                                        {"title": "Ticket reference", "data": "ticketReference", "defaultContent": "-"},
                                        {"title": "Purchase History ID", "data": "pur_id", "defaultContent": "-"},
                                        {"title": "RRN", "data": "rrn", "defaultContent": "-"}
                                    ]
                        });
            }
        }


        function generateFile() {


            if ($('#from_date').val() === "" || $('#to_date').val() === "") {
                showNotification('error', "Please Fill From Date And To Date");
            } else {

                var fromDate = $('#from_date').val();
                var toDate = $('#to_date').val();
                var lottery = $('#lottery').val();

                window.open("download_excel.htm?fromDate=" + fromDate +
                        "&toDate=" + toDate + "&gameType=" + lottery);
            }
        }
    </script>
</html>

