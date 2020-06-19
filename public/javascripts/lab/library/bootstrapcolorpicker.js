'use strict';

$('#picker').colorpicker({

	inline: true,
	container: true,
	customClass: 'colorpicker-2x',
	sliders: {
		saturation: {
			maxLeft: 200,
			maxTop: 200
		},
		hue: {
			maxTop: 200
		},
		alpha: {
			maxTop: 200
		}
	},
	extensions: [
		{
			name: 'swatches',
			options: { // extension options
				colors: {
					'black': '#000000',
					'gray': '#888888',
					'white': '#ffffff',
					'red': 'red',
					'default': '#777777',
					'primary': '#337ab7',
					'success': '#5cb85c',
					'info': '#5bc0de',
					'warning': '#f0ad4e',
					'danger': '#d9534f'
				},
				namesAsValues: true
			}
		}
	]
}).on('colorpickerChange colorpickerCreate', function (e) {

	e.colorpicker.picker.css('background-color', e.colorpicker.color.toRgbString());
//	e.colorpicker.picker.parents('.modal').find('.modal-header').css('background-color', e.colorpicker.color.toRgbString());
});

$('#okButton').on('click', function (e) {

	var button = $(this);

	var picker = button.closest(".modal-content").find('#picker');
	console.dir(picker.colorpicker('getValue'));
	console.dir(picker.data('colorpicker').color.toRgbString());
	console.dir(picker.data('colorpicker').color.toHexString());
	console.dir(picker.data('colorpicker').color.toHslString());

	$('#getButton').css({

		"backgroundColor": picker.colorpicker('getValue')
	});
})

$('#cp2').colorpicker({
	container: true,
	customClass: 'colorpicker-2x',
	format: null,
	sliders: {
		saturation: {
			maxLeft: 200,
			maxTop: 200
		},
		hue: {
			maxTop: 200
		},
		alpha: {
			maxTop: 200
		}
	},
	popover: {
		placement: 'auto'
	}
});
