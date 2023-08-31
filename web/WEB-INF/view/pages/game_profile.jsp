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
                                <h4>Special Game Profile Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-12 col-xs-12 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float: none;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Game Profile</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">


                                            <div class="form-group">
                                                <label>Description</label>
                                                <input maxlength="100" type="text" class="form-control" id="description" name="description" placeholder="Enter description" />
                                            </div>

                                            <div class="form-group">
                                                <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" id="btn_reset" onclick="resetSearchFrom()">Reset</button>
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
                                        <th>Name</th>                                                                                                
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
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-white">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4 id="window_title" class="modal-title">Add New Game Profile</h4>
                                <hr class="hr" />
                            </div>


                            <div class="modal-body">
                                <!-- model body -->


                                <div class="row">
                                    <div class="">

                                        <form class="form-horizontal" id="new_form" enctype="multipart/form-data">


                                            <div class="form-group col-lg-6">
                                                <label >Name<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" type="text" class="form-control" id="name"  name="name" maxlength="100">
                                                </div>
                                            </div>

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




                                            <div class="form-group">
                                                <label class="control-label col-sm-5"></label>
                                                <div class="col-sm-7">


                                                </div>
                                            </div>


                                        </form>
                                    </div>
                                    <div class="col-md-4">
                                        <div id="view_update_icon" style="border: gainsboro solid thin;padding: 2px;">

                                        </div>
                                    </div>

                                    <hr>
                                    <div class="row">
                                        <div class="col-md-3">
                                            <label >Name<span class="text-red"> *</span></label>
                                        </div>
                                        <div class="col-md-3">
                                            <label >Verification Digits<span class="text-red"> *</span></label>
                                        </div>
                                        <div class="col-md-3">
                                            <label >Status<span class="text-red"> *</span></label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-3">
                                            <input type="text" class="form-control" id="gameName"  name="gameName" maxlength="100">
                                        </div>
                                        <div class="col-md-2">
                                            <input   type="number" class="form-control" id="verificationDigits"  name="verificationDigits" maxlength="100">
                                        </div>
                                        <div class="col-md-3">
                                            <select class="form-control" id="gameStatusCode">
                                                <option selected value="0">--Select--</option>
                                                <option value="<%=SystemVarList.ACTIVE%>">Active</option>
                                                <option value="<%=SystemVarList.INACTIVE%>">Inactive</option>

                                            </select>
                                        </div>

                                        <div class="col-md-1">
                                            <button id="btn_save" type="button" class="btn submit" onclick="addItemToTable()">Add</button>
                                        </div>
                                    </div>
                                                
                                    <table class="table table-striped table-bordered bulk_action" id="item_table">
                                        <thead>
                                            <tr>                                                
                                                <th>Name</th>                                                          
                                                <th>Verification Digits</th>
                                                <th>Status</th>
                                                <th>Action</th>

                                            </tr>
                                        </thead>
                                        <tbody>


                                        </tbody>
                                    </table>

                                    <br>

                                </div>
                                <div class="form-group col-lg-6">
                                    <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                </div>   
                                <br>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>                                    
                                <button id="btn_reset" type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>                                   
                                <button id="btn_save" type="button" class="btn submit" onclick="clickSave()">Save</button>
                            </div>

                        </div>

                    </div>
                </div>
                <!-- View Modal -show selected item   -->
                <div class="modal fade" id="viewModel" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4  class="modal-title">Lottery: <span id="view_title"></span></h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <div class="row">
                                    <div class="col-md-8">
                                        <form class="form-horizontal" >
                                            <div class="form-group">
                                                <label class="control-label col-sm-5">Lottery Code:</label>
                                                <div class="col-sm-7">
                                                    <label class="control-label" id="view_productCode" ></label>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label col-sm-5">Description:</label>
                                                <div class="col-sm-7">                                          
                                                    <label maxlength="100" class="control-label "  id="view_description" ></label>
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
                                    <div class="row">
                                        <div class="col-md-4">   
                                            <div id="view_icon" style="border: gainsboro solid thin;padding: 2px;">

                                            </div>

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
        var itemTable;
        var storageBucket;
        var storageApp;
        $(document).ready(function () {

            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });


            itemTable = $('#item_table').dataTable({
                "searching": false
            });

            //hide update button
            $('#btn_update').hide();



        });

        function hideShowButton() {
            //hide/show button
            $('#btn_update').hide();
            $('#btn_get_reset').hide();
            $('#view_update_icon').hide();
            $('#btn_save').show();
            $('#new_form #btn_reset').show();
            $('#new_form #productCode').prop('readonly', false);
            $("#window_title").html("Add New Game Profile");
            $('#new_form #icon').attr('data-validation', 'required-sp-allow');
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

                            var viewBtn = "<span onclick='viewItem(\"" + item.id + "\")' class='fa fa-eye btn btn-primary' title='View Lottery'></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.id + "\")' class='fa fa-trash-o btn btn-danger' title='Delete Lottery'></span>";

                            var row = [item.name, item.status, viewBtn + " " + delBtn];
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
                    type: 'GET',
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
            showConfirmMsg("Delete Profile", "Are you sure to delete :" + id + "?", deleteFunction);
        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        function resetNewFrom() {
            var form = $('#new_form');
            resetFrom(form);
            $('#btn_save').prop('disabled', false);
        }


        function addItemToTable() {
            var gameName = $('#gameName').val();
            var verificationDigits = $('#verificationDigits').val();
            var gameStatusCode = $('#gameStatusCode').val(); 
            
            if (gameName !== "" && verificationDigits !== "" && gameStatusCode !== "0") {
                var delBtn = "<span onclick='deleteGameItem(\"" + gameName + "\")' class='fa fa-trash-o btn btn-danger' title='Remove Item'></span>";
                var row = [gameName, verificationDigits, gameStatusCode, delBtn];
                itemTable.fnAddData(row);

                $('#gameName').val("");
                $('#verificationDigits').val("");
                $('#gameStatusCode').val("0");
            } else {
                showNotification("error", "Please enter required data");
            }


        }


        function refreshPage() {
            location.reload();
        }

        function clickSave() {
            
            var nameInput = document.getElementById("name");
            var nameValue = nameInput.value;
            var statusInput = document.getElementById("statusCode");
            var statusValue = statusInput.value;
          
            var table = document.getElementById("item_table");
            var tbody = table.getElementsByTagName("tbody")[0];
            var rows = tbody.getElementsByTagName("tr");
            
            var data = [];
            
            for(var i=0; i < rows.length; i++) {
                var row = rows[i];
                var cells = row.getElementsByTagName("td");
                
                var rowData = {
                    name: cells[0].textContent,
                    verificationDigits: cells[1].textContent,
                    status: cells[2].textContent
                };
                data.push(rowData);
            }
            
            var data_set = {
                name: nameValue,
                status: statusValue,
                data: data
            };
            
            console.log(data_set);
            
            var serialized_data = JSON.stringify(data_set);
            console.log(serialized_data);
            
//            data_set.push(nameValue);
//            data_set.push(statusValue);
//            data_set.push(data);
//            console.log(data_set);
           
           
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
                    $('#btn_save').prop('disabled', false);
                    searchProduct();
                    //show msg
                    showNotification(response.status, response.msg);
                    console.log('Success response - game profile');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                }
            });

            $('#btn_save').prop('disabled', false);
            
        }






    </script>
</html>
