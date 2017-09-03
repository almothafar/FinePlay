'use strict';

var optionSorter = function(a, b){

	return parseInt($(a).val()) > parseInt($(b).val()) ? 1 : -1;
}

$('.remove').on('click', function(){

	var selector = '#select>select';
	$(this).parent().parent().find('select>option:selected').appendTo(selector);
	$(selector + '>option').sort(optionSorter).appendTo(selector);
});

$('#selectDialog').on('show.bs.modal', function (e) {

	var button = $(e.relatedTarget);
	var group = button.data('group');
	$('#okButton').data('group', group);
});
$('#okButton').on('click', function (e) {

	var group = $(this).data('group');
	var selector = '#group-' + group + '>select';
	$('#select>select>option:selected').appendTo(selector);
	$(selector + '>option').sort(optionSorter).appendTo(selector);
});
