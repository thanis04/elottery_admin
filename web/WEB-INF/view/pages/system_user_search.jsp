<%-- 
    Document   : system_user_search
    Created on : May 25, 2021, 4:17:26 PM
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

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left">
                                <h4>System User Search</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search System User</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">
                                            <div class="">
                                                <div class="">
                                                    <div class="form-group">
                                                        <label class="">Username</label>
                                                        <div class="">
                                                            <input maxlength="50" class="form-control" type="text" name="username" id="username" placeholder="Enter Username" />
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <label class="">User Role</label>
                                                        <div class="">                                                    
                                                            <select data-validation="required" type="text" class="form-control" name="userRoleCode" id="userRoleCode"  >
                                                                <option selected value="0">--Select Role--</option>
                                                                <c:forEach var="item" items="${user_role_select_box}">
                                                                    <option value="${item.userrolecode}">${item.description}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <div class="">
                                                            <div class="add-button">
                                                                <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                                <button type="button" class="btn submit" onclick="search();">Search</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <br/>

                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row" id="count_modal">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">
                                    <center>
                                        <span id="tot_users"></span> 
                                        <span id="act_users"></span> 
                                        <span id="inact_users"></span>
                                    </center>

                                    <div class="">
                                        <div class="add-button">
                                            <button id="view_privilege" type="button" class='btn btn-primary'  onclick="getPrivillages()" data-toggle="modal" data-target="#addNew">View Privilege</button>
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
                                        <th>User Name</th>
                                        <th>Employee Name</th>                                                          
                                        <th>User Role</th>                                                           
                                        <th>Status</th>
                                        <!--<th>Action</th>-->
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

                                    <div class="row" id="det" ></div>

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


        var resultTable;
        $(document).ready(function () {

            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            //hide update button
            $('#btn_update').hide();
            $('#count_modal').hide();
            $("#tot_users").hide();
            $("#act_users").hide();
            $("#inact_users").hide();
            $("#view_privilege").hide();
        });

        //search function
        function search() {
            $('#count_modal').hide();
            $("#tot_users").hide();
            $("#act_users").hide();
            $("#inact_users").hide();
            $("#view_privilege").hide();

            var formData = {
                "username": $('#username').val(),
                "userRoleCode": $('#userRoleCode').val()
            }

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
                            var row = [item.username, item.employee, item.userRole, item.status];
                            resultTable.fnAddData(row);
                        });
                        $('#count_modal').show();
                        $("#tot_users").html("<h4><b class='badge badge-success' style='font-size: 18px;'>Total System Users : " + response.total_count + "</b></h4>");
                        $("#act_users").html("<b style='font-size: 18px;'>Active " + $('#userRoleCode').find(":selected").text() + " Users : " + response.active_count + "</b> <br>");
                        $("#inact_users").html("<b style='font-size: 18px;'>Inactive " + $('#userRoleCode').find(":selected").text() + " Users : " + response.inactive_count + "</b> <br>");

                        $("#tot_users").show();
                        if ($('#userRoleCode').val() !== '0') {
                            $("#act_users").show();
                            $("#inact_users").show();
                            $("#view_privilege").show();
                        }
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function getPrivillages() {
            var formData = {
                "userRoleCode": $('#userRoleCode').val()
            }

            $.ajax({
                data: formData,
                url: "view_privilege.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);
                    if (response.status === true) {
                        console.log(response)
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

            $('#search_result').hide();

            //hide update button
            $('#btn_update').hide();
            $('#count_modal').hide();
            $("#tot_users").hide();
            $("#act_users").hide();
            $("#inact_users").hide();
            $("#view_privilege").hide();
        }

    </script>
</html>

