<%-- 
    Document   : user_review_req
    Created on : Aug 18, 2021, 1:26:19 PM
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
                                <h4>Account Deletion Request</h4>
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
                                                <label>NIC</label>
                                                <input maxlength="20" class="form-control" type="text" name="nic" id="nic" placeholder="Enter NIC" />
                                            </div>

                                            <div class="form-group">
                                                <label>Mobile No</label>                                                  
                                                <input maxlength="100" class="form-control" type="text" name="mobileNo" id="mobileNo" placeholder="Enter Mobile No" />
                                            </div>

                                            <div class="form-group">
                                                <!--<button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>-->
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
                                        <th>Full Name</th>
                                        <th>Username</th>                                                          
                                        <th>NIC</th>
                                        <th>Mobile Number</th>
                                        <th>Account Created Date</th>
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
                                    <h4 id="window_title" class="modal-title">Request Deletion</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">
                                    <!-- model body -->
                                    <input data-validation="required" class="form-control" type="text" name="id" id="id"  maxlength="29"/>
                                    <div class="form-group col-lg-12">
                                        <label>Customer Name</label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="name" id="name"  maxlength="29"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >NIC No</label>
                                        <div class="">
                                            <input data-validation="required-nic" class="form-control" type="text" name="nic" id="nic" maxlength="15"/>
                                        </div>
                                    </div>                                 

                                    <div class="form-group col-lg-6">
                                        <label >Contact No</label>
                                        <div class="">
                                            <input maxlength="10" data-validation="required-tel" class="form-control" type="text" name="contactno" id="contactno" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-12">
                                        <label >Remark<span class="text-red"> *</span> 
                                        </label><span class="text-secondary" style="font-size: 12px"> (Please specify who requested the account deletion)</span> 
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="remark" id="remark" maxlength="256"/>
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
                                    <button id="btn_update" type="button" class="btn btn-primary" onclick="checkIsItCanDelete()"> Submit </button>
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
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });

        });

        function reset() {
            $('#search_result').hide();
            var form = $('#search_form');
            resetFrom(form);
            resultTable.fnClearTable();
        }
        //search function
        function search() {

            if ($('#nic').val() === "" && $('#mobileNo').val() === "") {
                showNotification('error', "Please Fill Atleast One Field");
            } else {
                var nic = $('#nic').val();
                var mobileNo = $('#mobileNo').val();
                if ($('#nic').val() === "") {
                    nic = "-";
                }
                if ($('#mobileNo').val() === "") {
                    mobileNo = "-";
                }

                var formData = {
                    'nic': nic,
                    'mobileNo': mobileNo
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
                                    requestBtn = "<span onclick='editItem(\"" + item.id + "\")' class='fa fa-trash btn btn-success' title='Request Deletion'></span>";
                                }

                                var row = [item.first_name + " " + item.last_name, item.username, item.nic, item.mobile_no, item.create_time, requestBtn];
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

        function updateItem() {
            let length = $('#new_form #remark').val().length;
            if ($('#new_form #remark').val() === "") {
                showNotification('error', "Please specify who requested the deletion");
            } else if (length > 250) {
                showNotification('error', "Text is too long");
            } else {
                var formData = {
                    'remark': $('#new_form #remark').val(),
                    'id': $('#new_form #id').val()
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
        }


        function editItem(id) {
            resetNewFrom();
            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'GET',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#addNew').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#new_form #name').val(record.name);
                        $('#new_form #name').attr('disabled', true);
                        $('#new_form #nic').val(record.nic);
                        $('#new_form #nic').attr('disabled', true);
                        $('#new_form #contactno').val(record.mobileNo);
                        $('#new_form #contactno').attr('disabled', true);
                        $('#new_form #id').val(record.id);
                        $('#new_form #id').hide();
                        //enable/disable ui elemnts
                        $('#btn_save').hide();
                        $('#btn_update').show();
//                        $("#window_title").html("Edit Employee Details:" + record.name)
                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }


        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        function resetNewFrom() {
            $('#new_form #remark').val("");
        }

        function closeModal() {
            $('#addNew').modal('toggle');
        }


    </script>
</html>


