'use strict';

(function(){

	window.markedstrap = function(selector){

		$(selector + ' table').addClass('table table-bordered table-striped');

		$(selector + ' pre').addClass('p-3 bg-light');

		$(selector + ' ul>li>input[type="checkbox"]').parents('ul').addClass('list-unstyled');

		$(selector + ' blockquote').addClass('blockquote');
	}
})();