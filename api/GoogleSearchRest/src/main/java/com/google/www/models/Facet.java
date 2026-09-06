package com.google.www.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Facet {

	private String anchor;
	private String label;
	@JsonAlias("label_with_op")
	private String labelWithOp;

	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabelWithOp() {
		return labelWithOp;
	}

	public void setLabelWithOp(String labelWithOp) {
		this.labelWithOp = labelWithOp;
	}

}