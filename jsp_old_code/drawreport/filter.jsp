<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<c:if test="${activeTab.module}" >
	<div id="moduleDiv" >
		<form id="moduleOptions" name="moduleOptions" method="post" action="<c:url value="/drawreport/index.do" />">
			<spring:nestedPath path="command">
				<spring:bind path="id" >
					<span class="spanFilterValues">				
					<select name="id" data-csp-submit-change="1">
						<option value="" <c:if test="${empty status.value}">selected</c:if>><fmt:message key="draw.report.filter.availableDrawReports"/></option>
						<c:forEach items="${viewableDraws}" var="draw">
							<option value="${draw.id}" <c:if test="${status.value == draw.id}">selected</c:if>>${draw.name}</option>
						</c:forEach>
					</select>
					</span>
				</spring:bind>
				
				<spring:bind path="tabId" >
					<input type="hidden" name="${status.expression}" value="${activeTab.id}" />
				</spring:bind>
			</spring:nestedPath>
		</form>
	</div>
</c:if>

<c:if test="${! empty command.id}">

<div id="filterDiv" class="drawFilter">
	<h3> <fmt:message key="draw.report.filter.title"/> </h3>
	
	<!--previously selected values, for paging etc. -->
	<input id="drawId" type="hidden" value="<c:out value="${command.id}"/>" />
	<input id="selectedPayeeId" type="hidden" value="<c:out value="${command.selectedPayeeId}"/>" />
	<input id="selectedGroupId" type="hidden" value="<c:out value="${command.selectedGroupId}"/>" />
	<input id="selectedPeriod" type="hidden" value="<c:out value="${command.selectedPeriod}"/>" />
	<!-- 'old' because the one to be submitted is also manipulated by JS -->
	<input id="oldSelectedPeriodLevel" type="hidden" value="<c:out value="${command.selectedPeriodLevel}"/>" />
	<input id="selectedTemplate" type="hidden" value="<c:out value="${command.selectedTemplate}"/>" />
	
	<form id="filterOptions" name="filterOptions"  method="post">
		<spring:nestedPath path="command">
			
			<spring:bind path="id" >
				<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
			</spring:bind>
			
			<spring:bind path="tabId" >
				<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${activeTab.id}"/>" />
			</spring:bind>
			
			<spring:bind path="token" >
				<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
			</spring:bind>
			
			<div>
				<div class="filterItem">		
					<span class="drawFilterLabel"><fmt:message key="draw.report.filter.select.by.payee"/></span>
					<spring:bind path="selectedPayeeId" >
						<span class="spanFilterValues">
						<select id="payeeIdSelect" name="${status.expression}">
							<option value="" <c:if test="${empty status.value}">selected</c:if>><fmt:message key="draw.report.filter.allPayees"/></option> 
							<c:forEach items="${payees}" var="payee" varStatus="varStatus">
								<option 
									value="<c:out value="${payee.id}" />"
									<c:if test="${status.value == payee.id}">selected</c:if>
								>
								<c:out value="${payee.displayName}" /></option>
							</c:forEach>
						</select>
						</span>
					</spring:bind>
					<c:if test="${! empty groups}">
						<span class="drawFilterLabel"><fmt:message key="draw.report.filter.select.by.group"/></span>
						<spring:bind path="selectedGroupId" >
							<span class="spanFilterValues">
							<select id="groupIdSelect" name="selectedGroupId">
								<option value="" <c:if test="${empty command.selectedGroupId}">selected</c:if> ><fmt:message key="draw.report.filter.allGroups"/></option> 
								<c:forEach items="${groups}" var="group" varStatus="varStatus">
									<option 
										value="<c:out value="${group.id}" />"
										<c:if test="${command.selectedGroupId == group.id}">selected</c:if>
									>
										<c:out value="${group.name}" /></option>
								</c:forEach>
							</select>
							</span>
						</spring:bind>
					</c:if>
				</div>
						
				<div class="filterItem">
					<span class="drawFilterLabel"><fmt:message key="draw.report.filter.calendar"/></span>
					<span><c:out value="${calendar}"/></span>
				</div>
					
				<div class="filterItem">
					<span class="drawFilterLabel"><fmt:message key="draw.report.filter.select.draw"/></span>
					<spring:bind path="selectedTemplate">
						<span class="spanFilterValues">					
						<select id="templateSelect" name="selectedTemplate">
							<option value="" <c:if test="${empty command.selectedTemplate}">selected</c:if> ></option>
							<c:forEach items="${templates}" var="template" varStatus="varStatus">
								<option 
									value="<c:out value="${template.id}" />"
									<c:if test="${command.selectedTemplate == template.id}">selected</c:if>
									>
								<c:out value="${template.name}" /></option>
							</c:forEach>
						</select>
						</span>
					</spring:bind>
					<span  class="drawFilterLabel"><fmt:message key="draw.report.filter.period"/></span>
					
					<spring:bind path="selectedPeriodLevel" >
						<input type="hidden" id="selectedPeriodLevel" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
					</spring:bind>
					
					<spring:bind path="selectedPeriod">
						<div style="display:none" id="periodLevels">
							<c:forEach items="${periods}" var="period" varStatus="varStatus">
								<input type="hidden" id="periodLevels_${varStatus.index}" value="<c:out value="${period.level}"/>" />
							</c:forEach>
						</div>
						
						<span class="spanFilterValues">						
						<select id="periodSelect" name="selectedPeriod">
							<option value="" <c:if test="${empty command.selectedPeriod}">selected</c:if> ><fmt:message key="draw.report.filter.allPeriods"/></option>
							<c:forEach items="${periods}" var="period" varStatus="varStatus">
								<option 
									value="<c:out value="${period.name}" />"
									<c:if test="${command.selectedPeriod == period.name && command.selectedPeriodLevel == period.level}">selected</c:if>
									>
								<c:out value="${period.name}" /></option>
							</c:forEach>
						</select>
						</span>
					</spring:bind>
				</div>
			</div>
				
			<div style="clear:both; float:none">
				<!-- apply -->
				<input type="submit" class="action_button" value="<fmt:message key="action.apply"/>"/>
			</div>
					
		</spring:nestedPath>
	</form>
</div>

<div id="drawTabsDiv">
	<ul id="drawTabs">
		<li id="overviewTab">
			<p><a id="overviewLink" href="#"><fmt:message key="draw.report.overview.tabTitle"/></a></p>
		</li>
		<li id="assignmentsTab">
			<p><a id="assignmentsLink" href="#" ><fmt:message key="draw.report.assignments.tabTitle"/></a></p>
		</li>
		<li id="ytdpayoutTab">
			<p><a id="ytdPayoutLink" href="#" ><fmt:message key="draw.report.ytdpayout.tabTitle"/></a></p>
		</li>
		<li id="periodpayoutTab">
			<p><a id="periodPayoutLink" href="#" ><fmt:message key="draw.report.periodpayout.tabTitle"/></a></p>
		</li>
	</ul>
	<ul id="drawTabplaceholder"></ul>
</div>

</c:if>
