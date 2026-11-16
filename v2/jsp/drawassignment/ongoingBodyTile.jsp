<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

		<div id="drawassignment">
			<jsp:directive.include file="header.jsp" />

			<div id="ongoing">
				<div id="multiEditBox">
					<div id="title">
						<fmt:message key="draw.assign.mass.edit" />:
					</div>
					<input class="action_button" id="applyButton" type="button" value="<fmt:message key="
						draw.assign.apply" />" />
					<input class="action_button" id="unassignAllButton" type="button" value="<fmt:message key="
						draw.assign.unassign" />" />
					<span id="massEditDate">
						<fmt:message key="draw.assign.start.date" />: <input class="datepickerInput" type="text"
							id="datepicker" />
					</span>
					<span id="massEditPeriods">
						<fmt:message key="draw.assign.draw.amount" />: <input type="text" id="amount" />
					</span>
				</div>

				<form:form id="ongoingForm" modelAttribute="command" method="post">

					<form:hidden id="selectedPayeeId" path="selectedPayeeId"></form:hidden>
					<form:hidden id="selectedGroupId" path="selectedGroupId"></form:hidden>
					<form:hidden id="drawId" path="drawId"></form:hidden>
					<form:hidden id="workflowObjectId" path="workflowObjectId"></form:hidden>

					<spring:bind path="tabId">
						<input type="hidden" name="${status.expression}" value="${activeTab.id}" />
					</spring:bind>

					<div style="clear:both;">
						<div id="hideInactivePayees">
							<form:hidden path="clientLocale"></form:hidden>
							<form:hidden id="pageNumber" path="pageNumber"></form:hidden>
							<form:hidden path="totalPages"></form:hidden>
							<form:hidden id="sortColumn" path="sortColumn"></form:hidden>
							<form:hidden id="descending" path="descending"></form:hidden>
							<form:hidden path="rowsPerPage"></form:hidden>
							<form:checkbox id="checkbox_hideInactivePayees" path="hideInactivePayees" />
							<fmt:message key="draw.assign.hide.inactive.payees" />
						</div>
					</div>

					<spring:bind path="command.token">
						<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
						<c:out value="${status.value}" />" />
					</spring:bind>

					<spring:hasBindErrors name="command">
						<spring:bind path="command.*">
							<div id="errors">
								<c:forEach items="${status.errorMessages}" var="error">
									<p class="error">
										<c:out value="${error}" />
									</p>
								</c:forEach>
							</div>
						</spring:bind>
					</spring:hasBindErrors>

					<table id="ongoingTable" cellspacing="0" class="assignmentTable">
						<thead>
							<tr>
								<th class="checkBoxHeader">
									<input type="checkbox" id="headerCheckbox"></input>
								</th>
								<th>
									<div id="headerPayeeID" class="sortingLink sortingNone">
										<input type="hidden" value="PayeeID" />
										<fmt:message key="draw.assign.id" />
									</div>
								</th>
								<th>
									<div id="headerPayeeName" class="sortingLink sortingNone">
										<input type="hidden" value="PayeeName" />
										<fmt:message key="draw.assign.payee" />
									</div>
								</th>
								<th class="longHeader">
									<div id="headerTemplateName" class="sortingLink sortingNone">
										<input type="hidden" value="TemplateName" />
										<fmt:message key="draw.assign.draw.type" />
									</div>
								</th>
								<th>
									<div id="headerStartDate" class="sortingLink sortingNone">
										<input type="hidden" value="StartDate" />
										<fmt:message key="draw.assign.start.period" />
									</div>
								</th>
								<th>
									<div id="headerEndDate" class="sortingLink sortingNone">
										<input type="hidden" value="EndDate" />
										<fmt:message key="draw.assign.end.date" />
									</div>
								</th>
								<th>
									<div id="headerAmount" class="sortingLink sortingNone">
										<input type="hidden" value="Amount" />
										<fmt:message key="draw.assign.draw.amount" />
									</div>
								</th>
								<th>
									<div id="headerUnassign">
										<input type="hidden" value="Unassign" />
										<fmt:message key="draw.assign.unassign" />
									</div>
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.ongoingAssignments}" var="ongoing" varStatus="ongoingStatus">
								<tr id="row_${ongoingStatus.index}" <c:choose>
									<c:when test="${ongoingStatus.index % 2 == 0}">
										class="evenRow"
									</c:when>
									<c:otherwise>
										class="oddRow"
									</c:otherwise>
									</c:choose>
									>
									<form:hidden id="assignmentId_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].assignmentId" />
									<form:hidden id="templateId_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].templateId" />
									<form:hidden id="timeId_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].timeId" />
									<form:hidden id="level_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].level" />
									<form:hidden id="unassigned_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].unassigned" />
									<form:hidden id="rowVersion_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].rowVersion" />
									<form:hidden id="activeId_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].active" />
									<form:hidden id="lockedStartDateId_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].lockedStartDate" />
									<form:hidden id="fixedAmountId_${ongoingStatus.index}"
										path="ongoingAssignments[${ongoingStatus.index}].fixedAmount" />

									<td>
										<input id="ongoing_${ongoingStatus.index}" type="checkbox" <c:if
											test="${!ongoing.active}"> disabled="disabled"</c:if>></input>
									</td>
									<td>
										<form:hidden id="payeeId_${ongoingStatus.index}"
											path="ongoingAssignments[${ongoingStatus.index}].payeeId" />
										<span id="payeeId_${ongoingStatus.index}_text">
											<c:out value="${ongoing.payeeId}" />
										</span>
									</td>
									<td>
										<form:hidden id="payeeName_${ongoingStatus.index}"
											path="ongoingAssignments[${ongoingStatus.index}].payeeName" />
										<span id="payeeName_${ongoingStatus.index}_text">
											<c:out value="${ongoing.payeeName}" />
										</span>
									</td>
									<td>
										<form:hidden path="ongoingAssignments[${ongoingStatus.index}].templateName" />
										<span id="templateName_${ongoingStatus.index}_text">
											<c:out value="${ongoing.templateName}" />
										</span>
									</td>
									<td>
										<form:hidden id="startPeriod_${ongoingStatus.index}"
											path="ongoingAssignments[${ongoingStatus.index}].startPeriod" />
										<form:hidden id="oldStartDateFormatted_${ongoingStatus.index}"
											path="ongoingAssignments[${ongoingStatus.index}].oldStartDateFormatted" />

										<c:choose>
											<c:when test="${ongoing.lockedStartDate}">
												<form:hidden id="newStartDateFormatted_${ongoingStatus.index}"
													path="ongoingAssignments[${ongoingStatus.index}].newStartDateFormatted" />
												<input type="text" class="datepickerInput" disabled="disabled"
													value="${ongoing.newStartDateFormatted}"></input>
											</c:when>
											<c:otherwise>
												<form:input id="newStartDateFormatted_${ongoingStatus.index}"
													cssClass="datepickerInput"
													path="ongoingAssignments[${ongoingStatus.index}].newStartDateFormatted" />
											</c:otherwise>
										</c:choose>

										<span id="startPeriod_${ongoingStatus.index}_text" class="startPeriod">
											<c:out value="${ongoing.startPeriod}" />
										</span>
									</td>
									<td>
										<form:hidden
											path="ongoingAssignments[${ongoingStatus.index}].endDateFormatted" />
										<span id="endDate_${ongoingStatus.index}_text">
											<c:out value="${ongoing.endDateFormatted}" />
										</span>
									</td>
									<td>
										<form:hidden id="oldAmount_${ongoingStatus.index}_text"
											path="ongoingAssignments[${ongoingStatus.index}].oldAmount" />

										<c:choose>
											<c:when test="${ongoing.fixedAmount}">
												<form:hidden id="newAmount_${ongoingStatus.index}"
													path="ongoingAssignments[${ongoingStatus.index}].newAmount" />
												<input type="text" disabled="disabled"
													value="${ongoing.newAmount}"></input>
											</c:when>
											<c:otherwise>
												<form:input id="newAmount_${ongoingStatus.index}"
													path="ongoingAssignments[${ongoingStatus.index}].newAmount" />
											</c:otherwise>
										</c:choose>
									</td>
									<td>
										<div class="action_item">
											<a href="#"><img id="unassign_${ongoingStatus.index}"
													class="assignmentImage" src="<c:url value='/images/clear.png' />"
												/></a>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="save">
						<input class="action_button" id="saveButtonOngoing" type="submit" value="<fmt:message key="
							draw.assign.save" />"></input>
					</div>
				</form:form>
			</div>
			<jsp:directive.include file="page-footer.jsp" />

			<script type="text/javascript"> jQuery.noConflict(); </script>
			<script src="<c:url value='/scripts/jquery-ui.min.js' />" type="text/javascript"></script>
			<tr:datepicker />
			<script type="text/javascript" src="<c:url value='/scripts/drawassignment.js' />"></script>
			<script type="text/javascript" src="<c:url value='/scripts/ongoing-draws.js' />"></script>
		</div>
