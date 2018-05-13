'use strict';

var frappeganttMessages = function(messageKey) {

	return $("#frappegantt_messages").data('messages')[messageKey];
}

moment.locale(frappeganttMessages(MessageKeys.FRAPPEGANTT_LANG));
