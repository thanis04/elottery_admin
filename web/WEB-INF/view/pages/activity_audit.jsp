<%-- 
    Document   : activity_audit
    Created on : May 5, 2021, 4:24:00 PM
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
                                <h4>Audit Log</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" action="show_report_bydate.htm"  class="form-horizontal" id="search_form" role="form">

                                            <div class="form-group">
                                                <label class="">Reports<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <select data-validation="required" type="text" class="form-control" name="pagecode" id="pagecode"  >
                                                        <option selected value="0">--Select Report--</option>
                                                        <option value="EM">Employee Management</option>
                                                        <option value="UM">System User Management</option>
                                                        <option value="URM">User Role Management</option>
                                                        <option value="PIM">Lottery Item Management</option>
                                                        <option value="PM">Lottery Management</option>
                                                        <option value="PPM">Lottery Profile Management</option>
                                                        <option value="UPM">User Privilege Management</option>
                                                        <option value="TFU">Ticket File Upload</option>
                                                        <option value="TFA">Ticket File Approval</option>
                                                        <option value="RM">Winner File Upload</option>
                                                        <option value="RA">Winning File Approval</option>
                                                        <option value="RUP">Daily Result Upload</option>
                                                        <option value="RSA">Daily Result Upload Approval</option>
                                                        <option value="DM">Device Management</option>
                                                        <option value="ROBST">Request Status Rollback</option>
                                                        <option value="ROBAP">Approve Status Rollback</option>
                                                        <option value="PINRQRS">PIN Reset Request</option>
                                                        <option value="PINAPPRS">PIN Reset Approve</option>
                                                        <option value="SACC">System Access</option>
                                                        <option value="USARR">Account Deletion Request</option>
                                                        <option value="USARA">Account Deletion Approve</option>
                                                        <option value="UCLR">User Claim Request</option>
                                                        <option value="UCLA">User Claim Approve</option>
                                                        <option value="SBARE">Sub-Agent Management</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="">From Date</label>
                                                <input  type="text" class="form-control date" id="created_date" name="created_date" placeholder="Select from date" />
                                            </div>

                                            <div class="form-group">
                                                <label class="">To Date
                                                    <!--<span class="text-red"> *</span>-->
                                                </label>
                                                <input  type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>

                                            <div class="form-group" align="right">
                                                <button  type="button" class="btn btn-secondary" onclick="reset();">Reset</button>
                                                <button type="button" class="btn submit" onclick="loadTable();">Search</button>                                    
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
                                        <th>ID</th>
                                        <th>Section</th>
                                        <th>Page</th>
                                        <th>User Name</th>
                                        <th>Employee Id</th>
                                        <th>Action</th>
                                        <th>Action Date</th>
                                        <th>In Details</th>
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="viewModel" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4  class="modal-title">View Details : <span id="view_title"></span></h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <div class="row" id="det">
                                    <!--                                    <div class="col-md-6" >
                                                                            <ul class="list-group" id="det">
                                    
                                                                            </ul>
                                                                        </div>-->
                                    <!--                                    <div class="col-md-6">
                                                                            <ul class="list-group" id="det1">
                                    
                                                                            </ul>
                                                                        </div>-->
                                </div>



                            </div>
                            <div class="modal-footer">                              
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            </div>
                        </div>

                    </div>
                </div>


                <jsp:include page="../common/footer.jsp"/>
            </div>
        </div>
    </body>
    <script>

        var resultTable = "";
        var moreData = "";
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

        //load days for selected product
//        $('#sectioncode').change(function () {
//            if ($('#sectioncode').val() !== "0") {
//                loadPages();
//            }
//
//        });


//        function loadPages() {
//            var section = $('#sectioncode').val();
//            var dataString = "sectioncode=" + section;
//            $.ajax({
//                data: dataString,
//                url: "load_page_list.htm",
//                type: 'GET',
//                success: function (data, textStatus, jqXHR) {
//                    //clear table
//                    var response = JSON.parse(data);
//
//                    if (response.status === true) {
//
//                        var options = '<option selected value="0">--Select--</option>';
//                        var optionsSize = 0;
//                        //set data table data
//                        var pageData = response.list;
//                        $(pageData).each(function (index) {
//                            var row = pageData[index];
//                            //create option line
//                            var option = '<option value=' + row.page_id + '>' + row.page_name + '</option>';
//
//                            //add to string
//                            options = options + option;
//                            optionsSize++;
//
//                        });
//
//                        $('#pagecode').html(options);
//
//                        //check day size
//                        if (optionsSize === 0) {
//                            //no days 
//                            showNotification('warning', 'No days assign to selected product')
//                        }
//                    }
//
//
//                },
//                error: function (jqXHR, textStatus, errorThrown) {
//
//                }
//
//            });
//        }


        function loadTable() {

            var section = "";
            var pagecode = $('#pagecode').val();
            var created = $('#created_date').val();
            var toDate = $('#to_date').val();

            if (created === "" || created === null) {
                created = "-";
            }

            if (toDate === "" || toDate === null) {
                toDate = "-";
            }

            if (pagecode === "0") {
                showNotification('error', "Please Fill Mandatory Field");
            } else {

                if (($('#created_date').val() === "" && $('#to_date').val() === "") ||
                        ($('#created_date').val() !== "" && $('#to_date').val() !== "")) {

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
                                                "dlbWbSection": section,
                                                "dlbWbPage": pagecode,
                                                "date": created,
                                                "toDate": toDate
                                            },
                                            type: "GET",
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
                                            {"title": "ID", "data": "id", "defaultContent": "-"},
                                            {"title": "Section", "data": "section", "defaultContent": "-"},
                                            {"title": "Page", "data": "page", "defaultContent": "-"},
                                            {"title": "Action", "data": "action", "defaultContent": "-"},
                                            {"title": "User Name", "data": "user_name", "defaultContent": "-"},
                                            {"title": "Employee Id", "data": "emp_id", "defaultContent": "-"},
                                            {"title": "Action Date", "data": "created_time", "defaultContent": "-"},
//                                        {"title": "In Detail", "data": "description", "defaultContent": "-"}
                                            {"title": "In Detail", "data": null,
                                                render: function (data, type, row)
                                                {
                                                    return "<button type=\"button\" class=\"btn btn-info btn-sm \" type=\"button\"  onclick=\"getDetail('" + data.id + "');\" >View Detail</button>";
                                                }
                                            }
                                        ]
                            });

//                            "<button type=\"button\" class=\"btn btn-info btn-sm \" type=\"button\"  onclick=\"viewDetail('" + data.description + " |Action Date <b>:</b> " + data.created_time + "');\" >View Detail</button> <br>" +
//                                                            
                } else {
                    showNotification('error', "Please select both from date and to date!");
                }

            }

        }



        function getDetail(id) {
            $('#det1').html("");
            $('#det').html("");

            $.ajax({
                url: "getDetail.htm?id=" + id,
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    //clear table
                    var response = JSON.parse(data);
                    $('#viewModel').modal().show();
                    var totalHtml = "";
                    if (response.data.previous !== "") {
                        var array = response.data.previous.split('|');
                        var checkCurrent = response.data.current.split('|');
                        var detailsSize = 0;
                        var details = "<div class='col-md-6'><ul class='list-group'><li class='list-group-item'><b>Previous Data</b></li>";

                        $(array).each(function (index) {
                            var row = array[index];
                            var currRow = checkCurrent[index];

                            var preRow1 = row.replace("Previous ", "");
                            //create option line
                            if (row.includes("null") === false) {
                                if (row.trim() !== "") {
                                    var detail = "";
                                    if (currRow !== preRow1) {
                                        detail = '<li style="background-color: #F2E4C9;" style="box-sizing: border-box;" class="list-group-item col-5">' + row + '</li>';
                                    } else {
                                        detail = '<li style="box-sizing: border-box;" class="list-group-item col-5">' + row + '</li>';
                                    }
                                    //add to string
                                    details = details + detail;
                                    detailsSize++;
                                }
                            }

                        });
                        details = details + "</ul></div>"
                        totalHtml = totalHtml + details;


                    }

                    if (response.data.current !== "") {
                        var array = response.data.current.split('|');
                        var checkPreviouse = response.data.previous.split('|');
                        var detailsSize = 0;
                        var details = "<div class='col-md-6'><ul class='list-group'>";
                        if (response.data.previous !== "") {
                            details = details + "<li class='list-group-item'><b>Current Data</b></li>";
                        }
                        $(array).each(function (index) {
                            var row = array[index];
                            var preRow = "";
                            if (checkPreviouse === null) {
                                preRow = "";
                            } else {
                                preRow = checkPreviouse[index];
                                if (preRow !== undefined) {
                                    preRow = preRow.replace("Previous ", "");
                                }
                            }
                            //create option line
                            if (row.includes("null") === false) {
                                if (row.trim() !== "") {

                                    var detail = "";
                                    if (preRow !== "" && preRow !== undefined) {
                                        if (preRow !== row) {
                                            detail = '<li style="background-color: #F2E4C9;" class="list-group-item">' + row + '</li>';
                                        } else {
                                            detail = '<li class="list-group-item">' + row + '</li>';
                                        }
                                    } else {
                                        detail = '<li class="list-group-item">' + row + '</li>';
                                    }
                                    //add to string
                                    details = details + detail;
                                    detailsSize++;
                                }
                            }
                        });

                        details = details + "</ul></div>"
                        totalHtml = totalHtml + details;


                    }
                    totalHtml = totalHtml +
                            "<br> <div class='col-md-12'><ul class='list-group'><li class='list-group-item'>" +
                            response.data.actionDate +
                            "</li></ul></div>";
                    $('#det').html(totalHtml);
//                    var array1 = response.data.current.split('|');


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }

        function viewDetail(data) {
//            '" + data.description + "'
            $('#viewModel').modal().show();
            var array = data.split('|');
            console.log(array);
            var detailsSize = 0;
            var details = "";
            $(array).each(function (index) {
                var row = array[index];
                //create option line
                if (row.includes("null") === false) {
                    var detail = '<li class="list-group-item">' + row + '</li>';

                    //add to string
                    details = details + detail;
                    detailsSize++;
                }
            });

            $('#det').html(details);
        }
    </script>
</html>
