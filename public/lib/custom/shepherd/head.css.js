'use strict';

injectStyle('.tippy-popper[x-placement^="bottom"] .tippy-arrow', {
	'border-bottom-color': getTheme().primary.disabledBackgroundColor
});

injectStyle('.shepherd-element.shepherd-has-title .shepherd-content header', {
	'background': getTheme().primary.disabledBackgroundColor
});

injectStyle('.shepherd-element .shepherd-content header .shepherd-title', {
	'color': getTheme().primary.color
});

injectStyle('.shepherd-element .shepherd-content footer .shepherd-buttons li .shepherd-button', {
	'background': getTheme().primary.backgroundColor
});