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
                                <h4>Lottery Item Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Lottery Item</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">


                                            <div class="form-group">
                                                <label>Lottery Item</label>
                                                <input type="text" class="form-control" id="productItem"  name="itemCode" placeholder="Enter lottery item name" />
                                            </div>

                                            <div class="form-group">
                                                <label>Lottery Description</label>
                                                <input maxlength="100" type="text" class="form-control" id="description" name="description" placeholder="Enter lottery description" />
                                            </div>

                                            <div class="form-group">
                                                <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                    <button type="button" class="btn submit" onclick="searchProduct();">Search</button>
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
                                        <th>Product Code</th>
                                        <th>Description</th>                                                          
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
                        <form class="form-horizontal" id="new_form" enctype="multipart/form-data">
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header bg-white">
                                    <button type="button" class="close " data-dismiss="modal">&times;</button>
                                    <h4 id="window_title" class="modal-title">Add New Lottery Item</h4>
                                    <hr class="hr" />
                                </div>
                                <div class="modal-body">
                                    <!-- model body -->
                                    <div class="row">
                                        <div class="">

                                            <div class="form-group col-lg-6" >
                                                <label class="required-input"> Item<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input maxlength="5" data-validation="required" type="text" class="form-control" id="itemCode"  name="itemCode">
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label class="required-input">Description<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input maxlength="100" data-validation="required" type="text" class="form-control" id="description"  name="description">
                                                </div>
                                            </div>

                                            <!--                                            <div class="form-group">
                                                                                            <label class="control-label col-sm-5">Product Icon:<span class="text-danger"> *</span></label>
                                                                                            <div class="col-sm-7">
                                                                                                <input data-validation="required" type="file"  class="form-control" id="productIcon"  name="productIcon">
                                                                                            </div>
                                                                                        </div>-->

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
                                                <label class="required-input"></label>
                                                <div class="">


                                                </div>
                                            </div>

                                        </div>
                                        <div class="form-group col-lg-6">
                                            <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                        </div>  
                                    </div>


                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button id="btn_update" type="button" class="btn btn-primary" onclick="updateItem()">Update</button>
                                    <button id="btn_reset" type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                    <button id="btn_save" type="button" class="btn submit" onclick="saveItem()">Save</button>
                                </div>

                            </div>
                        </form>
                    </div>
                </div>
                <!-- View Modal -show selected item   -->
                <div class="modal fade" id="viewModel" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4  class="modal-title">Product : <span id="view_title"></span></h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <div class="row">
                                    <div class="col-md-8">
                                        <form class="form-horizontal" >
                                            <div class="form-group">
                                                <label class="control-label col-sm-5">Item Code:</label>
                                                <div class="col-sm-7">
                                                    <label  class="control-label" id="view_itemCode" ></label>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label col-sm-5">Description:</label>
                                                <div class="col-sm-7">                                          
                                                    <label class="control-label "  id="view_description" ></label>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label col-sm-5">Status:</label>
                                                <div class="col-sm-7">
                                                    <div class="form-group">                                                
                                                        <label class="control-label " id="view_statusCode" ></label>
                                                    </div>
                                                </div>
                                            </div>

                                        </form>
                                    </div>
                                    <div class="col-md-4">   
                                        <div id="view_icon" style="border: gainsboro solid thin;padding: 2px;">

                                        </div>

                                    </div>
                                </div>



                            </div>
                            <div class="modal-footer">                              
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
            $('#new_form #btn_reset').show();
            $('#btn_save').show();
            $('#new_form #itemCode').prop('readonly', false);
            $("#window_title").html("Add New Lottery Item")
            resetNewFrom();




        }

        //search function
        function searchProduct() {
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
                            var editBtn = "<span onclick='editItem(\"" + item.itemCode + "\")' class='fa fa-pencil btn btn-success' title='Edit Product Item'></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.itemCode + "\")' class='fa fa-trash-o btn btn-danger' title='Delete Product Item'></span>";

                            var row = [item.itemCode, item.description, item.status, editBtn + " " + delBtn];
                            resultTable.fnAddData(row);
                        });
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }

        function saveItem() {
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


                        searchProduct();
                        //show msg
                        showNotification(response.status, response.msg);



                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }

        }

        function deleteItem(id) {

            //delete function
            function deleteFunction() {
                var dataString = "code=" + id;
                $.ajax({
                    data: dataString,
                    type: 'POST',
                    url: "delete.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //show msg
                        showNotification(response.status, response.msg);

                        //refresh table
                        searchProduct();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }
                });

            }
            //confirm box -IF 'YES' -> call deleteFunction
            showConfirmMsg("Delete Product Item", "Are you sure  to delete product item:" + id + "?", deleteFunction)
        }


        function editItem(code) {
            var dataString = "code=" + code;

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
                        $('#new_form #itemCode').val(record.itemCode);
                        $('#new_form #description').val(record.description);
                        $('#new_form #statusCode').val(record.status);


                        //set icon
//                        if (record.icon === null) {//check is null
//                            //set default image                           
//                            $('#view_update_icon').html("<img src='<c:url value="/resources/images/no_img_smal.jpg"></c:url>' />");
//                        } else {
//                            //load real image
//                            $('#view_update_icon').html("<img width='163px' height='200px' src='data:image/jpeg;base64," + record.icon + "' />");
//                        }

                        //set read only productCode
                        $('#new_form #itemCode').prop('readonly', true);
                        $('#btn_save').hide();
                        $('#btn_update').show();
                        $('#new_form #btn_reset').hide();
                        $('#view_update_icon').show();
                        $("#window_title").html("Edit Product Item Details")



                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


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
                        searchProduct();




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
            resetFrom(form);
        }

    </script>
</html>
