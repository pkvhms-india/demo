<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />


<div id="message_center">

	<div class="heading inbox">
		<fmt:message key="messagecenter.sectionLinks.inbox" />
	</div>

	<div class="messages">

		<c:set var="row_count" scope="page" value="0" />

		<table cellspacing="0" id="messages">

			<tr class="header_row">
				<th class="table_margin">&nbsp;</th>
				<th><fmt:message key="messagecenter.messages.from" /></th>
				<th><fmt:message key="messagecenter.messages.category" /></th>
				<th><fmt:message key="messagecenter.messages.summary" /></th>
				<th><fmt:message key="messagecenter.messages.date" /></th>
				<th class="table_margin">&nbsp;</th>
			</tr>

			<c:forEach items="${command.messages}" var="message">

				<c:set var="is_new" scope="page" value="${message.isNew}" />
				
				<tr class="message_row<c:if test="${is_new}">_new</c:if>">
					<td class="table_margin">&nbsp;</td>
					<c:choose>
						<c:when test="${message.category == 'ADMIN_MESSAGE'}">
							<td>
								<fmt:message key="messagecenter.messages.admin" />
							</td>
							<td><div class="label_category category_admin"><span class="category_text"><fmt:message key="messagecenter.messages.category.admin" /></span></div></td>							
							<td>
								<c:url value="/messages/view.html" var="messageURL">
									  <c:param name="id"   value="${message.id}" />
								</c:url>
								<a href="${messageURL}">
									<fmt:message key="messagecenter.messages.summary.adminNote">
										<fmt:param value="${message.summary}" />
									</fmt:message>
								</a>
							</td>
							<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${message.date}" /></td>
						</c:when>
						<c:when test="${message.category == 'ADMIN_PAYEE_MESSAGE'}">
							<td>
								<fmt:message key="messagecenter.messages.admin" />
							</td>
							<td><div class="label_category category_admin"><span class="category_text"><fmt:message key="messagecenter.messages.category.admin" /></span></div></td>
							<td>
								<c:url value="/messages/viewPayeeNote.html" var="messageURL">
									  <c:param name="id"   value="${message.id}" />
								</c:url>
								<a href="${messageURL}">
									<fmt:message key="messagecenter.messages.summary.adminNote">
										<fmt:param value="${message.summary}" />
									</fmt:message>
								</a>
							</td>
							<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${message.date}" /></td>
						</c:when>
						<c:when test="${message.category == 'DOCUMENT'}">
							<td>
								<c:choose>
									<c:when test="${message.from != null}">
										(<c:out value="${message.from}"/>) <c:out value="${message.summary}" />
									</c:when>
									<c:otherwise>
										<fmt:message key="messagecenter.messages.admin" />
									</c:otherwise>
								</c:choose>
							</td>
							<td><div class="label_category category_doc"><span class="category_text"><fmt:message key="messagecenter.messages.category.doc" /></span></div></td>							
							<td>
								<c:url value="/documents/download.do" var="downloadURL">
										  <c:param name="id"   value="${message.id}" />
								</c:url>
								<a href="${downloadURL}">
									<fmt:message key="messagecenter.messages.summary.document">
										<fmt:param value="${message.summary}" />
									</fmt:message>
								</a>
							</td>
							<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${message.date}" /></td>
						</c:when>
						<c:when test="${message.category == 'SIGNOFF'}">
							<td>
								<c:choose>
									<c:when test="${message.from != null}">
										(<c:out value="${message.from}"/>) <c:out value="${message.summary}" />
									</c:when>
									<c:otherwise>
										<fmt:message key="messagecenter.messages.admin" />
									</c:otherwise>
								</c:choose>
							</td>
							<td><div class="label_category category_signoff"><span class="category_text"><fmt:message key="messagecenter.messages.category.signoff" /></span></div></td>							
							<td>
								<a href="<c:url value="/workflow/index.html" />">
									<fmt:message key="messagecenter.messages.summary.signoff">
										<fmt:param value="${message.summary}" />
									</fmt:message>
								</a>
							</td>
							<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${message.date}" /></td>
						</c:when>
						<c:when test="${message.category == 'INQUIRY_OPEN' || message.category == 'INQUIRY_RESUBMITTED' || message.category == 'INQUIRY_RETURNED' || message.category == 'INQUIRY_ESCALATED' || message.category == 'INQUIRY_COMMENTED' || message.category == 'INQUIRY_CLOSED'}">
							<td>
								<c:choose>
									<c:when test="${message.from != null}">
										(<c:out value="${message.from}"/>) <c:out value="${message.summary}" />
									</c:when>
									<c:otherwise>
										<fmt:message key="messagecenter.messages.admin" />
									</c:otherwise>
								</c:choose>
							</td>
							<td><div class="label_category category_inquiry"><span class="category_text"><fmt:message key="messagecenter.messages.category.inquiry" /></span></div></td>
							<td>
								<a href="<c:url value="/inquiries/view.do">
										<c:param name="id" value="${message.id}" />
									</c:url>">
									<c:choose>
										<c:when test="${message.category == 'INQUIRY_OPEN'}">
											<c:choose>
												<c:when test="${message.from != null}">
													<c:choose>
														<c:when test="${message.summary == null }">
															<fmt:message key="messagecenter.messages.summary.inquiriesSubmittedByDeletedUser">
																<fmt:param value="${message.from}" />
																<fmt:param value="${message.id}" />
															</fmt:message>
														</c:when>
														<c:otherwise>
															<fmt:message key="messagecenter.messages.summary.inquiriesSubmitted">
																<fmt:param value="${message.from}" />
																<fmt:param value="${message.summary}" />
																<fmt:param value="${message.id}" />
															</fmt:message>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<fmt:message key="messagecenter.messages.summary.adminInquiriesSubmitted">
														<fmt:param value="${message.id}" />
													</fmt:message>
												</c:otherwise>
											</c:choose>
										</c:when>
										<c:when test="${message.category == 'INQUIRY_RESUBMITTED'}">
											<c:choose>
												<c:when test="${message.from != null}">
													<c:choose>
														<c:when test="${message.summary == null }">
															<fmt:message key="messagecenter.messages.summary.inquiriesResubmittedByDeletedUser">
																<fmt:param value="${message.from}" />
																<fmt:param value="${message.id}" />
															</fmt:message>
														</c:when>
														<c:otherwise>
															<fmt:message key="messagecenter.messages.summary.inquiriesResubmitted">
																<fmt:param value="${message.from}" />
																<fmt:param value="${message.summary}" />
																<fmt:param value="${message.id}" />
															</fmt:message>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<fmt:message key="messagecenter.messages.summary.adminInquiriesResubmitted">
														<fmt:param value="${message.id}" />
													</fmt:message>
												</c:otherwise>
											</c:choose>
											
										</c:when>
										<c:when test="${message.category == 'INQUIRY_RETURNED'}">
											<fmt:message key="messagecenter.messages.summary.inquiriesReturned">
												<fmt:param value="${message.id}" />
											</fmt:message>
										</c:when>										
										<c:when test="${message.category == 'INQUIRY_ESCALATED'}">
											<fmt:message key="messagecenter.messages.summary.inquiriesEscalated">
												<fmt:param value="${message.id}" />
											</fmt:message>
										</c:when>										
										<c:when test="${message.category == 'INQUIRY_COMMENTED'}">
											<fmt:message key="messagecenter.messages.summary.inquiriesCommented">
												<fmt:param value="${message.id}" />
											</fmt:message>
										</c:when>										
										<c:when test="${message.category == 'INQUIRY_CLOSED'}">
											<fmt:message key="messagecenter.messages.summary.inquiriesClosed">
												<fmt:param value="${message.id}" />
											</fmt:message>
										</c:when>										
									</c:choose>
								</a>
							</td>
							<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${message.date}" /></td>
						</c:when>
					</c:choose>
					<td class="table_margin">&nbsp;</td>
				</tr>

				<c:set var="row_count" scope="page" value="${row_count + 1}" />

			</c:forEach>
			
			<c:if test="${row_count == 0}">
				<tr><td colspan="6"/><fmt:message key="messagecenter.noMessagesFound" /></tr>
			</c:if>
			
		</table>

	</div>

	<br/>

	<div id="paging">
		<div id="btnPrev" class="btnPaging" data-csp-nav="<c:url value='${command.prevUrl}' />">
			<img src="<c:url value="/images/arrow_prev.png" />" /><span><fmt:message key="messagecenter.navigation.previous" /></span>
		</div>
		<c:if test="${command.isNextButtonVisible}">
			<div id="btnNext" class="btnPaging" data-csp-nav="<c:url value='${command.nextUrl}' />">
				<span><fmt:message key="messagecenter.navigation.next" /></span><img src="<c:url value="/images/arrow_next.png" />" />
			</div>
		</c:if>
	</div>

	<div>
		<fmt:message key="messagecenter.navigation.dateRange">
			<fmt:param><fmt:formatDate dateStyle="short" value="${command.startDate}" /></fmt:param>
			<fmt:param><fmt:formatDate dateStyle="short" value="${command.endDate}" /></fmt:param>
		</fmt:message>
		<br>
		<fmt:message key="messagecenter.lastCalcTime" />:&nbsp;
		<c:choose>
			<c:when test="${!empty lastCalcTime}">
				<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${lastCalcTime}" />
			</c:when>
			<c:otherwise>
				<fmt:message key="messagecenter.lastCalcTimeNever" />
			</c:otherwise>
		</c:choose>
	</div>

</div>

