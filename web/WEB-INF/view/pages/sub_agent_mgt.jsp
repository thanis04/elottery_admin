<%-- 
    Document   : sub_agent_mgt
    Created on : Aug 31, 2021, 12:45:37 PM
    Author     : nipuna_k
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>

        <style>
            .panel-title > a:before {
                float: right !important;
                font-family: FontAwesome;
                content:"\f068";
                padding-right: 5px;
            }
            .panel-title > a.collapsed:before {
                float: right !important;
                content:"\f067";
            }
            .panel-title > a:hover, 
            .panel-title > a:active, 
            .panel-title > a:focus  {
                text-decoration:none;
            }
        </style>
    </head>

    <body class="nav-md">
        <div class="container body">
            <div class="main_container">
                <!-- include side bar  -->
                <jsp:include page="../common/sidebar.jsp"/>
                <!-- top navigation -->
                <jsp:include page="../common/header.jsp"/>
                <!-- /top navigation -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left" >
                                <h4>Sub-Agent Management</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">

                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" class="form-horizontal" id="search_form" role="form">

                                            <div class="form-group">
                                                <label class="">Sub-Agent ID</label>
                                                <input  type="text" class="form-control" id="emp_id" 
                                                        name="emp_id" placeholder="Enter sub-agent id" />
                                            </div>

                                            <div class="form-group">
                                                <label class="">Sub-Agent Name</label>
                                                <input  type="text" class="form-control" id="emp_name" name="emp_name" placeholder="Enter sub-agent name" />
                                            </div>

                                            <div class="form-group">
                                                <button type="button" class="btn add-new" data-toggle="modal" data-target="#addNew" onclick="resetNewFrom()">Add Sub-Agent</button>
                                                <div class="add-button">
                                                    <button  type="button" class="btn btn-secondary" onclick="reset();">Reset</button>
                                                    <button type="button" class="btn submit" onclick="loadTable();">Search</button>
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
                                <h4>Report Data</h4>                                        
                                <div class="clearfix"></div>
                            </div>
                            <!-- table -->
                            <table class="table table-striped table-bordered bulk_action" id="result_table">
                                <thead>
                                    <tr>
                                        <th>Sub-Agent ID</th>
                                        <th>Sub-Agent Name</th>
                                        <th>Contact No</th>
                                        <th>Email</th>
                                        <th>Created Date</th>
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

                <div class="modal fade" id="addNew" role="dialog">
                    <div class="modal-dialog">   

                        <form class="form-horizontal" id="new_form" >
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header bg-white">
                                    <button id="btn_small_close" type="button" class="close " data-dismiss="modal">&times;</button>
                                    <h4 id="window_title" class="modal-title">Add New Sub Agent</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">
                                    <!-- model body -->

                                    <div class="form-group col-lg-12">
                                        <label>Sub Agent Code<span class="text-red"> *</span></label>
                                        <span class="text-secondary" style="font-size: 10px">
                                            (Only Characters and Numbers)
                                        </span>
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="text" name="subAgentCode" id="subAgentCode" 
                                                   maxlength="15" 
                                                   onkeydown="return /^([^ ]*)$/i.test(event.key)"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >First Name<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control"  
                                                   onkeydown="return /^([^0-9]*)$/i.test(event.key)"
                                                   type="text" name="firstName" id="firstName" maxlength="15" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >Last Name<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text"  
                                                   name="lastName" id="lastName" maxlength="15" 
                                                   onkeydown="return /^([^0-9]*)$/i.test(event.key)"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-12">
                                        <label >Address<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" 
                                                   name="address" id="address" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>Province<span class="text-red"> *</span></label>
                                        <div class="">
                                            <div class="form-group">
                                                <div class="selectContainer">
                                                    <select data-validation="required" class="form-control" name="province" id="province" onchange="selectProvince()">
                                                        <option selected value="0">--Select--</option>

                                                        <c:forEach var="item" items="${province_list}">
                                                            <option value="${item.id}">${item.name}</option>
                                                        </c:forEach>                                                              
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >District<span class="text-red"> *</span></label>
                                        <div class="">
                                            <div class="form-group">
                                                <div class="selectContainer district">
                                                    <select data-validation="required" class="form-control" name="district" id="district">
                                                        <option selected value="0">--Select--</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >Email<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input onblur="isEmail(this.value)" data-validation="required-email"
                                                   class="form-control" 
                                                   type="text" name="email" id="email" maxlength="255" 
                                                   onkeydown="return /^([^ ]*)$/i.test(event.key)" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >Mobile<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="number" name="mobile" id="mobile" maxlength="15"
                                                   onkeydown="return /^([^.]*)$/i.test(event.key)"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >Alternative Contact Number</label>
                                        <div class="">
                                            <input class="form-control" 
                                                   type="number" name="alternativeContactNo" 
                                                   id="alternativeContactNo" maxlength="15" 
                                                   onkeydown="return /^([^.]*)$/i.test(event.key)"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >NIC/Passport/DL No<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="text" name="nic" id="nic" maxlength="15" 
                                                   onkeydown="return /^([^ ]*)$/i.test(event.key)" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-12" id="accordion" role="tablist" aria-multiselectable="false">

                                        <div class="panel panel-default">
                                            <div class="panel-heading" role="tab" id="headingOne">
                                                <h4 class="panel-title">
                                                    <a role="button" data-toggle="collapse" href="#collapseOne" 
                                                       aria-expanded="true" aria-controls="collapseOne">
                                                        <b>Details of Business Entity</b> 
                                                    </a>
                                                </h4>
                                            </div>
                                            <div id="collapseOne" class="panel-collapse collapse in" 
                                                 role="tabpanel" aria-labelledby="headingOne">
                                                <div class="panel-body" style="overflow-y: scroll;">

                                                    <div class="form-group col-lg-6">
                                                        <label>Name of the Business</label>
                                                        <div class="">
                                                            <input class="form-control" 
                                                                   type="text" name="nob" id="nob" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Registered Address</label>
                                                        <div class="">
                                                            <input class="form-control" 
                                                                   type="text" name="regAddress" id="regAddress" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Business Registration Number</label>
                                                        <div class="">
                                                            <input class="form-control" 
                                                                   type="text" name="businessRegNo" id="businessRegNo" maxlength="255" />
                                                        </div>
                                                    </div>

                                                    <div class="form-group col-lg-6">
                                                        <label>Email</label>
                                                        <div class="">
                                                            <input class="form-control" data-validation="email"
                                                                   type="email" name="businessEmail" 
                                                                   id="businessEmail" maxlength="255" />
                                                        </div>
                                                    </div>

                                                    <div class="form-group col-lg-6">
                                                        <label>Phone</label>
                                                        <div class="">
                                                            <input class="form-control" 
                                                                   type="number" name="businessPhoneNo" 
                                                                   id="businessPhoneNo" maxlength="255" 
                                                                   onkeydown="return /^([^.]*)$/i.test(event.key)"/>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>

                                        <div class="panel panel-default">
                                            <div class="panel-heading" role="tab" id="headingTwo">
                                                <h4 class="panel-title">
                                                    <a class="collapsed" role="button" data-toggle="collapse" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                                                        <b>Bank Details</b>
                                                    </a>
                                                </h4>
                                            </div>
                                            <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                                                <div class="panel-body">
                                                    <div class="form-group col-lg-6">
                                                        <label>Bank Name<span class="text-red"> *</span></label>
                                                        <div class="">
                                                            <input data-validation="required" class="form-control" 
                                                                   type="text" name="bankName" id="bankName" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Branch Name<span class="text-red"> *</span></label>
                                                        <div class="">
                                                            <input data-validation="required" class="form-control" 
                                                                   type="text" name="branchName" id="branchName" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Account Number<span class="text-red"> *</span></label>
                                                        <div class="">
                                                            <input data-validation="required" class="form-control" 
                                                                   type="number" name="accoutNumber" id="accoutNumber" maxlength="255" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>

                                        <div class="panel panel-default" id="doc_upload">
                                            <div class="panel-heading" role="tab" id="headingThree">
                                                <h4 class="panel-title">
                                                    <a class="collapsed" role="button" data-toggle="collapse" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                                                        <b>Document Upload</b>
                                                    </a>
                                                </h4>
                                            </div>
                                            <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
                                                <div class="panel-body">
                                                    <div class="form-group col-lg-6">
                                                        <label>NIC/Passport Copy<span class="text-red"> *</span></label>
                                                        <div class="">
                                                            <input class="form-control" data-validation="required-sp-allow" 
                                                                   accept=".jpg,.jpeg,.png,.pdf"
                                                                   type="file" name="nicFile" 
                                                                   id="nicFile" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Scanned Copy of Passbook<span class="text-red"> *</span></label>
                                                        <div class="">
                                                            <input class="form-control"  data-validation="required-sp-allow" 
                                                                   accept=".jpg,.jpeg,.png,.pdf"
                                                                   type="file" name="scanCopyPassbookFile" 
                                                                   id="scanCopyPassbookFile" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Proof of Address</label>
                                                        <div class="">
                                                            <input class="form-control" data-validation="file" type="file" 
                                                                   accept=".jpg,.jpeg,.png,.pdf"
                                                                   name="proofOfAddressFile" id="proofOfAddressFile"/>
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Business Registration</label>
                                                        <div class="">
                                                            <input class="form-control" data-validation="file" type="file" 
                                                                   accept=".jpg,.jpeg,.png,.pdf"
                                                                   name="businessRegFile" id="businessRegFile"/>
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>

                                        </div>
                                    </div>


                                    <div class="row">
                                        <div class="form-group col-lg-6">
                                            <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                        </div>    
                                    </div>


                                </div>

                                <div class="modal-footer">
                                    <button id="btn_close" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button id="btn_reset"type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                    <button id="btn_save" type="button" class="btn submit " onclick="saveItem()"> Submit </button>
                                </div>
                            </div>
                        </form>  
                    </div>
                </div>

                <div class="modal fade" id="viewDet" role="dialog">
                    <div class="modal-dialog">
                        <form class="form-horizontal" id="view_form" >
                            <!-- Modal content-->
                            <div class="modal-content">
                                <div class="modal-header bg-white">
                                    <button id="btn_small_close" type="button" class="close " data-dismiss="modal">&times;</button>
                                    <h4 id="window_title" class="modal-title">View Sub Agent</h4>
                                    <hr class="hr" />
                                </div>

                                <div class="modal-body">
                                    <!-- model body -->

                                    <div class="form-group col-lg-12">
                                        <label >Sub Agent Code</label>
                                        <span class="text-secondary" style="font-size: 10px">
                                            (Only Characters and Numbers)
                                        </span>
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="text" name="subAgentCode" id="subAgentCode" 
                                                   maxlength="15" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >First Name</label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="text" name="firstName" id="firstName" maxlength="15" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>Last Name</label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text"  
                                                   name="lastName" id="lastName" maxlength="15"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-12">
                                        <label >Address</label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" 
                                                   name="address" id="address" />
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>Province</label>
                                        <div class="">
                                            <div class="form-group">
                                                <div class="selectContainer">
                                                    <select data-validation="required" class="form-control" name="province" id="province" onchange="selectProvince()">
                                                        <option selected value="0">--Select--</option>

                                                        <c:forEach var="item" items="${province_list}">
                                                            <option value="${item.id}">${item.name}</option>
                                                        </c:forEach>                                                              
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>District</label>
                                        <div class="">
                                            <div class="form-group">
                                                <div class="selectContainer district">
                                                    <input data-validation="required" class="form-control" type="text"  
                                                           name="districtName" id="districtName" maxlength="15"/>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>Email</label>
                                        <div class="">
                                            <input data-validation="required-email"
                                                   class="form-control" 
                                                   type="text" name="email" id="email" maxlength="255"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>Mobile</label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="number" name="mobile" id="mobile" maxlength="15"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>Alternative Contact Number</label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="number" name="alternativeContactNo" 
                                                   id="alternativeContactNo" maxlength="15"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label>NIC/Passport/DL No</label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" 
                                                   type="text" name="nic" id="nic" maxlength="15"/>
                                        </div>
                                    </div>

                                    <div class="form-group  col-lg-12" id="view_accordion" role="tablist"  aria-multiselectable="false">

                                        <div class="panel panel-default">
                                            <div class="panel-heading" role="tab" id="view_headingOne">
                                                <h4 class="panel-title">
                                                    <a role="button" data-toggle="collapse" href="#view_collapseOne" 
                                                       aria-expanded="true" aria-controls="view_collapseOne">
                                                        <b>Details of Business Entity</b> 
                                                    </a>
                                                </h4>
                                            </div>
                                            <div id="view_collapseOne" class="panel-collapse collapse in" 
                                                 role="tabpanel" aria-labelledby="view_headingOne">
                                                <div class="panel-body" style="overflow-y: scroll;">

                                                    <div class="form-group col-lg-6">
                                                        <label>Name of the Business</label>
                                                        <div class="">
                                                            <input class="form-control" 
                                                                   type="text" name="nob" id="nob" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Registered Address</label>
                                                        <div class="">
                                                            <input class="form-control" 
                                                                   type="text" name="regAddress" id="regAddress" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Business Registration Number</label>
                                                        <div class="">
                                                            <input class="form-control" 
                                                                   type="text" name="businessRegNo" id="businessRegNo" maxlength="255" />
                                                        </div>
                                                    </div>

                                                    <div class="form-group col-lg-6">
                                                        <label>Email</label>
                                                        <div class="">
                                                            <input class="form-control" data-validation="email"
                                                                   type="email" name="businessEmail" 
                                                                   id="businessEmail" maxlength="255" />
                                                        </div>
                                                    </div>

                                                    <div class="form-group col-lg-6">
                                                        <label>Phone</label>
                                                        <div class="">
                                                            <input class="form-control" 
                                                                   type="number" name="businessPhoneNo" 
                                                                   id="businessPhoneNo" maxlength="255" />
                                                        </div>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>

                                        <div class="panel panel-default">
                                            <div class="panel-heading" role="tab" id="view_headingTwo">
                                                <h4 class="panel-title">
                                                    <a class="collapsed" role="button" data-toggle="collapse" href="#view_collapseTwo" aria-expanded="false" aria-controls="view_collapseTwo">
                                                        <b>Bank Details</b>
                                                    </a>
                                                </h4>
                                            </div>
                                            <div id="view_collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="view_headingTwo">
                                                <div class="panel-body">
                                                    <div class="form-group col-lg-6">
                                                        <label>Bank Name</label>
                                                        <div class="">
                                                            <input data-validation="required" class="form-control" 
                                                                   type="text" name="bankName" id="bankName" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Branch Name</label>
                                                        <div class="">
                                                            <input data-validation="required" class="form-control" 
                                                                   type="text" name="branchName" id="branchName" maxlength="255" />
                                                        </div>
                                                    </div>
                                                    <div class="form-group col-lg-6">
                                                        <label>Account Number</label>
                                                        <div class="">
                                                            <input data-validation="required" class="form-control" 
                                                                   type="number" name="accoutNumber" id="accoutNumber" maxlength="255" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>

                                        <div class="panel panel-default" id="view_upload">
                                            <div class="panel-heading" role="tab" id="view_headingFour">
                                                <h4 class="panel-title">
                                                    <a class="collapsed" role="button" data-toggle="collapse" href="#view_collapseFour" aria-expanded="false" aria-controls="view_collapseFour">
                                                        <b>Uploaded Documents</b>
                                                    </a>
                                                </h4>
                                            </div>
                                            <div id="view_collapseFour" class="panel-collapse collapse" role="tabpanel" aria-labelledby="view_headingFour">
                                                <div class="panel-body">
                                                    <div class="form-group col-md-6" id="nicView">

                                                    </div>
                                                    <div class="form-group col-md-6" id="passbook">

                                                    </div>
                                                    <div class="form-group col-md-6" id="proofAdd">

                                                    </div>
                                                    <div class="form-group col-md-6" id="businessReg">

                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <span class="pull-left text-secondary"></span>

                                    </div>    
                                </div>
                                <div class="modal-footer">
                                    <button id="btn_close" type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <!--                                    <button id="btn_reset"type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                                                        <button id="btn_save" type="button" class="btn submit " onclick="saveItem()"> Submit </button>-->
                                </div>
                            </div>
                        </form>  
                    </div>
                </div>

                <jsp:include page="../common/footer.jsp"/>
            </div>
        </div>
    </body>
    <script>




        var resultTable = "";
        var moreData = "";

        var nicFileName = "";
        var passbook = "";
        var proofAddress = "";
        var bisRegName = "";

        $(document).ready(function () {
            $("#collapseOne").collapse('show');
            $("#collapseTwo").collapse('show');
            $("#collapseThree").collapse('show');
            //datatable
            $('#search_result').hide();
//            resultTable = $('#result_table').dataTable({
//                "searching": false
//            });
            resultTable = null;
            //set date selecter         
            $(function () {
                $(".date").datepicker({dateFormat: 'yy-mm-dd', "maxDate": new Date});
            });

            $('#proofOfAddressFile').prop('required', false);
            $('#businessRegFile').prop('required', false);
        });

//        $('#nicFile').change(function () {
//            var fileSource = "C:/elottery_tmp/";
//            var fileName = $('#nicFile').val().replace(/C:\\fakepath\\/i, '');
//            var tmpPath = fileSource;
//            fileSource = tmpPath + "//" + fileName;
//        });


        function selectProvince() {
            var formData = {
                "id": $('#province').val()
            }

            $.ajax({
                data: formData,
                type: 'GET',
                url: "getDistricts.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);
                    var totalHtml = "";
                    totalHtml = totalHtml + '<select data-validation="required" ' +
                            'class="form-control" name="district" id="district"> ' +
                            '<option selected value="0">--Select--</option> ';
                    $(response.district_list).each(function (index) {
                        var row = response.district_list[index];
                        totalHtml = totalHtml + '<option value="' + row.id + '">' + row.name + '</option> ';
                    });
                    totalHtml = totalHtml + '</select>'
                    $('.district').html(totalHtml);
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }

        function reset() {
            $('#search_result').hide();
            var form = $('#search_form');
            resetFrom(form);
            resultTable.fnClearTable();
        }

        function loadTable() {

            var section = "";
            var empID = $('#emp_id').val();
            var empName = $('#emp_name').val();

            if (empID === "" || empID === null) {
                empID = "-";
            }

            if (empName === "" || empName === null) {
                empName = "-";
            }

            if (resultTable !== null) {
                resultTable.destroy();
                resultTable = null;
            }
            $('#search_result').show();

            resultTable = $('#result_table').DataTable
                    ({

                        "ajax":
                                {
                                    url: "show_data.htm",
                                    data: {
                                        "empId": empID,
                                        "empName": empName
                                    },
                                    type: "GET",
                                    error: function (xhr, status, error)
                                    {
                                        alert(status);
                                    }
                                },
                        "paging": true,
                        "serverSide": true,
                        "processing": true,
                        "order": [],
                        "search": false,
                        "header": true,
                        "columns":
                                [
                                    {"title": "Sub-Agent ID", "data": "emp_id", "defaultContent": "-"},
                                    {"title": "Sub-Agent Name", "data": "emp_name", "defaultContent": "-"},
                                    {"title": "Contact No", "data": "emp_contactno", "defaultContent": "-"},
                                    {"title": "Email", "data": "emp_email", "defaultContent": "-"},
                                    {"title": "Created Date", "data": "emp_created", "defaultContent": "-"},
                                    {"title": "Status", "data": "emp_status", "defaultContent": "-"},
                                    {"title": "Action", "data": null,
                                        render: function (data, type, row)
                                        {
                                            return "<button type=\"button\" class=\"btn btn-info btn-sm \" type=\"button\"  onclick=\"getDetail('" + data.emp_id + "');\" >View</button>";
                                        }
                                    }
                                ]
                    });

        }

        function getDetail(id) {
            resetViewFrom();
            $('#view_form input').attr('readonly', 'readonly');
            $('#viewDet').modal('toggle');

            var formData = {
                "agentCode": id
            }

            $.ajax({
                data: formData,
                type: 'GET',
                url: "getSubAgent.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);
                    var record = response.record;
                    console.log(response);
                    $("#view_form #subAgentCode").val(record.subAgentCode);
                    $("#view_form #firstName").val(record.firstName);
                    $("#view_form #lastName").val(record.lastName);
                    $("#view_form #address").val(record.address);
                    $("#view_form #province").val(record.province);
                    $("#view_form #districtName").val(record.districtName);
                    $("#view_form #email").val(record.email);
                    $("#view_form #mobile").val(record.mobile);
                    $("#view_form #alternativeContactNo").val(record.alternativeContactNo);
                    console.log(record.nic);
                    $("#view_form #nic").val(record.nic);

                    if (record.business !== undefined) {
                        $("#view_form #businessEmail").val(record.business.businessEmail);
                        $("#view_form #businessPhoneNo").val(record.business.businessPhoneNo);
                        $("#view_form #businessRegNo").val(record.business.businessRegNo);
                        $("#view_form #nob").val(record.business.nob);
                        $("#view_form #regAddress").val(record.business.regAddress);
                    }

                    $("#view_form #accoutNumber").val(record.bank.accoutNumber);
                    $("#view_form #bankName").val(record.bank.bankName);
                    $("#view_form #branchName").val(record.bank.branchName);

                    if (record.nic_file !== "-") {
                        var nicHTML = "<label>NIC/Passport Copy</label><div class=''><span onclick='downloadNIC(\"" + record.nic_file.file + "\")' class='fa fa-download btn btn-primary' title='Download Content'></span></div>";
                        $('#nicView').html(nicHTML);
                        nicFileName = record.subAgentCode;
                        nicFileName = nicFileName + "_NIC/Passport Copy";
                    }
                    if (record.passBook !== "-") {
                        var passbookHTML = "<label>Scanned Copy of Passbook </label><div class=''><span onclick='downloadPassbook(\"" + record.passBook.file + "\")' class='fa fa-download btn btn-primary' title='Download Content'></span></div>";
                        $('#passbook').html(passbookHTML);
                        passbook = record.subAgentCode;
                        passbook = passbook + "_Scanned Copy of Passbook";
                    }
                    if (record.proofAddress_File !== "-") {
                        var proofAddHTML = "<label>Proof of Address</label><div class=''><span onclick='downloadProofAddress(\"" + record.proofAddress_File.file + "\")' class='fa fa-download btn btn-primary' title='Download Content'></span></div>";
                        $('#proofAdd').html(proofAddHTML);
                        proofAddress = record.subAgentCode;
                        proofAddress = proofAddress + "_Proof of Address";
                    }
                    if (record.businessReg_File !== "-") {
                        var businessRegHTML = "<label>Business Registration</label><div class=''><span onclick='downloadBusinessReg(\"" + record.businessReg_File.file + "\")' class='fa fa-download btn btn-primary' title='Download Content'></span></div>";
                        $('#businessReg').html(businessRegHTML);
                        bisRegName = record.subAgentCode;
                        bisRegName = bisRegName + "_Business Registration";
                    }

                    $("#view_form #province").attr("disabled", true);
                    $("#view_form #district").attr("disabled", true);
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }

        function downladFileNIC(record) {
            var file = new File([record.nic.file], record.subAgentCode + "-NIC",
                    {type: record.nic.type, lastModified: Date.now()});

        }

        function saveItem() {

            $('#proofOfAddressFile').prop('required', false);
            $('#businessRegFile').prop('required', false);

            $("#collapseOne").collapse('show');
            $("#collapseTwo").collapse('show');
            $("#collapseThree").collapse('show');

            if (isValid($('#new_form'))) {
                if (nicFile.files[0] !== undefined) {
                    var iSize = (nicFile.files[0].size / 1024);
                    iSize = (Math.round(iSize * 100) / 100)
                    if (iSize > 1024) {
                        showNotification("error", "NIC/Passport file size should be below 1MB");
                        return;
                    }
                }
                if (scanCopyPassbookFile.files[0] !== undefined) {
                    var iSize = (scanCopyPassbookFile.files[0].size / 1024);
                    iSize = (Math.round(iSize * 100) / 100)
                    if (iSize > 1024) {
                        showNotification("error", "Scanned Copy of Passbook file size should be below 1MB");
                        return;
                    }
                }
                if (proofOfAddressFile.files[0] !== undefined) {
                    var iSize = (proofOfAddressFile.files[0].size / 1024);
                    iSize = (Math.round(iSize * 100) / 100)
                    if (iSize > 1024) {
                        showNotification("error", "Proof of Address file size should be below 1MB");
                        return;
                    }
                }
                if (businessRegFile.files[0] !== undefined) {
                    var iSize = (businessRegFile.files[0].size / 1024);
                    iSize = (Math.round(iSize * 100) / 100)
                    if (iSize > 1024) {
                        showNotification("error", "Business Registration file size should be below 1MB");
                        return;
                    }
                }
                var fieldData = createJSONObject($('#new_form'));
//                var formData = $('#new_form').serialize();
                var formData = new FormData();
                formData.append("form_data", JSON.stringify(fieldData));
                formData.append("nicFile", nicFile.files[0]);
                formData.append("scanCopyPassbookFile", scanCopyPassbookFile.files[0]);
                formData.append("proofOfAddressFile", proofOfAddressFile.files[0] === undefined ? null : proofOfAddressFile.files[0]);
                formData.append("businessRegFile", businessRegFile.files[0] === undefined ? null : businessRegFile.files[0]);

                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "save.htm",
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);
                        //hide window if record is ok
                        if (response.status === 'success') {
                            $('#addNew').modal('toggle');
                            resetNewFrom();
                        }

                        showNotification(response.status, response.msg);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });

            }
        }

        function resetViewFrom() {
            nicFileName = "";
            passbook = "";
            proofAddress = "";
            bisRegName = "";
            $("#view_collapseOne").collapse('hide')
            $("#view_collapseTwo").collapse('hide')
            $("#view_collapseThree").collapse('hide')
            var form = $('#view_form');
            resetFrom(form);
        }

        function resetNewFrom() {
            $("#collapseOne").collapse('hide')
            $("#collapseTwo").collapse('hide')
            $("#collapseThree").collapse('hide')
            var form = $('#new_form');
            resetFrom(form);
        }

        function downloadNIC(subAgentId) {
            window.open("download.htm?fileId=" + subAgentId + "&fileName=" + nicFileName);
        }

        function downloadPassbook(subAgentId) {
            window.open("download.htm?fileId=" + subAgentId + "&fileName=" + passbook);
        }
        function downloadProofAddress(subAgentId) {
            window.open("download.htm?fileId=" + subAgentId + "&fileName=" + proofAddress);
        }
        function downloadBusinessReg(subAgentId) {
            window.open("download.htm?fileId=" + subAgentId + "&fileName=" + bisRegName);
        }


        function isValid(form) {
            //reset style
            $(form).find("input").removeClass('error-indicate');
            $(form).find("select").removeClass('error-indicate');
            $(form).find("textarea").removeClass('error-indicate');
            $(form).find("file").removeClass('error-indicate');


            $('#errorMsg').removeClass("alert");//reset old alerts
            $('#errorMsg').html("");
            var inputs = form.find('input');//get inputs from Form    
            var selects = form.find('select');//get select from Form
            var textareas = form.find('textarea');//get textarea from Form
            var files = form.find('file');//get files



            var errors = 0;

//------------------------form input boxes validations---------------------------------------//
            $(inputs).each(function (index) {

                var input = inputs[index];
                $(input).removeClass('bg-pink');
                var valType = $(input).attr('data-validation');

                console.log(input);
                console.log("value-> " + valType);
                if (valType === 'required') {
                    var val = $(input).val().trim();
                    if (isEmpty(val)) {
                        errors++;
                        $(input).addClass('error-indicate');
                    }
                }

                if (valType === 'required-email') {
                    var val = $(input).val().trim();

                    if (isEmpty(val)) {
                        errors++;
                        $(input).addClass('error-indicate');
                    } else {
                        if (!isEmail(val)) {
                            errors++;
                            showNotification('error', "Please enter valid e-mail address ");
                            $(input).addClass('error-indicate');

                        }
                    }
                }


                if (valType === 'required-sp-allow') {//check is required
                    var val = $(input).val().trim();
                    if (isEmpty(val)) {
                        errors++;
                        $(input).addClass('error-indicate');

                    }
                }

                var val = $(input).val().trim();
                if (!isEmpty(val)) {
                    if (valType === 'required-sp-allow' || valType === 'required-email' || valType === 'email' || valType === 'sp-allow' || valType === 'file') {

                    } else {
                        if (!isAllowedTexts(val)) {
                            errors++;
                            //indicate error                
                            $(input).addClass('error-indicate');
                        }
                    }
                }
//--------------------required telephone no fields validations
                if (valType === 'required-tel') {
                    var val = $(input).val().trim();
                    if (isEmpty(val)) {
                        errors++;
                        $(input).addClass('error-indicate');
                    } else {
                        if (!isPhonenumber(val)) {//check telephone
                            errors++;//increment errors count   
                            $(input).addClass('error-indicate');
                            $(input).attr('placeholder', 'Please enter valid number');
                            showNotification('error', "Please enter valid phone number");
                        }
                    }
                }
//--------------------telephone no fields validations
                if (valType === 'tel') {//check is required
                    var val = $(input).val().trim();
                    if (val.length > 0) {//check user is type email or not
                        //user type email
                        if (!isPhonenumber(val)) {
                            errors++;//increment errors count                   
                            $('#errorMsg').html("Please enter valid phone no ");
                            showNotification('error', "Please enter a valid phone number");
                            $(input).addClass('error-indicate');
                        }
                    }
                }

//--------------------only number fields validations
                if (valType === 'number') {
                    if (isNumber(val)) {
                        errors++;//increment errors count
                        //indicate error                
                        $(input).addClass('error-indicate');
                        $(input).attr('placeholder', 'Please enter number');
                    }
                }


                //leangth validation 
                if (valType === 'length') {//
                    var minlength = $(input).attr('minlength');
                    var val = $(input).val().trim();
                    if (val.length < minlength) {
                        errors++;//increment errors count
                        //indicate error                
                        $(input).addClass('error-indicate');
                        $(input).attr('placeholder', 'value is too short');
                    }
                }

                //--------------------------input type validation---------------------//
                var inputType = $(input).attr('type');

                //password validation
                if (inputType === "password") {//check is password input           
                    var minlength = $(input).attr('minlength');
                    var val = $(input).val().trim();
                    if (val.length < minlength) {
                        errors++;//increment errors count
                        //indicate error                
                        $(input).addClass('error-indicate');
                        $(input).attr('placeholder', 'Password is too short');
                    }
                }

                if (valType === "required-nic") {//check is nic
                    var val = $(input).val().trim();
                    if (isEmpty(val)) {
                        $(input).addClass('error-indicate');
                        errors++;
                    } else {
                        if (val.length > 0 && val.length < 13) {//check user is type nic or not
                            //user type nic
                            if (!isNIC(val)) {
                                errors++;//increment errors count                   

                                showNotification('error', "Please enter valid NIC number ");
                                $(input).addClass('error-indicate');
                            }
                        }
                    }
                }

                //nic validation
                if (valType === "nic") {//check is nic
                    var val = $(input).val().trim();
                    if (val.length > 0 && val.length < 13) {//check user is type nic or not
                        //user type nic
                        if (!isNIC(val)) {
                            errors++;//increment errors count                   
                            $('#errorMsg').html("Please enter valid NIC number ");
                            //showNotification('error', "Please enter valid NIC number ");
                            $(input).addClass('error-indicate');

                        }
                    }

                }


                //date validation
                if (valType === "date") {//check is nic
                    var val = $(input).val().trim();

                    if (!isEmpty(val)) {
                        if (!isDate(val)) {//check user is type nic or not
                            //user type nic
                            errors++;//increment errors count                   
                            showNotification('error', "Please enter valid date ");
                            $(input).addClass('error-indicate');
                        }
                    }

                }



                if (valType === "required-date") {//check is nic
                    var val = $(input).val().trim();
                    if (isEmpty(val)) {
                        $(input).addClass('error-indicate');
                        errors++;
                    } else {
                        if (!isDate(val)) {
                            errors++;
                            $(input).addClass('error-indicate');
                        }
                    }
                }
            });

            $(textareas).each(function (index) {//loop inputs
                var textarea = textareas[index];//get input object
                $(textarea).removeClass('bg-pink');//remove already error indicate
                var valType = $(textarea).attr('data-validation');//get input validation type        
                //--------------------required fields validations
                if (valType === 'required') {//check is required
                    var val = $(textarea).val().trim();
                    if (isEmpty(val)) {
                        errors++;
                        $(textarea).addClass('error-indicate');
                    }
                }
            });



//------------------------select boxes validations---------------------------------------//
            $(selects).each(function (index) {//loop selects
                var select = selects[index];//get input object
                var val = $.trim($(select).val().trim());
                var valType = $(select).attr('data-validation');//get input validation type

                if (valType === 'required') {//check is required
                    if (isSelectBoxEmpty(val)) {
                        errors++;//increment errors count
                        //indicate error  
                        $(select).addClass('error-indicate');
                    }
                }
            });



            $(files).each(function (index) {
                var file = files[index];
                $(file).removeClass('bg-pink');
                var valType = $(file).attr('data-validation');

                console.log("file - > " + valType);
                if (valType === 'required') {
                    var val = $(file).val().trim();
                    if (isEmpty(val)) {
                        errors++;
                        $(file).addClass('error-indicate');
                    }
                }
            });

            if (errors > 0) {
                $('#errorMsg').html("<br>Please enter valid data to indicated fields ");
                $('#errorMsg').addClass("alert");
                showNotification('error', "Please enter valid data to indicated fields ");
                return false;
            } else {
                return true;
            }


        }

    </script>
</html>
