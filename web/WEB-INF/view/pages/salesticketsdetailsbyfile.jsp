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
                                <h4>Sales Ticket Details by File</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                            <form autocomplete="off" method="post" target="_blank" action="show_report_bydateAndCategory.htm"  class="form-horizontal" id="search_form" role="form">
                                            <input type="hidden" id="fileid" name="fileid" value="${fileid}"/>
                                                
                                            <div class="form-group">
                                                <label class="">Product<span class="text-red"> *</span></label>
                                                <input readonly="" data-validation="required" type="text" class="form-control" id="from_date" name="product" value="${product}"/>
                                            </div>
                                            <div class="form-group">
                                                <label class="">Draw No<span class="text-red"> *</span></label>
                                                <input readonly="" data-validation="required" type="text" class="form-control" id="to_date" name="drowno"  value="${drowno}"/>
                                            </div>
                                            <div class="form-group">
                                                <label class="">Draw Date<span class="text-red"> *</span></label>
                                                <input readonly="" data-validation="required" type="text" class="form-control" id="from_date" name="drowdate" value="${drowdate}"/>
                                            </div>
                                            <div class="form-group">
                                                <label class="">Uploaded by<span class="text-red"> *</span></label>
                                                <input readonly="" data-validation="required" type="text" class="form-control" id="uploadedby" name="to_date" value="${uploadedby}"/>
                                            </div>
                                            <div class="form-group">
                                                <label class="">Approved by<span class="text-red"> *</span></label>
                                                <input readonly="" data-validation="required" type="text" class="form-control" id="approvedby" name="to_date" value="${approvedby}"/>
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
                            <table class="table table-striped table-bordered bulk_action" id="searched_table">
                                <thead>
                                    <tr>
                                        <!--<th>ID</th>-->
                                        <th>Serial Number</th>
                                        <th>Lottery Numbers</th>
                                        <th>Mobile Number</th> 
                                        <th>Nic Number</th> 
                                         <th>Action</th> 
                                    </tr>
                                </thead>
                                <tbody>
<!--                                    <tr>
                                        <td>01</td>
                                        <td>5263</td>
                                        <td>2018-09-01</td>
                                        <td>6587</td>
                                        <td>65</td>
                                        <td>855</td>
                                        <td>855</td>
                                     <!-- ///////////////////////////   
                                    </tr>-->

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
        <form id="reciept" name="reciept" method="post" target="_blank">
            <input type="hidden" id="id" name="id">
        </form>
    </body>
    <script>
        var resultTable;
        $(document).ready(function () {

            //datatable
            $('#search_result').show();
//            resultTable = $('#searched_table').dataTable({
//                "searching": false
//            });

            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd',"maxDate": new Date});
            });
            //search();
            loadTable();
        });

        //search function
        /*
        function search() {
           
                var fileid = $('#fileid').val();
                 //alert("function called========" + fileid)  
                 
                    $.ajax({
                        data:{
                            "fileid":fileid 
                        },
                        url: "viewsalesdetails.htm",
                        type: 'POST',
                        success: function (data, textStatus, jqXHR) {
                            //clear table
                            var response = JSON.parse(data);
                            console.log("data"+data);
                           
                            if (response.status === true) 
                            {
                                $('#search_result').show();
                                //resultTable.fnClearTable();
                                var tabledata = response.search_result;
                                
                                
                                $(tabledata).each(function (index) 
                                {
                                    var item = tabledata[index];
                                    var buttonTag = "<button type=\"button\" class=\"btn btn-info btn-sm \" type=\"button\"  onclick=\"view(" + item.nic + );\" >View Transaction</button>" ;
                                     var row = [
                                                item.serialno, 
                                                item.lotterynumber, 
                                                item.mobileno, 
                                                item.nic,
                                                buttonTag
                                            ];     
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
               //
        }
        */
        
        function loadTable() 
        {
                var fileid = $('#fileid').val();
                var result = {};
                
                resultTable = $('#searched_table').DataTable
                ({
                    "ajax": 
                    {
                        url: "viewSalesdetailspaginated.htm",
                        data: {
                            "fileid":fileid 
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
                    "columns": 
                    [
                        {"title": "Serial Number", "data": "serialno"},
                        {"title": "Lottery Numbers", "data": "lotterynumber", "defaultContent": "-"},
                        {"title": "Mobile Number", "data": "mobileno", "defaultContent": "-"},
                        {"title": "Nic Number", "data": "nic", "defaultContent": "-"},
                        {"title": "Action", "data": null,
                            render: function (data, type, row)
                            {
                                return "<button type=\"button\" class=\"btn btn-info btn-sm \" type=\"button\"  onclick=\"viewTranceAction('" + data.txnid + "');\" >View Transaction</button>";
                            }
                        }
                    ]
            });
    }
    
    function viewTranceAction(item)
    {
        $('#reciept #id').val(item);
        document.reciept.action = "${pageContext.servletContext.contextPath}/ticket_winning_file/list_ticket.htm";
        document.reciept.submit();
    }
    
    </script>
    
    

</html>

