'use strict';

$(document).ready(function() {

	var paperjsMessages = function(messageKey) {

		return $("#paperjs_messages").data('messages')[messageKey];
	}

	var url = paperjsMessages('defaultUrl');
	$.ajax({
		method:"GET",
		url:url,
		dataType: "html",
		timeout: 3 * 1000
	})
	.then(
		function (responseHtml) {

			var papercss = $(responseHtml).filter('.sheets');

			if(1 == papercss.length){

				$('body').prepend(papercss);

				var executePrint = paperjsMessages('executePrint');
				if (executePrint) {

					window.print();
				}
			}else{

				$('body').prepend("<p>Illeagl Paper.</p><br>" + responseHtml);
			}
		},
		function (jqXHR, textStatus, errorThrown) {

			$('body').prepend('Status'+'&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;'+'Error'+'&nbsp;<strong>'+errorThrown+'</strong>');
		}
	);
});
