<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<div id="plan_payee_form">
		<form method="post" name="selector" data-csp-submit-fn="onSubmitForm">
			<c:choose>
				<c:when test="${!(activeTab.compensationPlan || activeTab.tailoredReport)}">
					<spring:bind path="command.planId">
						<select id="plan_select" name="<c:out value=" ${status.expression}" />" data-csp-change-fn="submitForm"
						style="margin-bottom:10px">
						<option value="" <c:if test="${empty status.value}">selected</c:if>>
							<fmt:message key="payeeledger.allPlans" />
						</option>
						<c:forEach items="${plans}" var="plan">
							<option value="<c:out value=" ${plan.id}" />" <c:if test="${status.value == plan.id}">
								selected</c:if>>
							<c:out value="${plan}" />
							</option>
						</c:forEach>
						</select>
					</spring:bind>
				</c:when>
				<c:otherwise>
					<spring:bind path="command.planId">
						<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
						<c:out value="${status.value}" />" />
					</spring:bind>
				</c:otherwise>
			</c:choose>

			<spring:bind path="command.payeeId">
				<select id="payee_select" name="<c:out value=" ${status.expression}" />" <c:if test="${!manualSubmit}">
				 data-csp-change-fn="submitForm"</c:if>>
				<option value="" <c:if test="${empty status.value}">selected</c:if>>
					<fmt:message key="payeeledger.allPayees" />
				</option>
				<c:forEach items="${payees}" var="payee">
					<option value="<c:out value=" ${payee.id}" />" <c:if test="${status.value == payee.id}">selected
					</c:if>>
					<c:out value="${payee}" />
					</option>
				</c:forEach>
				</select>
			</spring:bind>

			<spring:bind path="command.periodIndex">
				<c:if test="${type!='summary'}">
					<select id="period_select" name="${status.expression}" <c:if
						test="${!manualSubmit}">onChange="submitForm();"
				</c:if>>
				<option value="-1" <c:if test="${empty status.value}">selected</c:if>>
					<fmt:message key="payeeledger.allPeriods" />
				</option>
				<c:forEach items="${periods}" var="period" varStatus="loopStatus">
					<option value="<c:out value=" ${loopStatus.index}" />" <c:if
						test="${status.value == loopStatus.index}">selected</c:if>>
					<c:out value="${period.name}" />
					</option>
				</c:forEach>
				</select>
				</c:if>
			</spring:bind>

			<spring:bind path="command.doRetrieveRows">
				<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
				<c:out value="${status.value}" />" />
			</spring:bind>

			<spring:bind path="command.isXLSXExport">
				<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
				<c:out value="${status.value}" />" />
			</spring:bind>

			<spring:bind path="command.token">
				<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
				<c:out value="${status.value}" />" />
			</spring:bind>

			<c:if test="${manualSubmit}">
				<input type="submit" id="button_apply" class="action_button" data-csp-pressed="1"
					value="<fmt:message key='action.apply' />" />
				<c:if test="${!empty rows}">
					<input type="submit" id="button_export_XLS" class="action_button" value="<fmt:message key="
						action.exportToXls" />" />
					<input type="submit" id="button_export_XLSX" class="action_button" value="<fmt:message key="
						action.exportToXlsx" />" />
				</c:if>
			</c:if>
		</form>
	</div>
