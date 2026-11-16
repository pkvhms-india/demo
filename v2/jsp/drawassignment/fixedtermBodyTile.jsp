<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

		<div id="drawassignment">
			<jsp:directive.include file="header.jsp" />

			<div id="fixedterm">

				<div id="multiEditBox">
					<div id="title">
						<fmt:message key="draw.assign.mass.edit" />:
					</div>
					<input class="action_button" id="applyButton" type="button" value="<fmt:message key="
						draw.assign.apply" />" />
					<input class="action_button" id="unassignAllButton" type="button" value="<fmt:message key="
						draw.assign.unassign" />" />
					<span id="massEditDate">
						<fmt:message key="draw.assign.start.date" />: <input type="text" class="datepickerInput"
							id="datepicker" />
					</span>
					<span id="massEditPeriods">
						<fmt:message key="draw.assign.period" />:
					</span>
					<span id="massEditAmount">
						<fmt:message key="draw.assign.amount" />: <input id="massAmount" type="text" />
					</span>
					<span id="massShortenExtend" class="massEditDuration">
						<span id='massEditPeriodCount'>0</span>
						<a href="#"><img id="massShorten" src="<c:url value='/images/remove.png' />"
							align="absMiddle"/></a>
						<a href="#"><img id="massExtend" src="<c:url value='/images/add.png' />"
							align="absMiddle"/></a>
					</span>
				</div>

				<input id="cannotRemoveLocked" type="hidden" value="<fmt:message key="
					draw.assign.cannot.remove.locked" />" />
				<input id="cannotRemovePeriodFixedDuration" type="hidden" value="<fmt:message key="
					draw.assign.cannot.remove.fixed.duration" />" />
				<input id="cannotAddPeriodFixedDuration" type="hidden" value="<fmt:message key="
					draw.assign.cannot.add.period.fixed.duration" />" />
				<input id="cannotAddPeriodNotEnoughPeriod" type="hidden" value="<fmt:message key="
					draw.assign.cannot.add.period.enough.period" />" />
				<input id="notApplicable" type="hidden" value="<fmt:message key='draw.assign.not.applicable' />" />

				<form:form id="fixedtermForm" modelAttribute="command" method="post">

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

					<input id="maxPeriodCountId" type="hidden" value="${command.maxPeriodCount}"></input>
					<table id="fixedtermTable" cellspacing="0" class="assignmentTable">
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
								<th>
									<div id="headerTemplateName" class="sortingLink sortingNone">
										<input type="hidden" value="TemplateName" />
										<fmt:message key="draw.assign.draw.type" />
									</div>
								</th>
								<th class="longHeader">
									<div id="headerStartDate" class="sortingLink sortingNone">
										<input type="hidden" value="StartDate" />
										<fmt:message key="draw.assign.start.period" />
									</div>
								</th>

								<c:forEach begin="1" end="${command.maxPeriodCount}" var="periodIndex">
									<th>
										<fmt:message key="draw.assign.period.i.amount">
											<fmt:param value="${periodIndex}" />
										</fmt:message>
									</th>
								</c:forEach>

								<th>
									<fmt:message key="draw.assign.modify" />/
									<fmt:message key="draw.assign.unassign" />
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${command.fixedTermAssignments}" var="fixedterm"
								varStatus="fixedtermStatus">
								<tr id="row_${fixedtermStatus.index}" <c:choose>
									<c:when test="${fixedtermStatus.index % 2 == 0}">
										class="evenRow"
									</c:when>
									<c:otherwise>
										class="oddRow"
									</c:otherwise>
									</c:choose>
									>
									<form:hidden id="assignmentId_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].assignmentId" />
									<form:hidden id="templateId_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].templateId" />
									<form:hidden id="timeId_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].timeId" />
									<form:hidden id="level_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].level" />
									<form:hidden id="unassigned_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].unassigned" />
									<form:hidden id="rowVersion_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].rowVersion" />
									<form:hidden id="active_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].active" />
									<form:hidden id="defaultAmount_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].defaultAmount" />
									<form:hidden id="fixedAmountId_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].fixedAmount" />
									<form:hidden id="lockedStartDateId_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].lockedStartDate" />
									<form:hidden id="fixedDurationId_${fixedtermStatus.index}"
										path="fixedTermAssignments[${fixedtermStatus.index}].fixedDuration" />

									<td>
										<input id="fixedTermMaxAllowedPeriod_${fixedtermStatus.index}" type="hidden"
											value="${fixedterm.periodAmountCount}" />
										<input id="lastActivePeriod_${fixedtermStatus.index}" type="hidden"
											value="${fixedterm.lastActivePeriodIndex}" />
										<input id="lastLockedPeriod_${fixedtermStatus.index}" type="hidden"
											value="${fixedterm.lastLockedPeriodIndex}" />

										<input id="fixedterm_${fixedtermStatus.index}" type="checkbox"></input>
									</td>
									<td>
										<form:hidden path="fixedTermAssignments[${fixedtermStatus.index}].payeeId" />
										<span id="payeeId_${fixedtermStatus.index}_text">
											<c:out value="${fixedterm.payeeId}" />
										</span>
									</td>
									<td>
										<form:hidden path="fixedTermAssignments[${fixedtermStatus.index}].payeeName" />
										<span id="payeeName_${fixedtermStatus.index}_text">
											<c:out value="${fixedterm.payeeName}" />
										</span>
									</td>
									<td>
										<form:hidden
											path="fixedTermAssignments[${fixedtermStatus.index}].templateName" />
										<span id="templateName_${fixedtermStatus.index}_text">
											<c:out value="${fixedterm.templateName}" />
										</span>
									</td>
									<td>
										<form:hidden id="startPeriod_${fixedtermStatus.index}"
											path="fixedTermAssignments[${fixedtermStatus.index}].startPeriod" />
										<form:hidden id="oldStartDateFormatted_${fixedtermStatus.index}"
											path="fixedTermAssignments[${fixedtermStatus.index}].oldStartDateFormatted" />

										<c:choose>
											<c:when test="${fixedterm.lockedStartDate}">
												<form:hidden id="newStartDateFormatted_${fixedtermStatus.index}"
													path="fixedTermAssignments[${fixedtermStatus.index}].newStartDateFormatted" />
												<input type="text" class="datepickerInput" disabled="disabled"
													value="${fixedterm.newStartDateFormatted}"></input>
											</c:when>
											<c:otherwise>
												<form:input id="newStartDateFormatted_${fixedtermStatus.index}"
													cssClass="datepickerInput"
													path="fixedTermAssignments[${fixedtermStatus.index}].newStartDateFormatted" />
											</c:otherwise>
										</c:choose>

										<span id="startPeriod_${fixedtermStatus.index}_text">
											<c:out value="${fixedterm.startPeriod}" />
										</span>
									</td>
									<c:if test="${command.maxPeriodCount > 0}">
										<c:forEach begin="0" end="${command.maxPeriodCount - 1}" var="periodIndex">
											<td>
												<c:choose>
													<c:when test="${periodIndex < fixedterm.periodAmountCount }">
														<form:hidden
															id="oldActivated_${fixedtermStatus.index}_Period_${periodIndex}"
															path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].oldActivated" />
														<form:hidden
															id="newActivated_${fixedtermStatus.index}_Period_${periodIndex}"
															path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].newActivated" />
														<form:hidden
															id="locked_${fixedtermStatus.index}_Period_${periodIndex}"
															path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].locked" />
														<form:hidden
															id="periodStart_${fixedtermStatus.index}_Period_${periodIndex}"
															path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].periodStart" />
														<form:hidden
															id="oldAmount_${fixedtermStatus.index}_Period_${periodIndex}"
															path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].oldAmount" />
														<form:hidden
															id="rowVersion_${fixedtermStatus.index}_Period_${periodIndex}"
															path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].rowVersion" />

														<span
															id="amountSpan_${fixedtermStatus.index}_Period_${periodIndex}">
															<c:choose>
																<c:when
																	test="${fixedterm.periodAmounts[periodIndex].locked}">
																	<!-- implies old activated -->
																	<form:hidden
																		id="newAmount_${fixedtermStatus.index}_Period_${periodIndex}"
																		path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].newAmount" />
																	<input type="text" disabled="disabled"
																		value="${fixedterm.periodAmounts[periodIndex].newAmount}" />
																</c:when>
																<c:otherwise>
																	<c:if
																		test="${fixedterm.periodAmounts[periodIndex].oldActivated}">
																		<c:choose>
																			<c:when test="${fixedterm.fixedAmount}">
																				<!-- implies old activated -->
																				<form:hidden
																					id="newAmount_${fixedtermStatus.index}_Period_${periodIndex}"
																					path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].newAmount" />
																				<input type="text" disabled="disabled"
																					value="${fixedterm.periodAmounts[periodIndex].newAmount}" />
																			</c:when>
																			<c:otherwise>
																				<form:input
																					id="newAmount_${fixedtermStatus.index}_Period_${periodIndex}"
																					path="fixedTermAssignments[${fixedtermStatus.index}].periodAmounts[${periodIndex}].newAmount" />
																			</c:otherwise>
																		</c:choose>
																	</c:if>
																</c:otherwise>

															</c:choose>

															<c:if
																test="${!fixedterm.periodAmounts[periodIndex].oldActivated}">
																<fmt:message key="draw.assign.not.applicable" />
															</c:if>
														</span>

														<!-- this period is initially inactive -->
													</c:when>
													<c:otherwise>
														<fmt:message key="draw.assign.not.applicable" />
													</c:otherwise>
												</c:choose>
											</td>
										</c:forEach>
									</c:if>
									<td>
										<a href="#"><img id="shorten_${fixedtermStatus.index}" src="<c:url value="
												/images/remove.png" />" /></a>
										<a href="#"><img id="extend_${fixedtermStatus.index}" src="<c:url value="
												/images/add.png" />" /></a>
										<a href="#"><img id="unassign_${fixedtermStatus.index}" src="<c:url value="
												/images/clear.png" />" /></a>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="save">
						<input id="saveButtonFixedterm" class="action_button" type="submit" value="<fmt:message key="
							draw.assign.save" />"></input>
					</div>
				</form:form>
			</div>
			<jsp:directive.include file="page-footer.jsp" />

			<script type="text/javascript"> jQuery.noConflict(); </script>
			<script src="<c:url value='/scripts/jquery-ui.min.js' />" type="text/javascript"></script>
			<tr:datepicker />
			<script type="text/javascript" src="<c:url value='/scripts/drawassignment.js' />"></script>
			<script type="text/javascript" src="<c:url value='/scripts/fixedterm-draws.js' />"></script>

		</div>

		</html>
