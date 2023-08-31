<%-- 
    Document   : rpt_daily_transaction_summary
    Created on : Feb 28, 2020, 1:25:35 PM
    Author     : nipuna_k
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
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left" >
                                <h4>Daily Transaction Summary Report</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="show_report_bydateAndCategory.htm"  class="form-horizontal" id="search_form" role="form">
                                            <div class="form-group">
                                                <label class="">From Date (Collection Date)<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date (Collection Date)<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>

                                            <div class="form-group" align="right">
                                                <button type="reset" class="btn btn-secondary" >Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">View</button>
                                                <button type="button" class="btn print" onclick="print();">Print Report</button>
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
                            <div >
                                <div class="row">
                                    <div  class="col-md-3 "><h5>From Date</h5></div>
                                    <div class="col-md-4">
                                        <h5 class="text-primary" id="from_dateD"></h5>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3"><h5>To Date</h5></div>                                   
                                    <div class="col-md-4">
                                        <h5 class="text-primary"  id="to_dateD"></h5>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-3"><h5>Totally Collection</h5></div>

                                    <div class="col-md-4">
                                        <h5 class="text-primary"  id="tot_collection"></h5>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3"><h5>Total Commission (income)</h5></div>
                                    <div class="col-md-4">
                                        <h5 class="text-primary"  id="tot_commission"></h5>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3"><h5>Net Amount to be Transfered to DLB</h5></div>
                                    <div  class="col-md-4">
                                        <h5 class="text-primary"  id="win_amt_transfered"></h5>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-3"><h5>Totally Bank & Other Charges (Cost)</h5></div>
                                    <div  class="col-md-4">
                                        <h5 class="text-primary"  id="tot_bank_charges"></h5>
                                    </div>
                                </div>

                                <br>
                                <table class="table table-striped table-bordered bulk_action" id="result_table">
                                    <thead>
                                        <tr>
                                            <th>Collection Date</th>
                                            <th>Totally Collection</th>
                                            <th>Total Commission (income)</th>
                                            <th><span>Net Amount to be</span><br> Transfered to DLB</span></th>
                                            <th><span>Winning Amount to be</span> <br>Transfered to DLB operating A/C</th>
                                            <th><span>Totally Bank &</span><br> Other Charges (Cost)</th>                                  
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

        var resultTable = 0.00;
        var tot_collection = 0.00;
        var tot_commission = 0.00;
        var net_amt_transfered = 0.00;
        var win_amt_transfered = 0.00;
        var tot_bank_charges = 0.00;

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

        function search() {

            if (validateForm($('#search_form'))) {
                var formData = $('#search_form').serialize();
                var fromDate = $('#from_date').val();
                var toDate = $('#to_date').val();


                if (checkFromDateNToDate(fromDate, toDate)) {
                    $.ajax({
                        data: formData,
                        url: "show_report.htm",
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

                                    var row = [item.date, item.tot_collection, item.tot_commission, item.net_amt_transfered, item.win_amt_transfered, item.tot_bank_charges];

                                    tot_collection = tot_collection + parseFloat(item.tot_collection);
                                    tot_commission = tot_commission + parseFloat(item.tot_commission);
                                    net_amt_transfered = net_amt_transfered + parseFloat(item.net_amt_transfered);
                                    win_amt_transfered = win_amt_transfered + parseFloat(item.win_amt_transfered);
                                    tot_bank_charges = tot_bank_charges + parseFloat(item.tot_bank_charges);

                                    resultTable.fnAddData(row);
                                });


                                $('#tot_collection').html("LKR "+tot_collection);
                                $('#tot_commission').html("LKR "+tot_commission);
                                $('#net_amt_transfered').html("LKR "+net_amt_transfered);
                                $('#win_amt_transfered').html("LKR "+win_amt_transfered);
                                $('#tot_bank_charges').html("LKR "+tot_bank_charges);

                                $('#from_dateD').html($('#from_date').val());
                                $('#to_dateD').html($('#to_date').val());

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



        }

        function generateFile() {

            var fromDate = $('#from_date').val();
            var toDate = $('#to_date').val();
            var lottery = $('#lottery').val();

            window.open("download_excel.htm?from_date=" + fromDate + "&to_date=" + toDate + "&lottery=" + lottery);

        }
    </script>
</html>
