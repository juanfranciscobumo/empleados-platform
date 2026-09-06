package com.google.www.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Label {

	private String name;
	private String displayName;
	@JsonAlias("label_with_op")
	private String labelWithOp;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getLabelWithOp() {
		return labelWithOp;
	}

	public void setLabelWithOp(String labelWithOp) {
		this.labelWithOp = labelWithOp;
	}

}
