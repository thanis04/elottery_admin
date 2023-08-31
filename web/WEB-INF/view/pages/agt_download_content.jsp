<%-- 
    Document   : agt_download_content
    Created on : Apr 16, 2021, 8:36:49 AM
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
                                <h4>Marketing Content Download</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>


                        <div id="search_result">
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
            var formData = {
                "name": ""
            }

            $.ajax({
                data: formData,
                url: "search_agent.htm",
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
                            var download = "<span id='download' onclick='downloadFile(\"" + item.url + "\")' class='fa fa-download btn btn-primary' title='Download Content'></span>";
                            var row = [item.id, item.name, item.description, item.createdTime, download];
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
                            fileName = fileName+".rar"
                        }
                        a.href = window.URL.createObjectURL(blob);
                        a.download = fileName.replace('_marketing', '');
                        a.click();
                    });


        }

        function download(objurl) {
            var storage = firebase.storage();
            var httpsReference = storage.refFromURL(objurl);
            var storageRef = storage.ref(httpsReference.location.path_ + '.jpeg');
            // Get the download URL
            storageRef.getDownloadURL()
                    .then((url) => {
                        // Insert url into an <img> tag to "download"
                    })
                    .catch((error) => {
                        // A full list of error codes is available at
                        // https://firebase.google.com/docs/storage/web/handle-errors
                        switch (error.code) {
                            case 'storage/object-not-found':
                                // File doesn't exist
                                break;
                            case 'storage/unauthorized':
                                // User doesn't have permission to access the object
                                break;
                            case 'storage/canceled':
                                // User canceled the upload
                                break;

                                // ...

                            case 'storage/unknown':
                                // Unknown error occurred, inspect the server response
                                break;
                        }
                    });
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

