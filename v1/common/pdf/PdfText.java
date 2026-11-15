package com.varicent.common.pdf;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactoryImp;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.varicent.core.system.windows.fonts.FontLinking;

public class PdfText implements PdfElement {
	private static final Logger logger = Logger.getLogger(PdfText.class);
	private FontFactoryImp fontFactory;
	private Font font;
	private String text;
	private float maxWidth;

	public PdfText(FontFactoryImp fontFactory) {
		this.fontFactory = fontFactory;
		this.text = "";
		this.setFont(fontFactory.getFont("Tahoma", 10.0F, 0));
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getMaxWidth() {
		return this.maxWidth;
	}

	public void setMaxWidth(float width) {
		this.maxWidth = width;
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		if (font.getBaseFont() != null) {
			this.font = font;
		} else {
			if (this.font == null) {
				logger.info("Preferred font \"Tahoma\" is not installed");
				this.font = this.fontFactory.getFont("Helvetica", 10.0F, 0);
			}

			this.font.setSize(font.getSize());
			this.font.setColor(font.getColor());
			this.font.setStyle(font.getStyle());
		}

	}

	@Override
	public float calculateMinimumWidth() {
		float minWidth = 0.0F;
		BaseFont baseFont = this.font.getBaseFont();

		for (String s : this.text.split("\\s+")) {
			minWidth = Math.max(minWidth, baseFont.getWidthPoint(s + " ", this.font.getSize()));
		}

		if (this.maxWidth > 0.0F && minWidth > this.maxWidth) {
			minWidth = this.maxWidth;
		}

		return minWidth;
	}

	@Override
	public float calculateMinimumHeight() {
		return this.text.split("\\s+").length * this.font.getSize();
	}

	public Phrase toElement() {
		float fontSize = this.font.getSize();
		int fontStyle = this.font.getStyle() & -5;
		BaseColor fontColor = this.font.getColor();
		Font newFont = null;
		if (this.font.getBaseFont().getFontType() == 0) {
			newFont = new Font(this.font.getBaseFont(), fontSize, fontStyle, fontColor);
		} else {
			newFont = this.fontFactory.getFont(this.font.getFamilyname(), "Identity-H", fontSize, fontStyle, fontColor);
		}

		FontSelector fs = new FontSelector();
		fs.addFont(newFont);
		String newFontFamilyName = newFont.getFamilyname();
		Collection<String> linkedFontNames = FontLinking.getInstance().getFontLink(newFontFamilyName);
		if (linkedFontNames == null) {
			linkedFontNames = FontLinking.getInstance().getFontLink("Tahoma");
		}

		if (linkedFontNames != null) {
			for (String linkedFontName : linkedFontNames) {
				Font linkedFont = this.fontFactory.getFont(linkedFontName);
				if (linkedFont != null) {
					BaseFont linkedFontBase = linkedFont.getBaseFont();
					if (linkedFontBase != null) {
						Font newLinkedFont = this.fontFactory.getFont(linkedFont.getFamilyname(), "Identity-H",
								fontSize, fontStyle, fontColor);
						fs.addFont(newLinkedFont);
					} else {
						logger.warn(String.format("Error evaluating base font of \"%s\", which is linked by \"%s\"",
								linkedFontName, newFontFamilyName));
					}
				} else {
					logger.warn(String.format("Error evaluating font \"%s\", which is linked by \"%s\"", linkedFontName,
							newFontFamilyName));
				}
			}
		}

		Phrase phrase = fs.process(this.text);
		if (this.font.isUnderlined()) {
			float thickness = this.font.isBold() ? 1.2F : 1.0F;

			for (Chunk c : phrase.getChunks()) {
				c.setUnderline(thickness, -1.0F);
			}
		}

		return phrase;
	}

	@Override
	public Element[] toElements() {
		return new Element[] { this.toElement() };
	}
}
