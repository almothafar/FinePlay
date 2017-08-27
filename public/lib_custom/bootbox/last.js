'use strict';

var sheets = document.styleSheets;
var sheet = sheets[sheets.length - 1];
sheet.insertRule('.bootbox.modal .btn-default{' +
	'border-color: '+ $('html').data('primary-border-color') +';' +
	'color: '+ $('html').data('primary-background-color') +';' +
'}', sheet.cssRules.length);

bootbox.setDefaults({locale: "@Messages(MessageKeys.BOOTBOX_LANG)"});
