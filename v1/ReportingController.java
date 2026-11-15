package com.varicent.reporting.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.StringMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.varicent.BPM.service.BPMManager;
import com.varicent.common.payee.domain.Payee;
import com.varicent.common.pdf.PdfDocument;
import com.varicent.common.tab.domain.Tab;
import com.varicent.common.tab.domain.TabGroup;
import com.varicent.common.tab.domain.TabObject;
import com.varicent.common.tab.service.TabManager;
import com.varicent.common.ui.domain.SelectOption;
import com.varicent.concert.service.ConcertManager;
import com.varicent.core.exceptions.ReportOverLimitException;
import com.varicent.core.i18n.Messages;
import com.varicent.core.text.Format;
import com.varicent.core.util.Download;
import com.varicent.core.util.Entry;
import com.varicent.core.util.HttpRequestUtils;
import com.varicent.core.util.XPathWrapper;
import com.varicent.reporting.domain.Image;
import com.varicent.reporting.domain.Rendering;
import com.varicent.reporting.domain.ReportData;
import com.varicent.reporting.domain.WebReportName;
import com.varicent.reporting.domain.exceptions.ExpandXmlException;
import com.varicent.reporting.service.ReportingManager;
import com.varicent.reporting.web.view.ReportingDatagridExcelView;
import com.varicent.reporting.web.view.ReportingPdfView;
import com.varicent.security.domain.UserAccount;
import com.varicent.workflow.domain.WorkflowObject;
import com.varicent.workflow.service.WorkflowManager;
import com.varicent.workflow.web.InquiryCommand;

@Controller
public class ReportingController {
	private static final String VIEW = "reporting/index";
	private static final String PDF_VIEW = "reportingPdfView";
	private static final String REPORT_ID_PARAM = "ReportID";
	private static final String CONCERT_MODE_PARAM = "concertMode";
	private static final String DATAGRID_EXPORT_FILENAME_XLS = "Datagrid-{0}.xls";
	private static final String DATAGRID_EXPORT_FILENAME_XLSX = "Datagrid-{0}.xlsx";
	private static final String FIXEDGRID_EXPORT_FILENAME_XLS = "Fixedgrid-{0}.xls";
	private static final String FIXEDGRID_EXPORT_FILENAME_XLSX = "Fixedgrid-{0}.xlsx";
	private static final String DATAGRID_EXCEL_VIEW = "reportingDatagridExcelView";
	private static final String FIXEDGRID_EXCEL_VIEW = "reportingFixedgridExcelView";
	private static final String ACTIVE_TAB_ATTRIBUTE = "activeTab";
	private static final String SHOW_INQUIRY = "showInquiry";
	public static final String SHOW_BATCH_EXPORT_TO_PDF = "showBatchExportToPdf";
	public static final String SHOW_EXPORT_TO_PDF = "showExportToPdf";
	private final ReportingManager reportingManager;
	private final WorkflowManager workflowManager;
	private final BPMManager bpmManager;
	private final Messages messages;
	private final ConcertManager concertManager;
	@Autowired
	private TabManager tabMgr;

	@Autowired
	public ReportingController(ReportingManager reportingManager, ConcertManager concertManager,
			WorkflowManager workflowManager, BPMManager bpmManager, Messages messages) {
		this.reportingManager = reportingManager;
		this.workflowManager = workflowManager;
		this.bpmManager = bpmManager;
		this.messages = messages;
		this.concertManager = concertManager;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringMultipartFileEditor());
	}

	@RequestMapping({ "/reporting/export.do" })
	public ModelAndView export_reporting(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		return this.export(cmd, request, response, false, new HashMap(), 0, -1);
	}

	@RequestMapping(value = { "/reporting/gettabs.do" }, method = { RequestMethod.GET })
	@ResponseBody
	public String getTabs() {
		List<Tab> userTabs = this.tabMgr.getTabs();
		List<Tab> filteredTabs = new ArrayList();

		for (int i = 0; i < userTabs.size(); ++i) {
			Tab userTab = userTabs.get(i);
			if (userTab.isTabGroup()) {
				List<Tab> subTabs = ((TabGroup) userTab).getSubTabs();

				for (int j = 0; j < subTabs.size(); ++j) {
					Tab subTab = subTabs.get(j);
					if (!subTab.isExternalURL()) {
						filteredTabs.add(subTab);
					}
				}
			} else if (!userTab.isExternalURL()) {
				filteredTabs.add(userTab);
			}
		}

		StringBuffer sb = new StringBuffer();
		sb.append("[");

		for (int i = 0; i < filteredTabs.size(); ++i) {
			this.writeTabInfo(sb, filteredTabs.get(i), i == filteredTabs.size() - 1);
		}

		sb.append("]");
		return sb.toString();
	}

	private void writeTabInfo(StringBuffer sb, Tab tab, boolean isLastItem) {
		sb.append("{\"id\":");
		sb.append(tab.getId());
		sb.append(",\"name\":\"");
		sb.append(tab.getName());
		sb.append("\",\"url\":\"");
		sb.append(tab.getUrl());
		sb.append("\"}");
		if (!isLastItem) {
			sb.append(",");
		}

	}

	@RequestMapping(value = { "/reporting/getusertabs.do" }, params = { "userId" }, method = { RequestMethod.GET })
	@ResponseBody
	public String getUserTabs(@RequestParam String userId) {
		List<Integer> viewableTabs = this.concertManager.getTabIds(userId);
		String response = "[";

		for (int i = 0; i < viewableTabs.size(); ++i) {
			response = response + viewableTabs.get(i).toString();
			if (i != viewableTabs.size() - 1) {
				response = response + ",";
			}
		}

		response = response + "]";
		return response;
	}

	@RequestMapping(value = { "/reporting/checkuserpermissions.do" }, params = { "userId", "reportId" }, method = {
			RequestMethod.GET })
	@ResponseBody
	public Boolean checkUserPermissions(@RequestParam String userId, @RequestParam Integer reportId) {
		Payee user = this.concertManager.getPayeeByEmail(userId);
		return user != null && this.concertManager.isViewableReport(reportId, user.getId()) ? true : false;
	}

	@RequestMapping(value = { "/reporting/scalarValue.do" }, params = { "reportId", "sourceName",
			"userEmail" }, method = { RequestMethod.GET })
	@ResponseBody
	public String scalarValue(@RequestParam Integer reportId, @RequestParam String sourceName,
			@RequestParam String userEmail) {
		if (!this.reportingManager.isViewableReport(reportId)) {
			return "{\"error\":\"Report not available or not accessible\"}";
		} else {
			Payee user = this.concertManager.getPayeeByEmail(userEmail);
			ReportData reportData = this.populateData(reportId, sourceName, user.getId());
			if (reportData.errorMessage != null) {
				return "{\"error\":\"" + reportData.errorMessage.getKey().toString() + "\"}";
			} else {
				Object valObj = reportData.data.get(sourceName);
				if (valObj == null) {
					return "{\"error\":\"Requested value not returned by report\"}";
				} else if (valObj instanceof BigDecimal) {
					String valueJson = String.valueOf(valObj);
					return "{\"reportId\":" + reportId + ", \"valueName\":\"" + sourceName + "\", \"value\":"
							+ valueJson + "}";
				} else {
					return "{\"error\":\"Unsupported value type returned by report\"}";
				}
			}
		}
	}

	public ModelAndView export(@ModelAttribute("PresenterCommand") ReportingCommand cmd, HttpServletRequest request,
			HttpServletResponse response, boolean forBPM, HashMap<String, Boolean> isViewableMap, int bpmFormID,
			int tokenID) {
		new ModelAndView("reporting/index");
		if (!cmd.hasId()) {
			return this.createEmptyView();
		} else if (!cmd.checkAndUpdateToken()) {
			return this.createEmptyView();
		} else {
			int reportId = cmd.getId();
			String exportName = request.getParameter("exportName");

			org.jdom.Document doc;
			try {
				doc = this.reportingManager.getExpandedXml(reportId, this.getParams(request, true),
						HttpRequestUtils.getVisibleTabIdWorkflowObjectIdMap(request), cmd, forBPM, isViewableMap,
						bpmFormID, tokenID, true);
			} catch (ExpandXmlException ex) {
				ModelAndView mav = new ModelAndView("reporting/index");
				if (this.causedByReportOverLimit(ex)) {
					mav.addObject("datagridContent", Messages.Key.REPORTING_ERROR_OVER_LIMIT.toString());
					mav.addObject("datagridName", exportName);
				} else {
					Rendering rendering = ex.getErrorPage();
					mav.addObject("styles", rendering.getStyles());
					mav.addObject("content", rendering.getContent());
				}

				return mav;
			}

			if (doc == null) {
				return this.createEmptyView();
			} else {
				HashMap<String, String> chartStreamMap = new HashMap();

				for (int i = 0; i < cmd.getChartFileNames().size(); ++i) {
					chartStreamMap.put(cmd.getChartFileNames().get(i), cmd.getChartStreams().get(i));
				}

				List<Image> images = this.reportingManager.getImages(reportId);
				ModelAndView var17 = new ModelAndView("reportingPdfView");
				var17.addObject("reportId", reportId);
				var17.addObject("document", doc);
				var17.addObject("images", images);
				var17.addObject("chartStreamMap", chartStreamMap);
				XMLOutputter outputter = new XMLOutputter();
				outputter.outputString(doc);
				String fileName = this.getReportName(reportId) + ".pdf";
				Download.asAttachment(response, request, Download.FileType.PDF, fileName);
				return var17;
			}
		}
	}

	@RequestMapping(value = { "/reporting/reporting/getFiltered.do" }, method = { RequestMethod.POST })
	@ResponseBody
	public List<SelectOption> getFilteredJSON(HttpServletResponse response, @RequestParam String reportIdString,
			@RequestParam String[] restrictParams, @RequestParam String[] restrictValues,
			@RequestParam String sourceTable, @RequestParam String sourceIDColumn,
			@RequestParam String sourceNameColumn, @RequestParam String targetPL, @RequestParam String sortCol,
			@RequestParam String sortDescending) throws XMLStreamException, ReportOverLimitException {
		int reportId = Integer.parseInt(reportIdString);
		List<SelectOption> result = this.reportingManager.getFiltered(reportId, restrictParams, restrictValues,
				sourceTable, sourceIDColumn, sourceNameColumn, targetPL, sortCol, sortDescending, false, true);
		return result;
	}

	@RequestMapping(value = { "/reporting/reporting/searchField.do" }, method = { RequestMethod.POST })
	@ResponseBody
	public List<SelectOption> searchFieldJSON(HttpServletResponse response, @RequestParam String reportIdString,
			@RequestParam String searchString, @RequestParam String sourceTable, @RequestParam String sourceNameColumn,
			@RequestParam Map<String, Object> params) throws XMLStreamException, ReportOverLimitException {
		int reportId = Integer.parseInt(reportIdString);
		List<SelectOption> result = this.reportingManager.searchField(reportId, searchString, sourceTable,
				sourceNameColumn, params, false);
		return result;
	}

	@RequestMapping({ "/reporting/batch_export.do" })
	public ModelAndView batch_export_reporting(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) throws DocumentException, IOException {
		return this.batch_export(cmd, request, response, false, new HashMap(), 0);
	}

	public ModelAndView batch_export(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response, boolean forBPM,
			HashMap<String, Boolean> isViewableMap, int bpmFormID) throws DocumentException, IOException {
		ModelAndView mav = new ModelAndView("reporting/index");
		if (!cmd.hasId()) {
			return this.createEmptyView();
		} else if (!cmd.checkAndUpdateToken()) {
			return this.createEmptyView();
		} else {
			cmd.addReferencesTable(this.reportingManager.getPayeeIDReferencedPort());
			int reportId = cmd.getId();

			org.jdom.Document doc;
			try {
				doc = this.reportingManager.getExpandedXml(reportId, this.getParams(request, true),
						HttpRequestUtils.getVisibleTabIdWorkflowObjectIdMap(request), cmd, forBPM, isViewableMap,
						bpmFormID, -1);
			} catch (ExpandXmlException ex) {
				Rendering rendering = ex.getErrorPage();
				mav.addObject("styles", rendering.getStyles());
				mav.addObject("content", rendering.getContent());
				return mav;
			}

			if (doc == null) {
				return this.createEmptyView();
			} else {
				List<Image> images = this.reportingManager.getImages(reportId);
				mav = new ModelAndView("reportingPdfView");
				mav.addObject("reportId", reportId);
				mav.addObject("document", doc);
				mav.addObject("images", images);
				mav.addObject("chartStreamMap", (Object) null);
				String fileName = this.getReportName(reportId) + ".zip";
				Download.asAttachment(response, request, Download.FileType.ZIP, fileName);
				ZipArchiveOutputStream zip = new ZipArchiveOutputStream(response.getOutputStream());
				zip.setEncoding("UTF-8");
				Set<String> usedNames = new HashSet();

				for (Entry<String, String> entry : cmd.getPicklistPayeeIDs()) {
					try {
						doc = this.reportingManager.getExpandedXml(reportId,
								this.getParamsAt(request, true, entry.Key, cmd.getParameter()),
								HttpRequestUtils.getVisibleTabIdWorkflowObjectIdMap(request), cmd, forBPM,
								isViewableMap, bpmFormID, -1);
					} catch (ExpandXmlException var22) {
						continue;
					}

					if (doc != null) {
						String pdfFileName = getSafeFileName(entry.Value + ", " + entry.Key);
						pdfFileName = getUniqueName(pdfFileName, usedNames);
						pdfFileName = pdfFileName + ".pdf";
						usedNames.add(pdfFileName);
						ZipArchiveEntry ze = new ZipArchiveEntry(pdfFileName);
						zip.putArchiveEntry(ze);
						Document document = new Document();
						PdfWriter writer = PdfWriter.getInstance(document, zip);
						writer.setCloseStream(false);
						PdfDocument pdf = new PdfDocument();
						ReportingPdfView.buildDocument(doc, images, request, pdf, (HashMap) null);
						pdf.addPdfMetadata(document);
						document.open();
						pdf.buildPdf(document, writer, this.messages);
						document.close();
						zip.closeArchiveEntry();
					}
				}

				zip.close();
				return null;
			}
		}
	}

	private static String getSafeFileName(String filename) {
		return filename.replaceAll("[\\\\/:\\*?\"<>|]", "_");
	}

	private static String getUniqueName(String name, Set<String> usedNames) {
		String newName = name;

		for (int suffix = 1; usedNames.contains(newName); ++suffix) {
			newName = newName + "(" + suffix + ")";
		}

		usedNames.add(newName);
		return newName;
	}

	private String getReportName(int reportID) {
		return this.reportingManager.getReportName(reportID);
	}

	ModelAndView createEmptyView() {
		ModelAndView mav = new ModelAndView("reporting/index");
		ReportingCommand cmd = new ReportingCommand();
		cmd.setCanExport(false);
		cmd.setCanBatchExport(false);
		cmd.updateToken();
		mav.addObject("PresenterCommand", cmd);
		mav.addObject("inquiriesEnabled", false);
		return mav;
	}

	ModelAndView createViewWithMessage(String messageID, String param) {
		ModelAndView mav = new ModelAndView("reporting/index");
		ReportingCommand cmd = new ReportingCommand();
		cmd.setCanExport(false);
		cmd.setCanBatchExport(false);
		cmd.updateToken();
		mav.addObject("PresenterCommand", cmd);
		mav.addObject("reportingMessage", messageID);
		if (param != null) {
			mav.addObject("reportingMessageParam", param);
		}

		mav.addObject("inquiriesEnabled", false);
		return mav;
	}

	@RequestMapping({ "/reporting/exportDatagridToXLS.do" })
	public ModelAndView exportDatagridToXLS_reporting(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		return this.exportDatagridToExcel(cmd, request, response, false, false, 0);
	}

	@RequestMapping({ "/reporting/exportDatagridToXLSX.do" })
	public ModelAndView exportDatagridToXLSX_reporting(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		return this.exportDatagridToExcel(cmd, request, response, true, false, 0);
	}

	public ModelAndView exportDatagridToExcel(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response, boolean isXLSXExport, boolean forBPM,
			int bpmFormID) {
		if (!cmd.hasId()) {
			return this.createEmptyView();
		} else {
			int reportId = cmd.getId();
			if (forBPM) {
				if (!this.bpmManager.isViewableReport(reportId)) {
					return this.createEmptyView();
				}
			} else if (!this.reportingManager.isViewableReport(reportId)) {
				return this.createEmptyView();
			}

			if (!cmd.checkAndUpdateToken()) {
				return this.createEmptyView();
			} else {
				ModelAndView mav = new ModelAndView("reportingDatagridExcelView");
				String exportName = request.getParameter("exportName");

				org.jdom.Document doc;
				try {
					doc = this.reportingManager.getExpandedXml(reportId, this.getParams(request, false),
							HttpRequestUtils.getVisibleTabIdWorkflowObjectIdMap(request), cmd, forBPM, bpmFormID);
				} catch (ExpandXmlException ex) {
					mav = new ModelAndView("reporting/index");
					if (this.causedByReportOverLimit(ex)) {
						mav.addObject("datagridContent", Messages.Key.REPORTING_ERROR_OVER_LIMIT.toString());
						mav.addObject("datagridName", exportName);
					} else {
						Rendering rendering = ex.getErrorPage();
						mav.addObject("styles", rendering.getStyles());
						mav.addObject("content", rendering.getContent());
					}

					return mav;
				}

				if (!this.dataGridExists(exportName, doc)) {
					return this.createEmptyView();
				} else {
					if (!isXLSXExport) {
						try {
							if (ReportingDatagridExcelView.countRows(doc, exportName) > 65536) {
								mav = new ModelAndView("reporting/index");
								mav.addObject("datagridContent",
										Messages.Key.REPORTING_ERROR_DATAGRID_TOO_BIG.toString());
								mav.addObject("datagridName", exportName);
								return mav;
							}
						} catch (Exception var13) {
						}
					}

					mav.addObject("reportId", reportId);
					mav.addObject("document", doc);
					mav.addObject("datagridName", exportName);
					String fileName = MessageFormat.format(isXLSXExport ? "Datagrid-{0}.xlsx" : "Datagrid-{0}.xls",
							Format.asTimestamp(new Date()));
					mav.addObject("isXLSXExport", isXLSXExport);
					mav.addObject("fileName", fileName);
					return mav;
				}
			}
		}
	}

	boolean causedByReportOverLimit(Throwable ex) {
		return ex.getCause() != null && ex.getCause().getCause() == null
				? ex.getCause() instanceof ReportOverLimitException
				: this.causedByReportOverLimit(ex.getCause());
	}

	boolean dataGridExists(String exportName, org.jdom.Document doc) {
		try {
			Namespace ns = Namespace.getNamespace("v", "http://varicent.com/WebReport/RenderXml");
			Element[] datagrids = XPathWrapper.getElements("v:WebReport/v:XmlBody/v:DataGridDisplay", doc, ns);

			for (Element datagridDisplay : datagrids) {
				Element name = XPathWrapper.getElement("v:DataGrid/v:DataGridName", datagridDisplay, ns);
				if (name.getValue().equals(exportName)) {
					return true;
				}
			}

			return false;
		} catch (JDOMException var10) {
			return false;
		}
	}

	@RequestMapping({ "/reporting/exportFixedgridToXLS.do" })
	public ModelAndView exportFixedgridToXLS_reporting(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		return this.exportFixedgridToExcel(cmd, request, response, false, false, 0);
	}

	@RequestMapping({ "/reporting/exportFixedgridToXLSX.do" })
	public ModelAndView exportFixedgridToXLSX_reporting(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		return this.exportFixedgridToExcel(cmd, request, response, true, false, 0);
	}

	public ModelAndView exportFixedgridToExcel(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response, boolean isXLSXExport, boolean forBPM,
			int bpmFormID) {
		if (!cmd.hasId()) {
			return this.createEmptyView();
		} else {
			int reportId = cmd.getId();
			if (forBPM) {
				if (!this.bpmManager.isViewableReport(reportId)) {
					return this.createEmptyView();
				}
			} else if (!this.reportingManager.isViewableReport(reportId)) {
				return this.createEmptyView();
			}

			if (!cmd.checkAndUpdateToken()) {
				return this.createEmptyView();
			} else {
				ModelAndView mav = new ModelAndView("reportingFixedgridExcelView");

				org.jdom.Document doc;
				try {
					doc = this.reportingManager.getExpandedXml(reportId, this.getParams(request, true),
							HttpRequestUtils.getVisibleTabIdWorkflowObjectIdMap(request), cmd, forBPM, bpmFormID);
				} catch (ExpandXmlException ex) {
					mav = new ModelAndView("reporting/index");
					Rendering rendering = ex.getErrorPage();
					mav.addObject("styles", rendering.getStyles());
					mav.addObject("content", rendering.getContent());
					return mav;
				}

				String exportName = request.getParameter("exportName");
				mav.addObject("reportId", reportId);
				mav.addObject("document", doc);
				mav.addObject("fixedgridName", exportName);
				String fileName = MessageFormat.format(isXLSXExport ? "Fixedgrid-{0}.xlsx" : "Fixedgrid-{0}.xls",
						Format.asTimestamp(new Date()));
				mav.addObject("isXLSXExport", isXLSXExport);
				mav.addObject("fileName", fileName);
				return mav;
			}
		}
	}

	private int getReportId(HttpServletRequest request) {
		String reportId = null;
		Tab activeTab = (Tab) WebUtils.getSessionAttribute(request, "activeTab");
		if (activeTab != null && activeTab.isPresenterReport()) {
			TabObject tabObject = (TabObject) activeTab;
			reportId = tabObject.getObjectId();
		} else {
			reportId = request.getParameter("ReportID");
		}

		if (reportId != null) {
			return Integer.parseInt(reportId);
		} else {
			Assert.state(false, "Report ID not found in request.");
			return Integer.MIN_VALUE;
		}
	}

	private boolean getConcertMode(HttpServletRequest request) {
		Boolean queryResult = false;
		Boolean cookieResult = false;
		String concertMode = request.getParameter("concertMode");
		if (concertMode != null) {
			queryResult = Boolean.parseBoolean(concertMode);
		}

		for (Cookie c : request.getCookies()) {
			if (c.getName().equals("concertMode")) {
				cookieResult = Boolean.parseBoolean(c.getValue());
			}
		}

		return cookieResult || queryResult;
	}

	@ModelAttribute("reportList")
	public List<WebReportName> getReportList() {
		return this.reportingManager.getViewableReports();
	}

	private boolean hasReportId(HttpServletRequest request) {
		Tab activeTab = (Tab) WebUtils.getSessionAttribute(request, "activeTab");
		if (activeTab != null && activeTab.isPresenterReport()) {
			return true;
		} else {
			String reportId = request.getParameter("ReportID");
			return reportId != null;
		}
	}

	private boolean isSigStatusSubmitted(HttpServletRequest request, int id) {
		HashSet<Integer> submitted = (HashSet) WebUtils.getSessionAttribute(request, "SigStatusSubmitted");
		return submitted != null && submitted.contains(id);
	}

	private void clearErrorsForNewRequest(HttpServletRequest request) {
		WebUtils.setSessionAttribute(request, "command", (Object) null);
		WebUtils.setSessionAttribute(request, "error", (Object) null);
	}

	@RequestMapping(value = { "/reporting/index.html", "/reporting/reporting/*.html" }, method = { RequestMethod.GET })
	public ModelAndView prepareView(HttpServletRequest request, HttpServletResponse response) {
		this.clearErrorsForNewRequest(request);
		request.getSession().setAttribute("token", UserAccount.getCurrent().getToken());
		ModelAndView mav = new ModelAndView("reporting/index");
		Map<String, Object> params = this.getParams(request, false);
		if (params.size() > 0) {
			Assert.state(params.containsKey("ReportID"));
			int reportID = Integer.parseInt(params.get("ReportID").toString());
			if (!this.reportingManager.isViewableReport(reportID)) {
				return this.createViewWithMessage(Messages.Key.REPORTING_ERROR_LACK_ACCESS.toString(), (String) null);
			}

			params.remove("ReportID");
			ReportingCommand cmd = new ReportingCommand();
			cmd.setId(reportID);
			cmd.setReportChanged(false);
			this.populateModel(mav, cmd, params, request);
		} else if (this.hasReportId(request)) {
			int reportId = this.getReportId(request);
			if (!this.reportingManager.isViewableReport(reportId)) {
				return this.createEmptyView();
			}

			ReportingCommand cmd = new ReportingCommand();
			cmd.setId(reportId);
			cmd.updateToken();
			this.populateModel(mav, cmd, params, request);
		} else {
			ReportingCommand cmd = new ReportingCommand();
			cmd.updateToken();
			mav.addObject("PresenterCommand", cmd);
		}

		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Pragma", "no-cache");
		return mav;
	}

	@RequestMapping(value = { "/reporting/index.html" }, method = { RequestMethod.POST })
	public RedirectView processForm(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request) {
		RedirectView redirect = new RedirectView("report.html", true);
		WebUtils.setSessionAttribute(request, "PresenterCommand", cmd);
		WebUtils.setSessionAttribute(request, "ReportId", cmd.getId());
		WebUtils.setSessionAttribute(request, "ParametersMap", this.getParams(request, true));

		for (String key : this.getParams(request, true).keySet()) {
			if (key.startsWith("SIG_STATUS_")) {
				HashSet<Integer> submitted = (HashSet) WebUtils.getSessionAttribute(request, "SigStatusSubmitted");
				if (submitted == null) {
					submitted = new HashSet();
				}

				submitted.add(cmd.getId());
				WebUtils.setSessionAttribute(request, "SigStatusSubmitted", submitted);
			}
		}

		redirect.setExposeModelAttributes(true);
		return redirect;
	}

	@RequestMapping(value = { "/reporting/report.html" }, method = { RequestMethod.GET })
	public ModelAndView prepareAfterPostView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("reporting/index");
		ReportingCommand cmd = (ReportingCommand) WebUtils.getSessionAttribute(request, "PresenterCommand");
		int id = WebUtils.getSessionAttribute(request, "ReportId") == null ? -1
				: ((Number) WebUtils.getSessionAttribute(request, "ReportId")).intValue();
		if (id == -1) {
			WebUtils.setSessionAttribute(request, "PresenterCommand", (Object) null);
			return this.prepareView(request, response);
		} else {
			if (cmd == null) {
				cmd = new ReportingCommand();
				cmd.setId(id);
				cmd.updateToken();
			}

			if (!cmd.checkAndUpdateToken()) {
				return this.createEmptyView();
			} else if (cmd.hasId() && this.reportingManager.isViewableReport(cmd.getId())) {
				if (cmd.hasReportChanged()) {
					cmd.setReportChanged(false);
				}

				cmd.setResetSigStatus(!this.isSigStatusSubmitted(request, id));
				Map<String, Object> paramMap = (Map) WebUtils.getSessionAttribute(request, "ParametersMap");
				Map<String, Object> emptyMap = new HashMap();
				if (paramMap == null) {
					paramMap = emptyMap;
				}

				if (!cmd.getPageLocale().equals(LocaleContextHolder.getLocale().toString())) {
					mav.addObject("error", Messages.Key.LOCALE_PAGE_MISMATCH.toString());
					paramMap = emptyMap;
				}

				this.populateModel(mav, cmd, paramMap, request);
				WebUtils.setSessionAttribute(request, "mav", mav);
				WebUtils.setSessionAttribute(request, "ParametersMap", emptyMap);
				WebUtils.setSessionAttribute(request, "PresenterCommand", (Object) null);
				return mav;
			} else {
				mav.addObject("PresenterCommand", cmd);
				mav.addObject("error", Messages.Key.WEBREPORT_ERROR_REMOVED.toString());
				return mav;
			}
		}
	}

	private void cleanParams(Map<String, Object> params) {
		ArrayList<String> signatureIDs = (ArrayList) params.get("SIGNATURE_IDS");
		if (signatureIDs != null) {
			for (String signatureId : signatureIDs) {
				params.remove("SIG_STATUS_" + signatureId);
				params.remove("SIG_ID_" + signatureId);
			}

			params.remove("SIGNATURE_IDS");
		}

	}

	private Map<String, Object> getParamsAt(HttpServletRequest request, boolean post, String payeeID,
			String parameter) {
		Map<String, String> rawParams = HttpRequestUtils.getParams(request);
		String paramName = "L_" + parameter;
		if (rawParams.containsKey(paramName)) {
			rawParams.put(paramName, payeeID);
		}

		Map<String, Object> params = this.getParams(rawParams, post);
		if (!rawParams.containsKey(paramName) && parameter != "") {
			params.put(parameter, payeeID);
		}

		return params;
	}

	private Map<String, Object> getParams(HttpServletRequest request, boolean post) {
		Map<String, String> rawParams = HttpRequestUtils.getParams(request);
		return this.getParams(rawParams, post);
	}

	private Map<String, Object> getParams(Map<String, String> rawParams, boolean post) {
		Map<String, Object> params = new HashMap();

		for (Map.Entry<String, String> pair : rawParams.entrySet()) {
			String prefixedName = pair.getKey();
			Object value = pair.getValue();
			Assert.state("L_".length() == "P_".length());
			if (!prefixedName.equalsIgnoreCase("x") && !prefixedName.equalsIgnoreCase("y")) {
				String name = prefixedName.substring("L_".length());
				if (post) {
					if (prefixedName.startsWith("L_") && !params.containsKey(name)) {
						params.put(name, value);
					} else if (prefixedName.startsWith("P_")) {
						if (params.containsKey(name)) {
							params.remove(name);
						}

						params.put(name, value);
					} else if (!prefixedName.startsWith("IR_") && !prefixedName.startsWith("Edited_IR_")) {
						if (prefixedName.startsWith("Edited_DE_")) {
							params.put(prefixedName, value);
						} else if (prefixedName.startsWith("SIG_ID_")) {
							ArrayList<String> signatureIds = (ArrayList) params.get("SIGNATURE_IDS");
							if (signatureIds == null) {
								signatureIds = new ArrayList();
							}

							signatureIds.add(prefixedName.substring("SIG_ID_".length()));
							params.put("SIGNATURE_IDS", signatureIds);
						} else if (prefixedName.startsWith("SIG_STATUS_")) {
							params.put(prefixedName, value);
						}
					} else {
						params.put(prefixedName, value);
					}
				} else if (prefixedName.startsWith("L_")) {
					params.put(name, value);
				}
			}
		}

		return params;
	}

	private void populateModel(ModelAndView mav, ReportingCommand cmd, Map<String, Object> params,
			HttpServletRequest request) {
		Rendering rendering = null;
		cmd.addReferencesTable(this.reportingManager.getPayeeIDReferencedPort());
		cmd.updateToken();
		cmd.setConcertMode(this.getConcertMode(request));
		cmd.setResetSigStatus(!this.isSigStatusSubmitted(request, cmd.getId()));
		HashMap<String, Boolean> isViewableMap = new HashMap();
		HashMap<String, Boolean> isEditableMap = new HashMap();
		if (cmd.hasReportChanged()) {
			rendering = this.reportingManager.getRendering(cmd.getId(), new HashMap(),
					HttpRequestUtils.getVisibleTabIdWorkflowObjectIdMap(request), isViewableMap, isEditableMap, 0, cmd,
					-1, false);
		} else {
			rendering = this.reportingManager.getRendering(cmd.getId(), params,
					HttpRequestUtils.getVisibleTabIdWorkflowObjectIdMap(request), isViewableMap, isEditableMap, 0, cmd,
					-1, false);
		}

		if (rendering.getMessage() != null) {
			mav.addObject("error", rendering.getMessage().getKey().toString());
			if (rendering.getMessage().getParams() != null) {
				mav.addObject("errorParams", rendering.getMessage().getParams());
			}
		}

		cmd.setCanExport(rendering.canExport());
		cmd.setCanBatchExport(
				cmd.getCanExport() && cmd.getPayeePicklistCount() == 1 && cmd.getPicklistPayeeIDs().size() > 0);
		WebUtils.setSessionAttribute(request, "showBatchExportToPdf", cmd.getCanBatchExport());
		WebUtils.setSessionAttribute(request, "showExportToPdf", cmd.getId() > 0 && cmd.getCanExport());
		mav.addObject("PresenterCommand", cmd);
		mav.addObject("styles", rendering.getStyles());
		mav.addObject("content", rendering.getContent());
		mav.addObject("javascript", rendering.getJavascript());
		InquiryCommand inquiryCmd = new InquiryCommand();
		inquiryCmd.updateToken();
		mav.addObject("inquiryCommand", inquiryCmd);
		boolean inquiriesEnabled = this.workflowManager.inquiriesEnabled(String.valueOf(cmd.getId()),
				WorkflowObject.Type.WEB_REPORT);
		mav.addObject("inquiriesEnabled", inquiriesEnabled);
		WebUtils.setSessionAttribute(request, "showInquiry", cmd.getId() > 0 && inquiriesEnabled);
		mav.addObject("defaultCategories", this.workflowManager.getDefaultCategories().values());
		mav.addObject("inquiryCategories", this.workflowManager.getUserDefinedCategories().values());
		this.cleanParams(params);
		mav.addObject("parameters", params);
		cmd.setPageLocale(LocaleContextHolder.getLocale().toString());
		mav.addObject("pageLocale", LocaleContextHolder.getLocale().toString());
	}

	private ReportData populateData(int reportId, String sourceName, String userID) {
		ReportData reportData = this.reportingManager.getReportDataItemBySourceName(reportId, sourceName, userID);
		return reportData;
	}
}
