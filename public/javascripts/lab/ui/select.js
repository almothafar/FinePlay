'use strict';

var selectInfo = {
	0:{
		'0 0':{
			'0 0 0':{},
			'0 0 1':{},
			'0 0 2':{}
		},
		'0 1':{
			'0 1 0':{},
			'0 1 1':{},
			'0 1 2':{}
		},
		'0 2':{
			'0 2 0':{},
			'0 2 1':{},
			'0 2 2':{}
		}
	},
	1:{
		'1 0':{
			'1 0 0':{},
			'1 0 1':{},
			'1 0 2':{}
		},
		'1 1':{
			'1 1 0':{},
			'1 1 1':{},
			'1 1 2':{}
		},
		'1 2':{
			'1 2 0':{},
			'1 2 1':{},
			'1 2 2':{}
		}
	},
	2:{
		'2 0':{
			'2 0 0':{},
			'2 0 1':{},
			'2 0 2':{}
		},
		'2 1':{
			'2 1 0':{},
			'2 1 1':{},
			'2 1 2':{}
		},
		'2 2':{
			'2 2 0':{},
			'2 2 1':{},
			'2 2 2':{}
		}
	},
}

$( "#bigSelect>select" ).on('change', function(e) {

	var path = [$(this).val()];
	update(path, $(this).parent().next().find("select"));
});

$( "#middleSelect>select" ).on('change', function(e) {

	var path = [$( "#bigSelect>select" ).val() , $(this).val()];
	update(path, $(this).parent().next().find("select"));
});

$( "#smaleSelect>select" ).on('change', function(e) {

});

var update = function(path, updateElement){

	updateElement.empty();

	var children = selectInfo;
	$.each(path, function(i, parent){

		children = children[parent];
	});

	$.each(children, function(key, value){

		var div = $('<option value="'+key+'">'+key+'</option>');
		updateElement.append(div);
	});

	updateElement.trigger("change");
}

var path = [];
update(path, $( "#bigSelect>select" ));
