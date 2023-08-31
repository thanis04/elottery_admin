<%-- 
    Document   : sub_agent_reg
    Created on : Aug 31, 2021, 7:48:28 AM
    Author     : nipuna_k
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
        <link href="<c:url value="/resources/custom/css/scannerjs.css"/>" rel="stylesheet">
        <script src='<c:url value="/resources/custom/js/scanner.js"/>'></script>    

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
                                <h4>Sub Agent Registration</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <!--col-md-6 col-lg-6--> 

                        <div class="row">
                            <div class="col-sm-10 col-lg-offset-1 col-md-offset-1 " style="float:none !important;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>New Sub Agent</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="new_form">

                                            <div class="form-group col-lg-6">
                                                <label >Sub Agent Code<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control" type="text" name="subAgentCode" id="subAgentCode" maxlength="15" />
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label >First Name<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control" type="text" name="firstName" id="firstName" maxlength="15" />
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label >Last Name<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control" type="text" name="lastName" id="lastName" maxlength="15" />
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label>Gender<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <div class="form-group">
                                                        <div class="selectContainer">
                                                            <select data-validation="required" class="form-control" name="gender" id="gender">
                                                                <option selected value="0">--Select--</option>
                                                                <option value="Central">Male</option>
                                                                <option value="Eastern">Female</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-12">
                                                <label >Address<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control" type="text" name="address" id="address" maxlength="15" />
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label>Province<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <div class="form-group">
                                                        <div class="selectContainer">
                                                            <select data-validation="required" class="form-control" name="province" id="province">
                                                                <option selected value="0">--Select--</option>
                                                                <option value="Central">Central</option>
                                                                <option value="Eastern">Eastern</option>
                                                                <option value="North Central">North Central</option>
                                                                <option value="Northern">Northern</option>
                                                                <option value="North Western">North Western</option>
                                                                <option value="Sabaragamuwa">Sabaragamuwa</option>
                                                                <option value="Southern">Southern</option>
                                                                <option value="Uva">Uva</option>
                                                                <option value="Western">Western</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label >District<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <div class="form-group">
                                                        <div class="selectContainer">
                                                            <select data-validation="required" class="form-control" name="district" id="district">
                                                                <option selected value="0">--Select--</option>
                                                                <option value="Ampara">Ampara</option>
                                                                <option value="Anuradhapura">Anuradhapura</option>
                                                                <option value="Batticaloa">Batticaloa</option>
                                                                <option value="Colombo">Colombo</option>
                                                                <option value="Galle">Galle</option>
                                                                <option value="Gampaha">Gampaha</option>
                                                                <option value="Hambantota">Hambantota</option>
                                                                <option value="Jaffna">Jaffna</option>
                                                                <option value="Kalutara">Kalutara</option>
                                                                <option value="Kandy">Kandy</option>
                                                                <option value="Kegalle">Kegalle</option>
                                                                <option value="Kilinochchi">Kilinochchi</option>
                                                                <option value="Kurunegala">Kurunegala</option>
                                                                <option value="Mannar">Mannar</option>
                                                                <option value="Matale">Matale</option>
                                                                <option value="Matara">Matara</option>
                                                                <option value="Monaragala">Monaragala</option>
                                                                <option value="Mullaitivu">Mullaitivu</option>
                                                                <option value="Nuwara Eliya">Nuwara Eliya</option>
                                                                <option value="Polonnaruwa">Polonnaruwa</option>
                                                                <option value="Puttalam">Puttalam</option>
                                                                <option value="Ratnapura">Ratnapura</option>
                                                                <option value="Trincomalee">Trincomalee</option>
                                                                <option value="Vavuniya">Vavuniya</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            
                                            <div class="form-group col-lg-6">
                                                <label >Email<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control" type="text" name="email" id="email" maxlength="15" />
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label >Mobile<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control" type="text" name="mobile" id="mobile" maxlength="15" />
                                                </div>
                                            </div>
                                            
                                            <div class="form-group col-lg-6">
                                                <label >Alternative Contact Number<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control" type="text" name="alternativeContactNo" id="alternativeContactNo" maxlength="15" />
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label >NIC/Passport/DL No<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control" type="text" name="nic" id="nic" maxlength="15" />
                                                </div>
                                            </div>
                                            
                                            <div class="form-group">
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                    <button type="button" class="btn submit" onclick="saveItem()">Submit</button>
                                                </div>
                                            </div>

                                            <br>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="search_result">
                            <div class="x_title" align="center">
                                <h4>Search Result</h4>                                        
                                <div class="clearfix"></div>
                            </div>
                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Employee Code</th>
                                        <th>Name</th>                                                          
                                        <th>Address</th>
                                        <th>Contact No</th>
                                        <th>E-mail</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
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
        var publicKey = "";
        var actionType = "";

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

        }

        var resultTable;
        $(document).ready(function () {

            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false,
                "paging": true
            });

            //hide update button
            $('#btn_update').hide();

        });

        function hideShowButton() {
            //hide/show button
            $('#btn_update').hide();
            $('#view_update_icon').hide();
            $('#btn_save').show();
            $('#new_form #product').prop('readonly', false);
            $('#new_form #btn_reset').show();
            $("#window_title").html("Add New Employee List")
            resetNewFrom();
        }

        function saveItem() {
            if (validateForm($('#new_form'))) {
                var formData = $('#new_form').serialize();
                if (validateForm($('#new_form'))) {
                    $.ajax({
                        data: formData,
                        type: 'POST',
                        url: "save.htm",
                        success: function (data, textStatus, jqXHR) {
                            var response = JSON.parse(data);

                            //hide window if record is ok
                            if (response.status === 'success') {
                                $('#addNew').modal('toggle');
                            }

                            search();

                            //show msg
                            showNotification(response.status, response.msg);
                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }

                    });
                }
            }
        }

        function resetSearchFrom() {
            var form = $('#new_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        function resetNewFrom() {
            var form = $('#new_form');
            $('#new_form #employeeid').prop('readonly', false);
            $('#new_form *').attr('disabled', false);
            resetFrom(form);
        }

        function closeModal() {
            $('#addNew').modal('toggle');
        }

    </script>
</html>

