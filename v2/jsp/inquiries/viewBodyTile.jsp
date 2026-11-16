<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />
	<fmt:message var="escalateStr" key="inquiries.escalate" />
	<fmt:message var="returnStr" key="inquiries.return" />
	<fmt:message var="closeStr" key="inquiries.close" />
	<fmt:message var="resubmitStr" key="action.resubmit" />
	<c:set var="escalateStrLen" value="${fn:length(escalateStr)}" />
	<c:set var="returnStrLen" value="${fn:length(returnStr)}" />
	<c:set var="closeStrLen" value="${fn:length(closeStr)}" />
	<c:set var="resubmitStrLen" value="${fn:length(resubmitStr)}" />

	<c:if test="${!empty msgKey}">
		<p class="error" id="inquiryError">
			<fmt:message key="${msgKey}" />
		</p>
	</c:if>
	<c:if test="${!empty inquiry}">

		<div id="inquiry">



			<c:choose>
				<c:when test="${navMsg.nextOlder.category == 'ADMIN_MESSAGE'}">
					<c:url value="/messages/view.html" var="prevUrl">
						<c:param name="id" value="${navMsg.nextOlder.id}" />
						<c:param name="type" value="${navMsg.listType}" />
					</c:url>
				</c:when>
				<c:when test="${navMsg.nextOlder.category == 'ADMIN_PAYEE_MESSAGE'}">
					<c:url value="/messages/viewPayeeNote.html" var="prevUrl">
						<c:param name="id" value="${navMsg.nextOlder.id}" />
						<c:param name="type" value="${navMsg.listType}" />
					</c:url>
				</c:when>
				<c:when
					test="${(navMsg.nextOlder.category == 'INQUIRY_RETURNED') || (navMsg.nextOlder.category == 'INQUIRY_OPEN') || (navMsg.nextOlder.category == 'INQUIRY_ESCALATED') || (navMsg.nextOlder.category == 'INQUIRY_RESUBMITTED') || (navMsg.nextOlder.category == 'INQUIRY_COMMENTED') || (navMsg.nextOlder.category == 'INQUIRY_CLOSED')}">
					<c:url value="/inquiries/view.do" var="prevUrl">
						<c:param name="id" value="${navMsg.nextOlder.id}" />
						<c:param name="subtype" value="${subtype}" />
						<c:param name="status" value="${status}" />
						<c:param name="type" value="${navMsg.listType}" />
						<c:param name="token" value="${inquiry.token}" />

					</c:url>
				</c:when>
			</c:choose>
			<c:choose>
				<c:when test="${navMsg.nextNewer.category == 'ADMIN_MESSAGE'}">
					<c:url value="/messages/view.html" var="nextUrl">
						<c:param name="id" value="${navMsg.nextNewer.id}" />
						<c:param name="type" value="${navMsg.listType}" />
					</c:url>
				</c:when>
				<c:when test="${navMsg.nextNewer.category == 'ADMIN_PAYEE_MESSAGE'}">
					<c:url value="/messages/viewPayeeNote.html" var="nextUrl">
						<c:param name="id" value="${navMsg.nextNewer.id}" />
						<c:param name="type" value="${navMsg.listType}" />
					</c:url>
				</c:when>
				<c:when
					test="${(navMsg.nextNewer.category == 'INQUIRY_RETURNED') || (navMsg.nextNewer.category == 'INQUIRY_OPEN') || (navMsg.nextNewer.category == 'INQUIRY_ESCALATED') || (navMsg.nextNewer.category == 'INQUIRY_RESUBMITTED') || (navMsg.nextNewer.category == 'INQUIRY_COMMENTED') || (navMsg.nextNewer.category == 'INQUIRY_CLOSED')}">
					<c:url value="/inquiries/view.do" var="nextUrl">
						<c:param name="id" value="${navMsg.nextNewer.id}" />
						<c:param name="subtype" value="${subtype}" />
						<c:param name="status" value="${status}" />
						<c:param name="type" value="${navMsg.listType}" />
						<c:param name="token" value="${inquiry.token}" />
					</c:url>
				</c:when>
			</c:choose>

			<div id="header">
				<c:choose>
					<c:when test="${empty navMsg.nextOlder}">
						<div class="nav_btn_nopr"></div>
					</c:when>
					<c:otherwise>
						<a class="nav_btn_prev" href="${prevUrl}"></a>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${empty navMsg.nextNewer}">
						<div class="nav_btn_nonx"></div>
					</c:when>
					<c:otherwise>
						<a class="nav_btn_next" href="${nextUrl}"></a>
					</c:otherwise>
				</c:choose>
				<fmt:message key="inquiries.view.title" />
			</div>
			<div class="spacer"></div>
			<div id="left_panel">
				<div id="left_panel_wrapper">
					<div id="inquiry_details1">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td class="key">
									<fmt:message key="inquiries.view.id" />:
								</td>
								<td>
									<c:out value="${inquiry.id}" />
								</td>
							</tr>
							<tr>
								<td class="key">
									<fmt:message key="inquiries.object" />:
								</td>
								<td><a href="<c:url value='${inquiry.object.url}' />">
									<c:out value="${inquiry.object}" /></a>
								</td>
							</tr>
							<tr>
								<td class="key">
									<fmt:message key="inquiries.category" />:
								</td>
								<td>
									<c:choose>
										<c:when test="${empty categories[inquiry.category]}">
											<fmt:message key="${defaultCategories[inquiry.category].key}" />
										</c:when>
										<c:otherwise>
											<c:out value="${categories[inquiry.category]}" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</table>
					</div>

					<div id="inquiry_details2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td class="key">
									<fmt:message key="inquiries.author" />:
								</td>
								<td>
									<c:choose>
										<c:when test="${empty inquiry.creator.id}">
											<fmt:message key="messagecenter.messages.deletedUser" />
										</c:when>
										<c:otherwise>
											<c:out value="${inquiry.creator}" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td class="key">
									<fmt:message key="inquiries.created" />:
								</td>
								<td>
									<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
										value="${inquiry.created}" />
								</td>
							</tr>
							<tr>
								<td class="key">
									<fmt:message key="inquiries.updated" />:
								</td>
								<td>
									<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
										value="${inquiry.updated}" />
								</td>
							</tr>
							<tr>
								<td class="key">
									<fmt:message key="inquiries.assignee" />:
								</td>
								<td>
									<c:choose>
										<c:when test="${empty inquiry.assignee}">
											<fmt:message key="inquiries.assignee.none" />
										</c:when>
										<c:otherwise>
											<c:out value="${inquiry.assignee}" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
									<c:if test="${context.activeHandler}">
										<c:choose>
											<c:when test="${empty inquiry.assignee}">
												<form method="post" action="<c:url value='/inquiries/assign.do' />">
												<input type="hidden" name="id" value="<c:out value="
													${inquiry.id}" />"/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="assign_button"
													class="action_button_gradient round_left round_right"
													value="<fmt:message key='inquiries.assign' />" />
												</form>
											</c:when>
											<c:otherwise>
												<form method="post" action="<c:url value='/inquiries/unassign.do' />">
												<input type="hidden" name="id" value="<c:out value="
													${inquiry.id}" />"/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="assign_button"
													class="action_button_gradient round_left round_right"
													value="<fmt:message key='inquiries.unassign' />" />
												</form>
											</c:otherwise>
										</c:choose>
									</c:if>
								</td>
							</tr>
							<tr>
								<td class="key">
									<fmt:message key="inquiries.status" />:
								</td>
								<td>
									<c:choose>
										<c:when test="${inquiry.status == 'OPEN'}">
											<div id="status_icon" class="open">
												<div id="status_icon_gradient">
													<fmt:message key="inquiries.status.open" />
												</div>
											</div>
										</c:when>
										<c:when test="${inquiry.status == 'INVALID'}">
											<div id="status_icon" class="closed">
												<div id="status_icon_gradient">
													<fmt:message key="inquiries.status.invalid" />
												</div>
											</div>
										</c:when>
										<c:when test="${inquiry.status == 'FIXED'}">
											<div id="status_icon" class="closed">
												<div id="status_icon_gradient">
													<fmt:message key="inquiries.status.fixed" />
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div id="status_icon" class="closed">
												<div id="status_icon_gradient">
													<fmt:message key="inquiries.status.closed" />
												</div>
											</div>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
						</table>
					</div>

					<c:choose>
						<c:when test="${context.activeHandler}">
							<div id="inquiry_actions">
								<c:choose>
									<c:when test="${inquiry.status != 'CLOSED'}">
										<c:if test="${escalateStrLen > 8 || returnStrLen > 8 || closeStrLen > 8}"
											var="doNotFitInOneLine">
										</c:if>
										<c:choose>
											<c:when test="${doNotFitInOneLine}">
												<form method="post" action="<c:url value='/inquiries/escalate.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="escalate_button1"
													class="action_button_gradient round_left_top round_right_top"
													value="<fmt:message key='inquiries.escalate' />" />
												</form>
												<br />
												<form method="post" action="<c:url value='/inquiries/return.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="return_button1" class="action_button_gradient"
													value="<fmt:message key='inquiries.return' />" />
												</form>
												<br />
												<form method="post" action="<c:url value='/inquiries/close.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />"
												/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="close_button1"
													class="action_button_gradient round_right_bottom round_left_bottom"
													value="<fmt:message key='inquiries.close' />" />
												</form>
											</c:when>
											<c:otherwise>
												<form method="post" action="<c:url value='/inquiries/escalate.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="escalate_button3"
													class="action_button_gradient round_left" value="<fmt:message key="
													inquiries.escalate" />" />
												</form>
												<form method="post" action="<c:url value='/inquiries/return.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="return_button3" class="action_button_gradient"
													value="<fmt:message key='inquiries.return' />" />
												</form>
												<form method="post" action="<c:url value='/inquiries/close.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />"
												/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="close_button3"
													class="action_button_gradient round_right" value="<fmt:message key="
													inquiries.close" />" />
												</form>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<c:if test="${escalateStrLen > 11 || returnStrLen > 11}"
											var="doNotFitInOneLine">
										</c:if>
										<c:choose>
											<c:when test="${doNotFitInOneLine}">
												<form method="post" action="<c:url value='/inquiries/escalate.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="escalate_button1"
													class="action_button_gradient round_left_top round_right_top"
													value="<fmt:message key='inquiries.escalate' />" />
												</form>
												<br />
												<form method="post" action="<c:url value='/inquiries/return.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="return_button1"
													class="action_button_gradient round_left_bottom round_right_bottom"
													value="<fmt:message key='inquiries.return' />" />
												</form>
											</c:when>
											<c:otherwise>
												<form method="post" action="<c:url value='/inquiries/escalate.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="escalate_button2"
													class="action_button_gradient round_left" value="<fmt:message key="
													inquiries.escalate" />" />
												</form>
												<form method="post" action="<c:url value='/inquiries/return.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />">
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="return_button2"
													class="action_button_gradient round_right" value="<fmt:message key="
													inquiries.return" />" />
												</form>
											</c:otherwise>
										</c:choose>
										</form>
									</c:otherwise>
								</c:choose>
							</div>
						</c:when>
						<c:when test="${context.creator}">
							<c:choose>
								<c:when test="${inactive && hasInquiryTree && inquiry.status != 'CLOSED'}">
									<c:if test="${resubmitStrLen > 11 || closeStrLen > 11}" var="doNotFitInOneLine">
									</c:if>
									<c:choose>
										<c:when test="${doNotFitInOneLine}">
											<div id="inquiry_actions">
												<form method="post" action="<c:url value='/inquiries/resubmit.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />"
												/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="resubmit_button1"
													class="action_button_gradient round_left" value="<fmt:message key="
													action.resubmit" />" />
												</form>
												<br />
												<form method="post" action="<c:url value='/inquiries/close.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />"
												/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="close_button1"
													class="action_button_gradient round_right" value="<fmt:message key="
													inquiries.close" />" />
												</form>
											</div>
										</c:when>
										<c:otherwise>
											<div id="inquiry_actions">
												<form method="post" action="<c:url value='/inquiries/resubmit.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />"
												/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="resubmit_button2"
													class="action_button_gradient round_left" value="<fmt:message key="
													action.resubmit" />" />
												</form>
												<form method="post" action="<c:url value='/inquiries/close.do' />">
												<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />"
												/>
												<input type="hidden" name="token" value="<c:out value="
													${inquiry.token}" />" />
												<input type="submit" id="close_button2"
													class="action_button_gradient round_right" value="<fmt:message key="
													inquiries.close" />" />
												</form>
											</div>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:if test="${inactive && hasInquiryTree}">
										<div id="inquiry_actions">
											<form method="post" action="<c:url value='/inquiries/resubmit.do' />">
											<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />" />
											<input type="hidden" name="token" value="<c:out value="
												${inquiry.token}" />" />
											<input type="submit" id="resubmit_button1"
												class="action_button_gradient round_left round_right"
												value="<fmt:message key='action.resubmit' />" />
											</form>
										</div>
									</c:if>
									<c:if test="${inquiry.status != 'CLOSED'}">
										<div id="inquiry_actions">
											<form method="post" action="<c:url value='/inquiries/close.do' />">
											<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />" />
											<input type="hidden" name="token" value="<c:out value="
												${inquiry.token}" />" />
											<input type="submit" id="close_button1"
												class="action_button_gradient round_left round_right"
												value="<fmt:message key='inquiries.close' />" />
											</form>
										</div>
									</c:if>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:if test="${inquiry.status != 'CLOSED'}">
								<div id="inquiry_actions">
									<form method="post" action="<c:url value='/inquiries/close.do' />">
									<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />" />
									<input type="hidden" name="token" value="<c:out value=" ${inquiry.token}" />" />
									<input type="submit" id="close_button1"
										class="action_button_gradient round_left round_right" value="<fmt:message key="
										inquiries.close" />" />
									</form>
								</div>
							</c:if>
						</c:otherwise>
					</c:choose>

					<div id="inquiry_actions1_resubmit" style="display:none;">
						<input type="submit" id="resubmit_button1" class="action_button_gradient round_left round_right"
							value="Resubmit Inquiry" />
					</div>
				</div>
			</div>
			<div id="right_panel">

				<div id="comments">
					<div class="title2">
						<fmt:message key="inquiries.view.comments.title" />
					</div>
					<c:choose>
						<c:when test="${!empty comments}">
							<c:forEach items="${comments}" var="comment">
								<div class="comment_container">
									<div class="comment_details">
										<c:choose>
											<c:when test="${!empty comment.author}">
												<c:choose>
													<c:when test="${comment.author.id == null}">
														<span class="author_webuser">
															<fmt:message key="messagecenter.messages.deletedUser" />
														</span> - <span class="date">
															<fmt:formatDate type="both" dateStyle="short"
																timeStyle="short" value="${comment.created}" />
														</span>
													</c:when>
													<c:otherwise>
														<span class="author_webuser">
															<c:out value="${comment.author}" />
														</span> - <span class="date">
															<fmt:formatDate type="both" dateStyle="short"
																timeStyle="short" value="${comment.created}" />
														</span>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<span class="author_admin">
													<fmt:message key="messagecenter.messages.admin" />
												</span> - <span class="date">
													<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
														value="${comment.created}" />
												</span>
											</c:otherwise>
										</c:choose>
									</div>
									<div class="comment_body">
										<c:out value="${comment.text}" />
									</div>

								</div>
							</c:forEach>
							<div id="attachments">
								<span class="title3">
									<fmt:message key="inquiries.view.attachments.title" />:
								</span>
								<div class="attach-all">
									<c:choose>
										<c:when test="${!empty fileNames}">
											<c:forEach items="${fileNames}" var="fileName" varStatus="varStatus">
												<c:url value="/inquiries/download.do" var="downloadURL">
													<c:param name="id" value="${inquiry.id}" />
													<c:param name="index" value="${varStatus.index}" />
												</c:url>
												<c:url value="/inquiries/thumbnail.do" var="thumbURL">
													<c:param name="id" value="${inquiry.id}" />
													<c:param name="index" value="${varStatus.index}" />
												</c:url>
												<a href="${downloadURL}" title="${fileName}">
													<div class="attach-tab">
														<div class="attach-row">
															<c:out value="${fileName}" />
														</div>
													</div>
												</a>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<span>
												<fmt:message key="documents.none" />
											</span>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<p class="infobox_empty_msg">
								<fmt:message key="inquiries.view.noComments" />
							</p>
						</c:otherwise>
					</c:choose>
				</div>

				<c:if test="${inquiry.status != 'CLOSED'}">
					<div id="comment_submission">
						<div class="title2">
							<fmt:message key="inquiries.view.comments.leavecomment" />
						</div>
						<form method="post" action="<c:url value="
							/inquiries/view/update.do?id=${inquiry.id}&subtype=${subtype}&status=${status}" />"
						enctype="multipart/form-data" data-csp-validate="ValidateFileLength">
						<input type="hidden" name="id" value="<c:out value=" ${inquiry.id}" />" />
						<input type="hidden" name="token" value="<c:out value=" ${inquiry.token}" />" />
						<div id="browse_for_attachment">
							<div class="attach_button">
								<label id="attach_label">
									<input id="input_button" class="action_button_gradient round_left round_right"
										type="button" value="<fmt:message key='inquiries.view.attach' />"
								 data-csp-keypress-fn="attachFile" data-csp-keypress-args='[&quot;@event&quot;]'/>
									<input id="input_file" type="file" name="attachment" />
								</label>
							</div>
							<div id="attach_name_div"><span id="attach_name"></span><a id="attach_del">&times;</a></div>
						</div>
						<textarea id="comment_box" name="comment"></textarea>
						<input type="submit" id="add_comment_button"
							class="action_button_gradient round_left round_right" value="<fmt:message key="
							inquiries.view.addComment.title" />" />
						</form>
					</div>
				</c:if>
			</div>
			<div class="spacer"></div>
		</div>
	</c:if>
