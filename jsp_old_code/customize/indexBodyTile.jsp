<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="customize">

	<div id="customize_form">
		<form id="customize_input" method="post">
			<spring:nestedPath path="command">
				<spring:bind path="tableChanged">
					<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
				</spring:bind>

				<spring:bind path="token">
					<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
				</spring:bind>
				

				<div id="customize_select">
					<spring:bind path="table">
						<select name="<c:out value="${status.expression}"/>" data-csp-set-field="tableChanged" data-csp-set-value="true" data-csp-submit-change="1">
							<option value="" <c:if test="${empty status.value}">selected</c:if>><fmt:message key="customize.defaultTableOption" /></option>
							<c:forEach items="${tables}" var="table">
								<option value="${table}"<c:if test="${status.value == table}">selected</c:if>><tr:translate>${table}</tr:translate></option>
							</c:forEach>
						</select>
					</spring:bind>
					<c:if test="${!empty columns}">
						<img id="toggler" src="<c:url value="/images/form.png" />" title="<fmt:message key="customize.tooltip.toggleTableForm" />" />
					</c:if>
				</div>

				<c:if test="${!empty columns}">
					<div id="customize_columns">
						<table cellspacing="0">
							<spring:bind path="filterValues">
								<c:forEach items="${columns}" var="column" varStatus="varStatus">
									<c:set var="filter_value" scope="page" value="${command.filterValues[varStatus.index]}" />
									<tr>
										<td class="label"><tr:translate>${column.name}</tr:translate></td>
										<td>
											<c:choose>
												<c:when test="${columns[varStatus.index].hasPicklist}">
													<select name="${status.expression}">
														<option value="" <c:if test="${empty filter_value}">selected</c:if>></option>
														<c:forEach items="${columns[varStatus.index].options}" var="option">
															<option value="<c:out value="${option}"/>" <c:if test="${option.value == filter_value}">selected</c:if>><c:out value="${option}"/></option>
														</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${filter_value}"/>" />
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
							</spring:bind>
						</table>
						<div id="actions">
							<input type="hidden" id="add_action_url" value="<c:url value="/dataedit/add.do" />" />
							<input type="submit" id="add" class="action_button" value="<fmt:message key="action.add" />" />
							<input type="submit" id="clear" class="action_button" value="<fmt:message key="action.clear" />" />
							<input type="submit" id="filter" class="action_button" value="<fmt:message key="action.filter" />" />
						</div>
					</div>
				</c:if>
			</spring:nestedPath>
		</form>

		<spring:hasBindErrors name="command">
			<spring:bind path="command.*">
				<div id="errors">
					<c:forEach items="${status.errorMessages}" var="error">
						<p class="error"><c:out value="${error}"/></p>
					</c:forEach>
				</div>
			</spring:bind>
		</spring:hasBindErrors>
	</div>

	<div id="customize_data">
		<table cellspacing="0">
			<c:if test="${!empty pendingRows}">
				<tr class="title_row">
					<td colspan="${fn:length(columns) + 1}"><fmt:message key="customize.pendingRows" /></td>
				</tr>
				<tr class="header_row">
					<c:forEach items="${columns}" var="column">
						<th><tr:translate>${column.name}</tr:translate></th>
					</c:forEach>
					<th></th>
				</tr>

				<c:forEach items="${pendingRows}" var="row" varStatus="rowStatus">
					<tr class="edit_row">
						<c:forEach items="${row.values}" var="value">
							<td><c:out value="${value}"/></td>
						</c:forEach>
						<td></td>
					</tr>
				</c:forEach>

				<tr>
					<td colspan="${fn:length(columns) + 1}">&nbsp;</td>
				</tr>
			</c:if>

			<c:if test="${!empty rows}">
				<tr class="title_row">
					<td colspan="${fn:length(columns) + 1}"><fmt:message key="customize.pendingUpdates" /></td>
				</tr>
				<tr class="header_row">
					<c:forEach items="${columns}" var="column">
						<th><tr:translate>${column.name}</tr:translate></th>
					</c:forEach>
					<th></th>
				</tr> 

				<c:forEach items="${rows}" var="row" varStatus="rowStatus">
					<c:choose>
						<c:when test="${function:instanceOf(row, 'com.varicent.customize.domain.EditRow')}">
							<tr class="edit_row">
								<c:forEach items="${row.values}" var="value">
									<td><c:out value="${value}"/></td>
								</c:forEach>
								<td></td>
							</tr>
						</c:when>
						<c:otherwise>
							<form method="post" action="<c:url value="/dataedit/edit.do" />">
								<tr class="data_row<c:if test="${rowStatus.index % 2 == 0}">_alt</c:if>">
									<spring:nestedPath path="command">
										<spring:bind path="table">
											<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.table}"/>" />
										</spring:bind>
												
										<spring:bind path="token">
											<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />
										</spring:bind>

										<spring:bind path="filterValues">
											<c:forEach items="${columns}" var="column" varStatus="varStatus">
												<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${command.filterValues[varStatus.index]}"/>" />
											</c:forEach>
										</spring:bind>
										
										<spring:bind path="originalDataRow">
											<c:forEach items="${row.values}" var="value">
												<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${value}"/>" />
											</c:forEach>
										</spring:bind>

										<spring:bind path="editedDataRow">
											<c:forEach items="${row.values}" var="value" varStatus="valueStatus">
												<c:choose>
													<c:when test="${columns[valueStatus.index].editable}">
														<c:choose>
															<c:when test="${row.locked}">
																<td><c:out value="${value}"/>&nbsp;<img src="<c:url value="/images/lock.png" />" title="<fmt:message key="customize.tooltip.locked" />" /></td>
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when test="${columns[valueStatus.index].hasPicklist}">
																		<td>
																			<select name="<c:out value="${status.expression}"/>">
																				<option value="" <c:if test="${empty value}">selected</c:if>></option>
																				<c:set var="contains" value="false"/>
																				<c:forEach items="${columns[valueStatus.index].options}" var="option">
																					<option value="<c:out value="${option}"/>" <c:if test="${option.value == value}">selected</c:if>><c:out value="${option}"/></option>
																					<c:if test="${option.value == value}">
																						<c:set var="contains" value="true" />
																					</c:if>
																				</c:forEach>
																				<c:if test="${!contains && !empty value}">
																					<option value="<c:out value="${value}"/>" selected><c:out value="${value}"/></option>
																				</c:if>
																			</select>
																		</td>
																	</c:when>
																	<c:otherwise>
																		<c:choose>
																			<c:when test="${columns[valueStatus.index].type == 'COMMENT'}">
																				<td><textarea name="<c:out value="${status.expression}"/>"><c:out value="${value}"/></textarea></td>
																			</c:when>
																			<c:otherwise>
																				<td><input type="text" name="<c:out value="${status.expression}"/>" value="<c:out value="${value}"/>" /></td>
																			</c:otherwise>
																		</c:choose>
																	</c:otherwise>
																</c:choose>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${columns[valueStatus.index].type == 'URL'}">
																<td><a href="${value}"><c:out value="${value}"/></a></td>
															</c:when>
															<c:otherwise>
																<td><c:out value="${value}"/></td>
															</c:otherwise>
														</c:choose>
														<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${value}"/>" />
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</spring:bind>
										<td>
											<c:if test="${!row.locked}">
												<input type="submit" class="action_button" value="<fmt:message key="action.submit" />" />
											</c:if>
										</td>
									</spring:nestedPath>
								</tr>
							</form>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:if>
		</table>
	</div>

</div>