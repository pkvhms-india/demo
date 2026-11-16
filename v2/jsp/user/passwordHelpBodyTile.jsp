<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<div id="passwordHelp_page">
		<security:authorize ifNotGranted="ROLE_LDAP,ROLE_SSO">
			<c:choose>
				<c:when test="${!empty success}">
					<p class="success">
						<fmt:message key="${success}" />
					</p>
				</c:when>
				<c:otherwise>
					<fmt:message key="user.passwordHelp.info" />
					<div id="passwordHelp_form">
						<form method="post">
							<table cellspacing="0">
								<tr>
									<td><input type="text" name="username" value="" /></td>
								</tr>
								<tr>
									<td><input type="submit" class="action_button" value="<fmt:message key="
											action.submit" />" /></td>
								</tr>
							</table>
						</form>
					</div>
					<br />

					<div id="links">
						<a href="mailto:<c:if test=" ${!empty adminEmail}">${adminEmail}</c:if>">
							<fmt:message key="login.forgotID" />
						</a>
					</div>
					<br />
				</c:otherwise>
			</c:choose>

			<spring:hasBindErrors name="command">
				<spring:bind path="command.*">
					<c:forEach items="${status.errorMessages}" var="error">
						<p class="error">
							<c:out value="${error}" escapeXml="false" />
						</p>
					</c:forEach>
				</spring:bind>
			</spring:hasBindErrors>
		</security:authorize>
	</div>