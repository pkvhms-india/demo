<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	(c) Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<title><fmt:message key="reporting.pageTitle" /></title>
	
	<link rel="stylesheet" href="<c:url value="/styles/jquery/jquery-ui.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="/styles/varicent.pagination.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="/styles/varicent.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="/styles/compplanmanager.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="/styles/dataTables.css" />" type="text/css" />

	<script src="<c:url value="/scripts/jquery.min.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/jquery-migrate.js" />" type="text/javascript"></script>

	<script>
		jQuery.noConflict();
	</script>

	<script src="<c:url value="/scripts/form.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/inquiries.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/report-math.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/reporting.js" />" type="text/javascript"></script>

	<script src="<c:url value="/scripts/FusionCharts.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/fusioncharts-exporting.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/compplanmanager/d3.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/compplanmanager/transactionReport.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/compplanmanager/earningsReport.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/compplanmanager/pulseGraphProperties.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/compplanmanager/performanceReport.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/compplanmanager/rankingReport.js" />" type="text/javascript"></script>
