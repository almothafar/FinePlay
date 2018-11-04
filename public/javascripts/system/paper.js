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

				$('body').prepend('<div class="sheets"><section class="sheet padding-10mm"><article><h1>Illeagl Paper.</h1><p>' + responseHtml + '</p></article></section></div>');
			}
		},
		function (jqXHR, textStatus, errorThrown) {

			$('body').prepend('<div class="sheets"><section class="sheet padding-10mm"><article><h1>Error.</h1><p>' + 'Status'+'&nbsp;<strong>'+textStatus+'</strong>&nbsp;-&nbsp;'+'Error'+'&nbsp;<strong>'+errorThrown+'</strong>' + '</p></article></section></div>');
		}
	);
});
