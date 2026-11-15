package com.varicent.common.ui.domain;

public class CellFont {
	private static final String CSS_NONE = "none";
	private static final String CSS_NORMAL = "normal";
	private static final String CSS_BOLD = "bold";
	private static final String CSS_ITALIC = "italic";
	private static final String CSS_UNDERLINE = "underline";
	private static final String CSS_STRIKEOUT = "line-through";
	private static final short FLAG_REGULAR = 0;
	private static final short FLAG_BOLD = 1;
	private static final short FLAG_ITALIC = 2;
	private static final short FLAG_UNDERLINE = 4;
	private static final short FLAG_STRIKEOUT = 8;
	private String decoration;
	private final String family;
	private final String style;
	private final String weight;
	private float size;

	public static CellFont defaultFont() {
		return new CellFont("Tahoma", 9.75F, (short) 0);
	}

	public CellFont(String family, float size, short style) {
		this.family = family;
		this.size = size;
		this.style = this.generateStyle(style);
		this.weight = this.generateWeight(style);
		this.decoration = this.generateDecoration(style);
	}

	public String getDecoration() {
		return this.decoration;
	}

	public String getFamily() {
		return this.family;
	}

	public float getSize() {
		return this.size;
	}

	public String getStyle() {
		return this.style;
	}

	public void scaleSize(float scale) {
		assert scale > 0.0F;

		this.size *= scale;
	}

	public String getWeight() {
		return this.weight;
	}

	public boolean isBold() {
		return this.weight.equals("bold");
	}

	public boolean isItalic() {
		return this.style.equals("italic");
	}

	public boolean isStruckout() {
		return this.decoration.contains("line-through");
	}

	public boolean isUnderlined() {
		return this.decoration.contains("underline");
	}

	private String generateDecoration(short style) {
		StringBuilder sb = new StringBuilder();
		if ((style & 4) == 4) {
			sb.append("underline");
		}

		if ((style & 8) == 8) {
			if (sb.length() > 0) {
				sb.append(" ");
			}

			sb.append("line-through");
		}

		if (sb.length() == 0) {
			sb.append("none");
		}

		return sb.toString();
	}

	private String generateStyle(short style) {
		return (style & 2) == 2 ? "italic" : "normal";
	}

	private String generateWeight(short style) {
		return (style & 1) == 1 ? "bold" : "normal";
	}

	public CellFont(String family, float size, String weight, String cssStyle) {
		this(family, size, getStyle(weight, cssStyle));
	}

	public CellFont(String family, float size, String weight, String cssStyle, String decoration) {
		this(family, size, getStyle(weight, cssStyle));
		this.decoration = decoration;
	}

	private static short getStyle(String weight, String cssStyle) {
		short style = 0;
		if (weight.equalsIgnoreCase("bold")) {
			style = (short) (style | 1);
		}

		if (cssStyle.equalsIgnoreCase("italic")) {
			style = (short) (style | 2);
		}

		return style;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj == this) {
			return true;
		} else if (!(obj instanceof CellFont)) {
			return false;
		} else {
			CellFont other = (CellFont) obj;
			return this.family.equals(other.family) && this.style.equals(other.style)
					&& this.weight.equals(other.weight) && this.size == other.size
					&& this.decoration.equals(other.decoration);
		}
	}

	@Override
	public int hashCode() {
		return this.family.hashCode() + this.style.hashCode() + this.weight.hashCode() + (int) this.size
				+ this.decoration.hashCode();
	}
}
