<%-- 
    Document   : login
    Created on : Sep 29, 2017, 4:25:37 PM
    Author     : kasun_n
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Login Page</title>
        <!-- ----------------- CSS ------------------->
        <!-- Bootstrap -->
        <link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">

        <!-- Font Awesome -->   
        <link href="<c:url value="/resources/font-awesome/css/font-awesome.min.css"/>" rel="stylesheet">

        <!-- Login Page style -->
        <link href="<c:url value="/resources/common/css/login_page.css"/>" rel="stylesheet">
        <style>
            @import url('https://fonts.googleapis.com/css?family=Open+Sans');
        </style>

        <link href="<c:url value="/resources/custom/css/scannerjs.css"/>" rel="stylesheet">
    </head>
    <body>
        <div class="container">
            <div class="col-sm-12" align="center">
                <%--       <h3>e-lottery sales and distribution solution</h3>        --%>
                <img src='<c:url value="/resources/common/images/dlb_logo.png"/>' width="200px" class="logo_image img-responsive"/>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3">
                    <div class="panel panel-login">
                        <div class="heading">
                            <h4 class="text-primary">dashboard login</h4>                                                            
                        </div>
                        <div class="panel-body" align="center">
                            <div class="row">
                                <div class="col-sm-12">
                                    <form id="login-form" action="${pageContext.servletContext.contextPath}/login.htm" method="post" role="form" style="display: block;">
                                        <div class="login-error">
                                            <center><span id="display_msg" class="text-danger">${msg}</span></center>
                                        </div>
                                        <div class="input-append">
                                            <span class="input-icon"><i class="fa fa-user"></i></span>
                                            <input type="text" name="username" id="username" tabindex="1" class="form-input" placeholder="Username" value="">

                                        </div>
                                        <div class="input-append">
                                            <span class="input-icon"><i class="fa fa-lock"></i></span>
                                            <input type="password" name="password" id="password" tabindex="2" class="form-input" placeholder="Password">
                                        </div>
                                        <input name="public_key" id="public_key" type="hidden"/>
                                        <div align="center">
                                            <input  class="form-submit" type="submit" name="login-submit" id="login-submit" tabindex="4" value="Login">
                                            <!--<input onclick="getPublicKey();"  class="form-submit" type="button" name="login-submit" id="login-submit" tabindex="4" value="Login">-->
                                        </div>


                                    </form>
                                    <div align="center">
                                        <a href="${pageContext.servletContext.contextPath}/forget_password.htm" class="pass_reset" >
                                            Forgot your password?
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div> 

            <div class="modal fade" id="resetPw" role="dialog">
                <div class="modal-dialog">                       
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header bg-white">
                            <button type="button" class="close " data-dismiss="modal">&times;</button>
                            <h4 id="window_title" class="modal-title">Submit Username</h4>

                        </div>
                        <div class="modal-body">

                            <form class="form-horizontal" id="reset_password" action="${pageContext.servletContext.contextPath}/reset_password.htm">
                                <div class="form-group col-md-12">
                                    <label class="required-input">Username<span style="color: red"> *</span></label>
                                    <div class="">
                                        <input maxlength="16"  data-validation="required" class="form-control" type="text" name="username_pwrs" id="username_pwrs" />
                                    </div>

                                </div>
                                <div class="form-group col-md-12">
                                    <span id="display_err" class="text-danger">Username cannot be empty</span>
                                </div>
                                <div class="form-group col-md-2"></div>
                                <div class="form-group col-md-10" 
                                     style="background-color: #f8efc0; color: #eb9316; border-radius: 10px;">
                                    <!--<label class="required-input">Username<span style="color: red"> *</span></label>-->
                                    <div class="">
                                        <h5><span class="fa fa-info"></span> Please enter your Username and we will send you a password reset link. </h5>

                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-lg-6">
                                        <span class="pull-left text-secondary"> Mandatory fields are marked with [<span style="color: red">*</span>] </span>
                                    </div> 
                                </div>

                            </form>                            
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                            <button id="btn_save" type="button" class="btn btn-primary " onclick="resetPassword()"> Submit </button>
                        </div>
                    </div>

                </div>
            </div>    

            <div class="modal fade" id="resetPwSuccess" role="dialog">
                <div class="modal-dialog">                       
                    <!-- Modal content-->
                    <div class="modal-content">
                        <div class="modal-header bg-white">
                            <button type="button" class="close " data-dismiss="modal">&times;</button>
                            <!--<h4 id="window_title" class="modal-title">Submit Username</h4>-->

                        </div>
                        <div class="modal-body">

                            <form class="form-horizontal" id="reset_password" >
                                <center>
                                    <div class="form-group col-md-3 "></div>
                                    <div class="form-group col-md-8 " 
                                         style="background-color: #BCF5BC; color: green; border-radius: 50px;">
                                        <!--<label class="required-input">Username<span style="color: red"> *</span></label>-->

                                        <h5> Please check your inbox on instruction to reset your password! </h5>


                                    </div>
                                </center>


                            </form>  
                            <div class="row">
                                <div class="col-lg-6">
                                    <!--<span class="pull-left text-secondary"> Mandatory fields are marked with [<span style="color: red">*</span>] </span>-->
                                </div> 
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

                        </div>
                    </div>

                </div>
            </div>  

        </div>



        <!-- footer content -->
        <jsp:include page="../common/footer.jsp"/>
        <!-- /footer content --> 
    </body>
    <script src='<c:url value="/resources/jquery/jquery.min.js"/>'></script>
    <script src='<c:url value="/resources/custom/js/custom.min.js"/>'></script>
    <script src='<c:url value="/resources/jquery/jquery-ui.js"/>'></script>
    <script src='<c:url value="/resources/custom/js/scanner.js"/>'></script>    
    <script src='<c:url value="/resources/bootstrap/js/bootstrap.min.js"/>'></script>
    <script src='<c:url value="/resources/loader/js/main.js"/>'></script>
    <script src='<c:url value="/resources/dual_list/jquery.bootstrap-duallistbox.js"/>'></script>
    <script>
//        $(document).ready(function () {
//            //option A
//            $("#login-form").submit(function (e) {
//                e.preventDefault(e);
//            });
//        });
                                $(document).ready(function () {
//                                    $('.pass_reset').on("click", function () {
//                                        $('#resetPw').modal('show');
//                                    });
                                    $("#display_err").hide();
                                });

                                var publicKey = "";

                                function getPublicKey() {

                                    var val = scanner.isConnectedToScanWebSocket();

                                    if (val) {

                                        scanner.signText(responseHandle,
                                                {
                                                    "req_param":
                                                            {
                                                                "param": $('#username').val()
                                                            }
                                                }
                                        );

                                    } else {

                                        scanner.displayInstallScanAppEnableJavaPopup(true);

                                        $.notify({
                                            icon: 'fa fa-times-circle',
                                            message: 'Please start the local service.'
                                        }, {
                                            type: 'danger',
                                            z_index: 9999
                                        });

                                    }
                                }

                                function responseHandle(successful, mesg, response) {

                                    if (!successful) { // On error
                                        $.notify({
                                            icon: 'fa fa-times-circle',
                                            message: "Error. " + mesg
                                        }, {
                                            type: 'danger',
                                            z_index: 9999
                                        });
                                        return;
                                    }

                                    if (successful && mesg !== null) {

                                        var response = $.parseJSON(mesg);

                                        if (response.STATUS) {
                                            publicKey = response.param.signedtext;
                                            $('#public_key').val(publicKey);

                                            login();


                                        } else {
                                            $('#display_msg').html(response.MSG);

                                        }

                                        return;
                                    }

                                }

                                function openPasswordResetModal() {
                                    $('#resetPw').modal('show');
                                }

                                function resetPassword() {
                                    var username = $('#username_pwrs').val();
                                    if (username === "") {
                                        $("#display_err").show();
                                    } else {

                                        var formData = {
                                            "username": username
                                        }

                                        $.ajax({
                                            url: "reset_password.htm?username=" + username,
                                            type: 'GET',
                                            success: function (data, textStatus, jqXHR) {

                                            },
                                            error: function (jqXHR, textStatus, errorThrown) {

                                            }

                                        });
//                                        showNotification("success", "Please check your inbox on instruction to reset your password");
                                        $('#resetPw').modal('toggle');
                                        $('#resetPwSuccess').modal('show');
                                        $('#username_pwrs').val("");
                                    }
                                }

                                function login() {
                                    $('#login-form').submit();
                                }
    </script>
</html>
