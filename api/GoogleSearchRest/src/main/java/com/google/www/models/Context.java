package com.google.www.models;

import java.util.List;

public class Context {

	private String title;
	private List<List<Facet>> facets = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<List<Facet>> getFacets() {
		return facets;
	}

	public void setFacets(List<List<Facet>> facets) {
		this.facets = facets;
	}

}