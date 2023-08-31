<%-- 
    Document   : device_mgt
    Created on : Oct 11, 2017, 4:17:17 PM
    Author     : methsiri_h
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>

        <style type="text/css">
            /*            .modal-dialog {
                            width: 100% !important;
                            height: 100% !important;
                            padding: 0;
                        }
            
                        .modal-content {
                            height: 100%;
                            border-radius: 0;
                        }*/

            /*            .modal-content {
                            width: 600px;
                            align-items: center;
                            margin-left: 30%;
                            height: 170px;
                            margin-top: 10%;
                        }*/


            .pop_window{
                width: 213vh !important;
                margin: 30px auto !important;
            }
        </style>
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
                                <h4>Device Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Device</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  autocomplete="off"  class="form-horizontal" id="search_form">


                                            <div class="form-group">
                                                <label class="">Last Login Date (From Date)<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select Date">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Last Login Date (To Date)<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select Date">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Mobile No</label>
                                                <div class="">
                                                    <input data-validation="tel" maxlength="10" type="text" onkeydown="isNumberOnly(event)" class="form-control" type="text" name="mobileNo" id="mobileNo" placeholder="Enter Mobile No"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">NIC No</label>
                                                <div class="">
                                                    <input maxlength="12" data-validation="nic"  class="form-control" type="text" name="nic" id="nic" placeholder="Enter NIC No"/>
                                                </div>
                                            </div>

                                            <div class="form-group" align="right">
                                                <label class=""></label>
                                                <div class="">
                                                    <button type="button" class="btn btn-secondary" onclick="resetMyTheForm(); this.form.reset();">Reset</button>
                                                    <button type="button" class="btn submit" onclick="searchDevice()">Search</button>
                                                    <!--<button type="button" class="btn btn-default" onclick="this.form.reset()">Reset</button>-->
                                                </div>
                                            </div>

                                            <br>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="search_result">
                            <div class="x_title" align="center">
                                <h4>Search Result</h4>                                        
                                <div class="clearfix"></div>
                            </div>
                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Device Name</th>                                                          
                                        <th>Name</th>
                                        <th>NIC No</th>
                                        <th>Mobile</th>
                                        <th>Last Login Date</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>

                <!-- footer content -->
                <jsp:include page="../common/footer.jsp"/>
                <!-- /footer content -->
            </div>
        </div>

        <!-- Modal -->
        <div id="modalActivity" class="modal fade" data-backdrop="static"  role="dialog" data-keyboard="true" tabindex="-1">
            <div class="modal-dialog pop_window">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Activity History</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table table-striped table-bordered bulk_action" id="activity_table">
                            <thead>
                                <tr>
                                    <th>Date</th>                                                          
                                    <th>Description</th>                                       
                                </tr>
                            </thead>
                            <tbody>


                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>   

        <!-- Modal -->
        <div id="modalTransaction" class="modal fade" data-backdrop="static" role="dialog" data-keyboard="true" tabindex="-1">
            <div class="modal-dialog pop_window">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Transaction History</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table table-striped table-bordered bulk_action" id="tranaction_table">
                            <thead>
                                <tr>
                                    <th>Date</th>                                                          
                                    <th>Type</th>
                                    <th>Status</th>
                                    <th>Amount</th>                                      
                                </tr>
                            </thead>
                            <tbody>


                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>     
        <!-- Modal -->
        <div id="modalPurchase" class="modal fade" data-backdrop="static" role="dialog" data-keyboard="true" tabindex="-1">
            <div class="modal-dialog col-md-12 pop_window">

                <!-- Modal content-->
                <div class="modal-content ">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Purchase History</h4>
                    </div>
                    <div class="modal-body">
                        <table class="table table-striped table-bordered bulk_action" id="purchase_table">
                            <thead>
                                <tr>                                       
                                    <th>Date</th>                                                          
                                    <th>Lottery</th>
                                    <th>Draw No</th>
                                    <th>Draw Date</th>
                                    <th>Ticket No</th>
                                    <th>Payment Method</th>
                                    <th>Masking Card No/Account No</th>
                                </tr>
                            </thead>
                            <tbody>


                            </tbody>
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>

            </div>
        </div>     
    </body>

    <script>
        var resultTable;
        var activityTable;
        var tranactionTable;
        var purchaseTable;
        $(document).ready(function () {

            //datatable
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });


            activityTable = $('#activity_table').dataTable({
                "searching": false
            });

            tranactionTable = $('#tranaction_table').dataTable({
                "searching": false
            });

            purchaseTable = $('#purchase_table').dataTable({
                "searching": false
            });

            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });



        });

        //search function
        function searchDevice() {
            var formData = $('#search_form').serialize();

            if (validateSearch()) {
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
                                var actionBtn = "";
                                if (item.pin_request === 0) {
                                    actionBtn = '<button title="Pin Requested" id="btn-' + item.id + '" type="button" class="btn btn-success" onclick="requestPin(' + item.id + ')"><span class="fa fa-toggle-on"></span></button>';
                                } else {
                                    actionBtn = '<button title="Pin Requested" disabled id="btn-"+' + item.id + ' type="button" class="btn btn-default"  ><span class="fa fa-toggle-on"></span></button>';

                                }

                                //view history
                                actionBtn = actionBtn + ' <button title="View Activity History"  type="button" class="btn btn-info" onclick="viewActivity(' + item.id + ')" ><span class="fa fa-history"></span></button>';

                                //view purchase history
                                actionBtn = actionBtn + ' <button title="View Purchase History"  type="button" class="btn btn-warning" onclick="viewPurchase(' + item.id + ')" ><span class="fa fa-shopping-cart"></span></button>';

                                //view transaction
                                actionBtn = actionBtn + ' <button title="View Transaction History"  type="button" class="btn btn-primary" onclick="viewTransaction(' + item.id + ')" ><span class="fa fa-exchange"></span></button>';





                                var row = [item.brand + " - " + item.model, item.fanme + " " + item.lanme, item.nic, item.mobile, item.login_date, actionBtn];
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

        }

        function resetMyTheForm() {
            resultTable.fnClearTable();
        }



        //search critia validate
        function validateSearch() {
            var from_date = $('#from_date').val();
            var to_date = $('#to_date').val();
            var nic = $('#nic').val();
            var mobileNo = $('#mobileNo').val();


            $('#from_date').removeClass('error-indicate');
            $('#to_date').removeClass('error-indicate');

            if (!checkFromDateNToDate(from_date, to_date)) {
                showNotification('error', 'Invalid date combination');
                return false;
            } else if (nic === '' && mobileNo === '' && from_date === '' && to_date === '') {
                if (from_date === '') {
                    $('#from_date').addClass('error-indicate');
                }
                if (to_date === '') {
                    $('#to_date').addClass('error-indicate');
                }
                showNotification('error', "Search criteria can't be empty");
                return false;

            } else if (nic !== '' && !isNIC(nic)) {
                showNotification('error', "Invalid NIC");
                return false;

            } else if (mobileNo !== '' && !isPhonenumber(mobileNo)) {
                showNotification('error', "Invalid Mobile No");
                return false;
            }


            return true;
        }

        //request pin function
        function requestPin(id) {
            function requestPinFunction() {
                var dataString = "id=" + id;

                $.ajax({
                    data: dataString,
                    url: "request_pin.htm",
                    type: 'POST',
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //hide window if record is ok
                        if (response.status === 'success') {
                            //set button as disabled
                            $('#btn-' + id + '').prop('disabled', true);
                            $('#btn-' + id + '').removeClass('btn btn-success');
                            $('#btn-' + id + '').addClass('btn btn-default');
                            $('#btn-' + id + '').html('<span class="fa fa-toggle-on"></span>');
                        }

                        //show msg
                        showNotification(response.status, response.msg);

                    }, error: function (jqXHR, textStatus, errorThrown) {

                    }
                });

            }
            showConfirmMsg('Request PIN', 'Are you sure to request PIN', requestPinFunction);
        }



        function viewActivity(id) {
            
            $('#activity_table').dataTable().fnClearTable();

            $('#modalActivity').modal('show');
            var dataString = "id=" + id + "&type=activity";
            $.ajax({
                data: dataString,
                url: "view_history.htm",
                type: 'POST',
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        var tabledata = response.search_result;
                        $(tabledata).each(function (index) {
                            var item = tabledata[index];

                            var row = [item.date, item.description];
                            activityTable.fnAddData(row);
                        });
                    } else {
                        //show msg
                        showNotification(response.status, response.msg);

                    }


                }, error: function (jqXHR, textStatus, errorThrown) {

                }
            });
        }

        function viewPurchase(id) {
            $('#purchase_table').dataTable().fnClearTable();
            
            $('#modalPurchase').modal('show');
            var dataString = "id=" + id + "&type=purchase";
            $.ajax({
                data: dataString,
                url: "view_history.htm",
                type: 'POST',
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        var tabledata = response.search_result;

                        $(tabledata).each(function (index) {
                            var item = tabledata[index];

                            var row = [item.date, item.lottery, item.draw_no, item.draw_date, item.serial_no, item.payment_method, item.reference_no];
                            purchaseTable.fnAddData(row);
                        });
                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }



                }, error: function (jqXHR, textStatus, errorThrown) {

                }
            });
        }

        function viewTransaction(id) {
            $('#tranaction_table').dataTable().fnClearTable();
            
            $('#modalTransaction').modal('show');
            var dataString = "id=" + id + "&type=transaction_history";
            $.ajax({
                data: dataString,
                url: "view_history.htm",
                type: 'POST',
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        var tabledata = response.search_result;
                        $(tabledata).each(function (index) {
                            var item = tabledata[index];

                            var row = [item.date, item.type, item.status, item.amount];
                            tranactionTable.fnAddData(row);
                        });
                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }



                }, error: function (jqXHR, textStatus, errorThrown) {

                }
            });
        }


    </script>

</html>
