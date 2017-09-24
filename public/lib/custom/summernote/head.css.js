'use strict';

var color = $.Color(getTheme().primary.backgroundColor);
var lightColor = $.Color({hue: color.hue(), saturation: color.saturation(), lightness: 0.9, alpha: color.alpha()}).toHexString();

injectStyle('.note-toolbar.card-header', {
	'background-color': lightColor
});

injectStyle('.note-editor.note-frame.card', {
	'border-radius': '0.25rem'
});
