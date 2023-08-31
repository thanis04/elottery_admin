<%-- 
    Document   : audit_trace
    Created on : Oct 19, 2017, 11:22:20 AM
    Author     : salinda_r
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

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left" >
                                <h4>Transaction - Account Transfer Report</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">                                   
                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="print_report_bydate.htm"  class="form-horizontal" id="search_form" role="form">
                                            <div class="form-group">
                                                <label class="">From Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">To Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">Mobile No</label>
                                                <input  data-validation="tel" maxlength="10"   type="text" class="form-control " id="mobile_no" name="mobile_no" placeholder="Type Mobile" />
                                                <input value="18" maxlength="10"   type="hidden" class="form-control " id="trans_type" name="trans_type"  />
                                            </div>
                                            <div class="form-group">
                                                <label class="">NIC</label>
                                                <input maxlength="12" data-validation="nic"   type="text" class="form-control " id="nic" name="nic" placeholder="Type NIC" />
                                                <input maxlength="12"  value="15"   type="hidden" class="form-control " id="trans_type" name="trans_type"  />
                                            </div>



                                            <div class="form-group">
                                                <label>Bank </label>
                                                <div>
                                                    <select  type="text" class="form-control" name="bank_code" id="bank_code"  >
                                                        <option selected value="0">--Select--</option>
                                                        <c:forEach var="item" items="${listBank}">
                                                            <option value="${item.bankCode}">${item.name}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">From Account</label>
                                                <input maxlength="12"   type="text" class="form-control " id="from_acc_no" name="from_acc_no" placeholder="From Account No" />

                                            </div>

                                            <div class="form-group">
                                                <label class="">To Account</label>
                                                <input maxlength="12"    type="text" class="form-control " id="to_acc_no" name="to_acc_no" placeholder="To Account No" />
                                            </div>


                                            <div class="form-group" align="right">
                                                <button type="reset" class="btn btn-secondary" >Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">View</button>
                                                <button type="button" class="btn print" onclick="print();">Print</button>
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
                                        <th>Transaction No</th>
                                        <th>Date</th>
                                        <th>Name</th>
                                        <th>Mobile No</th>
                                        <th>NIC</th>                                       
                                        <th>Bank Code</th>   
                                        <th>Account Holder Name</th>   
                                        <th>From Account</th>       
                                        <th>To Account</th>       
                                        <th >Amount</th>
                                    </tr>
                                </thead>
                                <tfoot>
                                    <tr>
                                        <th colspan="9" style="text-align:right"> Total:</th>
                                        <th style="text-align:right">0</th>
                                    </tr>
                                </tfoot>
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
                "searching": false,
                "footerCallback": function (row, data, start, end, display) {
                    var api = this.api(), data;

                    // Remove the formatting to get integer data for summation
                    var intVal = function (i) {
                        return typeof i === 'string' ?
                                i.replace(/[\$,]/g, '') * 1 :
                                typeof i === 'number' ?
                                i : 0;
                    };

                    // Total over all pages
                    total = api
                            .column(9)
                            .data()
                            .reduce(function (a, b) {
                                return intVal(a) + intVal(b);
                            }, 0);

                    // Total over this page
                    pageTotal = api
                            .column(9, {page: 'current'})
                            .data()
                            .reduce(function (a, b) {
                                return intVal(a) + intVal(b);
                            }, 0);

                    // Update footer
                    pageTotal = (Math.round(pageTotal * 100) / 100).toString();
                    total = (Math.round(total * 100) / 100).toString();
                    $(api.column(9).footer()).html(
                            'Rs.' + parseFloat(pageTotal).toFixed(2) + ' ( Rs.' + parseFloat(total).toFixed(2) + ' Total)'
                            );
                }
            });

            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });

        });

        //search function
        function search() {

            if (validateForm($('#search_form'))) {
                var formData = $('#search_form').serialize();

                var fromDate = $('#from_date').val();
                var toDate = $('#to_date').val();

                if (checkFromDateNToDate(fromDate, toDate)) {
                    $.ajax({
                        data: formData,
                        url: "show_bank_tranfer_report_bydate.htm",
                        type: 'POST',
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
//                                    
//                                        <th>Transaction No</th>
//                                        <th>Date</th>
//                                        <th>Name</th>
//                                        <th>Mobile No</th>
//                                        <th>NIC</th>                                       
//                                        <th>Bank Code</th>   
//                                        <th>Account Holder Name</th>   
//                                        <th>From Account</th>       
//                                        <th>To Account</th>       
//                                        <th>Amount</th>

                                    var row = [item.id, item.datetime, item.name, item.mobile_no, item.nic, item.bank, item.holder_name, item.from_acc, item.to_acc, item.amount];
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


        }
        
        function print() {

            if (validateForm($('#search_form'))) {
                $('#search_form').attr('action', 'bank_tranfer_report_bydate_print.htm');
                $('#search_form').submit();
            }
        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }

    </script>

</html>
