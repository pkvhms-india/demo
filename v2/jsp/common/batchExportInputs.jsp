<spring:nestedPath path="${Command}">
	<spring:bind path="id">
		<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
		<c:out value="${status.value}" />" />
	</spring:bind>

	<spring:bind path="token">
		<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
		<c:out value="${status.value}" />" />
	</spring:bind>

	<c:forEach items="${colsToHide}" varStatus="curTable">
		<c:forEach items="${curTable}" varStatus="curItem">
			<spring:bind path="colsToHide[${curTable.index}][${curItem.index}]">
				<input type="hidden" name="<c:out value=" ${status.expression}" />"
				id="
				<c:out value="${status.expression}" />"
				value="
				<c:out value="${status.value}" />" />
			</spring:bind>
		</c:forEach>
	</c:forEach>

	<div id="hiddenColsBatch"></div>
</spring:nestedPath>