'use strict';

var Strings = function() {

	return {

		removeNewLine: function(string){

			if(string == null){

				throw "string is null.";
			}

			return string.replace(/\r|\n|\r\n/g, "");
		}
	}
}();
