package com.varicent.common.ui.domain;

import java.awt.Color;

public class CellStyle {
	protected final CellFont font;
	protected final Color color;
	protected final Color backColor;
	protected final Alignment alignment;

	public CellStyle(CellFont font, Color color, Color backColor, Alignment alignment) {
		this.font = font;
		this.color = color;
		this.backColor = backColor;
		this.alignment = alignment;
	}

	public Alignment getAlignment() {
		return this.alignment;
	}

	public Color getBackColor() {
		return this.backColor;
	}

	public Color getColor() {
		return this.color;
	}

	public CellFont getFont() {
		return this.font;
	}

	public boolean isTransparent() {
		return this.getBackColor().getAlpha() == 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (!(obj instanceof CellStyle)) {
			return false;
		} else {
			CellStyle other = (CellStyle) obj;
			return this.font.equals(other.font) && this.color.equals(other.color)
					&& this.backColor.equals(other.backColor) && this.alignment.equals(other.alignment);
		}
	}

	@Override
	public int hashCode() {
		return this.font.hashCode() + this.color.hashCode() + this.backColor.hashCode() + this.alignment.hashCode();
	}

	public static enum Alignment {
		JUSTIFY, LEFT, CENTER, RIGHT;
	}

	public static enum VerticalAlignment {
		TOP, MIDDLE, BOTTOM;
	}
}
