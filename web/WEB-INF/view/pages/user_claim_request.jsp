<%-- 
    Document   : user_claim_request
    Created on : Sep 14, 2021, 4:46:39 PM
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
        <link href="<c:url value="/resources/custom/css/scannerjs.css"/>" rel="stylesheet">
        <script src='<c:url value="/resources/custom/js/scanner.js"/>'></script>    

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
                                <h4>User Claim Request</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Customer</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">
                                            <div class="form-group">
                                                <label>Serial No</label>
                                                <input maxlength="20" class="form-control" type="text" name="serialNo" id="serialNo" placeholder="Enter Serial No" />
                                            </div>

                                            <div class="form-group">
                                                <label>Transaction ID</label>                                                  
                                                <input maxlength="100" class="form-control" type="text" name="txnId" id="txnId" placeholder="Enter Transaction Id" />
                                            </div>

                                            <div class="form-group">
                                                <label>Purchase history ID</label>                                                  
                                                <input maxlength="100" class="form-control" type="text" name="purchaseId" id="purchaseId" placeholder="Enter Purchase history ID" />
                                            </div>
                                            <div class="form-group">
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                    <button type="button" class="btn submit" onclick="search();">Search</button>
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
                                        <th>Ticket Id (Purchase History ID)</th>
                                        <th>Serial No</th>
                                        <th>Lottery Name</th>
                                        <th>Draw Date</th>
                                        <th>Purchased Date</th>
                                        <th>NIC</th>
                                        <th>Winning Amount</th>
                                        <th>Transaction Id</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>


                <!-- /page content -->

                <!-- Add new Modal -->
                <div class="modal fade" id="addNew" role="dialog">
                    <div class="modal-dialog">   

                        <form class="form-horizontal" id="new_form" >
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header bg-white">
                                    <button id="btn_small_close" type="button" class="close " data-dismiss="modal">&times;</button>
                                    <h4 id="window_title" class="modal-title">Request</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">
                                    <!-- model body -->
                                    <input class="form-control" type="text" 
                                           name="id" id="id"  maxlength="29"/>
                                    <input class="form-control" type="text" 
                                           name="txnId" id="txnId"  maxlength="29"/>
                                    <div class="form-group col-lg-12">
                                        <label>New Status<span class="text-red"> *</span></label>
                                        <div class="">
                                            <select type="text" class="form-control" 
                                                    name="newStatus" id="newStatus"  onchange="changeNewStatus()">
                                                <option selected value="0">--Select Status--</option>
                                                <option value="20">Unclaimed</option>
                                                <option value="25">Claimed</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >First Statement<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input type="text" class="form-control
                                                   dateMin" id="firstStatement" name="firstStatement" 
                                                   placeholder="Select First Statement" />
                                        </div>
                                    </div>                                 

                                    <div class="form-group col-lg-6" id="ifUnclaimed">
                                        <label >Second Statement<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input type="text" class="form-control
                                                   dateMin" id="secondStatement" name="secondStatement" 
                                                   placeholder="Select Second Statement" />
                                        </div>
                                    </div> 

                                    <div class="form-group col-lg-12">
                                        <label >Remark<span class="text-red"> *</span> 
                                        </label><span class="text-secondary" style="font-size: 12px"></span> 
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="text" name="remark" id="remark" maxlength="256"/>
                                        </div>
                                    </div>

                                    <input  type="hidden" name="tokenPublicKey" id="tokenPublicKey"/>
                                    <!--<input data-validation="required-sp-allow" type="hidden" name="tokenPublicKey" id="tokenPublicKey"/>-->

                                    <div class="row">
                                        <div class="form-group col-lg-6">
                                            <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                        </div>    
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button id="btn_close" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button id="btn_update" type="button" class="btn btn-primary" onclick="saveRequest()"> Submit </button>
                                    <button id="btn_reset"type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                </div>
                            </div>
                        </form>  
                    </div>
                </div>

                <!-- footer content -->
                <jsp:include page="../common/footer.jsp"/>
                <!-- /footer content -->
            </div>
        </div>
    </body>

    <script>
        var resultTable;
        $(document).ready(function () {

            //datatable
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            //set date selecter         
            $(function () {
                $("#firstStatement").datepicker({dateFormat: 'yy-mm-dd'});
                $("#secondStatement").datepicker({dateFormat: 'yy-mm-dd'});
            });

            $('#ifUnclaimed').hide();

        });

        function changeNewStatus() {
            if ($('#newStatus').val() === "20") {
                $('#ifUnclaimed').show();
            } else {
                $('#ifUnclaimed').hide();
            }
        }

        function reset() {
            $('#search_result').hide();
            var form = $('#search_form');
            resetFrom(form);
            resultTable.fnClearTable();
        }
        //search function
        function search() {

            if ($('#txnId').val() === "" && $('#serialNo').val() === "" && $('#purchaseId').val() === "") {
                showNotification('error', "Please Fill At least One Field!");
            } else {
                var txnId = $('#txnId').val();
                var serialNo = $('#serialNo').val();
                var purchaseId = $('#purchaseId').val();

                if ($('#txnId').val() !== "" && $('#serialNo').val() !== "") {
                    showNotification('error', "You cannot enter Transaction Id with Serial No or Purchase Id!");
                    return;
                }

                if ($('#txnId').val() !== "" && $('#purchaseId').val() !== "") {
                    showNotification('error', "You cannot enter Transaction Id with Serial No or Purchase Id!");
                    return;
                }

                if ($('#txnId').val() === "") {
                    txnId = "-";
                }
                if ($('#serialNo').val() === "") {
                    serialNo = "-";
                }
                if ($('#purchaseId').val() === "") {
                    purchaseId = "-";
                }

                var formData = {
                    'txnId': txnId,
                    'serialNo': serialNo,
                    'purchaseId': purchaseId
                }

                $.ajax({
                    data: formData,
                    url: "search.htm",
                    type: 'GET',
                    success: function (data, textStatus, jqXHR) {
                        //clear table
                        var response = JSON.parse(data);

                        if (response.status === "success") {
                            $('#search_result').show();
                            resultTable.fnClearTable();

                            //set data table data
                            var tabledata = response.search_result;
                            $(tabledata).each(function (index) {
                                var item = tabledata[index];
                                var requestBtn = "";
                                if (item.isRequested) {
                                    requestBtn = "<span class='badge badge-warning'>Already Requested</span>";
                                } else {
                                    requestBtn = "<span onclick='openModal(\"" + item.purchase_id + "\", \"" + item.txn_id + "\")' class='btn btn-success' title='Request'>Request</span>";
                                }

                                var row = [
                                    item.purchase_id,
                                    item.serial_number,
                                    item.lottery_name,
                                    item.draw_date,
                                    item.pur_date,
                                    item.nic,
                                    item.winning_amount,
                                    item.txn_id,
                                    item.status,
                                    requestBtn
                                ];
                                resultTable.fnAddData(row);
                            });
                        }


                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }
        }

        function checkIsItCanDelete() {
            var formData = {
                'id': $('#new_form #id').val()
            }
            $.ajax({
                data: formData,
                type: 'GET',
                url: "getPermissionByWallet.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    if (response.status === 'success') {
                        updateItem();
                    } else {
                        showNotification(response.status, response.msg);
                    }
                    search();

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }

        function saveRequest() {

            var formData = {};

            if ($('#newStatus').val() === "20") {

                if ($('#firstStatement').val() === "" || $('#secondStatement').val() === "" || $('#remark').val() === "") {
                    showNotification("error", "Please fill all the feilds");
                    return;
                } else if (new Date($('#firstStatement').val()) >= new Date($('#secondStatement').val())) {
                    showNotification("error", "First statement date should be less than the second statement date");
                    return;
                } else {

                    if ($('#firstStatement').val() === $('#secondStatement').val()) {
                        showNotification("error", "First statement and second statement cannot be same!");
                        return;
                    }

                    formData = {
                        "statementStatus": $('#newStatus').val(),
                        "firstStatement": $('#firstStatement').val(),
                        "secondStatement": $('#secondStatement').val(),
                        "remark": $('#remark').val(),
                        "purchaseId": $('#id').val(),
                        "txnId": $('#txnId').val()
                    };
                }
            } else if ($('#newStatus').val() === "25") {
                if ($('#firstStatement').val() === "" || $('#remark').val() === "") {
                    showNotification("error", "Please fill all the feilds");
                    return;
                } else {
                    formData = {
                        "statementStatus": $('#newStatus').val(),
                        "firstStatement": $('#firstStatement').val(),
                        "remark": $('#remark').val(),
                        "purchaseId": $('#id').val(),
                        "txnId": $('#txnId').val()
                    };
                }
            } else if ($('#newStatus').val() === "0") {
                showNotification("error", "Please fill all the feilds");
                return;
            }


            $.ajax({
                data: formData,
                type: 'POST',
                url: "request.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#addNew').modal('toggle');
                    }
                    //show msg
                    showNotification(response.status, response.msg);
                    //refresh table
                    search();
                    resetNewFrom();
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }


        function openModal(id, txnId) {
            $('#new_form #txnId').val(txnId);
            $('#new_form #id').val(id);
            $('#new_form #id').hide();
            $('#new_form #txnId').hide();
            $('#addNew').modal('toggle');
        }


        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        function resetNewFrom() {
            var form = $('#new_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }

        function closeModal() {
            $('#addNew').modal('toggle');
        }


    </script>
</html>
