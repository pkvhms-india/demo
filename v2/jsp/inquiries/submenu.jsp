<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<script src="<c:url value='/scripts/inquiry-dropdown.js' />" type="text/javascript"></script>

	<div id="submenu_container">
		<div id="submenu">
			<ul>
				<li class="<c:if test=" ${selectedList=='UNASSIGNED' }">selected</c:if>">
					<div class="submenu_text_container"><a href="<c:url value='/inquiries/unassigned.html' />">
						<fmt:message key="inquiries.unassigned.title" /></a>
					</div>
					<div class="status_dropdown_closed"></div>
					<div class="status_dropdown_open">
						<div class="status_dropdown_top"></div>
						<div class="status_dropdown_main">
							<div class="status_icon<c:if test=" ${selectedList=='UNASSIGNED' && view==1}"> on</c:if>"
							 data-csp-nav="<c:url value='/inquiries/unassigned.html?statusView=1' />"><div
									id="status_icon_gradient">
									<fmt:message key="inquiries.status.open" />
								</div>
							</div>
							<div class="status_icon<c:if test=" ${selectedList=='UNASSIGNED' && view==2}"> on</c:if>"
							 data-csp-nav="<c:url value='/inquiries/unassigned.html?statusView=2' />"><div
									id="status_icon_gradient">
									<fmt:message key="inquiries.status.closed" />
								</div>
							</div>
						</div>
					</div>
				</li>
				<li class="<c:if test=" ${selectedList=='ASSIGNED' }">selected</c:if>">
					<div class="submenu_text_container"><a href="<c:url value='/inquiries/assigned.html' />">
						<fmt:message key="inquiries.assigned.title" /></a>
					</div>
					<div class="status_dropdown_closed"></div>
					<div class="status_dropdown_open">
						<div class="status_dropdown_top"></div>
						<div class="status_dropdown_main">
							<div class="status_icon<c:if test=" ${selectedList=='ASSIGNED' && view==1}"> on</c:if>"
							 data-csp-nav="<c:url value='/inquiries/assigned.html?statusView=1' />"><div
									id="status_icon_gradient">
									<fmt:message key="inquiries.status.open" />
								</div>
							</div>
							<div class="status_icon<c:if test=" ${selectedList=='ASSIGNED' && view==2}"> on</c:if>"
							 data-csp-nav="<c:url value='/inquiries/assigned.html?statusView=2' />"><div
									id="status_icon_gradient">
									<fmt:message key="inquiries.status.closed" />
								</div>
							</div>
						</div>
					</div>
				</li>
				<li class="<c:if test=" ${selectedList=='MYINQUIRIES' }">selected</c:if>">
					<div class="submenu_text_container"><a href="<c:url value='/inquiries/myInquiries.html' />">
						<fmt:message key="inquiries.myInquiries.title" /></a>
					</div>
					<div class="status_dropdown_closed"></div>
					<div class="status_dropdown_open">
						<div class="status_dropdown_top"></div>
						<div class="status_dropdown_main">
							<div class="status_icon<c:if test=" ${selectedList=='MYINQUIRIES' && view==1}"> on</c:if>"
							 data-csp-nav="<c:url value='/inquiries/myInquiries.html?statusView=1' />"><div
									id="status_icon_gradient">
									<fmt:message key="inquiries.status.open" />
								</div>
							</div>
							<div class="status_icon<c:if test=" ${selectedList=='MYINQUIRIES' && view==2}"> on</c:if>"
							 data-csp-nav="<c:url value='/inquiries/myInquiries.html?statusView=2' />"><div
									id="status_icon_gradient">
									<fmt:message key="inquiries.status.closed" />
								</div>
							</div>
						</div>
					</div>
				</li>
				<li class="<c:if test=" ${selectedList=='INQUIRYLOG' }">selected</c:if>">
					<div class="submenu_text_container"><a href="<c:url value='/inquiries/inquiryLog.html' />">
						<fmt:message key="inquiries.inquiryLog.title" /></a>
					</div>
					<div class="status_dropdown_closed"></div>
					<div class="status_dropdown_open">
						<div class="status_dropdown_top"></div>
						<div class="status_dropdown_main">
							<div class="status_icon<c:if test=" ${selectedList=='INQUIRYLOG' && view==1}"> on</c:if>"
							 data-csp-nav="<c:url value='/inquiries/inquiryLog.html?statusView=1' />"><div
									id="status_icon_gradient">
									<fmt:message key="inquiries.status.open" />
								</div>
							</div>
							<div class="status_icon<c:if test=" ${selectedList=='INQUIRYLOG' && view==2}"> on</c:if>"
							 data-csp-nav="<c:url value='/inquiries/inquiryLog.html?statusView=2' />"><div
									id="status_icon_gradient">
									<fmt:message key="inquiries.status.closed" />
								</div>
							</div>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
