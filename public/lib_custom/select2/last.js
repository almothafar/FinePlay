'use strict';

insertStyle('.select2-container--default .select2-selection', {
	'border-radius': getTheme().borderRadius
});

insertStyle('.form-control-lg+.select2-container--default .select2-selection', {
	'border-radius': getTheme().largeBorderRadius
});

insertStyle('.form-control-sm+.select2-container--default .select2-selection', {
	'border-radius': getTheme().smallBorderRadius
});

insertStyle('.select2-container--default .select2-selection--single', {
	'background-color': getTheme().backgroundColor
});

insertStyle('.select2-container--default .select2-selection--multiple', {
	'background-color': getTheme().backgroundColor
});

insertStyle('.select2-container--default .select2-results__option--highlighted[aria-selected]', {
	'background-color': getTheme().primary.backgroundColor
});
