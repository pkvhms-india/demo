<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<title><fmt:message key="reporting.pageTitle" /></title>
	
	<link rel="stylesheet" href="<c:url value="/styles/jquery/jquery-ui.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="/styles/jquery/jquery-ui.theme.css" />" type="text/css" />
	<link rel="stylesheet" href="<c:url value="/styles/varicent.pagination.css" />" type="text/css" />

	<script src="<c:url value="/scripts/jquery.min.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/jquery-migrate.js" />" type="text/javascript"></script>
	
	<script>
		jQuery.noConflict();
	</script>
	
	<script src="<c:url value="/scripts/form.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/inquiries.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/report-math.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/reporting.js" />" type="text/javascript"></script>
	
	<script src="<c:url value="/scripts/d3.min.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/d3chart.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/FusionCharts.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/fusioncharts-exporting.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/rgbcolor.js" />" type="text/javascript"></script>
	<script src="<c:url value="/scripts/canvg.js" />" type="text/javascript"></script>

	<style type="text/css">
		<%-- The children of <style> elements, like <script>, are implicitly CDATA,
		so entity references are not allowed. We could unescape all of them,
		but it doesn't appear that anything other than "&" is used here.
		escapeXml is still necessary to avoid script injection. --%>
		${fn:replace(fn:escapeXml(styles), "&amp;", "&")}
	</style>