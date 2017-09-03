'use strict';

$("#permissionWarning").append("<span></span>").hide();

if(window.Notification){

	$("#Notification").removeClass("badge-danger").addClass("badge-success");

	if(window.localStorage){

		$("#LocalStorage").removeClass("badge-danger").addClass("badge-success");

		Forms.restore('#inputArea');
		Notification.requestPermission(function(permission){

			switch(permission){
				case "default":
				case "denied":

					$("#permissionWarning>span").html("<strong>Notification permission</strong> "+permission);
					$("#permissionWarning").show();
					break;
				case "granted":

					setInterval(function(){

						Forms.store('#inputArea');
						new Notification("Temporary saved", {});
					},5000)

					break;
			};
		});
	}
}
