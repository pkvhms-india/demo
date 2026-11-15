package com.varicent.common.ui.domain;

public class SelectOption implements Comparable<SelectOption> {
	private final String name;
	private final String value;

	public SelectOption(String name, String value) {
		this.name = name == null ? value : name;
		this.value = value;
	}

	@Override
	public int compareTo(SelectOption option) {
		int result = this.value.compareTo(option.getValue());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (!(o instanceof SelectOption)) {
			return false;
		} else {
			SelectOption option = (SelectOption) o;
			if (!this.name.equals(option.name)) {
				return false;
			} else {
				return this.value.equals(option.value);
			}
		}
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	String unescapeApostrophe(String a) {
		return a.replace("&apos;", "'");
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + this.name.hashCode();
		result = 37 * result + this.value.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
