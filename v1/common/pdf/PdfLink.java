package com.varicent.common.pdf;

import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactoryImp;
import com.itextpdf.text.Phrase;

public class PdfLink implements PdfElement {
	private String url = "";
	private PdfText text;

	public PdfLink(FontFactoryImp fontFactory) {
		this.text = new PdfText(fontFactory);
	}

	public Phrase toElement() {
		Phrase phrase = this.text.toElement();
		List<Chunk> chunks = phrase.getChunks();
		if (chunks.size() > 0) {
			chunks.get(0).setAnchor(this.url);
		}

		return phrase;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public PdfText getText() {
		return this.text;
	}

	public void setText(PdfText text) {
		this.text = text;
	}

	@Override
	public float calculateMinimumWidth() {
		return this.text.calculateMinimumWidth();
	}

	@Override
	public float calculateMinimumHeight() {
		return this.text.calculateMinimumHeight();
	}

	@Override
	public Element[] toElements() {
		return new Element[] { this.toElement() };
	}
}
