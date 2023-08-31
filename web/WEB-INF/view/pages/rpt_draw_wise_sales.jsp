<%-- 
    Document   : rpt_draw_wise_sales
    Created on : Jul 14, 2021, 9:21:41 AM
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
                                <h4>Draw Wise Sales Insight</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" class="form-horizontal" 
                                              id="search_form" role="form">
                                            <div class="form-group">
                                                <label class="">Draw No<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required"  class="form-control" 
                                                           type="text"  maxlength="10" name="drawNo" id="drawNo" 
                                                           placeholder="Enter Draw No" />
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label>Lottery Type<span class="text-red"> *</span></label>
                                                <div>
                                                    <select  type="text" class="form-control" name="lottery" id="lottery"  >
                                                        <option selected value="0">--Select--</option>
                                                        <c:forEach var="item" items="${product_select_box}">
                                                            <option value="${item.productCode}">
                                                                ${item.description}
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label>Report Type<span class="text-red"> *</span></label>
                                                <div>
                                                    <select  type="text" class="form-control" name="reportType" id="reportType"  >
                                                        <option selected value="0">--Select--</option>
                                                        <option value="1">Users who purchased lotteries</option>
                                                        <option value="2">Users who did not purchase lotteries</option>
                                                    </select>
                                                </div>
                                            </div>


                                            <div class="form-group" align="right">
                                                <button  type="button" class="btn btn-secondary" onclick="reset();">Reset</button>
                                                <button type="button" class="btn submit" onclick="loadTable();">Search</button>
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

            if ($('#drawNo').val() === "" || $('#lottery').val() === "0" || $('#reportType').val() === "0") {
                showNotification('error', "Please fill all the fields");
            } else {
//            alert(page);
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
                                            "drawNo": $('#drawNo').val(),
                                            "lottery": $('#lottery').val(),
                                            "reportType": $('#reportType').val()
                                        },
                                        type: "POST",
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
                                        {"title": "Customer name", "data": "name", "defaultContent": "-"},
                                        {"title": "Contact number", "data": "mobile_no", "defaultContent": "-"}
                                    ]
                        });
            }
        }


        function generateFile() {


            if ($('#drawNo').val() === "" || $('#lottery').val() === "0" || $('#reportType').val() === "0") {
                showNotification('error', "Please fill all the fields");
            } else {

                var drawNo = $('#drawNo').val();
                var lottery = $('#lottery').val();
                var reportType = $('#reportType').val();

                window.open("download_excel.htm?drawNo=" + drawNo +
                        "&lottery=" + lottery + "&reportType=" + reportType);
            }
        }
    </script>
</html>

