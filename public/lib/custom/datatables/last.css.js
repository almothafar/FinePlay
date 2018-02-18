'use strict';

injectStyle('table.dataTable tr.selected>td', {
	'color': getTheme().primary.color + ' !important',
	'background-color': getTheme().primary.backgroundColor + ' !important'
});