<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<script src="<c:url value='/scripts/jquery.min.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/jquery-migrate.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/drawreport.js' />" type="text/javascript"></script>
	<script type="text/javascript">
		jQuery.noConflict();
		jQuery(document).ready(function ($) {
			$('#filterOptions').attr("action", "overview.do");
		});
	</script>
	<title>
		<fmt:message key="draw.report.pageTitle" />
	</title>
