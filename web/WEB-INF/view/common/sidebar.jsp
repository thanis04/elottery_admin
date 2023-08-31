<%-- 
    Document   : sidebar
    Created on : Sep 29, 2017, 4:58:34 PM
    Author     : kasun_n
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="col-md-3 left_col">
    <div class="left_col scroll-view">
<%--
        <div class="navbar nav_title" style="border: 0; padding: 0 4px;">
            <a href="" class="site_title"><i class="fa fa-dashboard"></i>&nbsp; <span>Dashboard</span></a>  
        </div>
--%>
        <div class="clearfix"></div>

        <!-- menu profile quick info -->
        
        <div class="profile clearfix">
            <center>
            <div class="profile_pic">              
                <a href=""><img src='<c:url value="/resources/common/images/dlb_logo.png"/>' width="150px"/></a>
            </div>
            <hr>
            </center>
        

            <div class="clearfix"></div>
        </div>
        <!-- /menu profile quick info -->

        <br />

        <!-- sidebar menu -->
        <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
<!--            <div class="menu_section">              
                <ul class="nav side-menu">
                    <li><a><i class="fa fa-dashboard"></i> Dashboard <span class="fa fa-chevron-down"></span></a>

                    </li>



                </ul>
            </div>-->

            
            <div class="menu_section">
<!--                <div class="nav-title">
                    <b>Control Panel</b>
                </div> -->
                <ul class="nav side-menu">
                    <c:forEach var="section" items="${sessionScope.user_privileged_sections}">
                        <li><a><i class="fa fa-list-alt"> </i>  ${section.description} <span class="fa fa-angle-right nav-icon"></span></a>
                            <ul class="nav child_menu">
                                <c:forEach var="page" items="${sessionScope.user_privileges}">
                                    <c:if test="${section.sectioncode eq page.dlbWbSection.sectioncode}">
                                        <li><a href="${pageContext.servletContext.contextPath}${page.dlbWbPage.url}">${page.dlbWbPage.description}</a></li>
                                        </c:if>

                                </c:forEach>
                            </ul>
                        </li>

                    </c:forEach>
                </ul>
            </div>

        </div>
        <!-- /sidebar menu -->

        <!-- /menu footer buttons -->
 <!--       <div class="sidebar-footer hidden-small">
    
            <a data-toggle="tooltip" data-placement="top" title="Settings">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
            </a>
            <a data-toggle="tooltip" data-placement="top" title="FullScreen">
                <span class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
            </a>
            <a data-toggle="tooltip" data-placement="top" title="Lock">
                <span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span>
            </a>
          <a data-toggle="tooltip" data-placement="top" title="Logout" href="login.html">
                <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
            </a>
        </div> -->
        <!-- /menu footer buttons -->
    </div>
</div>

            