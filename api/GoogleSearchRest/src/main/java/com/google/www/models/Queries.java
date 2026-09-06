package com.google.www.models;

import java.util.List;

public class Queries {

	private List<Request> request = null;
	private List<NextPage>nextPage=null;

	public List<NextPage> getNextPage() {
		return nextPage;
	}

	public void setNextPage(List<NextPage> nextPage) {
		this.nextPage = nextPage;
	}

	public List<Request> getRequest() {
		return request;
	}

	public void setRequest(List<Request> request) {
		this.request = request;
	}

}