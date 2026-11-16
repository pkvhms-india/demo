<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />


	<div id="message_center">

		<div class="heading admin_messages">
			<fmt:message key="messagecenter.sectionLinks.adminMessages" />
		</div>

		<div class="messages">
			<table cellspacing="0" id="messages">
				<tr class="header_row">
					<th class="table_margin">&nbsp;</th>
					<th>
						<fmt:message key="messagecenter.messages.from" />
					</th>
					<th>
						<fmt:message key="messagecenter.messages.summary" />
					</th>
					<th>
						<fmt:message key="messagecenter.messages.date" />
					</th>
					<th class="table_margin">&nbsp;</th>
				</tr>
				<c:choose>
					<c:when test="${!empty messages}">
						<c:forEach items="${messages}" var="message">
							<c:choose>
								<c:when test="${message.category == 'ADMIN_MESSAGE'}">
									<c:url value="/messages/view.html" var="messageURL">
										<c:param name="id" value="${message.id}" />
									</c:url>
								</c:when>
								<c:when test="${message.category == 'ADMIN_PAYEE_MESSAGE'}">
									<c:url value="/messages/viewPayeeNote.html" var="messageURL">
										<c:param name="id" value="${message.id}" />
									</c:url>
								</c:when>
							</c:choose>
							<tr class="message_row" data-csp-click-fn="goToURL" data-csp-click-arg="${messageURL}">
								<td class="table_margin">&nbsp;</td>
								<td>
									<fmt:message key="messagecenter.messages.admin" />
								</td>
								<td><a href="${messageURL}">
										<c:out value="${message.subject}" />
									</a></td>
								<td>
									<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
										value="${message.posted}" />
								</td>
								<td class="table_margin">&nbsp;</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="5" />
							<fmt:message key="messagecenter.noMessagesFound" />
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
		</div>
	</div>

	<script type="text/javascript">
		function goToURL(url) {
			window.location.href = url;
		}
	</script>