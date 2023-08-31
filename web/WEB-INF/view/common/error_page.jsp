<%-- 
    Document   : errorPage
    Created on : Jun 29, 2018, 4:20:20 PM
    Author     : kasun_n
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>
    </head>

    <body class="nav-md">
        <div class="container body" >
            <div class="main_container">
                <!-- include side bar  -->
                <jsp:include page="sidebar.jsp"/>
                <!-- top navigation -->
                <jsp:include page="header.jsp"/>
                <!-- /top navigation -->

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;" >
                    <br>
                    <br>
                    <br>
                    <div class="">
                         <center>
                                <div class="title_right">
                                    <h1>${error_code}</h1>
                                    <h4>${error_msg}</h4>
                                </div>
                            </center>
                        <!-- footer content -->
                        <!-- footer content -->
                        <jsp:include page="../common/footer.jsp"/>
                        <!-- /footer content -->
                        <!-- /footer content -->
                    </div>
                    <!-- /page content -->
                </div>

            </div>




    </body>
</html>