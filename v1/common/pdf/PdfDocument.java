package com.varicent.common.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.varicent.core.i18n.Messages;

public class PdfDocument {
	public static String Extension = "pdf";
	public static String Filter = "Portable Document Format files (*.pdf)|*.pdf";
	public static float Margin = 36.0F;
	private BaseFont defaultFooterFont;
	private List<PdfElement> pdfElements = new ArrayList();
	private PageOrientation orientation;
	private boolean numberPages;
	private boolean TotalPageNumber;
	private PdfImage headerImage;
	private float topMargin;
	private float rightMargin;
	private float bottomMargin;
	private float leftMargin;
	private boolean expandToFit;
	private String footerText;
	private float scaleValue;
	protected PdfTemplate footerTextTemplate;
	protected float fontSize;
	protected float offset;
	protected PdfTemplate footerTotalPageNumber;

	public PdfDocument() {
		try {
			this.defaultFooterFont = BaseFont.createFont();
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		this.orientation = PdfDocument.PageOrientation.Portrait;
		this.numberPages = false;
		this.TotalPageNumber = false;
		this.headerImage = null;
		this.topMargin = this.rightMargin = this.bottomMargin = this.leftMargin = Margin;
		this.expandToFit = false;
		this.scaleValue = 1.0F;
		this.footerText = "";
	}

	public void setOrientation(PageOrientation orientation) {
		this.orientation = orientation;
	}

	public boolean isNumberPages() {
		return this.numberPages;
	}

	public void setNumberPages(boolean numberPages) {
		this.numberPages = numberPages;
	}

	public boolean isTotalNumberPages() {
		return this.TotalPageNumber;
	}

	public void setTotalPageNumber(boolean TotalPageNumber) {
		this.TotalPageNumber = TotalPageNumber;
	}

	public PdfImage getHeaderImage() {
		return this.headerImage;
	}

	public void setHeaderImage(PdfImage headerImage) {
		this.headerImage = headerImage;
	}

	public float getWidth() {
		return this.orientation.getPageWidth();
	}

	public float getHeight() {
		return this.orientation.getPageHeight();
	}

	public float getPrintableWidth() {
		return this.orientation.getPageWidth() - this.leftMargin - this.rightMargin;
	}

	public float getPrintableHeight() {
		return this.orientation.getPageHeight() - this.topMargin - this.bottomMargin;
	}

	public float getTopMargin() {
		return this.topMargin;
	}

	public void setTopMargin(float topMargin) {
		this.topMargin = topMargin;
	}

	public float getRightMargin() {
		return this.rightMargin;
	}

	public void setRightMargin(float rightMargin) {
		this.rightMargin = rightMargin;
	}

	public float getBottomMargin() {
		return this.bottomMargin;
	}

	public void setBottomMargin(float bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public float getLeftMargin() {
		return this.leftMargin;
	}

	public void setLeftMargin(float leftMargin) {
		this.leftMargin = leftMargin;
	}

	public float getScaleValue() {
		return this.scaleValue;
	}

	public void setScaleValue(float scaleValue) {
		this.scaleValue = scaleValue;
	}

	public String getFooterText() {
		return this.footerText;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	public boolean isExpandToFit() {
		return this.expandToFit;
	}

	public void setExpandToFit(boolean expandToFit) {
		this.expandToFit = expandToFit;
	}

	public void add(PdfElement pdfElement) {
		this.pdfElements.add(pdfElement);
	}

	public PdfElement[] getElements() {
		PdfElement[] arr = new PdfElement[this.pdfElements.size()];
		return (PdfElement[]) this.pdfElements.toArray(arr);
	}

	public void addPdfMetadata(Document doc) {
		Rectangle page = new Rectangle(this.getWidth(), this.getHeight());
		if (this.expandToFit) {
			float minWidth = this.calculateMinimumWidth() + this.leftMargin + this.rightMargin;
			float newWidth = Math.max(minWidth, page.getWidth());
			page.setTop(page.getTop() / page.getRight() * newWidth);
			page.setRight(newWidth);

			for (PdfElement element : this.pdfElements) {
				if (element instanceof PdfTable) {
					PdfTableFormatter.resizeColumnsToFit((PdfTable) element,
							newWidth - this.leftMargin - this.rightMargin);
				}
			}
		}

		float minHeight = this.calculateMinimumHeight();
		if (page.getTop() < minHeight) {
			page.setTop(minHeight);
		}

		doc.setPageSize(page);
		doc.setMargins(this.leftMargin, this.rightMargin, this.topMargin, this.bottomMargin);
	}

	float calculateMinimumWidth() {
		float minWidth = 0.0F;

		for (PdfElement element : this.pdfElements) {
			minWidth = Math.max(minWidth, element.calculateMinimumWidth());
		}

		return minWidth;
	}

	float calculateMinimumHeight() {
		float max = 0.0F;

		for (PdfElement element : this.pdfElements) {
			max = Math.max(max, element.calculateMinimumHeight());
		}

		float headerHeight = this.topMargin;
		if (this.headerImage != null) {
			headerHeight = Math.max(headerHeight, this.headerImage.calculateMinimumHeight());
		}

		return max + headerHeight + this.bottomMargin;
	}

	public void buildPdf(Document doc, PdfWriter writer, Messages messages) {
		try {
			this.footerTextTemplate = null;
			this.footerTotalPageNumber = null;
			this.fontSize = 9.75F * (this.NeedsScaling() ? this.scaleValue : 1.0F);
			this.offset = this.defaultFooterFont.getWidthPoint(" of 100", this.fontSize);
			if (this.footerText != "") {
				this.footerTextTemplate = writer.getDirectContent()
						.createTemplate(doc.getPageSize().getWidth() * 3.0F / 4.0F, doc.bottomMargin());
				this.footerTextTemplate.setFontAndSize(this.defaultFooterFont, this.fontSize);
				this.footerTextTemplate.beginText();
				this.footerTextTemplate.setTextMatrix(0.0F, doc.bottomMargin() / 2.0F);
				this.footerTextTemplate.showText(this.footerText);
				this.footerTextTemplate.endText();
			}

			if (this.TotalPageNumber) {
				this.footerTotalPageNumber = writer.getDirectContent().createTemplate(this.offset, doc.bottomMargin());
				this.footerTotalPageNumber.setFontAndSize(this.defaultFooterFont, this.fontSize);
			}

			int elementCount = 0;
			writer.setPageEvent(new PdfPageEventHelperImpl(messages));

			for (PdfElement pdfElement : this.pdfElements) {
				Element[] elements = pdfElement.toElements();
				elementCount += elements.length;

				for (Element element : elements) {
					doc.add(element);
				}
			}

			if (elementCount == 0) {
				doc.add(new Phrase(" "));
			}

			if (this.TotalPageNumber) {
				this.footerTotalPageNumber.beginText();
				this.footerTotalPageNumber.showText(" of " + Integer.toString(writer.getPageNumber()));
				this.footerTotalPageNumber.endText();
			}

		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}

	boolean NeedsScaling() {
		return this.scaleValue != 1.0F && this.scaleValue != 0.0F;
	}

	public static enum PageOrientation {
		Portrait(PageSize.A4), Landscape(PageSize.A4.rotate());

		Rectangle pageSizeTemplate;

		private PageOrientation(Rectangle page) {
			this.pageSizeTemplate = page;
		}

		public float getPageHeight() {
			return this.pageSizeTemplate.getHeight();
		}

		public float getPageWidth() {
			return this.pageSizeTemplate.getWidth();
		}
	}

	private class PdfPageEventHelperImpl extends PdfPageEventHelper {
		private final Messages messages;

		public PdfPageEventHelperImpl(Messages messages) {
			this.messages = messages;
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			if (PdfDocument.this.footerText != "") {
				writer.getDirectContent().addTemplate(PdfDocument.this.footerTextTemplate, document.left(), 0.0F);
			}

			if (PdfDocument.this.numberPages) {
				String text = this.messages.getMessage(Messages.Key.WEBREPORT_PDF_PAGE_NUMBER,
						LocaleContextHolder.getLocale(), writer.getPageNumber());
				float textOffset = PdfDocument.this.defaultFooterFont.getWidthPoint(text, PdfDocument.this.fontSize);
				if (PdfDocument.this.TotalPageNumber) {
					writer.getDirectContent().addTemplate(PdfDocument.this.footerTotalPageNumber,
							document.right() - PdfDocument.this.offset, document.bottomMargin() / 2.0F);
					textOffset += PdfDocument.this.offset;
				}

				writer.getDirectContent().beginText();
				writer.getDirectContent().setFontAndSize(PdfDocument.this.defaultFooterFont, PdfDocument.this.fontSize);
				writer.getDirectContent().setTextMatrix(document.right() - textOffset, document.bottomMargin() / 2.0F);
				writer.getDirectContent().showText(text);
				writer.getDirectContent().endText();
			}

		}
	}
}
