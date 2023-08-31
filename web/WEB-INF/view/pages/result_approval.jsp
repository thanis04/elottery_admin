<%-- 
    Document   : result_approval
    Created on : Oct 17, 2017, 10:54:46 AM
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

        <style>
            .btn-circle.btn-lg {
                width: 105px !important;
                height: 50px !important;
                font-size: 14px !important;
                line-height: 1.33 !important;
                margin: 3px !important;
                border: solid lightblue !important;
                padding: 0 !important;
            }
        </style>

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
                                <h4>Daily Result Upload Approval</h4>
                            </div>




                        </div>


                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">


                                <div id="search_result">

                                    <!-- table -->
                                    <table class="table table-striped table-bordered bulk_action" id="result_table">
                                        <thead>
                                            <tr>
                                                <th>#</th>
                                                <th>Product</th>                                                          
                                                <th>Day</th>
                                                <th>Draw No</th>
                                                <th>Draw Date</th>
                                                <th>Status</th>
                                                <th>Action</th>
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


                <!-- /page content -->
                <!-- View Modal -show selected item   -->
                <div class="modal fade" id="viewModal" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4  class="modal-title"><span id="viewModal_view_title"></span></h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <div class="row">                                   
                                    <form class="form-horizontal" id="approvalForm">
                                        <div class="form-group">
                                            <label class="control-label col-sm-5">ID:</label>
                                            <div class="col-sm-4">
                                                <label class="control-label" id="view_id" ></label>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Product:</label>
                                            <div class="col-sm-4">
                                                <label class="control-label" id="view_product" ></label>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Day:</label>
                                            <div class="col-sm-4">                                          
                                                <label class="control-label "  id="view_day" ></label>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Draw No:</label>
                                            <div class="col-sm-4">                                          
                                                <label class="control-label "  id="view_draw_no" ></label>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Draw Date:</label>
                                            <div class="col-sm-4">                                          
                                                <label class="control-label "  id="view_draw_date" ></label>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Status:</label>
                                            <div class="col-sm-4s">
                                                <div class="form-group">                                                
                                                    <label class="control-label badge badge-success" id="view_statusCode" ></label>
                                                </div>
                                            </div>
                                        </div>                                      

                                        <br>
                                        <hr>                                      
                                        <label>Normal Result</label>

                                        <div  id="result_item">

                                        </div>
                                        <br>                                        
                                        <label>Special Game Result</label>

                                        <div id="sp_items">

                                        </div>

                                    </form>
                                </div>
                            </div>
                            <div class="modal-footer">                              
                                <button id="btn_approve" type="button" class="btn btn-success" onclick="approval();" >Approve</button>
                                <button type="button" class="btn btn-default" onclick="refreshPage()" data-dismiss="modal">Close</button>
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



            //load profile
            $('#new_form #profileCode').change(function () {
                if ($('#new_form #profileCode').val() !== "0") {
                    loadProfile();
                }

            });

            //load product function
            //clear table
            $('#search_result').show();
            resultTable.fnClearTable();

            //set data table data
            var tabledata = ${search_result};
            $(tabledata).each(function (index) {
                var item = tabledata[index];
                var viewBtn = "<span onclick='viewItem(\"" + item.id + "\",\" " + item.day_code + "\")' class='fa fa-eye btn btn-primary'  title='View'> </span>";
                var status = "<span class='badge badge-pill badge-success'>" + item.status + "</span>";


                var row = [item.id, item.product, item.day, item.draw_no, item.date, status, viewBtn];
                resultTable.fnAddData(row);
            });


        });


        //view model Item
        function viewItem(id) {
            $('#result_item').html("");
            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#viewModal').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#view_id').html(record.id);
                        $('#view_product').html(record.product);
                        $('#view_day').html(record.day_code);
                        $('#view_draw_no').html(record.draw_no);
                        $('#view_draw_date').html(record.draw_date);
                        $('#view_statusCode').html(record.status);


                        $('#viewModal_view_title').html("View Result: " + record.product);

                        var items = response.record.items;
                        var sp_items = response.record.sp_items;

                        $(items).each(function (index) {
                            var item = items[index];
                            //add new button                           
                            var itemBtn = "<button  type='button' class='btn btn-default btn-circle btn-circle btn-lg'>" + item.value + "</button>";


                            $('#result_item').append(itemBtn);
                        });

                        $(sp_items).each(function (index) {
                            var item = sp_items[index];
                            //add new button   
                            var itemBtn = " <label>" + item.item + ": " + item.value + "</label><br>";

                            $('#sp_items').append(itemBtn);
                        });




                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }

        //Loding Profile 
        function loadProfile() {
            var profileCode = $('#new_form #profileCode').val();
            var dataString = "id=" + parseInt(profileCode);
            $.ajax({
                data: dataString,
                url: "${pageContext.servletContext.contextPath}/product_profile/load_profile.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    $('#result_input_panal').html("");
                    //clear table
                    var response = JSON.parse(data);

                    var resultInputs = "";

                    if (response.status === true) {
                        var items = response.itemList;

                        var dayCode = response.dayCode;
                        $(items).each(function (index) {
                            var item = items[index];
                            var resultInput = "";
                            if (item.value_type === 'select') {
                                //load special symbol list for select
                                var symbolSelectItems = "<br/><div class='form-group'>\n\
                                                         <label class='control-label col-sm-3'>" + item.description + ":</label>\n\
                                                         <select code='" + item.code + "' style='width:30%' data-validation='required' class='form-control'>\n\
                                                         <option value=0 selected>--Select--</option>";
                                var symbols = item.symbols;
                                $(symbols).each(function (index) {
                                    var symbol = symbols[index];
                                    var option = '<option >' + symbol.description + '</option>';

                                    symbolSelectItems = symbolSelectItems + option;
                                });

                                symbolSelectItems = symbolSelectItems + "</select></div> "
                                resultInput = symbolSelectItems;

                            } else {
                                resultInput = "<input code=" + item.code + " data-validation='required' class='col-sm-2 input-round-text form-control' placeholder=" + item.description + " id=" + item.code + " />";
                            }


                            resultInputs = resultInputs + resultInput;
                        });

                        //display result inputs
                        $('#result_input_panal').html(resultInputs);
                        $('#dayCode').val(dayCode)


                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }


        function approval() {
            function approval() {
                var id = $('#view_id').html();
                var dataString = "id=" + id;

                $.ajax({
                    data: dataString,
                    url: "update.htm",
                    type: 'POST',
                    success: function (data, textStatus, jqXHR) {
                        //clear table
                        var response = JSON.parse(data);

                        if (response.status === true) {
                            $('#btn_approve').hide();
                            $('#viewModal').modal('toggle');
                            showNotification('success', response.msg);

                            location.reload();
                        } else {
                            showNotification('error', response.msg);
                        }


                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }
            showConfirmMsg('Approve Result', 'Are you sure to approve result', approval);

        }

        function refreshPage() {
            location.reload();
        }

    </script>
</html>

