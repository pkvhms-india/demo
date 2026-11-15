package com.varicent.BPM.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.support.StringMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

import com.itextpdf.text.DocumentException;
import com.varicent.BPM.domain.BPMTokenView;
import com.varicent.BPM.domain.BPMWorkflowView;
import com.varicent.BPM.service.BPMManager;
import com.varicent.core.exceptions.DatabaseConcurrencyException;
import com.varicent.core.exceptions.IllegalValueException;
import com.varicent.core.exceptions.ReportOverLimitException;
import com.varicent.core.i18n.Messages;
import com.varicent.core.util.HttpRequestUtils;
import com.varicent.reporting.domain.Rendering;
import com.varicent.reporting.service.ReportingManager;
import com.varicent.reporting.web.ReportingCommand;
import com.varicent.reporting.web.ReportingController;

@Controller("bpmController")
public class BPMController {
	public static final String VIEW = "bpm/index";
	public static final String VIEW_REDIRECT = "redirect:index.html";
	public static final String INITIALIZE = "bpm/initialize";
	public static final String TOKENLIST = "bpm/tokenlist";
	public static final String VIEW_COMPLETED_TOKEN_HISTORY = "bpm/completedtokenhistorylistpage";
	private static final String PDF_VIEW = "reportingPdfView";
	private BPMManager bpmManager;
	private ReportingManager reportingManager;
	private ReportingController reportingController;
	private final Messages messages;

	@Autowired
	public BPMController(BPMManager bpmManager, ReportingManager reportingManager, Messages messages,
			ReportingController reportingController) {
		this.bpmManager = bpmManager;
		this.reportingManager = reportingManager;
		this.messages = messages;
		this.reportingController = reportingController;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringMultipartFileEditor());
	}

	@RequestMapping(value = { "/bpm/index.html" }, method = { RequestMethod.GET })
	public ModelAndView prepareView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("bpm/index");
		String startParameter = request.getParameter("start");
		int startIndex = 0;
		if (startParameter != null) {
			try {
				startIndex = Integer.parseInt(startParameter);
			} catch (NumberFormatException var10) {
				startIndex = 0;
			}
		}

		boolean sortDescending = Boolean.parseBoolean(request.getParameter("sortDesc"));
		WebUtils.setSessionAttribute(request, "bpmWorkflowlistStart", startIndex);
		BPMWorkflowsCommand command = null;
		int workflowsCount = this.bpmManager.getInitiatableWorkflowsCount();
		int BPMCompletedTokenCount = this.bpmManager.BPMCompletedTokenCount(true, true);
		mav.addObject("historyCount", BPMCompletedTokenCount);
		command = new BPMWorkflowsCommand(startIndex, 10, workflowsCount,
				this.bpmManager.getInitiatableWorkflows(startIndex, 10, sortDescending));
		mav.addObject("workflowsCount", workflowsCount);
		mav.addObject("tokensCount", this.bpmManager.getBPMTokenViewsCount());
		mav.addObject("activeBPMTab", "index");
		this.populateModel(mav);
		mav.addObject("hideSubhead", 1);
		mav.addObject("command", command);
		mav.addObject("start", startIndex);
		mav.addObject("sortDescending", sortDescending);
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Pragma", "no-cache");
		return mav;
	}

	@RequestMapping(value = { "/bpm/completedtokenhistorylistpage.html" }, method = { RequestMethod.GET })
	public ModelAndView bpmHistoryView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("bpm/completedtokenhistorylistpage");
		String startParameter = request.getParameter("start");
		int startIndex = 1;
		if (startParameter != null) {
			try {
				startIndex = Integer.parseInt(startParameter);
			} catch (NumberFormatException var10) {
				startIndex = 0;
			}
		}

		BPMTokenView[] completedTokens = this.bpmManager.getCompletedTokenViews(startIndex, 10);
		mav.addObject("activeBPMTab", "history");
		int BPMCompletedTokenCount = this.bpmManager.BPMCompletedTokenCount(true, true);
		mav.addObject("historyCount", BPMCompletedTokenCount);
		BPMTokenHistoryPageCommand command = new BPMTokenHistoryPageCommand(startIndex, 10, BPMCompletedTokenCount,
				completedTokens);
		mav.addObject("command", command);
		int workflowsCount = this.bpmManager.getInitiatableWorkflowsCount();
		mav.addObject("completedTokens", completedTokens);
		mav.addObject("workflowsCount", workflowsCount);
		mav.addObject("tokensCount", this.bpmManager.getBPMTokenViewsCount());
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Pragma", "no-cache");
		return mav;
	}

	@RequestMapping(value = { "/bpm/initialize.html" }, method = { RequestMethod.GET })
	public ModelAndView prepareInitializeView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("bpm/initialize");
		this.populateInitializeModelAndView(mav, request, response);
		return mav;
	}

	private boolean populateInitializeModelAndView(ModelAndView mav, HttpServletRequest request,
			HttpServletResponse response) {
		boolean noError = true;
		Map<String, Object> paramMap = (Map) WebUtils.getSessionAttribute(request, "ParametersMap");
		String error = (String) WebUtils.getSessionAttribute(request, "error");
		Object workflowIdObj = request.getParameter("workflowID");
		if (workflowIdObj != null) {
			workflowIdObj = Integer.parseInt((String) workflowIdObj);
		} else if (workflowIdObj == null && WebUtils.getSessionAttribute(request, "WorkflowId") != null) {
			workflowIdObj = WebUtils.getSessionAttribute(request, "WorkflowId");
		}

		if (error != null && !error.equals("error.login")) {
			mav.addObject("error", error);
		}

		if (workflowIdObj != null) {
			int workflowID = (Integer) workflowIdObj;
			if (this.canInitiateWorkflow(workflowID)) {
				int reportID = this.bpmManager.getWorkflowReportID(workflowID);
				int formID = 0;

				try {
					formID = this.bpmManager.getUninitiatedFormID(workflowID, true);
				} catch (DatabaseConcurrencyException var13) {
					mav.clear();
					mav.setViewName("redirect:index.html");
					return false;
				}

				boolean isSaveAndSubmit = this.bpmManager.isStartNodeSaveAndSubmit(workflowID);
				mav.addObject("saveAndSubmit", isSaveAndSubmit);
				ReportingCommand reportingCommand = new ReportingCommand();
				reportingCommand.setId(reportID);
				noError = this.populateInitializeModel(request, paramMap, mav, reportingCommand, workflowID, formID,
						false);
			}
		}

		mav.addObject("hideSubhead", 1);
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Pragma", "no-cache");
		WebUtils.setSessionAttribute(request, "ParametersMap", new HashMap());
		WebUtils.setSessionAttribute(request, "error", (Object) null);
		return noError;
	}

	@RequestMapping(value = { "/bpm/tokenlist.html" }, method = { RequestMethod.GET })
	public ModelAndView prepareTokenListView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("bpm/tokenlist");
		String startParameter = request.getParameter("start");
		int startIndex = 0;
		if (startParameter != null) {
			try {
				startIndex = Integer.parseInt(startParameter);
			} catch (NumberFormatException var8) {
				startIndex = 0;
			}
		}

		WebUtils.setSessionAttribute(request, "bpmTokenlistStart", startIndex);
		mav.addObject("hideSubhead", 1);
		int tokenViewsCount = this.bpmManager.getBPMTokenViewsCount();
		mav.addObject("command", new BPMTokensCommand(startIndex, 10, tokenViewsCount,
				this.bpmManager.getBPMTokenViews(startIndex, 10)));
		int BPMCompletedTokenCount = this.bpmManager.BPMCompletedTokenCount(true, true);
		mav.addObject("historyCount", BPMCompletedTokenCount);
		mav.addObject("workflowsCount", this.bpmManager.getInitiatableWorkflowsCount());
		mav.addObject("tokensCount", tokenViewsCount);
		mav.addObject("activeBPMTab", "tokenlist");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Pragma", "no-cache");
		return mav;
	}

	boolean canInitiateWorkflow(int workflowID) {
		return this.bpmManager.canInitiateWorkflow(workflowID);
	}

	@RequestMapping(value = { "/bpm/initialize.html" }, method = { RequestMethod.POST })
	public RedirectView processForm(@ModelAttribute("command") ReportingCommand cmd, HttpServletRequest request) {
		RedirectView redirect = new RedirectView("initialize.html", true);
		WebUtils.setSessionAttribute(request, "ParametersMap", this.getParams(request, true));
		redirect.setExposeModelAttributes(true);
		if (cmd.hasReportChanged()) {
			cmd.setReportChanged(false);
		}

		if (!cmd.checkAndUpdateToken()) {
			WebUtils.setSessionAttribute(request, "error", Messages.Key.BPM_ERROR_VIEW.toString());
		} else {
			int workflowID = Integer.parseInt(request.getParameter("workflowID"));
			WebUtils.setSessionAttribute(request, "WorkflowId", workflowID);
		}

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

		return redirect;
	}

	private Map<String, Object> getParams(HttpServletRequest request, boolean post) {
		Map<String, Object> params = new HashMap();
		Enumeration<?> e = request.getParameterNames();

		while (e.hasMoreElements()) {
			String prefixedName = (String) e.nextElement();
			String value = request.getParameter(prefixedName).trim();
			this.addParams(prefixedName, post, params, value);
		}

		return params;
	}

	private void addParams(String prefixedName, boolean post, Map<String, Object> params, String value) {
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

	private boolean populateInitializeModel(HttpServletRequest request, Map<String, Object> params, ModelAndView mav,
			ReportingCommand reportingCommand, int workflowID, int formID, boolean isPost) {
		if (params == null || params.size() == 0) {
			if (!isPost) {
				params = this.bpmManager.getWorkflowParamsForInitialize(workflowID);
			} else {
				params = this.getParams(request, true);
			}
		}

		HashSet<Integer> submitted = (HashSet) WebUtils.getSessionAttribute(request, "SigStatusSubmitted");
		reportingCommand.setResetSigStatus(submitted == null || !submitted.contains(reportingCommand.getId()));
		Map<Integer, Integer> visibleTabIdWorkflowObjectIdMap = HttpRequestUtils
				.getVisibleTabIdWorkflowObjectIdMap(request);
		boolean noError = this.populateReportModel(mav, reportingCommand, params, visibleTabIdWorkflowObjectIdMap,
				workflowID, formID, -1);
		WebUtils.setSessionAttribute(request, "showBatchExportToPdf", reportingCommand.getCanBatchExport());
		WebUtils.setSessionAttribute(request, "showExportToPdf",
				reportingCommand.getId() > 0 && reportingCommand.getCanExport());
		BPMWorkflowView workflowView = this.bpmManager.getWorkflowView(workflowID);
		mav.addObject("workflow", workflowView);
		return noError;
	}

	private boolean populateReportModel(ModelAndView mav, ReportingCommand command, Map<String, Object> params,
			Map<Integer, Integer> visibleTabIdWorkflowObjectIdMap, int workflowID, int formID, int tokenId) {
		boolean noError = true;
		command.addReferencesTable(this.reportingManager.getPayeeIDReferencedPort());
		command.updateToken();
		HashMap<String, Boolean> isViewableMap = this.bpmManager.getWorkflowLayoutVisibilityMap(workflowID);
		HashMap<String, Boolean> isEditableMap = this.bpmManager.getWorkflowLayoutEditabilityMap(workflowID);
		Rendering reportRendering = this.reportingManager.getRendering(command.getId(), params,
				visibleTabIdWorkflowObjectIdMap, isViewableMap, isEditableMap, formID, command, tokenId, false);
		command.setCanExport(reportRendering.canExport());
		command.setCanBatchExport(command.getCanExport() && command.getPayeePicklistCount() == 1
				&& command.getPicklistPayeeIDs().size() > 0);
		mav.addObject("command", command);
		if (reportRendering.getMessage() != null) {
			mav.addObject("error", reportRendering.getMessage().getKey().toString());
			if (reportRendering.getMessage().getParams() != null) {
				mav.addObject("errorParams", reportRendering.getMessage().getParams());
				noError = false;
			}
		}

		mav.addObject("reporting_styles", reportRendering.getStyles());
		mav.addObject("reporting_content", reportRendering.getContent());
		mav.addObject("javascript", reportRendering.getJavascript());
		this.cleanParams(params);
		mav.addObject("parameters", params);
		return noError;
	}

	HashMap<String, Object> extractReportParameters(HttpServletRequest request) {
		HashMap<String, Object> params = new HashMap();

		for (String key : this.getParams(request, true).keySet()) {
			if (!key.startsWith("DE_") && !key.startsWith("IR_") && !key.startsWith("Edited_")) {
				params.put(key, this.getParams(request, true).get(key));
			}
		}

		return params;
	}

	@RequestMapping({ "/bpm/createtoken.do" })
	public ModelAndView actionPath(@ModelAttribute("command") ReportingCommand cmd, HttpServletRequest request,
			HttpServletResponse response) {
		if (!cmd.checkAndUpdateToken()) {
			return this.prepareView(request, response);
		} else {
			int workflowID = Integer.parseInt(request.getParameter("workflowID"));
			if (this.canInitiateWorkflow(workflowID)) {
				try {
					HashMap<String, Object> reportParams = this.extractReportParameters(request);
					int newTokenID = this.bpmManager.initiateWorkflow(workflowID, reportParams,
							HttpRequestUtils.getVisibleTabIdWorkflowObjectIdMap(request));
					this.bpmManager.updateBPMPreviousParameters(this.extractReportParameters(request), newTokenID);
				} catch (DatabaseConcurrencyException ex) {
					ModelAndView mav = this.prepareView(request, response);
					mav.addObject("error", ex.getMessageObject().getKey().toString());
					return mav;
				} catch (IllegalValueException ex) {
					ModelAndView mav = this.prepareView(request, response);
					mav.addObject("error", ex.getMessage());
					return mav;
				} catch (ReportOverLimitException ex) {
					ModelAndView mav = this.prepareView(request, response);
					mav.addObject("error", ex.getMessage());
					return mav;
				}

				ModelAndView mav = new ModelAndView("redirect:index.html");
				return mav;
			} else {
				ModelAndView mav = new ModelAndView("bpm/initialize");
				response.addHeader("Cache-Control", "no-cache");
				response.addHeader("Pragma", "no-cache");
				mav.addObject("hideSubhead", 1);
				return mav;
			}
		}
	}

	private void populateModel(ModelAndView mav) {
		BPMWorkflowView[] workflows = this.bpmManager.getInitiatableWorkflows();
		mav.addObject("workflows", workflows);
	}

	@RequestMapping({ "/bpm/batch_export.do" })
	public ModelAndView batch_export_bpm(@ModelAttribute("command") ReportingCommand cmd, HttpServletRequest request,
			HttpServletResponse response) throws DocumentException, IOException {
		PDFErrorViewType pdfErrorView = this.getPDFErrorView(request.getHeader("referer"));
		int formID = 0;

		HashMap<String, Boolean> isViewableMap;
		try {
			String workflowIDParam = request.getParameter("workflowID");
			String tokenIDParam = request.getParameter("bpmTokenID");
			int workflowID = Integer.parseInt(workflowIDParam);
			if (tokenIDParam != null) {
				formID = this.bpmManager.getFormID(Integer.parseInt(tokenIDParam));
			} else {
				formID = this.bpmManager.getUninitiatedFormID(workflowID, false);
			}

			isViewableMap = this.getIsViewableMap(workflowIDParam, tokenIDParam);
		} catch (Exception var10) {
			return this.createEmptyView(pdfErrorView, request, response);
		}

		return this.reportingController.batch_export(cmd, request, response, true, isViewableMap, formID);
	}

	@RequestMapping({ "/bpm/export.do" })
	public ModelAndView export_pdf(@ModelAttribute("command") ReportingCommand cmd, HttpServletRequest request,
			HttpServletResponse response) {
		if (!cmd.checkAndUpdateToken()) {
			return this.createEmptyView(BPMController.PDFErrorViewType.VIEW, request, response);
		} else {
			PDFErrorViewType pdfErrorView = this.getPDFErrorView(request.getHeader("referer"));
			int formID = 0;
			int tokenID = -1;

			HashMap<String, Boolean> isViewableMap;
			try {
				String workflowIDParam = request.getParameter("workflowID");
				String tokenIDParam = request.getParameter("bpmTokenID");
				int workflowID = Integer.parseInt(workflowIDParam);
				if (tokenIDParam != null) {
					tokenID = Integer.parseInt(tokenIDParam);
					formID = this.bpmManager.getFormID(tokenID);
				} else {
					formID = this.bpmManager.getUninitiatedFormID(workflowID, false);
				}

				isViewableMap = this.getIsViewableMap(request.getParameter("workflowID"),
						request.getParameter("bpmTokenID"));
			} catch (Exception var11) {
				return this.createEmptyView(pdfErrorView, request, response);
			}

			return this.reportingController.export(cmd, request, response, true, isViewableMap, formID, tokenID);
		}
	}

	private PDFErrorViewType getPDFErrorView(String referrer) {
		PDFErrorViewType pdfErrorView = BPMController.PDFErrorViewType.INDEX;
		String referrer_lc = referrer.toLowerCase();
		if (referrer_lc != null) {
			if (referrer_lc.contains("view.html")) {
				pdfErrorView = BPMController.PDFErrorViewType.VIEW;
			} else if (referrer_lc.contains("initialize.html")) {
				pdfErrorView = BPMController.PDFErrorViewType.INITIALIZE;
			}
		}

		return pdfErrorView;
	}

	private HashMap<String, Boolean> getIsViewableMap(String workflowIDParam, String bpmTokenIDParam) {
		int workflowID = Integer.parseInt(workflowIDParam);
		HashMap<String, Boolean> isViewableMap;
		if (bpmTokenIDParam == null) {
			isViewableMap = this.bpmManager.getWorkflowLayoutVisibilityMap(workflowID);
		} else {
			int bpmTokenID = Integer.parseInt(bpmTokenIDParam);
			isViewableMap = this.bpmManager.getTokenLayoutVisibilityMap(bpmTokenID);
		}

		return isViewableMap;
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

	private BPMTokenView[] mergeHistory(BPMTokenView[] active, BPMTokenView[] deleted) {
		int lActive = active.length;
		int lDeleted = deleted.length;
		BPMTokenView[] ret = new BPMTokenView[lActive + lDeleted];

		for (int i = 0; i < lActive; ++i) {
			ret[i] = active[i];
		}

		int i = lActive;

		for (int y = 0; i < lActive + lDeleted; ++y) {
			ret[i] = deleted[y];
			++i;
		}

		return ret;
	}

	private ModelAndView createEmptyView(PDFErrorViewType pdfErrorView, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mav = null;
		switch (pdfErrorView) {
		case INDEX:
			mav = this.prepareView(request, response);
			break;
		case INITIALIZE:
			mav = this.prepareInitializeView(request, response);
			break;
		case VIEW:
			mav = (new BPMViewController(this.bpmManager, this.reportingManager, this.messages)).prepareView(request,
					response);
		}

		mav.addObject("hideSubhead", 1);
		mav.addObject("error", Messages.Key.BPM_ERROR_VIEW.toString());
		return mav;
	}

	@RequestMapping({ "/bpm/exportDatagridToXLS.do" })
	public ModelAndView exportDatagridToXLS_bpm(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		String workflowIDParam = request.getParameter("workflowID");
		String bpmTokenIDParam = request.getParameter("bpmTokenID");
		int workflowID = 0;
		int bpmFormID = 0;

		try {
			workflowID = Integer.parseInt(workflowIDParam);
			if (bpmTokenIDParam != null) {
				int tokenID = Integer.parseInt(bpmTokenIDParam);
				bpmFormID = this.bpmManager.getFormID(tokenID);
			} else {
				bpmFormID = this.bpmManager.getUninitiatedFormID(workflowID, false);
			}
		} catch (Exception var9) {
			return this.createEmptyReportView();
		}

		return this.reportingController.exportDatagridToExcel(cmd, request, response, false, true, bpmFormID);
	}

	@RequestMapping({ "/bpm/exportDatagridToXLSX.do" })
	public ModelAndView exportDatagridToXLSX_bpm(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		String workflowIDParam = request.getParameter("workflowID");
		String bpmTokenIDParam = request.getParameter("bpmTokenID");
		int workflowID = 0;
		int bpmFormID = 0;

		try {
			workflowID = Integer.parseInt(workflowIDParam);
			if (bpmTokenIDParam != null) {
				int tokenID = Integer.parseInt(bpmTokenIDParam);
				bpmFormID = this.bpmManager.getFormID(tokenID);
			} else {
				bpmFormID = this.bpmManager.getUninitiatedFormID(workflowID, false);
			}
		} catch (Exception var9) {
			return this.createEmptyReportView();
		}

		return this.reportingController.exportDatagridToExcel(cmd, request, response, true, true, bpmFormID);
	}

	@RequestMapping({ "/bpm/exportFixedgridToXLS.do" })
	public ModelAndView exportFixedgridToXLS_bpm(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		String workflowIDParam = request.getParameter("workflowID");
		String bpmTokenIDParam = request.getParameter("bpmTokenID");
		int workflowID = 0;
		int bpmFormID = 0;

		try {
			workflowID = Integer.parseInt(workflowIDParam);
			if (bpmTokenIDParam != null) {
				int tokenID = Integer.parseInt(bpmTokenIDParam);
				bpmFormID = this.bpmManager.getFormID(tokenID);
			} else {
				bpmFormID = this.bpmManager.getUninitiatedFormID(workflowID, false);
			}
		} catch (Exception var9) {
			return this.createEmptyReportView();
		}

		return this.reportingController.exportFixedgridToExcel(cmd, request, response, false, true, bpmFormID);
	}

	@RequestMapping({ "/bpm/exportFixedgridToXLSX.do" })
	public ModelAndView exportFixedgridToXLSX_bpm(@ModelAttribute("PresenterCommand") ReportingCommand cmd,
			HttpServletRequest request, HttpServletResponse response) {
		String workflowIDParam = request.getParameter("workflowID");
		String bpmTokenIDParam = request.getParameter("bpmTokenID");
		int workflowID = 0;
		int bpmFormID = 0;

		try {
			workflowID = Integer.parseInt(workflowIDParam);
			if (bpmTokenIDParam != null) {
				int tokenID = Integer.parseInt(bpmTokenIDParam);
				bpmFormID = this.bpmManager.getFormID(tokenID);
			} else {
				bpmFormID = this.bpmManager.getUninitiatedFormID(workflowID, false);
			}
		} catch (Exception var9) {
			return this.createEmptyReportView();
		}

		return this.reportingController.exportFixedgridToExcel(cmd, request, response, true, true, bpmFormID);
	}

	@RequestMapping(value = { "/bpm/saveandcreatetoken.do" }, method = { RequestMethod.POST })
	public ModelAndView SaveAndInitialize(@ModelAttribute("command") ReportingCommand cmd, HttpServletRequest request,
			HttpServletResponse response) {
		if (!cmd.checkAndUpdateToken()) {
			return new ModelAndView("bpm/initialize");
		} else {
			ModelAndView mav = new ModelAndView("bpm/initialize");
			WebUtils.setSessionAttribute(request, "ParametersMap", this.getParams(request, true));
			boolean hasError = !this.populateInitializeModelAndView(mav, request, response);
			return hasError ? mav : this.actionPath(cmd, request, response);
		}
	}

	ModelAndView createEmptyReportView() {
		ModelAndView mav = new ModelAndView("bpm/index");
		ReportingCommand cmd = new ReportingCommand();
		cmd.setCanExport(false);
		cmd.setCanBatchExport(false);
		cmd.updateToken();
		mav.addObject("command", cmd);
		mav.addObject("inquiriesEnabled", false);
		return mav;
	}

	private static enum PDFErrorViewType {
		VIEW, INITIALIZE, INDEX;
	}
}
