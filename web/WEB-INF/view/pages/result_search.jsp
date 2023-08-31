<%-- 
    Document   : result_search
    Created on : May 28, 2021, 10:58:04 AM
    Author     : nipuna_k
--%>

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

          <style>
            .btn-circle.btn-lg {
                width: 105px !important;
                height: 50px !important;
                font-size: 14px !important;
                line-height: 1.33 !important;
                margin: 3px !important;
                border: solid lightblue !important;
                padding: 0 !important;
            }
        </style>
    </head>

    <body class="nav-md" >
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
                                <h4>Result Search</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Result</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  autocomplete="off"   class="form-horizontal" id="search_form">
                                            <div class="form-group">
                                                <label class="">Product<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <select data-validation="required" type="text" class="form-control" name="dlbWbProduct.productCode" id="product"  >
                                                        <option selected value="0">--Select Product--</option>
                                                        <c:forEach var="item" items="${product_select_box}">
                                                            <option value="${item.productCode}">${item.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Draw No<span class="text-red">*</span></label>
                                                <div class="">
                                                    <input data-validation="required" class="form-control"  type="text" name="draw_no" id="draw_no" placeholder="Enter Draw No" maxlength="15" onkeydown="isNumberOnly(event)"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Draw Date</label>
                                                <div class="">
                                                    <input readonly type="text" class="form-control date" id="draw_date" name="draw_date" placeholder="Select Date" />
                                                </div>
                                            </div>



                                            <div class="form-group">
                                                <label class="control-label"></label>
                                                <div class="">
                                                    <!--<button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>-->
                                                    <div class="add-button">
                                                        <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                        <button type="button" class="btn submit" onclick="searchProduct();">Search</button>
                                                    </div>
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
                                        <th>#</th>
                                        <th>Product</th>                                                          
                                        <th>Day</th>
                                        <th>Draw No</th>
                                        <th>Draw Date</th>
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


                <!-- /page content -->

                <!-- Add new Modal -->
                <div class="modal fade" id="addNew" role="dialog">
                    <div class="modal-dialog">                       
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-white">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4 id="window_title" class="modal-title">Add New Result</h4>
                                <hr class="hr" />
                            </div>
                            <div class="modal-body">
                                <!-- model body -->

                                <form  autocomplete="off"  class="form-horizontal" id="new_form" >
                                    <div class="form-group col-md-6">
                                        <label class="required-input">Product<span class="text-red"> *</span></label>
                                        <div class="">
                                            <select data-validation="required" type="text" class="form-control" id="product"  name="dlbWbProduct.productCode"  >
                                                <option selected value="0">--Select--</option>
                                                <c:forEach var="item" items="${product_select_box}">
                                                    <option value="${item.productCode}">${item.description}</option>
                                                </c:forEach>
                                            </select>
                                        </div>


                                    </div>
                                    <div class="form-group col-md-6">
                                        <label class="required-input">Day<span class="text-red"> *</span></label>
                                        <div class="">
                                            <select data-validation="required" type="text" class="form-control" name="dlbWbProductProfile.id" id="profileCode"  >
                                                <option selected value="0">--Select--</option>
                                                <c:forEach var="item" items="${day_select_box}">
                                                    <option value="${item.dayCode}">${item.description}</option>        
                                                </c:forEach>
                                            </select>
                                        </div>


                                    </div>

                                    <div class="form-group col-md-6">
                                        <label class="required-input">Draw No<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" min="1" type="text" class="form-control" id="drawNo" name="drawNo"  maxlength="10" onkeydown="isNumberOnly(event)">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label class="required-input">Draw Date<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input readonly data-validation="required-date" type="text" class="form-control date" id="drawDate" name="draw_date">

                                            <input type="hidden" value="${user}" class="form-control date" id="username" name="username"/>
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label class="required-input">Next Draw Date<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input readonly maxlength="10"  data-validation="required-date" type="text" class="form-control date" id="nextDate" name="nextDate">
                                        </div>
                                    </div>
                                    <div class="form-group col-md-6">
                                        <label class="required-input">Next Jackpot<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input onClick="this.setSelectionRange(0, this.value.length)" data-validation="required-sp-allow"   class="form-control jackpot-amt"  min="1" name="nextJackpot" id="nextJackpot" placeholder="Enter Jackpot Amount" maxlength="25"/>
                                        </div>
                                    </div>

                                    <div class="form-group col-md-6">
                                        <label class="required-input">Winning Logic<span class="text-red"> *</span></label>
                                        <div class="">
                                            <select  type="text" class="form-control" id="dlbSwtStWinningLogic.logicId"  name="dlbSwtStWinningLogic.logicId"  >
                                                <option selected value="0">--Select--</option>
                                                <c:forEach var="item" items="${logics}">
                                                    <option value="${item.logicId}">${item.logicDescription}</option>
                                                </c:forEach>
                                            </select>
                                        </div>


                                    </div>



                                    <input type="hidden" id="dayCode" name="dlbWbWeekDay.dayCode"/>
                                </form>   
                                <hr class="hr" />
                                <h5>Result Values (General Draw)</h5>
                                <div class="row bg-info" align="center">
                                    <form id="result_form" name="result_form" id="result_form" style="padding: 0px 20px; background-color: #fff;">                                        
                                        <div id="result_input_panal">

                                        </div>
                                    </form>
                                </div>

                                <h5>Special Draw Game Result</h5>
                                <div class="row bg-info" align="center">
                                    <form id="result_sp_form" name="result_sp_form" style="padding: 0px 20px; background-color: #fff;">                                        
                                        <div id="result_sp_input_panal">

                                        </div>
                                    </form>
                                </div>

                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                    </div> 
                                </div>

                                <br>


                            </div>

                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>                               
                                <button type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>
                                <button id="btn_save" type="button" class="btn submit" onclick="saveItem()">Save Result</button>
                            </div>
                        </div>

                    </div>
                </div>
                <!-- View Modal -show selected item   -->
                <div class="modal fade" id="viewModal" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-warning">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4  class="modal-title"><span id="viewModal_view_title"></span></h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <div class="row">                                   
                                    <form class="form-horizontal" >
                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Product:</label>
                                            <div class="col-sm-4">
                                                <label class="control-label" id="view_product" ></label>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Day:</label>
                                            <div class="col-sm-4">                                          
                                                <label class="control-label "  id="view_day" ></label>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Draw No:</label>
                                            <div class="col-sm-4">                                          
                                                <label class="control-label "  id="view_draw_no" ></label>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Draw Date:</label>
                                            <div class="col-sm-4">                                          
                                                <label class="control-label "  id="view_draw_date" ></label>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-sm-5">Status:</label>
                                            <div class="col-sm-4">
                                                <div class="form-group">                                                
                                                    <label class="control-label " id="view_statusCode" ></label>
                                                </div>
                                            </div>
                                        </div>
                                        <hr>
                                        <br>
                                        
                                        <label>Normal Result</label>

                                        <div id="result_item">

                                        </div>
                                        <br>                                        
                                        <label>Special Game Result</label>
                                        <div id="sp_items">

                                        </div>

                                    </form>


                                </div>



                            </div>
                            <div class="modal-footer">                              
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
        var resultTable;
        var publicKey = "";



        $(document).ready(function () {




            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            //hide update button
            $('#btn_update').hide();

            //load days for selected product
            $('#new_form #product').change(function () {
                if ($('#new_form #product').val() !== "0") {
                    loadDayListByProduct();
                }

            });

            //load profile
            $('#new_form #profileCode').change(function () {
                if ($('#new_form #profileCode').val() !== "0") {
                    loadProfile();
                }

            });

            //set date selecter         

            $(function () {
                $("#draw_date").datepicker({"showAnim": "blind", "dateFormat": "yy-mm-dd", "maxDate": new Date});
                $("#drawDate").datepicker({"showAnim": "blind", "dateFormat": "yy-mm-dd", "maxDate": new Date});
                $("#nextDate").datepicker({"showAnim": "blind", "dateFormat": "yy-mm-dd", "minDate": new Date});
            });


        });

        function hideShowButton() {


            //hide/show button
            $('#btn_update').hide();
            $('#view_update_icon').hide();
            $('#btn_save').show();
            $('#new_form #product').prop('readonly', false);
            resetNewFrom();




        }

        //search function
        function searchProduct() {


            $('#search_form #product').removeClass('error-indicate');
            $('#search_form #draw_no').removeClass('error-indicate');
            $('#search_form #draw_date').removeClass('error-indicate');



            if ($('#search_form #product').val() !== '' && ($('#search_form #draw_no').val() !== '' || $('#search_form #draw_date').val() !== '')) {

                var formData = $('#search_form').serialize() + "&draw_date_str=" + $('#draw_date').val();

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
                                var viewBtn = "<span onclick='viewItem(\"" + item.id + "\",\" " + item.day_code + "\")' class='fa fa-eye btn btn-primary'  title='View'> </span>";
//                                var editBtn = "<span onclick='editItem(\"" + item.id + "\",\" " + item.day_code + "\")' class='fa fa-pencil btn btn-info' title='View Results'>Edit </span>";
                                var status = "<span class='badge badge-dark'>" + item.status + "</span>";
//                                var delBtn = "<span onclick='deleteItem(\"" + item.id + "\",\" " + item.day_code + "\")' class='fa fa-trash-o btn btn-danger' title='Delete Product'></span>";

                                var row = [item.id, item.product, item.day, item.draw_no, item.date, status, viewBtn];
                                resultTable.fnAddData(row);
                            });
                        } else {
                            showNotification('error', response.msg);
                        }


                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });

            } else {


                if ($('#search_form #product').val() === '0') {
                    $('#search_form #product').addClass('error-indicate');
                }

                if ($('#search_form #draw_no').val() === '') {
                    $('#search_form #draw_no').addClass('error-indicate');
                }

              

                showNotification('error', 'Please enter valid data to search');
            }


        }

        function saveItem() {


            //set result sets
            var resultForm = $('#result_form');
            var inputs = resultForm.find('input');//get inputs from Form 
            var selects = resultForm.find('select');//get inputs from Form 

            //get special inputs
            var resultSpform = $('#result_sp_form');
            console.info(resultSpform);
            var inputsSpform = resultSpform.find('input');


            var lines = new Array();
           

            $(inputs).each(function (index) {//loop inputs
                var input = inputs[index];//get input object  
                var val = $(input).val();
                var code = $(input).attr('code');

                //put to array              
                lines.push({code: code, val: val});


            });
            
             $(selects).each(function (index) {//loop inputs
                var select = selects[index];//get input object  
                var val = $(select).val();
                var code = $(select).attr('code');

                //put to array              
                lines.push({code: code, val: val});


            });

            var spLines = new Array();
            $(inputsSpform).each(function (index) {//loop inputs
                var input = inputsSpform[index];//get input object  
                var val = $(input).val();
                var gameId = parseInt($(input).attr('gameId'));

                //put to array              
                spLines.push({gameId: gameId, val: val});


            });


            console.info(JSON.stringify(spLines));
            var formData = $('#new_form').serialize() + "&items=" + JSON.stringify(lines) + "&public_key=" + publicKey + "&spLines=" + JSON.stringify(spLines);


            if (validateForm($('#new_form'))) {

                if (validateForm($('#result_form'))) {
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
                        searchProduct();




                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }

        }


        function viewItem(id) {
            $('#result_item').html("");
            var dataString = "id=" + id;

            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#viewModal').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#view_product').html(record.product);
                        $('#view_day').html(record.day_code);
                        $('#view_draw_no').html(record.draw_no);
                        $('#view_draw_date').html(record.draw_date);
                        $('#view_statusCode').html(record.status);


                        $('#viewModal_view_title').html("View Result: " + record.product);

                        var items = response.record.items;
                        var sp_items = response.record.sp_items;

                        $(items).each(function (index) {
                            var item = items[index];
                            //add new button                           
                            var itemBtn = "<button  type='button' class='btn btn-default btn-circle btn-circle btn-lg'>" + item.value + "</button>";

                            $('#result_item').append(itemBtn);
                        });



                        $(sp_items).each(function (index) {
                            var item = sp_items[index];
                            //add new button                           
                            var itemBtn = " <label>" + item.item + ": " + item.value + "</label><br>";

                            $('#sp_items').append(itemBtn);
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
        function deleteItem(code, day) {

            //delete function
            function deleteFunction() {
                var dataString = "code=" + code + "&day=" + day;
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
            showConfirmMsg("Delete Product", "Are you sure to delete product Item" + code + "?", deleteFunction)
        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        function resetNewFrom() {
            var form = $('#new_form');
            $('#result_input_panal').html("");
            $('#result_sp_input_panal').html("");
            resetFrom(form);
        }

        function loadDayListByProduct() {
            var product = $('#new_form #product').val();
            var dataString = "productCode=" + product;
            $.ajax({
                data: dataString,
                url: "${pageContext.servletContext.contextPath}/product_profile/load_profile_list.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    //clear table
                    var response = JSON.parse(data);

                    if (response.status === true) {

                        var options = '<option selected value="0">--Select--</option>';
                        var optionsSize = 0;
                        //set data table data
                        var tabledata = response.day_list;
                        $(tabledata).each(function (index) {
                            var row = tabledata[index];
                            //create option line
                            var option = '<option value=' + row.day_code + '>' + row.description + '</option>';

                            //add to string
                            options = options + option;
                            optionsSize++;

                        });

                        $('#new_form #profileCode').html(options);

                        //check day size
                        if (optionsSize === 0) {
                            //no days 
                            showNotification('warning', 'No days assign to selected product')
                        }
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }

        function loadProfile() {
            var profileCode = $('#new_form #profileCode').val();
            var dataString = "id=" + parseInt(profileCode);
            $.ajax({
                data: dataString,
                url: "${pageContext.servletContext.contextPath}/product_profile/load_profile.htm",
                type: 'GET',
                success: function (data, textStatus, jqXHR) {
                    $('#result_input_panal').html("");
                    $('#result_sp_input_panal').html("");
                    //clear table
                    var response = JSON.parse(data);


                    $('#drawNo').val("");
                    var resultInputs = "";
                    var spGamesInputs = "";

                    if (response.status === true) {
                        var items = response.itemList;
                        var spGames = response.game_list;


                        var dayCode = response.dayCode;

                        if (!jQuery.isEmptyObject(items)) {
                            $(items).each(function (index) {
                                var item = items[index];
                                var resultInput = "";
                                if (item.value_type === 'select') {
                                    //load special symbol list for select
                                    var symbolSelectItems = "<br/><div class='form-group'>\n\
                                                         <select code='" + item.code + "' data-validation='required' class='form-control'>\n\
                                                         <option value=0 selected>--Select " + item.description + "--</option>";
                                    var symbols = item.symbols;
                                    $(symbols).each(function (index) {
                                        var symbol = symbols[index];
                                        var option = '<option >' + symbol.description + '</option>';

                                        symbolSelectItems = symbolSelectItems + option;
                                    });

                                    symbolSelectItems = symbolSelectItems + "</select></div> "
                                    resultInput = symbolSelectItems;

                                } else {
                                    resultInput = "<input code=" + item.code + " data-validation='required' maxlength='2' class='input-round-text form-control' placeholder=" + item.description + " id=" + item.code + " />";
                                }


                                resultInputs = resultInputs + resultInput;
                            });
                        } else {
                            showNotification('warning', 'No lottery profile found  to selected lottery and day');
                        }

                        //load special games result ** if exists

                        if (!jQuery.isEmptyObject(spGames)) {

                            $(spGames).each(function (index) {
                                var item = spGames[index];
                                var spGamesInput = "<input gameId=" + item.id + " data-validation='required' maxlength='8' class='input-round-text form-control' placeholder=" + item.name + " id=" + item.id + " /> ";
                                spGamesInputs += spGamesInput;
                            });



                        }


                        console.info(spGamesInputs);
                        //display result inputs
                        $('#result_input_panal').html(resultInputs);
                        $('#result_sp_input_panal').html(spGamesInputs);
                        $('#dayCode').val(dayCode)
                        $('#drawNo').val(response.next_draw);


                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function validateSearch() {
            var product = $('#product').val();
            if (product === '0') {
                showNotification('error', 'Please select lottery type');
                return false;
            }


            return true;
        }



        function getPublicKey() {

            var val = scanner.isConnectedToScanWebSocket();

            if (val) {

                scanner.signText(responseHandle,
                        {
                            "req_param":
                                    {
                                        "param": $('#username').val()
                                    }
                        }
                );

            } else {

                scanner.displayInstallScanAppEnableJavaPopup(true);

                $.notify({
                    icon: 'fa fa-times-circle',
                    message: 'Please start the local service.'
                }, {
                    type: 'danger',
                    z_index: 9999
                });

            }
        }

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

            if (successful && mesg !== null) {



                var response = $.parseJSON(mesg);



                if (response.STATUS) {
                    publicKey = response.param.signedtext;

                } else {
                    $.notify({
                        icon: 'fa fa-times-circle',
                        message: response.MSG
                    }, {
                        type: 'danger',
                        z_index: 9999
                    });
                }

                return;
            }

        }


        $(function () {
            //$('.price-val').mask("99/99/9999");
            $('.jackpot-amt').maskMoney();
        });




    </script>
</html>

