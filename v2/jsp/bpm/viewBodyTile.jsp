<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
<div id="bpm_content">
	<div class="overlay"></div>
	<c:set var="Command" value="reportingCommand" />
	<c:if test="${empty error}">
		<table id="bpm_workflow_info">
			<tr>
				<th class="workflow_info_cell backbutton_container" rowspan="2">
					<c:choose>
						<c:when test="${!empty bpmTokenlistStart}">
							<c:set var="backbutton_href" value="tokenlist.html?start=${bpmTokenlistStart}" />
						</c:when>
						<c:otherwise>
							<c:set var="backbutton_href" value="tokenlist.html" />
						</c:otherwise>
					</c:choose>
					<div class="button" data-csp-nav="${backbutton_href}">
						<fmt:message key="bpm.backtolisting" />
					</div>
				</th>
				<th class="workflow_info_cell historybutton_container" rowspan="2">
					<div id="openButton" class="button">
						<fmt:message key="bpm.viewhistory" />
					</div>
				</th>
				<th class="workflow_info_cell report_name_column">
					<fmt:message key="bpm.reportname" />
				</th>
				<th class="workflow_info_cell submitted_by_column">
					<fmt:message key="bpm.submittedby" />
				</th>
				<th class="workflow_info_cell workflow_info_last_cell date_column">
					<fmt:message key="bpm.lastmodified" />
				</th>
			</tr>
			<tr class="header_cell">
				<c:choose>
					<c:when test="${not empty selectedToken}">
						<td class="workflow_info_cell" title="${selectedToken.report}">
							<c:out value="${selectedToken.report}" />
						</td>
					</c:when>
					<c:otherwise>
						<td class="workflow_info_cell"></td>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${not empty selectedToken}">
						<td class="workflow_info_cell" title="(${selectedToken.payee}) ${selectedToken.payeeName}">
							(
							<c:out value="${selectedToken.payee}" />)
							<c:out value="${selectedToken.payeeName}" />
						</td>
					</c:when>
					<c:otherwise>
						<td class="workflow_info_cell"></td>
					</c:otherwise>
				</c:choose>
				<td class="workflow_info_cell workflow_info_last_cell">
					<c:if test="${not empty selectedToken}">
						<fmt:formatDate value="${selectedToken.lastModified}" type="date" dateStyle="long" />
					</c:if>
				</td>
			</tr>
			<tr>
				<td id="bpm_action_paths_container" colspan="5" class="paths workflow_info_last_cell">
					<c:if test="${empty error}">
						<div class="bpm_centering_container">

							<c:if test="${not empty paths}">
								<div id="bpm_action_paths" class="bpm_centered_content">
									<c:forEach items="${paths}" var="path">
										<input class="action_button" type="button"
											data-csp-click-fn="displayActionConfirmation"
											data-csp-click-args='[${path.pathID}, ${bpmCommand.BPMTokenID}, ${bpmCommand.BPMTokenVersion}, "${path.javascriptName}", ${path.saveAndSubmit}]'
										class="action_button" name="P_
										<c:out value="${path.pathID}" />" value="
										<c:out value="${path.name}" />" />
									</c:forEach>
								</div>

								<div id="bpm_action_confirm_box" class="bpm_hidden">
									<form id="bpmPath" class="" name="bpmPath" method="post"
										enctype="multipart/form-data" action="<c:url value='/bpm/actionpath.do' />">

									<div id="bpm_action_name" class="bpm_action_name_component"></div>
									<div class="bpm_action_name_component">
										<input type="submit" class="action_button confirm_button" id="just_submit"
											value=<fmt:message key="bpm.workflow.confirm" /> />
										<button type="button" class="action_button confirm_button" id="save_and_submit"
										 data-csp-click-fn="confirmActionPathSaveSubmit" data-csp-click-arg="<c:url value='/bpm/savesubmit.do' />">
										<fmt:message key="bpm.workflow.confirm" /></button>

										<a href="#" data-csp-click-fn="cancelActionPath">
											<fmt:message key="bpm.workflow.cancel" />
										</a>
									</div>
									<input type="hidden" name="BPMTokenID" id="BPMTokenID" />
									<input type="hidden" name="BPMTokenVersion" id="BPMTokenVersion" />
									<input type="hidden" name="bpmPathID" id="bpmPathID" />
									<input type="hidden" name="token" value="${bpmCommand.token}" />
									</form>
								</div>
							</c:if>

						</div>
					</c:if>
				</td>
		</table>


		<div id="bpm_token_history">
			<jsp:directive.include file="/WEB-INF/jsp/bpm/progresstokenhistorytable.jsp" />
		</div>
	</c:if>

	<c:if test="${!empty reportingCommand}">
		<c:if test="${empty error}">
			<c:if test="${reportingCommand.canBatchExport}">
				<div id="export_form">
					<form name="pdfExport" id="batchPdfExportForm" method="post" enctype="multipart/form-data"
						action="<c:url value='/bpm/batch_export.do' />">
					<jsp:directive.include file="../common/batchExportInputs.jsp" />

					<input type="hidden" name="workflowID" value="<c:out value=" ${selectedToken.workflowID}" />" />
					<input type="hidden" name="bpmTokenID" value="<c:out value=" ${bpmCommand.BPMTokenID}" />" />

					<c:forEach items="${parameters}" var="pair">
						<input type="hidden" name="L_<c:out value=" ${pair.key}" />" value="
						<c:out value="${pair.value}" />" />
					</c:forEach>
					</form>
				</div>
			</c:if>
		</c:if>


		<div id="export_form">
			<form name="pdfExport" id="pdfExportForm" method="post" enctype="multipart/form-data" action="<c:url value="
				/bpm/export.do" />">
			<jsp:directive.include file="../common/exportInputs.jsp" />

			<input type="hidden" name="workflowID" value="<c:out value=" ${selectedToken.workflowID}" />" />
			<input type="hidden" name="bpmTokenID" value="<c:out value=" ${bpmCommand.BPMTokenID}" />" />
			<input type="hidden" name="exportName" />

			<c:forEach items="${parameters}" var="pair">
				<input type="hidden" name="L_${pair.key}" value="<c:out value=" ${pair.value}" />" />
			</c:forEach>

			</form>
		</div>
	</c:if>

	<div id="separator"></div>

	<div id="reporting_content">
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

		<c:if test="${!empty reportingCommand}">

			<form id="mainContent" name="mainContent" action="<c:url value="
				/bpm/view.html?bpmTokenID=${selectedToken.tokenID}" />" method="post">

			<input type="hidden" id="report_bpmPathID" name="report_bpmPathID" />
			<input type="hidden" id="report_bpmTokenID" name="report_bpmTokenID" />

			<jsp:directive.include file="../common/commonReportingContent.jsp" />

			<input type="hidden" name="bpmTokenVersion" value="${bpmCommand.BPMTokenVersion}" />

			${reporting_content}

			<input type="hidden" name="exportName" />

			<c:forEach items="${parameters}" var="pair">
				<input type="hidden" name="L_<c:out value=" ${pair.key}" />" value="
				<c:out value="${pair.value}" />" />
			</c:forEach>

			<input type="hidden" id="InputRuleValidationFailedMsgID" value="<fmt:message key="
				error.webreports.input.rule.validation" />" />
			<input type="hidden" id="InputRuleValidationErrorSubmitID" value="<fmt:message key="
				error.webreports.fix.input.rule.errors" />" />
			</form>
		</c:if>

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
		<tr:datepicker />
		<script type="text/javascript">

			jQuery(window).load(function () {
				setupReportSizeAccommodation(jQuery("#reporting_content").find("table"));

				jQuery('input.datepickerInput').each(function () {
					var originalDate = jQuery(this).val();
					if (getIEVersionNumber() == 8 && originalDate != "") {
						jQuery(this).datepicker("setDate", originalDate);
					} else {
						jQuery(this).val(originalDate);
					}
				});
			});

			window.onload = initialize;

			function initialize() {
				// regular export
				var exportForm = $('pdfExportForm');
				if (exportForm) {
					var exportAction = $('export-link');
					if (exportAction) {
						exportAction.observe('click', function () {
							exportForm.submit();
						});
					}
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

				document.getElementById("pdfExportForm").onsubmit = doExportSubmit;

				initializeParameterInputs(jQuery('#bpmPath'));

				jQuery('form[name="pdfExport"]').each(function () {
					initializeParameterInputs(jQuery(this));
				});

			}

			function initializeParameterInputs(targetForm) {
				var mainContent = jQuery('form#mainContent');
				mainContent.find('input[name^="P_"], select[name^="P_"]').each(function () {
					var hiddenInput = "<input type=\"hidden\" name=\"" + jQuery(this).attr('name') + "\" value=\"" + jQuery(this).val() + "\" />";
					targetForm.append(hiddenInput);
				});
			}

			function doExportSubmit(e) {
				try {
					e.preventDefault();
				} catch (ex) {event.returnValue = false;} //cancels default action in ie8, prevent default is supported in ie9
				PDFExportMessage = "<fmt:message key="reporting.PDFGeneration" />"
				ExportCharts();
			}

			function getActionConfirmationMessage(actionName) {
				return "<fmt:message key="bpm.workflow.confirmaction" /> (" + actionName + ") ";
			}

			function displayActionConfirmation(pathID, tokenID, tokenVersion, actionName, saveAndSubmit) {
				jQuery("#bpmPathID").val(pathID);
				jQuery("#BPMTokenID").val(tokenID);
				jQuery("#BPMTokenVersion").val(tokenVersion);
				jQuery("#bpm_action_name").attr("title", actionName);
				jQuery("#bpm_action_name").html(getActionConfirmationMessage(actionName));

				jQuery("#bpm_action_paths_container").addClass("paths_confirming");
				jQuery("#bpm_action_paths_container").removeClass("paths");
				jQuery("#bpm_action_paths").hide();
				jQuery("#bpm_action_confirm_box").removeClass("bpm_hidden");
				jQuery("#bpm_action_confirm_box").addClass("bpm_centered_content");

				jQuery("#report_bpmPathID").val(pathID);
				jQuery("#report_bpmTokenID").val(tokenID);

				if (saveAndSubmit) {
					jQuery("#just_submit").hide();
					jQuery("#save_and_submit").show();
				}
				else {
					jQuery("#just_submit").show();
					jQuery("#save_and_submit").hide();
				}
			}

			//Keep it?
			function confirmActionPath() {
				jQuery("#bpmPath").submit();
			}

			function confirmActionPathSaveSubmit(actionURL) {
				var tokenID = jQuery("#report_bpmTokenID").val();
				jQuery("#mainContent").attr("action", actionURL + "?bpmTokenID=" + tokenID);
				jQuery("#mainContent").submit();
			}

			function cancelActionPath() {
				jQuery("#bpm_action_paths_container").addClass("paths");
				jQuery("#bpm_action_paths_container").removeClass("paths_confirming");
				jQuery("#bpm_action_confirm_box").removeClass("bpm_centered_content");
				jQuery("#bpm_action_confirm_box").addClass("bpm_hidden");
				jQuery("#bpm_action_paths").show();

				jQuery("#report_bpmPathID").val("");
				jQuery("#report_bpmTokenID").val("");
			}

		</script>
	</div>
</div>