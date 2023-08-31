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
                                <h4>Winning Tickets</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="GET" target="_blank" action="show_report_bydateAndCategory.htm"  class="form-horizontal" id="search_form" role="form">
                                            <div class="form-group">
                                                <!--<label class="">From Date<span class="text-red"> *</span></label>-->
                                                <input data-validation="required" type="hidden" class="form-control date" id="end" name="end" value="14" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">
                                                <!--<label class="">From Date<span class="text-red"> *</span></label>-->
                                                <input data-validation="required" type="hidden" class="form-control date" id="start" name="start" value="0" placeholder="Select form date" />
                                            </div>

                                            <input type="hidden" value="${winingFile.id}" name="id" id="id"/>

                                            <div class="form-group">
                                                <label>File No</label>
                                                <div>
                                                    <label class="form-control" >${winingFile.id}</label>

                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label>Lottery Type</label>
                                                <div>
                                                    <label class="form-control" >${winingFile.productDescription}</label>

                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Draw Date</label>
                                                <label class="form-control" >${winingFile.date}</label>
                                            </div>
                                            <div class="form-group">
                                                <label class="">Draw No</label>
                                                <label class="form-control" >${winingFile.drawNo}</label>
                                            </div>                                            




                                            <br>
                                        </form>
                                    </div>
                                    <div class="modal-footer">     
                                        <button type="button" id="btn_approve"  class="btn btn-warning" onclick="print();"><span ></span> Print Report</button>

                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="search_result">
                             <div class="x_title" align="center">
                                <h4> Tickets</h4>                                        
                                <div class="clearfix"></div>
                            </div>

                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" >
                                <thead>
                                <td>Serial No</td>
                                <td>Numbers</td>
                                <td>Price</td>
                                <td>Mobile NO</td>
                                <td>NIC</td>
                                <td></td>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${record}">
                                        <tr>

                                            <td>${item.serialNumber}</td>
                                            <td>${item.lotteryNumbers}</td>
                                            <td>${item.winingPrize}</td>
                                            <td>${item.mobileNo}</td>
                                            <td>${item.nic}</td>
                                            <td><a target="_blank" href="list_transaction.htm?id=${item.tid}"><button class="btn btn-success">View Transaction</button></a></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>


                        </div>
                    </div>
                </div>


            </div>


    </body>

    <script>

        var result_table = "";
      
        $(document).ready(function () {


            result_table = $('#result_table').dataTable({
                "searching": false
            });

            

            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });

        });

        function print() {
            $('#search_form').attr('action', 'list_ticket_print.htm');
            $('#search_form').submit();
        }
       

     

    </script>

</html>
