<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<tiles:insertDefinition name="mainTile">
	<tiles:putAttribute name="header" value="/WEB-INF/jsp/inquiries/inquirylogHeaderTile.jsp"/>
	<tiles:putAttribute name="body" value="/WEB-INF/jsp/inquiries/inquirylogBodyTile.jsp"/> 
</tiles:insertDefinition>				
				