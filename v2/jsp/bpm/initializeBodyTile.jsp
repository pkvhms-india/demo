<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
<div id="bpm_content">
	<!-- Confirmation -->
	<c:set var="Command" value="command" />
	<div id="bpminitialize_controls">
		<c:choose>
			<c:when test="${(not empty reporting_content) || error != null}">
				<div id="bpm_initialize_controls" class="initialize_controls">
					<div class="name" title="${workflow.name}">
						<c:out value="${workflow.name}" />
					</div>
					<c:if test="${empty error}">
						<div class="start_button">
							<div class="form">
								<input type="button" class="action_button" data-csp-click-fn="startButton_Click"
									value="<fmt:message key='bpm.workflow.initialize.start' />"/>
							</div>
							<div class="caption" title="<fmt:message key='bpm.workflow.initialize.caption' />">
							<fmt:message key="bpm.workflow.initialize.caption" />
						</div>
				</div>

				</c:if>
				<div class="description" title="${workflow.description}">
					<c:out value="${workflow.description}" />
				</div>

	</div>
	<div id="divInitializeConfirmation" class="initialize_confirmation bpm_centering_container">
		<form id="bpmInitialize" class="bpm_centered_content" name="bpmInitialize" method="post"
			enctype="multipart/form-data" action="<c:url value='/bpm/createtoken.do' />">
		<div class="bpm_action_name_component">
			<fmt:message key="bpm.workflow.confirmaction" />
		</div>
		<div class="bpm_action_name_component">
			<c:if test="${!saveAndSubmit}">
				<input type="button" class="action_button" data-csp-click-fn="confirmButton_Click" value="<fmt:message key="
					bpm.workflow.confirm" />" />
			</c:if>
			<c:if test="${saveAndSubmit}">
				<input type="button" class="action_button" data-csp-click-fn="saveAndConfirmButton_Click" data-csp-click-arg="<c:url value='/bpm/saveandcreatetoken.do' />" value="
				<fmt:message key="bpm.workflow.confirm" />" />
			</c:if>
			<a href="#" data-csp-click-fn="cancelButton_Click">
				<fmt:message key="bpm.workflow.cancel" />
			</a>
		</div>
		<input type="hidden" name="workflowID" value="<c:out value=" ${workflow.workflowID}" />" />
		<spring:nestedPath path="command">
			<spring:bind path="token">
				<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
				<c:out value="${status.value}" />" />
			</spring:bind>
		</spring:nestedPath>
		</form>
	</div>
	</c:when>
	<c:otherwise>
		<div class="description">
			<fmt:message key="bpm.workflow.initialize.nopermission" />
		</div>
	</c:otherwise>
	</c:choose>
</div>

<!-- Report -->
<c:if test="${!empty command}">
	<c:if test="${command.canBatchExport}">
		<div id="export_form">
			<form name="pdfExport" id="batchPdfExportForm" method="post" enctype="multipart/form-data"
				action="<c:url value='/bpm/batch_export.do' />">
			<jsp:directive.include file="../common/batchExportInputs.jsp" />

			<c:forEach items="${parameters}" var="pair">
				<input type="hidden" name="L_<c:out value=" ${pair.key}" />" value="
				<c:out value="${pair.value}" />" />
			</c:forEach>

			<input type="hidden" name="workflowID" value="<c:out value=" ${workflow.workflowID}" />" />
			</form>
		</div>
	</c:if>

	<div id="export_form">
		<form name="pdfExport" id="pdfExportForm" method="post" enctype="multipart/form-data" action="<c:url value="
			/bpm/export.do" />">
		<jsp:directive.include file="../common/exportInputs.jsp" />

		<input type="hidden" name="workflowID" value="<c:out value=" ${workflow.workflowID}" />" />
		<input type="hidden" name="exportName" />

		<c:forEach items="${parameters}" var="pair">
			<input type="hidden" name="L_${pair.key}" value="<c:out value=" ${pair.value}" />" />
		</c:forEach>
		</form>
	</div>
	<div id="separator"></div>

	<div id="reporting_content">
		<form id="mainContent" name="mainContent" action="<c:url value="
			/bpm/initialize.html?workflowID=${workflow.workflowID}" />" method="post">

		<input type="hidden" name="workflowID" value="${workflow.workflowID}" />

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

				var mainContent = jQuery('form#mainContent');
				mainContent.find('input[name^="P_"], select[name^="P_"]').each(function () {
					var hiddenInput = "<input type=\"hidden\" name=\"" + jQuery(this).attr('name') + "\" value=\"" + jQuery(this).val() + "\" />";
					jQuery('#bpmInitialize').append(hiddenInput);
				});
			}

			function doExportSubmit(e) {
				try {
					e.preventDefault();
				} catch (ex) {event.returnValue = false;} //cancels default action in ie8, prevent default is supported in ie9
				PDFExportMessage = "<fmt:message key="reporting.PDFGeneration" />"
				ExportCharts();
			}

			function startButton_Click() {
				jQuery("#bpm_initialize_controls").hide();
				jQuery("#tdInitializeContainer").addClass("initialize_container_confirmation");
				jQuery("#divInitializeConfirmation").show();
			}

			function confirmButton_Click() {
				jQuery("#bpmInitialize").submit();
			}

			function saveAndConfirmButton_Click(actionURL) {
				jQuery("#mainContent").attr("action", actionURL);
				jQuery("#mainContent").submit();
			}

			function cancelButton_Click() {
				jQuery("#divInitializeConfirmation").hide();
				jQuery("#tdInitializeContainer").removeClass("initialize_container_confirmation");
				jQuery("#bpm_initialize_controls").show();
			}

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
		</script>

	</div>
</c:if>
<!-- End Report -->
</div>