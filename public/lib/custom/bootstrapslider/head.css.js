'use strict';

injectStyle('.slider.slider-disabled .slider-handle', {
	'background-image': 'linear-gradient(to bottom, ' + getTheme().primary.disabledBackgroundColor + ' 0, ' + getTheme().primary.disabledBackgroundColor + ' 100%);'
});

injectStyle('.slider-handle', {
	'background-image': 'linear-gradient(to bottom, ' + getTheme().primary.backgroundColor + ' 0, ' + getTheme().primary.backgroundColor + ' 100%);'
});
