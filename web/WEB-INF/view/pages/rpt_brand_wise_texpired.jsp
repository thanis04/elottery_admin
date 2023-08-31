<%-- 
    Document   : rpt_brand_wise_expired
    Created on : Mar 1, 2021, 1:51:20 PM
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
                                <h4>Expired ticket report</h4>
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
                                                <label class="">From Date
                                                    <!--<span class="text-red"> *</span>-->
                                                </label>
                                                <input  type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date
                                                    <!--<span class="text-red"> *</span>-->
                                                </label>
                                                <input  type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>

                                            <!--                                            <div class="form-group">
                                                <label class="">Draw number </label>
                                                <input  type="text" class="form-control" id="draw_no" name="draw_no" placeholder="Enter draw number" />
                                                                                        </div>-->

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
                                        <th>Lottery name</th>
                                        <th>Draw number</th>
                                        <th>Draw date</th>
                                        <th>Expiry date</th>
                                        <th>Ticket reference</th>
                                        <th>Purchased date</th>
                                        <th>NIC number</th>
                                        <th>Customer reference</th>
                                        <th>Ticket value</th>
                                        <th>Prize value</th>
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
        var page = 1;


        $(document).ready(function () {

            //datatable
            $('#search_result').hide();
//            resultTable = $('#result_table').dataTable({
//                "searching": false,
//                "paging": true
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

        function search() {

//            var letterNumber = /^[0-9a-zA-Z]+$/;
//            if ($('#agent_code').val().match(letterNumber)) {

//                if ($('#agent_code').val() === "" || $('#from_date').val() === "" ||
//                        $('#to_date').val() === "") {
//                    showNotification('error', "Please Fill All the Fields");
//                } else {



            var formData = {
                "gameType": $('#lottery').val(),
//                        "fromDate": null,
//                        "toDate": null,
                "drawNumber": "",
                "start": page
            }

            $.ajax({
                data: formData,
                url: "show_report_data.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    //clear table
                    var response = JSON.parse(data);
                    if (response.status === "success") {
                        $('#search_result').show();
                        resultTable.fnClearTable();

                        //set data table data
                        var tabledata = response.result;
                        $(tabledata).each(function (index) {
                            var item = tabledata[index];

                            var row = [item.lotteryType, item.drawNo, item.expDate,
                                item.drawDate, item.ticketReference,
                                item.purchasedDate, item.nicNumber,
                                item.customerReference, item.ticketValue,
                                item.prizeValue];

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

        }


        $('#result_table').on('page.dt', function () {
            var info = resultTable.page.info();
            page = info.page + 1;
            console.log(page);
//            alert('changed - page ' + page + ' out of ' + info.pages + ' is clicked');
            search();
        });

        function loadTable() {

            if (($('#from_date').val() === "" && $('#to_date').val() === "") ||
                    ($('#from_date').val() !== "" && $('#to_date').val() !== "")) {
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
                                            "drawNumber": "",
                                            "fromDate": $("#from_date").val(),
                                            "toDate": $("#to_date").val()
//                                        "start": page,
//                                        "end": "10"
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
                                        {"title": "Expiry date", "data": "expDate", "defaultContent": "-"},
                                        {"title": "Ticket reference", "data": "ticketReference", "defaultContent": "-"},
                                        {"title": "Purchased date", "data": "purchasedDate", "defaultContent": "-"},
                                        {"title": "NIC number", "data": "nicNumber", "defaultContent": "-"},
                                        {"title": "Customer reference", "data": "customerReference", "defaultContent": "-"},
                                        {"title": "Ticket value", "data": "ticketValue", "defaultContent": "-"},
                                        {"title": "Prize value", "data": "prizeValue", "defaultContent": "-"},
                                    ]
                        });
            } else {
                showNotification('error', "Please select both from date and to date!");
            }
        }


        function generateFile() {

            if (($('#from_date').val() === "" && $('#to_date').val() === "") ||
                    ($('#from_date').val() !== "" && $('#to_date').val() !== "")) {

                var gameType = $('#lottery').val();
                var drawNumber = "";
                var fromDate = $('#from_date').val();
                var toDate = $('#to_date').val();
                window.open("download_excel.htm?gameType=" + gameType + "&drawNumber=" + drawNumber + "&from_date=" + fromDate + "&to_date=" + toDate);
            } else {
                showNotification('error', "Please select both from date and to date!");
            }
        }
    </script>
</html>
