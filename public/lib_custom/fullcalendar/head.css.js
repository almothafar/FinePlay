'use strict';

injectStyle('.fc-button-group, .fc-view-container', {
	'background-color': getTheme().backgroundColor
});

injectStyle('.fc-event', {
	'border': '1px solid ' + getTheme().primary.backgroundColor
});

injectStyle('.fc-event, .fc-event-dot', {
	'background-color': getTheme().primary.backgroundColor
});
