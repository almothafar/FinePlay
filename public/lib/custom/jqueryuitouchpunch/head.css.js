'use strict';

injectStyle('.ui-draggable:not(.ui-draggable-disabled), .ui-sortable:not(.ui-sortable-disabled)', {
	'-ms-touch-action': 'none',
	'touch-action': 'none'
});
