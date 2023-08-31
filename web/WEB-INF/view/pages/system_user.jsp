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
                                <h4>System User Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search System User</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">
                                            <div class="">
                                                <div class="">
                                                    <div class="form-group">
                                                        <label class="">Username</label>
                                                        <div class="">
                                                            <input maxlength="50" class="form-control" type="text" name="username" id="username" placeholder="Enter Username" />
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <label class="">User Role</label>
                                                        <div class="">                                                    
                                                            <select data-validation="required" type="text" class="form-control" name="dlbWbUserRole.userrolecode" id="userrolecode"  >
                                                                <option selected value="0">--Select Role--</option>
                                                                <c:forEach var="item" items="${user_role_select_box}">
                                                                    <option value="${item.userrolecode}">${item.description}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </div>

                                                    <div class="form-group">
                                                        <div class="">

                                                            <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew"> Add New </button>

                                                            <div class="add-button">
                                                                <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                                <button type="button" class="btn submit" onclick="search();">Search</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <br/>

                                            </div>
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
                                        <th>User Name</th>
                                        <th>Employee Name</th>                                                          
                                        <th>User Role</th>                                                           
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>


                                </tbody>
                            </table>
                        </div>
                    </div>


                    <!-- /page content -->

                    <!-- Add new Modal -->
                    <div class="modal fade" id="addNew" role="dialog">
                        <div class="modal-dialog">                       
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header bg-white">
                                    <button type="button" class="close " data-dismiss="modal">&times;</button>
                                    <h4 id="window_title" class="modal-title">Add New System User</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">

                                    <form class="form-horizontal" id="new_form" >
                                        <div class="form-group col-md-6">
                                            <label class="">User Name<span class="text-red"> *</span></label>
                                            <div class="">
                                                <input maxlength="60" data-validation="required" class="form-control" type="text" name="username" id="username" />
                                            </div>
                                        </div>

                                        <div class="form-group col-md-6">
                                            <label class="">Employee<span class="text-red"> *</span></label>
                                            <div class="">                                                    
                                                <select data-validation="required" type="text" class="form-control" name="dlbWbEmployee.employeeid" id="employeeid"  >
                                                    <option selected value="0">--Select--</option>
                                                    <c:forEach var="item" items="${employee_select_box}">
                                                        <option value="${item.employeeid}">${item.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>       


                                        <div id="passwordRow">
                                            <div class="form-group col-md-6">
                                                <label class="">Password<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input maxlength="100" data-validation="required-sp-allow" class="form-control"  type="password" name="password" id="password" />
                                                </div>
                                            </div>                                         
                                            <div class="form-group col-md-6">
                                                <label class="">Confirm Password<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input maxlength="100" data-validation="required-sp-allow" class="form-control" type="password" name="con_password" id="con_password" />
                                                </div>
                                            </div>

                                        </div>


                                        <div class="form-group col-md-6">
                                            <label class="">Status<span class="text-red"> *</span></label>
                                            <div class="">
                                                <div class="form-group">
                                                    <div class="selectContainer">
                                                        <select data-validation="required" class="form-control" name="dlbStatus.statusCode" id="statusCode">
                                                            <option selected value="0">--Select--</option>
                                                            <option value="<%=SystemVarList.ACTIVE%>">Active</option>
                                                            <option value="<%=SystemVarList.INACTIVE%>">Inactive</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="form-group col-md-6">
                                            <label class="">User Role<span class="text-red"> *</span></label>
                                            <div class="">                                                    
                                                <select data-validation="required" type="text" class="form-control" name="dlbWbUserRole.userrolecode" id="userrolecode"  >
                                                    <option selected value="0">--Select--</option>
                                                    <c:forEach var="item" items="${user_role_select_box}">
                                                        <option value="${item.userrolecode}">${item.description}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>  


                                        <div class="form-group">
                                            <label class=""></label>
                                            <div class="">

                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-lg-6">
                                                <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                            </div> 
                                        </div>
                                    </form>                            
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button id="btn_reset1"type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                    <button id="btn_update" type="button" class="btn btn-primary" onclick="updateItem()"> Update </button>
                                    <button id="btn_save" type="button" class="btn submit " onclick="saveItem()"> Add </button>
                                </div>
                            </div>

                        </div>
                    </div>
                    <!-- change password Modal -->
                    <div class="modal fade" id="changePasswdModal" role="dialog">
                        <div class="modal-dialog">                       
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header bg-primary">
                                    <button type="button" class="close " data-dismiss="modal">&times;</button>
                                    <h4 id="window_title" class="modal-title">Change User Password</h4>
                                </div>
                                <div class="modal-body">
                                    <!-- model body -->
                                    <form class="form-horizontal" id="change_pass_form" >
                                        <div class="form-group">
                                            <label class="control-label col-sm-3">User Name<span class="text-danger"> *</span></label>
                                            <div class="col-sm-7">
                                                <input onblur="validateForm($('#new_form');" data-validation="required" class="form-control" type="text" name="username" id="username" />
                                            </div>
                                        </div> 

                                        <div class="form-group">
                                            <label class="control-label col-sm-3">Password:<span class="text-danger"> *</span></label>
                                            <div class="col-sm-7">
                                                <input onblur="validateForm($('#new_form');" data-validation="required-sp-allow" class="form-control"  type="password" name="password" id="password" maxlength="20" />
                                            </div>
                                        </div>                                        

                                        <div class="form-group">
                                            <label class="control-label col-sm-3">Confirm Password: <span class="text-danger"> *</span></label>
                                            <div class="col-sm-7">
                                                <input onblur="validateForm($('#new_form');" data-validation="required-sp-allow" class="form-control" type="password" name="con_password" id="con_password" maxlength="20" />
                                            </div>
                                        </div>  
                                        <div class="form-group">
                                            <label class="control-label col-sm-3"></label>
                                            <div class="col-sm-7">
                                                <!--<button id="btn_save" type="button" class="btn btn-success " onclick="saveItem()"> Add </button>-->                                           
                                                <button id="btn_update_pass" type="button" class="btn btn-primary" onclick="updatePassword()"> Update Password </button>
                                                <button id="btn_reset"type="button" class="btn btn-default" onclick="resetChnageForm()" >Reset</button>

                                            </div>
                                        </div>
                                    </form>                            
                                </div>
                                <div class="modal-footer">
                                    <span class="pull-left text-danger">[*] mark as required</span>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
        var Base64 = {

// private property
            _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

// public method for encoding
            encode: function (input) {
                var output = "";
                var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
                var i = 0;

                input = Base64._utf8_encode(input);

                while (i < input.length) {

                    chr1 = input.charCodeAt(i++);
                    chr2 = input.charCodeAt(i++);
                    chr3 = input.charCodeAt(i++);

                    enc1 = chr1 >> 2;
                    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;

                    if (isNaN(chr2)) {
                        enc3 = enc4 = 64;
                    } else if (isNaN(chr3)) {
                        enc4 = 64;
                    }

                    output = output +
                            this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
                            this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

                }

                return output;
            },

// public method for decoding
            decode: function (input) {
                var output = "";
                var chr1, chr2, chr3;
                var enc1, enc2, enc3, enc4;
                var i = 0;

                input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

                while (i < input.length) {

                    enc1 = this._keyStr.indexOf(input.charAt(i++));
                    enc2 = this._keyStr.indexOf(input.charAt(i++));
                    enc3 = this._keyStr.indexOf(input.charAt(i++));
                    enc4 = this._keyStr.indexOf(input.charAt(i++));

                    chr1 = (enc1 << 2) | (enc2 >> 4);
                    chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                    chr3 = ((enc3 & 3) << 6) | enc4;

                    output = output + String.fromCharCode(chr1);

                    if (enc3 != 64) {
                        output = output + String.fromCharCode(chr2);
                    }
                    if (enc4 != 64) {
                        output = output + String.fromCharCode(chr3);
                    }

                }

                output = Base64._utf8_decode(output);

                return output;

            },

// private method for UTF-8 encoding
            _utf8_encode: function (string) {
                string = string.replace(/\r\n/g, "\n");
                var utftext = "";

                for (var n = 0; n < string.length; n++) {

                    var c = string.charCodeAt(n);

                    if (c < 128) {
                        utftext += String.fromCharCode(c);
                    } else if ((c > 127) && (c < 2048)) {
                        utftext += String.fromCharCode((c >> 6) | 192);
                        utftext += String.fromCharCode((c & 63) | 128);
                    } else {
                        utftext += String.fromCharCode((c >> 12) | 224);
                        utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                        utftext += String.fromCharCode((c & 63) | 128);
                    }

                }

                return utftext;
            },

// private method for UTF-8 decoding
            _utf8_decode: function (utftext) {
                var string = "";
                var i = 0;
                var c = c1 = c2 = 0;

                while (i < utftext.length) {

                    c = utftext.charCodeAt(i);

                    if (c < 128) {
                        string += String.fromCharCode(c);
                        i++;
                    } else if ((c > 191) && (c < 224)) {
                        c2 = utftext.charCodeAt(i + 1);
                        string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                        i += 2;
                    } else {
                        c2 = utftext.charCodeAt(i + 1);
                        c3 = utftext.charCodeAt(i + 2);
                        string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                        i += 3;
                    }

                }

                return string;
            }

        }

        var resultTable;
        $(document).ready(function () {

            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            //hide update button
            $('#btn_update').hide();

        });

        function hideShowButton() {
            //hide/show button
            $('#btn_update').hide();
            $('#btn_update_pass').hide();
            $('#view_update_icon').hide();
            $('#btn_save').show();
            $('#btn_reset1').show();
            $('#passwordRow').show();
            $('#con_password').attr('required-sp-allow', 'required-sp-allow');
            $('#password').attr('data-validation', 'required-sp-allow');
            $('#new_form #product').prop('readonly', false);
            $('#new_form #username').prop('readonly', false);
            $("#window_title").html("Add New User ")
            resetNewFrom();




        }

        //search function
        function search() {
            var formData = $('#search_form').serialize();

            $.ajax({
                data: formData,
                url: "search.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    //clear table
                    var response = JSON.parse(data);

                    if (response.status === true) {
                        $('#search_result').show();
                        resultTable.fnClearTable();

                        //set data table data
                        var tabledata = response.search_result;
                        $(tabledata).each(function (index) {
                            var item = tabledata[index];
                            var changePw = "<span onclick='changePassword(\"" + item.username + "\")' class='fa fa fa-lock btn btn-primary' title='Change Password'></span>";
                            var editBtn = "<span onclick='editItem(\"" + item.username + "\")' class='fa fa-pencil btn btn-success' title='Edit '></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.username + "\")' class='fa fa-trash btn btn-danger' title='Delete '></span>";

                            var row = [item.username, item.employee, item.userRole, item.status, changePw + " " + editBtn + " " + delBtn];
                            resultTable.fnAddData(row);
                        });
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function saveItem() {
            var formData = $('#new_form').serialize();


            if (validateForm($('#new_form'))) {

                var password = $('#new_form #password').val();
                var con_password = $('#new_form #con_password').val();

                if (password === con_password) {
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

                            //show msg
                            showNotification(response.status, response.msg);



                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }

                    });
                } else {
                    showNotification('warning', 'Password and confirm password is different');
                }



            }

        }
        function updateItem() {
            var formData = $('#new_form').serialize();

            if (validateForm($('#new_form'))) {
                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "update.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //hide window if record is ok
                        if (response.status === 'success') {
                            $('#addNew').modal('toggle');
                        }

                        //show msg
                        showNotification(response.status, response.msg);

                        //refresh table
                        search();




                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }

        }
        function updatePassword() {
            var formData = $('#change_pass_form').serialize();

            if (validateForm($('#change_pass_form'))) {
                var password = $('#change_pass_form #password').val();
                var con_password = $('#change_pass_form #con_password').val();

                if (password === con_password) {
                    $.ajax({
                        data: formData,
                        type: 'POST',
                        url: "update_password_admin.htm",
                        success: function (data, textStatus, jqXHR) {
                            var response = JSON.parse(data);

                            //hide window if record is ok
                            if (response.status === 'success') {
                                $('#changePasswdModal').modal('toggle');
                            }

                            //show msg
                            showNotification(response.status, response.msg);

                            //refresh table
                            search();




                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }

                    });
                } else {
                    showNotification('warning', 'Password and confirm password is different');
                }


            }

        }


        function editItem(id) {
            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#addNew').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#new_form #username').val(record.username);
                        $('#new_form #employeeid').val(record.employee);
                        $('#new_form #userrolecode').val(record.userRole);
                        $('#new_form #statusCode').val(record.status);

                        //hide  passwords
                        $('#passwordRow').hide();
                        $('#btn_update_pass').show();
                        $('#password').removeAttr('data-validation');
                        $('#con_password').removeAttr('data-validation');






                        //enable/disable ui elemnts
                        $('#new_form *').attr('disabled', false);
                        $('#new_form #username').prop('readonly', true);
                        $('#btn_reset1').hide();
                        $('#btn_save').hide();
                        $('#btn_update').show();
                        $("#window_title").html("Edit System User Details: " + record.username)



                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }
        function changePassword(userName) {

            resetChnageForm();

            $('#changePasswdModal').modal().show();
            $('#change_pass_form #username').val(userName);
            $('#change_pass_form #username').prop('readonly', true);

            $('#btn_add').hide();
            $('#window_title').html("Change Password : " + userName);



        }
//        function deleteItem(id) {
//
//            //delete function
//            function deleteFunction() {
//                var dataString = "id=" + id;
//                $.ajax({
//                    data: dataString,
//                    type: 'POST',
//                    url: "delete.htm",
//                    success: function (data, textStatus, jqXHR) {
//                        var response = JSON.parse(data);
//
//                        //show msg
//                        showNotification(response.status, response.msg);
//
//                        //refresh table
//                        search();
//                    },
//                    error: function (jqXHR, textStatus, errorThrown) {
//
//                    }
//                });
//
//            }
//            //confirm box -IF 'YES' -> call deleteFunction
//            showConfirmMsg("Delete User", "Are you sure to delete user " + id + "?", deleteFunction)
//        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }

        function resetNewFrom() {
            var form = $('#new_form');
            $('#new_form #employeeid').prop('readonly', false);
            $('#new_form *').attr('disabled', false);
            resetFrom(form);
        }

        function resetChnageForm() {

            $('#change_pass_form #password').val("");
            $('#change_pass_form #con_password').val("");
            $('#change_pass_form #password').removeClass('error-indicate');
            $('#change_pass_form #con_password').removeClass('error-indicate');

        }

        function deleteItem(id) {

            //delete function
            function deleteFunction() {
                var dataString = "id=" + id;
                $.ajax({
                    data: dataString,
                    type: 'POST',
                    url: "delete.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //show msg
                        showNotification(response.status, response.msg);


                        //refresh table
                        search();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }
                });

            }
            //confirm box -IF 'YES' -> call deleteFunction
            showConfirmMsg("Delete User", "Are you sure to delete user " + id + "?", deleteFunction)
        }

    </script>
</html>
