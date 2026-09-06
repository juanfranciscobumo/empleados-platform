package com.google.www.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagemap {

	private List<Metatag> metatags = null;
	@JsonAlias("cse_thumbnail")
	private List<CseThumbnail> cseThumbnail = null;
	@JsonAlias("cse_image")
	private List<CseImage> cseImage = null;
	@JsonAlias("collegeoruniversity")
	private List<Collegeoruniversity> collegeoruniversity = null;
	private List<Postaladdress> postaladdress = null;
	private List<Webpage> webpage = null;
	private List<Sitenavigationelement> sitenavigationelement = null;
	private List<Newsarticle> newsarticle = null;
	private List<Item> item = null;

	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}

	public List<Metatag> getMetatags() {
		return metatags;
	}

	public void setMetatags(List<Metatag> metatags) {
		this.metatags = metatags;
	}

	public List<CseThumbnail> getCseThumbnail() {
		return cseThumbnail;
	}

	public void setCseThumbnail(List<CseThumbnail> cseThumbnail) {
		this.cseThumbnail = cseThumbnail;
	}

	public List<CseImage> getCseImage() {
		return cseImage;
	}

	public void setCseImage(List<CseImage> cseImage) {
		this.cseImage = cseImage;
	}

}