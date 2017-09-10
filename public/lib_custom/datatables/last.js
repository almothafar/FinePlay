'use strict';

var dataTablesMessages = function(messageKey){

	return $("#datatables_messages").data('messages')[messageKey];
}

$.fn.DataTable.defaults.oLanguage.sUrl = dataTablesMessages("langUrl");

ready('.dataTables_wrapper', function(){

	$('.dataTables_filter>label').each(function(){

		$(this)[0].firstChild.textContent= dataTablesMessages(MessageKeys.FILTER) +":";
	});
});