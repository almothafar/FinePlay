'use strict';

$(document).ready(function() {

	var doughnutChartCtx = document.getElementById("doughnutChart");
	var doughnutData = {
		labels: [
			"Red",
			"Green",
			"Yellow"
		],
		datasets: [
			{
				data: [300, 50, 100],
				backgroundColor: [
					"#FF6384",
					"#36A2EB",
					"#FFCE56"
				],
				hoverBackgroundColor: [
					"#FF6384",
					"#36A2EB",
					"#FFCE56"
				]
			}
		]
	};
	var doughnutChart = new Chart(doughnutChartCtx, {
		type: 'doughnut',
		data: doughnutData,
		animation:{
			animateScale:true
		}
	});

	//

	$('#pdfButton').on('click', function() {

		var canvas = $('#doughnutChart')[0];

		var imgData = canvas.toDataURL();
		var doc = new jsPDF()

		doc.text(20, 20, 'This is the default font.');

		doc.setFont('courier');
		doc.setFontType('normal');
		doc.text(20, 30, 'This is courier normal.');

		doc.setFont('times');
		doc.setFontType('italic');
		doc.text(20, 40, 'This is times italic.');

		doc.setFont('helvetica');
		doc.setFontType('bold');
		doc.text(20, 50, 'This is helvetica bold.');

		doc.setFont('courier');
		doc.setFontType('bolditalic');
		doc.text(20, 60, 'This is courier bolditalic.');

		var width = doc.internal.pageSize.width;
		doc.addImage(imgData, 'JPEG', 55, 70, width / 2, width / 2);

		doc.save('document.pdf');
	});
});
