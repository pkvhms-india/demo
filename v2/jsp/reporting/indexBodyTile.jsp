<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<c:if test="${PresenterCommand.concertMode}">
		<div class="concertToolbar">
			<div class="concertBtnContainer">
				<div class="concertCreateMetricBtn"></div>
			</div>
		</div>
	</c:if>

	<div id="reporting">
		<div>
			<c:if test="${!empty param.msgKey}">
				<p class="error" id="errorMsg" style="margin:0">
					<fmt:message key="${param.msgKey}" />
				</p>
			</c:if>
			<input type="hidden" id="invalidUrlMsg" value='<fmt:message key="reporting.invalidUrl"/>' />
		</div>
		<c:set var="Command" value="PresenterCommand" />
		<div id="inquiry_export_wrapper">
			<div id="inquiry_wrapper">
				<c:if test="${activeTab.module}">
					<div id="select_form">
						<c:set var="report_url" scope="page" value="/reporting/index.html${activeTab.parameterUrl}" />
						<form method="post" action="<c:url value='${report_url}' />">
						<spring:nestedPath path="PresenterCommand">
							<spring:bind path="reportChanged">
								<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
								<c:out value="${status.value}" />" />
							</spring:bind>

							<spring:bind path="token">
								<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
								<c:out value="${status.value}" />" />
							</spring:bind>

							<spring:bind path="id">
								<span class="spanFilterValues">
									<select id="report_select" name="<c:out value=" ${status.expression}" />"
								 data-csp-set-field="reportChanged" data-csp-set-value="true" data-csp-submit-change="1">
									<option value="-1" <c:if test="${status.value == webReportName.id}">selected
				</c:if>>
				<fmt:message key="reporting.defaultReportOption" />
				</option>
				<c:forEach items="${reportList}" var="webReportName">
					<option value="<c:out value=" ${webReportName.id}" />" <c:if
						test="${status.value == webReportName.id}">selected</c:if>>
					<c:out value="${webReportName}" />
					</option>
				</c:forEach>
				</select>
				</span>
				</spring:bind>
				</spring:nestedPath>
				</form>
			</div>
			</c:if>
		</div>

		<c:if test="${PresenterCommand.canBatchExport}">
			<div id="export_form">
				<form name="pdfExport" id="batchPdfExportForm" method="post" enctype="multipart/form-data"
					action="<c:url value='/reporting/batch_export.do' />">
				<jsp:directive.include file="../common/batchExportInputs.jsp" />

				<c:forEach items="${parameters}" var="pair">
					<input type="hidden" name="L_<c:out value=" ${pair.key}" />" value="
					<c:out value="${pair.value}" />" />
				</c:forEach>
				</form>
			</div>
		</c:if>

		<div id="export_form">
			<form name="pdfExport" id="pdfExportForm" method="post" enctype="multipart/form-data" action="<c:url value="
				/reporting/export.do" />">

			<jsp:directive.include file="../common/exportInputs.jsp" />
			<input type="hidden" name="exportName" />

			<c:forEach items="${parameters}" var="pair">
				<input type="hidden" name="L_${pair.key}" value="<c:out value=" ${pair.value}" />" />
			</c:forEach>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		window.onload = initialize;

		function initialize() {
			PDFExportMessage = "<fmt:message key="reporting.PDFGeneration" />"

			// regular export
			var exportForm = $('pdfExportForm');
			if (exportForm) {
				var exportAction = $('export-link');
				if (exportAction)
					exportAction.observe('click', ExportCharts);
			}

			// batch export
			var batchExportForm = $('batchPdfExportForm');
			if (batchExportForm) {
				var batchExportAction = $('batch-export-link');
				if (batchExportAction) {
					batchExportAction.observe('click', function () {
						batchExportForm.submit();
					});
				}
			}
		}
	</script>

	<div id="separator" style="min-height: 1px;"></div>
	<c:if test="${PresenterCommand.id > 0 && inquiriesEnabled}">

		<div id="inquiry_form" style="display:none">
			<c:url value="/inquiries/submit.do" var="inquriesSubmitUrl">
				<c:param name="objectId" value="${PresenterCommand.id}" />
				<c:param name="objectType" value="5" />
			</c:url>
			<c:if test="${!empty msgKey}">
				<p class="error">
					<fmt:message key="${msgKey}" />
				</p>
			</c:if>

			<form method="post" enctype="multipart/form-data" action="${inquriesSubmitUrl}"
			 data-csp-validate="ValidateInquiryInputs">
				<spring:nestedPath path="inquiryCommand">
					<div class="form_row">
						<div class="title">
							<span>
								<fmt:message key="inquiries.form.title" /><span>
						</div>
						<table cellspacing="0">
							<tr>
								<td>
									<fmt:message key="inquiries.category" />:&nbsp;
								</td>
								<td>
									<spring:bind path="category">
										<span class="spanFilterValues">
											<select name="category">
												<c:forEach items="${defaultCategories}" var="category">
													<option value="<c:out value=" ${category.id}" />">
													<fmt:message key="${category.key}" />
													</option>
												</c:forEach>
												<c:forEach items="${inquiryCategories}" var="category">
													<option value="<c:out value=" ${category.id}" />">
													<c:out value="${category}" />
													</option>
												</c:forEach>
											</select>
										</span>
									</spring:bind>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="inquiries.attachment" />:&nbsp;
								</td>
								<td>
									<spring:bind path="attachment">
										<input type="file" name="attachment" id="attachment" />
									</spring:bind>
									<spring:bind path="token">
										<input type="hidden" name="token" value="${inquiryCommand.token}" />
									</spring:bind>
									<span id="fileNameError">
										<fmt:message key="inquiries.fileNameError" />
									</span>
								</td>
							</tr>
						</table>
					</div>
					<div class="form_row">
						<spring:bind path="comment">
							<textarea id="comment" class="comment_box" name="comment"></textarea>
						</spring:bind>
						<p>
							<span id="commentLengthError">
								<fmt:message key="error.inquiry.comment" />
							</span>
						</p>
					</div>
					<div class="form_row">
						<input type="submit" class="action_button submit_button" value="<fmt:message key="
							action.submit" />" />
					</div>
				</spring:nestedPath>
			</form>
		</div>

	</c:if>
	<div id="reporting_content">
		<form name="mainContent" id="mainContent" action="<c:url value='/reporting/index.html' />" method="post">
		<jsp:directive.include file="../common/commonReportingContent.jsp" />

		<p class="error">
			<c:out value="${null}" escapeXml="false">
				<c:if test="${!empty error}">
					<fmt:message key="${error}">
						<c:forEach items="${errorParams}" varStatus="varStatus">
							<fmt:param>
								<tr:translate>${errorParams[varStatus.index]}</tr:translate>
							</fmt:param>
						</c:forEach>
					</fmt:message>
				</c:if>
			</c:out>
		</p>

		${content}

		<input type="hidden" id="copyInquiryTooltip" value="<fmt:message key='webreport.tooltip.copyToInquiry' />"/>
		<input type="hidden" name="exportName" />
		<input type="hidden" class="WebReportLocalization" replaceattr="title" key="ExportToExcel2003"
			value="<fmt:message key='webreport.title.exporttoexcel2003' />" />
		<input type="hidden" class="WebReportLocalization" replaceattr="title" key="ExportToExcel2007"
			value="<fmt:message key='webreport.title.exporttoexcel2007' />" />
		<input type="hidden" class="WebReportLocalization" replaceattr="title" key="DataGridOptions"
			value="<fmt:message key='webreport.datagrid.options.title' />" />
		<input type="hidden" class="WebReportLocalization" replaceattr="text" key="ExportToExcelDropdownTitle"
			value="<fmt:message key='webreport.datagrid.options.name' />" />
		<spring:bind path="PresenterCommand.pageLocale">
			<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
			<c:out value="${status.value}" />" />
		</spring:bind>

		<c:forEach items="${parameters}" var="pair">
			<input type="hidden" name="L_<c:out value=" ${pair.key}" />" value="
			<c:out value="${pair.value}" />" />
		</c:forEach>

		<input type="hidden" id="InputRuleValidationFailedMsgID" value="<fmt:message key="
			error.webreports.input.rule.validation" />" />
		<input type="hidden" id="InputRuleValidationErrorSubmitID" value="<fmt:message key="
			error.webreports.fix.input.rule.errors" />" />
		</form>

		<c:if test="${!empty javascript}">
			<script src="<c:url value='/scripts/jshashtable.js' />" type="text/javascript"></script>
			<script src="<c:url value='/scripts/jquery.numberformatter.min.js' />" type="text/javascript"></script>
			<script type="text/javascript">
				var pageFirstMessage = "<fmt:message key="pagination.First"/>";
				var pagePrevMessage = "<fmt:message key="pagination.Previous"/>";
				var pageNextMessage = "<fmt:message key="pagination.Next"/>";
				var pageLastMessage = "<fmt:message key="pagination.Last"/>";
				var pageDirtyMessage = "<fmt:message key="pagination.Dirty"/>";
				var pageSizeMessage = "<fmt:message key="pagination.PageSize"/>";
				var pageDirtyForSortMessage = "<fmt:message key="pagination.DirtyForSort"/>";
			</script>
			<script src="<c:url value='/scripts/varicent.pagination.js' />" type="text/javascript"></script>
			<script type="text/javascript">${javascript}</script>
		</c:if>

		<script src="<c:url value='/scripts/jquery-ui.min.js' />" type="text/javascript"></script>
		<tr:datepicker></tr:datepicker>
		<script type="text/javascript">
			jQuery(document).ready(function () {
				var i, iLen,
					localization = doQuerySelectorAll('input.WebReportLocalization');

				for (i = 0, iLen = localization.length; i < iLen; i++) {
					var input = localization[i],
						val = input.value,
						tag = jQuery(doQuerySelectorAll('[tag="' + input.getAttribute("key") + '"]'));

					if (tag.length) {
						if (input.getAttribute("replaceattr") === 'text') {
							tag.text(val);
						} else {
							tag.attr('replaceattr', val);
						}
					}
				}

				jQuery('input.datepickerInput').each(function () {
					var datePicker = jQuery(this),
						originalDate = datePicker.val();

					datePicker.datepicker('option', 'dateFormat', jQuery("#clientLocale").val().toLowerCase());
					datePicker.val(originalDate);
				});
			});
		</script>
	</div>
	</div>