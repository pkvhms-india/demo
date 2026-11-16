<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />


<tiles:insertDefinition name="mainTile">
	<tiles:putAttribute name="header" value="/WEB-INF/jsp/drawreport/periodpayoutHeaderTile.jsp" />
	<tiles:putAttribute name="body" value="/WEB-INF/jsp/drawreport/periodpayoutBodyTile.jsp" />
</tiles:insertDefinition>