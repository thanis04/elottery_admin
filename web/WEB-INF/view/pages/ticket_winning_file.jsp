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
        <style>
            .pop_window{
                width: 180vh !important;
                margin: 30px auto !important;
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
                            <div class="title_left" >
                                <h4>Winning Ticket Search</h4>
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
                                            <div class="form-group">
                                                <label class="">From Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="from_date" name="from_date" placeholder="Select form date" />
                                            </div>
                                            <div class="form-group">    
                                                <label class="">To Date<span class="text-red"> *</span></label>
                                                <input data-validation="required" type="text" class="form-control date" id="to_date" name="to_date" placeholder="Select to date" />
                                            </div>


                                            <div class="form-group" align="right">
                                                <button  type="reset" class="btn btn-secondary" >Reset</button>
                                                <button type="button" class="btn submit" onclick="search();">Search</button>
                                                <button type="button" class="btn print" onclick="print();">Print Report</button>
                                                <!--                                               <button type="button" class="btn print" onclick="printSummery();">Print Summary Report</button>-->
                                            </div>

                                            <hr>




                                            <br>
                                        </form>

                                        <div id="prize_gen_pnl" class="row">
                                            <div class="col-md-3">
                                                <label class="">User Claimed Amount<span class="text-red"></span></label>
                                            </div>
                                            <div class="col-md-4">
                                                <input readonly="" type="text" class="form-control col-md-3" id="claimedAmount" name="claimedAmount" />

                                            </div>
                                            <div class="col-md-2">
                                                <button type="button" class="btn btn-warning" onclick="generateFile();">Generate Prize Pay File</button>
                                            </div>
                                        </div>
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
                            <table class="table table-striped table-bordered bulk_action" id="searched_table">
                                <thead>
                                    <tr>
                                        <th>Draw Date</th>                                                            
                                        <th>No of Winning Tickets</th>
                                        <th>Claim Pending - From Epic</th>
                                        <th>Claim Pending - From DLB</th>
                                        <th>User Claimed - From Epic</th>                                    
                                        <th>User Claimed - From DLB</th>                                    
                                        <th>Below 20K Winning (Epic Claim)</th>                                    
                                        <th>Above 20K Winning (DLB Claim)</th>                                    
                                        <th>Total Winnings</th>                                    
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div  class="modal fade " id="more_det" role="dialog"  data-backdrop="static" data-keyboard="false">
                    <div class="modal-dialog pop_window">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <button type="button" class="close "  data-dismiss="modal">&times;</button>
                                <h4   class="modal-title">Winning Draws - Draw Date: <span id="view_draw_date"></span> </h4>
                            </div>
                            <div class="modal-body">    
                                <table class="table table-striped table-bordered bulk_action" id="tbl_ticket">
                                    <thead>
                                    <th>Lottery</th>
                                    <th>Draw No</th>
                                    <th>No of Winning Tickets</th>
                                    <th>Below 20K (Epic Claim Pending)</th>
                                    <th>User Claimed (From Epic)</th>
                                    <th>Above 20K (DLB Claim Pending)</th>
                                    <th>User Claimed (From DLB)</th>
                                    <th>Total Winnings</th>                                       
                                    </thead>                                   

                                </table>


                            </div>

                        </div>
                    </div>
                </div>


                <!-- footer content -->
                <jsp:include page="../common/footer.jsp"/>
                <!-- /footer content -->
                </body>

                <script>

                    var resultTable = "";
                    var tbl_ticket = "";

                    (function ($) {
                        $.fn.inputFilter = function (inputFilter) {
                            return this.on("input keydown keyup mousedown mouseup select contextmenu drop", function () {
                                if (inputFilter(this.value)) {
                                    this.oldValue = this.value;
                                    this.oldSelectionStart = this.selectionStart;
                                    this.oldSelectionEnd = this.selectionEnd;
                                } else if (this.hasOwnProperty("oldValue")) {
                                    this.value = this.oldValue;
                                    this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
                                } else {
                                    this.value = "";
                                }
                            });
                        };
                    }(jQuery));

                    $("#ref_no").inputFilter(function (value) {
                        return /^\d*$/.test(value) && (value === "" || parseInt(value) <= 9999999999);
                    });


                    $(document).ready(function () {

                        //datatable
                        $('#search_result').hide();


                        tbl_ticket = $('#tbl_ticket').dataTable({
                            "searching": false
                        });

                        //set date selecter         
                        $(function () {
                            $("#from_date").datepicker({dateFormat: 'yy-mm-dd'});
                            $("#to_date").datepicker({dateFormat: 'yy-mm-dd'});
                            $("#draw_date").datepicker({dateFormat: 'yy-mm-dd'});
                        });
                        resultTable = null;

                        $('#prize_gen_pnl').hide();
                    });



                    function viewPaymentMdl(id) {
                        $('#accptTrnsModel').modal().show();
                        $('#view_id').val(id);
                        var dataString = "id=" + id;
                        $.ajax({
                            data: dataString,
                            url: "view_payment.htm",
                            type: 'GET',
                            success: function (data, textStatus, jqXHR) {
                                //clear table
                                var response = JSON.parse(data);
                                if (response.status === true) {
                                    $('#view_id').val(id);
                                    $('#view_amt').html(response.record);
                                } else {
                                    //show msg
                                    showNotification('error', response.msg);
                                }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {

                            }
                        });
                    }
                    function acceptPayment() {

                        var id = $('#view_id').val();
                        var refNo = $('#ref_no').val();
                        var dataString = "id=" + id + "&ref_no=" + refNo;

                        if (validateForm($('#frm_payment'))) {
                            $.ajax({
                                data: dataString,
                                url: "accept_payment.htm",
                                type: 'GET',
                                success: function (data, textStatus, jqXHR) {
                                    //clear table
                                    var response = JSON.parse(data);
                                    if (response.status === true) {
                                        $('#accptTrnsModel').modal('hide');
                                        search();
                                        showNotification('success', response.msg);
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

                    function generateFile() {
                        
                        if($('#claimedAmount').val()>0) {
                            
                                 var dataString = "";
                        $.ajax({
                            data: dataString,
                            url: "generate_prize.htm",
                            type: 'GET',
                            success: function (data, textStatus, jqXHR) {
                                //clear table
                                var res = JSON.parse(data);
                                var f = document.createElement("form");
                                f.setAttribute('method', "post");
                                f.setAttribute('action', "download_prize_file.htm");

                                var i = document.createElement("input"); //input element, text
                                i.setAttribute('type', "text");
                                i.setAttribute('name', "filePath");
                                i.setAttribute('value', res.filePath);

                                var j = document.createElement("input"); //input element, text
                                j.setAttribute('type', "text");
                                j.setAttribute('name', "filename");
                                j.setAttribute('value', res.filename);

                                f.appendChild(i);
                                f.appendChild(j);
                                document.getElementsByTagName('body')[0].appendChild(f);
                                console.info(f);
                                f.submit();
                                
                                $('#prize_gen_pnl').hide();

                            },
                            error: function (jqXHR, textStatus, errorThrown) {

                            }
                        });

                            
                        }
                        else {
                            showNotification("error","No claimed amount to generate");
                        }

                   


                    }


                    function search()
                    {

                        var user_claimed_amount = 0.0;
                        //alert("came in to search1");
                        var category = $('#category option:selected').val();

                        if (validateForm($('#search_form'))) {

                            //alert("came in to search2");
                            var fromDate = $('#from_date').val();
                            var toDate = $('#to_date').val();
                            //                var draw_date = $('#draw_date').val();
                            //                var draw_no = $('#draw_no').val();
                            //                var lottery = $('#lottery').val();

                            var formData = "from_date=" + fromDate + "&to_date=" + toDate + "&draw_date=" + "&start1=0&end2=100";
                            //alert("came in to search2");  + draw_date + "&draw_no=" + draw_no + "&lottery=" + lottery
                            if (checkFromDateNToDate(fromDate, toDate) && category !== null)
                            {  //alert("came in to search3");



                                if (resultTable !== null) {                   //Destroy and re-initialize the table if it already has been initialized.
                                    resultTable.destroy();
                                    resultTable = null;
                                }

                                $('#search_result').show();




                                resultTable = $('#searched_table').DataTable
                                        ({
                                            "ajax":
                                                    {
                                                        url: "searchpaginated.htm",
                                                        data: {
                                                            "from_date": fromDate,
                                                            "to_date": toDate,
                                                            "start1": "0",
                                                            "end1": "100"
                                                        },
                                                        type: "POST",
                                                        error: function (xhr, status, error)
                                                        {
                                                            if (xhr.status == 401)
                                                            {
                                                                showNotification('error', "Session Expired");
                                                            }
                                                        }
                                                    },
                                            "paging": true,
                                            "serverSide": true,
                                            "processing": true,
                                            "order": [],
                                            "search": false,
                                            "columns":
                                                    [
                                                        {"title": "Draw Date", "data": "draw_date", "defaultContent": "-"},
                                                        //                                            {"title": "Uploaded Date", "data": "uploaded_date", "defaultContent": "-"},
                                                        {"title": "No of Winning Tickets", "data": null,
                                                            render: function (data, type, row)
                                                            {
                                                                return "<a target='_blank' tooltip='View Report' href='list_ticket.htm?id=" + data.id + "'>" + data.no_of_winning + "<a/>";
                                                            }
                                                        },
                                                        {"title": "Claim Peding - From Epic ", "data": "epic_amount", "defaultContent": "-"},
                                                        {"title": "Claim Peding - From DLB", "data": "dlb_amount", "defaultContent": "-"},
                                                        {"title": "User Claimed - From Epic ", "data": "user_claimed_amount", "defaultContent": "-"},
                                                        {"title": "User Claimed - From DLB", "data": "user_claimed_dlb", "defaultContent": "-"},
                                                        {"title": "Below 20K Winning (Epic Claim) ", "data": "total_epic_win", "defaultContent": "-"},
                                                        {"title": "Above 20K Winning (DLB Claim) ", "data": "total_dlb_win", "defaultContent": "-"},
                                                        {"title": "Total Winnings", "data": "total_winnings", "defaultContent": "-"},
                                                        {"title": "Action", "data": null,
                                                            render: function (data, type, row)
                                                            {
                                                                user_claimed_amount = user_claimed_amount + data.user_claimed_amount;
                                                                $('#claimedAmount').val(user_claimed_amount);
                                                                var generateBtn = "";
                                                                if (data.user_claimed_amount > 0) {
                                                                    generateBtn = "<button type='button'  onclick='generateFile(" + data.draw_date_str + ")' class='btn btn-info' click>Generate Prize Pay File</button>";

                                                                }

//                                                                if (data.file_gen_amount > 0) {
//                                                                    generateBtn =+ "<button  onclick='viewPrizeFileHistory(" + data.draw_date_str + ")' class='btn btn-warning' click>View Generate History</button>";
//
//                                                                }


                                                                return "<button  onclick='openMoreDialog(" + data.draw_date_str + ")' class='btn btn-success' click>View Deatils</button>" + generateBtn;

                                                            }
                                                            //                                                ," + data.lottery + "," + data.draw_no + "," + data.uploaded_date + "," + data.statusDes + "
                                                        }

                                                    ]
                                        });

                            } else
                            {
                                showNotification('error', 'Invalid date combination');
                            }
                        }



                        $('#prize_gen_pnl').show()();


                        console.info(user_claimed_amount);


                    }

                    function openMoreDialog(draw_date) {
                        $('#more_det').modal().show();
                        var dataString = "draw_date=" + draw_date;
                        $.ajax({
                            data: dataString,
                            url: "findByDateWinningFile.htm",
                            type: 'GET',
                            success: function (data, textStatus, jqXHR) {
                                //clear table
                                var response = JSON.parse(data);
                                if (response.status === true) {

                                    tbl_ticket.fnClearTable();

                                    //set data table data
                                    var tabledata = response.data;
                                    $(tabledata).each(function (index) {
                                        var item = tabledata[index];

                                        var row = [item.productDescription, item.drawNo, item.no_of_winning, item.epic_amount, item.user_claimed_amount, item.dlb_amount, item.dlb_claim_amount, item.total_winnings];
                                        tbl_ticket.fnAddData(row);
                                    });

                                    $('#view_draw_date').html(response.draw_date)

                                } else {
                                    showNotification('error', response.msg);
                                }
                            },
                            error: function (jqXHR, textStatus, errorThrown) {

                            }
                        });
                    }

                    function print() {
                        if (resultTable.data().count() > 0) {
                            if (validateForm($('#search_form'))) {
                                $('#search_form').attr('action', 'print_ticket_winning_report.htm');
                                //                $('#search_form').attr('action', 'print.htm');
                                $('#search_form').submit();
                            }
                        } else {
                            showNotification('error', 'No data to print');
                        }



                    }



                </script>

                </html>
