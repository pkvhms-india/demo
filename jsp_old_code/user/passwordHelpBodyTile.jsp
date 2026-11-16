<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="passwordHelp_page">
	<security:authorize ifNotGranted="ROLE_LDAP,ROLE_SSO">
		<c:choose>
			<c:when test="${!empty success}">
				<p class="success"><fmt:message key="${success}" /></p>
			</c:when>
			<c:otherwise>
				<fmt:message key="user.passwordHelp.info" />
				<div id="passwordHelp_form">	
					<form method="post">
						<table cellspacing="0" id="passwordHelp_table">
							<tr>
								<td id="td_username">
									<input class="input_field user_field empty" type="text" name="username" value="">
									<span><fmt:message key="user.passwordReset.sampleEmail" /></span>
								</td>
							</tr>
							<spring:hasBindErrors name="command">
								<spring:bind path="command.*">
									<c:forEach items="${status.errorMessages}" var="error">
										<tr>
											<td id="css_error">
												<c:out value="${error}" escapeXml="false" />
											</td>
									</tr>
									</c:forEach>
								</spring:bind>
							</spring:hasBindErrors>
							<tr>
								<td align="left">
									<input id="submit_button" type="submit" value="<fmt:message key="action.submit" />" />
								</td>
							</tr>
						</table>
						<table class="links">
							<tr>
								<td>
									<a class="link" href="mailto:<c:if test="${!empty adminEmail}">${adminEmail}</c:if>"><fmt:message key="login.forgotID" /></a>
								</td>
							</tr>
						</table>
					</form>
				</div>
				<br/>
				<br/>
			</c:otherwise>
		</c:choose>
	</security:authorize>
</div>
