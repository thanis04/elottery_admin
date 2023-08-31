<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
                    <div class="">
                        <div class="page-title">
                            <div class="title_left">
                                <h3>Dashboard</h3>
                            </div>

                            <!--                            <div class="title_right">
                                                            <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
                                                                <div class="input-group">
                                                                    <input type="text" class="form-control" placeholder="Search for...">
                                                                    <span class="input-group-btn">
                                                                        <button class="btn btn-default" type="button">Go!</button>
                                                                    </span>
                                                                </div>
                                                            </div>
                                                        </div>-->
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>Dashboard</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                                            </li>
                                            <li class="dropdown">
                                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>

                                            </li>
                                            <li><a class="close-link"><i class="fa fa-close"></i></a>
                                            </li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
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
    <script>

        $(document).ready(function () {

            loadDashboardValues();

        });
        
        
        //search function
        function loadDashboardValues() {
            var days = $('#days').val();

            $.ajax({
                url: "dashboard/load.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    //clear table
                    var response = JSON.parse(data);
                    if (response.status === true) {
                        console.info(response);

                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }
    </script>
</html>
