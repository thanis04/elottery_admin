<%-- 
    Document   : purchase_schedule
    Created on : Mar 10, 2020, 12:31:02 PM
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

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left">
                                <h4>Purchase Schedule</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>
                        <div class="row">
                            <div class="col-md-6 col-sm-12 col-xs-12 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float: none;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Purchase Schedule</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form class="form-horizontal" id="search_form">
                                            <div class="form-group">
                                                <label>Mobile No/NIC</label>
                                                <input maxlength="15" type="text" class="form-control" id="mobile_nic"  name="mobile_nic" placeholder="Enter lottery code" />
                                            </div>
                                            <div class="form-group">
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" id="btn_reset" >Reset</button>
                                                    <button type="button" class="btn submit" onclick="searchByMobileAndNIC()">Search</button>
                                                </div>
                                            </div>
                                            <br>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row" id="search_result">
                            <div class="col-md-6 col-sm-12 col-xs-12 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float: none;">
                                <div class="x_panel">
                                    <div class="x_content">
                                        <form class="form-horizontal" id="search_form">
                                            <input maxlength="15" type="hidden" class="form-control" id="walletid"  name="walletid"/>
                                            <div class="form-group">
                                                <label>Name</label>
                                                <input maxlength="15" type="text" class="form-control" id="name"  name="name" readonly/>
                                            </div>
                                            <div class="form-group">
                                                <label>Mobile No</label>
                                                <input maxlength="15" type="text" class="form-control" id="mobile_no"  name="mobile_no" readonly/>
                                            </div>
                                            <div class="form-group">
                                                <label>NIC</label>
                                                <input maxlength="15" type="text" class="form-control" id="nic"  name="nic" readonly/>
                                            </div>
                                            <div class="form-group">
                                                <div class="add-button">
                                                    <button onclick="searchByWallet()" type="button" class="btn submit" data-toggle="collapse" data-target="#multiCollapseExample1" aria-expanded="false" aria-controls="multiCollapseExample1">Search</button>
                                                </div>
                                            </div>
                                        </form>

                                        <div class="collapse multi-collapse" id="multiCollapseExample1">
                                            <div id="search_result">                                               
                                                <table class="table table-striped table-bordered bulk_action" id="result_table">
                                                    <thead>
                                                        <tr>
                                                            <th>Show Date</th>                                                          
                                                            <th>Lottery</th>
                                                            <th>QT</th>
                                                            <th>Payment</th>
                                                            <th>Status</th>
                                                            <th>Create Date</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>

                                    </div>                                    
                                </div>
                            </div>
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
            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });
        });

        function searchByMobileAndNIC() {
            var code = $('#mobile_nic').val();
            var dataString = "key=" + code;
//            alert(code);
            $.ajax({
                data: dataString,
                url: "search_by_key.htm",
                type: 'POST',
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);
                    $('#search_result').show();
                    $("#walletid").val(response.id);
                    $("#name").val(response.name);
                    $("#mobile_no").val(response.mobileNo);
                    $("#nic").val(response.nic);
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function searchByWallet() {
            var code = $('#walletid').val();
            var dataString = "walletId=" + code;

            console.log(code);

            $.ajax({
                data: dataString,
                url: "search.htm",
                type: 'POST',
                success: function (data, textStatus, jqXHR) {
                    console.log(data);
                    var response = JSON.parse(data);
//                    if (response.status === true) {
//                        resultTable.fnClearTable();
//                        var tabledata = response.search_result;
//                        $(tabledata).each(function (index) {
//                            var row = [item.date, item.day_code, item.description, item.status, editBtn + " " + delBtn];
//                            resultTable.fnAddData(row);
//                        });
//                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            });

        }
    </script>
</html>
