<jsp:directive.page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" />
<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />


<tiles:insertDefinition name="mainTile">
	<tiles:putAttribute name="header" value="/WEB-INF/jsp/payeeledger/summaryHeaderTile.jsp"/>
	<tiles:putAttribute name="body" value="/WEB-INF/jsp/payeeledger/summaryBodyTile.jsp"/> 
</tiles:insertDefinition>
