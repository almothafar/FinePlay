'use strict';

injectStyle('.fc-view-container', {
	'background-color': getTheme().backgroundColor
});

injectStyle('.fc-event', {
	'border': '1px solid ' + getTheme().primary.backgroundColor
});

injectStyle('.fc-event, .fc-event-dot', {
	'background-color': getTheme().primary.backgroundColor
});

injectStyle('.fc-button', {
	'color': getTheme().primary.backgroundColor,
	'border-color': getTheme().primary.backgroundColor
});

injectStyle('.fc-state-active', {
	'color': getTheme().primary.color,
	'background-color': getTheme().primary.backgroundColor
});
