'use strict';

injectStyle('.select2-container--default .select2-selection', {
	'border-radius': getTheme().borderRadius
});

injectStyle('.form-control-lg+.select2-container--default .select2-selection', {
	'border-radius': getTheme().borderRadiusLarge
});

injectStyle('.form-control-sm+.select2-container--default .select2-selection', {
	'border-radius': getTheme().borderRadiusSmall
});

injectStyle('.form-control+.select2-container--default .select2-selection__choice', {
	'border-radius': getTheme().borderRadius
});

injectStyle('.form-control-lg+.select2-container--default .select2-selection__choice', {
	'border-radius': getTheme().borderRadiusLarge
});

injectStyle('.form-control-sm+.select2-container--default .select2-selection__choice', {
	'border-radius': getTheme().borderRadiusSmall
});

injectStyle('.select2-container--default .select2-selection--single', {
	'background-color': getTheme().backgroundColor
});

injectStyle('.select2-container--default .select2-selection--multiple', {
	'background-color': getTheme().backgroundColor
});

injectStyle('.select2-container--default .select2-results__option--highlighted[aria-selected]', {
	'background-color': getTheme().primary.backgroundColor
});
