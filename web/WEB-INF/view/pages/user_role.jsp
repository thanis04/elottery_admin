<%-- 
    Document   : user_role
    Created on : Oct 11, 2017, 10:50:17 AM
    Author     : ridmi_g
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
    </head>

    <body class="nav-md">
        <div class="container body">
            <div class="main_container">
                <!-- include side bar  -->
                <jsp:include page="../common/sidebar.jsp"/>
                <!-- top navigation -->
                <jsp:include page="../common/header.jsp"/>
                <!-- /top navigation -->

                <!--page content-->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left">
                                <h4>User Role Management</h4>
                            </div>     
                        </div>


                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search User Role</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">
                                            <div class="">
                                                <div class="">
                                                    <div class="form-group">
                                                        <label class="">User Role Code </label>
                                                        <input maxlength="16" class="form-control" type="text" name="userrolecode" id="userrolecode" placeholder="Enter User Role" />
                                                    </div>

                                                    <div class="form-group">
                                                        <label class="">User Role Description</label>                                                 
                                                        <input maxlength="250" class="form-control" type="text" name="description" id="description" placeholder="Enter Description" /> 
                                                    </div>

                                                    <div class="form-group">
                                                        <label class=""></label>
                                                        <div class="">
                                                            <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>
                                                            <div class="add-button">
                                                                <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                                <button type="button" class="btn submit" onclick="search();">Search</button>
                                                            </div>
                                                            <!--
                                                            <button type="button" onclick="hideShowButton()" class="btn btn-success" data-toggle="modal" data-target="#addNew">Add New</button>
                                                            -->
                                                        </div>
                                                    </div>
                                                </div>
                                                <br/>
                                        </form>
                                    </div>
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
                                    <th>User Role Code </th>
                                    <th>User Role Description</th>                                                          
                                    <th>Status</th>
                                    <th>Action</th>

                                </tr>
                            </thead>
                            <tbody>


                            </tbody>
                        </table>
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
                                <h4 id="window_title" class="modal-title">Add New User Role</h4>
                                <hr class="hr" />
                            </div>
                            <div class="modal-body">

                                <form class="form-horizontal" id="new_form" >
                                    <div class="form-group col-md-6">
                                        <label class="required-input">User Role<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input maxlength="16"  data-validation="required" class="form-control" type="text" name="userrolecode" id="userrolecodesave" />
                                        </div>


                                    </div>

                                    <div class="form-group col-md-6">
                                        <label class="required-input">User Role Description<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input maxlength="250" data-validation="required" class="form-control" type="text" name="description" id="descriptionsave" />
                                        </div>
                                    </div>                                         

                                    <div class="form-group col-md-6">
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
                                                <!--<input type="checkbox" checked data-toggle="toggle" data-on="Active" data-off="Inactive" data-width="80" name="dlbStatus.statusCode" id="statusCode" data-onstyle="warning">-->

                                            </div>
                                        </div>
                                    </div>



                                    <div class="form-group">
                                        <label class=""></label>
                                        <div class="">

                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-lg-6">
                                            <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                        </div> 
                                    </div>
                                </form>                            
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                <button id="btn_reset"type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                <button id="btn_update" type="button" class="btn btn-primary" onclick="updateItem()"> Update </button>
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
    </div>

</body>



<script>
    //( document ).ready() block.
    var resultTable;
    $(document).ready(function () {

        //init data table
        $('#search_result').hide();
        resultTable = $('#result_table').dataTable({
            "searching": false
        });

        //hide update button
        $('#btn_update').hide();


    });

    function hideShowButton() {
        //hide/show button
        $('#btn_update').hide();
        $('#view_update_icon').hide();
        $('#btn_save').show();
        $('#btn_reset').show();
        $('#new_form #userrolecodesave').prop('readonly', false);
        $("#window_title").html("Add New User Role")
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
                        var editBtn = "<span onclick='editItem(\"" + item.userrolecode + "\")' class='fa fa-pencil btn btn-success' title='Edit User role'></span>";
                        var delBtn = "<span onclick='deleteItem(\"" + item.userrolecode + "\")' class='fa fa-trash-o btn btn-danger' title='Delete User role'></span>";

                        var row = [item.userrolecode, item.description, item.status, editBtn + " " + delBtn];
                        resultTable.fnAddData(row);
                    });
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
        showConfirmMsg("Delete User Role", "Are you sure you want to delete the role '" + id + "'" + "?", deleteFunction)
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
                    $('#new_form #userrolecodesave').val(record.userrolecode);
                    $('#new_form #descriptionsave').val(record.description);
                    $('#new_form #statusCode').val(record.status);



//                    enable/disable ui elemnts
                    $('#new_form *').attr('disabled', false);
                    $('#new_form #userrolecodesave').prop('readonly', true);
                    $('#btn_save').hide();
                    $('#btn_update').show();
                    $('#btn_reset').hide();
                    $("#window_title").html("Edit User Role Details:")



                } else {
                    //show msg
                    showNotification(response.status, response.msg);
                }

            },
            error: function (jqXHR, textStatus, errorThrown) {

            }

        });


    }


    function saveItem() {
        
        $('#userrolecodesave').removeClass('error-indicate');
        $('#descriptionsave').removeClass('error-indicate');
        
        var formData = $('#new_form').serialize();

        var userrolecode = $('#userrolecodesave').val();
        var description = $('#descriptionsave').val();

        var codeCount = userrolecode.replace(/\s+/g, '').length;
        var descriptionCount = description.replace(/\s+/g, '').length;

        if (codeCount === 0 || descriptionCount === 0) {

            if (codeCount === 0) {
                $('#userrolecodesave').addClass('error-indicate');
            }

            if (descriptionCount === 0) {
                $('#descriptionsave').addClass('error-indicate');
            }


            showNotification('error', "Please enter valid data...!");
            return;
        }

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

                    //show msg
                    showNotification(response.status, response.msg);



                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }

    }
    function updateItem() {
        var formData = $('#new_form').serialize();

        //toggle value
        var status = "ACT";
        if ($("#statusCode").prop("checked")) {
            status = "ACT";

        } else {
            status = "DCT";
        }

        formData = formData + "&dlbStatus.statusCode=" + status;

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


    function resetSearchFrom() {
        var form = $('#search_form');
        resultTable.fnClearTable();
        resetFrom(form);
    }
    function resetNewFrom() {
        var form = $('#new_form');
        $('#new_form #userrolecodesave').prop('readonly', false);
        $('#new_form *').attr('disabled', false);
        resetFrom(form);
    }

</script>  
</html>
