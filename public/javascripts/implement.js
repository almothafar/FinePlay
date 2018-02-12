'use strict';

var injectStyle = function(selector, css) {

	var rule = selector + "{";
	for ( var key in css) {

		var value = css[key];
		rule = rule + key + ": " + value + ";"
	}
	rule = rule + "}";

	var sheets = document.styleSheets;
	var sheet = sheets[sheets.length - 1];
	sheet.insertRule(rule, sheet.cssRules.length);
}

var isStickySupported = function() {

	return [ '', '-webkit-', '-moz-', '-ms-' ].some(function(prefix){

		var elem = document.createElement('div');
		elem.style = 'position:' + prefix + 'sticky' + ';';
		return elem.style.position.indexOf('sticky') !== -1;
	});
}();
