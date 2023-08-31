/*------------------------------------------------------------------
 * System nofification Funactions
 *------------------------------------------------------------------*/
//Message types //alert | success | error | warning | info

function showNotification(type, message) {


    $.notify({
        icon: 'fa fa-times-circle',
        message: message
    }, {
        type: type,
        z_index: 9999
    });

    $(".se-pre-con").fadeOut("slow");
}


function showConfirmMsg(title, message, success_fun) {

    $.ConfirmMessageBox({
        title: title,
        message: message,
        success_fun: success_fun,
        showActionButton: true,
        actionButtionText: 'YES'
    }).show();

}

//ConfirmMessageBox------------------------------------
$.ConfirmMessageBox = function (options) {
    return Object.create(ConfirmMessageBox).init(options);
};

var ConfirmMessageBox = {
    container: null,
    options: {
        title: '',
        message: '',
        success_fun: '',
        showActionButton: true,
        actionButtionText: 'YES'
    },
    init: function (options) {
        this.options = $.extend({}, this.options, options);
        this.container = "<div id='confmessage' class='modal fade bs-example-modal-sm' tabindex='-1' role='dialog' aria-labelledby='mySmallModalLabel'>" +
                "<div class='modal-dialog modal-sm'>" +
                "<div class='modal-content'>" +
                "<div class='modal-header '>" +
                "<h4 class='modal-title' id='myModalLabel'>" + this.options.title + "" +
                "<button type='button' class='close' data-dismiss='modal' aria-label='Close'><span aria-hidden='true'>&times;</span></button> </h4> </div>" +
                "<div class='modal-body'> " + this.options.message + " </div> ";
        if (this.options.showActionButton) {
            this.container = this.container + "<div class='modal-footer'> <button type='button' class='btn btn-default' data-dismiss='modal'>No</button> <button type='button' class='btn btn-info' id='confirmok' data-dismiss='modal' >" + this.options.actionButtionText + "</button>";
        }
        this.container = this.container + "</div> </div></div></div></div>";
        return this;
    },
    show: function () {
        $('#confmessage').remove();
        $(this.container).appendTo('body');
        $('#confirmok').click(this.options.success_fun);
        $('#confmessage').modal();
        $('#confmessage').modal({keyboard: false});
    }

};