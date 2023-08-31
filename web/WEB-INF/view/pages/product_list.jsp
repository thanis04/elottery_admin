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
                                <h4>Lottery List Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-12 col-xs-12 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float: none;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Lottery</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">
                                            <div class="form-group">
                                                <label>Lottery</label>
                                                <div>
                                                    <select data-validation="required" type="text" class="form-control" name="id.productCode" id="product"  >
                                                        <option selected value="0">--Select--</option>
                                                        <c:forEach var="item" items="${product_select_box}">
                                                            <option value="${item.productCode}">${item.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label>Day</label>
                                                <div>
                                                    <select data-validation="required" type="text" class="form-control" name="id.dayCode" id="dayCode"  >
                                                        <option selected value="0">--Select--</option>
                                                        <c:forEach var="item" items="${day_select_box}">
                                                            <option value="${item.dayCode}">${item.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                    <button type="button" class="btn submit" onclick="searchProduct();">Search</button>
                                                </div>
                                            </div>
                                    </div>

                                    <br>
                                    </form>
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
                                        <th>Day Code</th>                                                          
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
                        <h4 id="window_title" class="modal-title">Add New Lottery List</h4>
                        <hr class="hr" />
                    </div>
                    <div class="modal-body">
                        <!-- model body -->
                        <form class="form-horizontal" id="new_form" >
                            <div class="form-group col-lg-6">
                                <label class="required-input">Product<span class="text-red"> *</span></label>
                                <div class="">
                                    <select data-validation="required" type="text" class="form-control" name="id.productCode" id="product"  >
                                        <option selected value="0">--Select--</option>
                                        <c:forEach var="item" items="${product_select_box}">
                                            <option value="${item.productCode}">${item.description}</option>
                                        </c:forEach>
                                    </select>
                                </div>


                            </div>

                            <div class="form-group col-lg-6">
                                <label class="required-input">Day<span class="text-red"> *</span></label>
                                <div class="">
                                    <select data-validation="required" type="text" class="form-control" name="id.dayCode" id="dayCode"  >
                                        <option selected value="0">--Select--</option>
                                        <c:forEach var="item" items="${day_select_box}">
                                            <option value="${item.dayCode}">${item.description}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>                                         

                            <div class="form-group col-lg-6">
                                <label class="required-input">Status:</label>
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
                        </form> 
                        <div class="row">
                            <div class="form-group col-lg-6">
                                <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                            </div> 
                        </div>

                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

                        <button id="btn_update" type="button" class="btn btn-primary" onclick="updateItem()">Update</button>
                        <button type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                        <button id="btn_save" type="button" class="btn submit" onclick="saveItem()">Add</button>
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
        $('#btn_save').show();
        $('#new_form #product').prop('readonly', false);
        $("#window_title").html("Add New Lottery List")
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
                       
                        var editBtn = "<span onclick='editItem(\"" + item.productCode + "\",\" " + item.day_code + "\")' class='fa fa-pencil btn btn-success' title='Edit Lottery'></span>";
                        var delBtn = "<span onclick='deleteItem(\"" + item.productCode + "\",\" " + item.day_code + "\")' class='fa fa-trash-o btn btn-danger' title='Delete Lottery'></span>";

                        var row = [item.productCode, item.day_code, item.description, item.status, editBtn + " " + delBtn];
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


    function editItem(code, day) {
        var dataString = "code=" + code + "&day=" + day;

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
                    $('#new_form #product').val(record.productCode);
                    $('#new_form #dayCode').val(record.day_code);
                    $('#new_form #statusCode').val(record.status);


                    //set read only productCode
                    $('#new_form #productCode').prop('readonly', true);
                    $('#btn_save').hide();
                    $('#btn_update').show();
                    $("#window_title").html("Edit Product List Details")



                } else {
                    //show msg
                    showNotification(response.status, response.msg);
                }

            },
            error: function (jqXHR, textStatus, errorThrown) {

            }

        });


    }
    function deleteItem(code, day) {

        //delete function
        function deleteFunction() {
            var dataString = "code=" + code + "&day=" + day;
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
        showConfirmMsg("Delete Product", "Are you sure to delete product Item" + code + "?", deleteFunction)
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
