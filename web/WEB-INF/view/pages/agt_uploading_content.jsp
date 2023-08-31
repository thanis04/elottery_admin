<%-- 
    Document   : agt_uploading_content
    Created on : Apr 16, 2021, 8:33:48 AM
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
                                <h4>Marketing Content Upload </h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Search Marketing Content</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="search_form">                                           

                                            <div class="form-group">
                                                <label>Name</label>                                                   
                                                <input class="form-control" type="text" name="nameSearch" id="nameSearch" placeholder="Search" maxlength="64" />
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
                                        <th>#</th>
                                        <th>Name</th>
                                        <th>Description</th>                                                          
                                        <th>Uploaded date</th>
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
                                <h4 id="window_title" class="modal-title">Add New Content</h4>
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
                                        <label >Name<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="name" id="name" placeholder="Name" maxlength="63"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label >Description<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="description" id="description" placeholder="Description" maxlength="63"/>
                                        </div>
                                    </div>

                                    <div class="form-group" id="media">
                                        <label >Media Asset<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required-sp-allow"  data-validation="required-sp-allow"  type="file" class="form-control" id="image" name="image"  />
                                        </div>
                                    </div>

                                    <div class="row">
                                        <img id="prv_image" width="80%"/>
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
                                <!--<button id="btn_get_reset" type="button" class="btn btn-secondary" onclick="editReset($('#new_form #id').val())" >Reset</button>-->
                                <button type="button" id="btn_upload" class="btn submit" onclick="clickSave()">Save</button>                                                      
                                <!--<button type="button" id="btn_update" class="btn submit" onclick="clickUpdate()">Update</button>-->                                                      
                                <button type="button" id="btn_reset"  class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button> 
                            </div>
                        </div>

                    </div>
                </div>

                <!--Edit Model-->

                <div class="modal fade" id="edit" role="dialog">
                    <div class="modal-dialog">                       
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-white">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4 id="window_title" class="modal-title">Update Content</h4>
                                <hr class="hr" />
                            </div>
                            <div class="modal-body modal-promotion">
                                <!-- model body -->
                                <form  autocomplete="off"  class="form-horizontal" id="edit_form">                                         

                                    <div class="form-group">                                     
                                        <div class="">
                                            <input maxlength="10"  value="0" class="form-control" type="hidden" name="id" id="id"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label >Name<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="name" id="name" placeholder="Name" maxlength="63"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label>Description<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="description" id="description" placeholder="Description" maxlength="63"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label>Status<span class="text-red"> *</span></label>
                                        <div class="">
                                            <select data-validation="required" class="form-control" name="statusCode" id="statusCode">
                                                <option value="ACTIVE">Active</option>
                                                <option value="INACTIVE">Inactive</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label>Media Asset</label>
                                        <div class="">
                                            <input type="file" class="form-control" id="imageEdit" name="imageEdit" />
                                        </div>
                                    </div>

                                    <div class="form-group" id="fileItem">

                                    </div>

                                    <div class="row">
                                        <img id="prv_image" width="80%"/>
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
                                <!--<button id="btn_get_reset" type="button" class="btn btn-secondary" onclick="editReset($('#new_form #id').val())" >Reset</button>-->
                                <!--<button type="button" id="btn_upload" class="btn submit" onclick="clickSave()">Save</button>-->                                                      
                                <button type="button" id="btn_update" class="btn submit" onclick="clickUpdate()">Update</button>                                                      
                                <!--<button type="button" id="btn_reset"  class="btn btn-secondary" onclick="resetNewFrom()" >Reset</button>--> 
                            </div>
                        </div>

                    </div>
                </div>

                <div class="modal fade" id="viewCon" role="dialog">
                    <div class="modal-dialog">                       
                        <!-- Modal content-->
                        <div class="modal-content">
                            <div class="modal-header bg-white">
                                <button type="button" class="close " data-dismiss="modal">&times;</button>
                                <h4 id="window_title" class="modal-title">View Content</h4>
                                <hr class="hr" />
                            </div>
                            <div class="modal-body modal-promotion">
                                <!-- model body -->
                                <form  autocomplete="off"  class="form-horizontal" id="view_form">                                         

                                    <div class="form-group">                                     
                                        <div class="">
                                            <input maxlength="10"  value="0" class="form-control" type="hidden" name="id" id="id"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label >Name<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="name" id="name" placeholder="Name" maxlength="63"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label >Description<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="description" id="description" placeholder="Description" maxlength="63"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label >Status<span class="text-red"> *</span></label>
                                        <div class="">
                                            <input data-validation="required" class="form-control" type="text" name="status" id="status" placeholder="Status" maxlength="63"/>
                                        </div>
                                    </div> 

                                    <div class="row">
                                        <img id="prv_image" width="80%"/>
                                    </div>
                                </form> 
                                <div class="form-group" id="fileItemView">

                                </div>
                                <div class="row">
                                    <div class="form-group col-lg-6">
                                        <span class="pull-left text-secondary"> Mandatory fields are marked with [<span class="text-red">*</span>] </span>

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

        var largeImgUrl = "";
        var smallImgUrl = "";
        var imgUrl = "";

        var storageBucket = "";
        var storageApp = "";

        var resultTable;
        $(document).ready(function () {

            //init data table
            $('#search_result').hide();
            resultTable = $('#result_table').dataTable({
                "searching": false
            });

            search();
            //hide update button

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
            $("#window_title").html("Add Content ");
            $('#new_form #image').attr('data-validation', 'required-sp-allow');
            resetNewFrom();

        }

        //search function
        function search() {
            var name = "";
            if ($('#nameSearch').val() === null || $('#nameSearch').val() === "") {
                name = "null";
            } else {
                name = $('#nameSearch').val();
            }

            var formData = {
                "name": name
            }

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
                            var viewBtn = "<span onclick='viewItem(\"" + item.id + "\")' class='fa fa-eye btn btn-primary' title='View Content'></span>";
                            var editBtn = "<span onclick='editItem(\"" + item.id + "\")' class='fa fa-pencil btn btn-success' title='Edit Content'></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.id + "\")' class='fa fa-trash btn btn-danger' title='Delete Content'></span>";
                            var row = [item.id, item.name, item.description, item.createdTime, editBtn + ' ' + viewBtn + ' ' + delBtn];
                            resultTable.fnAddData(row);
                        });
                    }


                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });

        }

        function downloadFile(url) {
            var storage = firebase.storage();
            var httpsReference = storage.refFromURL(url);
            var fileName = httpsReference.location.path_;
            fetch(url)
                    .then(res => res.blob())
                    .then(blob => {
                        const a = document.createElement('a');
                        if (blob.type === "application/octet-stream") {
                            fileName = fileName + ".rar"
                        }
                        a.href = window.URL.createObjectURL(blob);
                        a.download = fileName.replace('_marketing', '');
                        a.click();
                    });
        }

        function clickSave() {

            if (validateForm($('#new_form'))) {
                $('#btn_upload').prop('disabled', true);
                uploadImage('SAVE');
            }

        }

        function uploadImage(type) {

            var name = $("#new_form #name").val();
            const marketingFile = $('#image').get(0).files[0];

            var storageRef = firebase.storage().ref();

            var metadata = {
                contentType: marketingFile.type
            };
            var uploadTaskSm = storageRef.child(name + "_marketing").put(marketingFile, metadata);

            getUrlSm(uploadTaskSm, type);
        }

        function getUrlSm(image, type) {

            image.on('state_changed', function (snapshot) {

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
                image.snapshot.ref.getDownloadURL().then(function (downloadURL) {

                    //getUrlLg(uploadTaskLg);               
                    imgUrl = downloadURL;
                    $('#new_form #prv_smImage').attr('src', imgUrl);
                    save();
                });
            });
        }

        function save() {

            var formData = {
                "name": $('#name').val(),
                "description": $('#description').val(),
                "url": imgUrl
            }

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
                        $('#addNew').modal('toggle');
                        if (response.status === 'success') {
                            resetFrom($('#new_form'));
                            imgUrl = "";
                            setTimeout(refreshPage, 2000);
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }
//               
//           
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
                        $('#viewCon').modal().show();

                        var record = response.record;

                        $('#viewCon #id').val(record.id);
                        $('#viewCon #name').val(record.name);
                        $('#viewCon #description').val(record.description);
//                        $('#viewCon #prv_image').attr('src', record.url);
                        $('#fileItemView').empty();
                        $('#viewCon #fileItemView').append("<span onclick='downloadFile(\"" + record.url + "\")' class='fa fa-download btn btn-primary' title='Download Content'></span>");
                        $('#viewCon #status').val(record.status);

                        //enable/disable ui elemnts
                        $('#view_form *').attr('disabled', true);
                        $('#view_form #id').prop('readonly', true);
                        $('#fileItemView').prop('disabled', false);

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
                            var viewBtn = "<span onclick='viewItem(\"" + item.id + "\")' class='fa fa-eye btn btn-primary' title='Edit Content'></span>";
                            var editBtn = "<span onclick='editItem(\"" + item.id + "\")' class='fa fa-pencil btn btn-success' title='Edit Content'></span>";
                            var delBtn = "<span onclick='deleteItem(\"" + item.id + "\")' class='fa fa-trash btn btn-danger' title='Delete Content'></span>";

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
            showConfirmMsg("Delete Content", "Are you sure to delete content?", deleteFunction);
            var itemName = "";

            var dataString = "id=" + id;
            function deleteFunction() {
                $.ajax({
                    data: dataString,
                    type: 'POST',
                    url: "get.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);
                        var record = response.record;
                        if (response.status === 'success') {
                            itemName = record.url;

                            var dataString = "id=" + id;
                            $.ajax({
                                data: dataString,
                                type: 'POST',
                                url: "delete.htm",
                                success: function (data, textStatus, jqXHR) {
                                    var response = JSON.parse(data);
                                    //delete on firebase
                                    deleteImage(itemName);
                                    //show msg
                                    showNotification(response.status, response.msg);
                                    setTimeout(refreshPage, 2000);
                                },
                                error: function (jqXHR, textStatus, errorThrown) {

                                }
                            });


                        } else {
                            showNotification(response.status, response.msg);
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
            }
        }


        function deleteImage(val) {
            var desertRef = firebase.storage().refFromURL(val);
            desertRef.delete().then(function () {
            }).catch(function (error) {
            });
        }

        function clickUpdate() {
            if (validateForm($('#edit_form'))) {
                $('#btn_update').prop('disabled', true);
                var image = $('#edit_form #imageEdit').val();
                if (image !== '') {
                    uploadUpdatedImage();
                }

                updateItem();

            }


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
                        $('#edit').modal().show();

                        //show item details to form
                        var record = response.record;
                        $('#edit_form #id').val(record.id);
                        $('#edit_form #name').val(record.name);
                        $('#edit_form #description').val(record.description);
                        $('#edit_form #statusCode').val(record.status);
//                        $('#edit_form #prv_image').attr('src', record.url);

                        $('#edit_form #fileItem').empty();
                        $('#edit_form #fileItem').append("<span onclick='downloadFile(\"" + record.url + "\")' class='fa fa-download btn btn-primary' title='Download Content'></span>");
                        imgUrl = record.url;
                    } else {
                        //show msg
                        showNotification(response.status, response.msg);
                    }

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });


        }

        function uploadUpdatedImage() {


            var name = $("#edit_form #name").val();
            const marketingFile = $('#edit_form #imageEdit').get(0).files[0];


            // Points to the root reference
            var storageRef = firebase.storage().ref();

            var metadata = {
                contentType: marketingFile.type
            };

            // Upload the file and metadata
            var uploadTask = storageRef.child(name + "_marketing").put(marketingFile, metadata);
            getUpdatedUrlLg(uploadTask);


        }

        function getUpdatedUrlLg(uploadTaskLg) {

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

                    imgUrl = downloadURL;
                    $('#edit_form #prv_image').attr('src', largeImgUrl);

                    updateItem();

                });
            });
        }

        function updateItem() {
            //convert to json object
            var formData = {
                "id": $('#edit_form #id').val(),
                "name": $('#edit_form #name').val(),
                "description": $('#edit_form #description').val(),
                "status": $('#edit_form #statusCode').val(),
                "url": imgUrl,
            }

            if (validateForm($('#edit_form'))) {
                $.ajax({
                    data: formData,
                    type: 'POST',
                    url: "update.htm",
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //hide window if record is ok
                        if (response.status === 'success') {
                            $('#edit').modal('toggle');

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
