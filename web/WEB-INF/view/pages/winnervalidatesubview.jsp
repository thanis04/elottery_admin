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
                                        <div class="row pull-right">                                           
                                            <a href="#verificationPanel"><button onclick="verify()"  id="verifybutton" class="btn btn-success"><i class="glyphicon glyphicon-check"></i> Verify</button></a>

                                            <button onclick="print();" class="btn btn-danger"><i class="glyphicon glyphicon-print"></i> Print</button>                                          
                                        </div>
                                        <br>
                                        <h4 style="font-weight: lighter">Status: <span style="color: black" id="status"></span></h4>
                                        <div class="row">
                                            <div class="col-md-4">
                                                 <label style="font-weight: lighter" >Winner's Image</label>
                                                <img width="100%" height="100%" src="${profile}"/>
                                            </div>
                                            <div class="col-md-4">
                                                <img src=""/>
                                            </div>
                                            <div class="col-md-4">
                                                <img src=""/>
                                            </div>
                                        </div>

                                        <form autocomplete="off" method="get" target="_blank"  class="form-horizontal" id="search_form" role="form">
                                            <input type="hidden" class="form-control" id ="walletid" name ="walletid" placeholder = "Select form date" value="${walletid}" />
                                            <input type="hidden" class="form-control" id ="phid" name ="phid" placeholder = "Select form date" value="${phid}" />
                                            <input type="hidden" class="form-control" id ="purchStatus" name ="purchStatus"  />                                    

                                            <br>  

                                            <br>
                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label style="font-weight: lighter" >Winner's Full Name:</label>
                                                </div>
                                                <div class="col-md-8">
                                                    <label class="">${name}</label>
                                                </div>

                                            </div>
                                            <br>         
                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label style="font-weight: lighter">NIC/DL/Passport No:</label>
                                                </div>
                                                <div class="col-md-8">
                                                    <label class="">${nic}</label>
                                                </div>

                                            </div>
                                            <br>

                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label style="font-weight: lighter">Mobile:</label>
                                                </div>
                                                <div class="col-md-8">
                                                    <label class="">${mobile}</label>
                                                </div>

                                            </div>
                                            <br>


                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label style="font-weight: lighter">Lottery Name:</label>
                                                </div>
                                                <div class="col-md-8">
                                                    <label class="">${lotteryName}</label>
                                                </div>

                                            </div>
                                            <br>

                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label style="font-weight: lighter">Serial Number:</label>
                                                </div>
                                                <div class="col-md-8">
                                                    <label class="">${serialNumber}</label>
                                                </div>
                                            </div>
                                            <br>

                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label style="font-weight: lighter">Lottery Numbers:</label>
                                                </div>
                                                <div class="col-md-8">
                                                    <label class="">${lotteryNumbers}</label>
                                                </div>
                                            </div>        
                                            <br>


                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label style="font-weight: lighter">Winning Prize:</label>
                                                </div>
                                                <div class="col-md-8">
                                                    <label class="">${winningPrize}</label>
                                                </div>
                                            </div>
                                            <br>


                                            <div class="row">
                                                <label class="control-label" style="padding-left: 52px">NIC Front</label>
                                                <div class="form-group">
                                                    <div class="col-md-6">
                                                        <img id="nicFront" 
                                                             src="<c:choose>
                                                                 <c:when test="${nicFront ne null }">data:image/jpeg;base64,${nicFront}</c:when>
                                                                 <c:otherwise><c:url value="/resources/images/no_img_mid.jpg"/></c:otherwise>
                                                             </c:choose>" height="200"/>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <label class="control-label" style="padding-left: 52px">NIC Back</label>
                                                <div class="form-group">
                                                    <div class="col-md-6">
                                                        <img id="nicFront" 
                                                             src="<c:choose>
                                                                 <c:when test="${nicBack ne null }">data:image/jpeg;base64,${nicBack}</c:when>
                                                                 <c:otherwise><c:url value="/resources/images/no_img_mid.jpg"/></c:otherwise>
                                                             </c:choose>" height="200"/>
                                                    </div>
                                                </div>
                                            </div>                                           

                                            <br>
                                        </form>



                                        <div id="verificationPanel">
                                            <h3 id="msg" style="font-weight: lighter;" >Please enter OTP sent to ${mobile} and press Submit button</h3>
                                            <br>
                                            <div class="row">
                                                <div class="col-md-3">
                                                    <label style="font-weight: lighter" >Enter OTP</label>
                                                </div>
                                                <div class="col-md-4">
                                                    <input data-validation="required" type="text" class="form-control" id="otp" name="otp" placeholder="Please Enter OTP" />
                                                </div>
                                                <div class="col-md-4">
                                                    <button onclick="verifyOtp();"  class="btn btn-primary"><i class="glyphicon glyphicon-check"></i> Submit</button>
                                                </div>

                                            </div>


                                            <br><br>
                                        </div>



                                        <div id="paybutton">
                                            <button type="button" class="btn submit" onclick="payment();">Mark As Pay</button>
                                        </div>



                                    </div>
                                </div>
                            </div>
                        </div>

                        <!--                        <div id="search_result">
                                                    <div class="x_title" align="center">
                                                        <h4>Report Data</h4>                                        
                                                        <div class="clearfix"></div>
                                                    </div>
                                                     table 
                                                    <table class="table table-striped table-bordered bulk_action" id="result_table">
                                                        <thead>
                                                            <tr>
                                                                <th>NIC</th>
                                                                <th>Mobile</th>
                                                                <th>Name</th>
                                                                <th>Serial No</th>
                                                                <th>Status</th>
                                                                <th>Winning Amount</th>                                      
                                                                <th>Action</th>                                      
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                        
                        
                                                        </tbody>
                                                    </table>
                                                </div>-->
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
    </form>

    <script>

        var resultTable;
        $('#purchStatus').val('${purchStatus}');


        $(document).ready(function () {

            statusListner('${purchStatus}');

            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });

        });


        function statusListner(status) {

            $('#verifybutton').hide();
            $('#verificationPanel').hide();
            $('#paybutton').hide();



            if (status === "21") {//init status
                $('#verifybutton').show();
                $('#verificationPanel').hide();
                $('#paybutton').hide();
                $('#status').html("Claim Pending");
                $("#status").css("color", "#00aeef");
            }

            if (status === "3") {//send otp

                $('#verificationPanel').show();
                $('#verifybutton').hide();
                $('#status').html("OTP Verification Pending");


            }

            if (status === "4") {//otp succes
                $('#verifybutton').hide();
                $('#verificationPanel').hide();
                $('#paybutton').show();
                $('#status').html("OTP Verification Success");


            }

            if (status === "5") {//otp faild
                $('#verificationPanel').show();
                $('#verifybutton').hide();
                $('#status').html("OTP Verification Failed");


            }

            if (status === "44") {
                $('#verificationPanel').hide();
                $('#verifybutton').hide();
                $('#paybutton').hide();
                $('#status').html("Paid to Winner");


            }

        }


        var interval = "";
        function verify()
        {

            var formData = $('#search_form').serialize();

            $.ajax
                    ({
                        data: formData,
                        url: "verify.htm",
                        type: 'POST',
                        success: function (data, textStatus, jqXHR)
                        {

                            var phid = $('#phid').val();
                            var response = JSON.parse(data);
                            if (response.status === true)
                            {
                                //alert("came here4");
                                if (response.updatestatus === true)
                                {
                                    //uupdate page status
                                    statusListner("3");

                                    window.location.hash = '#verificationPanel';
                                    $("#status").css("color", "blue");
                                    var data = "";

                                }

                            } else
                            {
                                //alert("came here5");
                                showNotification('error', response.msg);
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown)
                        {
                            //alert("came here4");
                        }
                    });

        }



        function payment()
        {
            //uupdate page status
            statusListner("44");
            var formData = $('#search_form').serialize();

            $.ajax
                    ({
                        data: formData,
                        url: "payment.htm",
                        type: 'POST',
                        success: function (data, textStatus, jqXHR)
                        {

                            var phid = $('#phid').val();
                            var response = JSON.parse(data);
                            if (response.status === true)
                            {
                                showNotification('success', "Payment successfully updated");
                                $("#status").css("color", "green");
                                $("#paybutton").hide();
                            } else
                            {
                                //alert("came here5");
                                showNotification('error', response.msg);
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown)
                        {
                            //alert("came here4");
                        }
                    });

        }
        function print() {
            var walletid = $('#walletid').val();
            var phid = $('#phid').val();           
            $('#search_form').attr('action', 'print.htm');
            $('#search_form').submit();
        }
       

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }

        function view(walletid)
        {
            $('#reciept #walletid').val(walletid);
            document.reciept.action = "${pageContext.servletContext.contextPath}/winnervalidation/view.htm";
            document.reciept.submit();
        }


        function verifyOtp()
        {


            var formData = "walletid=" + $('#walletid').val() + "&otp=" + $('#otp').val() + "&pid=${phid}";

            $.ajax
                    ({
                        data: formData,
                        url: "verify-otp.htm",
                        type: 'POST',
                        success: function (data, textStatus, jqXHR)
                        {

                            var response = JSON.parse(data);
                            if (response.updatestatus === true)
                            {
                                //uupdate page status
                                statusListner("4");
                                $('#paybutton').show();

                            } else {
                                statusListner("5");
                                $("#status").css("color", "red");
                                $('#msg').html("Invalide OTP. Please try again");
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown)
                        {
                            //alert("came here4");
                        }
                    });

        }
    </script>

</html>


