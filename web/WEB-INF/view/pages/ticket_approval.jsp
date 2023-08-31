<%-- 
    Document   : result_approval
    Created on : Oct 17, 2017, 10:54:46 AM
    Author     : methsiri_h
--%>

<%@page import="com.epic.dlb.util.common.SystemVarList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>

        <style>
            .dark-area {
                background-color: #666;
                padding: 40px;
                margin: 0 -40px 20px -40px;
                clear: both;
            }

            .clearfix:before,.clearfix:after {content: " "; display: table;}
            .clearfix:after {clear: both;}
            .clearfix {*zoom: 1;}
        </style>

        <link href="<c:url value="/resources/circle-percentage/css/circle.css"/>" rel="stylesheet">
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
                                <h4>Ticket Files</h4>
                            </div>

                        </div>

                        <br>
                        <br>
                        <br>

                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">                               
                                <!-- table -->
                                <table class="table table-striped table-bordered bulk_action" id="result_table">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Lottery</th>                                                          
                                            <th>Date </th>
                                            <th>Draw No</th>                                               
                                            <th>No of Tickets</th>                                               
                                            <th>Status</th>
                                            <th>Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%--<c:forEach var="row" items="${pending_list}">--%>
                                        <!--                                            <tr>
                                                                                        <td>${row.id}</td>
                                                                                        <td>${row.productDescription}</td>
                                                                                        <td>${row.date}</td>
                                                                                        <td>${row.drawNo}</td>
                                                                                        <td>${row.noOfTicket}</td>
                                                                                        <td><span class="badge badge-pill badge-success">${row.dlbStatus.description}</span></td>                                               
                                                                                        <td>-->

                                        <%--<c:choose>--%>
                                        <%--<c:when test="${row.dlbStatus.statusCode eq '31'}">--%>                                                         
                                            <!--<span onclick="viewRecord('${row.id}')" class="fa fa-eye btn btn-primary" title='View' ></span>-->
                                        <%--</c:when>--%>
                                        <%--<c:when test="${row.dlbStatus.statusCode eq '47'}">--%>
                                           <!--<span onclick="viewRecord('${row.id}')" class="fa fa-eye btn btn-danger" title='View' ></span>-->                                                          
                                        <%--</c:when>--%>
                                        <%--<c:when test="${row.dlbStatus.statusCode eq '30'}">--%>
                                           <!--<span onclick="viewRecord('${row.id}')" class="fa fa-eye btn btn-warning" title='View' ></span>-->
                                        <%--</c:when>--%>
                                        <%--</c:choose>--%>

                                        <!--</td>-->

                                        <!--</tr>-->
                                        <%--</c:forEach>--%>
                                    </tbody>
                                </table>


                            </div>
                        </div>


                    </div>
                </div>


                <!-- /page content -->
                <!-- View Modal -show selected item   -->
                <div class="modal fade" id="viewModal" role="dialog" data-backdrop="static" data-keyboard="false"  >
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <button type="button" class="close " onclick="refreshPage();" data-dismiss="modal">&times;</button>
                                <h4 id="window_title" class="modal-title">Ticket File Approval</h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <h4>Ticket File Details</h4>
                                <hr style="background-color: lightgray;margin-top: 0px;">
                                <div class="row">     
                                    <div class="col-md-9">    
                                        <input  type="hidden" id="view_id"/>
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Lottery</p>
                                            <div class="col-sm-7">
                                                <label class="control-label" id="view_product" ></label>
                                            </div>
                                        </div>
                                        <br>

                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Draw No</p>
                                            <div class="col-sm-7">                                          
                                                <label class="control-label "  id="view_draw_no" ></label>
                                            </div>
                                        </div>
                                        <br>

                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Draw Date</p>
                                            <div class="col-sm-7">                                          
                                                <label class="control-label "  id="view_draw_date" ></label>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">No of Tickets</p>
                                            <div class="col-sm-7">                                          
                                                <label class="control-label "  id="view_no_of_ticket" ></label>
                                            </div>
                                        </div>
                                        <br>

                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Status</p>
                                            <div class="col-sm-7">
                                                <div class="form-group">                                                
                                                    <label class="control-label badge bg-blue-sky" id="view_statusCode" ></label>
                                                </div>
                                            </div>
                                        </div>
                                        <br>
                                    </div>
                                    <div class="col-md-3">

                                        <div id="progessDisplay" class="c100  small">
                                            <span id="progessDigit">0%</span>
                                            <div class="slice">
                                                <div class="bar"></div>
                                                <div class="fill"></div>
                                            </div>
                                        </div>

                                    </div>

                                </div>
                                <h4>Validation status</h4>
                                <hr style="background-color: lightgray;margin-top: 0px;">
                                <div class="row">
                                    <div class="col-md-9">
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">File name validation</p>
                                            <div class="col-sm-7">
                                                <label class="control-label fa fa-2x " id="view_filname_check" ></label>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Mac address validation</p>
                                            <div class="col-sm-7">
                                                <label class="control-label fa fa-2x" id="mac_address_check" ></label>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Hash code validation</p>
                                            <div class="col-sm-7">
                                                <label class="control-label fa fa-2x" id="hash_code_check" ></label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer">     
                                <button type="button" id="btn_approve" onclick="approve()" class="btn btn-success" ><span class="fa fa-check-circle"></span> Approve</button>
                                <button type="button" class="btn btn-default" onclick="refreshPage()" data-dismiss="modal">Close</button>
                            </div>
                        </div>

                    </div>
                </div>

                <!--Report-->
                <div class="modal fade" id="reportModal" role="dialog" data-backdrop="static" data-keyboard="false">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">

                                <h4  class="modal-title">Ticket File Approval Report</h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->     
                                <div class="row">  
                                    <div class="alert alert-success">
                                        <span id="rpt_msg"></span>
                                    </div>
                                    <div class="col-md-12">                                          
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Lottery</p>
                                            <div class="col-sm-7">                                          
                                                <label class="control-label text-right "  id="view_rpt_lottery" ></label>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Draw Date</p>
                                            <div class="col-sm-7">                                          
                                                <label class="control-label text-right "  id="view_rpt_draw_date" ></label>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Draw No</p>
                                            <div class="col-sm-7">                                          
                                                <label class="control-label text-right "  id="view_rpt_draw_no" ></label>
                                            </div>
                                        </div>
                                        <br>                                        
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Status</p>
                                            <div class="col-sm-7">  
                                                <label class="control-label badge bg-blue-sky " id="view_rpt_status" ></label>
                                            </div>
                                        </div>
                                        <br>
                                        <br>
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Total Records on file</p>
                                            <div class="col-sm-7">                                          
                                                <label class="control-label text-right "  id="view_total_count" ></label>
                                            </div>
                                        </div>
                                        <br>
                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Successfully uploaded Records</p>
                                            <div class="col-sm-7">
                                                <label class="control-label text-right" id="view_success_count" ></label>
                                            </div>
                                        </div>
                                        <br>

                                        <div class="form-group">
                                            <p class="text-secondary col-sm-5">Error Records</p>
                                            <div class="col-sm-7">                                          
                                                <label class="control-label text-right" id="view_error_count" ></label>
                                            </div>
                                        </div>
                                        <br>


                                        <br>                                      
                                    </div>
                                </div>                              
                            </div>
                            <div class="modal-footer">     
                                <button onclick="refreshPage()" type="button" class="btn btn-success" data-dismiss="modal"><i class="fa fa-check"></i> Ok</button>
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

            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false,
                "order": [[ 3, "desc" ]]
            });
            loadTable();

            $('#progessDisplay').hide();
        });


        function loadTable() {
            var formData;
            $.ajax({
                data: formData,
                url: "getPendingList.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {

                    //clear table
                    var response = JSON.parse(data);
                    console.log(response.dataList);
                    if (response.status === "success") {
//                        console.log(response.dataList);
                        $('#search_result').show();
                        resultTable.fnClearTable();
                        var tabledata = response.dataList;

                        $(tabledata).each(function (index) {
                            var item = tabledata[index];
                            var row;
                            var btn;
                            if (item.statusCode === 31) {
                                btn = "<span onclick='viewRecord(" + item.id + ")' class='fa fa-eye btn btn-primary' title='View' ></span>"
                            } else if (item.statusCode === 47) {
                                btn = "<span onclick='viewRecord(" + item.id + ")' class='fa fa-eye btn btn-danger' title='View' ></span>"
                            } else if (item.statusCode === 30) {
                                btn = "<span onclick='viewRecord(" + item.id + ")' class='fa fa-eye btn btn-warning' title='View' ></span>"
                            } else {
                                btn = "";
                            }
                            row = [item.id, item.productDescription, item.date, item.drawNo, item.noOfTicket,
                                "<span class='badge badge-pill badge-success'>" + item.statusDescription + "</span>", btn];
                            resultTable.fnAddData(row);
                        });
//                        resultTable.column(0).data().sort().reverse();
                    } else {
                        //show msg
                        showNotification('error', response.msg);
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }

        function viewRecord(id) {
            $('#viewModal').modal().show();

            var dataString = "id=" + id;
            $('#view_id').val(id);
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

                        $('#view_product').html(record.lottery);
                        $('#view_draw_no').html(record.draw_no);
                        $('#view_draw_date').html(record.draw_date);
                        $('#view_no_of_ticket').html(record.no_of_tk);
                        $('#view_statusCode').html(record.status);

                        if (record.status === 'Processing') {
                            $('#btn_approve').hide();
                            $('#progessDisplay').show();
                            showProgress(record.id);
                        }



                        if (!record.filename_check || !record.mac_check || !record.hash_check) {
                            $('#btn_approve').attr("disabled", true);
                        }

                        if (record.filename_check) {
                            $('#view_filname_check').addClass('fa-check');
                            $('#view_filname_check').addClass('text-success');
                            $('#view_filname_check').removeClass('fa-remove');
                            $('#view_filname_check').removeClass('text-danger');
                        } else {
                            $('#view_filname_check').addClass('fa-remove')
                            $('#view_filname_check').addClass('text-danger')
                            $('#view_filname_check').removeClass('fa-check');
                            $('#view_filname_check').removeClass('text-success');
                        }

                        if (record.mac_check) {
                            $('#mac_address_check').addClass('fa-check');
                            $('#mac_address_check').addClass('text-success');
                            $('#mac_address_check').removeClass('fa-remove');
                            $('#mac_address_check').removeClass('text-danger');
                        } else {
                            $('#mac_address_check').addClass('fa-remove')
                            $('#mac_address_check').addClass('text-danger')
                            $('#mac_address_check').removeClass('fa-check');
                            $('#mac_address_check').removeClass('text-success');
                        }

                        if (record.hash_check) {
                            $('#hash_code_check').addClass('text-success');
                            $('#hash_code_check').addClass('fa-check');
                            $('#hash_code_check').removeClass('fa-remove');
                            $('#hash_code_check').removeClass('text-danger');
                        } else {
                            $('#hash_code_check').addClass('text-danger');
                            $('#hash_code_check').addClass('fa-remove');
                            $('#hash_code_check').removeClass('fa-check');
                            $('#hash_code_check').removeClass('text-success');
                        }


                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function approve() {

            showConfirmMsg('Approve', 'Are you sure to approve Ticket file ?', fnApprove);

            function fnApprove() {
                var id = $('#view_id').val();
                var dataString = "id=" + id;

                $.ajax({
                    data: dataString,
                    type: 'POST',
                    url: "approve_wining_file.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);
                        $('#btn_approve').prop('disabled', true);
                        $('#progessDisplay').show();
                        // showNotification(response.status,response.msg);
                        showProgress(response.id);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });



            }

        }


        function showProgress(id) {
            var status = false;


            $('#loader-wrapper').html("");

            var refreshIntervalId = setInterval(fnShowProcessProgress, 1000);

            function fnShowProcessProgress() {
                var dataString = "id=" + id;
                $.ajax({
                    data: dataString,
                    type: 'POST',
                    url: "show_wining_file_approve_progress.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //hide window if record is ok
                        if (response.tst === 'INT' || response.tst === 'PND') {


                            var progress = parseInt(response.tp);
                            var progressMin = parseInt(response.tp - 1);

                            $('#progessDigit').html(response.tp + "%");
                            $('#progessDisplay').removeClass("p" + progressMin);
                            $('#progessDisplay').addClass("p" + progress);

                        } else if (response.tst === 'CML') {
                            //show msg
                            status = true;
                            $('#progessDisplay').hide();
                            if (response.status === 'error') {
                                showNotification(response.status, response.msg);
                            }
                            showReport(id);
                            $('#viewModal').modal().hide();

                        }


                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        status = true;
                    }

                });

                //stop interval when process is completed
                if (status) {
                    clearInterval(refreshIntervalId);
                }

            }
        }

        function showReport(id) {
            $('#reportModal').modal().show();
            $('#viewModal').modal('toggle');

            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get_upload_report.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {

                        var record = response.record;
                        $('#view_total_count').html(record.no_of_tk);
                        $('#view_success_count').html(parseInt(record.no_of_tk) - parseInt(response.error_count));
                        $('#view_error_count').html(response.error_count);

                        $('#view_rpt_lottery').html(record.lottery);
                        $('#view_rpt_draw_date').html(record.draw_date);
                        $('#view_rpt_draw_no').html(record.draw_no);
                        $('#view_rpt_status').html(record.status);
                        $('#rpt_msg').html(response.msg);


                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }


        function refreshPage() {
            location.reload();
        }




    </script>
</html>

