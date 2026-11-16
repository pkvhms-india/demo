<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<div id="message_center">

		<div class="heading tasks_signoff">
			<fmt:message key="workflow.signoff" />
		</div>

		<div class="messages">
			<table>
				<tbody>
					<tr class="header_row">
						<th class="table_margin">&nbsp;</th>
						<th class="signoff_column">
							<fmt:message key="workflow.signoff" />
						</th>
						<th class="object_column">
							<fmt:message key="workflow.signoff.object" />
						</th>
						<th class="started_column">
							<fmt:message key="workflow.signoff.started" />
						</th>
						<th class="from_column">
							<fmt:message key="workflow.signoff.from" />
						</th>
						<th class="approve_column"></th>
						<th class="table_margin">&nbsp;</th>
					</tr>
					<c:choose>
						<c:when test="${!empty signoffs}">
							<c:forEach items="${signoffs}" var="signoff">
								<tr class="message_row">
									<td class="table_margin">&nbsp;</td>
									<td>
										<c:out value="${signoff.name}" />
									</td>
									<td><a href="<c:url value='${signoff.object.url}' />">
										<c:out value="${signoff.object}" /></a>
									</td>
									<td>
										<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
											value="${signoff.start}" />
									</td>
									<td>
										<div class="from_column_cell">
											<c:choose>
												<c:when test="${!empty signoff.signees}">
													<c:forEach items="${signoff.signees}" var="signee">
														<c:out value="${signee}" /><br />
													</c:forEach>
												</c:when>
												<c:otherwise>
													<fmt:message key="workflow.signoff.admin" />
												</c:otherwise>
											</c:choose>
										</div>
									</td>
									<td>
										<div class="approve_container">
											<form method="post" action="<c:url value='/workflow/approve.do' />">
											<input type="hidden" name="id" value="<c:out value=" ${signoff.id}" />" />
											<input type="hidden" name="nodeId" value="<c:out value="
												${signoff.nodeId}" />" />
											<input type="hidden" name="token" value="${token}" />
											<input type="submit" class="approve_button" value="<fmt:message key="
												action.approve" />" />
											</form>
										</div>
									</td>
									<td class="table_margin">&nbsp;</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="7" />
								<fmt:message key="workflow.signoff.noSignoffs" />
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>

		<div id="tasks_spacer"></div>

		<div class="heading tasks_signofflog">
			<fmt:message key="workflow.signoffLog.title" />
		</div>

		<div class="messages">
			<table>
				<tbody>
					<tr class="header_row">
						<th class="table_margin">&nbsp;</th>
						<th class="signoff_column">
							<fmt:message key="workflow.signoff" />
						</th>
						<th class="object_column">
							<fmt:message key="workflow.signoff.object" />
						</th>
						<th class="started_column">
							<fmt:message key="workflow.signoff.started" />
						</th>
						<th class="approved_column">
							<fmt:message key="workflow.signoff.approved" />
						</th>
						<th class="table_margin">&nbsp;</th>
					</tr>
					<c:choose>
						<c:when test="${!empty signoffLog}">
							<c:forEach items="${signoffLog}" var="signoff">
								<tr class="message_row">
									<td class="table_margin">&nbsp;</td>
									<td>
										<c:out value="${signoff.name}" />
									</td>
									<td><a href="<c:url value='${signoff.object.url}' />">
										<c:out value="${signoff.object}" /></a>
									</td>
									<td>
										<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
											value="${signoff.start}" />
									</td>
									<td>
										<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
											value="${signoff.end}" />
									</td>
									<td class="table_margin">&nbsp;</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="6" />
								<fmt:message key="workflow.signoff.noSignoffs" />
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</div>

	</div>
