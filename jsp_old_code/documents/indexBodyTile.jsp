<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="message_center">

	<div class="heading documents">
		<fmt:message key="messagecenter.sectionLinks.documents" />
	</div>

	<div class="messages">
	
		<c:if test="${!empty msgKey}">
			<p class="error"><fmt:message key="${msgKey}" /></p>
		</c:if>

		<c:set var="row_count" scope="page" value="0" />

		<table cellspacing="0" id="messages">

			<tr class="header_row">
				<th class="table_margin">&nbsp;</th>
				<th><fmt:message key="messagecenter.messages.name" /></th>
				<th><fmt:message key="messagecenter.messages.date" /></th>
				<th class="table_margin">&nbsp;</th>
			</tr>

			<c:choose>
				<c:when test="${!empty attachments}">
						<c:forEach items="${attachments}" var="attachment">
							<tr class="message_row">
								<td class="table_margin"/>
								<td>
									<c:url value="/documents/download.do" var="downloadURL">
									  <c:param name="id" value="${attachment.fileId}" />
									</c:url>
									
									<a href="${downloadURL}">
									
										<c:choose>
											<c:when test="${attachment.fileType == 'DOC'}">
												<img src="<c:url value="/images/icon_word.png" />" />
											</c:when>
											<c:when test="${attachment.fileType == 'XLS'}">
												<img src="<c:url value="/images/icon_excel.png" />" />
											</c:when>
											<c:when test="${attachment.fileType == 'PPT'}">
												<img src="<c:url value="/images/icon_powerpoint.png" />" />
											</c:when>
											<c:when test="${attachment.fileType == 'PDF'}">
												<img src="<c:url value="/images/icon_pdf.gif" />" />
											</c:when>
											<c:otherwise>
												<img src="<c:url value="/images/icon_file.png" />" />
											</c:otherwise>
										</c:choose>			

										<c:out value="${attachment.name}" />
									</a>
								</td>
								<td>
									<fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${attachment.date}" />
								</td>
								<td class="table_margin"/>
							</tr>
						</c:forEach>
				</c:when>
				<c:otherwise>
					<tr><td colspan="4"/><fmt:message key="documents.noDocumentsFound" /></tr>
				</c:otherwise>
			</c:choose>

		</table>
		
	</div>
</div>

