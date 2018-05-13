/**
 * 设置日历插件
 */
function dateRangePicker() {
	$('#reportrange').daterangepicker({
		maxDate : moment(), 			//最大时间
		showDropdowns : true,
		showWeekNumbers : false, 		//是否显示第几周
		timePicker : false, 				//是否显示小时和分钟
		timePickerIncrement : 60, 		//时间的增量，单位为分钟
		timePicker12Hour : false, 		//是否使用12小时制来显示时间
		ranges : {
			'今日' : [ moment().startOf('day'), moment() ],
			'昨日' : [ moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day') ],
			'最近7日' : [ moment().subtract('days', 6), moment() ],
			'最近30日' : [ moment().subtract('days', 29), moment() ]
		},
		opens : 'right', 				//日期选择框的弹出位置
		buttonClasses : [ 'btn btn-default' ],
		applyClass : 'btn-small btn-primary blue',
		cancelClass : 'btn-small',
		format : 'YYYY-MM-DD', 			//控件中from和to 显示的日期格式  YYYY-MM-DD HH:mm:ss
		separator : ' to ',
		locale : {
			applyLabel : '确定',
			cancelLabel : '取消',
			fromLabel : '起始时间',
			toLabel : '结束时间',
			customRangeLabel : '自定义',
			daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
			monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
			firstDay : 1
		}
	}, function(start, end, label) {//格式化日期显示框
		$('#reportrange').html(start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
	});
}