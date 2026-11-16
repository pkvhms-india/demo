<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />


<c:url var="summary_url" scope="page" value="summary.html${activeTab.parameterUrl}" >

<c:if test="${!empty command.planId}">
	<c:param name="plan" value="${command.planId}" />

	<c:if test="${!empty command.payeeId}">
		<c:param name="payee" value="${command.payeeId}" />
	</c:if>
	
	<c:if test="${!empty command.periodIndex}">
		<c:param name="period" value="${command.periodIndex}" />
	</c:if>
</c:if>
</c:url>

<div id="ledger">
	<c:if test="${!activeTab.tailoredReport}">
		<div id="ledger_type"><fmt:message key="menu.detail" /> &mdash; <a href="${summary_url}"><fmt:message key="menu.summary" /></a></div>
	</c:if>

	<jsp:directive.include file="selector.jsp" />
	
	<c:set var="report_count" scope="page" value="1" />
	<c:set var="trace_count" scope="page" value="0" />
	<c:set var="sort_count" scope="page" value="0" />
	
	<c:if test="${excelError}">
		<div id="excelError"><fmt:message key="error.excel.tooManyRows" /></div>
	</c:if>
	
	<c:if test="${!empty rows}">
		<jsp:directive.include file="header.jsp" />
	
		<div id="detail">
				<c:forEach items="${rows}" var="row">
					<c:choose>
						<c:when test="${row.type == 'REPORT'}">
							<c:if test="${report_count > 1}">
								</table>
								<c:set var="trace_count" scope="page" value="0" />
								<c:set var="sort_count" scope="page" value="0" />
							</c:if>
							
							<div class="report_row">
								<c:forEach items="${row.values}" var="value"><c:out value="${value}"/></c:forEach>
							</div>
							<c:if test="${!empty row.comment}">
								<div class="comment_row"><c:out value="${row.comment}"/></div>
							</c:if>
							
							<c:set var="report_count" scope="page" value="${report_count + 1}" />
							
							<c:set var="alt_color" scope="page" value="${row.theme.alternatingColor}" />
							<c:set var="header_color" scope="page" value="${row.theme.headerColor}" />
							<c:set var="summary_color" scope="page" value="${row.theme.summaryColor}" />
							
							<table cellspacing="0">
						</c:when>
						<c:when test="${row.type == 'HEADER'}">
							<tr class="header_row" <c:if test="${!empty header_color}">style="background-color: ${header_color}"</c:if>>
								<th></th>
								<c:forEach items="${row.values}" var="value">
									<th><c:out value="${value}"/></th>
								</c:forEach>
							</tr>            
						</c:when>
						<c:when test="${row.type == 'RESULT'}">
							<tr class="result_row" <c:if test="${!empty summary_color}">style="background-color: ${summary_color}"</c:if>>
								<td>
									<c:if test="${inquiriesEnabled}">
										<img class="copy" src="<c:url value="/images/copy.png" />" title="<fmt:message key="payeeledger.tooltip.copyValues" />" />
									</c:if>
								</td>
								<c:forEach items="${row.formattedValues}" var="value">
									<td <c:if test="${!empty value}">class="rowValue"</c:if>><c:out value="${value}"/></td>
								</c:forEach>
							</tr>
						</c:when>
						<c:when test="${row.type == 'TRACE'}">
							<tr class="trace_row<c:if test="${trace_count % 2 == 0}">_alt</c:if>" <c:if test="${(trace_count % 2 == 0) && !empty alt_color}">style="background-color: ${alt_color}"</c:if>>
								<td>
									<c:if test="${!empty row.sourceColumns}">
										<img class="info" src="<c:url value="/images/info.png" />" title="<fmt:message key="payeeledger.tooltip.showSource" />" />
									</c:if>
									<c:if test="${inquiriesEnabled}">
										<img class="copy" src="<c:url value="/images/copy.png" />" title="<fmt:message key="payeeledger.tooltip.copyValues" />" />
									</c:if>
								</td>
								<c:forEach items="${row.formattedValues}" var="value" varStatus="valueStatus">
									<td <c:if test="${!empty value}">class="rowValue"</c:if>>
										<c:choose>
											<c:when test="${function:instanceOf(row.objects[valueStatus.index], 'com.varicent.core.util.Link')}">
												<c:set var="link" value="${row.objects[valueStatus.index]}" />
												<c:choose>
													<c:when test="${link.email}">
														<a href="mailto:${link.address}"><c:out value="${link.name}"/></a>
													</c:when>
													<c:when test="${link.url}">
														<a href="${link.address}"><c:out value="${link.name}"/></a>
													</c:when>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:out value="${value}"/>
											</c:otherwise>
										</c:choose>
									</td>
								</c:forEach>
							</tr>
							<c:set var="trace_count" scope="page" value="${trace_count + 1}" />
						</c:when>
						<c:when test="${row.type == 'SORT'}">
							<tr class="sort_row<c:if test="${sort_count % 2 == 0}">_alt</c:if>">
								<td>
									<c:if test="${!empty row.sourceColumns}">
										<img class="info" src="<c:url value="/images/info.png" />" title="<fmt:message key="payeeledger.tooltip.showSource" />" />
									</c:if>
									<c:if test="${inquiriesEnabled}">
										<img class="copy" src="<c:url value="/images/copy.png" />" title="<fmt:message key="payeeledger.tooltip.copyValues" />" />
									</c:if>
								</td>
								<c:forEach items="${row.values}" var="value">
									<td <c:if test="${!empty value}">class="rowValue"</c:if>><c:out value="${value}"/></td>
								</c:forEach>
							</tr>
							<c:set var="sort_count" scope="page" value="${sort_count + 1}" />
						</c:when>
					</c:choose>
					<c:if test="${!empty row.sourceColumns}">
						<tr class="source_row" style="display:none">
							<td colspan="${fn:length(row.values) + 1}">
								<table cellspacing="0">
									<c:forEach items="${row.sourceColumns}" var="entry">
										<tr>
											<td class="source_column"><tr:translate>${entry.key}</tr:translate></td>
											<td class="source_data"><c:out value="${entry.value}"/></td>
										</tr>
									</c:forEach>
								</table>
							</td>
						</tr>
					</c:if>
				</c:forEach>
			</table>
		</div>
	</c:if>

</div>

