'use strict';

(function(){

	// pseudo Markdown editor.

	markededit = {

		init: function(selector, options){

			var getPosition = function(element){

				var text = element.value;
				var position = {
					'lineStart': text.lastIndexOf('\n', element.selectionStart),
					'selectionStart': element.selectionStart,
					'selectionEnd': element.selectionEnd,
					'lineEnd': text.indexOf('\n', element.selectionEnd)
				};
				position['lineStart'] = -1 == position['lineStart'] ? 0 : position['lineStart'] + 1;
				position['lineEnd'] = -1 == position['lineEnd'] ? text.length : position['lineEnd'];

				return position;
			}

			var insertText = function(value, start, end, text){

				var left = value.substring(0, start);
				var right = value.substring(end, value.length);
				return left + text + right;
			}

			var toolButtons = $(selector + ' .btn-toolbar>.btn-group>.btn');
			toolButtons.tooltip({placement: 'bottom'});

			var textArea = $(selector + ' textarea')[0];
			toolButtons.on('click', function(e){

				var toolButton = $(this);
				var type = toolButton.data('type');
				var text = toolButton.data('text');

				var position = getPosition(textArea);
				var value = textArea.value;

				switch(type){
				case "PREPEND":

					textArea.value = insertText(value, position['lineStart'], position['lineStart'], text);
					break;
				case "WRAP":

					var selection = value.substring(position['selectionStart'], position['selectionEnd']);
					var wrapedText = text.replace('{}', selection);
					textArea.value = insertText(value, position['selectionStart'], position['selectionEnd'], wrapedText);
					break;
				case "APPEND":

					textArea.value = insertText(value, position['lineEnd'], position['lineEnd'], text);
					break;
				default:
				};

				e.stopPropagation();
			});

			return {

				markdown: function(){

					return textArea.value
				},
				setMarkdown: function(value){

					textArea.value = value;
				},
				html: function(){

					return marked(textArea.value);
				}
			}
		}
	}
})();
