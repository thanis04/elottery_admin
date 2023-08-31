<%-- 
    Document   : masterconfig
    Created on : Oct 9, 2017, 4:28:42 PM
    Author     : methsiri_h
--%>

<%@page import="com.epic.dlb.util.common.SystemVarList"%>
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
                                <h4>Risk Profile Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row" id="editPanel">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Edit Risk Profile</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <div>                                     
                                            <div class="">
                                                <input maxlength="10" class="form-control" type="hidden" name="typeName" id="typeName"/>
                                            </div>
                                        </div>
                                        <form  class="form-horizontal" id="new_form">

                                            <div class="form-group">
                                                <label>Description<span class="text-red"> *</span></label>                                                   
                                                <input maxlength="100" class="form-control" data-validation="required"  type="text" name="description" id="description" placeholder="Enter Description" />
                                            </div>
                                            <div class="form-group">
                                                <label>Value<span class="text-red"> *</span></label>                                                   
                                                <input class="form-control currency price-val" step="0.01" data-validation="required" type="text" name="value" id="value" placeholder="Enter Value" />
                                                <input class="form-control"  type="hidden" name="profileId" id="profileId"  />
                                                <!--data-validation="required"-->
                                            </div>
                                            <div class="form-group">
                                                <label>Profile<span class="text-red"> *</span></label>                                                  
                                                <select data-validation="required" class="form-control" name="dlbDeviceProfile.profileId" id="dlbDeviceProfile">
                                                    <option selected value="0">--Select--</option>
                                                    <c:forEach var="device" items="${listDeviceProfile}">
                                                        <option  value="${device.profileId}">${device.description}</option> 
                                                    </c:forEach>

                                                </select>
                                            </div>
                                            <div class="form-group">
                                                <label>Status<span class="text-red"> *</span></label>                                                  
                                                <select data-validation="required" class="form-control" name="dlbStatus.statusCode" id="statusCode">
                                                    <option selected value="0">--Select--</option>
                                                    <option value="<%=SystemVarList.ACTIVE%>">Active</option>
                                                    <option value="<%=SystemVarList.INACTIVE%>">Inactive</option>
                                                </select>
                                            </div>

                                            <div class="form-group">
                                                <div class="add-button">
                                                    <!--<button type="button" class="btn btn-secondary" onclick="resetForm()">Reset</button>-->
                                                    <button id="btn_save" type="button" class="btn submit" onclick="save()">Save</button>
                                                </div>
                                            </div>

                                            <br>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="search_result">
                            <br>
                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Profile</th>
                                        <th>Description</th>
                                        <th>Value</th> 
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="riskProfile" items="${listRiskProfile}">
                                        <tr>
                                            <td>${riskProfile.dlbDeviceProfile.description}</td>
                                            <td>${riskProfile.description}</td>
                                            <td>${riskProfile.value}</td>
                                            <td>${riskProfile.dlbStatus.description}</td>
                                            <td><span onclick="viewProfile('${riskProfile.profileId}')" class='fa fa-eye btn btn-primary' title='View Profile'></span>
                                                <span onclick="editProfile('${riskProfile.profileId}')" class='fa fa-pencil btn btn-success' title='Edit Profile'></span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
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

        var resultTable;
        $(document).ready(function () {
              $('#editPanel').hide();
            $('#typeName').val("1");

            resultTable = $('#result_table').dataTable({
                "searching": false
            });
        });


        function save() {
            var formdata = $('#new_form').serialize();
            var type = $('#typeName').val();

            if (type == "1") {
                if (validateForm($('#new_form'))) {
                    $.ajax({
                        type: 'POST',
                        url: "save.htm",
                        data: formdata,
                        success: function (data, textStatus, jqXHR) {
                            var response = JSON.parse(data);
                            showNotification(response.status, response.msg);
                            resetFrom($('#new_form'));                           
                            refreshTable();
                             $('#editPanel').hide();
                        },
                        error: function (data) {
                            var response = JSON.parse(data);
                            showNotification(response.status, response.msg);
                        }
                    });
                }
            } else if (type == 2) {

                $.ajax({
                    type: 'POST',
                    url: "update.htm",
                    data: formdata,
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);
                        showNotification(response.status, response.msg);
                        resetFrom($('#new_form'));
                        $('#typeName').val("1");
                        refreshTable();
                         $('#editPanel').hide();
                    },
                    error: function (data) {
                        var response = JSON.parse(data);
                        showNotification(response.status, response.msg);
                    }
                });
            }
        }

        function editProfile(id) {
             $('#editPanel').show();
               $('#btn_save').show();
            var dataString = "id=" + id;
            $.ajax({
                type: 'POST',
                url: "get.htm",
                data: dataString,
                success: function (data, textStatus, jqXHR) {

                    $([document.documentElement, document.body]).animate({
                        scrollTop: $("#description").offset().top
                    }, 1000);

                    var response = JSON.parse(data);
                    var record = response.record;

                    console.log(record.description);
                    console.log(record.value);
                    console.log(record.deviceProfileId);
                    console.log(record.status);

                    $('#new_form #description').val(record.description);
                    $('#new_form #description').prop('readonly', false);
                    $('#new_form #value').val(record.value);
                    $('#new_form #value').prop('readonly', false);
                    $('#new_form #profileId').val(record.deviceProfileId);
                    $('#new_form #dlbDeviceProfile').val(record.deviceProfile);
                    $('#new_form #dlbDeviceProfile').attr('disabled', false);
                    $('#new_form #statusCode').val(record.status);
                    $('#new_form #statusCode').attr('disabled', false);

                    $('#typeName').val("2");

                },
                error: function (data) {
                    var response = JSON.parse(data);
                    showNotification(response.status, response.msg);
                }
            });


        }


        function viewProfile(id) {
            $('#editPanel').show();
            $('#btn_save').hide();
            var dataString = "id=" + id;
            $.ajax({
                type: 'POST',
                url: "get.htm",
                data: dataString,
                success: function (data, textStatus, jqXHR) {

                    $([document.documentElement, document.body]).animate({
                        scrollTop: $("#description").offset().top
                    }, 1000);

                    var response = JSON.parse(data);
                    var record = response.record;

                    console.log(record.description);
                    console.log(record.value);
                    console.log(record.deviceProfileId);
                    console.log(record.status);

                    $('#new_form #description').val(record.description);
                    $('#new_form #description').prop('readonly', true);
                    $('#new_form #value').val(record.value);
                    $('#new_form #value').prop('readonly', true);
                    $('#new_form #profileId').val(record.deviceProfileId);
                    $('#new_form #dlbDeviceProfile').val(record.deviceProfile);
                    $('#new_form #dlbDeviceProfile').attr('disabled', true);
                    $('#new_form #statusCode').val(record.status);
                    $('#new_form #statusCode').attr('disabled', true);

                    $('#typeName').val("2");

                },
                error: function (data) {
                    var response = JSON.parse(data);
                    showNotification(response.status, response.msg);
                }
            });


        }

        function resetForm() {
            $('#typeName').val("1");

            $('#new_form #description').prop('readonly', false);
            $('#new_form #value').prop('readonly', false);
            $('#new_form #dlbDeviceProfile').attr('disabled', false);
            $('#new_form #statusCode').attr('disabled', false);

            resetFrom($('#new_form'));
        }

        function refreshTable() {
            $.ajax({
                type: 'POST',
                url: "list.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);
                    var record = response.record;
                    resultTable.fnClearTable();
                    $(record).each(function (index) {
                        var item = record[index];
                        var viewBtn = "<span onclick='viewProfile(\"" + item.profileId + "\")' class='fa fa-eye btn btn-primary' title='View Employee'></span>";
                        var editBtn = "<span onclick='editProfile(\"" + item.profileId + "\")' class='fa fa-pencil btn btn-success' title='Edit Employee'></span>";

                        var row = [item.deviceProfile, item.profileDes, item.value, item.status, viewBtn + " " + editBtn];
                        resultTable.fnAddData(row);

                    });


                },
                error: function (data) {
                    var response = JSON.parse(data);
                    showNotification(response.status, response.msg);
                }
            });
        }


    </script>
</html>
