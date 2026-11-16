<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="drawassignment">

<c:if test="${! empty messageKey}">
	<div id="errorPage">
		<span class="error"><fmt:message key="${messageKey}"/></span>
	</div>
</c:if>

<c:if test="${! empty command}">
	<jsp:directive.include file="header.jsp" />
</c:if>

</div>
