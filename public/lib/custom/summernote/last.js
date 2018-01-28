'use strict';

var summernoteMessages = function(messageKey) {

	return $("#summernote_messages").data('messages')[messageKey];
}

var lang = summernoteMessages(MessageKeys.SUMMERNOTE_LANG)
if ('' == lang) {
} else {

	$.summernote.options.lang = lang;
}

(function() {

	var copyStyle = function(elem, copyTarget) {

		if ($(elem).attr(copyTarget)) {

			elem.style.cssText = $(elem).attr(copyTarget);
		}
	}

	var orignHtmlFunc = $.fn.html;
	$.fn.html = $.extend(function() {

		var copyTarget;
		var result;
		if (arguments[0]) {

			var copyTarget = 'inline-style';
			var replacedValue = arguments[0].replace(/style/g, copyTarget);
			result = orignHtmlFunc.call(this, replacedValue);
		} else {

			// default of jQuery
			var copyTarget = 'style';
			result = orignHtmlFunc.apply(this, arguments);
		}

		if (//
			$(this).hasClass('dropdown-menu') || //
			$(this).hasClass('note-color-palette') || //
			$(this).hasClass('note-editable')) {

			$(this).find('*').each(function() {

				copyStyle(this, copyTarget);
			});
		}

		return result;
	}, orignHtmlFunc)
})();
