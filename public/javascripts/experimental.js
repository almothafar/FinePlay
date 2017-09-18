'use strict';

var printPart = function(selector){

	var printSheet = $("<div></div>", {
		id:'printSheet',
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
	$('body').append(printSheet);

	$('#system_base').addClass('d-print-none');

	var endPrint = function(){

		$('#system_base').removeClass('d-print-none');
		printSheet.remove();
	}

	ready('#system_base.d-print-none', function(){

		var start = new Date().getTime();
		window.print();
		var end = new Date().getTime();

		var isBlockingDialog = 500 < (end - start);
		if(isBlockingDialog){

			endPrint();
		}else{

			// for iOS Non bloking dialog.
			setTimeout(function(){

				endPrint();
			}, 2000);
		}
	});
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

	var getMenuBarHeight = function() {

		return $('#system_menu')[0].offsetHeight;
	}

	var getExtensionMenuHeight = function() {

		return $('#system_extension-menu')[0].offsetHeight;
	}

	getContent().css('top', getMenuBarHeight() + getExtensionMenuHeight() + 'px');
}

var showUsers = function(){

	showFromDown('#system_users');
	$('#system_base').addClass('blur');
}

var hideUsers = function(){

	hideToDown('#system_users', function(){

		$('#system_base').removeClass('blur');
	});
}

var getTheme = function(){

	var theme = {
		"backgroundColor": (function(){
			var color = $.Color($("#system_theme>#system_theme-normal").css("background-color"));
			return color.toRgbaString();
		})(),
		"borderRadius": (function(){
			return $("#system_theme>#system_theme-primary>.system_theme-primary-normal").css("border-radius");
		})(),
		"borderRadiusSmall": (function(){
			return $("#system_theme>#system_theme-primary>.system_theme-primary-normal-sm").css("border-radius");
		})(),
		"borderRadiusLarge": (function(){
			return $("#system_theme>#system_theme-primary>.system_theme-primary-normal-lg").css("border-radius");
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

var Messages = function(messageKey){

	return $("#system_messages").data('messages')[messageKey];
}
