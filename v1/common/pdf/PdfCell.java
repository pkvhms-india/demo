package com.varicent.common.pdf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfCell {
	final float PointPerPixel = 0.5625F;
	private List<PdfElement> elements = new ArrayList();
	private PdfCellBorders borders = new PdfCellBorders();
	private PdfCellPadding padding;
	private BaseColor background;
	private HorizontalAlignment horizontalAlign;
	private VerticalAlignment verticalAlign;
	private boolean noWrap;
	private boolean useBorderPadding;

	public PdfCell() {
		this.borders.setAllColor(BaseColor.WHITE);
		this.borders.setAllWidth(0.0F);
		this.padding = new PdfCellPadding();
		this.padding.setAll(2.0F);
		this.background = BaseColor.WHITE;
		this.horizontalAlign = PdfCell.HorizontalAlignment.Left;
		this.verticalAlign = PdfCell.VerticalAlignment.Middle;
		this.noWrap = false;
		this.useBorderPadding = true;
	}

	public PdfCellBorders getBorders() {
		return this.borders;
	}

	public void setBorders(PdfCellBorders borders) {
		this.borders = borders;
	}

	public PdfCellPadding getPadding() {
		return this.padding;
	}

	public void setPadding(PdfCellPadding padding) {
		this.padding = padding;
	}

	public BaseColor getBackground() {
		return this.background;
	}

	public void setBackground(Color background) {
		this.setBackground(new BaseColor(background.getRGB()));
	}

	public void setBackground(BaseColor background) {
		this.background = background;
	}

	public HorizontalAlignment getHorizontalAlign() {
		return this.horizontalAlign;
	}

	public void setHorizontalAlign(HorizontalAlignment horizontalAlign) {
		this.horizontalAlign = horizontalAlign;
	}

	public VerticalAlignment getVerticalAlign() {
		return this.verticalAlign;
	}

	public void setVerticalAlign(VerticalAlignment verticalAlign) {
		this.verticalAlign = verticalAlign;
	}

	public boolean isNoWrap() {
		return this.noWrap;
	}

	public void setNoWrap(boolean noWrap) {
		this.noWrap = noWrap;
	}

	public boolean getUseBorderPadding() {
		return this.useBorderPadding;
	}

	public void setUseBorderPadding(boolean useBorderPadding) {
		this.useBorderPadding = useBorderPadding;
	}

	public List<PdfElement> getElements() {
		return this.elements;
	}

	public void add(PdfElement element) {
		this.elements.add(element);
	}

	public float calculateMinimumWidth() {
		float minWidth = 0.0F;

		for (PdfElement element : this.elements) {
			minWidth = Math.max(minWidth, element.calculateMinimumWidth());
		}

		return minWidth + this.borders.left.width + this.borders.right.width + this.padding.left + this.padding.right;
	}

	public float calculateMinimumHeight() {
		float max = 0.0F;

		for (PdfElement element : this.elements) {
			max = Math.max(max, element.calculateMinimumHeight());
		}

		return max + this.borders.top.width + this.borders.bottom.width + this.padding.top + this.padding.bottom;
	}

	PdfPCell toElement() {
		new PdfPCell();
		boolean isImage = this.elements.size() == 1 && this.elements.get(0) instanceof PdfImage;
		PdfPCell cell;
		if (isImage) {
			Image img = ((PdfImage) this.elements.get(0)).toElement();
			cell = new PdfPCell(img, false);
		} else {
			cell = new PdfPCell();
		}

		cell.setUseAscender(true);
		cell.setUseDescender(true);
		cell.setBorderWidthTop(this.borders.getTop().getWidth() * 0.5625F);
		cell.setBorderWidthBottom(this.borders.getBottom().getWidth() * 0.5625F);
		cell.setBorderWidthLeft(this.borders.getLeft().getWidth() * 0.5625F);
		cell.setBorderWidthRight(this.borders.getRight().getWidth() * 0.5625F);
		cell.setBorderColorTop(this.borders.getTop().getColor());
		cell.setBorderColorBottom(this.borders.getBottom().getColor());
		cell.setBorderColorLeft(this.borders.getLeft().getColor());
		cell.setBorderColorRight(this.borders.getRight().getColor());
		cell.setPaddingTop(this.padding.getTop() * 0.5625F);
		cell.setPaddingBottom(this.padding.getBottom() * 0.5625F);
		cell.setPaddingLeft(this.padding.getLeft() * 0.5625F);
		cell.setPaddingRight(this.padding.getRight() * 0.5625F);
		cell.setBackgroundColor(this.background);
		switch (this.horizontalAlign) {
		case Center:
			cell.setHorizontalAlignment(1);
			break;
		case Justify:
			cell.setHorizontalAlignment(3);
			break;
		case Left:
			cell.setHorizontalAlignment(0);
			break;
		case Right:
			cell.setHorizontalAlignment(2);
		}

		switch (this.verticalAlign) {
		case Bottom:
			cell.setVerticalAlignment(6);
			break;
		case Middle:
			cell.setVerticalAlignment(5);
			break;
		case Top:
			cell.setVerticalAlignment(4);
		}

		cell.setNoWrap(this.noWrap);
		cell.setUseBorderPadding(this.useBorderPadding);
		if (isImage) {
			return cell;
		} else {
			List<Element> iElements = new ArrayList();

			for (PdfElement pdfElement : this.elements) {
				for (Element element : pdfElement.toElements()) {
					iElements.add(element);
				}
			}

			if ((cell.getPaddingTop() > 0.0F || cell.getPaddingBottom() > 0.0F)
					&& (iElements.isEmpty() || onlyContainsEmptyPhrases(iElements))) {
				float totalVerticalPadding = cell.getPaddingTop() + cell.getPaddingBottom();
				float totalVerticalBorderWidth = cell.getBorderWidthTop() + cell.getBorderWidthBottom();
				cell.setMinimumHeight(totalVerticalPadding + totalVerticalBorderWidth);
			}

			if (iElements.size() == 1) {
				Element element = iElements.get(0);
				if (element instanceof Phrase) {
					cell.setPhrase((Phrase) element);
				} else {
					cell.addElement(element);
				}
			} else {
				for (Element e : iElements) {
					cell.addElement(e);
				}
			}

			return cell;
		}
	}

	static boolean onlyContainsEmptyPhrases(Collection<Element> elements) {
		for (Element e : elements) {
			if (!(e instanceof Phrase) || !((Phrase) e).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	public static enum HorizontalAlignment {
		Justify, Left, Center, Right;
	}

	public static enum VerticalAlignment {
		Top, Middle, Bottom;
	}

	public class PdfCellBorders {
		private PdfCellBorder top = PdfCell.this.new PdfCellBorder();
		private PdfCellBorder bottom = PdfCell.this.new PdfCellBorder();
		private PdfCellBorder left = PdfCell.this.new PdfCellBorder();
		private PdfCellBorder right = PdfCell.this.new PdfCellBorder();

		public PdfCellBorder getTop() {
			return this.top;
		}

		public void setTop(PdfCellBorder top) {
			this.top = top;
		}

		public PdfCellBorder getBottom() {
			return this.bottom;
		}

		public void setBottom(PdfCellBorder bottom) {
			this.bottom = bottom;
		}

		public PdfCellBorder getLeft() {
			return this.left;
		}

		public void setLeft(PdfCellBorder left) {
			this.left = left;
		}

		public PdfCellBorder getRight() {
			return this.right;
		}

		public void setRight(PdfCellBorder right) {
			this.right = right;
		}

		public void setAllColor(Color color) {
			this.setAllColor(new BaseColor(color.getRGB()));
		}

		public void setAllColor(BaseColor color) {
			this.top.color = this.bottom.color = this.left.color = this.right.color = color;
		}

		public void setAllWidth(float value) {
			this.top.width = this.bottom.width = this.left.width = this.right.width = value;
		}
	}

	public class PdfCellBorder {
		private float width;
		private BaseColor color;

		public PdfCellBorder() {
			this(0.0F, BaseColor.BLACK);
		}

		public PdfCellBorder(float width, BaseColor color) {
			this.width = width;
			this.color = color;
		}

		public float getWidth() {
			return this.width;
		}

		public void setWidth(float width) {
			this.width = width;
		}

		public BaseColor getColor() {
			return this.color;
		}

		public void setColor(Color color) {
			this.setColor(new BaseColor(color.getRGB()));
		}

		public void setColor(BaseColor color) {
			this.color = color;
		}
	}

	public class PdfCellPadding {
		private float top;
		private float bottom;
		private float left;
		private float right;

		public PdfCellPadding() {
			this(0.0F);
		}

		public PdfCellPadding(float size) {
			this.top = this.bottom = this.left = this.right = size;
		}

		public float getTop() {
			return this.top;
		}

		public void setTop(float top) {
			this.top = top;
		}

		public float getBottom() {
			return this.bottom;
		}

		public void setBottom(float bottom) {
			this.bottom = bottom;
		}

		public float getLeft() {
			return this.left;
		}

		public void setLeft(float left) {
			this.left = left;
		}

		public float getRight() {
			return this.right;
		}

		public void setRight(float right) {
			this.right = right;
		}

		public void setAll(float value) {
			this.top = this.bottom = this.left = this.right = value;
		}
	}
}
