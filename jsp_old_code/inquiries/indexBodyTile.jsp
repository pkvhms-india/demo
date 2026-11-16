<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="message_center">

	<div class="heading inquiries">
		<fmt:message key="messagecenter.sectionLinks.inquiries" />
	</div>

	<div id="inquiries">

		<c:if test="${!empty msgKey}">
			<p class="error"><fmt:message key="${msgKey}" /></p>
		</c:if>
	
		<div id="counters">
			<div class="stat_col first">
				<div class="stat_cell header"></div>
				<div class="stat_cell open"><fmt:message key="inquiries.status.open"/></div>
				<div class="stat_cell"><fmt:message key="inquiries.status.closed"/></div>
			</div>
			<div class="vertical_separator"></div>
			<div class="stat_col">
				<div class="stat_cell header"><a href="<c:url value="/inquiries/unassigned.html" />"><fmt:message key="inquiries.unassigned.title" /></a></div>
				<div class="stat_cell open"><a href="<c:url value="/inquiries/unassigned.html?statusView=1" />">${inquiries.unassignedInquiriesCountOpen}</a></div>
				<div class="stat_cell"><a href="<c:url value="/inquiries/unassigned.html?statusView=2" />">${inquiries.unassignedInquiriesCount - inquiries.unassignedInquiriesCountOpen}</a></div>
			</div>
			<div class="vertical_separator"></div>
			<div class="stat_col">
				<div class="stat_cell header"><a href="<c:url value="/inquiries/assigned.html" />"><fmt:message key="inquiries.assigned.title" /></a></div>
				<div class="stat_cell open"><a href="<c:url value="/inquiries/assigned.html?statusView=1" />">${inquiries.assignedInquiriesCountOpen}</a></div>
				<div class="stat_cell"><a href="<c:url value="/inquiries/assigned.html?statusView=2" />">${inquiries.assignedInquiriesCount - inquiries.assignedInquiriesCountOpen}</a></div>		
			</div>
			<div class="vertical_separator"></div>
			<div class="stat_col">
				<div class="stat_cell header"><a href="<c:url value="/inquiries/myInquiries.html" />"><fmt:message key="inquiries.myInquiries.title" /></a></div>
				<div class="stat_cell open"><a href="<c:url value="/inquiries/myInquiries.html?statusView=1" />">${inquiries.createdInquiriesCountOpen}</a></div>
				<div class="stat_cell"><a href="<c:url value="/inquiries/myInquiries.html?statusView=2" />">${inquiries.createdInquiriesCount - inquiries.createdInquiriesCountOpen}</a></div>		
			</div>
			<div class="vertical_separator"></div>
			<div class="stat_col last">
				<div class="stat_cell header"><a href="<c:url value="/inquiries/inquiryLog.html" />"><fmt:message key="inquiries.inquiryLog.title" /></a></div>
				<div class="stat_cell open"><a href="<c:url value="/inquiries/inquiryLog.html?statusView=1" />">${inquiries.inquiryLogCountOpen}</a></div>
				<div class="stat_cell"><a href="<c:url value="/inquiries/inquiryLog.html?statusView=2" />">${inquiries.inquiryLogCount - inquiries.inquiryLogCountOpen}</a></div>		
			</div>
		</div>			

		<c:set var="row_limit" scope="page" value="2" />

		<!-- Unassigned Inquiries (assigned to my group but not assigned to anyone)-->		
		<c:set var="title" scope="page"><fmt:message key="inquiries.unassigned.title" /></c:set>
		<c:set var="titleURL" scope="page"><c:url value="/inquiries/unassigned.html" /></c:set>
		<c:set var="inquiryList" scope="page" value="${inquiries.unassignedInquiries}" />
		<c:set var="assigned" scope="page" value="true" />
		<c:set var="subtype" scope="page" value="0"/>
		<c:set var="status" scope="page" value="-1"/>
		<jsp:directive.include file="inquiries.jsp" />
		
		<!-- Assigned Inquiries (assigned to someone in my group) -->
		<c:set var="title" scope="page"><fmt:message key="inquiries.assigned.title" /></c:set>
		<c:set var="titleURL" scope="page"><c:url value="/inquiries/assigned.html" /></c:set>
		<c:set var="inquiryList" scope="page" value="${inquiries.assignedInquiries}" />
		<c:set var="assigned" scope="page" value="true" />
		<c:set var="subtype" scope="page" value="1"/>
		<c:set var="status" scope="page" value="-1"/>
		<jsp:directive.include file="inquiries.jsp" />
		
		<!-- My Inquiries (initiated by me) -->
		<c:set var="title" scope="page"><fmt:message key="inquiries.myInquiries.title" /></c:set>
		<c:set var="titleURL" scope="page"><c:url value="/inquiries/myInquiries.html" /></c:set>
		<c:set var="inquiryList" scope="page" value="${inquiries.createdInquiries}" />
		<c:set var="assigned" scope="page" value="false" />
		<c:set var="subtype" scope="page" value="2"/>
		<c:set var="status" scope="page" value="-1"/>
		<jsp:directive.include file="inquiries.jsp" />
		
		<!-- Inquiry Log (inquiries that I can see, ie anyone "above" me in the tree, and not assigned to me) -->
		<c:set var="title" scope="page"><fmt:message key="inquiries.inquiryLog.title" /></c:set>
		<c:set var="titleURL" scope="page"><c:url value="/inquiries/inquiryLog.html" /></c:set>
		<c:set var="inquiryList" scope="page" value="${inquiries.inquiryLog}" />
		<c:set var="assigned" scope="page" value="false" />
		<c:set var="subtype" scope="page" value="3"/>
		<c:set var="status" scope="page" value="-1"/>
		<jsp:directive.include file="inquiries.jsp" />
		
	</div>
</div>
