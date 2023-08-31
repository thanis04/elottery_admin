<%@page import="com.epic.dlb.util.common.SystemVarList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left">
                                <h4>Authorized Device Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>



                        <div id="search_result">
                            <br>
                            <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>
                            <br>
                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Device ID</th>
                                        <th>Description</th>
                                        <th>Mac Address</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="device" items="${device_list}">
                                        <tr>
                                            <td>${device.id}</td>
                                            <td>${device.description}</td>
                                            <td>${device.macAddress}</td>
                                            <td>${device.dlbStatus.description}</td>
                                            <td>
                                                <span onclick='viewItem(${device.id})' class='fa fa-eye btn btn-primary' title='View Device'></span>
                                                <span onclick='editItem(${device.id})' class='fa fa-pencil btn btn-success' title='Edit Device'></span>
                                            </td>
                                        </tr>
                                    </c:forEach>

                                </tbody>
                            </table>
                        </div>

                    </div>
                </div>


                <!-- /page content -->

                <!-- Add new Modal -->
                <div class="modal fade" id="addNew" role="dialog">
                    <div class="modal-dialog">
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-white">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4 id="window_title" class="modal-title">Add New Device List</h4>

                            </div>
                            <div class="modal-body">


                                <form class="form-horizontal" id="new_form" >
                                    <div class="form-group col-lg-6">
                                        <label class="required-input">ID<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input maxlength="50"  class="form-control" readonly type="text" name="id" id="id" />
                                        </div>

                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label class="required-input">Description<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input maxlength="60" data-validation="required" class="form-control" type="text" name="description" id="description" />
                                        </div>
                                    </div>
                                    <div class="form-group col-lg-6">
                                        <label class="required-input">MAC Address (Max 48) <span class="text-red"> *</span></label>
                                        <div class="">
                                            <input maxlength="48" data-validation="required-sp-allow" class="form-control" type="text" name="macAddress" id="macAddress" />
                                        </div>
                                    </div>


                                    <div class="form-group col-lg-6">
                                        <label class="required-input">Status<span class="text-red"> *</span></label>
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
                                        <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                    </div>




                                    <div class="form-group col-lg-6">
                                        <label class="required-input"></label>
                                        <div class="">

                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                <button id="btn_update" type="button" class="btn btn-primary" onclick="updateItem()"> Update </button>
                                <button id="btn_reset"type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                <button id="btn_save" type="button" class="btn submit " onclick="saveItem()"> Add </button>
                            </div>
                        </div>

                    </div>
                </div>

                <!-- footer content -->
                <jsp:include page="../common/footer.jsp"/>
                <!-- /footer content -->
            </div>
        </div>
    </body>

    <script>
//( document ).ready() block.
        var resultTable;
        $(document).ready(function () {
            //init data table
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

        });

        function hideShowButton() {
            //hide/show button
            $('#btn_update').hide();
            $('#view_update_icon').hide();
            $('#btn_save').show();
             $('#btn_reset').show();
            $('#new_form #product').prop('readonly', false);
            $("#window_title").html("Add Authorized Device ")
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
                            var viewBtn = "<span onclick='viewItem(\"" + item.id + "\")' class='fa fa-eye btn btn-primary' title='Edit Device'></span>";
                            var editBtn = "<span onclick='editItem(\"" + item.id + "\")' class='fa fa-pencil btn btn-success' title='Edit Device'></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.id + "\")' class='fa fa-trash-o btn btn-danger' title='Delete Device'></span>";

                            var row = [item.id, item.description, item.macAddress, item.status, viewBtn + " " + editBtn];
                            resultTable.fnAddData(row);
                        });
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function saveItem() {


            var text = $('#macAddress').val();




            var formData = $('#new_form').serialize();


            if (validateForm($('#new_form'))) {
                if (isMacAddress(text)) {
                    $.ajax({
                        data: formData,
                        type: 'POST',
                        url: "save.htm",
                        success: function (data, textStatus, jqXHR) {
                            var response = JSON.parse(data);

                            //hide window if record is ok
                            if (response.status === 'success') {
                                $('#addNew').modal('toggle');
                                search();
                            }

                            //show msg
                            showNotification(response.status, response.msg);



                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }

                    });
                } else {
                    showNotification('error', 'Please eneter valid mac address')
                }

            }

        }
        function updateItem() {
            var text = $('#macAddress').val();
            var formData = $('#new_form').serialize();

            if (validateForm($('#new_form'))) {

                if (isMacAddress(text)) {
                    $.ajax({
                        data: formData,
                        type: 'POST',
                        url: "update.htm",
                        success: function (data, textStatus, jqXHR) {
                            var response = JSON.parse(data);

                            //hide window if record is ok
                            if (response.status === 'success') {
                                $('#addNew').modal('toggle');
                                search();
                            }

                            //show msg
                            showNotification(response.status, response.msg);





                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }

                    });
                } else {
                    showNotification('error', 'Please eneter valid mac address')
                }

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
                        $('#new_form #id').val(record.id);
                        $('#new_form #description').val(record.description);
                        $('#new_form #macAddress').val(record.macAddress);
                        $('#new_form #statusCode').val(record.status);



                        //enable/disable ui elemnts
                        $('#new_form *').attr('disabled', false);
                        $('#new_form #employeeid').prop('readonly', true);
                        $('#btn_save').hide();
                        $('#btn_update').show();
                        $('#btn_reset').hide();
                        $("#window_title").html("Device Details:" + record.description)



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
                        $('#new_form #id').val(record.id);
                        $('#new_form #description').val(record.description);
                        $('#new_form #macAddress').val(record.macAddress);
                        $('#new_form #statusCode').val(record.status);

                        //set read only inputs
                        $('#new_form *').attr('disabled', true);


                        $('#btn_save').hide();
                        $('#btn_update').hide();
                        $('#btn_reset').hide();
                        $("#window_title").html("Device Details:" + record.description)



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
            showConfirmMsg("Delete Device", "Are you sure to delete employee: " + id + "?", deleteFunction)
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



    </script>
</html>
