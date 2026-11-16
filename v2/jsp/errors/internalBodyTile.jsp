<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<html>

	<div id="errorPage">
		<c:choose>
			<c:when test="${function:instanceOf(exception, 'com.varicent.core.exceptions.WebDisabledException')}">
				<p class="error">
					<fmt:message key="error.webDisabled" />
				</p>
			</c:when>
			<c:when test="${function:instanceOf(exception, 'com.varicent.core.exceptions.ModelVersionException')}">
				<p class="error">
					<fmt:message key="error.login.oldDatabaseVersion" />
				</p>
			</c:when>
			<c:when test="${function:instanceOf(exception, 'com.varicent.core.exceptions.UnicodeConfigException')}">
				<p class="error">
					<fmt:message key="error.login.invalidUnicodeConfig" />
				</p>
			</c:when>
			<c:when test="${function:instanceOf(exception, 'com.varicent.core.exceptions.GlobalLockException')}">
				<p class="error">
					<fmt:message key="error.locked" />
				</p>
			</c:when>
			<c:when
				test="${function:instanceOf(exception, 'org.springframework.web.multipart.MaxUploadSizeExceededException')}">
				<p class="error">
					<fmt:message key="error.maxUploadSizeExceeded" />
				</p>
			</c:when>
			<c:when
				test="${function:instanceOf(exception, 'org.springframework.dao.DeadlockLoserDataAccessException')}">
				<p class="error">
					<fmt:message key="error.webreports.modificationInProgress" />
				</p>
			</c:when>
			<c:when test="${function:instanceOf(exception, 'com.varicent.core.exceptions.ReportOverLimitException')}">
				<p class="error">
					<fmt:message key="reporting.error.picklistTooManyRows" />
				</p>
			</c:when>
			<c:otherwise>
				<p class="error">
					<fmt:message key="error.exception" />
				</p>
			</c:otherwise>
		</c:choose>
	</div>

	</html>