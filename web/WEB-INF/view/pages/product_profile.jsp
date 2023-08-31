<%@page import="com.epic.dlb.util.common.SystemVarList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>
        <style>

            .modal-dialog{
                overflow-y: initial !important
            }
            .modal-product-profile{
                height: 600px !important;
                overflow-y: auto;
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

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left">
                                <h4>Lottery Profile Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-12 col-xs-12 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float: none;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Lottery Profile</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">
                                            <div class="form-group">
                                                <label>Lottery <span class="text-red">*</span></label>
                                                <div>
                                                    <select data-validation="required" type="text" class="form-control" name="productCode" id="productCode"  >
                                                        <option selected value="0">--Select--</option>
                                                        <c:forEach var="item" items="${product_select_box}">
                                                            <option value="${item.productCode}">${item.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">                                
                                                    <label>Day <span class="text-red">*</span></label>
                                                <div>
                                                    <select data-validation="required" type="text" class="form-control" name="dayCode" id="dayCode">
                                                        <option selected value="0">--Select--</option>
                                                        <c:forEach var="item" items="${day_select_box}">
                                                            <option value="${item.dayCode}">${item.description}</option>        
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <button type="button" onclick="hideShowButton();" class="btn add-new" data-backdrop="static" data-keyboard="false" data-toggle="modal" data-target="#addNew">Add New</button>
                                                <!--<button type="button" onclick="hideShowButton();">Add New</button>-->
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                    <button type="button" class="btn submit" onclick="searchProduct();">Search</button>
                                                </div>
                                            </div>

                                            <br>

                                            <div id="search_result">
                                                <div class="x_title">
                                                    <h4>Search Result</h4>                                              
                                                    <div class="clearfix"></div>
                                                </div>
                                                <!-- table -->
                                                <table class="table table-striped table-bordered bulk_action" id="result_table">
                                                    <thead>
                                                        <tr>                                                                                                   
                                                            <th>Description</th>
                                                            <th>Status</th>
                                                            <th>Action</th>                                                      
                                                        </tr>
                                                    </thead>
                                                    <tbody>


                                                    </tbody>
                                                </table>
                                            </div>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>


                    </div>
                </div>


                <!-- /page content -->

                <!-- Add new Modal -->
                <div class="modal fade" id="addNew" role="dialog">
                    <div class="modal-dialog pop_window">                       
                        <!-- Modal content-->
                        <div class="modal-content" style="width: 780px;">
                            <div class="modal-header bg-white">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4 id="window_title" class="modal-title">Add New Lottery Profile</h4>

                            </div>
                            <div class="modal-body modal-product-profile">

                                <form autocomplete="off" class="form-horizontal" id="new_form"  >
                                    <div class="row">
                                        <div class="form-group col-lg-6">
                                            <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                        </div>   
                                    </div>
                                    <input id="id" name="id" type="hidden" value="0"/>

                                    <div class="form-group col-lg-6">
                                        <label >Lottery: <span class="text-red"> *</span></label>
                                        <div class="">
                                            <select data-validation="required" type="text" class="form-control" name="productCode" id="productCode"  >
                                                <option selected value="0">--Select--</option>
                                                <c:forEach var="item" items="${product_select_box}">
                                                    <option value="${item.productCode}">${item.description}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label >Day:<span class="text-red"> *</span></label>
                                        <div class="">
                                            <select data-validation="required" type="text" class="form-control" name="dayCode" id="dayCode"  >
                                                <option selected value="0">--Select--</option>
                                                <c:forEach var="item" items="${day_select_box}">
                                                    <option value="${item.dayCode}">${item.description}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>    

                                    <div class="form-group col-lg-6">
                                        <label >Product Template(Only allow Jpeg and png):<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required-sp-allow" type="file"  class="form-control" onchange="validateImage()" id="template"  name="template">
                                            <input type="hidden" id="url" name="url">
                                        </div>
                                    </div>



                                    <div class="form-group col-lg-6">
                                        <label >Status:<span class="text-red"> *</span></label>
                                        <div class="">
                                            <div class="form-group">
                                                <div class="selectContainer">
                                                    <select data-validation="required" class="form-control" name="statusCode" id="statusCode">
                                                        <option selected value="0">--Select--</option>
                                                        <option value="<%=SystemVarList.ACTIVE%>">Active</option>
                                                        <option value="<%=SystemVarList.INACTIVE%>">Inactive</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!--/**************************************************************************/-->
                                    <div class="row">
                                        <div class="col-md-12">


                                            <div class="row">
                                                <div class="col-lg-12"><hr class="hr" /></div>
                                            </div>

                                            <div class="row">

                                                <div class="form-group col-lg-6">
                                                    <label >Special Draw</label>
                                                    <div class="">
                                                        <input   type="checkbox"    checked data-toggle="toggle"  cheked data-on="Yes" data-off="No" id="specialDrawCk"  name="specialDrawCk" >
                                                        <input name="specialStatus" id="specialStatus" type="hidden"/>
                                                    </div>
                                                </div>

                                                <!--                                    style="display:none"-->
                                                <div id="s" >
                                                    <div class="form-group col-lg-6" >
                                                        <label >Special No Starting Position:</label>
                                                        <div class="">
                                                            <input  type="number" min="1" max="20"  class="form-control" id="specialPos" name="specialPos" >

                                                        </div>
                                                    </div>
                                                </div> 

                                                <div class="row">

                                                    <div class="form-group col-lg-6">                                                      
                                                        <div class="">                                                       
                                                        </div>
                                                    </div>                                                    

                                                    <!--                                    style="display:none"-->
                                                    <div id="s" >
                                                        <div class="form-group col-lg-6" >
                                                            <label>Special Game Profile:</label>
                                                            <div class="">
                                                                <select  data-validation="required" class="form-control" name="gameProfile" id="gameProfile">
                                                                    <option selected value="0">--Select--</option>
                                                                    <c:forEach var="item" items="${game_profile_list}">
                                                                        <option value="${item.id}">${item.name}</option>
                                                                    </c:forEach>
                                                                    
                                                                 
                                                                </select>

                                                            </div>
                                                        </div>
                                                    </div> 
                                                </div>
                                            </div>


                                            <div class="row">
                                                <div class="col-lg-12"><hr class="hr" /></div>
                                            </div>




                                            <div class="row">

                                                <div class="form-group col-lg-6">
                                                    <label >English Letter:</label>
                                                    <div class="">
                                                        <input   type="checkbox"    checked data-toggle="toggle"  cheked data-on="Yes" data-off="No" id="engLetterCk"  name="engLetterCk" >
                                                        <input name="engLetter" id="engLetter" type="hidden"/>
                                                    </div>
                                                </div>

                                                <!--                                    style="display:none"-->
                                                <div id="s" >
                                                    <div class="form-group col-lg-6" >
                                                        <label >English Letter Position:</label>
                                                        <div class="">
                                                            <input  type="number" min="1" max="20"  class="form-control" id="engPos" name="engPos" >

                                                        </div>
                                                    </div>
                                                </div> 
                                            </div>

                                            <br>
                                            <div class="row">
                                                <div class="form-group col-lg-6">
                                                    <label >Zodiac symbol</label>
                                                    <div class="">
                                                        <input   type="checkbox"    checked data-toggle="toggle"  cheked data-on="Yes" data-off="No" id="zodiacCk"  name="zodiacCk" >
                                                        <input  name="zodiac" id="zodiac" type="hidden"/>

                                                    </div>
                                                </div>

                                                <!--                                    style="display:none"-->
                                                <div id="s" >
                                                    <div class="form-group col-lg-6" >
                                                        <label >Zodiac symbol Position:</label>
                                                        <div class="">
                                                            <input min="1" max="20"  type="number"  class="form-control" id="zodiacPos" name="zodiacPos" >

                                                        </div>
                                                    </div>
                                                </div> 
                                            </div>
                                        </div>

                                    </div>

                                    <br>

                                    <div class="row">
                                        <div class="form-group col-lg-6">
                                            <label >Bonus No</label>
                                            <div class="">
                                                <input   type="checkbox"    checked data-toggle="toggle"  cheked data-on="Yes" data-off="No" id="bonusNoCk"  name="bonusNoCk" >
                                                <input name="bonusNo" id="bonusNo" type="hidden"/>

                                            </div>
                                        </div>

                                        <!--                                    style="display:none"-->
                                        <div id="row" >
                                            <div class="form-group col-lg-6" >
                                                <label >Bonus No Position:</label>
                                                <div class="">
                                                    <input min="1" max="20"  type="number"  class="form-control" id="bonusPos" name="bonusPos" >
                                                </div>
                                            </div>
                                        </div>    
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-12"><hr class="hr" /></div>
                                    </div>

                                    <!--/**************************************************************************/-->


                                    <div class="row">
                                        <div class="col-lg-12">
                                            <label>Product Item:<span class="text-danger">*</span></label>
                                            <div class="">
                                                <select  class="form-control" name="product_item" id="product_item">
                                                    <option selected value="0">--Select--</option>
                                                    <c:forEach var="item" items="${product_item_list}">
                                                        <option value="${item.itemCode}">${item.description}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="">
                                                <br/>
                                                <button type="button" class="btn add-new" onclick="addProductItem()">Add</button>
                                                <hr class="hr" />
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div  style="border: gainsboro solid thin;padding: 2px;" id="view_icon_pnl">
                                            <img id="view_icon_add"  width="100%" />
                                        </div>
                                    </div>


                                </form> 


                                <p class="text-red">* Click on Item to Remove Items</p>
                                <br>
                                <div class="row" style="min-height: 100px;">
                                    <br>
                                    <form id="items_form">
                                        <div id="items" class="col-md-12">

                                        </div>
                                    </form>
                                </div>

                            </div>

                            <div class="modal-footer">

                                <button id="btn_update" type="button" class="btn btn-primary" onclick="updateItem()">Update</button>
                                <button id="btn_new_reset"  class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                <button id="btn_save" type="button" class="btn submit" onclick="clickSave();">Save</button>
                            </div>

                        </div>

                    </div>

                </div>

                <!-- View Modal -show selected item   -->
                <div class="modal fade" id="viewModel" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <!--<button type="button" class="close " data-dismiss="modal">&times;</button>-->
                                <h4  class="modal-title">Lottery: <span id="view_title"></span></h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <div class="row">
                                    <div class="col-md-8">
                                        <form class="form-horizontal" >
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Lottery Name: </label>
                                                <div class="col-sm-7">
                                                    <label class="control-label" id="view_lotteryName" ></label>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Day: </label>
                                                <div class="col-sm-7">                                          
                                                    <label class="control-label "  id="view_day" ></label>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Status: </label>
                                                <div class="col-sm-7">
                                                    <div class="form-group">                                                
                                                        <label class="control-label " id="view_status" ></label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Special Status: </label>
                                                <div class="col-sm-7">
                                                    <div class="form-group">                                                 
                                                        <label class="control-label " id="view_sp_draw" ></label>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="control-label col-sm-4">Items: </label>
                                                <div class="col-sm-8">
                                                    <div class="form-group">                                                
                                                        <label class="control-label " id="view_productItem" ></label>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                    </div>

                                </div>

                                <div class="row">

                                    <br>                                
                                    <div class="col-md-12">   
                                        <div  style="border: gainsboro solid thin;padding: 2px;">
                                            <img id="view_icon"  width="100%" />
                                        </div>

                                    </div>


                                </div>



                            </div>
                            <div class="modal-footer">                              
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
    var productItems;
    $(document).ready(function () {


        $("#engPos").attr('disabled', true);
        $("#bonusPos").attr('disabled', true);
        $("#zodiacPos").attr('disabled', true);
        $("#specialPos").attr('disabled', true);
        $("#gameProfile").attr('disabled', true);

        $('#engLetterCk').bootstrapToggle('off');
        $('#zodiacCk').bootstrapToggle('off');
        $('#bonusNoCk').bootstrapToggle('off');
        $('#specialDrawCk').bootstrapToggle('off');

        //init data table
        $('#search_result').hide();
        resultTable = $('#result_table').dataTable({
            "searching": false
        });
        productItems = $('#product_items').dataTable({
            "paging": false,
            "ordering": false,
            "info": false,
            "searching": false
        });

        //hide update button
        $('#btn_update').hide();
        $('#btn_edit_reset').hide();


        //load days for selected product
        $('#new_form #product').change(function () {
            if ($('#new_form #product').val() !== "0") {
                loadDayListByProduct();
            }

        });

        $('#modal').modal('toggle');
    });

    function hideShowButton() {

        //hide/show button
        $('#btn_update').hide();
        $('#view_update_icon').hide();
        $('#btn_save').show();
        $('#btn_edit_reset').show();
        $('#new_form #product').prop('readonly', false);
        $("#window_title").html("Add New Product Profile");
        $('#view_icon_pnl').hide();
        resetNewFrom();

    }


    function clickSave() {
        $('#product_item').removeClass('error-indicate')

        if (validateForm($('#new_form'))) {
//            uploadImageToFirebase();

            if (checkPotionUniqness()) {
                saveItem();
            }


        } else {
            //set items           
            var lines = new Array();

            var resultForm = $('#items_form');
            var buttons = resultForm.find('button');//get inputs from Form 
            $(buttons).each(function (index) {
                var button = buttons[index];//get input object                 
                var val = $(button).attr('code').trim();
                var order = $(button).attr('order').trim();
                lines[index] = {val, order};

            });


            if (lines.length == 0) {
                $('#product_item').addClass('error-indicate')
            }
        }


    }

    function saveItem() {
        $('#btn_save').prop('disabled', true);

        //set items           
        var lines = new Array();

        var resultForm = $('#items_form');
        var buttons = resultForm.find('button');//get inputs from Form 
        $(buttons).each(function (index) {
            var button = buttons[index];//get input object                 
            var val = $(button).attr('code').trim();
            var order = $(button).attr('order').trim();
            lines[index] = {val, order};

        });

        if (lines.length > 0) {
            //check box val
            var specialStatus = "29";

            if ($('#specialDrawCk').prop('checked')) {
                specialStatus = "32";

            }

            //convert to json object
            var fieldData = createJSONObject($('#new_form'));
            var file = $('#new_form')[0];
            var formData = new FormData(file);

            formData.append("field_data", JSON.stringify(fieldData));
            formData.append("specialDraw", specialStatus);
            formData.append("items", JSON.stringify(lines));
            formData.append("template", file);



//        if (validateForm($('#new_form'))) {
//            if ($('#template').val().lastIndexOf(".png") == -1 && $('#template').val().lastIndexOf(".jpg") == -1 && $('#template').val().lastIndexOf(".jpeg") == -1) {
//                showNotification('error', 'The file is invalid or unsupported');
//
//            } else {
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
                    }

                    $('#btn_save').prop('disabled', false);

                    searchProduct();
                    //show msg
                    showNotification(response.status, response.msg);

                    $('#btn_save').prop('disabled', false);

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    $('#btn_save').prop('disabled', false);
                }

            });

        } else {
            showNotification('error', 'Please add lottery items ');
            $('#btn_save').prop('disabled', false);
        }


//            }


//        }
    }

//this function is not used ---- kasun nadeeshana - 2019-01-31
    function uploadImageToFirebase() {
        var proCode = $("#new_form #productCode").val();
        var dayCode = $("#new_form #dayCode").val();

        var des = proCode + " " + dayCode;
        const file = $('#template').get(0).files[0];

        var storageBucket = "gs://testproject-ff23b.appspot.com";

        var config = {
            apiKey: "AIzaSyAHJ2mQJIDkQ0ngtjA5TNbKkQ53gjW3u00",
            authDomain: "testproject-ff23b.appspot.com",
            databaseURL: "https://testproject-ff23b.firebaseio.com/",
            projectId: "testproject-ff23b",
            storageBucket: storageBucket
        };
        var a = firebase.initializeApp(config);

        // Points to the root reference
        var storageRef = firebase.storage().ref();

        var metadata = {
            contentType: 'image/jpeg'
        };

        // Upload the file and metadata
        var uploadTask = storageRef.child(des).put(file, metadata);
        console.log(a);
        console.log(uploadTask);
        console.log(storageBucket + "/" + des);
        console.log(storageRef.child(des));

        return getUrl(uploadTask);
    }


//this function is not used ---- kasun nadeeshana - 2019-01-31
    function getUrl(uploadTask) {

        uploadTask.on('state_changed', function (snapshot) {

            var progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
            console.log('Upload is ' + progress + '% done');
            switch (snapshot.state) {
                case firebase.storage.TaskState.PAUSED: // or 'paused'
                    console.log('Upload is paused');
                    break;
                case firebase.storage.TaskState.RUNNING: // or 'running'
                    console.log('Upload is running');
                    break;
            }
        }, function (error) {
            // Handle unsuccessful uploads
        }, function () {
            // Handle successful uploads on complete
            // For instance, get the download URL: https://firebasestorage.googleapis.com/...
            uploadTask.snapshot.ref.getDownloadURL().then(function (downloadURL) {
                console.log('File available at', downloadURL);

                $('#url').val(downloadURL);
                saveItem();
                return downloadURL;
            });
        });
    }


    function updateItem() {

        if (checkPotionUniqness()) {
            //set items           
            var lines = new Array();

            var resultForm = $('#items_form');
            var buttons = resultForm.find('button');//get inputs from Form 
            $(buttons).each(function (index) {
                var button = buttons[index];//get input object                 
                var val = $(button).attr('code').trim();
                var order = $(button).attr('order').trim();
                lines[index] = {val, order};

            });

            if (lines.length > 0) {
                //check box val
                var specialStatus = "29";

                if ($('#specialDrawCk').prop('checked')) {
                    specialStatus = "32";

                }

                //convert to json object
                var fieldData = createJSONObject($('#new_form'));
                var file = $('#new_form')[0];
                var formData = new FormData(file);

                formData.append("field_data", JSON.stringify(fieldData));
                formData.append("specialDraw", specialStatus);
                formData.append("items", JSON.stringify(lines));
                formData.append("template", file);



//        if (validateForm($('#new_form'))) {
//            if ($('#template').val().lastIndexOf(".png") == -1 && $('#template').val().lastIndexOf(".jpg") == -1 && $('#template').val().lastIndexOf(".jpeg") == -1) {
//                showNotification('error', 'The file is invalid or unsupported');
//
//            } else {
                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "update.htm",
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //hide window if record is ok
                        if (response.status === 'success') {
                            $('#addNew').modal('toggle');
                        }

                        $('#btn_save').prop('disabled', false);

                        searchProduct();
                        //show msg
                        showNotification(response.status, response.msg);



                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });

            } else {
                showNotification('error', 'Please add lottery items ');
            }
        }


    }


    function editItem(id) {
        $('#view_icon_pnl').show();
        var dataString = "id=" + id;

        $.ajax({
            data: dataString,
            type: 'GET',
            url: "get.htm",
            success: function (data, textStatus, jqXHR) {
                var response = JSON.parse(data);

                //hide window if record is ok
                if (response.status === 'success') {
                    $('#addNew').modal().show();

                    //show item details to form
                    var record = response.record;
                    $('#new_form #id').val(id);
                    $('#new_form #productCode').val(record.lottery);
                    $('#new_form #dayCode').val(record.day_code);
                    $('#new_form #statusCode').val(record.status);
                    $('#new_form #sp_draw').val(record.sp_draw);



                    if (record.eng_status === 'YES') {
                        $('#engLetterCk').bootstrapToggle('on');
                        $('#engPos').prop('disabled', false);
                        $('#engPos').val(record.eng_pos);
                    } else {
                        $('#engLetterCk').bootstrapToggle('off');
                        $('#engPos').prop('disabled', true);
                    }


                    if (record.bonus_status === 'YES') {
                        $('#bonusNoCk').bootstrapToggle('on');
                        $('#bonusPos').prop('disabled', false);
                        $('#bonusPos').val(record.bonus_pos);
                    } else {
                        $('#bonusNoCk').bootstrapToggle('off');
                        $('#bonusPos').prop('disabled', true);

                    }

                    if (record.zd_status === 'YES') {
                        $('#zodiacCk').bootstrapToggle('on');
                        $('#zodiacPos').prop('disabled', false);
                        $('#zodiacPos').val(record.zd_pos);
                    } else {
                        $('#zodiacCk').bootstrapToggle('off');
                        $('#zodiacPos').prop('disabled', true);
                    }

                    if (record.specialCk === 'YES') {
                        $('#specialDrawCk').bootstrapToggle('on');
                        $('#specialPos').prop('disabled', false);
                        $('#specialPos').val(record.specialPos);
                    } else {
                        $('#specialDrawCk').bootstrapToggle('off');
                        $('#specialPos').prop('disabled', true);

                    }

                    var nxtOrder = 0;
                    $('#items').html("");
                    $(record.product_item).each(function (index) {
                        var item = record.product_item[index];
                        var itemBtn = "<button order='" + nxtOrder + "' title='Remove Item' id=" + item.id + " onclick='removeItem(\"" + item.id + "\")' code=" + item.id + " type='button' class='btn btn-default btn-circle btn-circle btn-xl'>" + item.product_item + "</button>";
                        $('#items').append(itemBtn);
                        nxtOrder++;
                    });

                    $('#view_icon_add').attr('src', 'data:image/png;base64,' + record.template);
                    //set read only productCode
                    $('#new_form #productCode').prop('readonly', true);
                    $('#btn_save').hide();
                    $('#btn_new_reset').hide();
                    $('#btn_update').show();
                    $('#btn_edit_reset').show();
                    $("#window_title").html("Edit Profile Details");



                } else {
                    //show msg
                    showNotification(response.status, response.msg);
                }

            },
            error: function (jqXHR, textStatus, errorThrown) {

            }

        });


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
                    searchProduct();
                },
                error: function (jqXHR, textStatus, errorThrown) {

                }
            });

        }
        //confirm box -IF 'YES' -> call deleteFunction
        showConfirmMsg("Delete Product profile ", "Are you sure to delete product profile?", deleteFunction);
    }

    function viewItem(id) {

        $('#viewModel').modal().show();

        var dataString = "id=" + id;

        $.ajax({
            data: dataString,
            type: 'POST',
            url: "view.htm",
//            console.log("next");
            success: function (data, textStatus, jqXHR) {
                $('#view_icon_pnl').show();
                var response = JSON.parse(data);

                //hide window if record is ok
                if (response.status === 'success') {


                    var record = response.record;

                    $('#new_form #id').val(id);
                    $('#view_title').html(record.lottery_name);
                    $('#view_lotteryName').html(record.lottery_name);
                    $('#view_day').html(record.day);
                    $('#view_status').html(record.status);
                    $('#view_sp_draw').html(record.sp_draw);
                    $('#view_productItem').val(record.product_item);
                    $('#gameProfile').val(record.gameProfile);
                    


                    $('#view_icon').attr('src', 'data:image/png;base64,' + record.template);


                    $('#view_productItem').html("");
                    $(record.product_item).each(function (index) {
                        var item = record.product_item[index];
                        $('#view_productItem').append(item.product_item + " | ");
                    });


                } else {
                    //show msg
                    showNotification(response.status, response.msg);
                }

            },
            error: function (jqXHR, textStatus, errorThrown) {

            }

        });
    }

    function resetSearchFrom() {
        var form = $('#search_form');
        resultTable.fnClearTable();
        resetFrom(form);
    }
    function resetNewFrom() {
        var form = $('#new_form');
        var form1 = $('#items_form');
        resetFrom(form);
        resetFrom(form1);
        $('#items').html("");
        $('#search_result').hide();

        $('#new_form #id').val(0);

        $('#specialDrawCk').bootstrapToggle('off');
        $('#engLetterCk').bootstrapToggle('off');
        $('#zodiacCk').bootstrapToggle('off');
        $('#bonusNoCk').bootstrapToggle('off');
        $('#specialDrawCk').bootstrapToggle('off');

        $('#view_icon_add').attr('src', '');
    }

    function loadDayListByProduct() {
        var product = $('#new_form #product').val();
        var dataString = "productCode=" + product;
        $.ajax({
            data: dataString,
            url: "load_days.htm",
            type: 'GET',
            success: function (data, textStatus, jqXHR) {
                //clear table
                var response = JSON.parse(data);

                if (response.status === true) {

                    var options = '<option selected val="0">--Select--</option>';
                    var optionsSize = 0;
                    //set data table data
                    var tabledata = response.day_list;
                    $(tabledata).each(function (index) {
                        var row = tabledata[index];
                        //create option line
                        var option = '<option val=' + row.day_code + '>' + row.description + '</option>';

                        //add to string
                        options = options + option;
                        optionsSize++;

                    });

                    $('#new_form #dayCode').html(options);

                    //check day size
                    if (optionsSize === 0) {
                        //no days 
                        showNotification('warning', 'No days assign to selected product');
                    }
                }


            },
            error: function (jqXHR, textStatus, errorThrown) {

            }

        });
    }

    function addProductItem() {

        $('#view_icon_pnl').html("");

        var nxtOrder = 0; //for item order        

        var productItem = $('#new_form #product_item').val();
        var decription = $('#new_form #product_item option:selected').text().slice(0, 3);
        var isNewRow = true;


        if (productItem !== "0") {
            var resultForm = $('#items_form');
            var buttons = resultForm.find('button');//get inputs from Form 
            $(buttons).each(function (index) {

                var button = buttons[index];//get input object                 
                var val = $(button).attr('code').trim();

                if (val === productItem) {
                    isNewRow = false;
                }
                nxtOrder++;

            });

            nxtOrder = nxtOrder + 1;


            if (isNewRow) {
                //add new button
                var itemBtn = "<button order='" + nxtOrder + "' title='Remove Item' id=" + productItem + " onclick='removeItem(\"" + productItem + "\")' code=" + productItem + " type='button' class='btn btn-default btn-circle btn-circle btn-xl'>" + decription + "</button>";
                $('#items').append(itemBtn);
                //reset
                $('#new_form #product_item').val('0');

            } else {
                showNotification('error', "Item is already added");

            }
        } else {
            showNotification('error', "Please select item to add");
        }

    }


    function removeItem(row) {

        //remove table row inner funaction
        function removeRow() {
            $("#" + row).remove();

        }

        showConfirmMsg('Confirm', 'Are you sure to remove this item?', removeRow);



    }

//    /******************************************************/
    $(function () {
        $('#specialDrawCk').change(function () {
            //$(this).prop('checked');

            if ($(this).prop('checked')) {

                $('#drawNo').show();
                $('#drawNo').attr('data-validation', 'required');
            } else {
                $('#drawNo').hide();
                $('#drawNo').removeAttr('data-validation');
            }
        });


        $('#engLetterCk').change(function () {
            //$(this).prop('checked');

            if ($(this).prop('checked')) {

                $('#engPos').prop('disabled', false);
                $('#engPos').attr('data-validation', 'required');
                $('#engLetter').val('YES');
            } else {
                $('#engPos').prop('disabled', true);
                $('#engPos').val("")
                $('#engPos').removeAttr('data-validation');
                $('#engLetter').val('NO');
            }
        });
        $('#zodiacCk').change(function () {
            //$(this).prop('checked');

            if ($(this).prop('checked')) {

                $('#zodiacPos').prop('disabled', false);
                $('#zodiacPos').attr('data-validation', 'required');
                $('#zodiac').val('YES');
            } else {
                $('#zodiacPos').prop('disabled', true);
                $('#zodiacPos').val("")
                $('#zodiacPos').removeAttr('data-validation');
                $('#zodiac').val('NO');
            }
        });

        $('#bonusNoCk').change(function () {
            //$(this).prop('checked');

            if ($(this).prop('checked')) {

                $('#bonusPos').prop('disabled', false);
                $('#bonusPos').attr('data-validation', 'required');
                $('#bonusNo').val('YES');
            } else {
                $('#bonusPos').prop('disabled', true);
                $('#bonusPos').val("")
                $('#bonusPos').removeAttr('data-validation');
                $('#bonusNo').val('NO');
            }
        });


        $('#specialDrawCk').change(function () {
            //$(this).prop('checked');

            if ($(this).prop('checked')) {

                $('#specialPos').prop('disabled', false);
                $('#specialPos').attr('data-validation', 'required');
                $('#specialStatus').val('YES');
                 $("#gameProfile").attr('disabled', false);
                                    
            } else {
                $('#specialPos').prop('disabled', true);
                $('#specialPos').val("")
                $('#specialPos').removeAttr('data-validation');
                $('#specialStatus').val('NO');
                 $("#gameProfile").attr('disabled', true);
            }
        });


    });

//    /******************************************************/
    function validateImage() {
        if ($('#template').val().lastIndexOf(".png") == -1 && $('#template').val().lastIndexOf(".jpg") == -1 && $('#template').val().lastIndexOf(".jpeg") == -1) {
            $('#template').val("");
            showNotification('error', 'The file is invalid or unsupported');
//
        }
    }

    function checkPotionUniqness() {
        var engPos = $('#engPos').val();
        var zodiacPos = $('#zodiacPos').val();
        var bonusPos = $('#bonusPos').val();

        if (engPos === zodiacPos) {
            if (engPos !== '' || zodiacPos !== '') {
                showNotification('error', 'English Letter Position and Zodiac symbol Position cannot be same');
                return false;
            }


        } else if (engPos === bonusPos) {
            if (engPos !== '' || bonusPos !== '') {
                showNotification('error', 'English Letter Position and Bonus No Position cannot be same');
                return false;
            }


        } else if (zodiacPos === bonusPos) {
            if (zodiacPos !== '' || bonusPos !== '') {
                showNotification('error', 'Zodiac symbol Position and Bonus No Position cannot be same');
                return false;
            }
        }


        return true;
    }



    //search function
    function searchProduct() {

        var url_code = null;
        var p_code = $('#productCode').val();
        var day = $('#dayCode').val();
        var dataString = "&productCode=" + p_code + "&dayCode=" + day;

        $.ajax({
            data: dataString,
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
                        var editBtn = "<span onclick='editItem(\"" + item.id + "\")' class='fa fa-pencil btn btn-success' title='Edit Product'></span>";
                        var delBtn = "<span onclick='deleteItem(\"" + item.id + "\")' class='fa fa-trash-o btn btn-danger' title='Delete Profile'></span>";
                        var view = "<span onclick='viewItem(\"" + item.id + "\")' class='fa fa-eye btn btn-primary' title='View Profile'></span>";

                        var row = [item.description, item.status, " " + view + " " + editBtn + " " + delBtn];
                        resultTable.fnAddData(row);
                    });
                }


            },
            error: function (jqXHR, textStatus, errorThrown) {

            }

        });

    }
</script>
</html>
