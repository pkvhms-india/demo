<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<c:if test="${!empty message}">

		<c:choose>
			<c:when test="${navMsg.nextOlder.category == 'ADMIN_MESSAGE'}">
				<c:url value="/messages/view.html" var="prevUrl">
					<c:param name="id" value="${navMsg.nextOlder.id}" />
					<c:param name="type" value="${navMsg.listType}" />
				</c:url>
			</c:when>
			<c:when test="${navMsg.nextOlder.category == 'ADMIN_PAYEE_MESSAGE'}">
				<c:url value="/messages/viewPayeeNote.html" var="prevUrl">
					<c:param name="id" value="${navMsg.nextOlder.id}" />
					<c:param name="type" value="${navMsg.listType}" />
				</c:url>
			</c:when>
			<c:when
				test="${(navMsg.nextOlder.category == 'INQUIRY_RETURNED') || (navMsg.nextOlder.category == 'INQUIRY_OPEN') || (navMsg.nextOlder.category == 'INQUIRY_ESCALATED') || (navMsg.nextOlder.category == 'INQUIRY_RESUBMITTED') || (navMsg.nextOlder.category == 'INQUIRY_COMMENTED') || (navMsg.nextOlder.category == 'INQUIRY_CLOSED')}">
				<c:url value="/inquiries/view.do" var="prevUrl">
					<c:param name="id" value="${navMsg.nextOlder.id}" />
					<c:param name="type" value="${navMsg.listType}" />
				</c:url>
			</c:when>
		</c:choose>
		<c:choose>
			<c:when test="${navMsg.nextNewer.category == 'ADMIN_MESSAGE'}">
				<c:url value="/messages/view.html" var="nextUrl">
					<c:param name="id" value="${navMsg.nextNewer.id}" />
					<c:param name="type" value="${navMsg.listType}" />
				</c:url>
			</c:when>
			<c:when test="${navMsg.nextNewer.category == 'ADMIN_PAYEE_MESSAGE'}">
				<c:url value="/messages/viewPayeeNote.html" var="nextUrl">
					<c:param name="id" value="${navMsg.nextNewer.id}" />
					<c:param name="type" value="${navMsg.listType}" />
				</c:url>
			</c:when>
			<c:when
				test="${(navMsg.nextNewer.category == 'INQUIRY_RETURNED') || (navMsg.nextNewer.category == 'INQUIRY_OPEN') || (navMsg.nextNewer.category == 'INQUIRY_ESCALATED') || (navMsg.nextNewer.category == 'INQUIRY_RESUBMITTED') || (navMsg.nextNewer.category == 'INQUIRY_COMMENTED') || (navMsg.nextNewer.category == 'INQUIRY_CLOSED')}">
				<c:url value="/inquiries/view.do" var="nextUrl">
					<c:param name="id" value="${navMsg.nextNewer.id}" />
					<c:param name="type" value="${navMsg.listType}" />
				</c:url>
			</c:when>
		</c:choose>

		<div id="view_admin_message">

			<table id="message_container" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<td>
							<table id="message_header" cellpadding="0" cellspacing="0">
								<tbody>
									<tr>
										<td id="message_subject">
											<c:out value="${message.subject}" />
										</td>
										<td id="message_paging">
											<c:choose>
												<c:when test="${empty navMsg.nextOlder}">
													<div class="nav_btn_nopr"></div>
												</c:when>
												<c:otherwise>
													<a class="nav_btn_prev" href="${prevUrl}"></a>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${empty navMsg.nextNewer}">
													<div class="nav_btn_nonx"></div>
												</c:when>
												<c:otherwise>
													<a class="nav_btn_next" href="${nextUrl}"></a>
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td id="message_sender">
											<fmt:message key="messages.author" />:&nbsp;<span>
												<c:out value="${message.admin.name}" />
											</span>
										</td>
										<td id="message_date">
											<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
												value="${message.posted}" />
										</td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td id="message_body">
							<div id="message_body_div">
								<c:forEach var="msg" items="${message.body}">
									<c:out value="${msg}" />
									<br />
								</c:forEach>
							</div>
						</td>
					</tr>
				</tbody>
			</table>

			<div id="btnBack" class="btnPaging" data-csp-back="1">
				<img src="<c:url value='/images/arrow_prev.png' />" style="padding-right: 5px" /><span>
					<fmt:message key="messagecenter.navigation.back" />
				</span>
			</div>
		</div>
	</c:if>
	<script src="<c:url value='/scripts/message.js' />" type="text/javascript"></script>
