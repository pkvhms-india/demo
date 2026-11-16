<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<c:choose>
	<c:when test="${activeBPMTab == 'tokenlist'}">
		<c:set var="tokenlist_active" value="active" />
		<c:set var="index_active" value="" />
		<c:set var="history_active" value="" />
	</c:when>
	<c:when test="${activeBPMTab == 'index'}">
		<c:set var="tokenlist_active" value="" />
		<c:set var="index_active" value="active" />
		<c:set var="history_active" value="" />
	</c:when>
	<c:otherwise>
		<c:set var="tokenlist_active" value="" />
		<c:set var="index_active" value="" />
		<c:set var="history_active" value="active" />
	</c:otherwise>
</c:choose>

<div class="bpm_tabs">
<div class="tab <c:out value='${index_active}' />">
		<a href="<c:url value="index.html" />">
			<fmt:message key="bpm.links.index">
				<fmt:param value="${workflowsCount}" />
			</fmt:message>
		</a>
	</div>
	<div class="tab <c:out value='${tokenlist_active}'/>">
		<a href="<c:url value="tokenlist.html" />">
			<fmt:message key="bpm.links.tokenlist" >
				<fmt:param value="${tokensCount}" />
			</fmt:message>
		</a>
	</div>
	<div class="tab <c:out value='${history_active}'/>">
		<a href="<c:url value="completedtokenhistorylistpage.html" />">
			<fmt:message key="bpm.links.completedwfs" >
				<fmt:param value="${historyCount}" />
			</fmt:message>
		</a>
	</div>
</div>