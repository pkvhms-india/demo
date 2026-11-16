<spring:nestedPath path="${Command}">
	<spring:bind path="id">
		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
	</spring:bind>
	
	<spring:bind path="clientLocale">
		<input id="clientLocale" type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
	</spring:bind>
	
	<spring:bind path="token">
		<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
	</spring:bind>
</spring:nestedPath>

<c:if test="${!empty datagridContent}">
	<fmt:message key="${datagridContent}">
		<fmt:param value="${fn:escapeXml(datagridName)}"/>
	</fmt:message>
</c:if>

<c:if test="${!empty reportingMessage}">
	<fmt:message key="${reportingMessage}">
		<c:if test="${!empty reportingMessageParam}">
		<fmt:param value="${fn:escapeXml(reportingMessageParam)}"/>
		</c:if>
	</fmt:message>
</c:if>