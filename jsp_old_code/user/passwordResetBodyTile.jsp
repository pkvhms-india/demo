<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="passwordReset_page">

<security:authorize ifNotGranted="ROLE_LDAP,ROLE_SSO">
	<c:choose>
		<c:when test="${!empty success}">
			<p class="success"><fmt:message key="${success}" /></p>
			<div id="links">
				<br/>
				<a href="login.html"><fmt:message key="user.passwordReset.login" /></a>
			</div>
		</c:when>
		<c:when test="${!empty validKey}">
			<c:if test="${strongPasswordRequired}">
					<fmt:message key="user.password.strong"/>
			</c:if>
			
			<div id="passwordReset_form">
				<form method="post">
					<table cellspacing="0">
						<tr>
							<td><fmt:message key="user.password.new" /></td>
							<td><input type="password" name="newPassword" value="" /></td>
						</tr>
						<tr>
							<td><fmt:message key="user.password.confirm" /></td>
							<td><input type="password" name="confirmPassword" value="" /></td>
						</tr>
						<tr>
							<td></td>
							<td><input type="submit" class="action_button" value="<fmt:message key="action.submit" />" /></td>
						</tr>
					</table>
				</form>
			</div>
		
			<spring:hasBindErrors name="command">
				<spring:bind path="command.*">
					<c:forEach items="${status.errorMessages}" var="error">
						<p class="error"><c:out value="${error}" escapeXml="false" /></p>
					</c:forEach>
				</spring:bind>
			</spring:hasBindErrors>    
		</c:when>
		<c:otherwise>
			<p class="error"><fmt:message key="error.passwordReset.invalid" /></p>
			<div id="links">
			<br/><a href="passwordHelp.html"><fmt:message key="user.passwordReset.newRequest" /></a>
			</div>
		</c:otherwise>
	</c:choose>
</security:authorize>

</div>