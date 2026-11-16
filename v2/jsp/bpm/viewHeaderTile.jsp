<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<title>
	<fmt:message key="bpm.pageTitle" />
</title>
<link rel="stylesheet" href="<c:url value='/styles/jquery/jquery-ui.css' />" type="text/css" />
<link rel="stylesheet" href="<c:url value='/styles/jquery/jquery-ui.theme.css' />" type="text/css" />
<link rel="stylesheet" href="<c:url value='/styles/varicent.pagination.css' />" type="text/css" />
<script src="<c:url value='/scripts/jquery.min.js' />" type="text/javascript"></script>
<script src="<c:url value='/scripts/jquery-migrate.js' />" type="text/javascript"></script>
<script>
	jQuery.noConflict();
</script>

<script src="<c:url value='/scripts/bpmtokens_dropdown.js' />" type="text/javascript"></script>
<script src="<c:url value='/scripts/bpmwfhistoryview.js' />" type="text/javascript"></script>
<script src="<c:url value='/scripts/popup.js' />" type="text/javascript"></script>
<c:if test="${not empty reporting_content}">
	<script src="<c:url value='/scripts/inquiries.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/report-math.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/reporting.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/bpmreport.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/d3.min.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/d3chart.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/FusionCharts.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/fusioncharts-exporting.js' />" type="text/javascript"></script>

	<style type="text/css">
		$ {
			reporting_styles
		}
	</style>
</c:if>