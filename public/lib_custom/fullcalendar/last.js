'use strict';

insertStyle('.fc-button-group, .fc-view-container', {
	'background-color': getTheme().backgroundColor
});

insertStyle('.fc-event', {
	'border': '1px solid ' + getTheme().primary.backgroundColor
});

insertStyle('.fc-event, .fc-event-dot', {
	'background-color': getTheme().primary.backgroundColor
});
