<%-- 
    Document   : password_reset_header
    Created on : Jul 22, 2021, 9:29:55 AM
    Author     : nipuna_k
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- ----------------- CSS ------------------->
<!-- Bootstrap -->
<link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css"/>" rel="stylesheet">

<!-- Font Awesome -->   
<link href="<c:url value="/resources/font-awesome/css/font-awesome.min.css"/>" rel="stylesheet">

<!-- NProgress -->   
<link href="<c:url value="/resources/nprogress/nprogress.css"/>" rel="stylesheet">

<!-- Custom Theme Style -->   
<link href="<c:url value="/resources/custom/css/custom.css"/>" rel="stylesheet">

<!-- Data table Style -->   
<link href="<c:url value="/resources/datatables/css/dataTables.bootstrap.min.css"/>" rel="stylesheet">

<!-- Data Controller -->   
<link href="<c:url value="/resources/form_controller/formValidator.css"/>" rel="stylesheet">

<!-- Loader Style -->   
<link href="<c:url value="/resources/loader/css/main.css"/>" rel="stylesheet">

<!-- Loader Style -->   
<link href="<c:url value="/resources/jquery/jquery-ui.min.css"/>" rel="stylesheet">
<link href="<c:url value="/resources/jquery/jquery.notify.css"/>" rel="stylesheet">

<!-- circle buttons -->   
<link href="<c:url value="/resources/custom/css/circle_buttons.css"/>" rel="stylesheet">


<!-- toggle -->
<link href="<c:url value="/resources/toggle/css/bootstrap-toggle.min.css"/>" rel="stylesheet">

<!-- dual_list-->
<script src='<c:url value="/resources/dual_list/bootstrap-duallistbox.css"/>'></script>

<!--Sweet Alert-->
<link href="<c:url value="/resources/sweetalert/sweetalert.css"/>" rel="stylesheet">

<!--Loading icon-->
<div id="loader-wrapper">
    <div id="loader"></div>
    <div class="loader-section section-left"></div>
    <div class="loader-section section-right"></div>

</div>

<div class="top_nav">
    <div class="nav_menu">
        <nav>
            <ul class="head-style">
                <li>
                    <div class="nav toggle">
                        <a id="menu_toggle"><i class="fa fa-bars"></i></a>
                    </div>
                </li>
                <li>
                    <div class="header-headline">
                        <h4>
                            e-Lottery Sales & Distribution Solution
                        </h4>
                    </div>
                </li>
            </ul>
<!--            <ul class="nav navbar-nav navbar-right">
                <li class="">
                    <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <i class="fa fa-user fa-4x" aria-hidden="true"></i>  ${sessionScope.employee.name} (${sessionScope.user.username})
                        <span class=" fa fa-angle-down"></span>
                    </a>
                    <ul class="dropdown-menu dropdown-usermenu pull-right">                            
                        <li><a href="${pageContext.servletContext.contextPath}/logout.htm"><i class="fa fa-sign-out pull-right"></i> Log Out</a></li>
                        <li><a href="${pageContext.servletContext.contextPath}/passwordChange/show_page.htm"><i class="fa fa-pencil pull-right"></i> Password Change</a></li>
                    </ul>
                </li>


            </ul>-->
        </nav>
    </div>
    <!--JS-->
    <!-- jQuery -->
    <script src='<c:url value="/resources/jquery/jquery.min.js"/>'></script>

    <!-- Bootstrap -->
    <script src='<c:url value="/resources/bootstrap/js/bootstrap.min.js"/>'></script>

    <!-- FastClick -->
    <script src='<c:url value="/resources/fastclick/fastclick.js"/>'></script>

    <!-- NProgress -->
    <script src='<c:url value="/resources/nprogress/nprogress.js"/>'></script>

    <!-- Custom Theme Scripts -->
    <script src='<c:url value="/resources/custom/js/custom.min.js"/>'></script>

    <!-- datatables.net -->
    <script src='<c:url value="/resources/datatables/js/jquery.dataTables.min.js"/>'></script>
    <script src='<c:url value="/resources/datatables/js/dataTables.bootstrap.min.js"/>'></script>

    <!-- form controller-->
    <script src='<c:url value="/resources/form_controller/formValidator.js"/>'></script>
    <script src='<c:url value="/resources/form_controller/formJSONFactory.js"/>'></script>

    <!-- loader-->
    <script src='<c:url value="/resources/loader/js/main.js"/>'></script>

    <!-- jquery UI and notify-->
    <script src='<c:url value="/resources/jquery/jquery-ui.js"/>'></script>
    <script src='<c:url value="/resources/jquery/jquery.notify.min.js"/>'></script>
    <script src='<c:url value="/resources/bootstrap/js/bootstrap-notify.min.js"/>'></script>

    <!-- system Notification-->
    <script src='<c:url value="/resources/notification/systemNotification.js"/>'></script>    

    <!-- dual_list-->
    <script src='<c:url value="/resources/dual_list/jquery.bootstrap-duallistbox.js"/>'></script>

    <!--Sweet Alert-->
    <script src='<c:url value="/resources/sweetalert/sweetalert.min.js"/>'></script>


    <script src='<c:url value="/resources/testjs/jquery.maskMoney.js"/>'></script>

    <script src='<c:url value="/resources/testjs/jquery.maskedinput.js"/>'></script>

    <script src='<c:url value="/resources/firebase/firebase.js"/>'></script>

    <script>
        //show and hide loading icon for all ajax call
        $(document).ready(function () {
            $('#loader').fadeOut();
            $('#loader-wrapper').delay(400).fadeOut('slow')
        });

        $(document).ajaxStart(function () {
            $('#loader').fadeIn();
            $('#loader-wrapper').fadeIn('slow');
        });

        $(document).ajaxComplete(function () {
            $('#loader').fadeOut();
            $('#loader-wrapper').fadeOut('slow')
        });

        $(document).ajaxError(function () {
            $('#loader').fadeOut();
            $('#loader-wrapper').fadeOut('slow')
        });

        $(document).ready(function () {
            $("[type*='number']").keydown(function (e) {
                // Allow: backspace, delete, tab, escape, enter and .
                if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
                        // Allow: Ctrl+A, Command+A
                                (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) ||
                                // Allow: home, end, left, right, down, up
                                        (e.keyCode >= 35 && e.keyCode <= 40)) {
                            // let it happen, don't do anything
                            return;
                        }
                        // Ensure that it is a number and stop the keypress
                        if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                            e.preventDefault();
                        }
                    });
        });


    </script>
    <!-- toggle-->
    <script src='<c:url value="/resources/toggle/js/bootstrap-toggle.min.js"/>'></script>


</div>
