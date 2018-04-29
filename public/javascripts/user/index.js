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
		}else	if($("#usersDialog").is(".show")){

				$('#usersOk').trigger("click");
		}else{

			$('#signInButton').trigger("click");
		}

		e.preventDefault();
	}
});

//

var fixedUsers = $('#fixedUsers');
var fixedUsersData = JSON.parse($('#fixedUsersData').text());
$.each(fixedUsersData, function(){

	var fixedUser = this;

	var fixedUsersHtml = '';
	fixedUsersHtml = fixedUsersHtml + '<div class="rounded mx-1 my-1 fixedUser">';
	if('Dev' == getMode()){
		fixedUsersHtml = fixedUsersHtml +
			'<div class="fixedUser_info">' +
				'<span class="badge badge-pill badge-info">' + fixedUser.locale + '</span>&nbsp;' +
				'<span class="badge badge-pill badge-info">' + fixedUser.zoneId.id + '</span>&nbsp;' +
				'<span class="badge badge-pill badge-info">' + fixedUser.roles + '</span>' +
			'</div>';
	}
	fixedUsersHtml = fixedUsersHtml +
		'<div class="row justify-content-center">' +
			'<div class="my-1">' +
				'<button type="button" class="btn btn-success fixedUser_body fixedUserButton rounded-circle"><i class="far fa-user"></i></button>' +
			'</div>' +
		'</div>' +
		'<div class="fixedUser_caption"><strong class="fixedUser_UserId">' + fixedUser.userId + '</strong></div>';
	if('Dev' == getMode()){
		fixedUsersHtml = fixedUsersHtml +'<div class="fixedUser_Password d-none">' + fixedUser.password + '</div>';
	}
	fixedUsersHtml = fixedUsersHtml +'</div>';

	fixedUsers.append(fixedUsersHtml);
});

$('.fixedUserButton').on('click', function(event){

	$('#usersDialog').modal('hide');

	var eventSource = $(event.target);
	var fixedUserButton;
	if(eventSource.parent().hasClass('fixedUserButton')){

		fixedUserButton = eventSource.parent();
	}else if(eventSource.hasClass('fixedUserButton')){

		fixedUserButton = eventSource;
	}

	if(fixedUserButton){

		var fixedUser = fixedUserButton.parent().parent().parent();
		var userId = fixedUser.find('.fixedUser_UserId').text();
		var password = fixedUser.find('.fixedUser_Password').text();

		$('#userId').val(userId);
		$('#password').val(password);
		$('#signInForm').parsley().validate();
	}
});

if(1 <= fixedUsersData.length){

	$('#selectUserContainer').removeClass("d-none");
}

window.Parsley.addValidator('userid',　ParsleyValidators.UserIdValidator);
window.Parsley.addValidator('password',　ParsleyValidators.PasswordValidator);
$('#signInForm').parsley();

window.Parsley.on('form:error', function() {

	shake('#signInPanel');
});
