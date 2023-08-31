/*--
 *   Author     : Kasun-PC
 * -*/
/*-----------------------form validate--------------*/

function validateForm(form) {
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
    $(inputs).each(function (index) {//loop inputs

        var input = inputs[index];//get input object
        $(input).removeClass('bg-pink');//remove already error indicate
        var valType = $(input).attr('data-validation');//get input validation type


//--------------------required fields validations
        if (valType === 'required') {//check is required
            var val = $(input).val().trim();
            if (isEmpty(val)) {
                errors++;//increment errors count
                //indicate error                

                $(input).addClass('error-indicate');

            }
        }

        if (valType === 'required-email') {//check is required


            var val = $(input).val().trim();

            if (isEmpty(val)) {
                errors++;//increment errors count
                //indicate error            
                $(input).addClass('error-indicate');
            } else {
                if (!isEmail(val)) {
                    errors++;//increment errors count          
                    showNotification('error', "Please enter valid e-mail address ");
                    $(input).addClass('error-indicate');

                }
            }
        }


        if (valType === 'required-sp-allow') {//check is required
            var val = $(input).val().trim();
            if (isEmpty(val)) {
                errors++;//increment errors count
                //indicate error                

                $(input).addClass('error-indicate');

            }
        }

//check special charater
        var val = $(input).val().trim();
        if (!isEmpty(val)) {
            if (valType === 'required-sp-allow' || valType === 'required-email' || valType === 'email' || valType === 'sp-allow') {


            } else {

                if (!isAllowedTexts(val)) {
                    errors++;
                    //indicate error                
                    $(input).addClass('error-indicate');

                }
            }

        }






//--------------------required telephone no fields validations
        if (valType === 'required-tel') {//check is required
            var val = $(input).val().trim();
            if (isEmpty(val)) {
                errors++;//increment errors count
                //indicate error                
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





        //nic validation
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
                if (!isDate(val)) {//check user is type nic or not
                    //user type nic
                    errors++;//increment errors count    
                  
                    $(input).addClass('error-indicate');
                }
            }


        }

        console.info($(input));

    });

    $(textareas).each(function (index) {//loop inputs
        var textarea = textareas[index];//get input object
        $(textarea).removeClass('bg-pink');//remove already error indicate
        var valType = $(textarea).attr('data-validation');//get input validation type        
        //--------------------required fields validations
        if (valType === 'required') {//check is required
            var val = $(textarea).val().trim();
            if (isEmpty(val)) {
                errors++;//increment errors count
                //indicate error              

                $(textarea).addClass('error-indicate');
            }
        }

        console.info($(textarea));
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



    $(files).each(function (index) {//loop inputs

        var file = files[index];//get input object
        $(file).removeClass('bg-pink');//remove already error indicate
        var valType = $(file).attr('data-validation');//get input validation type


//--------------------required fields validations
        if (valType === 'required') {//check is required
            var val = $(file).val().trim();
            if (isEmpty(val)) {
                errors++;//increment errors count
                //indicate error                

                $(file).addClass('error-indicate');



            }
        }


    });


//check errors and indicate-------------------------------------------
    if (errors > 0) {//check error count and return status
        $('#errorMsg').html("<br>Please enter valid data to indicated fields ");//for popup window
        $('#errorMsg').addClass("alert");
        showNotification('error', "Please enter valid data to indicated fields ");
        return false;
    } else {
        return true;
    }


}

//-------------------------Functions------------------------------------------------
//------- check empty funcation
function isEmpty(val) {
    if (val.length === 0 || val === "") {
        return true;
    } else {
        return false;
    }
}
function isSelectBoxEmpty(val) {


    if (val === "--Select--" || val === "0" || val === "ALL") {
        return true;
    } else {
        return false;
    }
}
//------- check empty funcation
function isEmpty(val) {
    if (val == null || val.length == 0 || val == "") {
        return true;
    } else {       
        return false;
    }
}
//------- check is number funcation
function isNumber(val) {
    return $.isNumeric(val);
}

//------- check is email
function isEmail(email) {
   
    var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;
    if (filter.test(email)) {       
        return true;
    } else {
         showNotification('error', "Invalid email address ");
        return false;
    }
   
}

//reset form
function resetFrom(form) {

    $(form).find("input").val("");
    $(form).find("select").val(0);
    $(form).find("textarea").val("");
    $(form).find("input").removeClass('error-indicate');
    $(form).find("select").removeClass('error-indicate');
    $(form).find("textarea").removeClass('error-indicate');
    $(form).find("file").removeClass('error-indicate');
    $("#errorMsg").removeClass('alert');
    $("#errorMsg").html("");
    $("#password2").removeClass('error-indicate');
    $('#scan_status').val("Not Uploaded");


}



//block enter non Numeric  values to text box
function isNumberKey(e) {

    var key = e.charCode || e.keyCode || 0;//get key code           
    //check key code 
    /*allow backspace, tab, delete, enter, arrows, numbers and keypad numbers,
     home, end, period, and numpad decimal*/
    if (
            key == 8 ||
            key == 9 ||
            key == 13 ||
            key == 46 ||
            key == 110 ||
            key == 190 ||
            (key >= 35 && key <= 40) ||
            (key >= 48 && key <= 57) ||
            (key >= 96 && key <= 105)) {
        return true;
    }

    return false;
}


function isPhonenumber(tel)
{
    var filter = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;
    if (filter.test(tel)) {

        return true;
    } else {
        return false;
    }
    return true;
}

//check special charater
function isAllowedTexts(val) {
    var filter = /^[a-z A-Z 0-9 \/\\,_-]*$/;               //Backward slash is used to escape forward slashes
    return filter.test(val);
}

//------- check is nic
function isNIC(nic) {
    var filter = /^[0-9]{9}[vVxX]$|[0-9]{12}/;
    if (filter.test(nic)) {

        return true;
    } else {
        return false;
    }
    return true;
}


function checkFromDateNToDate(fromDate, toDate) {
    return fromDate <= toDate;
}

function isIPAddress(ip) {
    var filter = /(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/;

    if (filter.test(ip)) {
        return true;
    } else {
        return false;
    }
    return true;

}

function onlyNumbers(evt) {
    var e = window.event || evt;
    var charCode = e.which || e.keyCode;
    if (charCode > 47 && charCode < 58)
        return true;
    else
        return false;
}

function isUrl(urlLg, urlSm) {

    function isCurrency(val) {
        var filter = /^(\d{1,3}(\,\d{3})*|(\d+))(\.\d{2})/;

        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }
        console.info('call');
        return true;
    }

    console.log(urlLg);
    console.log(urlSm);
    var re = /^(http[s]?:\/\/){0,1}(www\.){0,1}[a-zA-Z0-9\.\-]+\.[a-zA-Z]{2,5}[\.]{0,1}/;
    if (re.test(urlLg) && re.test(urlSm)) {
        return true;
    } else {
        return false;
    }
    return true;
}




function isMacAddress(mac) {
    var filter = /^([0-9A-F]{2}[:-]){5}([0-9A-F]{2})$/;

    if (filter.test(mac)) {
        return true;
    } else {
        return false;
    }
    return true;

}

function isNumberOnly(e) {

    if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110]) !== -1 ||
            (e.keyCode === 65 && (e.ctrlKey === true || e.metaKey === true)) ||
            (e.keyCode >= 35 && e.keyCode <= 40))
    {
        return;
    }
    // Ensure that it is a number and stop the keypress
    if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
        //showNotification('error', "Please input numeric characters only"); 
        e.preventDefault();
    }
}

function isCurrencyValidate(data) {
    var currency = /^\d+(?:\.\d{0,2})$/;
    function onlyNumbers(evt) {
        var e = window.event || evt;
        var charCode = e.which || e.keyCode;
        if (charCode > 47 && charCode < 58)
            return true;
        else
            return false;
    }


    function isCurrency(val) {
        var filter = /^(\d{1,3}(\,\d{3})*|(\d+))(\.\d{2})/;

        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }


    }



    if (currency.test) {
        return true;
    } else {
        return false;
    }
    return true;

}

function isDate(txt) {

//    var regex = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/g;
    var regex = /([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/;


    return regex.test(txt);

}

function selectAllTest(element){
    element.select();
    console.info(element);
}


