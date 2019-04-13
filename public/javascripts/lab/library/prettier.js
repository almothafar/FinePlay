'use strict';

$(document).ready(function() {

	$('#htmlFormatButton').on('click', function(){

		var html = $('#htmlTabContent div textarea').eq(0).val();
		html = prettier.format(html, { parser: "html", plugins: prettierPlugins });
		$('#htmlTabContent div textarea').eq(1).val(html);
	});

	$('#cssFormatButton').on('click', function(){

		var css = $('#cssTabContent div textarea').eq(0).val();
		css = prettier.format(css, { parser: "css", plugins: prettierPlugins });
		$('#cssTabContent div textarea').eq(1).val(css);
	});

	$('#jsonFormatButton').on('click', function(){

		var json = $('#jsonTabContent div textarea').eq(0).val();
		json = prettier.format(json, { parser: "json", plugins: prettierPlugins });
		$('#jsonTabContent div textarea').eq(1).val(json);
	});
});
