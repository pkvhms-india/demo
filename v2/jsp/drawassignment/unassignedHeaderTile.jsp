<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

		<script src="<c:url value='/scripts/jquery.min.js' />" type="text/javascript"></script>
		<script src="<c:url value='/scripts/jquery-migrate.js' />" type="text/javascript"></script>
		<script type="text/javascript"> jQuery.noConflict(); </script>
		<script type="text/javascript" src="<c:url value='/scripts/drawassignment.js' />"></script>
		<script type="text/javascript" src="<c:url value='/scripts/unassigned-draws.js' />"></script>
		<title>
			<fmt:message key="draw.assign.unassign.page.title" />
		</title>
