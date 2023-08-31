<%-- 
    Document   : rpt_commission_other_fees
    Created on : Feb 28, 2020, 8:28:14 AM
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
                                <h4>Commission & Other Fees Report</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="show_report_bydateAndCategory.htm"  class="form-horizontal" id="search_form" role="form">

                                            <div class="form-group">
                                                <label>Lottery Type</label>
                                                <div>
                                                    <select  type="text" class="form-control" name="lottery" id="lottery"  >
                                                        <option selected value="0">--Select--</option>
                                                        <c:forEach var="item" items="${product_select_box}">
                                                            <option value="${item.productCode}">${item.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>
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
                            <div class="row">
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
                                    <div class="col-md-3"><h5>Cost</h5></div>

                                    <div class="col-md-4">
                                        <h5 class="text-primary"  id="tot_cost"></h5>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3"><h5>Commission</h5></div>
                                    <div class="col-md-4">
                                        <h5 class="text-primary"  id="tot_commission"></h5>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3"><h5>Bank Charge</h5></div>
                                    <div  class="col-md-4">
                                        <h5 class="text-primary"  id="bank_chg"></h5>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-3"><h5>Ticket Value</h5></div>
                                    <div  class="col-md-4">
                                        <h5 class="text-primary"  id="tot_ticket_amt"></h5>
                                    </div>
                                </div>
                            </div>
                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Lottery Name</th>
                                        <th>Draw No</th>
                                        <th>Draw Date</th>
                                        <th>Ticket Reference No</th>                                                                  
                                        <th>Cost</th>                                       
                                        <th>Commission</th>     
                                        <th>Bank Charge</th>     
                                        <th>Ticket Value</th>           
                                        <th>Currency</th>                                       
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

        function search() {

            if (validateForm($('#search_form'))) {
                var formData = $('#search_form').serialize();
                var fromDate = $('#from_date').val();
                var toDate = $('#to_date').val();

                $('#from_dateD').html(fromDate);
                $('#to_dateD').html(toDate);

                var tot_cost = 0.00;
                var tot_commission = 0.00;
                var bank_chg = 0.00;
                var tot_ticket_amt = 0.00;


                if (checkFromDateNToDate(fromDate, toDate)) {
                    $.ajax({
                        data: formData,
                        url: "show_report.htm",
                        type: 'GET',
                        success: function (data, textStatus, jqXHR) {
                            //clear table
                            var response = JSON.parse(data);
                            console.log(data);
                            if (response.status === true) {
                                $('#search_result').show();
                                resultTable.fnClearTable();

                                //set data table data
                                var tabledata = response.search_result;
                                $(tabledata).each(function (index) {
                                    var item = tabledata[index];
                                    var row = [item.lottery_name, item.draw_no, item.draw_date, item.tkt_reff, item.cost, item.commision, item.bank_charge, item.tkt_value, item.currency];
                                    resultTable.fnAddData(row);

                                    tot_cost = parseFloat(tot_cost) + parseFloat(item.cost);
                                    tot_commission = parseFloat(tot_commission) + parseFloat(item.commision);
                                    bank_chg = parseFloat(bank_chg) + parseFloat(item.bank_charge);
                                    tot_ticket_amt = parseFloat(tot_cost) + parseFloat(item.tkt_value);
                                });
                            } else {
                                //show msg
                                showNotification('error', response.msg);
                            }

                            $('#tot_cost').html(tot_cost.toFixed(2));
                            $('#tot_commission').html(tot_commission.toFixed(2));
                            $('#bank_chg').html(bank_chg.toFixed(2));
                            $('#tot_ticket_amt').html(tot_ticket_amt.toFixed(2));


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

            window.open("download_excel.htm?from_date=" + fromDate + "&to_date=" + toDate+"&lottery="+lottery);

        }
    </script>
</html>
