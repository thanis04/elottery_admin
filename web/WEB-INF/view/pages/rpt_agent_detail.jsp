<%-- 
    Document   : rpt_agent_detail
    Created on : May 20, 2021, 11:51:54 AM
    Author     : nipuna_k
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
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
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left" >
                                <h4>Sub Agents Report</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" class="form-horizontal" id="search_form" role="form">

                                            <div class="form-group">
                                                <label class="">Sub-Agent ID</label>
                                                <input  type="text" class="form-control" id="emp_id" name="emp_id" placeholder="Enter sub-agent id" />
                                            </div>

                                            <div class="form-group">
                                                <label class="">Sub-Agent Name</label>
                                                <input  type="text" class="form-control" id="emp_name" name="emp_name" placeholder="Enter sub-agent name" />
                                            </div>

                                            <div class="form-group" align="right">
                                                <button  type="button" class="btn btn-secondary" onclick="reset();">Reset</button>
                                                <button type="button" class="btn submit" onclick="loadTable();">Search</button>   
                                                <button type="button" class="btn btn-info" onclick="generateFile()">Download As Excel</button>
                                            </div>
                                            <br>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="search_result">
                            <div class="x_title" align="center">
                                <h4>Report Data</h4>                                        
                                <div class="clearfix"></div>
                            </div>
                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Sub-Agent ID</th>
                                        <th>Sub-Agent Name</th>
                                        <th>Contact No</th>
                                        <th>Email</th>
                                        <th>Created Date</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <jsp:include page="../common/footer.jsp"/>
            </div>
        </div>
    </body>
    <script>

        var resultTable = "";
        var moreData = "";
        $(document).ready(function () {

            //datatable
            $('#search_result').hide();
//            resultTable = $('#result_table').dataTable({
//                "searching": false
//            });
            resultTable = null;
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

        function loadTable() {

            var section = "";
            var empID = $('#emp_id').val();
            var empName = $('#emp_name').val();

            if (empID === "" || empID === null) {
                empID = "-";
            }

            if (empName === "" || empName === null) {
                empName = "-";
            }

            if (resultTable !== null) {
                resultTable.destroy();
                resultTable = null;
            }
            $('#search_result').show();

            resultTable = $('#result_table').DataTable
                    ({

                        "ajax":
                                {
                                    url: "show_data.htm",
                                    data: {
                                        "empId": empID,
                                        "empName": empName
                                    },
                                    type: "GET",
                                    error: function (xhr, status, error)
                                    {
                                        alert(status);
                                    }
                                },
                        "paging": true,
                        "serverSide": true,
                        "processing": true,
                        "order": [],
                        "search": false,
                        "header": true,
                        "columns":
                                [
                                    {"title": "Sub-Agent ID", "data": "emp_id", "defaultContent": "-"},
                                    {"title": "Sub-Agent Name", "data": "emp_name", "defaultContent": "-"},
                                    {"title": "Contact No", "data": "emp_contactno", "defaultContent": "-"},
                                    {"title": "Email", "data": "emp_email", "defaultContent": "-"},
                                    {"title": "Created Date", "data": "emp_created", "defaultContent": "-"},
                                    {"title": "Status", "data": "emp_status", "defaultContent": "-"}
                                ]
                    });

        }

        function generateFile() {

            var empID = $('#emp_id').val();
            var empName = $('#emp_name').val();

            if (empID === "" || empID === null) {
                empID = "-";
            }

            if (empName === "" || empName === null) {
                empName = "-";
            }
            window.open("download_excel.htm?empId=" + empID + "&empName=" + empName);

        }
    </script>
</html>
