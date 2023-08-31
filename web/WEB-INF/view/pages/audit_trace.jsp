<%-- 
    Document   : audit_trace
    Created on : Oct 19, 2017, 11:22:20 AM
    Author     : salinda_r
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left" >
                                <h4>Audit Trace Management</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Audit Trace</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form autocomplete="off"  class="form-horizontal" id="search_form" role="form">
                                            <div class="form-group">
                                                <label class="">Activity</label>
                                                <input class="form-control" type="text" name="activity" id="activity" maxlength="50" placeholder="Enter activity" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">Username</label>                                                  
                                                <input class="form-control" type="text" name="username" id="username" maxlength="20" placeholder="Enter username"/> 
                                            </div>
                                            <div class="form-group">
                                                <label class="">IP</label>                                               
                                                <input class="form-control" type="text" name="ip" id="ip" maxlength="15" placeholder="Enter IP address" /> 
                                            </div>
                                            <div class="form-group">
                                                <label class="">From Date</label>
                                                <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select From  Date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date</label>
                                                <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select To Date" />
                                            </div>

                                            <div class="form-group" align="right">
                                                <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">Search</button>
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
                                        <th>Date</th>
                                        <th>User Name</th>
                                        <th>Activity</th>                                                          
                                        <th>IP</th>
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
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

        //search function
        function search() {
            var formData = $('#search_form').serialize();
            var ip = $('#ip').val();
            var fromDate = $('#from_date').val();
            var toDate = $('#to_date').val();

            if (checkFromDateNToDate(fromDate, toDate)) {
                $('#ip').removeClass('error-indicate');
                if (ip !== '' && !isIPAddress(ip)) {
                    showNotification('error', 'invalid IP Address');
                    $('#ip').addClass('error-indicate');
                } else {
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

                                    var row = [item.createdtime, item.username, item.activity, item.ip];
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
                showNotification('error', 'Invalid date combination');
            }






        }
// function validateSearch() {
//            var from_date = $('#from_date').val();
//            var to_date = $('#to_date').val();
//            var ip = $('#ip').val();
//            var activity = $('#activity').val();
//             var username = $('#username').val();
//            
//
//            if (ip === '' && activity === '' && username==='' && from_date === '' && to_date === '') {
//                showNotification('error', "Search criteria can't be empty");
//                return false;
//
//            }
//
//            return true;
//        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
    </script>

</html>
