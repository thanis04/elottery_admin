<%-- 
    Document   : user_role_search
    Created on : May 25, 2021, 5:27:56 PM
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
    </head>

    <style>
        .modal-dialog{
            overflow-y: initial !important
        }
        .modal-user-view{
            height: 800px;
            overflow: auto;
            margin: 40px;
        }

    </style>
    
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
                                <h4>User Role Search</h4>
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
                                                            <!--                                                            <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>-->
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
                        <div class="modal-user-view">                       
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header bg-white">
                                    <button type="button" class="close " data-dismiss="modal">&times;</button>
                                    <h4 id="window_title" class="modal-title">Privileges</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">

                                    <div class="row" id="det"></div>

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
                        var viewBtn = "<span onclick='viewItem(\"" + item.userrolecode + "\")' class='btn btn-primary' title='View Privileges'>View Privileges</span>";
                        var row = [item.userrolecode, item.description, item.status, viewBtn];
                        resultTable.fnAddData(row);
                    });
                }


            },
            error: function (jqXHR, textStatus, errorThrown) {

            }

        });
    }

    function viewItem(userRoleCode) {
        $('#addNew').modal().show();
        var formData = {
            "userRoleCode": userRoleCode
        }

        $.ajax({
            data: formData,
            url: "view_privilege.htm",
            type: 'GET',
            success: function (data, textStatus, jqXHR) {
                var response = JSON.parse(data);
                if (response.status === true) {
                    var totalHtml = "";
                    $(response.search_result).each(function (index) {
                        var row = response.search_result[index];
                        totalHtml = totalHtml + "<div style='float:left; overflow-y: auto;height: 300px; bottom-padding:10px; margin: 10px;' class='col-md-2' ><ul class='list-group'><li style='background-color:#F8ED9C' class='list-group-item'><b>" + row.section + "</b></li>"
                        $(row.page).each(function (index) {
                            var row1 = row.page[index];
                            totalHtml = totalHtml + '<li style="box-sizing: border-box; background-color:#F2EDC4" class="list-group-item col-5">' + row1.page_name + '</li>'
                        });
                        totalHtml = totalHtml + "</ul></div>";
                        $('#det').html(totalHtml);
                    });
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
        var form = $('#new_form');
        $('#new_form #userrolecodesave').prop('readonly', false);
        $('#new_form *').attr('disabled', false);
        resetFrom(form);
    }

</script>  
</html>
