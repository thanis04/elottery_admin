<%-- 
    Document   : password_reset
    Created on : Jul 22, 2021, 9:08:25 AM
    Author     : nipuna_k
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>

        <link href="<c:url value="/resources/custom/css/scannerjs.css"/>" rel="stylesheet">
        <script src='<c:url value="/resources/custom/js/scanner.js"/>'></script>    

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

    <body class="nav-md" >
        <div class="container body">
            <div class="main_container">
                <!-- include side bar  -->
                <jsp:include page="../common/password_reset_other.jsp"/>
                <!-- top navigation -->
                <jsp:include page="../common/password_reset_header.jsp"/>
                <!-- /top navigation -->

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left">
                                <h4>Password Reset</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form  autocomplete="off"   class="form-horizontal" id="password_change_form">
                                            <div class="form-group">
                                                <label class="">New Password<span class="text-red">*</span></label>
                                                <div class="">
                                                    <input data-validation="required-sp-allow" class="form-control"  type="password" name="new_password" id="new_password" placeholder="Enter New Password" maxlength="100"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Confirm Password<span class="text-red">*</span></label>
                                                <div class="">
                                                    <input data-validation="required-sp-allow" class="form-control"  type="password" name="confirm_password" id="confirm_password" placeholder="Enter Confirm Password" maxlength="100"/>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label"></label>
                                                <div class="">
                                                    <div class="add-button">
                                                        <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                        <button type="button" class="btn submit" onclick="saveItem();">Submit</button>
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                        </form>
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
//( document ).ready() block.
        var resultTable;
        var publicKey = "";





        function saveItem() {

            if ($('#new_password').val() === "" || $('#confirm_password').val() === "") {
                showNotification("error", "Please fill all the fields");
            } else {

                if ($('#new_password').val() === $('#confirm_password').val()) {

                    var formData = {
                        "password": $('#new_password').val()
                    }

                    $.ajax({
                        data: formData,
                        type: 'POST',
                        url: "reset_password.htm",
                        success: function (data, textStatus, jqXHR) {
                            var response = JSON.parse(data);
                            //hide window if record is ok
                            if (response.status === 'success') {
                                console.log("success");
                                showNotification(response.status, response.msg);
                                var form = $('#password_change_form');
                                resetFrom(form);
                            } else {
                                showNotification(response.status, response.msg);
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            console.log(errorThrown);
                        }
                         
                    });
                    showNotification("success", "Password updated successfully");
                    window.location.replace("${pageContext.servletContext.contextPath}/logout.htm");
                } else {
                    showNotification("error", "The new password and confirmation password do not match");
                }
//            }
            }
        }

        function resetSearchFrom() {
            var form = $('#password_change_form');
            resetFrom(form);
        }




    </script>
</html>