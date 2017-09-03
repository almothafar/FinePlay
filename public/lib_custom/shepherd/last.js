'use strict';

insertStyle('.shepherd-element.shepherd-theme-default.shepherd-element-attached-top.shepherd-element-attached-center.shepherd-has-title .shepherd-content:before, .shepherd-element.shepherd-theme-default.shepherd-element-attached-top.shepherd-element-attached-right.shepherd-target-attached-bottom.shepherd-has-title .shepherd-content:before, .shepherd-element.shepherd-theme-default.shepherd-element-attached-top.shepherd-element-attached-left.shepherd-target-attached-bottom.shepherd-has-title .shepherd-content:before', {
	'border-bottom-color': getTheme().primary.disabledBackgroundColor
});

insertStyle('.shepherd-element.shepherd-theme-default.shepherd-has-title .shepherd-content header', {
	'background': getTheme().primary.disabledBackgroundColor
});

insertStyle('.shepherd-element.shepherd-theme-default .shepherd-content footer .shepherd-buttons li .shepherd-button', {
	'background': getTheme().primary.backgroundColor
});

Shepherd.on("show", function(e){

	if(!e.step.options.scrollToHandler){

		e.step.options.scrollToHandler = function(){

			// attachTo of '[selector] [direction]' only.
			var selector = e.step.options.attachTo.split(/\s+/)[0];
			var y = offsetTopFromTarget('#system_content', selector);
			getContent().animate({ scrollTop: y }, 'fast');
		};
	}
});
