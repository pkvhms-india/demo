<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="adjustment">
	<div class="infobox">
		<h2>
			<img src="<c:url value="/images/adjustment.png" />" />
			<fmt:message key="adjustment.title" />
		</h2>

		<div class="infobox_body">
			<p class="error">
				<c:out value="${null}" escapeXml="false">
				<c:if test="${!empty command.errorKey}">
					<fmt:message key="${command.errorKey}">
					<c:forEach items="${command.errorParams}" varStatus="varStatus">
						<fmt:param value="${command.errorParams[varStatus.index]}" />
					</c:forEach>
					</fmt:message>
				</c:if>
				</c:out>
			</p>
			<form method="post" action="<c:url value="/ledger/adjustment.html">
					<c:param name="plan" value="${command.plan}" />
					<c:param name="payee" value="${command.payee}" />
					<c:param name="period" value="${command.period}" />
				</c:url>">
			<input type="hidden" name="token" value="${adjToken}"/>
				<table cellspacing="0">
					<tr>
						<td><fmt:message key="adjustment.plan" /></td>
						<td><c:out value="${adjPlan}"/></td>
					</tr>
					<tr>
						<td><fmt:message key="adjustment.payee" /></td>
						<td><c:out value="${adjPayee}"/></td>
					</tr>
					<tr>
						<td><fmt:message key="adjustment.period" /></td>
						<td><c:out value="${command.period}"/></td>
					</tr>
					<tr>
						<td><fmt:message key="adjustment.value" /></td>
						<td><input type="text" name="value" value="" /></td>
					</tr>
					<tr>
						<td><fmt:message key="adjustment.comment" /></td>
						<td><textarea class="comment_box" name="comment"></textarea></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" class="action_button" value="<fmt:message key="action.submit" />" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</div>