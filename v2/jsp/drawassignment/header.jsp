<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<c:if test="${activeTab.module}">
		<div id="moduleDiv">
			<form id="moduleOptions" name="moduleOptions" method="post" action="<c:url value="
				/drawassignment/index.do" />">
			<spring:nestedPath path="command">
				<spring:bind path="drawId">
					<select name="drawId" data-csp-submit-change="1">
						<option value="0" <c:if test="${empty status.value}">selected
	</c:if>>
	<fmt:message key="draw.assign.select.availableDrawAssignments" />
	</option>
	<c:forEach items="${viewableDraws}" var="draw">
		<option value="${draw.id}" <c:if test="${status.value == draw.id}">selected</c:if>>${draw.name}</option>
	</c:forEach>
	</select>
	</spring:bind>
	<spring:bind path="tabId">
		<input type="hidden" name="${status.expression}" value="${activeTab.id}" />
	</spring:bind>
	</spring:nestedPath>
	</form>
	</div>
	</c:if>

	<c:if test="${ command.drawId != 0 }">
		<div id="draw-assignment">
			<div id="filters" class="drawFilter">
				<h3>
					<fmt:message key="draw.assign.select.parameters" />:
				</h3>
				<form method="post" id="filterForm">
					<spring:nestedPath path="command">
						<spring:bind path="drawId">
							<input type="hidden" name="${status.expression}" value="${status.value}" />
						</spring:bind>
						<spring:bind path="tabId">
							<input type="hidden" name="${status.expression}" value="${activeTab.id}" />
						</spring:bind>

						<div class="filterItem">
							<span class="drawFilterLabel">
								<fmt:message key="draw.assign.select.by.payee" />:
							</span>
							<spring:bind path="selectedPayeeId">
								<span class="spanFilterValues">
									<select name="${status.expression}" id="payeeIdSelect">
										<option value="" <c:if test="${empty status.value}">selected
	</c:if>>
	<fmt:message key="draw.assign.select.all.payees" />
	</option>
	<c:forEach items="${filterPayees}" var="payee">
		<option value="${payee.id}" <c:if test="${payee.id == status.value}">selected</c:if>>
			<c:out value="${payee.displayName}" />
		</option>
	</c:forEach>
	</select>
	</span>
	</spring:bind>
	<c:if test="${not empty filterGroups}">
		<span class="drawFilterLabel">
			<fmt:message key="draw.assign.select.by.group" />:
		</span>
		<spring:bind path="selectedGroupId">
			<span class="spanFilterValues">
				<select name="${status.expression}" id="groupIdSelect">
					<option value="0" <c:if test="${status.value == 0}">selected
	</c:if>>
	<fmt:message key="draw.assign.select.all.groups" />
	</option>
	<c:forEach items="${filterGroups}" var="group">
		<option value="${group.id}" <c:if test="${group.id == status.value}">selected</c:if> >
			<c:out value="${group.name}" />
		</option>
	</c:forEach>
	</select>
	</span>
	</spring:bind>
	</c:if>
	</div>
	<div class="action_item">
		<input class="action_button" type="submit" value="<fmt:message key='draw.assign.apply' />"></input>
	</div>
	</spring:nestedPath>
	</form>
	</div>
	</div>

	<c:url var="unassigned_url" scope="page" value="unassigned.html">
		<c:if test="${!empty command.drawId}">
			<c:param name="id" value="${command.drawId}" />
		</c:if>
		<c:if test="${!empty activeTab.id}">
			<c:param name="tabId" value="${activeTab.id}" />
		</c:if>
		<c:if test="${!empty command.selectedPayeeId}">
			<c:param name="selectedPayeeId" value="${command.selectedPayeeId}" />
		</c:if>
		<c:if test="${command.selectedGroupId != -1}">
			<c:param name="selectedGroupId" value="${command.selectedGroupId}" />
		</c:if>
	</c:url>
	<c:url var="ongoing_url" scope="page" value="ongoing.html">
		<c:if test="${!empty command.drawId}">
			<c:param name="id" value="${command.drawId}" />
		</c:if>
		<c:if test="${!empty activeTab.id}">
			<c:param name="tabId" value="${activeTab.id}" />
		</c:if>
		<c:if test="${!empty command.selectedPayeeId}">
			<c:param name="selectedPayeeId" value="${command.selectedPayeeId}" />
		</c:if>
		<c:if test="${command.selectedGroupId != -1}">
			<c:param name="selectedGroupId" value="${command.selectedGroupId}" />
		</c:if>
	</c:url>
	<c:url var="fixedTerm_url" scope="page" value="fixedterm.html">
		<c:if test="${!empty command.drawId}">
			<c:param name="id" value="${command.drawId}" />
		</c:if>
		<c:if test="${!empty activeTab.id}">
			<c:param name="tabId" value="${activeTab.id}" />
		</c:if>
		<c:if test="${!empty command.selectedPayeeId}">
			<c:param name="selectedPayeeId" value="${command.selectedPayeeId}" />
		</c:if>
		<c:if test="${command.selectedGroupId != -1}">
			<c:param name="selectedGroupId" value="${command.selectedGroupId}" />
		</c:if>
	</c:url>

	<div id="drawTabsDiv">
		<div class="tabHolder">
			<ul id="drawTabs">
				<li id="unassignedTab">
					<p><a href="${unassigned_url}">
							<fmt:message key="draw.assign.unassgined" />
						</a></p>
				</li>
				<li id="fixedtermTab">
					<p><a href="${fixedTerm_url}">
							<fmt:message key="draw.assign.fixedterm" />
						</a></p>
				</li>
				<li id="ongoingTab">
					<p><a href="${ongoing_url}">
							<fmt:message key="draw.assign.ongoing" />
						</a></p>
				</li>
			</ul>
			<ul id="drawTabplaceholder"></ul>
		</div>
	</div>

	</c:if>
