'use strict';

if(window.jQuery){$("#jQuery").removeClass("badge-danger").addClass("badge-success")}
if(window.HTMLAudioElement){$("#HTML5Audio").removeClass("badge-danger").addClass("badge-success")}
if(window.HTMLVideoElement){$("#HTML5Video").removeClass("badge-danger").addClass("badge-success")}
if(window.HTMLCanvasElement){$("#Canvas").removeClass("badge-danger").addClass("badge-success")}
if(window.sessionStorage){$("#SessionStorage").removeClass("badge-danger").addClass("badge-success")}
if(window.localStorage){$("#LocalStorage").removeClass("badge-danger").addClass("badge-success")}
if(window.File){$("#File").removeClass("badge-danger").addClass("badge-success")}
if(window.FileReader){$("#FileReader").removeClass("badge-danger").addClass("badge-success")}
if(window.URL){$("#URL").removeClass("badge-danger").addClass("badge-success")}
if(window.Blob){$("#Blob").removeClass("badge-danger").addClass("badge-success")}
if(window.WebSocket){$("#WebSocket").removeClass("badge-danger").addClass("badge-success")}
if(window.Worker){$("#WebWorkers").removeClass("badge-danger").addClass("badge-success")}
if(window.AudioContext){$("#WebAudio").removeClass("badge-danger").addClass("badge-success")}
if(window.Notification){$("#Notification").removeClass("badge-danger").addClass("badge-success")}
if(window.postMessage){$("#CrossDocumentMessaging").removeClass("badge-danger").addClass("badge-success")}
if(window.speechSynthesis){$("#SpeechSynthesis").removeClass("badge-danger").addClass("badge-success")}
if(window.speechRecognition){$("#SpeechRecognition").removeClass("badge-danger").addClass("badge-success")}
if(window.DeviceOrientationEvent){$("#DeviceOrientationEvent").removeClass("badge-danger").addClass("badge-success")}
if(window.DeviceMotionEvent){$("#DeviceMotionEvent").removeClass("badge-danger").addClass("badge-success")}
if(window.openDatabase){$("#WebSQLDatabase").removeClass("badge-danger").addClass("badge-success")}
if('GamepadEvent' in window){$("#GamepadEvent").removeClass("badge-danger").addClass("badge-success")}
if('indexedDB' in window){$("#IndexedDatabase").removeClass("badge-danger").addClass("badge-success")}
if(window.ActiveXObject){$("#ActiveX").removeClass("badge-danger").addClass("badge-success")}
if(!(typeof(new XMLHttpRequest).withCredentials==='undefind')){$("#XMLHttpRequestLevel2").removeClass("badge-danger").addClass("badge-success")}

var windowMap = new Object();
$.each(window, function(key, val) {

	var valType = typeof val;
	if('object' == valType || 'function' ==  valType ){return true;}
	windowMap[key] = val;
});

var windowKeys = Object.keys(windowMap);
windowKeys.sort();

$.each(windowKeys, function(i, key) {

	$("tbody").append("<tr></tr>");
	$("tbody > tr:last-child").append(
			"<td>" + key + "</td><td>" + windowMap[key] + "</td>");
});
