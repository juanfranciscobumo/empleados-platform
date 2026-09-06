package com.google.www.models;

import java.util.List;

public class ResponseGoogleSearch {
	private String kind;
	private Url url;
	private Queries queries;
	private Context context;
	private SearchInformation searchInformation;
	private Spelling spelling;
	private List<Item> items = null;

	public Spelling getSpelling() {
		return spelling;
	}

	public void setSpelling(Spelling spelling) {
		this.spelling = spelling;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public Queries getQueries() {
		return queries;
	}

	public void setQueries(Queries queries) {
		this.queries = queries;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public SearchInformation getSearchInformation() {
		return searchInformation;
	}

	public void setSearchInformation(SearchInformation searchInformation) {
		this.searchInformation = searchInformation;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

}
