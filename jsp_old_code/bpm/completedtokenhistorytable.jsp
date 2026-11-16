<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
<div id="bpm_token_history">
	<div class="heading bpm">
		<fmt:message key="messagecenter.sectionLinks.bpm" />
	</div>
	<div id="bpm_content" class="completed_history_workinfo">
	<table id="bpm_workflow_info">
		<tr>
			<th class="workflow_info_cell backbutton_container" rowspan="2">
				<c:set var="backbutton_href" value="completedtokenhistorylistpage.html" />
				<div class="button" data-csp-nav="${backbutton_href}"><fmt:message key="bpm.links.completedwfsnocount" /></div>							
			</th>
			
			<th class="workflow_info_cell backbutton_container" rowspan="2">
				<c:set var="backbutton_href" value="tokenlist.html" />
				<div class="button" data-csp-nav="${backbutton_href}"><fmt:message key="bpm.backtolisting" /></div>							
			</th>
			
			<th class="workflow_info_cell report_name_column" ><fmt:message key="bpm.reportname" /></th>
			<th class="workflow_info_cell submitted_by_column"><fmt:message key="bpm.submittedby" /></th>
			<th class="workflow_info_cell workflow_info_last_cell date_column"><fmt:message key="bpm.lastmodified" /></th>			
		</tr>
		<tr>
			<!-- Report Name -->
			<c:choose>
				<c:when test="${not empty reportname}">
					<td class="workflow_info_cell" title="${reportname}">
						<c:out value="${reportname}" />
					</td>
				</c:when>
				<c:otherwise>
					<td class="workflow_info_cell"></td>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${not empty submitby}">
					<td class="workflow_info_cell" title="${submitby}">
						<c:out value="${submitby}" />
					</td>
				</c:when>
				<c:otherwise>
					<td class="workflow_info_cell"></td>
				</c:otherwise>
			</c:choose>
			<td class="workflow_info_cell workflow_info_last_cell">
				<c:if test="${not empty submitdate}">
					<fmt:formatDate value="${submitdate}" type="date" dateStyle="long"  />
				</c:if>
			</td>
		</tr>
	</table>
	</div>
	<c:if test="${empty error}">
	<table id="bpm_completed_tokenhistory_content" class="tokens bpm_table history_content_table completed">
				<tr class="header_row">
					<th colspan="5" id="workflow_history_header"><fmt:message key="bpm.workflow.history.bpmHistoryHeader" >
					<fmt:param value="${BPMWorkflowName}" ></fmt:param>
					</fmt:message></th>
				</tr>
				<c:forEach items="${BPMHistory}" var="token" varStatus="rowCount">
					<tr class="token_row">
						<td colspan ="4"  class="token_action_cell">
							<c:if test="${not empty token.forceByWithStyle}">
								<span><fmt:message key="bpm.workflow.history.forcedby">
								<fmt:param value="${token.actionWithStyle}"></fmt:param>
								<fmt:param value="${token.actorWithStyle}"></fmt:param>
								<fmt:param value="${token.forceByWithStyle}"></fmt:param>
								</fmt:message>
								</span>
							</c:if>
						
							<c:if test="${empty token.forceByWithStyle}">
								<span><fmt:message key="bpm.workflow.history.notforcedby">
								<fmt:param value="${token.actionWithStyle}"></fmt:param>
								<fmt:param value="${token.actorWithStyle}"></fmt:param>
								</fmt:message>
								</span>
							</c:if>
						
							<div class="token_actionname_cell"><c:out value="${token.nodeName}"></c:out></div>						
						</td>
						<td  class="token_actionDate_cell">
							<fmt:formatDate pattern="MM/dd/yyyy hh:mm:ss a" dateStyle="long" timeStyle="long" value="${token.dateActioned}"/>
						</td>
					</tr>
				</c:forEach>
				<tr class="close_button_row"> 
					<td class ="buttoncell" colspan="4"></td>
					<td></td>
				</tr>
	</table>
	</c:if>
	<c:if test="${not empty error}">
		<div class="error"><fmt:message key="bpm.workflow.history.historyerror" /></div>
	</c:if>
</div>