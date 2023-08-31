/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*---------------------------craete HTML form data to JSON---------------------------------------*/
function createJSONObject(form) {
    var jsonObject = {};
    jsonArray=$(form).serializeArray();

    $.each(jsonArray, function () {
        if (jsonObject[this.name]) {
            if (!jsonObject[this.name].push) {
                jsonObject[this.name] = [jsonObject[this.name]];
            }
            jsonObject[this.name].push(this.value || '');
        } else {
            jsonObject[this.name] = this.value || '';
        }
    });
    
    return jsonObject;
}


