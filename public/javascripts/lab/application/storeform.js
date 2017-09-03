'use strict';

if(window.localStorage){

	$("#LocalStorage").removeClass("badge-danger").addClass("badge-success");

	$('#storeButton').on('click', function(){ Forms.store('form'); });
	$('#restoreButton').on('click', function(){ Forms.restore('form'); });

	$('#form0StoreButton').on('click', function(){ Forms.store('#form_0'); });
	$('#form0RestoreButton').on('click', function(){ Forms.restore('#form_0'); });
	$('#form1StoreButton').on('click', function(){ Forms.store('#form_1'); });
	$('#form1RestoreButton').on('click', function(){ Forms.restore('#form_1'); });
	$('#form2StoreButton').on('click', function(){ Forms.store('#form_2'); });
	$('#form2RestoreButton').on('click', function(){ Forms.restore('#form_2'); });
	$('#form3StoreButton').on('click', function(){ Forms.store('#form_3'); });
	$('#form3RestoreButton').on('click', function(){ Forms.restore('#form_3'); });
	$('#form4StoreButton').on('click', function(){ Forms.store('#form_4'); });
	$('#form4RestoreButton').on('click', function(){ Forms.restore('#form_4'); });
}
