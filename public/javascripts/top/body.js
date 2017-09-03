'use strict';

var cookies = function(){

	var cookie = document.cookie;
	var result = new Object();
	if("" != cookie){

		var cookies = cookie.split(";");
		for( var i = 0; i < cookies.length; i++ ){

			var cookie = cookies[ i ].split( '=' );
			var key = cookie[0].trim();
			var value = cookie[1].trim();

			result[ key ] = decodeURIComponent( value );
		}
	}
	return result;
}
