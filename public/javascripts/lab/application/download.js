'use strict';

if(window.URL){$("#URL").removeClass("badge-danger").addClass("badge-success")}
if("" == document.createElement('a').download){$("#DownloadAttribute").removeClass("badge-danger").addClass("badge-success")}

var blob = new Blob(["(｀・ω・´)\r\n(*´ω｀*)\r\n(´･ω･`)\r\n"], {type : 'application/octet-binary'});
var url = URL.createObjectURL(blob);
$("#local").attr("href", url);

$(window).on('keydown', function(e){

	if(e.altKey && e.keyCode === 89){

		$('.lab.d-none').removeClass('d-none');
		return false;
	}
})
