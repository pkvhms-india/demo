<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="bpm_center">
	<div class="heading bpm">
		<fmt:message key="messagecenter.sectionLinks.bpm" />
	</div>
	<jsp:directive.include file="/WEB-INF/jsp/bpm/bpmTabs.jsp" />
	<table class="workflows bpm_table">
		<c:choose>
			<c:when test="${!empty command.items}">
				<tr class="header_row">
					<th id="workflowname"><fmt:message key="bpm.workflow.workflow" />
						<img data-csp-click-fn="sort_Click" data-csp-mouseover-fn="sort_MouseOver" data-csp-mouseover-args='[&quot;@this&quot;]'
					 data-csp-mouseover-fn="sort_MouseOut" data-csp-mouseover-args='[&quot;@this&quot;]'
						src="<c:choose><c:when test=" ${sortDescending}">
						<c:url value="/images/dropdown.png" />
			</c:when>
			<c:otherwise>
				<c:url value="/images/arrow_up.png" />
			</c:otherwise>
		</c:choose>" />
					</th>
					<th id="description"><fmt:message
							key="bpm.workflow.description" /></th>
				</tr>
				<c:forEach items="${command.items}" var="workflow"
					varStatus="rowCount">
					<tr
						class="workflow_row ${rowCount.index % 2 == 0 ? 'odd_row' : 'even_row'}"
					 data-csp-hover-row="1"
					 data-csp-nav="<c:out value='initialize.html?workflowID=${workflow.workflowID}' />">
						<td class="workflow_name_cell"><a
							href="<c:out value=" initialize.html?workflowID=${workflow.workflowID}" />"
							title="
				<c:out value="${workflow.name}" />"> <c:out
									value="${workflow.name}" />
						</a></td>
						<td class="token_description_cell" title="${workflow.description}">
							<c:out value="${workflow.description}" />
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td><fmt:message key="bpm.center.noworkflow" /></td>
				</tr>
			</c:otherwise>
		</c:choose>
	</table>
	<div class="table_footer">
		<div class="current_page">
			<c:if test="${!empty command.items}">
				<fmt:message key="bpm.tokenlist.of">
					<fmt:param value="${command.firstItem.rowNumber}" />
					<fmt:param value="${command.lastItem.rowNumber}" />
					<fmt:param value="${command.totalItems}" />
				</fmt:message>
			</c:if>
		</div>
		<div id="paging">


			<div id="btnPrev" class="btnPrev btnPaging"
				<c:choose>
        <c:when test="${!empty command.prevURL}">
            data-csp-nav="${command.prevURL}&sortDesc=${sortDescending}"
        </c:when>
        <c:otherwise>
            disabled
        </c:otherwise>
    </c:choose>>
				<img src="<c:url value='/images/arrow_prev.png' />" /> <span>
					<fmt:message key="messagecenter.navigation.previous" />
				</span>
			</div>



			<div id="btnNext" class="btnNext btnPaging"
				<c:choose>
        <c:when test="${!empty command.nextURL}">
            data-csp-nav="${command.nextURL}&sortDesc=${sortDescending}"
        </c:when>
        <c:otherwise>
            disabled
        </c:otherwise>
    </c:choose>>
				<span> <fmt:message key="messagecenter.navigation.next" />
				</span> <img src="<c:url value='/images/arrow_next.png' />" />
			</div>



		</div>
	</div>

</div>

<script type="text/javascript">
	function sort_MouseOver(imgElement) {
		jQuery(imgElement).addClass("sorting_hover");
	}

	function sort_MouseOut(imgElement) {
		jQuery(imgElement).removeClass("sorting_hover");
	}

	function sort_Click() {
		location.href = "index.html?start=${start}&sortDesc=${!sortDescending}";
	}

	jQuery(document).ready(function() {
		jQuery('input.datepickerInput').each(function() {
			var originalDate = jQuery(this).val();
			if (getIEVersionNumber() == 8 && originalDate != "") {
				jQuery(this).datepicker("setDate", originalDate);
			} else {
				jQuery(this).val(originalDate);
			}
		});
	});
</script>