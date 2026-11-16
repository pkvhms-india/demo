<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />


<div id="footer">
		<input id ="pageNumberId" type="hidden" value="${command.pageNumber}"></input>
		<input id ="totalPagesId" type="hidden" value="${command.totalPages}"></input>
		<input id ="sortColumnId" type="hidden" value="${command.sortColumn}"></input>
		<input id ="descendingId" type="hidden" value="${command.descending}"></input>
		<input id ="rowsPerPageId" type="hidden" value="${command.rowsPerPage}"></input>
		<c:if test="${command.totalPages > 0}">
			<fmt:message key="common.paging.page" />
			<span id="pageNumberText"><c:out value="${command.pageNumber}" /></span>
			<fmt:message key="common.paging.of" />
			<c:out value="${command.totalPages}" />
							
			<c:out value = "       [" />
			<span id ="pagetable">
			<c:forEach begin="${1}" end="${20}" var="pageNumber">
				<c:if test="${pageNumber <= command.totalPages}">
				<span id="page_${pageNumber}">
					<c:choose>
						<c:when test="${pageNumber == command.pageNumber}" >
							<c:out value=" ${pageNumber} " />
						</c:when>
						<c:otherwise>
							<a href="#">
								<c:out value=" ${pageNumber} " />
							</a>
						</c:otherwise>
					</c:choose>
				</span>
				</c:if>
			</c:forEach>
			</span>
			<c:out value = "       ]" />
		</c:if>
</div>