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

    <style>
        .modal-user-view{
            height: 800px;
            overflow: auto;
            margin: 40px;
        }
    </style>

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
                                <h4>Ticket Search</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="show_report_bydateAndCategory.htm"  class="form-horizontal" id="search_form" role="form">
                                            <div class="form-group">
                                                <label class="">From Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">Draw No</label>
                                                <input onkeypress="return onlyNumbers()" type="text" maxlength="15" class="form-control " id="draw_no" name="draw_no" placeholder="Enter Draw No" />
                                            </div>
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

                                            <div class="form-group" align="right">
                                                <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">Search</button>
                                                <button  type="button" class="btn print" onclick="print();">Print Report</button>                                             

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
                                        <!--<th>ID</th>-->
                                        <th>Lottery Name</th>
                                        <th>Draw No</th> 
                                        <th>Draw Date</th>
                                        <th>No.of tickets</th>
                                        <th>Sold Tickets</th>
                                        <th>In Cart Tickets </th>
                                        <th>Un-Sold Tickets</th>
                                        <th>Uploaded Date</th>
                                        <th>Uploaded User</th>
                                        <th>Action</th>   
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="downloadModal" role="dialog">
                    <div class="modal-user-view">                       
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-white">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4 id="window_title" class="modal-title">Additional Records To Be Removed</h4>
                                <hr class="hr" />
                            </div>
                            <div class="modal-body">

                                <div id="search_result_1">
                                    <div class="x_title" align="center">

                                    </div>
                                    <!-- table -->
                                    <table class="table table-striped table-bordered bulk_action" id="result_table_1">
                                        <thead>
                                            <tr>
                                                <!--<th>ID</th>-->
                                                <th>Id</th>
                                                <th>Transaction No</th> 
                                                <th>Serial No</th>
                                                <th>Wallet Id</th>
                                                <th>Full Name</th>
                                                <th>NIC</th>
                                            </tr>
                                        </thead>
                                        <tbody>


                                        </tbody>
                                    </table>
                                </div>

                            </div>
                            <div class="modal-footer">
                                <span class="text-red">After Confirmation, You Can Generate Sales File</span>
                                <span id="btn-confirm-display"></span>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>

                    </div>
                </div>     

                <!-- footer content -->
                <jsp:include page="../common/footer.jsp"/>
                <!-- /footer content -->
            </div>
        </div>
        <form id="view" name="view" method="GET" target="_blank">
            <input type="hidden" id="id" name="id">
        </form>
        <form id="reciept" name="reciept" method="POST" target="_blank">
            <input type="hidden" id="fileid" name="fileid">
        </form>



    </body>
    <script>
        var resultTable;
        var resultTable_1;
        $(document).ready(function () {

            //datatable
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            resultTable_1 = $('#result_table_1').dataTable({
                "searching": false
            });

            //set date selecter         
            $(function () {
                $("#from_date").datepicker({dateFormat: 'yy-mm-dd'});
                $("#to_date").datepicker({dateFormat: 'yy-mm-dd'});
            });

        });

        //search function
        function search() {
            console.log("function")

            if (validateForm($('#search_form'))) {
                var formData = $('#search_form').serialize();
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
                                $('#search_result').show();
                                resultTable.fnClearTable();

                                //set data table data
                                var tabledata = response.search_result;
                                $(tabledata).each(function (index) {
                                    var item = tabledata[index];
                                    ///////////////////////yasitha strat
                                    var salesWithLink = "<a href =\"#\" onclick=\"viewSales('" + item.fileid + "');\" >" + item.sales + "</a>";
                                    var returnsWithLink = "<a href =\"#\" onclick=\"viewReturns('" + item.fileid + "');\" >" + item.rt + "</a>";
                                    var buttonTag = "";

                                    if (item.status === '27') {
                                        if (parseInt(item.cart) === 0) {
                                            buttonTag = "<span class='lable lable-success'  id='prg_" + item.fileid + "'></span><button id='btn_download_" + item.fileid + "' type=\"button\" class=\"btn btn-info btn-sm \" type=\"button\"  onclick=\"dowanload_file('" + item.fileid + "');\" > Generate Sales File </button>";
                                        } else {
                                            buttonTag = "<span title='Some tickets are in the cart. You can not generate sales file now. If business hours are over, please wait 10Minute to release tickets' class='label label-warning'>Purchasing In Progress </span>";

                                        }
                                    }

                                    if (item.status === '36') {
                                        buttonTag = "<span class='lable lable-success'  id='prg_" + item.fileid + "'></span><button id='btn_download_" + item.fileid + "' type=\"button\" class=\"btn btn-info btn-sm \" type=\"button\"  onclick=\"directDownload('" + item.fileid + "');\" > Download Sales File </button>";
                                    }

                                    var row = [
                                        item.productDescription,
                                        item.drawNo,
                                        item.drawDate,
                                        item.noOfTickets,
                                        salesWithLink,
                                        item.cart,
                                        returnsWithLink,
                                        item.createTime,
                                        item.lastUpdateUser,
                                        buttonTag
                                    ];
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


        function print() {

            console.log("print");
            if (validateForm($('#search_form'))) {
                $('#search_form').attr('action', 'search_print.htm');
                $('#search_form').submit();
            }

        }
        function printSummery() {
            if (validateForm($('#search_form'))) {
                $('#search_form').attr('action', 'print_summery_report_bydate.htm')
                $('#search_form').submit();
            }

        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }

        function viewSales(para)
        {
            $('#reciept #fileid').val(para);
            document.reciept.action = "${pageContext.servletContext.contextPath}/ticket_search/viewsales.htm";
            document.reciept.submit();
        }

        function viewReturns(para)
        {
            $('#reciept #fileid').val(para);
            document.reciept.action = "${pageContext.servletContext.contextPath}/ticket_search/viewreturns.htm";
            document.reciept.submit();
        }

        function directDownload(item) {
            $('#view #id').val(item);
            document.view.action = "${pageContext.servletContext.contextPath}/ticket/dowanload_sales_file.htm";
            document.view.submit();
            search();
        }


        function dowanload_file(item)
        {
            var formData = {
                "id": item
            };
            $.ajax({
                data: formData,
                url: "${pageContext.servletContext.contextPath}/ticket/get_duplicate.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {

                    var response = JSON.parse(data);
                    if (response.count !== 0) {
                        $('#search_result_1').show();
                        resultTable_1.fnClearTable();
                        var tabledata = response.record;

                        $(tabledata).each(function (index) {
                            var item = tabledata[index];
                            var row = [
                                item.id,
                                item.txn_no,
                                item.serial_no,
                                item.wallet_id,
                                item.name,
                                item.nic];
                            resultTable_1.fnAddData(row);
                        });
                        var btn = "<button type=\"button\" class=\"btn btn-info btn-sm \" type=\"button\"  onclick=\"removeDuplicate('" + item + "');\" > Confirm </button>"
                        $('#btn-confirm-display').html(btn);
                        $('#downloadModal').modal('toggle');
                    } else {
                        
                        $('#view #id').val(item);
                        document.view.action = "${pageContext.servletContext.contextPath}/ticket/dowanload_sales_file.htm";
                        document.view.submit();
                        search();
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            });



        }

        function removeDuplicate(id) {
            var formData = {
                "id": id
            };
            $.ajax({
                data: formData,
                url: "${pageContext.servletContext.contextPath}/ticket/remove_duplicate.htm",
                type: 'POST',
                success: function (data, textStatus, jqXHR) {
                    $('#downloadModal').modal('toggle');

                    var response = JSON.parse(data);
                    if (response.count === 0) {
                        showNotification(response.status, response.msg);
                        $('#view #id').val(id);
                        document.view.action = "${pageContext.servletContext.contextPath}/ticket/dowanload_sales_file.htm";
                        document.view.submit();
                        search();
                    } else {
                        dowanload_file(id);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            });
        }


        function requestSalesFile(id) {
            var dataString = "id=" + parseInt(id);
            $.ajax({
                data: dataString,
                type: 'GET',
                url: "${pageContext.servletContext.contextPath}/ticket/request_sales_file.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    showProgress(response.id);
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });



        }


        function showProgress(id) {
            var status = false;

            var prgVr = "#prg_" + id;
            $(prgVr).html("");
            console.info(prgVr);

            var refreshIntervalId = setInterval(fnShowProcessProgress, 1000);

            function fnShowProcessProgress() {
                var dataString = "id=" + parseInt(id);
                $.ajax({
                    data: dataString,
                    type: 'GET',
                    url: "${pageContext.servletContext.contextPath}/ticket/request_sales_file.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //hide window if record is ok
                        if (parseInt(response.prg) < 100) {
                            $(prgVr).html(response.prg);
                        } else if (parseInt(response.prg) === 100) {
                            $(prgVr).html(response.prg);
                            $(prgVr).html("Ready");
                            var btnDownload = "#btn_download_" + id;
                            $(btnDownload).val('Download Sales File');
                            $(btnDownload).addClass('btn btn-success');
                            $(btnDownload).click(function () {
                                dowanload_file(id);
                            });
                        }


                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        status = true;
                    }

                });

                //stop interval when process is completed
                if (status) {
                    clearInterval(refreshIntervalId);
                }

            }
        }


    </script>



</html>
