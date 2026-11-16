<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" />

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="function" uri="http://tagutils"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
	<meta content='width=device-width; initial-scale=1.0;' name='viewport' />

	<c:choose>
		<c:when test="${not empty theme && theme.themeType eq 'SFDC'}">
			<link rel="stylesheet" href="<c:url value="/styles/vsfdc.css" />" type="text/css" />
		</c:when>
		<c:otherwise>
			<link rel="stylesheet" href="<c:url value="/styles/varicent.css" />" type="text/css" />
		</c:otherwise>
	</c:choose>

	<link rel="icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon" />
	
	<script src="<c:url value="/scripts/prototype.js" />" type="text/javascript"></script>

	<%-- Binds the data-csp-* attributes that replaced the inline event handlers
		CSP blocks (F15). Delegated from the document, so it may load here in
		the head and still catch markup written later. --%>
	<script src="<c:url value="/scripts/csp-bindings.js" />" type="text/javascript"></script>

<tiles:insertAttribute name="header"/> 

	<style type="text/css">

		<c:if test="${not empty theme}">
			<c:choose>
				<c:when test="${theme.themeType eq 'Standard'}">
					.infobox h2 {
						background-color: ${theme.headerColor};
					}

					#customize_data .header_row {
						background-color: ${theme.headerColor};
					}
				</c:when>
				<c:when test="${theme.themeType eq 'SFDC'}">
					.infobox h2 {
						background-color: ${theme.headerColor};
					}

					#customize_data .header_row {
						background-color: ${theme.headerColor};
					}

					ul#tabs li.selected a
					{
						/* Selected tab link */
						background-color: ${theme.headerColor};
					}
				</c:when>
			</c:choose>
		</c:if>

	</style>
	

</head>

<body>
<div id="globalDiv">
	<security:authorize ifAllGranted="ROLE_ANONYMOUS">&nbsp;</security:authorize>
	<security:authorize ifAllGranted="ROLE_USER">
		<jsp:directive.include file="/WEB-INF/jsp/header/header.jsp" />
	</security:authorize>

	<div id="content">
		<tiles:insertAttribute name="body"/> 
	</div>
</div>
</body>

</html>

	