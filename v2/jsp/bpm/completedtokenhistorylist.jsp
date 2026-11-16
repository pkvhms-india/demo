<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="bpm_tokenlist">
	<div class="heading bpm">
		<fmt:message key="messagecenter.sectionLinks.bpm" />
	</div>

	<jsp:directive.include file="/WEB-INF/jsp/bpm/bpmTabs.jsp" />
	<table class="tokens bpm_table">
		<tr class="header_row">
			<th id="reportname">
				<fmt:message key="bpm.history.WorkFlowName" />
			</th>
			<th id="initiator">
				<fmt:message key="bpm.history.Initiator" />
			</th>
			<th id="initiatorName">
				<fmt:message key="bpm.history.InitiatorName" />
			</th>
			<th id="date">
				<fmt:message key="bpm.history.DateofInitiation" />
			</th>
			<th id="date">
				<fmt:message key="bpm.history.DateofCompletion" />
			</th>
		</tr>
		<c:forEach items="${completedTokens}" var="token" varStatus="rowCount">
			<tr class="token_row ${rowCount.index % 2 == 0 ? 'odd_row' : 'even_row'}" data-csp-hover-row="1"
			
			 data-csp-nav="completedtokenhistorypage.html?bpmTokenID=${token.tokenID}">
				<!-- <td>
							<div class="btn_review" data-csp-nav="<c:url value='view.html?bpmTokenID=${token.tokenID}' />">
								<a href="<c:out value="view.html?bpmTokenID=${token.tokenID}"/>"><fmt:message key="bpm.tokenlist.review" /></a>
							</div>
						</td>-->
				<td class="token_url_cell" title="<c:out value=" ${token.workflow}" />">
				<a href="<c:out value=" completedtokenhistorypage.html?bpmTokenID=${token.tokenID}" />" title="
				<c:out value="${token.workflow}" />">
				<c:out value="${token.workflow}" /></a>
				</td>
				<td class="initiator" title="<c:out value=" ${token.payee}" />">
				<c:out value="${token.payee}" />
				</td>
				<td class="initiatorName" title="<c:out value=" ${token.payeeName}" />">
				<c:out value="${token.payeeName}" />
				</td>
				<td class="tokens_date_cell" title="<fmt:formatDate type=" both" dateStyle="long" timeStyle="long"
					value="${token.createdAt}" />">
				<fmt:formatDate type="both" dateStyle="long" timeStyle="long" value="${token.createdAt}" />
				</td>
				<td class="tokens_date_cell" title="<fmt:formatDate type=" both" dateStyle="long" timeStyle="long"
					value="${token.lastModified}" />">
				<fmt:formatDate type="both" dateStyle="long" timeStyle="long" value="${token.lastModified}" />
				</td>
			</tr>
		</c:forEach>

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

			
<div id="btnPrev" class="btnPrev btnPaging"
    <c:choose>
        <c:when test="${!empty command.prevURL}">
            data-csp-nav="${command.prevURL}"
        </c:when>
        <c:otherwise>
            disabled="disabled"
        </c:otherwise>
    </c:choose>
>
    <img src="<c:url value='/images/arrow_prev.png' />" />
    <span>
        <fmt:message key="messagecenter.navigation.previous" />
    </span>
</div>

<div id="btnNext" class="btnNext btnPaging"
    <c:choose>
        <c:when test="${!empty command.nextURL}">
            data-csp-nav="${command.nextURL}"
        </c:when>
        <c:otherwise>
            disabled="disabled"
        </c:otherwise>
    </c:choose>
>
    <span>
        <fmt:message key="messagecenter.navigation.next" />
    </span>
    <img src="<c:url value='/images/arrow_next.png' />" />
</div>
			

		</div>
	</div>
</div>