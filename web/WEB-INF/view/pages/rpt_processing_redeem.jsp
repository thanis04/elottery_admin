<%-- 
    Document   : rpt_redemption_pending
    Created on : Feb 28, 2020, 7:54:29 AM
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
                                <h4> Redemption Processing Report</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="show_report_bydateAndCategory.htm"  class="form-horizontal" id="search_form" role="form">

                                            <div class="form-group">
                                                <label class="">From Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>

                                            <div class="form-group" align="right">
                                                <button type="reset" class="btn btn-secondary" >Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">View</button>
                                                <button type="button" class="btn btn-info" onclick="generateFile()">Download As Excel</button>
                                                <!--                                                <button type="button" class="btn print" onclick="printSummery();">Print Summary Report</button>-->
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
                                        <th>Lottery ID</th>
                                        <th>Lottery Name</th>
                                        <th>Draw No</th>
                                        <th>Draw Date</th> 
                                        <th>Customer Name</th>
                                        <th>Customer Username</th>
                                        <th>Customer NIC</th>
                                        <th>Winning Prize</th>                                       
                                        <th>Status</th>                                       
                                        <th>Last updated on</th>                                       
                                        <th>Action</th>                                       

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

    <!-- /page content -->
    <!-- View Modal -show selected item   -->
    <div class="modal fade" id="viewModal" role="dialog" data-backdrop="static" data-keyboard="false"  >
        <div class="modal-dialog">

            <!-- Modal content-->
            <div class="modal-content">
                <div class="modal-header bg-primary">
                    <button type="button" class="close " onclick="refreshPage();" data-dismiss="modal">&times;</button>
                    <h4 id="window_title" class="modal-title">Ticket File Approval</h4>
                </div>
                <div class="modal-body">
                    <!-- model body -->
                    <h4>Ticket File Details</h4>
                    <hr style="background-color: lightgray;margin-top: 0px;">
                    <div class="row">     
                        <div class="col-md-9">    
                            <input  type="hidden" id="view_id"/>
                            <div class="form-group">
                                <p class="text-secondary col-sm-5">Lottery</p>
                                <div class="col-sm-7">
                                    <label class="control-label" id="view_product" ></label>
                                </div>
                            </div>
                            <br>

                            <div class="form-group">
                                <p class="text-secondary col-sm-5">Draw No</p>
                                <div class="col-sm-7">                                          
                                    <label class="control-label "  id="view_draw_no" ></label>
                                </div>
                            </div>
                            <br>

                            <div class="form-group">
                                <p class="text-secondary col-sm-5">Draw Date</p>
                                <div class="col-sm-7">                                          
                                    <label class="control-label "  id="view_draw_date" ></label>
                                </div>
                            </div>
                            <br>
                            <div class="form-group">
                                <p class="text-secondary col-sm-5">Customer Name</p>
                                <div class="col-sm-7">                                          
                                    <label class="control-label "  id="view_customer" ></label>
                                </div>
                            </div>
                            <br>

                            <div class="form-group">
                                <p class="text-secondary col-sm-5">Current Status</p>
                                <div class="col-sm-7">
                                    <div class="form-group">                                                
                                        <label class="control-label badge bg-blue-sky" id="view_statusCode" ></label>
                                    </div>
                                </div>
                            </div>
                            <br>

                            <div class="form-group">
                                <p class="text-secondary col-sm-5">Change Status to</p>
                                <div class="col-sm-7">
                                    <div class="form-group">                                                

                                        <div class="selectContainer">
                                            <select data-validation="required" class="form-control" name="dlbStatus.statusCode" 
                                                    id="requestStatusCode">
                                                <option selected value="0">--Select--</option>
                                                <option value="25">Claimed Success</option>
                                                <option value="20">Restore to Won </option>

                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-3">

                            <div id="progessDisplay" class="c100  small">
                                <span id="progessDigit">0%</span>
                                <div class="slice">
                                    <div class="bar"></div>
                                    <div class="fill"></div>
                                </div>
                            </div>

                        </div>

                    </div>
                    <h4>Validation status</h4>
                    <hr style="background-color: lightgray;margin-top: 0px;">
                    <div class="row">
                        <div class="col-md-9">
                            <div class="form-group">
                                <p class="text-secondary col-sm-5">File name validation</p>
                                <div class="col-sm-7">
                                    <label class="control-label fa fa-2x " id="view_filname_check" ></label>
                                </div>
                            </div>
                            <br>
                            <div class="form-group">
                                <p class="text-secondary col-sm-5">Mac address validation</p>
                                <div class="col-sm-7">
                                    <label class="control-label fa fa-2x" id="mac_address_check" ></label>
                                </div>
                            </div>
                            <br>
                            <div class="form-group">
                                <p class="text-secondary col-sm-5">Hash code validation</p>
                                <div class="col-sm-7">
                                    <label class="control-label fa fa-2x" id="hash_code_check" ></label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">     
                    <button type="button" id="btn_approve" onclick="approve()" class="btn btn-success" ><span class="fa fa-check-circle"></span> Approve</button>
                    <button type="button" class="btn btn-default" onclick="refreshPage()" data-dismiss="modal">Close</button>
                </div>
            </div>

        </div>
    </div>

    <script>

        var resultTable;
        $(document).ready(function () {

            search();

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

        function search() {

            var formData = $('#search_form').serialize();
            var fromDate = $('#from_date').val();
            var toDate = $('#to_date').val();


            if (checkFromDateNToDate(fromDate, toDate)) {
                $.ajax({
                    data: formData,
                    url: "list_processing_redeem.htm",
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

                                var row = [item.id, item.productDescription, item.drawNo, item.drawDate, item.name, item.username, item.nic, item.winningPrize, item.status, item.lastUpdated, ''];
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
            } else {
                showNotification('error', 'Invalid date combination');
            }




        }

        function generateFile() {

            var fromDate = $('#from_date').val();
            var toDate = $('#to_date').val();

            window.open("download_excel.htm?from_date=" + fromDate + "&to_date=" + toDate);

        }
    </script>
</html>
