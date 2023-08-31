<%-- 
    Document   : employee_search
    Created on : May 25, 2021, 1:55:54 PM
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
                                <h4>Employee Search</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Employee</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">
                                            <div class="form-group">
                                                <label>Employee ID</label>
                                                <input maxlength="10" class="form-control" type="text" name="employeeid" id="employeeid" placeholder="Enter employee ID" />
                                            </div>

                                            <div class="form-group">
                                                <label>Name</label>                                                  

                                                <input maxlength="100" class="form-control" type="text" name="name" id="name" placeholder="Enter employee name" />

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
                                        <th>Employee Code</th>
                                        <th>Name</th>                                                          
                                        <th>Address</th>
                                        <th>Contact No</th>
                                        <th>E-mail</th>
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
                                    <h4 id="window_title" class="modal-title">Add New Employee List</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">
                                    <!-- model body -->

                                    <div class="form-group col-lg-6">
                                        <label >Employee ID<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input  data-validation="required" class="form-control" type="text" name="employeeid" id="employeeid" maxlength="15" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>Employee Name<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="name" id="name"  maxlength="29"/>
                                        </div>
                                    </div>                                         
                                    <div class="form-group col-lg-6">
                                        <label >NIC No<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required-nic" class="form-control" type="text" name="nic" id="nic" maxlength="15"/>
                                        </div>
                                    </div>                                         
                                    <div class="form-group col-lg-6">
                                        <label >Address<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="address" id="address" maxlength="256"/>
                                        </div>
                                    </div>                                         
                                    <div class="form-group col-lg-6">
                                        <label >Email<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input onblur="isEmail(this.value)" maxlength="64" data-validation="required-email" class="form-control" type="email" name="email" id="email" /> 
                                        </div>
                                    </div>                                         
                                    <div class="form-group col-lg-6">
                                        <label >Contact No<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input maxlength="10" data-validation="required-tel" class="form-control" type="text" onkeydown="isNumberOnly(event)" name="contactno" id="contactno" />
                                        </div>
                                    </div>   

                                    <input  type="hidden" name="tokenPublicKey" id="tokenPublicKey"/>
                                    <!--<input data-validation="required-sp-allow" type="hidden" name="tokenPublicKey" id="tokenPublicKey"/>-->

                                    <div class="form-group col-lg-6">
                                        <label >Status<span class="text-red"> *</span></label>
                                        <div class="">
                                            <div class="form-group">
                                                <div class="selectContainer">
                                                    <select data-validation="required" class="form-control" name="dlbStatus.statusCode" id="statusCode">
                                                        <option selected value="0">--Select--</option>
                                                        <option value="<%=SystemVarList.ACTIVE%>">Active</option>
                                                        <option value="<%=SystemVarList.INACTIVE%>">Inactive</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">

                                    </div>




                                    <div class="row">
                                        <div class="form-group col-lg-6">
                                            <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                        </div>    
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button id="btn_close" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button id="btn_update" type="button" class="btn btn-primary" onclick="updateItem()"> Update </button>
                                    <button id="btn_reset"type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                    <button id="btn_save" type="button" class="btn submit " onclick="saveItem()"> Add </button>
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
        var publicKey = "";
        var actionType = "";

        function getPublicKey(type) {

            var tokenPublicKey = $("#tokenPublicKey").val();

            if (tokenPublicKey === '') {
                publicKey = "";
                actionType = "";
                actionType = type;

                var val = scanner.isConnectedToScanWebSocket();

                if (val) {

                    scanner.getPublicKey(responseHandle,
                            {
                                "req_param":
                                        {
                                            "param": "user1"
                                        }
                            }
                    );

                } else {

                    scanner.displayInstallScanAppEnableJavaPopup(true);

                    $.notify({
                        icon: 'fa fa-times-circle',
                        message: 'Please start the local service.'
                    }, {
                        type: 'danger',
                        z_index: 9999
                    });

                }

                        scanner.getPublicKey(responseHandle,
                                {
                                    "req_param":
                                            {
                                                "param": "user1"
                                            }
                                }
                        );

                if (actionType === 'SAVE') {

                    if (validateForm($('#new_form'))) {
                        saveItem();
                    }



                } else if (actionType === 'UPDATE') {

                    if (validateForm($('#new_form'))) {
                        updateItem();
                    }


                }

                        $.notify({
                            icon: 'fa fa-times-circle',
                            message: 'Please start the local service.'
                        }, {
                            type: 'danger',
                            z_index: 9999
                        });

                    
                }
             else {
                showNotification('error', 'Token data getting fail. Make sure eToken is connected or local service is started');
            }





        }


        function responseHandle(successful, mesg, response) {

            if (!successful) { // On error
                $.notify({
                    icon: 'fa fa-times-circle',
                    message: "Error. " + mesg
                }, {
                    type: 'danger',
                    z_index: 9999
                });
                return;
            }

            if (successful && mesg !== null) {

                var response = $.parseJSON(mesg);

                if (response.STATUS) {
                    publicKey = response.param.publickey;
                    $('#tokenPublicKey').val(publicKey);

                    //call save or update function
                    if (actionType === 'SAVE') {

                        if (publicKey !== '') {

                            if (validateForm($('#new_form'))) {
                                saveItem();
                            }

                        } else {
                            showNotification('error', 'Token data getting fail. Make sure eToken is connected or local service is started');
                        }



                    } else if (actionType === 'UPDATE') {

                        if (publicKey !== '') {

                            if (validateForm($('#new_form'))) {
                                updateItem();
                            }

                        } else {
                            showNotification('error', 'Token data getting fail. Make sure eToken is connected or local service is started');
                        }


                    }

                } else {

                    $.notify({
                        icon: 'fa fa-times-circle',
                        message: response.MSG
                    }, {
                        type: 'danger',
                        z_index: 9999
                    });

                }

                return;
            }

        }
//( document ).ready() block.
        var resultTable;
        $(document).ready(function () {

            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false,
                 "paging": true
            });

            //hide update button
            $('#btn_update').hide();

        });

        function hideShowButton() {
            //hide/show button
            $('#btn_update').hide();
            $('#view_update_icon').hide();
            $('#btn_save').show();
            $('#new_form #product').prop('readonly', false);
            $('#new_form #btn_reset').show();
            $("#window_title").html("Add New Employee List")
            resetNewFrom();




        }

        //search function
        function search() {
            var formData = $('#search_form').serialize();

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
                            var viewBtn = "<span onclick='viewItem(\"" + item.employeeid + "\")' class='fa fa-eye btn btn-primary' title='View Employee'></span>";
//                            var editBtn = "<span onclick='editItem(\"" + item.employeeid + "\")' class='fa fa-pencil btn btn-success' title='Edit Employee'></span>";
//                            var delBtn = "<span onclick='deleteItem(\"" + item.employeeid + "\")' class='fa fa-trash-o btn btn-danger' title='Delete Employee'></span>";

                            var row = [item.employeeid, item.name, item.address, item.contactno, item.email, item.status, viewBtn ];
                            resultTable.fnAddData(row);
//                            + " " + editBtn + " " + delBtn
                        });
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }
//these lines are commneted for Bypass token
//        function saveItem() {
//            if (publicKey !== "") {
//                var formData = $('#new_form').serialize();
//
//
//                if (validateForm($('#new_form'))) {
//                    $.ajax({
//                        data: formData,
//                        type: 'POST',
//                        url: "save.htm",
//                        success: function (data, textStatus, jqXHR) {
//                            var response = JSON.parse(data);
//
//                            //hide window if record is ok
//                            if (response.status === 'success') {
//                                $('#addNew').modal('toggle');
//                            }
//
//                            search();
//
//                            //show msg
//                            showNotification(response.status, response.msg);
//
//
//
//                        },
//                        error: function (jqXHR, textStatus, errorThrown) {
//
//                        }
//
//                    });
//                }
//            } else {
//                //show msg
//                showNotification("Token", "Token data getting fail. Make sure eToken is connected or local service is started");
//            }
//
//
//        }
//        function updateItem() {
//            var formData = $('#new_form').serialize();
//
//            if (validateForm($('#new_form'))) {
//                $.ajax({
//                    data: formData,
//                    type: 'POST',
//                    url: "update.htm",
//                    success: function (data, textStatus, jqXHR) {
//                        var response = JSON.parse(data);
//
//                        //hide window if record is ok
//                        if (response.status === 'success') {
//                            $('#addNew').modal('toggle');
//                        }
//
//                        //show msg
//                        showNotification(response.status, response.msg);
//
//                        //refresh table
//                        search();
//
//
//
//
//                    },
//                    error: function (jqXHR, textStatus, errorThrown) {
//
//                    }
//
//                });
//            }
//
//        }
        function saveItem() {
            if (validateForm($('#new_form'))) {
                var formData = $('#new_form').serialize();


                if (validateForm($('#new_form'))) {
                    $.ajax({
                        data: formData,
                        type: 'POST',
                        url: "save.htm",
                        success: function (data, textStatus, jqXHR) {
                            var response = JSON.parse(data);

                            //hide window if record is ok
                            if (response.status === 'success') {
                                $('#addNew').modal('toggle');
                            }

                            search();

                            //show msg
                            showNotification(response.status, response.msg);



                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }

                    });
                }
                
             
            } 


        }
        function updateItem() {
            var formData = $('#new_form').serialize();

            if (validateForm($('#new_form'))) {
                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "update.htm",
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


        function editItem(id) {
            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#addNew').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#new_form #employeeid').val(record.employeeid);
                        $('#new_form #name').val(record.name);
                        $('#new_form #nic').val(record.nic);
                        $('#new_form #address').val(record.address);
                        $('#new_form #email').val(record.email);
                        $('#new_form #contactno').val(record.contactno);
                        $('#new_form #statusCode').val(record.status);


                        //enable/disable ui elemnts
                        $('#new_form *').attr('disabled', false);
                        $('#new_form #employeeid').prop('readonly', true);
                        $('#btn_save').hide();
                        $('#btn_update').show();
                        $('#new_form #btn_reset').hide();
                        $("#window_title").html("Edit Employee Details:" + record.name)



                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }
        function viewItem(id) {
            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#addNew').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#new_form #employeeid').val(record.employeeid);
                        $('#new_form #name').val(record.name);
                        $('#new_form #nic').val(record.nic);
                        $('#new_form #address').val(record.address);
                        $('#new_form #email').val(record.email);
                        $('#new_form #contactno').val(record.contactno);
                        $('#new_form #statusCode').val(record.status);


                        //set read only inputs
                        $('#new_form *').attr('disabled', true);



                        $('#btn_save').hide();
                        $('#btn_update').hide();
                        $('#btn_reset').hide();
                        $("#window_title").html("Employee Details:" + record.name)


                        $('#btn_close').attr('disabled', false);
                        $('#btn_small_close').attr('disabled', false);


                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }
        function deleteItem(id) {

            //delete function
            function deleteFunction() {
                var dataString = "id=" + id;
                $.ajax({
                    data: dataString,
                    type: 'POST',
                    url: "delete.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //show msg
                        showNotification(response.status, response.msg);

                        //refresh table
                        search();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }
                });

            }
            //confirm box -IF 'YES' -> call deleteFunction
            showConfirmMsg("Delete Employee", "Are you sure to delete employee: " + id + "?", deleteFunction)
        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        function resetNewFrom() {
            var form = $('#new_form');
            $('#new_form #employeeid').prop('readonly', false);
            $('#new_form *').attr('disabled', false);
            resetFrom(form);
        }

        function closeModal() {
            $('#addNew').modal('toggle');
        }






    </script>
</html>

