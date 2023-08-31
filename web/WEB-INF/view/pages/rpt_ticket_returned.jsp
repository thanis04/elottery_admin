<%-- 
    Document   : rpt_ticket_returned
    Created on : Feb 27, 2020, 4:43:06 PM
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
                                <h4>Ticket Returned Report</h4>
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
                                                <label class="">From Date<span class="text-red"> *</span></label>
                                                <input  type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date<span class="text-red"> *</span></label>
                                                <input  type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>


                                            <div class="form-group" align="right">
                                                <button  type="reset" class="btn btn-secondary" >Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">Search</button>
                                                <!--<button type="button" class="btn print" onclick="print();">Print Report</button>-->
                                                <button type="button" class="btn btn-info" onclick="generateFile()">Download As Excel</button>
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
                                        <th>Lottery Name</th>
                                        <th>Draw No</th>
                                        <th>Ticket Reference No</th>
                                        <th>Ticket Value</th>
                                        <th>Currency</th>
                                        <th>Ticket Purchased Date</th>
                                        <th>Ticket Returned Date</th>
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
            resultTable = null;
//            resultTable = $('#result_table').dataTable({
//                "searching": false
//            });

            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });

        });

        function search1() {

            if (validateForm($('#search_form'))) {
                var formData = $('#search_form').serialize();
                var category = $('#lottery').val();


                if (category !== null) {
                    $.ajax({
                        data: formData,
                        url: "show_report.htm",
                        type: 'GET',
                        success: function (data, textStatus, jqXHR) {
                            //clear table
                            var response = JSON.parse(data);
                            if (response.status === true) {
                                $('#search_result').show();
                                resultTable.fnClearTable();

                                //set data table data
                                var tabledata = response.search_result;
                                $(tabledata).each(function (index) {
                                    var item = tabledata[index];

                                    var row = [item.lottery_name, item.draw_no, item.draw_date, item.tkt_reff, item.tkt_value, item.currency, item.ticket_pur_date, item.ticket_ret_date];
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

        function search() {

            if ($('#from_date').val() === "" || $('#to_date').val() === "") {
                showNotification('error', "Please Fill From Date And To Date");
            } else {
                if (resultTable !== null) {
                    resultTable.destroy();
                    resultTable = null;
                }

                $('#search_result').show();

                resultTable = $('#result_table').DataTable
                        ({

                            "ajax":
                                    {
                                        url: "show_report.htm",
                                        data: {
                                            "gameType": $('#lottery').val(),
                                            "fromDate": $('#from_date').val(),
                                            "toDate": $('#to_date').val()
                                        },
                                        type: "GET",
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
                                        {"title": "Lottery name", "data": "lottery_name", "defaultContent": "-"},
                                        {"title": "Draw Number", "data": "draw_no", "defaultContent": "-"},
                                        {"title": "Ticket Reference Number", "data": "tkt_reff", "defaultContent": "-"},
                                        {"title": "Ticket Value", "data": "tkt_value", "defaultContent": "-"},
                                        {"title": "Currency", "data": "currency", "defaultContent": "-"},
                                        {"title": "Ticket Uploaded Date", "data": "ticket_upload_date", "defaultContent": "-"},
                                        {"title": "Ticket Returned Date", "data": "ticket_ret_date", "defaultContent": "-"}
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
