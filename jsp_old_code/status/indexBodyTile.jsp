<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />


	<div id="status">
		<c:if test="${showDetails}">
		<table cellspacing="0">
			<tr>
				<td>
					<fmt:message key="status.version" />
				</td>
				<td><span class="value">
						<c:out value="${status.applicationVersion}" />
					</span></td>
			</tr>
			<!-- <tr>
				<td>
					<fmt:message key="status.databaseName" />
				</td>
				<td><span class="value">
						<c:out value="${status.databaseName}" />
					</span></td>
			</tr>
			<tr>
				<td>
					<fmt:message key="status.databaseUnicode" />
				</td>
				<td><span class="value">
						<c:out value="${status.databaseUnicode}" />
					</span></td>
			</tr>
			<tr>
				<td>
					<fmt:message key="status.current" />
				</td>
				<td><span class="value">
						<c:out value="${status.databaseVersion}" />
					</span></td>
			</tr>
			<tr>
				<td>
					<fmt:message key="status.expected" />
				</td>
				<td><span class="value">
						<c:out value="${status.expectedDatabaseVersion}" />
					</span></td>
			</tr> -->
		</table>
		</c:if>
	</div>
