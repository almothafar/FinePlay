'use strict';

$(document).ready(function() {

	var lineChartCtx = document.getElementById("lineChart");
	var barData = {
		labels: [
			Messages('m1'),
			Messages('m2'),
			Messages('m3'),
			Messages('m4'),
			Messages('m5'),
			Messages('m6'),
			Messages('m7')
		],
		datasets: [
			{
				label: "My First dataset",
				fill: false,
				lineTension: 0.1,
				backgroundColor: "rgba(75,192,192,0.4)",
				borderColor: "rgba(75,192,192,1)",
				borderCapStyle: 'butt',
				borderDash: [],
				borderDashOffset: 0.0,
				borderJoinStyle: 'miter',
				pointBorderColor: "rgba(75,192,192,1)",
				pointBackgroundColor: "#fff",
				pointBorderWidth: 1,
				pointHoverRadius: 5,
				pointHoverBackgroundColor: "rgba(75,192,192,1)",
				pointHoverBorderColor: "rgba(220,220,220,1)",
				pointHoverBorderWidth: 2,
				pointRadius: 1,
				pointHitRadius: 10,
				data: [65, 59, 80, 81, 56, 55, 40],
			}
		]
	};
	var lineChart = new Chart(lineChartCtx, {
		type: 'line',
		data: barData,
		options: {
			scales: {
				yAxes: [{
					ticks: {
						beginAtZero:true
					}
				}]
			}
		}
	});

	var barChartCtx = document.getElementById("barChart");
	var barData = {
		labels: [
			Messages('m1'),
			Messages('m2'),
			Messages('m3'),
			Messages('m4'),
			Messages('m5'),
			Messages('m6'),
			Messages('m7')
		],
		datasets: [
			{
				label: "My First dataset",
				backgroundColor: "rgba(255,99,132,0.2)",
				borderColor: "rgba(255,99,132,1)",
				borderWidth: 1,
				hoverBackgroundColor: "rgba(255,99,132,0.4)",
				hoverBorderColor: "rgba(255,99,132,1)",
				data: [65, 59, 80, 81, 56, 55, 40],
			}
		]
	};
	var barChart = new Chart(barChartCtx, {
		type: 'bar',
		data: barData,
		options: {
			scales: {
				yAxes: [{
					ticks: {
						beginAtZero:true
					}
				}]
			}
		}
	});

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
});
