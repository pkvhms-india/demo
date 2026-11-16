<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<div id="ledger_header">

		<div class="actions">
			<c:if test="${!manualSubmit}">
				<div class="action_item">
					<c:url value="/ledger/${type}/export.do" var="ledgerURL" />

					<form method="post" action="${ledgerURL}">
						<spring:bind path="command.planId">
							<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
							<c:out value="${command.planId}" />" />
						</spring:bind>
						<spring:bind path="command.payeeId">
							<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
							<c:out value="${command.payeeId}" />" />
						</spring:bind>
						<spring:bind path="command.periodIndex">
							<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
							<c:out value="${command.periodIndex}" />" />
						</spring:bind>
						<spring:bind path="command.isXLSXExport">
							<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
							<c:out value="${status.value}" />" />
						</spring:bind>
						<spring:bind path="command.token">
							<input type="hidden" name="<c:out value=" ${status.expression}" />" value="
							<c:out value="${status.value}" />" />
						</spring:bind>

						<input type="submit" class="action_button" data-csp-set-field="isXLSXExport" data-csp-set-value="false" data-csp-on-click-set="1"
							value="<fmt:message key='action.exportToXls' />" />
						<input type="submit" class="action_button" data-csp-set-field="isXLSXExport" data-csp-set-value="true" data-csp-on-click-set="1"
							value="<fmt:message key='action.exportToXlsx' />" />
					</form>
				</div>
			</c:if>
		</div>

		<c:if test="${!empty command.planId && inquiriesEnabled}">
			<div id="inquiry_form" style="display:none">
				<c:url value="/inquiries/submit.do" var="inquiriesURL">
					<c:param name="objectId" value="${command.planId}" />
					<c:param name="objectType" value="1" />
					<c:param name="token" value="${command.token}" />
				</c:url>
				<form method="post" enctype="multipart/form-data" action="${inquiriesURL}"
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
							<p><span id="commentLengthError">
									<fmt:message key="error.inquiry.comment" />
								</span></p>
						</div>
						<div class="form_row">
							<input type="submit" class="action_button submit_button" value="<fmt:message key="
								action.submit" />" />
						</div>
					</spring:nestedPath>
				</form>
			</div>
		</c:if>

		<c:if test="${!empty param.error}">
			<div id="error_row" class="error">
				<fmt:message key="${param.error}" />
			</div>
		</c:if>

	</div>
