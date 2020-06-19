'use strict';

var printPart = function(selector){

	$('#printSheet').remove();
	$('#system_base').addClass('d-print-none');

	var printSheet = $('<div></div>', {
		id: 'printSheet',
		class: 'd-none d-print-block',
		css: {
			width: '100%',
			position: 'absolute',
			backgroundColor: 'white',
			zIndex: 10000
		}
	});

	var printArea = $(selector).clone();
	printArea.attr('id', printArea.attr('id')+'Area');

	printSheet.append(printArea);
	// It exists until the next printing time.
	$('body').append(printSheet);

	window.print();
}

var offsetTopFromTarget = function(targetSelector, selector){

	var isBaseElement = function(element){

		var position = $(element).css('position');
		return position == 'relative'||position == 'absolute'||position == 'fixed';
	}
	var elements = $(selector).parentsUntil(targetSelector).get().reverse();
	elements.push($(selector)[0]);

	var y = 0;
	var targetIsBase = isBaseElement($(targetSelector)[0]);
	if(!targetIsBase){

		y = $(targetSelector)[0].offsetTop
	}
	var parentIsBase = targetIsBase;
	$.each(elements, function(){

		var positionTop = this.offsetTop;
		if(parentIsBase){

			y = y + positionTop;
			parentIsBase = false;
		}else{

			y = y + (positionTop - y);
		}

		parentIsBase = isBaseElement(this);
	});

	return y;
}

var extendMenu = function(selector){

	$(selector).appendTo($('#system_extension-menu'));
	refreshMenu();
	refreshExtensionMenu();
	refreshContent();
}

var extendSurface = function(selector){

	$(selector).appendTo($('#system_surface'));
}


var hideFooter = function(func){

	$('#system_footer').addClass('d-none');
}

var setFontSize = function(size){

	// There are many places that don't change.
	$('html').css('font-size', size + 'px');
	refreshMenu();
	refreshExtensionMenu();
	refreshContent();
}

var refreshMenu = function(){

	if(isStickySupported){

		$('#system_menu').css('top', 0 + 'px');
	}else{
		console.log('polyfill for IE.');

		$('#system_menu').removeClass('sticky-top').css('position', 'absolute');
		var refreshMenuPosition = function(){

			var contentPosition = $("#system_content-left-corner").position().top;
			$('#system_menu').css('top', 0 - contentPosition + 'px');
		}
		refreshMenuPosition();
		$('#system_content').scroll(function(){

			refreshMenuPosition();
		});
	}
}

var refreshExtensionMenu = function(){

	if(isStickySupported){

		$('#system_extension-menu').css('top', menuHeight() + 'px');
	}else{
		console.log('polyfill for IE.');

		$('#system_extension-menu').removeClass('sticky-top').css('position', 'absolute');
		var refreshExtensionMenuPosition = function(){

			var contentPosition = $("#system_content-left-corner").position().top;
			$('#system_extension-menu').css('top', menuHeight() - contentPosition + 'px');
		}
		refreshExtensionMenuPosition();
		$('#system_content').scroll(function(){

			refreshExtensionMenuPosition();
		});
	}
}

var refreshContent = function(){

	if(isStickySupported){

	}else{
		console.log('polyfill for IE.');

		$('#system_content').css('padding-top', menuHeight() + extensionMenuHeight() + 'px');
	}
}

var getTheme = function(){

	var theme = {
		"backgroundColor": (function(){
			var color = $.Color($("#system_theme>#system_theme-normal").css("background-color"));
			return color.toRgbaString();
		})(),
		"borderRadius": (function(){
			var elem = $("#system_theme>#system_theme-primary>.system_theme-primary-normal");
			return 0 != elem.css("border-radius").length ? elem.css("border-radius") : elem.css("border-top-left-radius");
		})(),
		"borderRadiusSmall": (function(){
			var elem = $("#system_theme>#system_theme-primary>.system_theme-primary-normal-sm");
			return 0 != elem.css("border-radius").length ? elem.css("border-radius") : elem.css("border-top-left-radius");
		})(),
		"borderRadiusLarge": (function(){
			var elem = $("#system_theme>#system_theme-primary>.system_theme-primary-normal-lg");
			return 0 != elem.css("border-radius").length ? elem.css("border-radius") : elem.css("border-top-left-radius");
		})(),
		"primary":{
			"color": (function(){
				var color = $.Color($("#system_theme>#system_theme-primary>.system_theme-primary-normal").css("color"));
				return color.toRgbaString();
			})(),
			"backgroundColor": (function(){
				var color = $.Color($("#system_theme>#system_theme-primary>.system_theme-primary-normal").css("background-color"));
				return color.toRgbaString();
			})(),
			"backgroundColorActive": (function(){
				var color = $.Color($("#system_theme>#system_theme-primary>.system_theme-primary-active").css("background-color"));
				return color.toRgbaString();
			})(),
			"disabledBackgroundColor": (function(){
				var color = $.Color($("#system_theme>#system_theme-primary>.system_theme-primary-disabled").css("background-color"));
				var alpha = $('.system_theme-primary-disabled').css('opacity');
				var disabledBackgroundColor = $.Color(color.red(), color.green(), color.blue(), alpha);
				return disabledBackgroundColor.toRgbaString();
			})(),
		}
	}

	return theme;
}

var getMode = function(){

	return $("#system_mode").data('mode');
}

var getToken = function(){

	return $("#system_token").data('token');
}

var messages = function(messageKey){

	return $("#system_messages").data('messages')[messageKey];
}

var flash = function(){

	$('html').fadeOut(300).fadeIn(300);
}

$(document).ready(function() {

	// provisional coped for Bug? of Mobile Safari
	$('#system_content').eq(0)[0].style.webkitOverflowScrolling='touch';
});

$('.modal').on('shown.bs.modal', function (e) {

	$('#system_base').css('overflow','visible');
	$("#system_base").width('99.99999%');
}).on('hidden.bs.modal', function (e) {

	$('#system_base').css('overflow','hidden');
	$("#system_base").width('100%');
});