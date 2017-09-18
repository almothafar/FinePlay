'use strict';

var color = $.Color(getTheme().primary.backgroundColor);
var lightColor = $.Color({hue: color.hue(), saturation: color.saturation(), lightness: 0.9, alpha: color.alpha()}).toHexString();

injectStyle('.note-toolbar.card-header', {
	'background-color': lightColor,
	'border-top-right-radius': getTheme().borderRadius,
	'border-top-left-radius': getTheme().borderRadius
});

injectStyle('.note-editing-area, .note-editor.note-frame .note-statusbar', {
	'border-bottom-right-radius': getTheme().borderRadius,
	'border-bottom-left-radius': getTheme().borderRadius
});
