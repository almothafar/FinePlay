'use strict';

if(navigator.onLine){$("#OnLine").removeClass("badge-danger").addClass("badge-success")}
if(navigator.cookieEnabled){$("#Cookie").removeClass("badge-danger").addClass("badge-success")}
if(navigator.geolocation){$("#Geolocation").removeClass("badge-danger").addClass("badge-success")}
if(navigator.mediaDevices){$("#MediaDevices").removeClass("badge-danger").addClass("badge-success")}
if(navigator.share){$("#Share").removeClass("badge-danger").addClass("badge-success")}
if(navigator.vibrate){$("#Vibration").removeClass("badge-danger").addClass("badge-success")}

var navigatorMap = new Object();
$.each(navigator, function(key, val){

	var valType = typeof val;
	if('object' == valType || 'function' ==  valType ){return true;}
	navigatorMap[key] = val;
});

var navigatorKeys = Object.keys(navigatorMap);
navigatorKeys.sort();

$.each(navigatorKeys, function(i, key){

	$("#navigatorPropTable>tbody").append("<tr></tr>");
	$("#navigatorPropTable>tbody > tr:last-child").append("<td>"+key+"</td><td>"+navigatorMap[key]+"</td>");
});

var mimeTypes = new Array();
for ( var i = 0; i < navigator.mimeTypes.length; i++) {

  mimeTypes.push(navigator.mimeTypes[i]);
}
mimeTypes.sort(function(mimeType0,mimeType1){

	return mimeType0.description.localeCompare(mimeType1.description);
});
for ( i in mimeTypes) {

	var mimeType = mimeTypes[i];

	$("#mimeTypeTable>tbody").append("<tr></tr>");
	$("#mimeTypeTable>tbody > tr:last-child").append("<td>"+mimeType.description+"</td><td>"+mimeType.type+"</td><td>"+mimeType.suffixes+"</td>");
};
