'use strict';

injectStyle('.shepherd-element[x-placement^="bottom"].shepherd-has-title .popper__arrow', {
	'border-bottom-color': getTheme().primary.disabledBackgroundColor
});

injectStyle('.shepherd-element.shepherd-has-title .shepherd-content header', {
	'background': getTheme().primary.disabledBackgroundColor
});

injectStyle('.shepherd-element.shepherd-has-cancel-link .shepherd-content header h3', {
	'color': getTheme().primary.color
});

injectStyle('.shepherd-element .shepherd-content footer .shepherd-buttons li .shepherd-button', {
	'background': getTheme().primary.backgroundColor
});
