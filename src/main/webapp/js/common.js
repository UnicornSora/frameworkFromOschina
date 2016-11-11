/**
 * User: football98
 * Date: 16-9-28
 */
var Common = {

    DateTimeFormJons:function(value,rec,index){
        var timestamp = new Date(value);
        return timestamp.toLocaleString();
    }
};

function get_length(str){
    if (str == null) return 0;
    if (typeof str != "string"){
        str += "";
    }
    return str.replace(/[^\x00-\xff]/g,"aa").length;
}

$.extend($.fn.textbox.defaults.rules, {
    number : {
        validator : function(value, param) {
            return /^[0-9]*$/.test(value);
        },
        message : "请输入数字"
    },
    chinese : {
        validator : function(value, param) {
            var reg = /^[\u4e00-\u9fa5]+$/i;
            return reg.test(value);
        },
        message : "请输入中文"
    },
    checkLength: {
        validator: function(value, param){
            return param[0] >= get_length(value);
        },
        message: '请输入最大{0}位字符'
    },
    specialCharacter: {
        validator: function(value, param){
            var reg = new RegExp("[`~!@#$^&*()=|{}':;'\\[\\]<>~！@#￥……&*（）——|{}【】‘；：”“'、？]");
            return !reg.test(value);
        },
        message: '不允许输入特殊字符'
    },
    englishUpperCase : {// 验证英语大写
        validator : function(value) {
            return /^[A-Z]+$/.test(value);
        },
        message : '请输入大写字母'
    },
    englishLowerCase  : {// 验证英语小写
        validator : function(value) {
            return /^[a-z]+$/.test(value);
        },
        message : '请输入小写字母'
    },
    password  : {// 大、小写英文字母，数字
        validator : function(value) {
            return /^[A-Za-z0-9]+$/.test(value);
        },
        message : '请输入大、小写字母或者数字'
    },
    UpperCaseAndNumber  : {// 大英文字母，数字
        validator : function(value) {
            return /^[A-Z0-9]+$/.test(value);
        },
        message : '请输入大字母或者数字'
    }

});
