<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<div class="messages">

		<h2>
			<a href="${titleURL}" class="inquiriesTableHeader">
				${title}
				<c:if test="${!empty subtitle}"> &mdash; ${subtitle}</c:if>
			</a>
		</h2>

		<h3>
			<c:if test="${!empty row_limit && !empty inquiryList && fn:length(inquiryList) gt 0}">
				<c:choose>
					<c:when test="${ fn:length(inquiryList) gt row_limit}">
						<fmt:message key="inquiries.displayingXofY">
							<fmt:param value="${row_limit}" />
							<fmt:param value="${fn:length(inquiryList)}" />
						</fmt:message>
					</c:when>
					<c:otherwise>
						<fmt:message key="inquiries.displayingXofY">
							<fmt:param value="${fn:length(inquiryList)}" />
							<fmt:param value="${fn:length(inquiryList)}" />
						</fmt:message>
					</c:otherwise>
				</c:choose>
			</c:if>
		</h3>

		<table cellspacing="0" class="tablesorter">
			<thead>
				<tr class="header_row">
					<th width="10%">
						<fmt:message key="inquiries.id" />
					</th>
					<th width="10%" class="sorter-false">
						<fmt:message key="inquiries.status" />
					</th>
					<th width="10%" class="sorter-false">
						<fmt:message key="inquiries.category" />
					</th>
					<th width="20%" class="sorter-false">
						<fmt:message key="inquiries.object" />
					</th>
					<th width="10%" class="sorter-false">
						<fmt:message key="inquiries.author" />
					</th>
					<th width="10%" class="sorter-false">
						<fmt:message key="inquiries.assignee" />
					</th>
					<th width="10%" class="sorter-shortDate">
						<fmt:message key="inquiries.created" />
					</th>
					<th width="10%" class="sorter-shortDate">
						<fmt:message key="inquiries.updated" />
					</th>
					<th width="10%" class="sorter-false"></th>
				</tr>
			</thead>
			<tbody>
				<c:set var="row_count" scope="page" value="0" />

				<c:if test="${!empty inquiryList}">
					<c:forEach items="${inquiryList}" var="inquiry">

						<c:if test="${empty row_limit || (!empty row_limit && row_count < row_limit) }">
							<c:if
								test="${ empty view || (inquiry.status == 'OPEN' && view == 1) || ((inquiry.status =='CLOSED' || inquiry.status =='FIXED' || inquiry.status =='INVALID') && view == 2) }">
								<tr class="message_row<c:if test=" ${inquiry.isNew}">_new
							</c:if>">
							<td>${inquiry.id}</td>
							<td>
								<c:choose>
									<c:when test="${inquiry.status == 'OPEN'}">
										<div id="status_icon" class="open">
											<div id="status_icon_gradient">
												<fmt:message key="inquiries.status.open" />
											</div>
										</div>
									</c:when>
									<c:when test="${inquiry.status == 'INVALID'}">
										<div id="status_icon" class="closed">
											<div id="status_icon_gradient">
												<fmt:message key="inquiries.status.invalid" />
											</div>
										</div>
									</c:when>
									<c:when test="${inquiry.status == 'FIXED'}">
										<div id="status_icon" class="closed">
											<div id="status_icon_gradient">
												<fmt:message key="inquiries.status.fixed" />
											</div>
										</div>
									</c:when>
									<c:otherwise>
										<div id="status_icon" class="closed">
											<div id="status_icon_gradient">
												<fmt:message key="inquiries.status.closed" />
											</div>
										</div>
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${empty categories[inquiry.category]}">
										<fmt:message key="${defaultCategories[inquiry.category].key}" />
									</c:when>
									<c:otherwise>
										<c:out value="${categories[inquiry.category]}" />
									</c:otherwise>
								</c:choose>
							</td>
							<td><a href="<c:url value='${inquiry.object.url}' />">
								<c:out value="${inquiry.object}" /></a>
							</td>
							<td>
								<c:choose>
									<c:when test="${empty inquiry.creator.id}">
										<fmt:message key="messagecenter.messages.deletedUser" />
									</c:when>
									<c:otherwise>
										<c:out value="${inquiry.creator}" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${empty inquiry.assignee}">
										<fmt:message key="inquiries.assignee.none" />
									</c:when>
									<c:otherwise>
										<c:out value="${inquiry.assignee}" />
									</c:otherwise>
								</c:choose>
							</td>
							<td>
								<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
									pattern="MM/dd/yy hh:mm a" value="${inquiry.created}" />
							</td>
							<td>
								<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
									pattern="MM/dd/yy hh:mm a" value="${inquiry.updated}" />
							</td>

							<td class="<c:if test=" ${inquiry.status=='OPEN' && assigned=='true' }">
								inquiry_action_buttons
						</c:if>">
						<div class="actions">
							<div class="action_item">
								<form method="post" action="<c:url value='/inquiries/view.do' />">
								<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />" />
								<input type="hidden" name="subtype" value="<c:out value=" ${subtype}" />" />
								<input type="hidden" name="status" value="<c:out value=" ${status}" />" />
								<input type="hidden" name="token" value="<c:out value=" ${inquiry.token}" />" />
								<input type="submit" class="action_button" value="<fmt:message key='inquiries.view' />"
								/>

								</form>
							</div>
							<c:if test="${inquiry.status == 'OPEN' && assigned=='true'}">

								<c:set var="userInInquiryWorkflowGroup" scope="page" value="false" />
								<c:forEach items="${currentUserWorkflowGroups}" var="groupID">
									<c:if test="${inquiry.disputeAssignedToGroupID == groupID}">
										<c:set var="userInInquiryWorkflowGroup" scope="page" value="true" />
									</c:if>
								</c:forEach>

								<c:if test="${userInInquiryWorkflowGroup == 'true'}">
									<div class="action_item">
										<c:choose>
											<c:when test="${empty inquiry.assignee}">
												<form method="post" action="<c:url value='/inquiries/assign.do' />">
												<input type="hidden" name="id" value="<c:out value="
													${inquiry.id}" />"/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" class="action_button" value="<fmt:message key="
													inquiries.assign" />" />
												</form>
											</c:when>
											<c:otherwise>
												<form method="post" action="<c:url value='/inquiries/unassign.do' />">
												<input type="hidden" name="id" value="<c:out value="
													${inquiry.id}" />"/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" class="action_button" value="<fmt:message key="
													inquiries.unassign" />" />
												</form>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="action_item">
										<form method="post" action="<c:url value='/inquiries/escalate.do' />">
										<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
										<input type="hidden" name="token" value="<c:out value=" ${inquiry.token}" />" />
										<input type="submit" class="action_button" value="<fmt:message key="
											inquiries.escalate" />" />
										</form>
									</div>
									<div class="action_item">
										<form method="post" action="<c:url value='/inquiries/return.do' />">
										<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
										<input type="hidden" name="token" value="<c:out value=" ${inquiry.token}" />" />
										<input type="submit" class="action_button" value="<fmt:message key="
											inquiries.return" />" />
										</form>
									</div>
								</c:if>
							</c:if>
						</div>
						</td>
						</tr>
						<c:set var="row_count" scope="page" value="${row_count + 1}" />
				</c:if>
				</c:if>

				</c:forEach>
				</c:if>

				<c:if test="${row_count == 0}">
					<tr>
						<td colspan="9" />
						<fmt:message key="inquiries.noInquiriesFound" />
					</tr>
				</c:if>

			</tbody>
		</table>
	</div>
	<div id="spacer"></div>
