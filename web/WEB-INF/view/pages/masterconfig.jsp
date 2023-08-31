<%-- 
    Document   : masterconfig
    Created on : Oct 9, 2017, 4:28:42 PM
    Author     : methsiri_h
--%>

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
                            <div class="title_left">
                                <h4>Server Configuration</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-lg-6 col-lg-offset-3 col-md-offset-3 col-sm-offset-2 col-xs-offset-0" style="float: none;">
                                <div class="x_panel">

                                    <!--********************************--> 

                                    <!--                                    <div class="modal-header bg-default">
                                                                            <h4 id="window_title" class="modal-title">Set Master Configuration</h4>
                                                                        </div>-->
                                    <div class="modal-body">
                                        <!-- model body -->
                                        <form class="form-horizontal" id="new_form" >
                                            <c:set var="configData" value ="${masterConfigerdata}"/>

                                            <div class="form-group">
                                                <label class="required-input">ID<span class="text-red"> *</span></label>
                                                <input data-validation="required" maxlength="10" class="form-control" type="text" name="id" id="id" value="${configData.id}"/>
                                            </div>

                                            <div class="form-group">
                                                <label class="required-input">Max Pool<span class="text-red"> *</span></label>
                                                <input data-validation="required" n class="form-control" type="number" name="maxPool" id="maxPool" value="${configData.maxPool}" />
                                            </div>                                         

                                            <div class="form-group">
                                                <label class="required-input">Min Pool<span class="text-red"> *</span></label>
                                                <input data-validation="required" class="form-control" type="number" name="minPool" id="minPool" value="${configData.minPool}"/>
                                            </div>                                         

                                            <div class="form-group">
                                                <label class="required-input">Max Pool Queue<span class="text-red"> *</span></label>
                                                <input data-validation="required" class="form-control" type="number" name="maxQueueSize" id="maxQueueSize" value="${configData.maxQueueSize}" />
                                            </div>                                         



                                            <div class="form-group">
                                                <label class="required-input">Log Level<span class="text-red"> *</span></label>
                                                <input data-validation="required" class="form-control" type="number" name="logLevel" id="logLevel" value="${configData.logLevel}"/>
                                            </div>                                         

                                            <div class="form-group">
                                                <label class="required-input">No. of Log Files<span class="text-red"> *</span></label>
                                                <input data-validation="required" class="form-control" type="number" name="nofLogFileMaintain" id="nofLogFileMaintain" value="${configData.nofLogFileMaintain}" />
                                            </div>                                         

                                            <div class="form-group">
                                                <label class="required-input">Log Backup Status<span class="text-red"> *</span></label>
                                                <div class="selectContainer">
                                                    <select data-validation="required"  class="form-control" name="dlbStatusByLogBackupStatus.statusCode" id="statusCodeBackUp">
                                                        <option selected value="0">--Select--</option>
                                                        <option value="1"${configData.dlbStatusByLogBackupStatus.statusCode=='1'?'selected':''}>Active</option>
                                                        <option value="2"${configData.dlbStatusByLogBackupStatus.statusCode=='2'?'selected':''}>Inactive</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="required-input">Validation Status<span class="text-red"> *</span></label>
                                                <div class="selectContainer">
                                                    <select data-validation="required" class="form-control" name="dlbStatusByStatus.statusCode" id="statusCode">
                                                        <option selected value="0">--Select--</option>
                                                        <option value="1"${configData.dlbStatusByStatus.statusCode=='1'?'selected':''} >Active</option>
                                                        <option value="2"${configData.dlbStatusByStatus.statusCode=='2'?'selected':''}>Inactive</option>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="required-input">Log Backup<span class="text-red"> *</span></label>
                                                <input data-validation="required-sp-allow" maxlength="200" class="form-control" type="text" name="logBackupPath" id="logBackupPath" value="${configData.logBackupPath}"/>
                                            </div>

                                            <div class="form-group" align="right">
                                                <button id="btn_save" type="button" class="btn btn-secondary " onclick="this.form.reset()"> Reset </button>
                                                <button id="btn_update" type="button" class="btn submit" onclick="updateItem()"> Save </button>
                                            </div>

                                        </form>




                                        <!--********************************-->



                                    </div>
                                </div>
                            </div>
                        </div>


                        <!-- /page content -->

                        <!-- footer content -->
                        <jsp:include page="../common/footer.jsp"/>
                        <!-- /footer content -->
                    </div>
                </div>
            </div>
    </body>

    <script>




        

        function updateItem() {
            var formdata = $('#new_form').serialize();

            if (validateForm($('#new_form'))) {
                $.ajax({
                    data: formdata,
                    type: 'POST',
                    url: "update.htm",
                    success: function (data) {
                        var response = JSON.parse(data);
                        showNotification(response.status, response.msg);
                    },
                    error: function (data) {
                        var response = JSON.parse(data);
                        showNotification(response.status, response.msg);
                    }
                });
            }
        }
    </script>
</html>
