'use strict';

var timeMap = new Object();
timeMap['Browser UTC Time'] = new Date().toISOString();
timeMap['Browser Local Time'] = new Date().toLocaleString();
timeMap['Browser toString()'] = new Date().toString();

var timeKeys = Object.keys(timeMap);
timeKeys.sort();

$.each(timeKeys, function(i, key) {

	$("tbody").append("<tr></tr>");
	$("tbody > tr:last-child").append(
			"<td>" + key + "</td><td>" + timeMap[key] + "</td>");
});

//

var zeroMoment = moment({
	y : 0,
	M : 0,
	d : 0
});
var requestMoment = moment(Messages("clientDateTime"));

var requestDate = new Date();
setTimeout(function() {

	setInterval(function() {

		var currentDate = new Date();
		var progressSec = parseInt((currentDate.getTime() - requestDate
				.getTime()) / 1000);

		var fromZero = zeroMoment.clone().add(progressSec, 'second').format(
				'HH:mm:ss');
		var fromRequest = requestMoment.clone().add(progressSec, 'second')
				.format('YYYY-MM-DDTHH:mm:ss');

		console.log('Unverified: ' + fromRequest + ' (' + fromZero + ')');
	}, 1000);
}, 3000);
