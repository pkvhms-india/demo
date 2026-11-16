<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
	<script src="<c:url value='/scripts/jquery.tablesorter.min.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/jquery.metadata.js' />" type="text/javascript"></script>
	<script src="<c:url value='/scripts/inquiries-table.js' />" type="text/javascript"></script>
	<script>
		document.observe('dom:loaded', function () {
			initInquiryTable();
		});
	</script>
	<div id="message_center">

		<div class="heading inquiries">
			<fmt:message key="messagecenter.sectionLinks.inquiries" />
		</div>

		<div id="inquiries">
			<c:if test="${!empty msgKey}">
				<p class="error">
					<fmt:message key="${msgKey}" />
				</p>
			</c:if>

			<!-- Assigned Inquiries (assigned to someone in my group) -->
			<c:set var="title" scope="page">
				<fmt:message key="inquiries.assigned.title" />
			</c:set>
			<c:set var="titleURL" scope="page">
				<c:url value="/inquiries/assigned.html" />
			</c:set>
			<c:choose>
				<c:when test="${statusView == 1}">
					<c:set var="subtitle" scope="page">
						<fmt:message key="inquiries.status.open" />
					</c:set>
					<c:set var="status" scope="page" value="${statusView}" />
				</c:when>
				<c:when test="${statusView == 2 }">
					<c:set var="subtitle" scope="page">
						<fmt:message key="inquiries.status.closed" />
					</c:set>
					<c:set var="status" scope="page" value="${statusView}" />
				</c:when>
				<c:otherwise>
					<c:set var="status" scope="page" value="-1" />
				</c:otherwise>
			</c:choose>
			<c:set var="inquiryList" scope="page" value="${inquiries.assignedInquiries}" />
			<c:set var="selectedList" scope="page" value="ASSIGNED" />
			<c:set var="view" scope="page" value="${statusView}" />
			<c:set var="assigned" scope="page" value="true" />
			<c:set var="subtype" scope="page" value="1" />

			<jsp:directive.include file="submenu.jsp" />
			<jsp:directive.include file="inquiries.jsp" />

		</div>

	</div>
