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
                                <h4>Lottery Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-6 col-sm-12 col-xs-12 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float: none;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Lottery</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">


                                            <div class="form-group">
                                                <label>Lottery Code</label>
                                                <input maxlength="15" type="text" class="form-control" id="productCode"  name="productCode" placeholder="Enter lottery code" />
                                            </div>

                                            <div class="form-group">
                                                <label>Description</label>
                                                <input maxlength="64" type="text" class="form-control" id="description" name="description" placeholder="Enter description" />
                                            </div>

                                            <div class="form-group">
                                                <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" id="btn_reset" onclick="resetSearchFrom()">Reset</button>
                                                    <button type="button" class="btn submit" onclick="searchProduct();">Search</button>
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
                                        <th>Product Code</th>
                                        <th>Description</th>                                                          
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
                                <h4 id="window_title" class="modal-title">Add New Lottery</h4>
                                <hr class="hr" />
                            </div>

                            <form class="form-horizontal" id="new_form" enctype="multipart/form-data">
                                <div class="modal-body">
                                    <!-- model body -->


                                    <div class="row">
                                        <div class="">

                                            <div class="form-group col-lg-6">
                                                <label >Lottery Code<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input maxlength="15" data-validation="required" type="text" class="form-control" id="productCode"  name="productCode">
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label >Description<span class="text-red"> *</span></label>
                                                <div class="">
                                                    <input data-validation="required" type="text" class="form-control" id="description"  name="description" maxlength="42">
                                                </div>
                                            </div>

                                            <div class="form-group col-lg-6">
                                                <label >Product Icon (Only allow Jpeg and Png)<span class="text-red"> * </span></label>
                                                <div class="">
                                                    <input data-validation="required-sp-allow" type="file" onchange="validateImage();"  class="form-control" id="icon"  name="icon">

                                                </div>
                                            </div>




                                            <div class="form-group col-lg-6">
                                                <label >Status<span class="text-red"> *</span></label>
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

                                            <div class="form-group col-lg-6">
                                                <label id="upload_prg" class="label label-primary"></label>
                                            </div>


                                            <div class="form-group">
                                                <label class="control-label col-sm-5"></label>
                                                <div class="col-sm-7">


                                                </div>
                                            </div>



                                        </div>
                                        <div class="col-md-4">
                                            <div id="view_update_icon" style="border: gainsboro solid thin;padding: 2px;">

                                            </div>
                                        </div>



                                    </div>
                                    <div class="form-group col-lg-6">
                                        <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                    </div>   
                                    <br>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button id="btn_get_reset" type="button" class="btn btn-secondary" onclick="editItem($('#new_form #productCode').val())" >Reset</button>
                                    <button id="btn_update" type="button" class="btn btn-primary" onclick="updateFnItem()">Update</button>
                                    <button id="btn_reset" type="button" class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>                                   
                                    <button id="btn_save" type="button" class="btn submit" onclick="clickSave()">Add</button>
                                </div>
                            </form>
                        </div>

                    </div>
                </div>
                <!-- View Modal -show selected item   -->
                <div class="modal fade" id="viewModel" role="dialog">
                    <div class="modal-dialog">

                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-primary">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4  class="modal-title">Lottery: <span id="view_title"></span></h4>
                            </div>
                            <div class="modal-body">
                                <!-- model body -->
                                <div class="row">
                                    <div class="col-md-8">
                                        <form class="form-horizontal" >
                                            <div class="form-group">
                                                <label class="control-label col-sm-5">Lottery Code:</label>
                                                <div class="col-sm-7">
                                                    <label class="control-label" id="view_productCode" ></label>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label col-sm-5">Description:</label>
                                                <div class="col-sm-7">                                          
                                                    <label maxlength="50" class="control-label "  id="view_description" ></label>
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label class="control-label col-sm-5">Status:</label>
                                                <div class="col-sm-7">
                                                    <div class="form-group">                                                
                                                        <label class="control-label " id="view_statusCode" ></label>
                                                    </div>
                                                </div>
                                            </div>

                                        </form>
                                    </div>
                                

                                </div>
                                
                                    <div class="row">
                                        <div class="col-md-8">   
                                            <div id="view_icon" style="border: gainsboro solid thin;padding: 2px;">

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
                <!-- footer content -->
                <jsp:include page="../common/footer.jsp"/>
                <!-- /footer content -->
            </div>
        </div>
    </body>

    <script>

//( document ).ready() block.
        var resultTable;
        var storageBucket;
        var storageApp;
        $(document).ready(function () {

            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            //hide update button
            $('#btn_update').hide();

            var storageBucket = "gs://testproject-ff23b.appspot.com";

            var config = {
                apiKey: "AIzaSyAHJ2mQJIDkQ0ngtjA5TNbKkQ53gjW3u00",
                authDomain: "testproject-ff23b.appspot.com",
                databaseURL: "https://testproject-ff23b.firebaseio.com/",
                projectId: "testproject-ff23b",
                storageBucket: storageBucket
            };
            var storageApp = firebase.initializeApp(config);

        });

        function hideShowButton() {
            //hide/show button
            $('#btn_update').hide();
            $('#btn_get_reset').hide();
            $('#view_update_icon').hide();
            $('#btn_save').show();
            $('#new_form #btn_reset').show();
            $('#new_form #productCode').prop('readonly', false);
            $("#window_title").html("Add New Lottery");
             $('#new_form #icon').attr('data-validation', 'required-sp-allow');
            resetNewFrom();




        }

        //search function
        function searchProduct() {
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

                            var viewBtn = "<span onclick='viewItem(\"" + item.code + "\")' class='fa fa-eye btn btn-primary' title='View Lottery'></span>";
                            var editBtn = "<span onclick='editItem(\"" + item.code + "\")' class='fa fa-pencil btn btn-success' title='Edit Lottery'></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.code + "\")' class='fa fa-trash-o btn btn-danger' title='Delete Lottery'></span>";

                            var row = [item.code, item.description, item.status, viewBtn + " " + editBtn + " " + delBtn];
                            resultTable.fnAddData(row);
                        });
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function clickSave() {

            if (validateForm($('#new_form'))) {
                $('#btn_save').prop('disabled', true);
                uploadImageToFirebase('SAVE');
            } else {
                $('#btn_save').prop('disabled', false);
            }

        }

        function updateFnItem() {
            if (validateForm($('#new_form'))) {
                $('#btn_save').prop('disabled', true);
                if ($('#icon').val() !== '') {
                    uploadImageToFirebase('UPDATE');
                } else {
                    updateItem();
                }

            } else {
                $('#btn_save').prop('disabled', false);
            }
        }

        function saveItem() {
            var formData = $('#new_form').serialize() + "&productIcon=" + productIcon;

//            if (validateForm($('#new_form'))) {
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
                    $('#btn_save').prop('disabled', false);
                    searchProduct();
                    //show msg
                    showNotification(response.status, response.msg);

                    productIcon = "";



                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

            $('#btn_save').prop('disabled', false);
//            }  $('#btn_save').prop('disabled', false);

        }

        function uploadImageToFirebase(type) {
//            var proCode = $("#new_form #productCode").val();
//            var dayCode = $("#new_form #dayCode").val();

            var des = $("#new_form #productCode").val();

            const file = $('#icon').get(0).files[0];

            // Points to the root reference
            var storageRef = firebase.storage().ref();

            var metadata = {
                contentType: 'image/jpeg'
            };

            // Upload the file and metadata
            var uploadTask = storageRef.child(des).put(file, metadata);
            console.log(storageApp);
            console.log(uploadTask);
            console.log(storageBucket + "/" + des);
            console.log(storageRef.child(des));




            des = "";

            return getUrl(uploadTask, type);
        }

        function getUrl(uploadTask, type) {

            uploadTask.on('state_changed', function (snapshot) {

                var progress = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
                console.log('Upload is ' + progress + '% done');
                $('#upload_prg').html('Upload is ' + progress + '% done');
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
                $('#btn_save').prop('disabled', false);
            }, function () {
                // Handle successful uploads on complete
                // For instance, get the download URL: https://firebasestorage.googleapis.com/...
                uploadTask.snapshot.ref.getDownloadURL().then(function (downloadURL) {
                    console.log('File available at', downloadURL);

                    productIcon = downloadURL;
                    $('#view_update_icon').find('img').attr('src', productIcon);
                    if (type === 'SAVE') {
                        saveItem();
                    } else if (type === 'UPDATE') {
                        updateItem();
                    }


                    return downloadURL;
                });
            });
        }


        function updateItem() {
            //convert to json object
            var formData = $('#new_form').serialize() + "&productIcon=" + productIcon;


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

                        searchProduct();
                        //show msg
                        showNotification(response.status, response.msg);

                        setTimeout(refreshPage, 2000);



                        productIcon = "";

                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }

        }

        function viewItem(code) {

            var dataString = "code=" + code;
            $.ajax({
                data: dataString,
                type: 'POST',
                url: "get.htm",
                success: function (data, textStatus, jqXHR) {
                    var response = JSON.parse(data);

                    //hide window if record is ok
                    if (response.status === 'success') {
                        $('#viewModel').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#view_productCode').html(record.code);
                        $('#view_description').html(record.description);
                        $('#view_statusCode').html(record.status_des);
                        $('#view_title').html(record.description);

                        //set icon
                        if (record.icon === null) {//check is null
                            //set default image                           
                            $('#view_icon').html("<img  width='100%'  src='<c:url value="/resources/images/no_img_smal.jpg"></c:url>' />");
                        } else {
                            //load real image
                            $('#view_icon').html("<img width='100%' src='" + record.icon + "' />");
                        }






                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }
        function editItem(code) {
            var dataString = "code=" + code;
            $('#btn_get_reset').show();    
            $('#upload_prg').html('');
            resetNewFrom();

            $('#new_form #icon').attr('data-validation', 'sp-allow');
           
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
                        $('#new_form #productCode').val(record.code);
                        $('#new_form #description').val(record.description);
                        $('#new_form #statusCode').val(record.status);

//                       
                        productIcon=record.icon;

                        //set icon
                        if (record.icon === null) {//check is null
                            //set default image                           
                            $('#view_update_icon').html("<img src='<c:url value="/resources/images/no_img_smal.jpg"></c:url>' />");
                        } else {
                            //load real image
                            $('#view_update_icon').html("<img width='163px' height='200px' src='" + record.icon + "' />");
                        }

                        //set read only productCode
                        $('#new_form #productCode').prop('readonly', true);
                        $('#btn_save').hide();
                        $('#new_form #btn_reset').hide();
                        $('#btn_update').show();
                        $('#view_update_icon').show();
                        $("#window_title").html("Edit Product Details")



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
                var dataString = "code=" + id;
                $.ajax({
                    data: dataString,
                    type: 'POST',
                    url: "delete.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //delete on firebase
                        deleteImage(id);
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
            showConfirmMsg("Delete Product", "Are you sure to delete :" + id + "?", deleteFunction)
        }

        function resetSearchFrom() {
            var form = $('#search_form');
            resultTable.fnClearTable();
            resetFrom(form);
        }
        function resetNewFrom() {
            var form = $('#new_form');
            resetFrom(form);
            $('#btn_save').prop('disabled', false);
        }


        function validateImage() {
            if ($('#icon').val().lastIndexOf(".png") == -1 && $('#icon').val().lastIndexOf(".jpg") == -1 && $('#icon').val().lastIndexOf(".jpeg") == -1) {
                $('#icon').val("");
                showNotification('error', 'The file is invalid or unsupported');
//
            } else {
                var input = $('#icon');
                if (input.files && input.files[0]) {
                    var reader = new FileReader();

                    reader.onload = function (e) {
                        $('#view_update_icon').find('img').attr('src', e.target.result);
                    }

                    reader.readAsDataURL(input.files[0]);
                }
            }
        }

        function deleteImage(val) {
            // Get a reference to the storage service, which is used to create references in your storage bucket
            var storage = firebase.storage();

            // Create a storage reference from our storage service
            var storageRef = storage.ref();
            // Create a child reference

            // Child references can also take paths delimited by '/'
            var spaceRef = storageRef.child(val);
            // spaceRef now points to "images/space.jpg"
            // imagesRef still points to "images"

            // Delete the file
            spaceRef.delete().then(function () {
                // File deleted successfully
            }).catch(function (error) {
                // Uh-oh, an error occurred!
            });


        }

        function refreshPage() {
            location.reload();
        }




    </script>
</html>
