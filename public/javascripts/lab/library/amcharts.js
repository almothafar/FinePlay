'use strict';

$(document).ready(function() {

	am4core.useTheme(am4themes_animated);

	//

	var chart = am4core.create("chartdiv", am4charts.XYChart3D);

	chart.data = [{
		"country": "USA",
		"visits": 3025
	}, {
		"country": "Japan",
		"visits": 1809
	}, {
		"country": "Germany",
		"visits": 1322
	}, {
		"country": "UK",
		"visits": 1122
	}, {
		"country": "India",
		"visits": 984
	}, {
		"country": "Spain",
		"visits": 711
	}, {
		"country": "Netherlands",
		"visits": 665
	}, {
		"country": "Canada",
		"visits": 441
	}];

	var categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());
	categoryAxis.renderer.grid.template.location = 0;
	categoryAxis.dataFields.category = "country";
	categoryAxis.renderer.minGridDistance = 60;
	categoryAxis.renderer.grid.template.disabled = true;
	categoryAxis.renderer.baseGrid.disabled = true;
	categoryAxis.renderer.axisFills.template.disabled = true;
	categoryAxis.renderer.labels.template.dy = 20;

	var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());
	valueAxis.renderer.grid.template.disabled = true;
	valueAxis.renderer.baseGrid.disabled = true;
	valueAxis.renderer.labels.template.disabled = true;
	valueAxis.renderer.axisFills.template.disabled = true;
	valueAxis.renderer.minWidth = 0;

	var series = chart.series.push(new am4charts.ConeSeries());
	series.dataFields.categoryX = "country";
	series.dataFields.valueY = "visits";
	series.columns.template.tooltipText = "{valueY.value}";
	series.columns.template.tooltipY = 0;
	series.columns.template.strokeOpacity = 1;

	// as by default columns of the same series are of the same color, we add adapter which takes colors from chart.colors color set
	series.columns.template.adapter.add("fill", function(fill, target) {
		return chart.colors.getIndex(target.dataItem.index);
	});

	series.columns.template.adapter.add("stroke", function(stroke, target) {
		return chart.colors.getIndex(target.dataItem.index);
	});

	//

	var chart2 = am4core.create("chart2div", am4charts.XYChart);

	chart2.data = [{
		"category": "One",
		"value1": 1,
		"value2": 5,
		"value3": 3,
		"value4": 3
	}, {
		"category": "Two",
		"value1": 2,
		"value2": 5,
		"value3": 3,
		"value4": 4
	}, {
		"category": "Three",
		"value1": 3,
		"value2": 5,
		"value3": 4,
		"value4": 4
	}, {
		"category": "Four",
		"value1": 4,
		"value2": 5,
		"value3": 6,
		"value4": 4
	}, {
		"category": "Five",
		"value1": 3,
		"value2": 5,
		"value3": 4,
		"value4": 4
	}, {
		"category": "Six",
		"value1": 8,
		"value2": 7,
		"value3": 10,
		"value4": 4
	}, {
		"category": "Seven",
		"value1": 10,
		"value2": 8,
		"value3": 6,
		"value4": 4
	}]

	chart2.padding(30, 30, 10, 30);
	chart2.legend = new am4charts.Legend();

	chart2.colors.step = 2;

	var categoryAxis = chart2.xAxes.push(new am4charts.CategoryAxis());
	categoryAxis.dataFields.category = "category";
	categoryAxis.renderer.minGridDistance = 60;
	categoryAxis.renderer.grid.template.location = 0;
	categoryAxis.mouseEnabled = false;

	var valueAxis = chart2.yAxes.push(new am4charts.ValueAxis());
	valueAxis.tooltip.disabled = true;
	valueAxis.renderer.grid.template.strokeOpacity = 0.05;
	valueAxis.renderer.minGridDistance = 20;
	valueAxis.mouseEnabled = false;
	valueAxis.min = 0;
	valueAxis.renderer.minWidth = 35;

	var series1 = chart2.series.push(new am4charts.ColumnSeries());
	series1.columns.template.width = am4core.percent(80);
	series1.columns.template.tooltipText = "{name}: {valueY.value}";
	series1.name = "Series 1";
	series1.dataFields.categoryX = "category";
	series1.dataFields.valueY = "value1";
	series1.stacked = true;

	var series2 = chart2.series.push(new am4charts.ColumnSeries());
	series2.columns.template.width = am4core.percent(80);
	series2.columns.template.tooltipText = "{name}: {valueY.value}";
	series2.name = "Series 2";
	series2.dataFields.categoryX = "category";
	series2.dataFields.valueY = "value2";
	series2.stacked = true;

	var series3 = chart2.series.push(new am4charts.ColumnSeries());
	series3.columns.template.width = am4core.percent(80);
	series3.columns.template.tooltipText = "{name}: {valueY.value}";
	series3.name = "Series 3";
	series3.dataFields.categoryX = "category";
	series3.dataFields.valueY = "value3";
	series3.stacked = true;

	var series4 = chart2.series.push(new am4charts.ColumnSeries());
	series4.columns.template.width = am4core.percent(80);
	series4.columns.template.tooltipText = "{name}: {valueY.value}";
	series4.name = "Series 4";
	series4.dataFields.categoryX = "category";
	series4.dataFields.valueY = "value4";
	series4.stacked = true;

	chart2.scrollbarX = new am4core.Scrollbar();

	//


	var chart3 = am4core.create("chart3div", am4charts.PieChart3D);

	chart3.legend = new am4charts.Legend();

	chart3.data = [{
		"country": "Lithuania",
		"litres": 501.9
	}, {
		"country": "Czech Republic",
		"litres": 301.9
	}, {
		"country": "Ireland",
		"litres": 201.1
	}, {
		"country": "Germany",
		"litres": 165.8
	}, {
		"country": "Australia",
		"litres": 139.9
	}, {
		"country": "Austria",
		"litres": 128.3
	}, {
		"country": "UK",
		"litres": 99
	}, {
		"country": "Belgium",
		"litres": 60
	}, {
		"country": "The Netherlands",
		"litres": 50
	}];

	chart3.innerRadius = am4core.percent(40);

	var series = chart3.series.push(new am4charts.PieSeries3D());
	series.dataFields.value = "litres";
	series.dataFields.category = "country";

	//

	var chart4 = am4core.create("chart4div", am4charts.RadarChart);
	chart4.language.locale = window[Messages(MessageKeys.AMCHARTS_LANG)];

	chart4.data = [{
			category: "One",
			startDate1: "2018-01-01",
			endDate1: "2018-03-01"
		},
		{
			category: "One",
			startDate1: "2018-04-01",
			endDate1: "2018-08-15"
		},
		{
			category: "Two",
			startDate2: "2018-03-01",
			endDate2: "2018-06-01"
		},
		{
			category: "Two",
			startDate2: "2018-08-01",
			endDate2: "2018-10-01"
		},
		{
			category: "Three",
			startDate3: "2018-02-01",
			endDate3: "2018-07-01"
		},
		{
			category: "Four",
			startDate4: "2018-06-09",
			endDate4: "2018-09-01"
		},
		{
			category: "Four",
			startDate4: "2018-10-01",
			endDate4: "2019-01-01"
		},
		{
			category: "Five",
			startDate5: "2018-02-01",
			endDate5: "2018-04-15"
		},
		{
			category: "Five",
			startDate5: "2018-10-01",
			endDate5: "2018-12-31"
		}
	];

	chart4.padding(20, 20, 20, 20);
	chart4.colors.step = 2;
	chart4.dateFormatter.inputDateFormat = "YYYY-MM-dd";
	chart4.innerRadius = am4core.percent(40);

	var categoryAxis = chart4.yAxes.push(new am4charts.CategoryAxis());
	categoryAxis.dataFields.category = "category";
	categoryAxis.renderer.labels.template.location = 0.5;
	categoryAxis.renderer.labels.template.horizontalCenter = "right";
	categoryAxis.renderer.grid.template.location = 0;
	categoryAxis.renderer.tooltipLocation = 0.5;
	categoryAxis.renderer.grid.template.strokeOpacity = 0.07;
	categoryAxis.renderer.minGridDistance = 10;
	categoryAxis.mouseEnabled = false;
	categoryAxis.tooltip.disabled = true;

	var dateAxis = chart4.xAxes.push(new am4charts.DateAxis());
	dateAxis.renderer.labels.template.horizontalCenter = "left";
	dateAxis.strictMinMax = true;
	dateAxis.renderer.maxLabelPosition = 0.99;
	dateAxis.renderer.grid.template.strokeOpacity = 0.07;
	dateAxis.min = new Date(2018, 0, 0, 0, 0, 0).getTime();
	dateAxis.max = new Date(2019, 0, 0, 0, 0, 0).getTime();
	dateAxis.mouseEnabled = false;
	dateAxis.tooltip.disabled = true;
	dateAxis.periodChangeDateFormats.setKey("month", dateAxis.language.translate("_date_month"));
	dateAxis
	var series1 = chart4.series.push(new am4charts.RadarColumnSeries());
	series1.name = "Series 1";
	series1.dataFields.openDateX = "startDate1";
	series1.dataFields.dateX = "endDate1";
	series1.dataFields.categoryY = "category";
	series1.clustered = false;
	series1.columns.template.radarColumn.cornerRadius = 30;
	series1.columns.template.tooltipText = "{category}: {openDateX} - {dateX}";

	var series2 = chart4.series.push(new am4charts.RadarColumnSeries());
	series2.name = "Series 2";
	series2.dataFields.openDateX = "startDate2";
	series2.dataFields.dateX = "endDate2";
	series2.dataFields.categoryY = "category";
	series2.clustered = false;
	series2.columns.template.radarColumn.cornerRadius = 30;
	series2.columns.template.tooltipText = "{category}: {openDateX} - {dateX}";

	var series3 = chart4.series.push(new am4charts.RadarColumnSeries());
	series3.name = "Series 3";
	series3.dataFields.openDateX = "startDate3";
	series3.dataFields.dateX = "endDate3";
	series3.dataFields.categoryY = "category";
	series3.clustered = false;
	series3.columns.template.radarColumn.cornerRadius = 30;
	series3.columns.template.tooltipText = "{category}: {openDateX} - {dateX}";

	var series4 = chart4.series.push(new am4charts.RadarColumnSeries());
	series4.name = "Series 4";
	series4.dataFields.openDateX = "startDate4";
	series4.dataFields.dateX = "endDate4";
	series4.dataFields.categoryY = "category";
	series4.clustered = false;
	series4.columns.template.radarColumn.cornerRadius = 30;
	series4.columns.template.tooltipText = "{category}: {openDateX} - {dateX}";

	var series5 = chart4.series.push(new am4charts.RadarColumnSeries());
	series5.name = "Series 5";
	series5.dataFields.openDateX = "startDate5";
	series5.dataFields.dateX = "endDate5";
	series5.dataFields.categoryY = "category";
	series5.clustered = false;
	series5.columns.template.radarColumn.cornerRadius = 30;
	series5.columns.template.tooltipText = "{category}: {openDateX} - {dateX}";

	chart4.seriesContainer.zIndex = -1;

	chart4.scrollbarX = new am4core.Scrollbar();
	chart4.scrollbarY = new am4core.Scrollbar();

	chart4.cursor = new am4charts.RadarCursor();
	chart4.cursor.innerRadius = am4core.percent(40);
	chart4.cursor.lineY.disabled = true;

	var yearLabel = chart4.radarContainer.createChild(am4core.Label);
	yearLabel.text = "2018";
	yearLabel.fontSize = 30;
	yearLabel.horizontalCenter = "middle";
	yearLabel.verticalCenter = "middle";
});