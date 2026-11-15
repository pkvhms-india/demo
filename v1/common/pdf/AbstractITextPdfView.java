package com.varicent.common.pdf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;

public abstract class AbstractITextPdfView extends AbstractView {

	private static final String GENERATOR = "";

	public AbstractITextPdfView() {
		this.setContentType("application/pdf");
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ByteArrayOutputStream baos = this.createTemporaryOutputStream();
		Document doc = this.newDocument();
		PdfWriter writer = this.newWriter(doc, baos);
		this.prepareWriter(model, writer, request);
		this.buildPdfMetadata(model, doc, request);
		doc.open();
		this.buildPdfDocument(model, doc, writer, request, response);
		this.scrubGeneratorMetadata(writer);
		doc.close();
		this.writeToResponse(response, baos);
	}

	protected Document newDocument() {
		return new Document(PageSize.A4);
	}

	protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
		return PdfWriter.getInstance(document, os);
	}

	protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
			throws DocumentException {
		writer.setViewerPreferences(this.getViewerPreferences());
	}

	protected int getViewerPreferences() {
		return 2053;
	}

	protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
	}

	protected void scrubGeneratorMetadata(PdfWriter writer) {
		PdfDictionary info = writer.getInfo();
		if (info != null) {
			info.put(PdfName.PRODUCER, new PdfString(GENERATOR));
			info.put(PdfName.CREATOR, new PdfString(GENERATOR));
		}
	}

	protected abstract void buildPdfDocument(Map<String, Object> var1, Document var2, PdfWriter var3,
			HttpServletRequest var4, HttpServletResponse var5) throws Exception;
}
