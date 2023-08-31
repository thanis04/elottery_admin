<%-- 
    Document   : test
    Created on : Oct 18, 2018, 10:46:47 AM
    Author     : nipuna_k
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>e-Lottery Sales & Distribution Solution | Control Panel</title>
    </head>

    <body class="nav-md">
        <div class="container body">
            <div class="main_container">

                <!-- page content -->
                <div class="right_col" role="main" style="min-height: 600px;">
                    <div class="">
                        <div class="page-title">
                            <div class="title_left" >
                                <h4>Upload</h4>
                            </div>
                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-sm-12 col-md-6 col-lg-6 col-md-offset-3 col-lg-offset-3" style="float:none !important;">
                                <div class="x_panel">
                                    <!--action="save.htm"-->
                                    <div class="x_content">
                                        <form autocomplete="off" method="post" target="_blank" role="form" enctype="multipart/form-data">
                                            <div class="form-group">
                                                <label class="">Description<span class="text-red"> *</span></label>
                                                <input type="text" class="form-control date" id="description" name="description" placeholder="Description" />
                                            </div>
                                            <div class="form-group">
                                                <label class="">Image<span class="text-red"> *</span></label>
                                                <input type="file" class="form-control date" id="image" name="image" placeholder="Choose File" />
                                            </div>

                                            <div class="form-group" align="left">
                                                <button type="button" class="btn submit" onclick="save();">Save</button>
                                            </div>

                                            <br>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </body>  
    <script src="https://www.gstatic.com/firebasejs/5.5.5/firebase.js">

    </script>
    <script src='<c:url value="/resources/jquery/jquery.min.js"/>'>
    </script>
    <script>
        function save() {
            var storageBucket = "gs://testproject-ff23b.appspot.com";
            var des = $("#description").val();
            const file = $('#image').get(0).files[0];

            // Set the configuration for your app
            // TODO: Replace with your project's config object
            var config = {
                apiKey: "AIzaSyAHJ2mQJIDkQ0ngtjA5TNbKkQ53gjW3u00",
                authDomain: "testproject-ff23b.firebaseapp.com",
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
//            location.reload();  
            console.log(storageRef.child(des));

            getUrl(des);
        }

        function getUrl(des) {

            var storage = firebase.storage();
            storageRef = storage.ref('');
            storageRef.child(des).getDownloadURL().then(function (url) {
                var xhr = new XMLHttpRequest();
                xhr.responseType = 'blob';
                xhr.onload = function (event) {
                    var blob = xhr.response;
                };
                xhr.open('GET', url);
                xhr.send();

                // Or inserted into an <img> element:
                var img = document.getElementById('photo1');
                img.src = url;
            }).catch(function (error) {
                // Handle any errors
            });
        }
    </script>
</html>

