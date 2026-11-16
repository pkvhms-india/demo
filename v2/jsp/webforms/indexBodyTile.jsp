<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>


	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<div id="webforms">
		<c:if test="${activeTab.module}">
			<div id="select_form">
				<form method="post">
					<spring:nestedPath path="command">
						<spring:bind path="formChanged">
							<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
							<c:out value="${status.value}" />" />
						</spring:bind>

						<spring:bind path="token">
							<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
							<c:out value="${status.value}" />" />
						</spring:bind>

						<spring:bind path="id">
							<span class="spanFilterValues">
								<select name="${status.expression}"
								 data-csp-set-field="formChanged" data-csp-set-value="true" data-csp-submit-change="1">
									<option value="-1" <c:if test="${status.value == formDefinition.id}">selected
		</c:if>>
		<fmt:message key="webforms.defaultFormOption" />
		</option>
		<!-- Tabbing removed from loop to prevent extra whitespace in html -->
		<c:forEach items="${formList}" var="formDefinition">
			<option value="<c:out value=" ${formDefinition.id}" />" <c:if test="${status.value == formDefinition.id}">
				selected</c:if>>
			<c:out value="${formDefinition}" />
			</option>
		</c:forEach>
		</select>
		</span>
		</spring:bind>
		</spring:nestedPath>
		</form>
	</div>
	</c:if>

	<c:if test="${form.dataIncomplete}">
		<p class="error">
			<fmt:message key="error.webforms.data.incomplete" />
		</p>
	</c:if>

	<c:if test="${!empty webFormError}">
		<p class="error">
			<c:set var="errorMessage">
				<fmt:message key="${webFormError.message}">
					<!-- Tabbing removed from loop to prevent extra whitespace in html -->
					<c:forEach items="${webFormError.messageObject.params}" varStatus="varStatus">
						<fmt:param>
							<tr:translate>${webFormError.messageObject.params[varStatus.index]}</tr:translate>
						</fmt:param>
					</c:forEach>
				</fmt:message>
			</c:set>
			<c:out escapeXml="false" value="${errorMessage}" />
		</p>
	</c:if>

	<c:if test="${!empty filters}">
		<div id="filters">
			<form method="post">
				<spring:nestedPath path="command">
					<spring:bind path="id">
						<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
					</spring:bind>

					<spring:bind path="token">
						<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
					</spring:bind>

					<spring:bind path="resultRevision">
						<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${resultRevision}' />" />
					</spring:bind>

					<spring:bind path="systemDate">
						<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
					</spring:bind>

					<spring:bind path="pageLocale">
						<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
					</spring:bind>

					<!-- send back the list of filters so we know which filter value goes to which filter -->
					<!-- Tabbing removed from loop to prevent extra whitespace in html -->
					<c:forEach items="${filters}" var="filter" varStatus="varStatus">
						<input type="hidden" name="filterIds" value="<c:out value='${filter.id}' />"/>
					</c:forEach>

					<spring:bind path="filterValueMap">
						<!-- Tabbing removed from loop to prevent extra whitespace in html -->
						<c:forEach items="${filters}" var="filter" varStatus="varStatus">
							<div class="filter">
								<c:choose>
									<c:when test="${filter.display == 'FIELD'}">
										<div class="filter_field">
											<span>
												<c:out value="${filter.name}" />
											</span>
											<input type="text" name="filterValues" value="${status.value[filter.id]}" />
										</div>
									</c:when>
									<c:otherwise>
										<div class="filter_field">
											<span class="spanFilterValues">
												<select name="filterValues">
													<option value="" <c:if test="${empty status.value[filter.id]}">
														selected
	</c:if>>
	<c:out value="${filter.name}" />
	</option>
	<c:forEach items="${filter.options}" var="option">
		<option value="<c:out value=" ${option.value}" />" <c:if test="${status.value[filter.id] == option.value}">
			selected</c:if>>
		<c:choose>
			<c:when test="${!empty option.name}">
				<c:out value="(${option.value}) ${option.name}" />
			</c:when>
			<c:otherwise>
				<c:out value="${option.value}" />
			</c:otherwise>
		</c:choose>
		</option>
	</c:forEach>
	</select>
	</span>
	</div>
	</c:otherwise>
	</c:choose>
	</div>
	</c:forEach>
	</spring:bind>
	<div class="actions">
		<div class="action_item">
			<input type="submit" class="action_button" value="<fmt:message key='action.apply' />" />
		</div>
	</div>
	</spring:nestedPath>
	</form>
	</div>

	<c:if test="${form.filterFirst}">
		<div id="filterMessage">
			<fmt:message key="webforms.applyFilterFirst" />
		</div>
	</c:if>
	</c:if>

	<c:if test="${!empty form.javaScriptText}">
		<script type="text/javascript">${form.javaScriptText}</script>
	</c:if>

	<c:if test="${!empty form.grid && !form.dataIncomplete}">
		<div id="form">
			<form id="webform_input" method="post" action="<c:url value='/webforms/edit.do' />">
			<spring:nestedPath path="command">
				<spring:bind path="id">
					<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
				</spring:bind>

				<spring:bind path="redirectID">
					<input type="hidden" name="<c:out value='${status.expression}' />" value="" />
				</spring:bind>

				<spring:bind path="redirectFilterValues">
					<input type="hidden" name="<c:out value='${status.expression}' />" value="" />
				</spring:bind>

				<spring:bind path="token">
					<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
				</spring:bind>

				<spring:bind path="resultRevision">
					<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${resultRevision}' />" />
				</spring:bind>

				<spring:bind path="systemDate">
					<input type="hidden" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
				</spring:bind>

				<spring:bind path="isXLSXExport">
					<input type="hidden" id="isXLSXExport" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
				</spring:bind>

				<spring:bind path="isGLEdit">
					<input type="hidden" id="isGLEdit" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
				</spring:bind>

				<spring:bind path="pageLocale">
					<input type="hidden" id="pageLocale" name="<c:out value='${status.expression}' />" value="<c:out value='${status.value}' />" />
				</spring:bind>

				<!-- send back the list of filter ids and corresponding filter values -->
				<spring:bind path="filterIds">
					<!-- Tabbing removed from loop to prevent extra whitespace in html -->
					<c:forEach items="${filters}" var="filter" varStatus="varStatus">
						<input type="hidden" name="filterIds" value="<c:out value='${filter.id}' />"/>
					</c:forEach>
				</spring:bind>

				<spring:bind path="filterValueMap">
					<!-- Tabbing removed from loop to prevent extra whitespace in html -->
					<c:forEach items="${filters}" var="filter" varStatus="varStatus">
						<input type="hidden" name="filterValues" value="<c:out value='${status.value[filter.id]}' />"
						/>
					</c:forEach>
				</spring:bind>
			</spring:nestedPath>

			<table cellspacing="0" id="webform_table">
				<!-- Tabbing removed from loop to prevent extra whitespace in html -->
				<c:forEach items="${form.grid}" var="row" varStatus="rowStatus">
					<c:set var="spanning" value="false" />
					<c:set var="span" value="" />
					<tr>
						<c:forEach items="${row}" var="cell" varStatus="colStatus">
							<c:choose>
								<c:when test="${empty span}">
									<c:set var="span" value="${form.spans[rowStatus.index][colStatus.index]}" />
								</c:when>
								<c:otherwise>
									<c:if
										test="${colStatus.index > span.columnStart && colStatus.index <= span.columnEnd}">
										<c:set var="spanning" value="true" />
									</c:if>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${spanning}">
									<c:if test="${colStatus.index == span.columnEnd}">
										<c:set var="spanning" value="false" />
										<c:set var="span" value="" />
									</c:if>
								</c:when>
								<c:otherwise>
									<td <c:if test="${!empty span}">colspan="${span.length}"</c:if> class="webform_cell_style_${form.styleIdMap[cell.style.id]}"><c:choose>
		<c:when test="${empty cell.inputType}">
			<c:choose>
				<c:when test="${function:instanceOf(cell, 'com.varicent.webform.domain.ui.ResourceCell')}">
					<c:choose>
						<c:when test="${cell.image}"><img src="${cell.location}" alt="${cell.name}" /></c:when>
						<c:when test="${cell.link}"><a href="${cell.location}">${cell.name}</a></c:when>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${empty cell.webLink}">
							<c:choose>
								<c:when test="${cell.escapeContent}">
									<c:out value="${cell.content}" />
								</c:when>
								<c:otherwise>${cell.content}</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise><a href="${cell.webLink.document}?<c:forEach items=" ${cell.webLink.params}"
								var="linkParam">${linkParam.name}=<c:choose>
									<c:when test="${linkParam.type=='Constant'}">${linkParam.value}</c:when>
									<c:when test="${linkParam.type=='Scalar'}">
										<spring:bind path="command.scalarMap">${status.value[linkParam.value]}
										</spring:bind>
									</c:when>
									<c:when test="${linkParam.type=='RowValue'}">${linkParam.value}</c:when>
								</c:choose>&</c:forEach>">${cell.content}</a></c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<c:set var="isFormEditable" value="true" />
			<c:choose>
				<c:when test="${cell.inputType == 'SELECT'}"><span class="spanFilterValues"
						style="display:inline-block; overflow:hidden;"><select
							name="cell_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}"
							style="width: ${cell.options.width}em; text-align: ${fn:toLowerCase(cell.style.alignment)}; <c:if test='${cell.options.required}'>background-color: #fffdca;</c:if>" data-csp-rename="edit_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}"><option value="" <c:if
								test="${empty cell.content}">selected</c:if>></option>
							<c:forEach items="${cell.pickList}" var="option">
								<option value="<c:out value='${option.value}' />" <c:if
									test="${option.value == cell.content}">selected</c:if>>
								<c:out value="(${option.value}) ${option.name}" />
								</option>
							</c:forEach>
						</select></span></c:when>
				<c:when test="${cell.inputType == 'MULTI_SELECT'}">
					<c:set var="cellName" value="cell_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}" />
					<span class="spanFilterValues" style="display:inline-block; overflow:hidden;"><select
							multiple="multiple" id="<c:out value='${cellName}' />" name="<c:out value='${cellName}' />" style="width: ${cell.options.width}em; text-align:
						${fn:toLowerCase(cell.style.alignment)}; <c:if test='${cell.options.required}'>background-color: #fffdca;</c:if>" data-csp-rename="edit_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}"></select>
					</span>
					<script type="text/javascript">WebForm.observeMultiSelectCell($('${cellName}'));</script>
				</c:when>
				<c:when test="${cell.inputType == 'ADMIN'}">
					<c:set var="cellName" value="edit_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}" />
					<span id="group_${cell.elementId}"
						style="white-space: nowrap; width: ${cell.options.width}em; text-align: ${fn:toLowerCase(cell.style.alignment)}; <c:if test='${cell.options.required}'>background-color: #fffdca;</c:if>"><input type="radio"
							name="${cellName}" value="KEEP" checked="checked" /><img class="keep" src="<c:url value="
							/images/keep.png" />" title="
						<fmt:message key="webforms.admin.tooltip.keep" />" /><input type="radio" name="${cellName}"
							value="APPROVE" /><img class="approve" src="<c:url value='/images/approve.png' />" title="
						<fmt:message key="webforms.admin.tooltip.approve" />" /><input type="radio" name="${cellName}"
							value="REJECT" /><img class="reject" src="<c:url value='/images/reject.png' />"
						style="display:inline-block;" title="
						<fmt:message key="webforms.admin.tooltip.reject" />" />
					</span>
				</c:when>
				<c:when test="${cell.inputType == 'TEXTAREA'}"><textarea
						name="cell_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}"
						style="width: ${cell.options.width}em; text-align: ${fn:toLowerCase(cell.style.alignment)}; <c:if test='${cell.options.required}'>background-color: #fffdca;</c:if>" data-csp-rename="edit_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}" ><c:out value="${cell.content}" /></textarea>
				</c:when>
				<c:otherwise><input name="cell_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}"
						style="width: ${cell.options.width}em; text-align: ${fn:toLowerCase(cell.style.alignment)}; <c:if test='${cell.options.required}'>background-color: #fffdca;</c:if>" type="text" value="<c:out value='${cell.content}' />" data-csp-rename="edit_${cell.elementId}_${cell.row}_${cell.column}_${cell.keyId}" />
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
	</td>
	</c:otherwise>
	</c:choose>
	</c:forEach>
	</tr>
	</c:forEach>
	</table>

	<div class="actions">
		<c:if test="${isFormEditable}">
			<div class="action_item">
				<!--[if IE]><input type="text" style="display: none;" disabled="disabled" size="1" /><![endif]-->
				<input type="hidden" id="action_url_submit" value="<c:url value='/webforms/edit.do' />" />
				<input type="submit" id="button_submit" class="action_button" value="<fmt:message key='action.submit' />" />
			</div>
		</c:if>
		<div class="action_item">
			<input type="hidden" id="action_url_export" value="<c:url value='/webforms/export.do' />" />
			<input type="submit" id="button_export" class="action_button" value="<fmt:message key='action.exportToXls' />" />
			<input type="submit" id="button_export_XLSX" class="action_button" value="<fmt:message key='action.exportToXlsx' />" />
		</div>
		<c:if test="${canBatchExport}">
			<div class="action_item">
				<input type="hidden" id="action_url_export_batch" value="<c:url value='/webforms/exportBatch.do' />" />
				<input type="submit" id="button_export_batch" class="action_button" value="<fmt:message key='action.exportBatchToExcel' /> (.xls)" />
				<input type="submit" id="button_export_batch_XLSX" class="action_button" value="<fmt:message key='action.exportBatchToExcel' /> (.xlsx)" />
			</div>
		</c:if>
	</div>
	</form>
	</div>

	<c:if test="${!empty resultRevisionTime}">
		<p class="last_updated">
			<fmt:message key="webforms.resultsLastUpdated" />&nbsp;
			<fmt:formatDate value="${resultRevisionTime}" type="both" dateStyle="medium" timeStyle="short" />
		</p>
	</c:if>
	</c:if>
	</div>
