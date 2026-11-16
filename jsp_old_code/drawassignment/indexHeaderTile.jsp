<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<script src="<c:url value="/scripts/jquery.min.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/jquery-migrate.js" />" type="text/javascript"></script>
	<script type="text/javascript" > jQuery.noConflict(); </script>
	<script type="text/javascript" src="<c:url value="/scripts/drawassignment.js" />"></script>
	<script type="text/javascript" src="<c:url value="/scripts/unassigned-draws.js" />"></script>
	<title><fmt:message key="draw.assign.pageTitle"/></title>