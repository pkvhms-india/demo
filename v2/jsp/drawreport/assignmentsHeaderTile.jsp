<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<title>
		<fmt:message key="draw.report.pageTitle" />
	</title>
	<script src="<c:url value='/scripts/jquery.min.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/jquery-migrate.js' />" type="text/javascript"></script>
	<script type="text/javascript"> jQuery.noConflict(); </script>
	<script src="<c:url value='/scripts/drawreport.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/drawreport-tables.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/drawreport-assignments.js' />" type="text/javascript"></script>

	<style type="text/css">
		.showHideColImgDivOpen {
			border-right: 2px solid $ {
				command.reportColour
			}

			;

			border-left: 1px solid $ {
				command.reportColour
			}

			;

			border-top: 1px solid $ {
				command.reportColour
			}

			;
			border-bottom: 1px solid white;
			background-color: #FFF;
		}
	</style>
