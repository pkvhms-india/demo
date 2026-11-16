<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<tiles:insertDefinition name="mainTile">
	<tiles:putAttribute name="header" value="/WEB-INF/jsp/bpm/indexHeaderTile.jsp"/>
	<tiles:putAttribute name="body" value="/WEB-INF/jsp/bpm/indexBodyTile.jsp"/>
</tiles:insertDefinition>