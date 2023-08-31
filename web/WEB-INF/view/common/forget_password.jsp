<%-- 
    Document   : forget_password
    Created on : Aug 9, 2021, 1:02:25 PM
    Author     : nipuna_k
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

            .success-submit{
                color: #169F85
            }
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
                            <h4 class="text-primary">Forget Password</h4>                                                            
                        </div>
                        <div class="panel-body" align="center">
                            <div class="row">
                                <div class="col-sm-12">
                                    <form id="login-form" action="${pageContext.servletContext.contextPath}/reset_password.htm" method="post" role="form" style="display: block;">
                                        <div class="">
                                            <h5><span class="fa fa-info"></span> Please enter your username and we will send you a password reset link. </h5>
                                        </div>
                                        <hr>
                                        <div class="input-append">
                                            <span class="input-icon"><i class="fa fa-user"></i></span>
                                            <input type="text" name="username" id="username" tabindex="1" class="form-input" placeholder="Username" value="">
                                        </div>
                                        <div >
                                            <center><span id="display_msg" class="${type}">${msg}</span></center>
                                        </div>
                                        <hr>
                                        <div align="center">
                                            <input  class="form-submit" type="submit" name="reset-password" id="reset-password" tabindex="4" value="Submit">
                                            <!--<input onclick="getPublicKey();"  class="form-submit" type="button" name="login-submit" id="login-submit" tabindex="4" value="Login">-->
                                        </div>


                                    </form>
                                    <div align="center">
                                        <a href="${pageContext.servletContext.contextPath}/login.htm" class="pass_reset" >
                                            Login Page
                                        </a>
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
            if (window.history.replaceState) {
                window.history.replaceState(null, null, window.location.href);
            }
            $("#display_err").hide();
        });



        function resetPassword() {
            var username = $('#username_pwrs').val();
            if (username === "") {
                $("#display_err").show();
            } else {

                var formData = {
                    "username": username
                }

                $.ajax({
                    data: formData,
                    url: "reset_password.htm",
                    type: 'POST',
                    success: function (data, textStatus, jqXHR) {
                        console.log(data);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
//                                        showNotification("success", "Please check your inbox on instruction to reset your password");
//                                        $('#resetPw').modal('toggle');
//                                        $('#resetPwSuccess').modal('show');
                $('#username_pwrs').val("");
            }
        }


    </script>
</html>

