'use strict';

/* default.css */

injectStyle('.picker__input.picker__input--active', {
	'border-color': getTheme().primary.backgroundColor
});

injectStyle('.picker__day--highlighted, .picker__select--month:focus, .picker__select--year:focus', {
	'border-color': getTheme().primary.backgroundColor
});
injectStyle('.picker__nav--next:hover, .picker__nav--prev:hover', {
	'background': getTheme().primary.disabledBackgroundColor
});
injectStyle('.picker--focused .picker__day--highlighted, .picker__day--highlighted:hover, .picker__day--infocus:hover, .picker__day--outfocus:hover', {
	'background': getTheme().primary.disabledBackgroundColor
});
injectStyle('.picker__day--today:before', {
	'border-top': getTheme().primary.activeBackgroundColor
});
injectStyle('.picker--focused .picker__day--selected, .picker__day--selected, .picker__day--selected:hover', {
	'background': getTheme().primary.backgroundColor
});
injectStyle('.picker__button--clear:hover, .picker__button--close:hover, .picker__button--today:hover', {
	'background': getTheme().primary.disabledBackgroundColor,
	'border-bottom-color': getTheme().primary.disabledBackgroundColor
});
injectStyle('.picker__button--clear:focus, .picker__button--close:focus, .picker__button--today:focus', {
	'background': getTheme().primary.disabledBackgroundColor,
	'border-color': getTheme().primary.backgroundColor
});
injectStyle('.picker__button--today:before', {
	'border-top': getTheme().primary.activeBackgroundColor
});

/* default.time.css */

injectStyle('.picker--focused .picker__list-item--highlighted, .picker__list-item--highlighted:hover, .picker__list-item:hover', {
	'background': getTheme().primary.disabledBackgroundColor
});
injectStyle('.picker__list-item--highlighted, .picker__list-item:hover', {
	'border-color': getTheme().primary.backgroundColor
});
injectStyle('.picker--focused .picker__list-item--selected, .picker__list-item--selected, .picker__list-item--selected:hover', {
	'background': getTheme().primary.backgroundColor
});
