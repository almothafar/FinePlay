'use strict';

$("#default").on('click', function(e) {
	alert(e.target.id);
});

$("#disableLink").on('click', function(e) {
	e.preventDefault();
	alert(e.target.id);
});

$("#disableBubbling").on('click', function(e) {
	e.stopPropagation();
	alert(e.target.id);
});

$("#disableLinkAndBubbling").on('click', function(e) {
	e.preventDefault();
	e.stopPropagation();
	alert(e.target.id);
});

$("#disableAll").on('click', function(e) {
	return false;
	alert(e.target.id);
});

getContent().on('click', function(e) {
	alert(e.target.id + ' > ' + e.currentTarget.id);
});
