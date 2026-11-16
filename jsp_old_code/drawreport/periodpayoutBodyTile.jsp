<%--

	Licensed Materials - Property of IBM
	IBM Product Name: 5725I02
	© Copyright IBM Corp. 2005, 2018
	U.S. Government Users Restricted Rights: Use, duplication or disclosure
	restricted by GSA ADP Schedule Contract with IBM Corp.

--%>

<jsp:directive.include file="/WEB-INF/includes/taglibs.jsp" />

<div id="drawreport">

<jsp:directive.include file="filter.jsp" />

<div id="periodpayout">

<div id="pageHeader">
	<h3><fmt:message key="draw.report.periodpayout.title"/></h3>
</div>

<div style="display:none;">
	<form id="exportOptions" name="exportOptions" enctype="multipart/form-data" method="post" action="<c:url value="/drawreport/periodpayout/export.do"/>">
		<spring:nestedPath path="command">
		
			<!-- filter.jsp has this same binding, but with the id "drawId", so we don't need an id here -->
			<spring:bind path="id">
				<input type="hidden" name="${status.expression}" value="<c:out value="${status.value}" />" />
			</spring:bind>
			
			<spring:bind path="selectedPayeeId" >
				<input type="hidden" name="${status.expression}" value="<c:out value="${status.value}" />" />
			</spring:bind>
			
			<spring:bind path="selectedGroupId" >
				<input type="hidden" name="${status.expression}" value="<c:out value="${status.value}" />" />
			</spring:bind>
			
			<spring:bind path="selectedPeriod">
				<input type="hidden" name="${status.expression}" value="<c:out value="${status.value}" />" />
			</spring:bind>
			
			<spring:bind path="selectedPeriodLevel">
				<input type="hidden" name="${status.expression}" value="<c:out value="${status.value}" />" />
			</spring:bind>
			
			<spring:bind path="selectedTemplate">
				<input type="hidden" name="${status.expression}" value="<c:out value="${status.value}" />" />
			</spring:bind>
			
			<spring:bind path="token">
				<input type="hidden" name="<c:out value="${status.expression}"/>" value="<c:out value="${status.value}"/>" />	
			</spring:bind>
			
			<!-- extra display options -->
			<div id="columnVisibilities"></div>
				
			<div id="exportSortColumn"></div>
			
			<div id="exportSortDirection"></div>
			
			<input type="hidden" id="isXLSXExport" name="isXLSXExport" value="false" />
						
		</spring:nestedPath>
	</form>
</div>

<div id="tableContainer" class="DataGridDiv"">
	<div id="showHideContainer" class="showHideContainer">
		<div class="showHideColImgDiv" id="dropdown-toggleDiv">
			<img class="showHideColImg" src="../images/triangle_down_blue.png" alt="Data Grid Options" title="<fmt:message key="draw.report.dropdown.title"/>" id="dropdown-toggle"/>
		</div>
		<div class="showHideColDDRelativePos">
			<div class="showHideColDD" style="border-color: ${command.reportColour};" id="dropdown-box">
				<div class="ExportToExcelDDTitle"><fmt:message key="draw.report.dropdown.exporttoexcel"/></div>
				<div class="exportExcelDDElement"><img class="exportDatagridToXLS" src="../images/icon_excel2003.png"/>
					(.xls)
				</div>
				<div class="exportExcelDDElement"><img class="exportDatagridToXLSX" src="../images/icon_excel.png"/>
					(.xlsx)
				</div>
				<div class="divhr"><hr class="ddseparator"/></div>
				<div class="showHideColDDElement">
					<input type="hidden" value="0"/>
					<img class="labelCheckImg" src="../images/checkmark.png" style=""/>
					<fmt:message key="draw.report.periodpayout.headers.payeeID"/>
				</div>
				<div class="showHideColDDElement">
					<input type="hidden" value="1"/>
					<img class="labelCheckImg" src="../images/checkmark.png" style=""/>
					<fmt:message key="draw.report.periodpayout.headers.payee"/>
				</div>
				<div class="showHideColDDElement">
					<input type="hidden" value="2"/>
					<img class="labelCheckImg" src="../images/checkmark.png" style=""/>
					<fmt:message key="draw.report.periodpayout.headers.period"/>
				</div>
				<div class="showHideColDDElement">
					<input type="hidden" value="3"/>
					<img class="labelCheckImg" src="../images/checkmark.png" style=""/>
					<fmt:message key="draw.report.periodpayout.headers.earnings"/>
				</div>
				<div class="showHideColDDElement">
					<input type="hidden" value="4"/>
					<img class="labelCheckImg" src="../images/checkmark.png" style=""/>
					<fmt:message key="draw.report.periodpayout.headers.draw"/>
				</div>
				<div class="showHideColDDElement">
					<input type="hidden" value="5"/>
					<img class="labelCheckImg" src="../images/checkmark.png" style=""/>
					<fmt:message key="draw.report.periodpayout.headers.periodDraw"/>
				</div>
				<div class="showHideColDDElement">
					<input type="hidden" value="6"/>
					<img class="labelCheckImg" src="../images/checkmark.png" style=""/>
					<fmt:message key="draw.report.periodpayout.headers.balance"/>
				</div>
				<div class="showHideColDDElement">
					<input type="hidden" value="7"/>
					<img class="labelCheckImg" src="../images/checkmark.png" style=""/>
					<fmt:message key="draw.report.periodpayout.headers.payout"/>
				</div>
			</div>
		</div>
	</div>
	<!-- table?? -->
	<table id="dataTable" class="drawreport_table" cellspacing="0" style="width:100%;">
		<thead>
			<tr style="color:${reportForeColour};background-color:${command.reportColour};">
				<th style="width:12.5%;">
					<div class="sortingLink sortingNone sortingAsc">
						<input type="hidden" value="PayeeID"/>
						<fmt:message key="draw.report.periodpayout.headers.payeeID"/>
					</div>
				</th>
				<th style="width:12.5%;">
					<div class="sortingLink sortingNone">
						<input type="hidden" value="Name"/>
						<fmt:message key="draw.report.periodpayout.headers.payee"/>
					</div>
				</th>
				<th style="width:12.5%;">
					<div class="sortingLink sortingNone">
						<input type="hidden" value="PeriodEnd"/>
						<fmt:message key="draw.report.periodpayout.headers.period"/>
					</div>
				</th>
				<th style="width:12.5%;">
					<div class="sortingLink sortingNone">
						<input type="hidden" value="Earnings"/>
						<fmt:message key="draw.report.periodpayout.headers.earnings"/>
					</div>
				</th>
				<th style="width:12.5%;">
					<div class="sortingLink sortingNone">
						<input type="hidden" value="Amount"/>
						<fmt:message key="draw.report.periodpayout.headers.draw"/>
					</div>
				</th>
				<th style="width:12.5%;">
					<div class="sortingLink sortingNone">
						<input type="hidden" value="Value"/>
						<fmt:message key="draw.report.periodpayout.headers.periodDraw"/>
					</div>
				</th>
				<th style="width:12.5%;">
					<div class="sortingLink sortingNone">
						<input type="hidden" value="Balance"/>
						<fmt:message key="draw.report.periodpayout.headers.balance"/>
					</div>
				</th>
				<th style="width:12.5%;">
					<div class="sortingLink sortingNone" style="margin-right: 25px;">
						<input type="hidden" value="Payout"/>
						<fmt:message key="draw.report.periodpayout.headers.payout"/>
					</div>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${payoutdetail}" var="row" varStatus="rowStatus">
			<tr
				<c:choose>
					<c:when test="${rowStatus.index % 2 == 0}">
						class="drawreport_table_row_even"
					</c:when>
					<c:otherwise>
						class="drawreport_table_row_odd"
					</c:otherwise>
				</c:choose>
			>
				<c:forEach items="${row.values}" var="item" varStatus="status">
					<td>
						<span					
							<c:if test="${status.index <= 1 && rowStatus.index > 0 && item == payoutdetail[rowStatus.index - 1].values[status.index]}">
							style="display:none;"
							</c:if> > 
						<c:out value="${item}" />
						</span>
					</td>
				</c:forEach>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<jsp:directive.include file="/WEB-INF/jsp/drawassignment/page-footer.jsp" />
	
	<table id="totalsTable" class="total" cellspacing="0" style="width:100%;">
		<tr>
			<td style="width:12.5%;">
				<fmt:message key="draw.report.datagrid.total"/>
			</td>
			<td style="width:12.5%;">
				<span>
					${payouttotals.values[0]}
				</span>
			</td>
			<td style="width:12.5%;">
				<c:if test="${empty command.selectedPeriod}">
					<fmt:message key="draw.report.filter.allPeriods" />
				</c:if>
				<c:out value="${command.selectedPeriod}"/>
			</td>
			<td style="width:12.5%;">
				${payouttotals.values[1]}
			</td>
			<td style="width:12.5%;">
			<!-- draw (DO NOT FILL) -->
			</td>
			<td style="width:12.5%;">
				${payouttotals.values[2]}
			</td>
			<td style="width:12.5%;">
				${payouttotals.values[3]}
			</td>
			<td style="width:12.5%;">
				${payouttotals.values[4]}
			</td>
		</tr>
	</table>
	
</div>

</div>
</div>

</html>
