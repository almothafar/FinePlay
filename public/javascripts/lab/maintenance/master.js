'use strict';

showFromLeft('#masterContent');

var buildForm = function(method, action, json){

	var form = $('<form/>', {action: action, method: method});
	for(var key in json){

		var value = json[key];
		form.append($('<input/>', {type: 'hidden', name: key, value: value}))
	}

	return form;
}

var postForm = function(form){

	form.appendTo(document.body).submit();
}

$('.nextButton').on('click', function(e){

	e.preventDefault();

	var target = $(this).parent().parent().find('td').eq(0).text();

	hideToLeft('#masterContent', function(e){

		var json = {target: target, task: Routes.controllers.lab.maintenance.Maintenance.master().url + "?" + getToken()};
		var form = buildForm('post', Routes.controllers.lab.maintenance.Maintenance.detail().url + "?" + getToken(), json);
		postForm(form);
	});
});
