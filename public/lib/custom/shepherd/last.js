'use strict';

Shepherd.on("show", function(e){

	if(!e.step.options.scrollToHandler){

		e.step.options.scrollToHandler = function(){

			// attachTo of '[selector] [direction]' only.
			var selector = e.step.options.attachTo.split(/\s+/)[0];
			var y = offsetTopFromTarget('#system_content', selector) - menuHeight();
			getContent().animate({ scrollTop: y }, 'fast');
		};
	}
});
