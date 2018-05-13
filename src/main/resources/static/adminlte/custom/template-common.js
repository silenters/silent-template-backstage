/**
 * 创建DataTable
 * @param table_id	表格ID
 * @param dataTableOpetion	DataTable参数，若不填，则使用默认参数
 * @returns
 */
function createDataTable(table_id, dataTableOpetion) {
	// 若没有自定义设置DataTable参数，则使用默认参数
	if(isFalse(dataTableOpetion)) {
		dataTableOpetion = {
			"oLanguage": {
				"sSearch": "搜索:",
		        "sLengthMenu": "每页显示 _MENU_ 条记录",
		        "sZeroRecords": "Nothing found - 没有记录",
		        "sInfo": "显示第  _START_ 条到第  _END_ 条记录,一共  _TOTAL_ 条记录",
		        "sInfoEmpty": "显示0条记录",
		        "oPaginate": {
		        	"sPrevious": " 上一页 ",
		            "sNext":     " 下一页 ",
		        }
		    },
		    "bAutoWidth":false,
		    "bProcessing":true,
		    "bRetrieve":true,
		    "scrollX": true,
		    "aLengthMenu":[[10, 20, 50, -1], [10, 20, 50, "所有"]]
		};
	}
	return $("#"+table_id).DataTable(dataTableOpetion);
}

/**
 * 弹框
 * @returns
 */
function alertSuccess(message) {
	if(zeroModal) {
		zeroModal.success(message);
	} else {
		alert(message);
	}
}

/**
 * 弹框
 * @returns
 */
function alertError(message) {
	if(zeroModal) {
		zeroModal.error(message);
	} else {
		alert(message);
	}
}

/**
 * 确认框
 * @returns
 */
function confirm(message, function_success_handle) {
	if(zeroModal) {
		zeroModal.confirm(message, function_success_handle);
	} else {
		var r = confirm(message);
		if(r) {
			function_success_handle();
		}
	}
}

/**
 * 异步删除数据，并更新datatable列表
 * @param id  数据ID
 * @param url 删除URL
 */
function asyncDelete(id, url) {
	confirm("您确定要删除该数据么？", function() {
		var sendData = {"id": id};
		if(zeroModal) {
			zeroModal.loading(2);
		}
		ajaxPost(url, sendData, function(response) {
			if(zeroModal) {
				zeroModal.closeAll();
			}
	    	if (response.code == 0) {
	    		alertSuccess("删除成功");
	    		if(table) {
	    			//删除当前行
					table.row("#tr_"+id).remove().draw(false);
	    		}
			} else {
				if (response.msg) {
					alertError(response.msg);
				} else {
					alertError("删除失败");
				}
			}
	  	});
	});
}

/**
 * 延迟跳转页面，默认延迟一秒跳转
 * @param jumpPage
 * @returns
 */
function delayRedirectPage(jumpPage, time) {
	time = isTrue(time) ? time : 1000;
	setTimeout("redirectPage('"+ jumpPage +"')", time);
}

/**
 * 重定向跳转到指定页面
 * @param pageUrl 跳转页面URL
 */
function redirectPage(pageUrl) {
	if(pageUrl) {
		var browser = navigator.userAgent;
		console.log(browser);
		if(browser.indexOf("Chrome") != -1 || browser.indexOf("Firefox") != -1 ) {
			window.location.href = pageUrl;
		} else {
			pageUrl = pageUrl.substring(pageUrl.lastIndexOf("/") + 1, pageUrl.length);
			window.location.href = pageUrl;
		}
	}
}

/**
 * 根据字段获取字段在前端页面展示的名称<br>
 * 此方法应用时需要对表单中需要校验的字段的名称标签上添加 data-name标签，并赋值为field。以便能够通过字段扫描得到<br>
 * 字段展示标签必须为<label>或<span>
 * @param field
 * @returns
 */
function getFieldName(field) {
	var labels = $("body").find("label");
	var spans = $("body").find("span");
	var childrens = $.merge(labels, spans);
	for (var i = 0; i < childrens.length; i++) {
		if($(childrens[i]).attr("data-name") == field) {
			return $(childrens[i]).text();
		}
	}
	return "";
}

/**
 * 异步提交表单<br>
 * 1.返回处理结果会显示在 id="hint"的标签上。因此，在页面中要设置一个ID为hint的标签展示信息<br>
 * 2.当返回code=2时，表示表单上传字段验证失败，需要获取该字段展示名称，因此需要在页面字段名标签设置 data-name 标签，并在该标签中填写字段名称<br>
 * 3.其他异常处理，在id="hint"标签上展示信息<br>
 * 
 * @param form_id 要异步提交的表单ID
 * @param function_success_handle 请求处理成功之后的自定义处理回调函数，可缺省，若没有定义则仅弹框提示
 * @param function_validation_handle 字段校验失败之后的自定义处理回调函数，可缺省，若没有定义则仅弹框提示
 */
function ajaxSubmitForm(form_id, function_success_handle, function_validation_handle) {
	if(zeroModal) {
		zeroModal.loading(2);
	}
	$(form_id).ajaxSubmit(function(response){
		if(zeroModal) {
			zeroModal.closeAll();
		}
		// 请求参数验证失败
		if(response.code == -2) {
			if(function_validation_handle) {
				function_validation_handle(response);
			} else {
				alertError(getFieldName(response.data.field) + response.data.message);
			}
		}
		// 请求成功，code需要自行判断
		else {
			if(function_success_handle) {
				function_success_handle(response);
			} else if (response.code == 0) {
				alertSuccess(response.msg);
			} else {
				alertError(response.msg);
			}
		}
	});
}

/**
 * 异步POST提交组装好的对象信息（JSON格式）
 * 
 * @param url：请求URL
 * @param data：请求数据,JSON格式
 * @param function_success_handle 请求成功数据处理回调函数
 * @param function_validation_handle 字段校验失败之后的自定义处理回调函数，可缺省，若没有定义则仅弹框提示
 * @param function_error_handle 请求失败处理回调函数
 */
function ajaxPostObj(url, data, function_success_handle, function_validation_handle, function_error_handle) {
	ajax({
		url: url, 
		type: "POST", 
		data: data, 
		contentType: "application/json"
	},
	function(response) {
		if(zeroModal) {
			zeroModal.closeAll();
		}
		// 请求参数验证失败
		if(response.code == -2) {
			if(function_validation_handle) {
				function_validation_handle(response);
			} else {
				alertError(getFieldName(response.data.field) + response.data.message);
			}
		}
		// 请求成功，code需要自行判断
		else {
			if(function_success_handle) {
				function_success_handle(response);
			} else if (response.code == 0) {
				alertSuccess(response.msg);
			} else {
				alertError(response.msg);
			}
		}
	}, 
	function_error_handle);
}

/**
 * Ajax POST请求
 * 
 * @param url	请求的url
 * @param data	请求的数据
 * @param function_success	成功的函数
 * @param function_error	错误的函数
 */
function ajaxPost(url, data, function_success, function_error) {
	ajax({url: url, type: "POST", data: data}, function_success, function_error);
	
}

/**
 * Ajax Get请求
 * 
 * @param url	请求的url
 * @param data	请求的数据
 * @param function_success	成功的函数
 * @param function_error	错误的函数
 */
function ajaxGet(url, data, function_success, function_error) {
	ajax({url: url, type: "GET", data: data}, function_success, function_error);
}

/**
 * Ajax 请求
 * @param url	请求的url
 * @param type	请求的类型
 * @param async 是否是异步操作，true是，false不是
 * @param data	请求的数据
 * @param dataType	返回的数据类型
 * @param function_success	成功的函数
 * @param function_error	错误的函数
 */
function ajax(params, function_success, function_error) {
	if(!function_error) {
		function_error = function() {}
	}
	
	var type = params.type ? params.type : "POST";
	var url = params.url;
	var data = params.data;
	var async = params.async ? params.async : true;		// 默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false
	var contentType = params.contentType ? params.contentType : "application/x-www-form-urlencoded";
	
	$.ajax({
        type: type,
        url: url,
        data: data,
        async: async,
        cache: false,
        contentType: contentType,
        beforeSend: function(request) {
            request.setRequestHeader("ajax", "true");
        },
        success: function(data, status, xhr) {
        	var authUrl = xhr.getResponseHeader("AuthUrl");
        	if(authUrl != null) {
        		console.log("need login, url:%s", authUrl);
        		window.location.href = authUrl;
        		return;
        	}
        	function_success(data);
        },
        error: function (request, status, error) {
        	console.log("ajax query error status:%s error:%s", status, error);
        	function_error();
        }
    });
}

/**
 * 字符串为空、对象不存在、boolean值为false
 * @param o
 * @returns
 */
function isFalse (o) {
    if (!o || o === 'null' || o === 'undefined' || o === 'false' || o === 'NaN') return true
        return false
}

/**
 * 字符串不为空、对象存在、boolean值为true
 * @param o
 * @returns
 */
function isTrue (o) {
    return !this.isFalse(o)
}

/**
 * 时间转换函数
 * @param time
 * @returns {String}
 */
function timeToString(time) {
    var datetime = new Date();
    datetime.setTime(time);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
}

/**
 * 根据给定时间戳获取该时间戳相对当前时间已经过去多长时间，根据时长大小转换为
 * 分钟、小时、天、周、月等，多用于根据创建时间计算创建多长时间等需求
 * @param startTime 开始时间
 * @returns {String}
 */
function getDateDiff(startTime) {
	var now = new Date().getTime();
	return getStartEndTimeDiff(startTime, now);
}

/**
 * 根据给定的开始时间、结束时间计算两者之间相差的时间，根据时长大小转换为
 * 分钟、小时、天、周、月等，多用于根据创建时间计算创建多长时间等需求
 * @param startTime 开始时间
 * @param endTime 结束时间
 * @returns {String}
 */
function getStartEndTimeDiff(startTime, endTime) {
	var minute = 1000 * 60;
	var hour = minute * 60;
	var day = hour * 24;
	var halfamonth = day * 15;
	var month = day * 30;
	
	var diffValue = endTime - startTime;
	
	if (diffValue < 0) {
		return;
	}
	
	var monthC = diffValue / month;
	var weekC = diffValue / (7*day);
	var dayC = diffValue / day;
	var hourC = diffValue / hour;
	var minC = diffValue / minute;
	
	if(monthC >= 1){
		result = "" + parseInt(monthC) + "月";
	}
	else if(weekC >= 1){
		result = "" + parseInt(weekC) + "周";
	}
	else if(dayC >= 1){
		result = ""+ parseInt(dayC) + "天";
	}
	else if(hourC >= 1){
		result = "" + parseInt(hourC) + "小时";
	}
	else if(minC >= 1){
		result = "" + parseInt(minC) + "分钟";
	}
	else {
		result="小于1分钟";
	}
	return result;
}

/**
 * 是否为IOS操作系统
 * @returns
 */
function isIos () {
    var u = navigator.userAgent;
    //安卓手机 Android
    if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) {
        return false
    } 
    //苹果手机 iPhone
    else if (u.indexOf('iPhone') > -1) {
        return true
    } 
    //iPad 
    else if (u.indexOf('iPad') > -1) {
        return false
    } 
    //winphone手机 Windows Phone
    else if (u.indexOf('Windows Phone') > -1) {
        return false
    }
    else{
        return false
    }
}

/**
 * 是否为PC端
 * @returns
 */
function isPC () {
    var userAgentInfo = navigator.userAgent;
    var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
    var flag = true;
    for (var v = 0; v < Agents.length; v++) {
        if (userAgentInfo.indexOf(Agents[v]) > 0) {
            flag = false;
            break;
        }
    }
    return flag;
}

/**
 * 字符串判断，根据给定的字符串形式，返回该字符串是否符合该格式
 */
function checkStr (str, type) {
    switch (type) {
    	//手机号码
        case 'phone':
            return /^1[3|4|5|6|7|8|9][0-9]{9}$/.test(str);
        //座机
        case 'tel':
            return /^(0\d{2,3}-\d{7,8})(-\d{1,4})?$/.test(str);
        //身份证
        case 'card':
            return /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(str);
        //密码以字母开头，长度在6~18之间，只能包含字母、数字和下划线
        case 'pwd':
            return /^[a-zA-Z]\w{5,17}$/.test(str)
        //邮政编码
        case 'postal':
            return /[1-9]\d{5}(?!\d)/.test(str);
        //QQ号
        case 'QQ':
            return /^[1-9][0-9]{4,9}$/.test(str);
        //邮箱
        case 'email':
            return /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/.test(str);
        //金额(小数点2位)
        case 'money':
            return /^\d*(?:\.\d{0,2})?$/.test(str);
        //网址
        case 'URL':
            return /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/.test(str)
        //IP
        case 'IP':
            return /((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))/.test(str);
        //日期时间
        case 'date':
            return /^(\d{4})\-(\d{2})\-(\d{2}) (\d{2})(?:\:\d{2}|:(\d{2}):(\d{2}))$/.test(str) || /^(\d{4})\-(\d{2})\-(\d{2})$/.test(str)
        //数字
        case 'number':
            return /^[0-9]$/.test(str);
        //英文
        case 'english':
            return /^[a-zA-Z]+$/.test(str);
        //中文
        case 'chinese':
            return /^[\u4E00-\u9FA5]+$/.test(str);
        //小写
        case 'lower':
            return /^[a-z]+$/.test(str);
        //大写
        case 'upper':
            return /^[A-Z]+$/.test(str);
        //HTML标记
        case 'HTML':
            return /<("[^"]*"|'[^']*'|[^'">])*>/.test(str);
        default:
            return true;
    }
}

/**
 * 严格的身份证校验
 */
function isCardID(sId) {
    if (!/(^\d{15}$)|(^\d{17}(\d|X|x)$)/.test(sId)) {
        sys.log('你输入的身份证长度或格式错误.. CardID:' + sId);
        return false
    }
    //身份证城市
    var aCity={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"};
    if(!aCity[parseInt(sId.substr(0,2))]) { 
    	sys.log('你的身份证地区非法.. CardID:' + sId)
        return false
    }

    // 出生日期验证
    var sBirthday=(sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2))).replace(/-/g,"/"),
        d = new Date(sBirthday)
    if(sBirthday != (d.getFullYear()+"/"+ (d.getMonth()+1) + "/" + d.getDate())) {
    	sys.log('身份证上的出生日期非法.. CardID:' + sId)
        return false
    }

    // 身份证号码校验
    var sum = 0,
        weights =  [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2],
        codes = "10X98765432"
    for (var i = 0; i < sId.length - 1; i++) {
        sum += sId[i] * weights[i];
    }
    var last = codes[sum % 11]; //计算出来的最后一位身份证号码
    if (sId[sId.length-1] != last) { 
    	sys.log('你输入的身份证号非法.. CardID:' + sId)
        return false
    }
    return true
}

/**
 * 获取某月有多少天
 * @param time
 * @returns
 */
function getMonthOfDay (time) {
    var date = new Date(time)
    var year = date.getFullYear()
    var mouth = date.getMonth() + 1
    var days

    //当月份为二月时，根据闰年还是非闰年判断天数
    if (mouth == 2) {
        days = (year%4==0 && year%100==0 && year%400==0) || (year%4==0 && year%100!=0) ? 28 : 29
    } else if (mouth == 1 || mouth == 3 || mouth == 5 || mouth == 7 || mouth == 8 || mouth == 10 || mouth == 12) {
        //月份为：1,3,5,7,8,10,12 时，为大月.则天数为31；
        days = 31
    } else {
        //其他月份，天数为：30.
        days = 30
    }
    return days
}

/**
 * 获取某年有多少天
 */
function getYearOfDay (time) {
    var firstDayYear = this.getFirstDayOfYear(time);
    var lastDayYear = this.getLastDayOfYear(time);
    var numSecond = (new Date(lastDayYear).getTime() - new Date(firstDayYear).getTime())/1000;
    return Math.ceil(numSecond/(24*3600));
}

/**
 * 判断一个元素是否在数组中
 */
function contains (arr, val) {
    return arr.indexOf(val) != -1 ? true : false;
}

/**
 * @param  {arr} 数组
 * @param  {type} 1：从小到大   2：从大到小   3：随机
 * @return {Array}
 */
function sort (arr, type = 1) {
    return arr.sort( (a, b) => {
        switch(type) {
            case 1:
                return a - b;
            case 2:
                return b - a;
            case 3:
                return Math.random() - 0.5;
            default:
                return arr;
        }
    })
}

/**
 * 求两个集合的并集
 * @param a
 * @param b
 * @returns
 */
function union (a, b) {
    var newArr = a.concat(b);
    return this.unique(newArr);
}

/**
 * 求两个集合的交集
 * @param a
 * @param b
 * @returns
 */
function intersect (a, b) {
    var _this = this;
    a = this.unique(a);
    return this.map(a, function(o) {
        return _this.contains(b, o) ? o : null;
    });
}

/**
 * 删除其中一个元素
 * @param arr
 * @param ele
 * @returns
 */
function remove (arr, ele) {
    var index = arr.indexOf(ele);
    if(index > -1) {
        arr.splice(index, 1);
    }
    return arr;
}

/**
 * 获取数组中最大值
 */
function max (arr) {
    return Math.max.apply(null, arr);
}

/**
 * 获取数组中最小值
 */
function min (arr) {
    return Math.min.apply(null, arr);
}

/**
 * 求和
 */
function sum (arr) {
    return arr.reduce( (pre, cur) => {
        return pre + cur
    })
}

/**
 * 平均值
 */
function average (arr) {
    return this.sum(arr)/arr.length
}

/**
 * @param  {str} 
 * @param  {type}
 *       type:  1:首字母大写  2：首页母小写  3：大小写转换  4：全部大写  5：全部小写
 * @return {String}
 */
function changeCase (str, type) {
    type = type || 4
    switch (type) {
        case 1:
            return str.replace(/\b\w+\b/g, function (word) {
                return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();

            });
        case 2:
            return str.replace(/\b\w+\b/g, function (word) {
                return word.substring(0, 1).toLowerCase() + word.substring(1).toUpperCase();
            });
        case 3:
            return str.split('').map( function(word){
                if (/[a-z]/.test(word)) {
                    return word.toUpperCase();
                }else{
                    return word.toLowerCase()
                }
            }).join('')
        case 4:
            return str.toUpperCase();
        case 5:
            return str.toLowerCase();
        default:
            return str;
    }
}

/**
 * 检测密码强度
 */
function checkPwd (str) {
	var Lv = 0;
	if (str.length < 6) {
	    return Lv
	}
	if (/[0-9]/.test(str)) {
	    Lv++
	}
	if (/[a-z]/.test(str)) {
	    Lv++
	}
	if (/[A-Z]/.test(str)) {
	    Lv++
	}
	if (/[\.|-|_]/.test(str)) {
	    Lv++
	}
	return Lv;
}

/**
 * 获取指定范围内的随机数
 */
function random (min, max) {
    if (arguments.length === 2) {
        return Math.floor(min + Math.random() * ( (max+1) - min ))
    }else{
        return null;
    }
    
}

/**
 * 将阿拉伯数字翻译成中文的大写数字
 */
function numberToChinese (num) {
    var AA = new Array("零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十");
    var BB = new Array("", "十", "百", "仟", "萬", "億", "点", "");
    var a = ("" + num).replace(/(^0*)/g, "").split("."),
        k = 0,
        re = "";
    for(var i = a[0].length - 1; i >= 0; i--) {
        switch(k) {
            case 0:
                re = BB[7] + re;
                break;
            case 4:
                if(!new RegExp("0{4}//d{" + (a[0].length - i - 1) + "}$")
                    .test(a[0]))
                    re = BB[4] + re;
                break;
            case 8:
                re = BB[5] + re;
                BB[7] = BB[5];
                k = 0;
                break;
        }
        if(k % 4 == 2 && a[0].charAt(i + 2) != 0 && a[0].charAt(i + 1) == 0)
            re = AA[0] + re;
        if(a[0].charAt(i) != 0)
            re = AA[a[0].charAt(i)] + BB[k % 4] + re;
        k++;
    }

    if(a.length > 1) // 加上小数部分(如果有小数部分)
    {
        re += BB[6];
        for(var i = 0; i < a[1].length; i++)
            re += AA[a[1].charAt(i)];
    }
    if(re == '一十')
        re = "十";
    if(re.match(/^一/) && re.length == 3)
        re = re.replace("一", "");
    return re;
}

class StorageFn {
    constructor () {
        this.ls = window.localStorage;
        this.ss = window.sessionStorage;
    }

    /*-----------------cookie---------------------*/
    /*设置cookie*/
    setCookie (name, value, day) {
        var setting = arguments[0];
        if (Object.prototype.toString.call(setting).slice(8, -1) === 'Object'){
            for (var i in setting) {
                var oDate = new Date();
                oDate.setDate(oDate.getDate() + day);
                document.cookie = i + '=' + setting[i] + ';expires=' + oDate;
            }
        }else{
            var oDate = new Date();
            oDate.setDate(oDate.getDate() + day);
            document.cookie = name + '=' + value + ';expires=' + oDate;
        }
        
    }

    /*获取cookie*/
    getCookie (name) {
        var arr = document.cookie.split('; ');
        for (var i = 0; i < arr.length; i++) {
            var arr2 = arr[i].split('=');
            if (arr2[0] == name) {
                return arr2[1];
            }
        }
        return '';
    }

    /*删除cookie*/
    removeCookie (name) {
        this.setCookie(name, 1, -1);
    }


    /*-----------------localStorage---------------------*/
    /*设置localStorage*/
    setLocal(key, val) {
        var setting = arguments[0];
        if (Object.prototype.toString.call(setting).slice(8, -1) === 'Object'){
            for(var i in setting){
                this.ls.setItem(i, JSON.stringify(setting[i]))
            }
        }else{
            this.ls.setItem(key, JSON.stringify(val))
        }
        
    }

    /*获取localStorage*/
    getLocal(key) {
        if (key) return JSON.parse(this.ls.getItem(key))
        return null;
        
    }

    /*移除localStorage*/
    removeLocal(key) {
        this.ls.removeItem(key)
    }

    /*移除所有localStorage*/
    clearLocal() {
        this.ls.clear()
    }


    /*-----------------sessionStorage---------------------*/
    /*设置sessionStorage*/
    setSession(key, val) {
        var setting = arguments[0];
        if (Object.prototype.toString.call(setting).slice(8, -1) === 'Object'){
            for(var i in setting){
                this.ss.setItem(i, JSON.stringify(setting[i]))
            }
        }else{
            this.ss.setItem(key, JSON.stringify(val))
        }
        
    }

    /*获取sessionStorage*/
    getSession(key) {
        if (key) return JSON.parse(this.ss.getItem(key))
        return null;
        
    }

    /*移除sessionStorage*/
    removeSession(key) {
        this.ss.removeItem(key)
    }

    /*移除所有sessionStorage*/
    clearSession() {
        this.ss.clear()
    }
}

/**
 * 自定义log
 */
var customLogSwitch=true;
var sys={
	log:function(msg){
		if(customLogSwitch){
			 console.log(msg);
		}
    }
}
