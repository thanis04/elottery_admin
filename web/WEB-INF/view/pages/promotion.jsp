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
            .modal-promotion{
                height: 400px;
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
                                <h4>Promotion Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Promotion</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">                                           

                                            <div class="form-group">
                                                <label>Description</label>                                                   
                                                <input class="form-control" type="text" name="description" id="description" placeholder="Search" maxlength="64" />
                                            </div>

                                            <div class="form-group">
                                                <button type="button" onclick="hideShowButton()" class="btn add-new" data-toggle="modal" data-target="#addNew">Add New</button>
                                                <div class="add-button">
                                                    <button type="button" class="btn btn-secondary" onclick="resetSearchFrom()">Reset</button>
                                                    <button type="button" class="btn submit" onclick="search();">Search</button>
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
                                        <th>Promotion ID</th>
                                        <th>Description</th>                                                          
                                        <th>Valid From</th>
                                        <th>Valid To</th>                                     
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
                                <h4 id="window_title" class="modal-title">Add New Promotion List</h4>
                                <hr class="hr" />
                            </div>
                            <div class="modal-body modal-promotion">
                                <!-- model body -->
                                <form  autocomplete="off"  class="form-horizontal" id="new_form">                                         

                                    <div class="form-group">                                     
                                        <div class="">
                                            <input maxlength="10"  value="0" class="form-control" type="hidden" name="id" id="id"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label >Description<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="description" id="description" placeholder="Description" maxlength="63"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label >Valid From<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" type="text" class="form-control date" id="validFrom" name="validFromStr" placeholder="Select Date" />

                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label >Valid To<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input  data-validation="required" type="text" class="form-control date" id="validTo" name="validToStr" placeholder="Select Date" />
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label >Small Image (Jpeg and Png Only Max 10MB)<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input onchange="validateImageSml()" data-validation="required-sp-allow"  data-validation="required-sp-allow"  type="file" class="form-control" id="smImage" name="smImage"  />
                                        </div>
                                    </div>
                                    <div class="row">
                                        <img id="prv_smImage" width="80%"/>
                                    </div>
                                    <div class="form-group">
                                        <label>Large Image (Jpeg and Png Only Max 10MB)<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required-sp-allow" onchange="validateImageLg()"  type="file" class="form-control" id="lgImage" name="lgImage"  />
                                        </div>
                                    </div>

                                    <div class="row">
                                        <img id="prv_lgImage" width="80%"/>
                                    </div>


                                    <div class="form-group">
                                        <label>Status<span class="text-red"> *</span></label>
                                        <div class="">
                                            <select data-validation="required" class="form-control" name="dlbStatus.statusCode" id="statusCode">
                                                <option selected value="0">--Select--</option>
                                                <option value="<%=SystemVarList.ACTIVE%>">Active</option>
                                                <option value="<%=SystemVarList.INACTIVE%>">Inactive</option>

                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group col-lg-6">
                                        <label id="upload_prg" class="label label-primary"></label>
                                    </div>


                                </form> 

                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

                                    </div> 
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                <button id="btn_get_reset" type="button" class="btn btn-secondary" onclick="editReset($('#new_form #id').val())" >Reset</button>
                                <button type="button" id="btn_upload" class="btn submit" onclick="clickSave()">Save</button>                                                      
                                <button type="button" id="btn_update" class="btn submit" onclick="clickUpdate()">Update</button>                                                      
                                <button type="button" id="btn_reset"  class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button> 
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

        var largeImgUrl = "";
        var smallImgUrl = "";

        var storageBucket = "";
        var storageApp = "";

        var resultTable;
        $(document).ready(function () {
            $(function () {
                $("#validFrom").datepicker({"showAnim": "blind", "dateFormat": "yy-mm-dd"});
                $("#validTo").datepicker({"showAnim": "blind", "dateFormat": "yy-mm-dd"});
            });

            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            //hide update button
            $('#btn_update').hide();

            //firebase init
            storageBucket = "gs://testproject-ff23b.appspot.com";

            var config = {
                apiKey: "AIzaSyAHJ2mQJIDkQ0ngtjA5TNbKkQ53gjW3u00",
                authDomain: "testproject-ff23b.appspot.com",
                databaseURL: "https://testproject-ff23b.firebaseio.com/",
                projectId: "testproject-ff23b",
                storageBucket: storageBucket
            };
            storageApp = firebase.initializeApp(config);

        });

        function hideShowButton() {
            //hide/show button
            $('#btn_update').hide();
            $('#btn_get_reset').hide();
            $('#btn_reset').show();
            $('#view_update_icon').hide();
            $('#btn_upload').show();
//            $('#new_form #product').prop('readonly', false);
            $("#window_title").html("Add Promotion ");
            $('#new_form #smImage').attr('data-validation', 'required-sp-allow');
            $('#new_form #lgImage').attr('data-validation', 'required-sp-allow');
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
                            var viewBtn = "<span onclick='viewItem(\"" + item.id + "\")' class='fa fa-eye btn btn-primary' title='View Promotion'></span>";
                            var editBtn = "<span onclick='editItem(\"" + item.id + "\")' class='fa fa-pencil btn btn-success' title='Edit Promotion'></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.id + "\")' class='fa fa-trash btn btn-danger' title='Delete Promotion'></span>";

                            var row = [item.id, item.description, item.valid_from, item.valid_to, item.status, viewBtn + " " + editBtn + " " + delBtn];
                            resultTable.fnAddData(row);
                        });
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function clickSave() {

            var validFrom = $('#validFrom').val();
            var validTo = $('#validTo').val();
            if (checkFromDateNToDate(validFrom, validTo)) {
                if (validateForm($('#new_form'))) {
                    $('#btn_upload').prop('disabled', true);
                    uploadImage('SAVE');
                }



            } else {
                showNotification('error', 'Invalid Date combination');
                $('#btn_upload').prop('disabled', false);
            }

        }


        function clickUpdate() {
            var validFrom = $('#validFrom').val();
            var validTo = $('#validTo').val();
            if (checkFromDateNToDate(validFrom, validTo)) {
                if (validateForm($('#new_form'))) {
                    $('#btn_update').prop('disabled', true);
                    var smImage = $('#new_form #smImage').val();
                    var lgImage = $('#new_form #lgImage').val();
                    var description = $('#new_form #description').val();
                    if (smImage !== '') {
                        uploadUpdatedImage(description, 'S');
                        console.info('S');

                    }

                    if (lgImage !== '') {
                        uploadUpdatedImage(description, 'L');
                        console.info('L');
                    }
                    updateItem();

                }

            } else {
                showNotification('error', 'Invalid Date combination');
                $('#btn_update').prop('disabled', false);
            }
        }



        function savePromo() {


            //convert to json object
            var formData = $('#new_form').serialize() + "&smallImgUrl=" + smallImgUrl + "&largeImgUrl=" + largeImgUrl;


            if (validateForm($('#new_form'))) {

                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "save.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //show msg
                        showNotification(response.status, response.msg);
                        $('#btn_upload').prop('disabled', false);
                        refresh();
                        $('#addNew').modal('toggle');
                        if (response.status === 'success') {
                            resetFrom($('#new_form'));
                            smallImgUrl = "";
                            largeImgUrl = "";
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }
//               
//           
        }


        function updateItem() {
            //convert to json object
            var formData = $('#new_form').serialize() + "&smallImgUrl=" + smallImgUrl + "&largeImgUrl=" + largeImgUrl;


            if (validateForm($('#new_form'))) {
                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "update.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //hide window if record is ok
                        if (response.status === 'success') {

                            $('#prv_smImage').attr('src', smallImgUrl);


                            smallImgUrl = "";
                            largeImgUrl = "";
                            var id = $('#new_form #id').val();
                            $('#addNew').modal('toggle');

                            setTimeout(refreshPage, 2000);

                        }

                        search();
                        //show msg
                        showNotification(response.status, response.msg);
                        $('#btn_update').prop('disabled', false);


                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }

        }



        function uploadImage(type) {

            var des = $("#new_form #description").val();
            const lgFile = $('#lgImage').get(0).files[0];
            const smFile = $('#smImage').get(0).files[0];



            // Points to the root reference
            var storageRef = firebase.storage().ref();

            var metadata = {
                contentType: 'image/jpeg'
            };

            // Upload the file and metadata
            var uploadTaskSm = storageRef.child(des + "_sm").put(smFile, metadata);
            var uploadTaskLg = storageRef.child(des + "_lg").put(lgFile, metadata);

            getUrlSm(uploadTaskSm, uploadTaskLg, type);
        }

        function getUrlSm(uploadTaskSm, uploadTaskLg, type) {

            uploadTaskSm.on('state_changed', function (snapshot) {

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
                uploadTaskSm.snapshot.ref.getDownloadURL().then(function (downloadURL) {
                    console.log('File available at', downloadURL);

                    //getUrlLg(uploadTaskLg);               
                    smallImgUrl = downloadURL;
                    $('#new_form #prv_smImage').attr('src', smallImgUrl);

                    getUrlLg(uploadTaskLg, type);

                });
            });
        }



        function getUrlLg(uploadTaskLg, type) {

            uploadTaskLg.on('state_changed', function (snapshot) {

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
            }, function () {
                // Handle successful uploads on complete
                // For instance, get the download URL: https://firebasestorage.googleapis.com/...
                uploadTaskLg.snapshot.ref.getDownloadURL().then(function (downloadURL) {

                    largeImgUrl = downloadURL;
                    $('#new_form #prv_lgImage').attr('src', largeImgUrl);

                    if (type === 'SAVE') {
                        savePromo();
                    } else if (type === 'UPDATE') {
                        updateItem();

                    }

                });
            });
        }

        function editItem(id) {
            $('#btn_get_reset').show();
            $('#btn_reset').hide();
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
                        $('#new_form #id').val(record.id);
                        $('#new_form #description').val(record.description);
                        $('#new_form #validFrom').val(record.valid_from);
                        $('#new_form #validTo').val(record.valid_to);

                        $('#new_form #prv_smImage').attr('src', record.smallImgUrl);
                        $('#new_form #prv_lgImage').attr('src', record.largeImgUrl);

                        largeImgUrl = record.largeImgUrl;
                        smallImgUrl = record.smallImgUrl;

                        $('#new_form #statusCode').val(record.status);

                        //enable/disable ui elemnts
                        $('#new_form *').attr('disabled', false);
                        $('#new_form #employeeid').prop('readonly', false);
                        $('#btn_update').show();
                        $('#btn_upload').hide();
                        $("#window_title").html("Edit Promotion Details:" + record.description);

                        $('#new_form #smImage').attr('data-validation', 'sp-allow');
                        $('#new_form #lgImage').attr('data-validation', 'sp-allow');



                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }
        function viewItem(id) {
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


                        var record = response.record;
                        $('#new_form #id').val(record.id);
                        $('#new_form #description').val(record.description);
                        $('#new_form #validFrom').val(record.valid_from);
                        $('#new_form #validTo').val(record.valid_to);
                        $('#new_form #prv_smImage').attr('src', record.smallImgUrl);
                        $('#new_form #prv_lgImage').attr('src', record.largeImgUrl);
                        $('#new_form #statusCode').val(record.status);




                        //enable/disable ui elemnts
                        $('#new_form *').attr('disabled', true);
                        $('#new_form #id').prop('readonly', true);
                        $('#btn_upload').hide();
                        $('#btn_update').hide();
                        $('#btn_reset').hide();
                        $('#btn_get_reset').hide();
                        $("#window_title").html("View Promotion Details:" + record.description)



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
            $('#upload_prg').html('');
            resetFrom(form);
        }
        function resetNewFrom() {
            var form = $('#new_form');
            $('#new_form #employeeid').prop('readonly', false);
            $('#new_form *').attr('disabled', false);
            $('#btn_reset').show();
            $('#prv_lgImage').attr('src', '');
            $('#prv_smImage').attr('src', '');
            resetFrom(form);
        }





        //search function
        function refresh() {

            $.ajax({
                url: "list.htm",
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
                            var viewBtn = "<span onclick='viewItem(\"" + item.id + "\")' class='fa fa-eye btn btn-primary' title='Edit Promotion'></span>";
                            var editBtn = "<span onclick='editItem(\"" + item.id + "\")' class='fa fa-pencil btn btn-success' title='Edit Promotion'></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.id + "\")' class='fa fa-trash btn btn-danger' title='Delete Promotion'></span>";

                            var row = [item.id, item.description, item.valid_from, item.valid_to, item.status, viewBtn + " " + editBtn + " " + delBtn];
                            resultTable.fnAddData(row);
                        });
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
                        //delete on firebase
                        deleteImage(id + "_sm");
                        deleteImage(id + "_lg");
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
            showConfirmMsg("Delete Promotion", "Are you sure to delete promotion " + id + "?", deleteFunction)
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

        function validateImageSml() {
            if ($('#smImage').val().toUpperCase().lastIndexOf(".PNG")=== -1 &&
                    $('#smImage').val().toUpperCase().lastIndexOf(".JPG") === -1 &&
                    $('#smImage').val().toUpperCase().lastIndexOf(".JPEG") === -1) {
                $('#smImage').val("");
                showNotification('error', 'The file is invalid or unsupported');
//
            }


        }

        function validateImageLg() {
            if ($('#lgImage').val().toUpperCase().lastIndexOf(".PNG") === -1 &&
                    $('#lgImage').val().toUpperCase().lastIndexOf(".JPG") === -1 &&
                    $('#lgImage').val().toUpperCase().lastIndexOf(".JPEG") === -1) {
                $('#lgImage').val("");
                showNotification('error', 'The file is invalid or unsupported');
//
            }

        }




        function uploadUpdatedImage(description, type) {

            var des = '';
            var file = '';

            if (type === 'L') {
                des = description + "_lg";
                file = $('#lgImage').get(0).files[0];
            } else if (type === 'S') {
                des = description + "_sm";
                file = $('#smImage').get(0).files[0];
            }



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


        }

        function refreshPage() {
            location.reload();
        }

        function editReset(id) {
            $('#smImage').val('');
            $('#lgImage').val('');

            editItem(id);

        }




    </script>
</html>
