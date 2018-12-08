'use strict';

$(document).ready(function() {

	$('#htmlFormatButton').on('click', function(){

		var html = $('#htmlTabContent div:eq(0) textarea').val();
		html = prettier.format(html, { parser: "html", plugins: prettierPlugins });
		$('#htmlTabContent div:eq(1) textarea').val(html);
	});

	$('#cssFormatButton').on('click', function(){

		var css = $('#cssTabContent div:eq(0) textarea').val();
		css = prettier.format(css, { parser: "css", plugins: prettierPlugins });
		$('#cssTabContent div:eq(1) textarea').val(css);
	});

	$('#jsonFormatButton').on('click', function(){

		var json = $('#jsonTabContent div:eq(0) textarea').val();
		json = prettier.format(json, { parser: "json", plugins: prettierPlugins });
		$('#jsonTabContent div:eq(1) textarea').val(json);
	});
});
