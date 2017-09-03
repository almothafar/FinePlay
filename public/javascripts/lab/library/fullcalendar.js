'use strict';

var currentTimezone = Messages("clientZoneId");

var todayDate = moment().startOf('day');
var YM = todayDate.format('YYYY-MM');
var YESTERDAY = todayDate.clone().subtract(1, 'day').format('YYYY-MM-DD');
var TODAY = todayDate.format('YYYY-MM-DD');
var TOMORROW = todayDate.clone().add(1, 'day').format('YYYY-MM-DD');

$(document).ready(function() {

	$('#calendar').fullCalendar({
		lang: Messages(MessageKeys.FULLCALENDAR_LANG),
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay,listWeek'
		},
		now : Messages("clientDateTime"),
		defaultDate: Messages("clientDate"),
		timezone: currentTimezone,
		editable: true,
		selectable: true,
		eventLimit: true, // allow "more" link when too many events
		loading: function(bool) {
			$('#loading').toggle(bool);
		},
		events: [
			{
				title: 'All Day Event',
				start: YM + '-01'
			},
			{
				title: 'Long Event',
				start: YM + '-07',
				end: YM + '-10'
			},
			{
				id: 999,
				title: 'Repeating Event',
				start: YM + '-09T16:00:00'
			},
			{
				id: 999,
				title: 'Repeating Event',
				start: YM + '-16T16:00:00'
			},
			{
				title: 'Conference',
				start: YESTERDAY,
				end: TOMORROW
			},
			{
				title: 'Meeting',
				start: TODAY + 'T10:30:00',
				end: TODAY + 'T12:30:00'
			},
			{
				title: 'Lunch',
				start: TODAY + 'T12:00:00'
			},
			{
				title: 'Meeting',
				start: TODAY + 'T14:30:00'
			},
			{
				title: 'Happy Hour',
				start: TODAY + 'T17:30:00'
			},
			{
				title: 'Dinner',
				start: TODAY + 'T20:00:00'
			},
			{
				title: 'Birthday Party',
				start: TOMORROW + 'T07:00:00'
			},
			{
				title: 'Click for Google',
				url: 'http://google.com/',
				start: YM + '-28'
			}
		],
		eventRender: function(event, el) {
			// render the timezone offset below the event title
			if (event.start.hasZone()) {
				el.find('.fc-title').after(
					$('<div class="tzo"/>').text(event.start.format('Z'))
				);
			}
		},
		dayClick: function(date) {
			console.log('dayClick', date.toISOString(), currentTimezone);
		},
		select: function(startDate, endDate) {
			console.log('select', startDate.format(), endDate.format());
		}
	});
});
