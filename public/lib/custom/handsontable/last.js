'use strict';

var handsontableMessages = function(messageKey){

	return $("#handsontable_messages").data('messages')[messageKey];
}

Handsontable.hooks.add('beforeContextMenuSetItems', function(menuItems){

	var getCheckedString = function(originString, translatedString){

		var check = '<span class=\"selected\">✓</span>';
		var checkedString = originString.startsWith(check) ? check : '';
		return checkedString + translatedString;
	}

	$.each(menuItems, function(i, item){

		var item_name = item.name;
		switch (item.key) {

			case 'row_above': item.name = handsontableMessages(MessageKeys.INSERT__ROW__ABOVE); break;
			case 'row_below': item.name = handsontableMessages(MessageKeys.INSERT__ROW__BELOW); break;
			case 'col_left': item.name = handsontableMessages(MessageKeys.INSERT__COLUMN__LEFT); break;
			case 'col_right': item.name = handsontableMessages(MessageKeys.INSERT__COLUMN__RIGHT); break;
			case 'remove_row': item.name = handsontableMessages(MessageKeys.REMOVE__ROW); break;
			case 'remove_col': item.name = handsontableMessages(MessageKeys.REMOVE__COLUMN); break;
			case 'undo': item.name = handsontableMessages(MessageKeys.UNDO); break;
			case 'redo': item.name = handsontableMessages(MessageKeys.REDO); break;
			case 'make_read_only': item.name = function(){ return getCheckedString(item_name.call(this), handsontableMessages(MessageKeys.READ__ONLY)); }; break;
			case 'alignment': item.name = handsontableMessages(MessageKeys.ALIGNMENT);

				$.each(item.submenu.items, function(i, alignmentitem){

					var alignmentitem_name = alignmentitem.name;
					switch (alignmentitem.key) {

						case 'alignment:left': alignmentitem.name = function(){ return getCheckedString(alignmentitem_name.call(this), handsontableMessages(MessageKeys.LEFT)); }; break;
						case 'alignment:center': alignmentitem.name = function(){ return getCheckedString(alignmentitem_name.call(this), handsontableMessages(MessageKeys.CENTER)); }; break;
						case 'alignment:right': alignmentitem.name = function(){ return getCheckedString(alignmentitem_name.call(this), handsontableMessages(MessageKeys.RIGHT)); }; break;
						case 'alignment:justify': alignmentitem.name = function(){ return getCheckedString(alignmentitem_name.call(this), handsontableMessages(MessageKeys.JUSTIFY)); }; break;
						case 'alignment:top': alignmentitem.name = function(){ return getCheckedString(alignmentitem_name.call(this), handsontableMessages(MessageKeys.TOP)); }; break;
						case 'alignment:middle': alignmentitem.name = function(){ return getCheckedString(alignmentitem_name.call(this), handsontableMessages(MessageKeys.MIDDLE)); }; break;
						case 'alignment:bottom': alignmentitem.name = function(){ return getCheckedString(alignmentitem_name.call(this), handsontableMessages(MessageKeys.BOTTOM)); }; break;
						default:;
					};
				});
				break;
			case 'copy': item.name = handsontableMessages(MessageKeys.COPY); break;
			case 'cut': item.name = handsontableMessages(MessageKeys.CUT); break;
			default:;
		}
	});
});
