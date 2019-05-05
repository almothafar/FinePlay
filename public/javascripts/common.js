'use strict';

if (!window.console){

	window.console = {
		log: function(){},
		info: function(){},
		warn: function(){},
		error: function(){},
		trace: function(){},
		assert: function(){},
		count: function(){},
		clear: function(){},
		debug: function(){},
		dir: function(){},
		dirxml: function(){},
		table: function(){},
		profile: function(){},
		profileEnd: function(){},
		time: function(){},
		timeEnd: function(){},
		timeStamp: function(){},
		group: function(){},
		groupCollapsed: function(){},
		groupEnd: function(){},
		takeHeapSnapshot: function(){},
	};
}

console.time('welcome');

console.log("%c", "background: url(http://localhost:9000/assets/images/favicon.png); background-size: 100% 100%; padding: 32px 32px");
console.log("");

console.log("Welcome to %cfine%c✿%cplay!", "color:black;font-size:24px;","color:pink;font-size:26px;","color:black;font-size:24px;");

console.groupCollapsed('Script log');
	console.trace();
	var info = {URL:document.URL, Title:document.title, ContentType:document.contentType, Charset:document.charset, Browser:navigator.userAgent};
	if(console.table){console.table(info);};
	var obj = new Date().toString();
	console.log("Log", obj);
//	console.error("Error", obj);
//	console.warn("Warn", obj);
//	console.info("Info", obj);
//	debugger;
console.groupEnd('Script log');

console.timeEnd('welcome');

var shake = function(selector, func){

	$(selector).effect('shake', {}, 200, func);
}
var showFromLeft = function(selector, func){

	$(selector).css({'display':'none'}).removeClass("d-none");
	$(selector).effect( 'drop', {'mode':'show','direction':'left'}, 400, func);
}

var hideToLeft = function(selector, func){

	$(selector).effect( 'drop', {'mode':'hide','direction':'left'}, 400, func);
}

var showFromRight = function(selector, func){

	$(selector).css({'display':'none'}).removeClass("d-none");
	$(selector).effect( 'drop', {'mode':'show','direction':'right'}, 400, func);
}

var hideToRight = function(selector, func){

	$(selector).effect( 'drop', {'mode':'hide','direction':'right'}, 400, func);
}

var showFromStart = function(selector, func){

	if(!$('html').hasClass('dir-rtl')){

		showFromLeft(selector, func);
	}else{

		showFromRight(selector, func);
	}
}

var hideToStart = function(selector, func){

	if(!$('html').hasClass('dir-rtl')){

		hideToLeft(selector, func);
	}else{

		hideToRight(selector, func);
	}
}

var showFromEnd = function(selector, func){

	if(!$('html').hasClass('dir-rtl')){

		showFromRight(selector, func);
	}else{

		showFromLeft(selector, func);
	}
}

var hideToEnd = function(selector, func){

	if(!$('html').hasClass('dir-rtl')){

		hideToRight(selector, func);
	}else{

		hideToLeft(selector, func);
	}
}

var showFromUp = function(selector, func){

	$(selector).css({'display':'none'}).removeClass("d-none");
	$(selector).effect( 'drop', {'mode':'show','direction':'up'}, 400, func);
}

var hideToUp = function(selector, func){

	$(selector).effect( 'drop', {'mode':'hide','direction':'up'}, 400, func);
}

var showFromDown = function(selector, func){

	$(selector).css({'display':'none'}).removeClass("d-none");
	$(selector).effect( 'drop', {'mode':'show','direction':'down'}, 400, func);
}

var hideToDown = function(selector, func){

	$(selector).effect( 'drop', {'mode':'hide','direction':'down'}, 400, func);
}

var fadeInFromFront = function(selector, func){

	var target= $(selector);

	if(target.css('transition') && target.css('transform') && target.css('opacity')){

		target.animate({
			'opacity':'0'
		}, 0, function(){

			$(this).css({
				'transform':'scale(2,2)',
				'transition':'0.001s'
			}).one('transitionend', function(){

				$(this).css({
					'transform':'initial',
					'opacity':'1',
					'transition':'0.2s ease-out'
				}).one('transitionend', func)
			});
		});
	}else{

		if(func){

			func();
		}

		return;
	}
}

var fadeOutToFront = function(selector, func){

	var target= $(selector);

	if(target.css('transform') && target.css('transition') && target.css('opacity')){

		target.css({
			'transform':'scale(2,2)',
			'opacity':'0',
			'transition':'0.2s ease-in'
		}).one('transitionend', func);
	}else{

		if(func){

			func();
		}

		return;
	}
}

var tellAlert = function(type, text, wait){

	var icon ='';
	switch (type) {
	case 'success':

		icon = 'check';
		break;
	case 'info':

		icon = 'info-circle';
		break;
	case 'warning':

		icon = 'exclamation-triangle';
		break;
	case 'danger':

		icon = 'ban';
		break;
	default:

		break;
	}
	var html = '<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
					'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
					'<i class="fas fa-' + icon +'"></i>' +
					'\t' + text +
				'</div>';

	tell(html, wait);
}

var tell = function(text, wait){

	var isHtml = /<(".*?"|'.*?'|[^'"])*?>/.test(text);
	var html;
	if(isHtml){

		html = text;
	}else{

		html = '<div class="alert alert-secondary alert-dismissible fade show" role="alert">' +
					'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>' +
					'<i class="fas fa-bullhorn"></i>' +
					'\t' + text +
				'</div>';
	}

	var element = $('<div class="mb-3">' + html + '</div>');
	var elementId = new Date().getTime() + Math.floor( Math.random()*1000 );
	element.attr('id', elementId);

	var selector = '#'+element.attr('id');

	if(0 == $('#system_alerts').length){

		throw new Error("Not exist #system_alerts.");
	}

	$('#system_alerts').prepend(element);

	var rtlLeft = element.position().left - $('#system_menu').position().left;
	$(selector).effect( 'bounce', {}, 500);
	element.css({left: rtlLeft});

	if(wait == null){
		// null or undefined

		wait = -1;
	}

	if(-1 == wait){

		return;
	}

	var expire = function(){

		$(selector).fadeOut( "default", function(){

			$(selector).remove();
		});
	};

	setTimeout(function(){

		expire();
	},wait);
};

var notifyAlert = function(type, text, wait){

	var icon ='';
	var notifyInfo = {'icon': 'comment-alt', 'text': '', 'bg': ''};
	switch (type) {
	case 'success':

		notifyInfo = {'icon': 'check', 'text': 'light', 'bg': 'success'};
		break;
	case 'info':

		notifyInfo = {'icon': 'info-circle', 'text': 'light', 'bg': 'info'};
		break;
	case 'warning':

		notifyInfo = {'icon': 'exclamation-triangle', 'text': 'light', 'bg': 'warning'};
		break;
	case 'danger':

		notifyInfo = {'icon': 'ban', 'text': 'light', 'bg': 'danger'};
		break;
	default:

		break;
	}
	var html = '<div class="toast show mw-100" role="alert" aria-live="assertive" aria-atomic="true" data-autohide="false">' +
					'<div class="toast-header bg-' + notifyInfo.bg + '">' +
						'<i class="fas fa-' + notifyInfo.icon + ' text-' + notifyInfo.text + '"></i>' +
						'<strong class="mr-auto text-' + notifyInfo.text + '"></strong>' +
						'<small></small>' +
						'<button type="button" class="ml-2 mb-1 close text-' + notifyInfo.text + '" data-dismiss="toast" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
					'</div>' +
					'<div class="toast-body">' +
						text +
					'</div>' +
				'</div>';

	notify(html, wait);
}

var notify = function(text, wait){

	var isHtml = /<(".*?"|'.*?'|[^'"])*?>/.test(text);
	var html;
	if(isHtml){

		html = text;
	}else{

		var notifyInfo = {'icon': 'comment-alt', 'bg': ''};
		html = '<div class="toast show mw-100" role="alert" aria-live="assertive" aria-atomic="true" data-autohide="false">' +
					'<div class="toast-header bg-' + notifyInfo.bg + '">' +
						'<i class="fas fa-' + notifyInfo.icon + '"></i>' +
						'<strong class="mr-auto"></strong>' +
						'<small></small>' +
						'<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close"><span aria-hidden="true">&times;</span></button>' +
					'</div>' +
					'<div class="toast-body">' +
						text +
					'</div>' +
				'</div>';
	}

	var element = $('<div class="m-3">' + html + '</div>');
	element.children().addClass('shadow');
	var toast = element.find('.toast');
	if(1 <= toast.length){

		toast.find('button[data-dismiss="toast"]').on('click', function(){toast.remove()});
	}
	var elementId = new Date().getTime() + Math.floor( Math.random()*1000 );
	element.attr('id', elementId);

	var selector = '#'+element.attr('id');

	element.hide();
	$('#system_notifications').prepend(element);

	showFromEnd(selector);

	if(wait == null){
		// null or undefined

		wait = 7000;
	}

	if(-1 == wait){

		return;
	}

	var expire = function(){

		hideToEnd(selector, function(){

			$(selector).remove();
		});
	};

	setTimeout(function(){

		expire();
	},wait);
};

var showCover = function(func){

	showFromUp('#system_cover', function(){

		if(func){

			func();
		}
	});
}

var hideCover = function(func){

	hideToUp('#system_cover', function(event){

		if(func){

			func();
		}
	});
}

var showMagnifyText = function(text){

	$('#system_magnify-text #system_magnify-text-content').empty().text(text);
	$('#system_magnify-text').fadeIn('fast');
}

var hideMagnifyText = function(){

	$('#system_magnify-text').fadeOut(400);
}

var scrollContent = function(position){

	$('#system_content').animate({ scrollTop: position }, 400);
}

var menuHeight = function(){

	return $('#system_menu').outerHeight()
}

var extensionMenuHeight = function(){

	return $('#system_extension-menu').outerHeight()
}

var footerHeight = function(){

	return $('#system_footer').outerHeight()
}

var getContent = function() {

	return $("#system_content");
}

var enableHelp = function() {

	$("#helpButton").removeClass("d-none");
}

var enablePrint = function() {

	$("#printButton").removeClass("d-none");
}

var isExpand = function() {

	return $("#system_base").hasClass('system_expand') || $("#system_base").hasClass('system_help-expand');
}

var ready = function(selector, func){

	var timerId = setInterval(function() {

		if ($(selector)[0]) {

			clearInterval(timerId);

			if(func){

				func();
			}

			return;
		}
	}, 100);
}
