<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<fmt:setBundle basename="messages" var="msgLang" />

	<script src="<c:url value='/scripts/about.js' />" type="text/javascript"></script>

	<div class="modalPopup"></div>

	<div class="modalPopupWrapper">
		<div class="modalPopupPosition"></div>
		<div class="modalPopupContent">
			<img src="<c:url value='/images/about.png' />" />
			<p id="version"></p>
			<p id="description">
				<fmt:message key="topbar.about.description" bundle="${msgLang}" /><br />
				<br /><a target="_blank" href="<c:url value='/docs/IbmCognosIcmNotices.txt' />">
				<fmt:message key="topbar.about.notices" bundle="${msgLang}" /></a>
			</p>
			<div id="footer">
				<span id="left"><button class="okBtn">
						<fmt:message key="action.ok" />
					</button></span>
				<span id="right"><img id="java-logo" src="<c:url value='/images/java-logo.png' />" /></span>
			</div>
		</div>
	</div>
