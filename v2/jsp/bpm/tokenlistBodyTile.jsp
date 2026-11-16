<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="bpm_tokenlist">
	<div class="heading bpm">
		<fmt:message key="messagecenter.sectionLinks.bpm" />
	</div>
	<jsp:directive.include file="/WEB-INF/jsp/bpm/bpmTabs.jsp" />
	<table class="tokens bpm_table">
		<c:choose>
			<c:when test="${!empty command.items}">
				<tr class="header_row">
					<th id="reportname">
						<fmt:message key="bpm.tokenlist.reportname" />
					</th>
					<th id="actionname">
						<fmt:message key="bpm.tokenlist.actionname" />
					</th>
					<th id="submittedby">
						<fmt:message key="bpm.tokenlist.submittedby" />
					</th>
					<th id="submittedbyname">
						<fmt:message key="bpm.tokenlist.submittedbyname" />
					</th>
					<th id="date">
						<fmt:message key="bpm.tokenlist.date" />
					</th>
				</tr>
				<c:forEach items="${command.items}" var="token" varStatus="rowCount">
					<tr class="token_row ${rowCount.index % 2 == 0 ? 'odd_row' : 'even_row'}"
					 data-csp-hover-row="1"
					 data-csp-nav="view.html?bpmTokenID=${token.tokenID}">
						<!-- <td>
							<div class="btn_review" data-csp-nav="<c:url value='view.html?bpmTokenID=${token.tokenID}' />">
								<a href="<c:out value="view.html?bpmTokenID=${token.tokenID}"/>"><fmt:message key="bpm.tokenlist.review" /></a>
							</div>
						</td>-->
						<td class="token_url_cell" title="<c:out value=" ${token.report}" />">
						<a href="<c:out value=" view.html?bpmTokenID=${token.tokenID}" />" title="
						<c:out value="${token.report}" />">
						<c:out value="${token.report}" /></a>
						</td>
						<td class="token_actionname_cell" title="<c:out value=" ${token.nodeName}" />">
						<c:out value="${token.nodeName}" />
						</td>
						<td class="token_payee_cell" title="<c:out value=" ${token.payee}" />">
						<c:out value="${token.payee}" />
						</td>
						<td class="token_payee_cell" title="<c:out value=" ${token.payeeName}" />">
						<c:out value="${token.payeeName}" />
						</td>
						<td class="tokens_date_cell" title="<fmt:formatDate type=" both" dateStyle="long"
							timeStyle="long" value="${token.lastModified}" />">
						<fmt:formatDate type="both" dateStyle="long" timeStyle="long" value="${token.lastModified}" />
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td>
						<fmt:message key="bpm.tokenlist.notokens" />
					</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
	<div class="table_footer">
		<div class="page_info">
			<div class="date_range">
				<c:if test="${!empty command.items}">
					<fmt:message key="bpm.tokenlist.fromTo">
						<fmt:param>
							<fmt:formatDate type="date" dateStyle="short" value="${command.lastItem.lastModified}" />
						</fmt:param>
						<fmt:param>
							<fmt:formatDate type="date" dateStyle="short" value="${command.firstItem.lastModified}" />
						</fmt:param>
					</fmt:message>
				</c:if>
			</div>

			<div class="current_page">
				<c:if test="${!empty command.items}">
					<fmt:message key="bpm.tokenlist.of">
						<fmt:param value="${command.firstItem.rowNumber}" />
						<fmt:param value="${command.lastItem.rowNumber}" />
						<fmt:param value="${command.totalItems}" />
					</fmt:message>
				</c:if>
			</div>
		</div>


		<div id="paging">

			<div id="btnPrev" class="btnPrev btnPaging${empty command.prevURL ? ' disabled' : ''}"
				<c:if test="${!empty command.prevURL}">onclick="location.href='<c:out value="${command.prevURL}"/>';"</c:if>>
				<img src="<c:url value='/images/arrow_prev.png' />" /><span>
					<fmt:message key="messagecenter.navigation.previous" />
				</span>
			</div>

			<div id="btnNext" class="btnNext btnPaging${empty command.nextURL ? ' disabled' : ''}"
				<c:if test="${!empty command.nextURL}">onclick="location.href='<c:out value="${command.nextURL}"/>';"</c:if>>
				<span>
					<fmt:message key="messagecenter.navigation.next" />
				</span><img src="<c:url value='/images/arrow_next.png' />" />
			</div>

		</div>
	</div>

</div>