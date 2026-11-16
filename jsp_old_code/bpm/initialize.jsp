<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<tiles:insertDefinition name="mainTile">
	<tiles:putAttribute name="header" value="/WEB-INF/jsp/bpm/initializeHeaderTile.jsp"/>
	<tiles:putAttribute name="body" value="/WEB-INF/jsp/bpm/initializeBodyTile.jsp"/>
</tiles:insertDefinition>