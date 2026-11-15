package com.varicent.common.pdf;

import com.itextpdf.text.Element;

public interface PdfElement {
	Element[] toElements();

	float calculateMinimumWidth();

	float calculateMinimumHeight();
}
