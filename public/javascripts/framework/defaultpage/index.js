'use strict';

$('#tasklist').on('click', function (e) {

	var tasks = $('#tasklist>a');
	tasks.removeClass("active");
	var target = e.target;
	if(tasks.eq(0)[0]===target){
		$("#taskframe").attr({src:Routes.controllers.framework.defaultpage.Defaultpage.page("notfound").url});
		tasks.eq(0).addClass("active");
	}else if(tasks.eq(1)[0]===target){
		$("#taskframe").attr({src:Routes.controllers.framework.defaultpage.Defaultpage.page("badrequest").url});
		tasks.eq(1).addClass("active");
	}else if(tasks.eq(2)[0]===target){
		$("#taskframe").attr({src:Routes.controllers.framework.defaultpage.Defaultpage.page("unauthorized").url});
		tasks.eq(2).addClass("active");
	}else if(tasks.eq(3)[0]===target){
		$("#taskframe").attr({src:Routes.controllers.framework.defaultpage.Defaultpage.page("deverror").url});
		tasks.eq(3).addClass("active");
	}else if(tasks.eq(4)[0]===target){
		$("#taskframe").attr({src:Routes.controllers.framework.defaultpage.Defaultpage.page("devnotfound").url});
		tasks.eq(4).addClass("active");
	}else if(tasks.eq(5)[0]===target){
		$("#taskframe").attr({src:Routes.controllers.framework.defaultpage.Defaultpage.page("error").url});
		tasks.eq(5).addClass("active");
	}else if(tasks.eq(6)[0]===target){
		$("#taskframe").attr({src:Routes.controllers.framework.defaultpage.Defaultpage.page("todo").url});
		tasks.eq(6).addClass("active");
	}
});

