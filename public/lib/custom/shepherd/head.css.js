'use strict';

injectStyle('.shepherd-element.shepherd-theme-default.shepherd-element-attached-top.shepherd-element-attached-center.shepherd-has-title .shepherd-content:before, .shepherd-element.shepherd-theme-default.shepherd-element-attached-top.shepherd-element-attached-right.shepherd-target-attached-bottom.shepherd-has-title .shepherd-content:before, .shepherd-element.shepherd-theme-default.shepherd-element-attached-top.shepherd-element-attached-left.shepherd-target-attached-bottom.shepherd-has-title .shepherd-content:before', {
	'border-bottom-color': getTheme().primary.disabledBackgroundColor
});

injectStyle('.shepherd-element.shepherd-theme-default.shepherd-has-title .shepherd-content header', {
	'background': getTheme().primary.disabledBackgroundColor
});

injectStyle('.shepherd-element.shepherd-theme-default.shepherd-has-cancel-link .shepherd-content header h3', {
	'color': getTheme().primary.color
});

injectStyle('.shepherd-element.shepherd-theme-default .shepherd-content footer .shepherd-buttons li .shepherd-button', {
	'background': getTheme().primary.backgroundColor
});
