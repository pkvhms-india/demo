<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<div id="drawreport">
		<jsp:directive.include file="filter.jsp" />

		<div id="overview">

			<c:if test="${!empty command.selectedTemplate}">
				<div id="export_form">
					<form name="pdfExport" id="pdfExportForm" method="post" enctype="multipart/form-data"
						action="<c:url value='/drawreport/overview/export.do' />">
					<spring:nestedPath path="command">
						<spring:bind path="token">
							<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
							<c:out value="${status.value}" />" />
						</spring:bind>
						<input type="hidden" name="id" value="${command.id}" />

						<div id="chartInfo"></div>
					</spring:nestedPath>

					<input type="hidden" name="exportName" />
					</form>


					<script type="text/javascript">
						window.onload = initialize;

						function initialize() {
							PDFExportMessage = "<fmt:message key="reporting.PDFGeneration" />"

							// regular export
							var form = $('pdfExportForm');

							if (form) {
								var exportAction = $('export-link');
								if (exportAction) {
									exportAction.observe('click', ExportCharts);
								}
							}
						}
					</script>
				</div>

				<div id="charts">
					<div style="text-align:center;">
						<c:if test="${hasLogo}">
							<img src="<c:url value='/drawreport/overview/showImage.do'>
							<c:param name='drawId' value='${command.id}' />
							</c:url>" />
						</c:if>
					</div>

					<table style="width:100%;">
						<tr>
							<td style="width:50%;">
								<div style="text-align:right;">
									<input type="hidden" class="fusion-chart-name" value="onDrawsChart" />
									<div id="Fusion_onDrawsChart" class="fusionchart">
									</div>
								</div>
							</td>
							<script type="text/javascript">
								try {
									var chart1 = new FusionCharts("../charts/Pie2D.swf?chartWidth=550&amp;chartHeight=400", "onDrawsChart", 550, 400, "0", "1");
									chartTitle = RemoveApos("<fmt:message key="draw.report.overview.charts.percentOnDraws.title" />");
									capturing = RemoveApos("<fmt:message key="webreport.fusion.capturing" />");
									chart1.setXMLData(
										decodeURIComponent("<chart exportDialogMessage='" + capturing + "' showBorder='0' bgColor='#FFFFFF' bgAlpha='0' exportAction='save' exportAtClient='0' exportEnabled='1' exportFormats='JPG=JPG' exportShowMenuItem='0' exportHandler='Placeholder' streamlinedData='0' labelPadding='15' caption='" + chartTitle + "' xAxisName='' yAxisName='' labelDisplay='wrap' numberSuffix='%25' decimals='2' showValues='1' decimalSeparator='.' thousandSeparator=',' formatNumberScale='1' slicingDistance='0'><styles><definition><style name='titleFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='xAxisNameFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='yAxisNameFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='xAxisValuesFont' type='font' font='Tahoma' size='9.75' color='000000' bold='0' italic='0' underline='0' /><style name='yAxisValuesFont' type='font' font='Tahoma' size='9.75' color='000000' bold='0' italic='0' underline='0' /></definition><application><apply toObject='Caption' styles='titleFont'  /><apply toObject='XAxisName' styles='xAxisNameFont'  /><apply toObject='YAxisName' styles='yAxisNameFont'  /><apply toObject='DataLabels' styles='xAxisValuesFont'  /><apply toObject='YAxisValues' styles='yAxisValuesFont'  /></application></styles>\
							<set name='<fmt:message key="draw.report.overview.charts.percentOnDraws.onDraws"/>' value='<c:out value="${assignedPercent}" />' /><set name='<fmt:message key="draw.report.overview.charts.percentOnDraws.notOnDraws"/>' value='<c:out value="${100 - assignedPercent}" />' /></chart>"));
									chart1.configure("InvalidXMLText", "<fmt:message key='draw.report.overview.charts.invaliddata'/>");
									chart1.setTransparent(true);

									if (!!chart1._overrideJSChartConfiguration) {
										chart1._overrideJSChartConfiguration(
											{
												xAxis:
												{
													labels:
													{
														align: "center",
														rotation: 0
													}
												}
											});
									}

									chart1.render("Fusion_onDrawsChart");

								} catch (ex) { }
							</script>

							<td style="width:50%;">
								<div style="text-align:left;">
									<input type="hidden" class="fusion-chart-name" value="onDrawsChart2" />
									<div id="Fusion_onDrawsChart2" class="fusionchart">
									</div>
								</div>
							</td>
							<script type="text/javascript">
								try {
									var chart2 = new FusionCharts("../charts/Pie2D.swf?chartWidth=550&amp;chartHeight=400", "onDrawsChart2", 550, 400, "0", "1");
									chartTitle = RemoveApos("<fmt:message key="draw.report.overview.charts.percentOutstanding.title" />");
									capturing = RemoveApos("<fmt:message key="webreport.fusion.capturing" />");
									chart2.setXMLData(
										decodeURIComponent("<chart exportDialogMessage='" + capturing + "' showBorder='0' bgColor='#FFFFFF' bgAlpha='0' exportAction='save' exportAtClient='0' exportEnabled='1' exportFormats='JPG=JPG' exportShowMenuItem='0' exportHandler='Placeholder' streamlinedData='0' labelPadding='15' caption='" + chartTitle + "' xAxisName='' yAxisName='' labelDisplay='wrap' numberSuffix='%25' decimals='2' showValues='1' decimalSeparator='.' thousandSeparator=',' formatNumberScale='1' slicingDistance='0'><styles><definition><style name='titleFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='xAxisNameFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='yAxisNameFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='xAxisValuesFont' type='font' font='Tahoma' size='9.75' color='000000' bold='0' italic='0' underline='0' /><style name='yAxisValuesFont' type='font' font='Tahoma' size='9.75' color='000000' bold='0' italic='0' underline='0' /></definition><application><apply toObject='Caption' styles='titleFont'  /><apply toObject='XAxisName' styles='xAxisNameFont'  /><apply toObject='YAxisName' styles='yAxisNameFont'  /><apply toObject='DataLabels' styles='xAxisValuesFont'  /><apply toObject='YAxisValues' styles='yAxisValuesFont'  /></application></styles>\
							<set name='<fmt:message key="draw.report.overview.charts.percentOutstanding.outstanding"/>' value='<c:out value="${outstandingBalancePercent}"/>' /><set name='<fmt:message key="draw.report.overview.charts.percentOutstanding.notOutstanding"/>' value='<c:out value="${100 - outstandingBalancePercent}" />' /></chart>"));

									chart2.setTransparent(true);

									if (!!chart2._overrideJSChartConfiguration) {
										chart2._overrideJSChartConfiguration(
											{
												xAxis:
												{
													labels:
													{
														align: "center",
														rotation: 0
													}
												}
											});
									}

									chart2.render("Fusion_onDrawsChart2");

								} catch (ex) { }
							</script>

						</tr>
					</table>

					<div style="text-align:center;">
						<input type="hidden" class="fusion-chart-name" value="onDrawsChart3" />
						<div id="Fusion_onDrawsChart3" style="display: block;" class="fusionchart">
						</div>
					</div>
					<script type="text/javascript">
						try {
							var chart3 = new FusionCharts("../charts/Area2D.swf?chartWidth=550&amp;chartHeight=400", "onDrawsChart3", 550, 400, "0", "1");
							chartTitle = RemoveApos("<fmt:message key="draw.report.overview.charts.percentPayout.title" />");
							capturing = RemoveApos("<fmt:message key="webreport.fusion.capturing" />");
							chart3.setXMLData(
								decodeURIComponent("<chart exportDialogMessage='" + capturing + "' showBorder='0' bgColor='#FFFFFF' bgAlpha='0' exportAction='save' exportAtClient='0' exportEnabled='1' exportFormats='JPG=JPG' exportShowMenuItem='0' exportHandler='Placeholder' streamlinedData='0' labelPadding='15' caption='" + chartTitle + "' xAxisName='' yAxisName='' labelDisplay='rotate' numberSuffix='%25' decimals='2' showValues='0' decimalSeparator='.' thousandSeparator=',' formatNumberScale='1' plotFillColor='${command.reportColour}'><styles><definition><style name='titleFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='xAxisNameFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='yAxisNameFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='xAxisValuesFont' type='font' font='Tahoma' size='9.75' color='000000' bold='0' italic='0' underline='0' /><style name='yAxisValuesFont' type='font' font='Tahoma' size='9.75' color='000000' bold='0' italic='0' underline='0' /></definition><application><apply toObject='Caption' styles='titleFont'  /><apply toObject='XAxisName' styles='xAxisNameFont'  /><apply toObject='YAxisName' styles='yAxisNameFont'  /><apply toObject='DataLabels' styles='xAxisValuesFont'  /><apply toObject='YAxisValues' styles='yAxisValuesFont'  /></application></styles>\
						<c:forEach items="${averagePayoutPercents}" var="item">\
							<set name='<c:out value="${item.key}" />' value='<c:out value="${item.value}" />' />\
						</c:forEach>\
					</chart>"));

							chart3.setTransparent(true);

							if (!!chart3._overrideJSChartConfiguration) {
								chart3._overrideJSChartConfiguration(
									{
										xAxis:
										{
											labels:
											{
												align: "center",
												rotation: 0
											}
										}
									});
							}

							chart3.render("Fusion_onDrawsChart3");

						} catch (ex) { }
					</script>

					<div style="text-align:center;">
						<input type="hidden" class="fusion-chart-name" value="onDrawsChart4" />
						<div id="Fusion_onDrawsChart4" style="display: block;" class="fusionchart">
						</div>
					</div>
					<script type="text/javascript">
						try {
							var chart4 = new FusionCharts("../charts/MSColumn2D.swf?chartWidth=900&amp;chartHeight=400", "onDrawsChart4", 550, 400, "0", "1");
							chartTitle = RemoveApos("<fmt:message key="draw.report.overview.charts.drawVsCommission.title" />");
							capturing = RemoveApos("<fmt:message key="webreport.fusion.capturing" />");
							chart4.setXMLData(
								decodeURIComponent("<chart exportDialogMessage='" + capturing + "' showBorder='0' bgColor='#FFFFFF' bgAlpha='0' exportAction='save' exportAtClient='0' exportEnabled='1' exportFormats='JPG=JPG' exportShowMenuItem='0' exportHandler='Placeholder' streamlinedData='0' labelPadding='15' caption='" + chartTitle + "' xAxisName='' yAxisName='' labelDisplay='rotate' numberPrefix='' decimals='2' showValues='0' decimalSeparator='.' thousandSeparator=',' formatNumberScale='1' ><styles><definition><style name='titleFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='xAxisNameFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='yAxisNameFont' type='font' font='Tahoma' size='9.75' color='000000' bold='1' italic='0' underline='0' /><style name='xAxisValuesFont' type='font' font='Tahoma' size='9.75' color='000000' bold='0' italic='0' underline='0' /><style name='yAxisValuesFont' type='font' font='Tahoma' size='9.75' color='000000' bold='0' italic='0' underline='0' /></definition><application><apply toObject='Caption' styles='titleFont'  /><apply toObject='XAxisName' styles='xAxisNameFont'  /><apply toObject='YAxisName' styles='yAxisNameFont'  /><apply toObject='DataLabels' styles='xAxisValuesFont'  /><apply toObject='YAxisValues' styles='yAxisValuesFont'  /></application></styles>\
					<categories> \
					<c:forEach items="${drawsVsCommissions}" var="row"> \
						<category label='<c:out value="${row.values[0]}" />' /> \
					</c:forEach> \
					</categories> \
					<dataset seriesName='<fmt:message key="draw.report.overview.charts.drawVsCommission.draws"/>'>\
						<c:forEach items="${drawsVsCommissions}" var="row">\
							<set value='<c:out value="${row.objects[1]}" />' />\
						</c:forEach>\
					</dataset>\
					<dataset seriesName='<fmt:message key="draw.report.overview.charts.drawVsCommission.commissions"/>'>\
						<c:forEach items="${drawsVsCommissions}" var="row">\
							<set value='<c:out value="${row.objects[2]}" />' />\
						</c:forEach>\
					</dataset>\
					</chart>"));

							chart4.setTransparent(true);

							if (!!chart4._overrideJSChartConfiguration) {
								chart4._overrideJSChartConfiguration(
									{
										xAxis:
										{
											labels:
											{
												align: "center",
												rotation: 0
											}
										}
									});
							}

							chart4.render("Fusion_onDrawsChart4");

						} catch (ex) { }
					</script>

				</div>
			</c:if>
		</div>

	</div>
