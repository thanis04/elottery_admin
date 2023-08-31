<%@page import="com.epic.dlb.util.common.Configuration"%>
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
            body{
                background-color: #f0f0f0;
            }
            .progress{
                max-width:1000px;
                margin: 40px auto;
            }


            /* STYLES FOR PROGRESSBARS */

            .progress-radial, .progress-radial * {
                -webkit-box-sizing: content-box;
                -moz-box-sizing: content-box;
                box-sizing: content-box;
            }

            /* -------------------------------------
             * Bar container
             * ------------------------------------- */
            .progress-radial {
                float: left;
                margin-right: 4%;
                position: relative;
                width: 20%;
                border-radius: 50%;
            }
            .progress-radial:first-child {
                margin-left: 4%;
            }
            /* -------------------------------------
             * Optional centered circle w/text
             * ------------------------------------- */
            .progress-radial .overlay {
                position: absolute;
                width: 80%;
                background-color: #f0f0f0;
                border-radius: 50%;
                font-size: 14px;
                top:50%;
                left:50%;
                -webkit-transform: translate(-50%, -50%);
                -ms-transform: translate(-50%, -50%);
                transform: translate(-50%, -50%);
            }

            .progress-radial .overlay p{
                position: absolute;
                line-height: 40px;
                text-align: center;
                width: 100%;
                top:50%;
                margin-top: -20px;
            }

            /* -------------------------------------
             * Mixin for progress-% class
             * ------------------------------------- */
            .progress-0 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(0deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(90deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-5 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(342deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(108deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-10 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(324deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(126deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-15 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(306deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(144deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-20 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(288deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(162deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-25 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-30 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(252deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(198deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-35 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(234deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(216deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-40 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(216deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(234deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-45 {
                background-image: -webkit-linear-gradient(0deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(198deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #f0f0f0 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(252deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-50 {
                background-image: -webkit-linear-gradient(180deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(-90deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-55 {
                background-image: -webkit-linear-gradient(162deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(-72deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-60 {
                background-image: -webkit-linear-gradient(144deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(-54deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-65 {
                background-image: -webkit-linear-gradient(126deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(-36deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-70 {
                background-image: -webkit-linear-gradient(108deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(-18deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-75 {
                background-image: -webkit-linear-gradient(90deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(0deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-80 {
                background-image: -webkit-linear-gradient(72deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(18deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-85 {
                background-image: -webkit-linear-gradient(54deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(36deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-90 {
                background-image: -webkit-linear-gradient(36deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(54deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-95 {
                background-image: -webkit-linear-gradient(18deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(72deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
            }

            .progress-100 {
                background-image: -webkit-linear-gradient(0deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), -webkit-linear-gradient(180deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
                background-image: linear-gradient(90deg, #38b16b 50%, rgba(0, 0, 0, 0) 50%, rgba(0, 0, 0, 0)), linear-gradient(270deg, #38b16b 50%, #f0f0f0 50%, #f0f0f0);
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
                                <h4>Daily Winning File Upload</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-8 col-xs-12 col-md-offset-3 col-sm-offset-2 col-xs-offset-0">
                                <div class="x_panel">                                   
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="new_form">
                                            <div class="form-group">
                                                <label class="">Lottery<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <select data-validation="required" type="text" class="form-control" name="productCode" id="productCode"  >
                                                        <option selected value="0">--Select Product--</option>
                                                        <c:forEach var="item" items="${product_select_box}">
                                                            <option value="${item.productCode}">${item.description}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>                                          
                                            <div class="form-group">
                                                <label class="">Draw No<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input  data-validation="required" maxlength="10" class="form-control" type="text" onkeydown="isNumberOnly(event)" maxlength="10" name="drawNo" id="drawNo" placeholder="Enter Draw No" />
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Draw Date<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input readonly data-validation="required-date" type="text" class="form-control " id="drawDate" name="drawDate" placeholder="Select Date" />
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Ticket File ID<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input readonly  data-validation="required" maxlength="10" class="form-control" type="number" name="ticketID" id="ticketID"  maxlength="63"/>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="">Winning File (Text File Only)<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input  accept="txt" data-validation="required-sp-allow" type="file" class="form-control" id="result_file" name="result_file"  />
                                                </div>
                                            </div>
                                            <!--                                            <div class="form-group">
                                                                                            <label class="">Generated Check Sum<span class="text-red"> *</span></label>
                                                                                            <div class="">
                                                                                                <input readonly data-validation="required"  class="form-control" type="text"  maxlength="64" name="genCheckSum" id="genCheckSum"  />
                                                                                            </div>
                                                                                        </div>-->

                                            <!--                                            <div class="form-group">
                                                                                            <label class="">Original Check Sum(LMS)<span class="text-red"> *</span> <span id="checkSumStatus"></span></label>
                                                                                            <div class="">
                                                                                                <input data-validation="required"  class="form-control" type="text"  onkeypress="validateCheckSum();" onchange="validateCheckSum()" maxlength="64" name="origCheckSum" id="origCheckSum" placeholder="Enter Original File Check sum (LMS) for validate " />
                                                                                            </div>
                                                                                        </div>-->
                                            <br>
                                            <div class="form-group">
                                                <div class="form-group col-lg-6">
                                                    <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                                </div>   
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label"></label>
                                                <div class="">                                                   
                                                    <div class="add-button">
                                                        <button type="button" class="btn submit" onclick="resultFileUpload()"><span class="fa fa-upload"></span> Upload</button>                                                      
                                                        <button type="reset" class="btn btn-secondary">Reset</button>                                                      
                                                    </div>
                                                </div>
                                            </div>
                                            <br>
                                        </form>
                                    </div>
                                </div>
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
    </body>

    <script>
//( document ).ready() block.


        var fileSource = "C:/elottery_tmp/";



        var mac = "";
        var hash = "";
        var ip = "";
        var signText = "";



        function resultFileUpload() {
            //convert to json object
            var fieldData = createJSONObject($('#new_form'));
            var file = $('#new_form')[0];
            var productDes = $("#productCode option:selected").text();

            var formData = new FormData(file);
            formData.append("form_data", JSON.stringify(fieldData));
            formData.append("product_description", productDes);
            formData.append("mac", mac);
            formData.append("hash", hash);
            formData.append("hash_code_sign_key", signText);

            console.info(mac);
            console.info(hash);
//            if ($('#genCheckSum').val().trim() !== $('#origCheckSum').val().trim()) {
//                showNotification('error', 'Generated Check Sum is not match with Original Check Sum');
//                return
//            }

            if (validateForm($('#new_form'))) {

                if ($('#result_file').val().toUpperCase().lastIndexOf(".TXT") == -1) {
                    showNotification('error', 'The file is invalid or unsupported');
                } else {
                    $.ajax({
                        data: formData,
                        type: 'POST',
                        url: "upload_result_file.htm",
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function (data, textStatus, jqXHR) {
                            var response = JSON.parse(data);

                            //show msg
                            showNotification(response.status, response.msg);


                            if (response.status === 'success') {
                                resetFrom($('#new_form'));
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {

                        }

                    });
                }


            }
        }


        function getHashIpAndSign() {

            var val = scanner.isConnectedToScanWebSocket();
            
            if (val) {

                scanner.getHashIpAndSign(responseHandle,
                        {
                            "req_param":
                                    {
                                        "filepath": fileSource
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
                    mac = response.param.mac;
                    hash = response.param.hash;
                    ip = response.param.ip;
                    signText = response.param.signText;


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


        function selectFile() {
            fileSource = "C:/elottery_tmp/";
            mac = "";
            hash = "";
            ip = "";
            signText = "";

            if ($('#result_file').val().toUpperCase().lastIndexOf(".TXT") == -1) {
                $('#result_file').val("");
                showNotification('error', 'The file is invalid or unsupported');
//
            } else {
                var fileName = $('#result_file').val().replace(/C:\\fakepath\\/i, '');
                var tmpPath = fileSource;
                fileSource = tmpPath + "\\" + fileName;

                //get hash form websockert
                getHashIpAndSign();
//                generateCheckSum();

            }
        }

//        function generateCheckSum() {
//
//            var file = $('#new_form')[0];
//            var formData = new FormData(file);
//
//            if ($('#result_file').val() !== '') {
//
//                if ($('#result_file').val().toUpperCase().lastIndexOf(".TXT") == -1) {
//                    showNotification('error', 'The file is invalid or unsupported');
//                } else {
//                    $.ajax({
//                        data: formData,
//                        type: 'POST',
//                        url: "generate_check_sum.htm",
//                        cache: false,
//                        contentType: false,
//                        processData: false,
//                        success: function (data, textStatus, jqXHR) {
//                            var response = JSON.parse(data);
//
//                            //show msg
//                            if (response.status === 'success') {
//
//                                $('#genCheckSum').val(response.record);
//                            }
//
//                            if (response.status === 'error') {
//                                showNotification(response.status, response.msg);
//                            }
//
//
//                        },
//                        error: function (jqXHR, textStatus, errorThrown) {
//
//                        }
//
//                    });
//                }
//
//            } else {
//                showNotification('error', 'Please select ticket file');
//            }
//        }

//        function validateCheckSum() {
//            if ($('#origCheckSum').val().trim() === '') {
//                $('#checkSumStatus').html("");
//            } else {
//                $('#checkSumStatus').html("");
//
//                if ($('#genCheckSum').val().trim() === $('#origCheckSum').val().trim()) {
//                    $('#checkSumStatus').html("<span style='background-color: #5cb85c' class='badge bg-success'>Validation Ok</span>");
//                    $('#btn_upload').attr('disabled', false);
//                } else {
//                    $('#checkSumStatus').html("<span style='background-color: #d9534f' class='badge bg-danger'>Validation Fail</span>");
//
//                    $('#btn_upload').attr('disabled', true);
//                }
//            }
//        }


        $('#result_file').change(function () {
            selectFile();
        });


        $('#drawNo').focusout(function () {
            loadTicketFile();

        });


        $('#drawNo').change(function () {
            loadTicketFile();

        });



        function loadTicketFile() {
            $('#ticketID').val("");
            var prdoductCode = $('#productCode').val();
            var drawNo = $('#drawNo').val();

            if (prdoductCode !== 0 && drawNo !== "") {
                var dataString = "code=" + prdoductCode + "&draw_no=" + drawNo;
                $.ajax({
                    data: dataString,
                    type: 'GET',
                    url: "load_ticket_file.htm",
                    success: function (data, textStatus, jqXHR) {

                        var response = JSON.parse(data);
                        if (response.status) {
                            $('#ticketID').val(response.ticketID);
                            $('#drawDate').val(response.draw_date);
                        } else {
                            showNotification('error', response.msg);
                        }


                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }



        }


    </script>
</html>
