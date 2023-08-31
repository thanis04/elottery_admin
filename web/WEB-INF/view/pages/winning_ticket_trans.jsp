<%-- 
    Document   : audit_trace
    Created on : Oct 19, 2017, 11:22:20 AM
    Author     : salinda_r
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
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
                                <h4> Tickets For Transaction</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form class="form-horizontal" id="new_form" action="GET">
                                            <c:choose>
                                                <c:when test="${record eq 'transaction'}">
                                                    
                                                    <input type="hidden" class="form-control" id="id" name="id" value="${object.txnid}" />
                                                    <div class="row">
                                                        <div class="form-group col-lg-6">
                                                            <label >Transaction ID<span class="text-red"> *</span></label>
                                                            <div class="">
                                                               <label   data-validation="required" class="form-control" type="text" name="transID" id="transID" maxlength="15" >${object.txnid}</label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-lg-6">
                                                            <label >Purchase Date <span class="text-red"> *</span></label>
                                                            <div class="">
                                                                <label  data-validation="required" class="form-control" type="text" name="purchaseDate" id="purchaseDate" maxlength="15"><fmt:formatDate value="${object.updatedtime}" pattern="yyyy-MM-dd h:mm:ss"></fmt:formatDate></label>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="row">
                                                            <div class="form-group col-lg-6">
                                                                <label >Name<span class="text-red"> *</span></label>
                                                                <div class="">
                                                                    <label  data-validation="required" class="form-control" type="text" name="name" id="name" maxlength="15" >${object.dlbSwtStWallet.firstName} ${object.dlbSwtStWallet.lastName}</label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-lg-6">
                                                            <label >NIC<span class="text-red"> *</span></label>
                                                            <div class="">
                                                                <label  data-validation="required" class="form-control" type="text" name="nic" id="nic" maxlength="15" >${object.dlbSwtStWallet.nic}</label>
                                                            </div>
                                                        </div>
                                                    </div>


                                                </c:when>
                                               <c:when test="${record eq 'device'}">
                                                <input type="hidden" class="form-control" id="nic" name="nic" value="${nic}" />
                                                    <div class="row">
                                                        <div class="form-group col-lg-6">
                                                            <label >Name<span class="text-red"> *</span></label>
                                                            <div class="">
                                                                <label  data-validation="required" class="form-control" type="text" name="name" id="name" maxlength="15" >${object.firstName} ${object.lastName}</label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-lg-6">
                                                            <label >NIC<span class="text-red"> *</span></label>
                                                            <div class="">
                                                                <label  data-validation="required" class="form-control" type="text" name="nic" id="nic" maxlength="15" >${object.nic}</label>
                                                            </div>
                                                        </div>
                                                    </div>

                                                </c:when>
                                            </c:choose>




                                        </form>  
                                    </div>
                                    <div class="modal-footer">     
                                        <button type="button" id="btn_approve"  class="btn btn-warning" onclick="print();"><span ></span> Print Report</button>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <br>
                        <br>
                        <div id="search_result">

                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table" >
                                <thead>
                                <td>Serial No</td>
                                <td>Numbers</td>
                                <td>Price</td>

                                </thead>
                                <tbody>
                                    <%-- 
                                    <c:forEach var="item" items="${record_lst}">
                                        <tr>

                                            <td>${item.serialNo}</td>
                                            <td>${item.lotteryNumbers}</td>
                                            <td>${item.winningPrize}</td>
                                        </tr>
                                    </c:forEach>
                                    --%>
                                </tbody>
                            </table>


                        </div>
                    </div>
                </div>


            </div>


            <!-- Add new Modal -->
            <div class="modal fade modal-lg" id="viewTxModel" role="dialog">
                <div class="modal-dialog">                       
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header bg-white">
                            <button type="button" class="close " data-dismiss="modal">&times;</button>
                            <h4 id="window_title" class="modal-title">View Transactions</h4>

                        </div>
                        <div class="modal-body">
                            <!-- model body -->
                            <form class="form-horizontal" id="new_form" >

                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <label >Transaction ID<span class="text-red"> *</span></label>
                                        <div class="">
                                            <label  data-validation="required" class="form-control" type="text" name="transID" id="transID" maxlength="15" />
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <label >Purchase Date <span class="text-red"> *</span></label>
                                        <div class="">
                                            <label  data-validation="required" class="form-control" type="text" name="purchaseDate" id="purchaseDate" maxlength="15" />
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <label >Name<span class="text-red"> *</span></label>
                                        <div class="">
                                            <label  data-validation="required" class="form-control" type="text" name="name" id="name" maxlength="15" />
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <label >NIC<span class="text-red"> *</span></label>
                                        <div class="">
                                            <label  data-validation="required" class="form-control" type="text" name="nic" id="nic" maxlength="15" />
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <label >NIC<span class="text-red"> *</span></label>
                                        <div class="">

                                            <button id="btn_tx_print" type="button" id="btn_approve"  class="btn btn-warning" onclick="printTransaction();"><span ></span> Print Report</button>
                                        </div>
                                    </div>
                                </div>


                                <div id="resultTable">
                                    <div class="x_title" align="center">
                                        <h4>Purchased Ticket</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <!-- table -->
                                    <table class="table table-striped table-bordered bulk_action">
                                        <thead>
                                            <tr>
                                                <th>Serial No</th>
                                                <th>Numbers</th>
                                                <th>Price</th>                                       
                                            </tr>
                                        </thead>
                                        <tbody>

                                        </tbody>
                                    </table>
                                </div>


                            </form>  

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
            //alert("pausing");
            viewtable();
        });


        function printTransaction() {
            console.log("print");
            if (validateForm($('#new_form'))) {
                $('#new_form').attr('action', 'list_transaction_print.htm');
                $('#new_form').submit();
            }
        }

        function viewItem(id) {

            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'GET',
                url: "list_transaction.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === true) {
                        $('#viewTxModel').modal('show');

                        //show item details to form
                        var record = response.record;
                        $('#new_form #transID').html(record.id);
                        $('#new_form #purchaseDate').html(record.datetime);
                        $('#new_form #name').html(record.fanme + " " + record.lanme);
                        $('#new_form #nic').html(record.nic);


                        var records = response.record_lst;

                        $(records).each(function (index) {
                            var item = records[index];
                            var row = [item.serial_no, item.numbers, item.prize];
                            result_table.fnAddData(row);
                        })



                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }

        function viewtable()
        {
            var nic = $('#nic').val();
                resultTable = $('#result_table').DataTable
                ({
                    "ajax":
                    {
                        url: "list_transaction_by_nic_paginated.htm",
                        data:{
                                "nic":nic
                            },
                        type: 'GET',
                        error: function (xhr, status, error) 
                        {
                            if(xhr.status == 401)
                            {
                                showNotification('error', "Session Expired");
                            }
                        }
                    },
                    "destroy": true,
                    "paging": true,
                    "serverSide": true,
                    "processing": true,
                    "order": [],
                    "search": false,
                    "columns":
                    [
                        {"title": "Serial No", "data": "serialno", "defaultContent": "-"},
                        {"title": "Numbers", "data": "numbers", "defaultContent": "-"}, 
                        {"title": "Price", "data": "price", "defaultContent": "-"}
                    ]
                });
        }
        function print() {

            console.log("print");
            if (validateForm($('#new_form'))) {
                $('#new_form').attr('action', 'list_transaction_print.htm');
                $('#new_form').submit();
            }

        }

    </script>

</html>
