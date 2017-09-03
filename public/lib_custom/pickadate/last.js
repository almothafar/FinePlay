'use strict';

/* default.css */

insertStyle('.picker__input.picker__input--active', {
	'border-color': getTheme().primary.backgroundColor
});

insertStyle('.picker__day--highlighted, .picker__select--month:focus, .picker__select--year:focus', {
	'border-color': getTheme().primary.backgroundColor
});
insertStyle('.picker__nav--next:hover, .picker__nav--prev:hover', {
	'background': getTheme().primary.disabledBackgroundColor
});
insertStyle('.picker--focused .picker__day--highlighted, .picker__day--highlighted:hover, .picker__day--infocus:hover, .picker__day--outfocus:hover', {
	'background': getTheme().primary.disabledBackgroundColor
});
insertStyle('.picker__day--today:before', {
	'border-top': getTheme().primary.activeBackgroundColor
});
insertStyle('.picker--focused .picker__day--selected, .picker__day--selected, .picker__day--selected:hover', {
	'background': getTheme().primary.backgroundColor
});
insertStyle('.picker__button--clear:hover, .picker__button--close:hover, .picker__button--today:hover', {
	'background': getTheme().primary.disabledBackgroundColor,
	'border-bottom-color': getTheme().primary.disabledBackgroundColor
});
insertStyle('.picker__button--clear:focus, .picker__button--close:focus, .picker__button--today:focus', {
	'background': getTheme().primary.disabledBackgroundColor,
	'border-color': getTheme().primary.backgroundColor
});
insertStyle('.picker__button--today:before', {
	'border-top': getTheme().primary.activeBackgroundColor
});

/* default.time.css */

insertStyle('.picker--focused .picker__list-item--highlighted, .picker__list-item--highlighted:hover, .picker__list-item:hover', {
	'background': getTheme().primary.disabledBackgroundColor
});
insertStyle('.picker__list-item--highlighted, .picker__list-item:hover', {
	'border-color': getTheme().primary.backgroundColor
});
insertStyle('.picker--focused .picker__list-item--selected, .picker__list-item--selected, .picker__list-item--selected:hover', {
	'background': getTheme().primary.backgroundColor
});

var pickadateMessages = function(messageKey){

	return $("#pickadate_messages").data('messages')[messageKey];
}
// provisional coped for pickadate 3.5.6
jQuery.fn.pickadate.defaults.close = pickadateMessages(MessageKeys.CLOSE);
jQuery.extend( jQuery.fn.pickadate.defaults, jQuery.fn.pickadate.defaults);