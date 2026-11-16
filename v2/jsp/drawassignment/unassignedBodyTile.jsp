<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


		<div id="drawassignment">
			<jsp:directive.include file="header.jsp" />
			<div id="unassigned">

				<div id="selectTemplateBox">
					<select id="Selected_Template">
						<c:if test="${not empty availableTemplates}">
							<c:forEach items="${availableTemplates}" var="template">
								<option title="<c:out value=" ${template.name}" />" value="${template.id}">
								<c:out value="${template.name}" />
								</option>
							</c:forEach>
						</c:if>
					</select>
					<div class="action_item">
						<input class="action_button" id="apply" type="button" value="<fmt:message key="
							draw.assign.apply" />" />
					</div>
				</div>

				<form:form modelAttribute="command" method="post" action="unassigned/assign.do">
					<form:hidden id="selectedPayeeId" path="selectedPayeeId"></form:hidden>
					<form:hidden id="selectedGroupId" path="selectedGroupId"></form:hidden>
					<form:hidden id="drawId" path="drawId"></form:hidden>
					<form:hidden id="workflowObjectId" path="workflowObjectId"></form:hidden>

					<form:hidden id="pageNumber" path="pageNumber"></form:hidden>
					<form:hidden path="totalPages"></form:hidden>
					<form:hidden id="sortColumn" path="sortColumn"></form:hidden>
					<form:hidden id="descending" path="descending"></form:hidden>
					<form:hidden path="rowsPerPage"></form:hidden>

					<spring:bind path="tabId">
						<input type="hidden" name="${status.expression}" value="${activeTab.id}" />
					</spring:bind>

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

					<table id="unassignedTable" cellspacing="0" class="assignmentTable">
						<thead>
							<tr>
								<th>
									<input type="checkbox" id="headerCheckbox"></input>
								</th>
								<th>
									<div id="headerPayeeID" class="sortingLink sortingNone">
										<input type="hidden" value="PayeeID" />
										<fmt:message key="draw.assign.id" />
									</div>
								</th>
								<th>
									<div id="headerName" class="sortingLink sortingNone">
										<input type="hidden" value="Name" />
										<fmt:message key="draw.assign.payee" />
									</div>
								</th>
								<th>
									<div id="headerTitle" class="sortingLink sortingNone">
										<input type="hidden" value="Title" />
										<fmt:message key="draw.assign.title" />
									</div>
								</th>
								<th>
									<div id="headerParent" class="sortingLink sortingNone">
										<input type="hidden" value="Parent" />
										<fmt:message key="draw.assign.parent" />
									</div>
								</th>
								<th>
									<div id="headerReportsTo" class="sortingLink sortingNone">
										<input type="hidden" value="ReportsTo" />
										<fmt:message key="draw.assign.reports.to" />
									</div>
								</th>
								<th>
									<div id="headerDate_Of_Hire" class="sortingLink sortingNone">
										<input type="hidden" value="Date_Of_Hire" />
										<fmt:message key="draw.assign.date.of.hire" />
									</div>
								</th>
								<th>
									<fmt:message key="draw.assign.assign.to" />
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.unassignedPayees}" var="payee" varStatus="payeeStatus">

								<tr id="row_${payeeStatus.index}" <c:choose>
									<c:when test="${payeeStatus.index % 2 == 0}">
										class="evenRow"
									</c:when>
									<c:otherwise>
										class="oddRow"
									</c:otherwise>
									</c:choose>
									>
									<td>
										<input type="checkbox" id="payee_${payeeStatus.index}"></input>
									</td>
									<td>
										<form:hidden id="payeeId_${payeeStatus.index}"
											path="unassignedPayees[${payeeStatus.index}].id" />
										<span id="payeeId_${payeeStatus.index}_text">
											<c:out value="${payee.id}" />
										</span>
									</td>
									<td>
										<form:hidden id="payeeName_${payeeStatus.index}"
											path="unassignedPayees[${payeeStatus.index}].name" />
										<span id="payeeName_${payeeStatus.index}_text">
											<c:out value="${payee.name}" />
										</span>
									</td>
									<td>
										<form:hidden id="title_${payeeStatus.index}"
											path="unassignedPayees[${payeeStatus.index}].title" />
										<span id="title_${payeeStatus.index}_text">
											<c:out value="${payee.title}" />
										</span>
									</td>
									<td>
										<form:hidden id="payeeParent_${payeeStatus.index}"
											path="unassignedPayees[${payeeStatus.index}].parent" />
										<span id="payeeParent_${payeeStatus.index}_text">
											<c:out value="${payee.parent}" />
										</span>
									</td>
									<td>
										<form:hidden id="payeeReportsTo_${payeeStatus.index}"
											path="unassignedPayees[${payeeStatus.index}].reportsTo" />
										<span id="payeeReportsTo_${payeeStatus.index}_text">
											<c:out value="${payee.reportsTo}" />
										</span>
									</td>
									<td>
										<form:hidden id="payeeDateOfHire_${payeeStatus.index}"
											path="unassignedPayees[${payeeStatus.index}].dateOfHireFormatted" />
										<span id="payeeDateOfHire_${payeeStatus.index}_text">
											<c:out value="${payee.dateOfHireFormatted}" />
										</span>
									</td>
									<td>
										<form:hidden id="templateId_${payeeStatus.index}"
											path="unassignedPayees[${payeeStatus.index}].templateId" />
										<span id="templateId_${payeeStatus.index}_text"></span>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="save">
						<input class="action_button" type="submit" value="<fmt:message key="
							draw.assign.save" />"></input>
					</div>
				</form:form>

			</div>

			<jsp:directive.include file="unassignedFooter.jsp" />
			<div id="navigation_tab" class="nav">
				<button id="navigation_previous" class="action_button">
					<img src="<c:url value='/images/arrow_prev.png' />" >
					<span>
						<fmt:message key="draw.report.assignments.nav.previous" />
					</span>
				</button>
				<button id="navigation_next" class="action_button">
					<span>
						<fmt:message key="draw.report.assignments.nav.next" />
					</span>
					<img src="<c:url value='/images/arrow_next.png' />" >
				</button>
			</div>
		</div>
