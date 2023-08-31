<%-- 
    Document   : rpt_agt_user
    Created on : Feb 24, 2021, 9:44:59 AM
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
                                <h4>Customer Report</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="show_report_bydate.htm"  class="form-horizontal" id="search_form" role="form">


                                            <c:choose>
                                                <c:when test="${role == 'Sub Agents'}">
                                                    <div class="form-group">
                                                        <label class="">Agent Code</label>
                                                        <input  type="text" value="${subAgentCode}" class="form-control" id="agent_code" name="agent_code" placeholder="Enter agent code" readonly/>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="form-group">
                                                        <label class="">Agent Code<span class="text-red"> *</span></label>
                                                        <input  type="text" class="form-control" id="agent_code" name="agent_code" placeholder="Enter agent code" />
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>

                                            <div class="form-group">
                                                <label class="">From Date<span class="text-red"> *</span></label>
                                                <input  type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date<span class="text-red"> *</span></label>
                                                <input  type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>

                                            <div class="form-group" align="right">
                                                <button  type="button" class="btn btn-secondary" onclick="reset();">Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">Search</button>
                                                <!--<button type="button" class="btn print" onclick="print();">Print Report</button>-->
                                                <button type="button" class="btn btn-info" onclick="generateFile()">Download As Excel</button>
                                                <!--                                               <button type="button" class="btn print" onclick="printSummery();">Print Summary Report</button>-->
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
                                        <th>Name of the Customer</th>
                                        <th>Created Date/Time</th>
                                        <th>Last Login Date/Time</th>                                      
                                        <th>Last Transaction Date/Time</th>
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

        function search() {

            var startDate = new Date($('#from_date').val());
            var endDate = new Date($('#to_date').val());

            if (startDate > endDate) {
                showNotification('error', "Invalid Start Date and From Date");
            } else {
                var letterNumber = /^[0-9a-zA-Z]+$/;
                if ($('#agent_code').val().match(letterNumber)) {

                    if ($('#agent_code').val() === "" || $('#from_date').val() === "" ||
                            $('#to_date').val() === "") {
                        
                        showNotification('error', "Please Fill All the Fields");
                        
                    } else {
                        var formData = {
                            "agentCode": $('#agent_code').val(),
                            "fromDate": $('#from_date').val(),
                            "toDate": $('#to_date').val()
                        }

                        $.ajax({
                            data: formData,
                            url: "show_report_data.htm",
                            type: 'GET',
                            success: function (data, textStatus, jqXHR) {
                                console.log(data);
                                //clear table
                                var response = JSON.parse(data);
                                if (response.status === "success") {
                                    $('#search_result').show();
                                    resultTable.fnClearTable();

                                    //set data table data
                                    var tabledata = response.result;
                                    console.log(tabledata);
                                    $(tabledata).each(function (index) {
                                        var item = tabledata[index];

                                        var row = [item.fullName, item.createdTime,
                                            item.lastLoginTime, item.transactionDate];

                                        resultTable.fnAddData(row);
                                    });
                                } else {
                                    //show msg
                                    showNotification('error', response.msg);
                                }


                            },
                            error: function (jqXHR, textStatus, errorThrown) {

                            }

                        });
                    }
                } else {
                    showNotification('error', "Invalid Agent Code");
                }
            }

        }


        function generateFile() {
            var startDate = new Date($('#from_date').val());
            var endDate = new Date($('#to_date').val());

            if (startDate > endDate) {
                showNotification('error', "Invalid Start Date and From Date");
            } else {
                var letterNumber = /^[0-9a-zA-Z]+$/;
                if ($('#agent_code').val().match(letterNumber)) {
                    if ($('#agent_code').val() === "" || $('#from_date').val() === ""
                            || $('#to_date').val() === "") {
                        showNotification('error', "Please Fill All the Fields");
                    } else {

                        var agentCode = $('#agent_code').val();
                        var fromDate = $('#from_date').val();
                        var toDate = $('#to_date').val();

                        window.open("download_excel.htm?from_date=" + fromDate +
                                "&to_date=" + toDate + "&agent_code=" + agentCode);
                    }
                } else {
                    showNotification('error', "Invalid Agent Code");
                }
            }
        }
    </script>
</html>
