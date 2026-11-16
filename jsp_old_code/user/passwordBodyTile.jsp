<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="password_page">
	<div id="password_form">
		<c:choose>
			<c:when test="${!empty success}">
				<form method="get" action="../home.html">
			</c:when>
			<c:otherwise>
				<form method="post">
			</c:otherwise>
		</c:choose>
			<spring:nestedPath path="command">
					<spring:bind path="token">
						<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />						
					</spring:bind>
			</spring:nestedPath>

			<div id="password_desc">
				<c:choose>
					<c:when test="${!empty expiringWarning}">
						<p><fmt:message key="user.password.pageTitleExpiring" /></p>
					</c:when>
					<c:otherwise>
						<p><fmt:message key="user.password.pageTitle" /></p>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${!empty success}">
						<p class="success"><fmt:message key="${success}" /></p>
						</div>
						<div>
							<input type="submit" class="action_button" value="<fmt:message key="action.ok" />" />
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${!empty expiringWarning}">
								<p class="instructionLabel"><fmt:message key="user.password.pageDescExpiring">
									<fmt:param><b></fmt:param>
									<fmt:param>${expiringIn}</fmt:param>
									<fmt:param></b></fmt:param>
								</fmt:message></p>
							</c:when>
							<c:otherwise>
								<p class="instructionLabel"><fmt:message key="user.password.pageDesc">
									<fmt:param><br /></fmt:param>
									<fmt:param><b></fmt:param>
									<fmt:param></b></fmt:param>
								</fmt:message></p>
							</c:otherwise>
						</c:choose>
						
						<c:if test="${strongPasswordRequired}">
							<p class="strongPasswordLabel">
								<fmt:message key="user.password.strong"/>
							</p>
						</c:if>
						
						</div>
						<div>
							<label><fmt:message key="user.password.old" /></label>
							<input type="password" name="oldPassword" value="" data-csp-keypress-fn="CapsLockCheck" data-csp-keypress-args='[&quot;@event&quot;]' data-csp-block="copy cut" 
								class="input_field password_field"/>
						</div>
						<div>
							<label><fmt:message key="user.password.new" /></label>
							<input type="password" name="newPassword" value="" data-csp-keypress-fn="CapsLockCheck" data-csp-keypress-args='[&quot;@event&quot;]' data-csp-block="copy cut" 
								class="input_field password_field"/>
						</div>
						<div>
							<label><fmt:message key="user.password.confirm" /></label>
							<input type="password" name="confirmPassword" value="" data-csp-keypress-fn="CapsLockCheck" data-csp-keypress-args='[&quot;@event&quot;]' data-csp-block="copy cut" 
								class="input_field password_field"/>
						</div>
						<spring:hasBindErrors name="command">
							<spring:bind path="command.*">
								<c:forEach items="${status.errorMessages}" var="error">
									<p class="error"><c:out value="${error}" escapeXml="false" /></p>
								</c:forEach>
							</spring:bind>
						</spring:hasBindErrors>
						<div style="height:60px;">
							<input type="submit" class="action_button" value="<fmt:message key="action.save" />" />
							<div>
							<c:choose>
								<c:when test="${!empty expiringWarning}">
									<a href="../home.html" class="remindMe_link"><fmt:message key="user.password.remindLater" /></a>
								</c:when>
								<c:otherwise>
									<a href="../home.html" class="cancel_link"><fmt:message key="action.cancel" /></a>
								</c:otherwise>
							</c:choose>
							</div>
					</c:otherwise>
				</c:choose>
			</div>
			<c:choose>
				<c:when test="${!empty expiringWarning}">
					<div>
						<c:choose>
							<c:when test="${!empty lastWarning}">
								<p class="expiringLabel"><fmt:message key="user.password.lastWarning">
								</fmt:message></p>
							</c:when>
							<c:otherwise>
								<p class="expiringLabel"><fmt:message key="user.password.willRemindDays">
									<fmt:param>${notifyIn}</fmt:param>
								</fmt:message></p>
							</c:otherwise>
						</c:choose>
					</div>
				</c:when>
			</c:choose>
		</form>
	</div>
	
	<div id="capsLockAlert" style="visibility:hidden">
		<font color="red">
			Having Caps Lock on may cause you to enter your password incorrectly.<br/>
			You should press Caps Lock to turn it off before entering your password.
		</font>
	</div>

</div>
