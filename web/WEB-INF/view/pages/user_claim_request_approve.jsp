<%-- 
    Document   : user_claim_request_approve
    Created on : Sep 16, 2021, 12:28:15 PM
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
                                <h4>User Claim Request Approve</h4>
                            </div>

                        </div>
                        <br>
                        <br>
                        <div class="clearfix"></div>


                        <div id="search_result">

                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Ticket Id (Purchase history ID)</th>
                                        <th>Serial No</th>
                                        <th>NIC</th>
                                        <th>Winning Amount</th>
                                        <th>Transaction Id</th>
                                        <th>Current Status</th>
                                        <th>Requested Status</th>
                                        <th>Requested By</th>
                                        <th>First Statement Checked Date</th>
                                        <th>Second Statement Checked Date</th>
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
                                    <h4 id="window_title" class="modal-title">Approve</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">
                                    <!-- model body -->
                                    <input class="form-control" 
                                           type="text" name="id" id="id"  maxlength="29"/>
                                    <div class="form-group col-lg-12">
                                        <label>Current Status</label>
                                        <div class="">
                                            <input class="form-control" type="text" 
                                                   name="current_status" id="current_status"  maxlength="29"/>
                                        </div>
                                    </div>
                                    
                                    <div class="form-group col-lg-12">
                                        <label>Remark</label>
                                        <div class="">
                                            <textarea class="form-control" type="text" 
                                                      name="remarkArea" id="remarkArea"  maxlength="29"></textarea>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >New Status Requested</label>
                                        <div class="">
                                            <input class="form-control" type="text" 
                                                   name="new_status" id="new_status" maxlength="15"/>
                                        </div>
                                    </div>                                 

                                    <div class="form-group col-lg-6">
                                        <label >Requested by</label>
                                        <div class="">
                                            <input maxlength="10" class="form-control" 
                                                   type="text" name="reqBy" id="reqBy" />
                                        </div>
                                    </div>


                                    <input  type="hidden" name="tokenPublicKey" id="tokenPublicKey"/>
                                    <!--<input data-validation="required-sp-allow" type="hidden" name="tokenPublicKey" id="tokenPublicKey"/>-->

                                    <div class="row">
                                        <div class="form-group col-lg-6">
                                            <span class="pull-left text-secondary"> </span>

                                        </div>    
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button id="btn_close" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button id="btn_update" type="button" class="btn btn-primary" onclick="approve()"> Approve </button>
                                    <button id="btn_update" type="button" class="btn btn-primary" onclick="goingToReject()"> Reject </button>
                                </div>
                            </div>
                        </form>  
                    </div>
                </div>

                <div class="modal fade" id="rejectModal" role="dialog">
                    <div class="modal-dialog">   

                        <form class="form-horizontal" id="reject-form" >
                            <!-- Modal content-->
                            <div class="modal-content">

                                <div class="modal-header bg-white">
                                    <button id="btn_small_close" type="button" class="close " data-dismiss="modal">&times;</button>
                                    <h4 id="window_title" class="modal-title">Feedback Details</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">
                                    <div class="form-group col-lg-12">
                                        <label>Feedback<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input class="form-control" type="text" 
                                                   name="feedback" id="feedback" 
                                                   placeholder="Enter Feedback" 
                                                   maxlength="29"/>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="form-group col-lg-6">
                                            <span class="pull-left text-secondary"> </span>
                                        </div>    
                                    </div>
                                    <div class="modal-footer">
                                        <button id="btn_close" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        <button id="btn_update" type="button" class="btn btn-primary" onclick="reject()"> Submit </button>
                                    </div>
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
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });

            search();

        });

        function reset() {
            $('#search_result').hide();
            var form = $('#search_form');
            resetFrom(form);
            resultTable.fnClearTable();
        }
        //search function
        function search() {

            $.ajax({
                url: "search_requests.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    //clear table
                    var response = JSON.parse(data);
                    console.log(response);
                    if (response.status === "success") {
                        $('#search_result').show();
                        resultTable.fnClearTable();

                        //set data table data
                        var tabledata = response.search_result;
                        $(tabledata).each(function (index) {
                            var item = tabledata[index];
                            var requestBtn = "";



                            requestBtn = "<span onclick='openModal(\"" + item.id + "\")' \n\
                    class='btn btn-success' title='Approve'>Approve</span>";
                            var row = [
                                item.purchase_id,
                                item.serial_number,
                                item.nic,
                                item.winning_amount,
                                item.txn_id,
                                item.current_status,
                                item.requested_status,
                                item.requested_by,
                                item.first_date,
                                item.second_date,
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

        function checkIsItCanDelete() {
            var formData = {
                'id': $('#new_form #id').val()
            }
            $.ajax({
                data: formData,
                type: 'GET',
                url: "getPermission.htm",
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

        function approve() {

            showConfirmMsg('Approve', 'Are you sure you want to approve this request?', fnApprove);

            function fnApprove() {

                var formData = {
                    'id': $('#new_form #id').val(),
                    'status': "APPROVED",
                    'feedback': "",
                }
                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "approveReject.htm",
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
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    }
                });
            }
        }

        function goingToReject() {
            $("#rejectModal").modal('toggle');
        }

        function reject() {

            if ($('#feedback').val() === "") {
                showNotification("error", "Please fill feedback feild");
                return;
            }

            showConfirmMsg('Reject', 'Are you sure you want to reject this request?', fnReject);

            function fnReject() {

                var formData = {
                    'id': $('#new_form #id').val(),
                    'status': "REJECT",
                    'feedback': $('#feedback').val()
                };
                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "approveReject.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //hide window if record is ok
                        if (response.status === 'success') {
                            $("#rejectModal").modal('toggle');
                            $('#addNew').modal('toggle');
                        }
                        //show msg
                        showNotification(response.status, response.msg);
                        //refresh table
                        resetRejectFrom();
                        search();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                    }
                });
            }
        }

        function openModal(id) {

            $('#new_form #id').val(id);
            $('#new_form #id').hide();
            $('#addNew').modal('toggle');

            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                url: "get.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);
                    var modalData = response.result;
                    $('#new_form #current_status').val(modalData.current_status)
                    $('#new_form #current_status').attr('disabled', true);
                    $('#new_form #remarkArea').val(modalData.remark)
                    $('#new_form #remarkArea').attr('disabled', true);
                    $('#new_form #new_status').val(modalData.requested_status);
                    $('#new_form #new_status').attr('disabled', true);
                    $('#new_form #reqBy').val(modalData.requested_by);
                    $('#new_form #reqBy').attr('disabled', true);
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function resetRejectFrom() {
            var form = $('#reject-form');
            resultTable.fnClearTable();
            resetFrom(form);
        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        function resetNewFrom() {
            $('#new_form #requested_by').val("");
        }

        function closeModal() {
            $('#addNew').modal('toggle');
        }


    </script>
</html>
