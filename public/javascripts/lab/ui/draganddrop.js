'use strict';

$(".employee").draggable({
	helper:"clone",
	containment: ".room",
	revert: "invalid",
	opacity: 0.5,
});

$(".seat" ).droppable({
	accept : ".employee" ,
	drop : function(event , ui){

		var currentEmployee = $(this).find(".employee");
		var currentEmployeeSeat = $(this);

		var newEmployee = $(ui.draggable);
		var newEmployeeSeat = newEmployee.parent();
		newEmployee.detach();

		newEmployee.appendTo(currentEmployeeSeat);
		currentEmployee.appendTo(newEmployeeSeat);
	}
});
