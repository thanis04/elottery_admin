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
        <title>Winner Validation</title>
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
                                <h4>Winner Validation</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    
                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="print_report_bydate.htm"  class="form-horizontal" id="search_form" role="form">
                                            
                                            <div class="form-group">
                                                <label class="">NIC<span class="text-red"> *</span></label>
                                                <input data-validation="required-nic" type="text" maxlength="12" class="form-control" id="nic" name="nic" placeholder="Enter a valid NIC number" />
                                            </div>
                                            
                                            <div class="form-group">
                                                <label class="">Mobile<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" maxlength="13" onkeydown="isNumberOnly(event)" class="form-control" id="mobile" name="mobile" placeholder="Enter a valid contact number" />
                                            </div>
<!--
                                            <div class="form-group">
                                                <label>Lottery</label>
                                                <div>
                                                    <select  type="text" class="form-control" name="lottery" id="lottery"  >
                                                        <option selected value="0">--Select--</option>
                                                    <%--     
                                                        <c:forEach var="item" items="${product_select_box}">
                                                            <option value="${item.productCode}">${item.description}</option>
                                                        </c:forEach>
                                                    --%>
                                                    </select>
                                                </div>
                                            </div>
-->
                                            <div class="form-group" align="right">
                                            <!--    <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button> -->
                                                
                                            <!--    <button type="button" class="btn print" onclick="print();">Print Report</button> -->
                                            <!--    <button type="button" class="btn print" onclick="printSummery();">Print Summary Report</button> -->
                                            </div>

                                            <br>
                                        </form>
                                    <button type="button" class="btn submit" onclick="search();">Search</button>
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
                                        <th>NIC</th>
                                        <th>Mobile</th>
                                        <th>Name</th>
                                        <th>Lottery Name</th>
                                        <th>Serial No</th>
                                        <th>Status</th>
                                        <th>Winning Amount</th>                                      
                                        <th>Action</th>                                      
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
    
    <form id="reciept" name="reciept" method="post" target="_blank">
        <input type="hidden" id="walletid" name="walletid">
        <input type="hidden" id="phid" name="phid">
    </form>
    
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
                $(".date").datepicker({dateFormat: 'yy-mm-dd',"maxDate": new Date});
            });

        });

        function search() 
        {
            
            if (validateForm($('#search_form'))) {
          
            
                var nic = $('#nic').val();
                var mobile = $('#mobile').val();
                
                
                  var formData = "nic="+nic+"&mobile="+mobile;

                    $.ajax
                    ({
                        data: formData,
                        url: "search.htm",
                        type: 'POST',
                        success: function (data, textStatus, jqXHR) 
                        {
                            var response = JSON.parse(data);
                            if (response.status === true) 
                            {
                                $('#search_result').show();
                                resultTable.fnClearTable();

                                var tabledata = response.search_result;
                                $(tabledata).each(function (index) 
                                {
                                    var item = tabledata[index];
                                    var buttonTag ;
                                    if(item.statusFlag == 1)
                                    {
                                        buttonTag = "<button type=\"button\" class=\"btn btn-success \" type=\"button\"  onclick=\"view(" + item.walletId + ","  + item.phid + ");\" > <i class='fa fa-eye'></i> View </button>" ;
                                    }
                                    if(item.statusFlag == 0)
                                    {
                                        buttonTag ="-";
                                    }
                                    var row = [
                                                item.nic, 
                                                item.mobile, 
                                                item.name, 
                                                item.lotteryname, 
                                                item.serialno, 
                                                item.status,
                                                item.winningamount,
                                                buttonTag
                                            ];
                                    resultTable.fnAddData(row);
                                });
                            } 
                            else 
                            {
                                showNotification('error', response.msg);
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) 
                        {

                        }
                    });
                }

        }


        function print() {
            $('#search_form').attr('action', 'print_report_bydate.htm')
            $('#search_form').submit();
        }
        function printSummery() {
            $('#search_form').attr('action', 'print_summery_report_bydate.htm')
            $('#search_form').submit();
        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        
        function view(walletid,phid) 
        {
            $('#reciept #walletid').val(walletid);
            $('#reciept #phid').val(phid);
            document.reciept.action = "${pageContext.servletContext.contextPath}/winnervalidation/view.htm";
            document.reciept.submit();
        }
    </script>

</html>

