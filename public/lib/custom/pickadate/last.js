'use strict';

var pickadateMessages = function(messageKey){

	return $("#pickadate_messages").data('messages')[messageKey];
}
// provisional coped for pickadate 3.5.6
jQuery.fn.pickadate.defaults.close = pickadateMessages(MessageKeys.CLOSE);
jQuery.extend( jQuery.fn.pickadate.defaults, jQuery.fn.pickadate.defaults);