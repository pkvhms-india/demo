<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

			
<script src="<c:url value="/scripts/jquery.min.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/jquery-migrate.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/dropdown.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/common.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/concertIntegration.js" />" type="text/javascript"></script>
<script src="<c:url value="/scripts/easyXDM/easyXDM.min.js" />" type="text/javascript"></script>

<fmt:setBundle basename="messages" var="msgLang"/>

<security:authorize ifNotGranted="ROLE_ANONYMOUS">
<jsp:directive.include file="/WEB-INF/jsp/about/index.jsp" />

<c:if test="${webEnabled}">
<div id="masthead">
	<div id="nav">
	    <div class="masthead_subdiv">
				  <c:if test="${theme.themeType eq 'Standard' && theme.enabled && theme.headerImage != null}">
						  <div id="brand"><img src="<c:url value="/theme/logo.do" />" width="${logo.width}" height="${logo.height}" /></div>
					  </c:if>
	    </div>
	    <div class="masthead_subdiv">
			  <ul id="main_nav">
				  <c:forEach items="${tabs}" var="tab">
					  <li class="mainTab <c:if test="${!empty selectedTab && selectedTab.id eq tab.id}">selected</c:if>">
						  <a href="<c:url value="${tab.url}" />"><c:out value="${tab.name}"/></a>
					  </li>
				  </c:forEach>
			  </ul>
	    </div>
    
		<div id="utilities">

			<c:if test="${showInquiry}">
				<div class="inquiry_icon" id="inquire" title="<fmt:message key="topbar.inquiry"/>">
				</div>
			</c:if>

			<c:if test="${showBatchExportToPdf}">
				<div class="pdf_batch_export_icon" id="batch-export-link" title="<fmt:message key="topbar.batchExportToPDF"/>">
				</div>
			</c:if>

			<c:if test="${showExportToPdf}">
				<div class="pdf_export_icon" id="export-link" title="<fmt:message key="topbar.exportToPDF"/>">
				</div>
			</c:if>

			<c:if test="${showInquiry || showBatchExportToPdf ||showExportToPdf}">
				<div class="module-actions-divider">
				</div>
			</c:if>

			<div class="message_center_icon <c:if test="${showMessageCentre}">selected</c:if>"
                 data-csp-nav="<c:url value='/messages/index.html' />"
                 title="<fmt:message key="topbar.messageCenter"/>">
				<c:if test="${newMessages > 0}"><a class="badge" href="<c:url value="/messages/index.html" />"><c:out value="${newMessages}" /></a></c:if>
			</div>
			<div id="user_icon"></div>
			<security:authorize ifAnyGranted="ROLE_ANONYMOUS">
				<div id="current_user" title="">&nbsp;</div>
			</security:authorize>
			<security:authorize ifNotGranted="ROLE_ANONYMOUS">
				<div id="current_user" title="<security:authentication property="principal.email" />"><security:authentication property="principal.email" /></div>
			</security:authorize>
			<div id="downarrow">
				<a href="" class="logout"><div id="downarrow_icon"></div></a>
			
				<ul id="dropdown" >
					<security:authorize ifNotGranted="ROLE_ANONYMOUS,ROLE_LDAP,ROLE_SSO">
						<li><a href="<c:url value="/user/password.html" />"><img src="<c:url value="/images/password.png"/>" /><span><fmt:message key="topbar.changePassword" bundle="${msgLang}" /></span></a></li>
					</security:authorize>
					<li><a href="mailto:<c:if test="${!empty adminEmail}">${adminEmail}</c:if>"><img src="<c:url value="/images/help.png"/>" /><span><fmt:message key="topbar.help" bundle="${msgLang}"/></span></a></li>
					<li><a href="#" class="about"><var id="version" data="${appVersion}"></var><img src="<c:url value="/images/about-icon.png"/>" /><span><fmt:message key="topbar.about" bundle="${msgLang}" /></span></a></li>
					<li class="divider"></li>
					<li><a href="<c:url value="/logout.html"/>"><img src="<c:url value="/images/logout.png"/>" /><span><fmt:message key="topbar.logout" bundle="${msgLang}" /></span></a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<div id="subhead" <c:if test="${!showMessageCentre && (empty selectedTab || !selectedTab.tabGroup)}">class="hiddenSubtabArea"</c:if>>
	<div id="nav">
    <div id="n_subNavContainer">
		  <c:choose>
			  <c:when test="${showMessageCentre}" >
				  <ul id="n_subNav">
					  <c:forEach items="${msgTabs}" var="msgTab">
						  <li <c:if test="${!empty selectedMsgTab && selectedMsgTab.id eq msgTab.id}">class="selected"</c:if>>
							  <a href="<c:url value="${msgTab.url}" />">
								  <img src="<c:url value="${msgTab.imageUrl}"/>"/>
								  &nbsp;
								  <span>
									  <fmt:message key="${msgTab.id}" bundle="${msgLang}" />
									  &nbsp;
									  <c:if test="${msgTab.count > 0}">
										  <c:out value="(${msgTab.count})" />
									  </c:if>
								  </span>
							  </a>
						  </li>
					  </c:forEach>
				  </ul>
			  </c:when>
			  <c:otherwise>
				  <c:if test="${!empty selectedTab && selectedTab.tabGroup}">
					  <ul id="n_subNav">
						  <c:forEach items="${selectedTab.subTabs}" var="subTab">
							  <li <c:if test="${!empty selectedSubTab && selectedSubTab.id eq subTab.id}">class="selected"</c:if>>
								  <a href="<c:url value="${subTab.url}" />"><c:out value="${subTab.name}"/></a>
							  </li>
						  </c:forEach>
					  </ul>
				  </c:if>
			  </c:otherwise>
		  </c:choose>
    </div>
	</div>
</div>
</c:if>
</security:authorize>
