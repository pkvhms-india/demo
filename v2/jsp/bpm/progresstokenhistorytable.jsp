<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
<div class="popup popupx">
	<c:if test="${empty historyerror}">
		<table id="bpm_progress_token_header"
			class="tokens bpm_table bpm_historytable popup bpm_history_position bpm_history_header">
			<tr class="header_row">
				<th colspan="5" id="workflow_history_header" class="bg">
					<fmt:message key="bpm.workflow.history.bpmHistoryHeader">
						<fmt:param value="${BPMView.name}"></fmt:param>
					</fmt:message>
				</th>
			</tr>
		</table>
		<div class="popup bpm_historytable_body">
			<table id="uniquetable" class="tokens bpm_table bpm_historytable popup bpm_history_position">

				<c:forEach items="${BPMHistory}" var="token" varStatus="rowCount">
					<tr class="token_row">
						<td colspan="2" class="token_action_cell">
							<c:if test="${not empty token.forceByWithStyle}">
								<span>
									<fmt:message key="bpm.workflow.history.forcedby">
										<fmt:param value="${token.actionWithStyle}"></fmt:param>
										<fmt:param value="${token.actorWithStyle}"></fmt:param>
										<fmt:param value="${token.forceByWithStyle}"></fmt:param>
									</fmt:message>
								</span>
							</c:if>

							<c:if test="${empty token.forceByWithStyle}">
								<span>
									<fmt:message key="bpm.workflow.history.notforcedby">
										<fmt:param value="${token.actionWithStyle}"></fmt:param>
										<fmt:param value="${token.actorWithStyle}"></fmt:param>
									</fmt:message>
								</span>
							</c:if>
							<div class="token_actionname_cell">
								<c:out value="${token.nodeName}"></c:out>
							</div>
						</td>
						<td class="token_actionDate_cell">
							<fmt:formatDate pattern="MM/dd/yyyy hh:mm:ss a" dateStyle="long" timeStyle="long"
								value="${token.dateActioned}" />
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</c:if>
	<c:if test="${not empty historyerror}">
		<div class="error">
			<fmt:message key="bpm.workflow.history.historyerror" />
		</div>
	</c:if>

	<table id="history_button_table" class="tokens bpm_table bpm_historytable popup bpm_historytable_body_button">
		<tr class="close_button_row">
			<td class="buttoncell" colspan="8"></td>
			<td class="history_button_cell"><input id="history_close_button" type="button"
					class="action_button_history action_button" value="close"></td>
		</tr>
	</table>
</div>