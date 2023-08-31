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
                                <h4>User Privilege Management</h4>
                            </div>

                        </div>

                        <div class="clearfix"></div>

                        <div class="row">
                            <div class="col-md-12 col-sm-12 col-xs-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h4>Set User Privilege</h4>                                        
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
                                        <form  class="form-horizontal" id="data_from">

                                            <div class="col-md-6 col-md-offset-3">
                                                <div class="form-group">
                                                    <label class="">User Role Description</label>
                                                    <div class="">
                                                        <select data-validation="required" type="text" class="form-control" name="dlbWbUserRole.userrolecode" id="userrolecode"  >
                                                            <option selected value="0">--Select User Role--</option>
                                                            <c:forEach var="item" items="${user_role_select_box}">
                                                                <option value="${item.userrolecode}">${item.description}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <label class="">Sections</label>
                                                    <div class="">
                                                        <select data-validation="required" type="text" class="form-control" name="dlbWbSection.sectioncode" id="sectioncode"  >
                                                            <option selected value="0">--Select Section--</option>
                                                            <c:forEach var="item" items="${sections}">
                                                                <option value="${item.sectioncode}">${item.description}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>

                                                <!-- set subsection-->
                                                <!-- No sub sections, this filed only for future enhancement-->
                                                <input value="NA" type="hidden" id="dlbWbSubSection" name="dlbWbSubSection.subsectioncode"/>

                                                <div class="form-group" align="right">
                                                    <label class="control-label"></label>
                                                    <div class="">
                                                        <button class="btn btn-default" onclick="resetForm()">Reset</button>
                                                        <button id="btn_save" type="button" class="btn submit" class=" btn btn-default" onclick="save()">Save</button>                                                                                     
                                                    </div>
                                                </div>
                                            </div>
                                        </form>
                                        <hr>
                                        <select id="page_list" multiple="multiple"  name="page_list" class="demo2">

                                        </select>

                                        <br>





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
        var page_list;
        $(document).ready(function () {

            //init dual select box
            page_list = $('#page_list').bootstrapDualListbox({
                nonSelectedListLabel: 'Available pages',
                selectedListLabel: 'Selected pages',
                preserveSelectionOnMove: 'moved',
                moveOnSelect: false


            });

            $('#sectioncode').change(function () {
                loadPageListBySection();
            });

        });

        $('#userrolecode').change(function () {
            loadPageListBySection();
        });




        function save() {
            var dataForm = $('#data_from').serialize();
            //get selected pages to array
            var pageList = page_list.bootstrapDualListbox().val();

//            //to json array
//            var pageListJSON = new Array();
//            $(pageList).each(function (index) {
//                var page = pageList[index];
//                pageListJSON.push({page});
//            });

            if( $('#sectioncode').val()!=='0'){
                
                var dataString = dataForm + "&page_list=" + JSON.stringify(pageList);


                $.ajax({
                    data: dataString,
                    url: "save.htm",
                    type: 'POST',
                    success: function (data, textStatus, jqXHR) {
                        var response = JSON.parse(data);

                        //show msg
                        showNotification(response.status, response.msg);


                    },
                    error: function (jqXHR, textStatus, errorThrown) {

                    }

                });
        
            }
            
            else{
                 showNotification('error', 'Please select section');
            }

         


        }


        function loadPageListBySection() {
            var sectioncode = $('#sectioncode').val();
            var dataString = $('#data_from').serialize() + "&section=" + sectioncode;

            //clear data
            page_list.html("");
            $.ajax({
                data: dataString,
                url: "load_pages.htm",
                type: 'POST',
                success: function (data, textStatus, jqXHR) {
                    var pageList = JSON.parse(data);
                    $(pageList).each(function (index) {
                        var page = pageList[index];
                        page_list.append('<option ' + page.selected_attr + ' value="' + page.page_id + '">' + page.page_name + '</option>');
                        page_list.bootstrapDualListbox('refresh');
                    });

                },
                error: function (jqXHR, textStatus, errorThrown) {

                }

            });
        }


        function resetForm() {
            var form = $('#data_from');

            resetFrom(form);
        }

    </script>
</html>
