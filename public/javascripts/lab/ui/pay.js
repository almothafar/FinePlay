'use strict';

$(document).ready(function () {

	var start = moment.tz(messages("clientStartOffsetDateTime"), messages("clientZoneId"));
	var limit = moment.tz(messages("clientLimitOffsetDateTime"), messages("clientZoneId"));

	var requestDate = new Date();
	setInterval(function() {

		var currentDate = new Date();
		var progressMilliseconds = parseInt((currentDate.getTime() - requestDate.getTime()));

		var current = start.clone().add(progressMilliseconds, 'milliseconds');
		var duration = moment.duration(limit.diff(current))

		$('#time').text(current.format());

		$('#years').text(duration._data.years);
		$('#months').text(duration._data.months);
		$('#days').text(duration._data.days);
		$('#hours').text(duration._data.hours);
		$('#minutes').text(duration._data.minutes);
		$('#seconds').text(duration._data.seconds);
		$('#milliseconds').text(duration._data.milliseconds);
	}, 77);
});
