package com.varicent.reporting.web.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.PdfWriter;
import com.varicent.common.pdf.AbstractITextPdfView;
import com.varicent.common.pdf.PdfDocument;
import com.varicent.core.exceptions.ReportTooBigException;
import com.varicent.core.i18n.Messages;
import com.varicent.reporting.domain.Image;

@Component
public class ReportingPdfView extends AbstractITextPdfView {
	private final Messages messages;

	@Autowired
	public ReportingPdfView(Messages messages) {
		this.messages = messages;
	}

	public static void buildDocument(Document xml, List<Image> images, HttpServletRequest request, PdfDocument pdf,
			HashMap<String, String> chartStreamMap) {
		pdf.setExpandToFit(true);
		Map<Integer, Image> imageMap = new HashMap();

		for (Image image : images) {
			imageMap.put(image.getImageId(), image);
		}

		String requestURL = request.getRequestURL().toString();
		String webAddress = requestURL.substring(0, requestURL.lastIndexOf(47) + 1);
		PdfWebReportReader reader = new PdfWebReportReader(xml, imageMap, webAddress, pdf, chartStreamMap);
		reader.read();
		reader.setPageNumber();
		reader.setScaleValue();
		reader.setFooterText();
	}

	@Override
	protected void buildPdfDocument(Map<String, Object> model, com.itextpdf.text.Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			PdfDocument pdf = (PdfDocument) model.get("pdf");
			model.remove("pdf");
			pdf.buildPdf(document, writer, this.messages);
		} catch (OutOfMemoryError ex) {
			throw new ReportTooBigException(ex);
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	protected void buildPdfMetadata(Map<String, Object> model, com.itextpdf.text.Document document,
			HttpServletRequest request) {
		PdfDocument pdf = new PdfDocument();
		model.put("pdf", pdf);
		HashMap<String, String> chartStreamMap = (HashMap) model.get("chartStreamMap");
		buildDocument((Document) model.get("document"), (List) model.get("images"), request, pdf, chartStreamMap);
		pdf.addPdfMetadata(document);
		super.buildPdfMetadata(model, document, request);
	}
}
