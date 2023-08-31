<%-- 
    Document   : transaction_explorer
    Created on : Oct 12, 2017, 3:18:26 PM
    Author     : methsiri_h
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
                                <h4>Transaction Explorer</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Transactions</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form autocomplete="off"  class="form-horizontal" id="search_form">


                                            <div class="form-group">
                                                <label class="">From Date</label>
                                                <div class="">
                                                    <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select Date">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">To Date</label>
                                                <div class="">
                                                    <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select Date">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Mobile No</label>
                                                <div class="">
                                                    <input class="form-control" type="text" onkeydown="isNumberOnly(event)" name="dlbStDevice.mobileNo" id="mobileNo" placeholder="Enter Mobile No" />
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">NIC No</label>
                                                <div class="">
                                                    <input class="form-control" type="text" name="dlbStDevice.nic" id="nic" placeholder="Enter NIC No"/>
                                                </div>
                                            </div>

                                            <div class="form-group" align="right">
                                                <label class=""></label>
                                                <div class="">
                                                    <button type="button" class="btn btn-secondary" onclick="resetMyTheForm(); this.form.reset();">Reset</button>
                                                    <button type="button" class="btn submit" onclick="searchDevice()">Search</button>
                                                    <!--<button type="button" class="btn btn-default" onclick="this.form.reset()">Reset</button>-->
                                                </div>
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
                                                            <th>Transaction No</th>                                                          
                                                            <th>Date</th>
                                                            <th>Name</th>
                                                            <th>Nic No</th>
                                                            <th>Mobile</th>
                                                            <th>Transaction Type</th>
                                                            <th>Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>


                                                    </tbody>
                                                </table>
                                            </div>

                    </div>
                </div>
                <!-- /page content -->

                <!-- Add new Modal -->
                <div class="modal fade" id="addNew" role="dialog">
                    <div class="modal-dialog">                       
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <h4 id="window_title" class="modal-title">Transaction List</h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <form class="form-horizontal" id="new_form" >
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Transaction No:</label>
                                        <div class="col-sm-7">
                                            <input data-validation="required" class="form-control" type="text" name="id" id="id" />
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Date and Time:</label>
                                        <div class="col-sm-7">
                                            <input data-validation="required" class="form-control" type="text" name="datetime" id="datetime" />
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Name:</label>
                                        <div class="col-sm-7">
                                            <input data-validation="required" class="form-control" type="text" name="dlbStDevice.fname" id="name" />
                                        </div>
                                    </div>                                         
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">NIC No:</label>
                                        <div class="col-sm-7">
                                            <input data-validation="required" class="form-control" type="text" name="dlbStDevice.nic" id="nic" />
                                        </div>
                                    </div>                                         
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Mobile No:</label>
                                        <div class="col-sm-7">
                                            <input data-validation="required" class="form-control" type="text" name="dlbStDevice.mobileNo" id="mobileNo" />
                                        </div>
                                    </div>                                         
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Transaction Type</label>
                                        <div class="col-sm-7">
                                            <input data-validation="required" class="form-control" type="text" name="dlbSwtMtTxnType.description" id="description" />
                                        </div>
                                    </div>                                         
                                    <div class="form-group">
                                        <label class="control-label col-sm-3">Mobile Brand</label>
                                        <div class="col-sm-7">
                                            <input data-validation="required" class="form-control" type="text" name="dlbStDevice.brand" id="brand" />
                                        </div>
                                    </div>                                         
                                </form>                            
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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

            //datatable
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd'});
            });

        });

        //search function
        function searchDevice() {
            var formData = $('#search_form').serialize();
            
            if(validateSearch()){
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
                            var viewBtn = "<span onclick='viewItem(\"" + item.id + "\")' class='fa fa-eye btn btn-primary' title='Edit Product'></span>";
                            var row = [item.id, item.datetime, item.fanme + " " + item.lanme, item.nic, item.mobile, item.description, viewBtn];
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

          
        }
        
        //view search item
        function viewItem(id) {
            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#addNew').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#new_form #id').val(record.id);
                        $('#new_form #name').val(record.fanme +" "+ record.lanme);
                        $('#new_form #datetime').val(record.datetime);
                        $('#new_form #nic').val(record.nic);
                        $('#new_form #mobileNo').val(record.mobile);
                        $('#new_form #description').val(record.description);
                        $('#new_form #brand').val(record.brand);


                        //set read only inputs
                        $('#new_form *').attr('disabled', true);
                        $("#window_title").html("Transaction Details: " + record.id)

                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }

        function resetMyTheForm() {
            resultTable.fnClearTable();
        }
        
        //search critia validate
        function validateSearch() {
            var from_date = $('#from_date').val();
            var to_date = $('#to_date').val();
            var nic = $('#nic').val();
            var mobileNo = $('#mobileNo').val();

            if (from_date === '' || to_date === '') {
                showNotification('error', "from date or to date can't be empty");
                return false;
            } else if (nic === '' && mobileNo === '' && from_date === '' && to_date === '') {
                showNotification('error', "Search criteria can't be empty");
                return false;

            }

            return true;
        }
    </script>
</html>