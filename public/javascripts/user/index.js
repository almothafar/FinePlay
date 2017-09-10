'use strict';

//

if(Messages("formError")) {

	shake('#signInPanel');
}

var signInImageLink = "url(" + Messages("signInImage") + ")";
$('#system_base').css({
	"background-image": signInImageLink,
	"background-size": "cover",
	"background-position": "center"
});

$(window).on('keydown', function(e){

	var keyCode = e.which ? e.which : e.keyCode;
	if (13 == keyCode) {

		if($("#licenseDialog").is(".show")){

			$('#licenseOk').trigger("click");
		}else{

			$('#signInButton').trigger("click");
		}

		e.preventDefault();
	}
});

//

var fixedUsers = $('#fixedUsers>#user_list');
var fixedUsersData = JSON.parse($('#fixedUsersData').text());
$.each(fixedUsersData, function(){

	var fixedUser = this;

	var fixedUsersHtml = '';
	fixedUsersHtml = fixedUsersHtml + '<div class="rounded mx-3 my-3 fixedUser">';
	if('Dev' == getMode()){
		fixedUsersHtml = fixedUsersHtml +
			'<div class="fixedUser_info">' +
				'<span class="badge badge-pill badge-info mb-1">' + fixedUser.locale + '</span>&nbsp;' +
				'<span class="badge badge-pill badge-info mb-1">' + fixedUser.zoneId.id + '</span>&nbsp;' +
				'<span class="badge badge-pill badge-info mb-1">' + fixedUser.roles + '</span>' +
			'</div>';
	}
	fixedUsersHtml = fixedUsersHtml +
		'<div class="row justify-content-center">' +
			'<div class="my-1"><i class="fa fa-inverse fa-square fa-2x fa-user-o rounded-circle bg-success shadow fixedUser_body" aria-hidden="true"></i></div>' +
			'</div>' +
		'<div class="fixedUser_caption"><strong class="fixedUser_UserId">' + fixedUser.userId + '</strong></div>';
	if('Dev' == getMode()){
		fixedUsersHtml = fixedUsersHtml +'<div class="fixedUser_Password d-none">' + fixedUser.password + '</div>';
	}
	fixedUsersHtml = fixedUsersHtml +'</div>';

	fixedUsers.append(fixedUsersHtml);
});

$('#selectUser').on('click', function(){

	showUsers();
});

$('#system_users').on('click', function(){

	hideUsers();
});

$('.fixedUser').on('click', function(){

	var userId = $(this).find('.fixedUser_UserId').text();
	var password = $(this).find('.fixedUser_Password').text();

	$('#userId').val(userId);
	$('#password').val(password);

	hideUsers();
});

if(1 <= fixedUsersData.length){

	$('#selectUserContainer').removeClass("d-none");
}

