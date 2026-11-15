package com.varicent.common.theme.domain;

public class Theme {
	private final boolean enabled;
	private final String headerColor;
	private final byte[] headerImage;
	private final CustomThemeType themeType;

	public Theme(boolean enabled, String headerColor, byte[] headerImage, CustomThemeType themeType) {
		this.enabled = enabled;
		this.headerColor = headerColor;
		this.headerImage = headerImage;
		this.themeType = themeType;
	}

	public CustomThemeType getThemeType() {
		return this.themeType;
	}

	public String getHeaderColor() {
		return this.headerColor;
	}

	public byte[] getHeaderImage() {
		return this.headerImage;
	}

	public boolean getEnabled() {
		return this.enabled;
	}

	public static enum CustomThemeType {
		Standard(0), SFDC(1);

		private int code;

		private CustomThemeType(int code) {
			this.code = code;
		}

		public static CustomThemeType valueOf(int code) {
			for (CustomThemeType type : values()) {
				if (type.code == code) {
					return type;
				}
			}

			throw new IllegalArgumentException("invalid code: " + code);
		}
	}
}
