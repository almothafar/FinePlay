'use strict';

var getData = (function () {

	var data = JSON.parse($('#rowsJson').text());

	return function () {

		var page = parseInt(window.location.hash.replace('#', ''), 10) || 0,
			limit = Messages("pageSize"),
			row	 = page * limit,
			count = (page + 1) * limit,
			part = [];

		for (;row < count;row++) {
			part.push(data[row]);
		}

		return part;
	}
})();

var container = document.getElementById('hot_container');
var hot = new Handsontable(container, {

	data: getData(),
	rowHeaders: true,
	colHeaders: true,
	dropdownMenu: true,
	contextMenu: true
});

var redraw = function(){

	hot.updateSettings({

		height: $("#hot_container .htCore").height()
	});
}

$('#hot_pagination>.pagination>.page-item>a[href^=\"#\"].page-link').on('click', function() {

	var currentPage = parseInt(window.location.hash.replace('#', ''), 10) || 0;

	var href = $(this).attr("href");
	var targetPage;
	var target = href.replace('#', '')
	switch (target){
		case 'p':

			targetPage = currentPage - 1;
			break;
		case 'n':

			targetPage = currentPage + 1;
			break;
		default:

			targetPage = parseInt(target, 10) || 0;
			break;
	}

	var visibleCount = 3;
	var visibleIndexs = [];

	visibleIndexs.push(targetPage);
	do {

		if (visibleIndexs.length < visibleCount) {

			var last = visibleIndexs[visibleIndexs.length - 1];
			if (last < Messages("pageCount") - 1) {

				var next = last + 1;
				visibleIndexs.push(next);
			}
		}

		if (visibleIndexs.length < visibleCount) {

			var head = visibleIndexs[0];
			if (0 < head) {

				var prev = head - 1;
				visibleIndexs.unshift(prev);
			}
		}
	} while (visibleIndexs.length < visibleCount);

	var isEnabledFirst = 0 < targetPage;
	var isEnabledPrevious =  0 < targetPage;
	var isEnabledNext = targetPage < Messages("pageCount") - 1;
	var isEnabledLast = targetPage < Messages("pageCount") - 1;

	var pageItems = $('#hot_pagination>.pagination>.page-item');
	isEnabledFirst ? pageItems.eq(0).removeClass('disabled') : pageItems.eq(0).addClass('disabled');
	isEnabledPrevious ? pageItems.eq(1).removeClass('disabled') : pageItems.eq(1).addClass('disabled');
	for(var i = 2;i < pageItems.length -2;i++){
		var pageItem = pageItems.eq(i);
		visibleIndexs.indexOf(i-2) != -1 ? pageItem.show() : pageItem.hide();
		targetPage == (i-2) ? pageItem.addClass('active') : pageItem.removeClass('active');
	}
	isEnabledNext ? pageItems.eq(pageItems.length - 2).removeClass('disabled') : pageItems.eq(pageItems.length - 2).addClass('disabled');
	isEnabledLast ? pageItems.eq(pageItems.length - 1).removeClass('disabled') : pageItems.eq(pageItems.length - 1).addClass('disabled');

	window.location.hash = targetPage;

	return false;
});

Handsontable.dom.addEvent(window, 'hashchange', function (event) {

	hot.loadData(getData());
	redraw();
});

$(window).resize(function(){

	redraw();
});

$('#hot_pagination>.pagination>.page-item>a[href^=\"#\"].page-link').eq(0).trigger('click');
