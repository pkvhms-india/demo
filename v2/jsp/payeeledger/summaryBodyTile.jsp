<%-- Licensed Materials - Property of IBM IBM Product Name: 5725I02 © Copyright IBM Corp. 2005, 2015 U.S. Government
	Users Restricted Rights: Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp. --%>

	<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

	<c:url var="detail_url" scope="page" value="detail.html${activeTab.parameterUrl}">

		<c:if test="${!empty command.planId}">
			<c:param name="plan" value="${command.planId}" />

			<c:if test="${!empty command.payeeId}">
				<c:param name="payee" value="${command.payeeId}" />
			</c:if>

			<c:if test="${!empty command.periodIndex}">
				<c:param name="period" value="${command.periodIndex}" />
			</c:if>
		</c:if>

	</c:url>

	<div id="ledger">
		<div id="ledger_type"><a href="${detail_url}">
				<fmt:message key="menu.detail" />
			</a> &mdash;
			<fmt:message key="menu.summary" />
		</div>

		<jsp:directive.include file="selector.jsp" />

		<c:if test="${!empty rows}">
			<jsp:directive.include file="header.jsp" />

			<div id="summary">
				<table cellspacing="0">
					<thead>
						<tr>
							<th class="date_header">
								<fmt:message key="payeeledger.summary.period" />
							</th>
							<th class="value_header">
								<fmt:message key="payeeledger.summary.earnings" />
							</th>
							<th class="value_header">
								<fmt:message key="payeeledger.summary.netAdjustments" />
							</th>
							<th class="value_header">
								<fmt:message key="payeeledger.summary.netEarnings" />
							</th>
							<th class="value_header">
								<fmt:message key="payeeledger.summary.pending" />
							</th>
							<th class="value_header">
								<fmt:message key="payeeledger.summary.payment" />
							</th>
						</tr>
					</thead>

					<tfoot>
						<tr>
							<td colspan="6"></td>
						</tr>
					</tfoot>

					<c:forEach items="${rows}" var="row">
						<tbody>
							<tr <c:if test="${row.date == null}">class="total"
		</c:if>>
		<c:choose>
			<c:when test="${row.date == null}">
				<td class="total_header">
					<fmt:message key="payeeledger.summary.grandTotal" />
				</td>
			</c:when>
			<c:otherwise>
				<td class="date">
					<c:out value="${row.period}" />
				</td>
			</c:otherwise>
		</c:choose>
		<td class="value">
			<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row.earnings}" />
		</td>
		<td class="value">
			<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row.netAdjustments}" />
			<c:if test="${!empty row.adjustments}">
				<img class="adjustments" src="<c:url value='/images/arrow_down.png' />" />
			</c:if>
		</td>
		<td class="value">
			<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row.netEarnings}" />
		</td>
		<td class="value">
			<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row.pending}" />
		</td>
		<td class="value">
			<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row.payment}" />
		</td>
		<td class="value">
			<c:if test="${canAdjust && !isSelf && !row.locked && (row.date != null)}">
				<c:url value="/ledger/adjustment.html" var="ledgerURL">
					<c:param name="plan" value="${command.planId}" />
					<c:param name="payee" value="${command.payeeId}" />
					<c:param name="period" value="${row.period.name}" />
				</c:url>
				<form method="post" action="${ledgerURL}">
					<input type="submit" class="action_button" value="<fmt:message key="
						payeeledger.summary.actions.adjust" />" />
				</form>
			</c:if>
		</td>
		</tr>
		<c:if test="${!empty row.adjustments}">
			<c:forEach items="${row.adjustments}" var="adjustment">
				<tr class="adjustment_row">
					<td colspan="2">
						<c:choose>
							<c:when test="${adjustment.manual}">
								<fmt:message key="payeeledger.summary.adjustment.manual" />
							</c:when>
							<c:otherwise>
								<fmt:message key="payeeledger.summary.adjustment.allocation" />
							</c:otherwise>
						</c:choose>
					</td>
					<td class="value">
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2"
							value="${adjustment.value}" />
						<c:if test="${!empty row.adjustments}">
							<img class="adjustmentInfo" src="<c:url value='/images/info.png' />" />
						</c:if>
					</td>
					<td colspan="3"></td>
				</tr>
				<tr class="adjustment_details_row">
					<td colspan="6">
						<table cellspacing="0">
							<tr>
								<td class="details_header">
									<fmt:message key="payeeledger.summary.adjustment.createdBy" />
								</td>
								<td>
									<c:out value="${adjustment.createdBy}" />
								</td>
							</tr>
							<tr>
								<td class="details_header">
									<fmt:message key="payeeledger.summary.adjustment.createdAt" />
								</td>
								<td>
									<fmt:formatDate type="both" dateStyle="short" timeStyle="short"
										value="${adjustment.createdAt}" />
								</td>
							</tr>
							<c:if test="${!empty adjustment.comment}">
								<tr>
									<td class="details_header">
										<fmt:message key="payeeledger.summary.adjustment.comment" />
									</td>
									<td>
										<c:out value="${adjustment.comment}" />
									</td>
								</tr>
							</c:if>
						</table>
					</td>
				</tr>
			</c:forEach>
		</c:if>
		</tbody>
		</c:forEach>
		</table>
	</div>
	</c:if>

	</div>
