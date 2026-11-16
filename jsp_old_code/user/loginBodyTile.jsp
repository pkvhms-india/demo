<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="login_screen">
	<table id="login_layout" cellpadding="0" cellspacing="6">
		<tbody>
			<tr>
				<td id="left_pane" align="right">
					<c:if test="${theme.themeType eq 'Standard' && theme.enabled && theme.headerImage != null}">
						<img src="<c:url value="/theme/logo.do" />" />
					</c:if>
				</td>
				<td id="divider"></td>
				<td id="right_pane">
					<form method="post" id="loginForm" action="j_spring_security_check"	autocomplete="off">
						<table cellpadding="0" cellspacing="6">
							<tbody>
								<tr>
									<td id="td_username">
										<input class="input_field user_field empty" type="text" name="j_username" spellcheck="false"  />
										<span><fmt:message key="login.user" /></span>
									</td>
								</tr>
								<tr>
									<td id="td_password">
										<input class="input_field password_field empty" type="password" name="j_password" />
										<span><fmt:message key="login.password" /></span>
									</td>
								</tr>
								<c:choose>
									<c:when test="${!empty error}">
										<tr>
											<td id="css_error"><fmt:message key="${error}" />
											</td>
										</tr>
									</c:when>
									<c:when test="${!empty warning}">
										<tr>
											<td id="css_error"><c:out value="${warning}" />
											</td>
										</tr>
									</c:when>
								</c:choose>

								<tr>
									<td></td>
								</tr>
								<tr>
									<td align="right"><input id="login_button" type="submit"
										value="<fmt:message key="login.button" />" /></td>
								</tr>
								<tr>
									<td></td>
								</tr>

								<c:if test="${empty ldapAuth}">
									<table class="links">
									<c:choose>
										<c:when test="${!empty passwordReset}">
											<tr>
												<td><a class="link"
													href='mailto:<c:if test="${!empty adminEmail}">${adminEmail}</c:if>'><fmt:message
															key="login.forgotID" /> </a></td>
											</tr>
											<tr>
												<td><a class="link" href="passwordHelp.html"><fmt:message
															key="login.forgotPassword" /> </a></td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td><a class="link"
													href='mailto:<c:if test="${!empty adminEmail}">${adminEmail}</c:if>'><fmt:message
															key="login.forgotIdOrPassword" /> </a></td>
											</tr>
										</c:otherwise>
									</c:choose>
									<tr>
										<td><a class="link"
											href='mailto:<c:if test="${!empty adminEmail}">${adminEmail}</c:if>'><fmt:message
													key="login.noAccount" /> </a></td>
									</tr>
									</table>
								</c:if>

							</tbody>
						</table>
					</form></td>
			</tr>
		</tbody>
	</table>
	<div id="login_bottom_bar">
		<div id="bottom_link">
			<a href='https://www.ibm.com/privacy' rel='noopener noreferer' target='_blank'><fmt:message
					key="login.privacy" />
			</a>
		</div>
	</div>
</div>
