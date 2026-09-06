package com.google.www.models;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.HashMap;
import java.util.Map;

public class Metatag {
    @JsonAlias("og:type")
    private String ogType;
    @JsonAlias("article:published_time")
    private String articlePublishedTime;
    @JsonAlias("og:site_name")
    private String ogSiteName;
    @JsonAlias("article:modified_time")
    private String articleModifiedTime;
    private String viewport;
    @JsonAlias("og:title")
    private String ogTitle;
    @JsonAlias("og:locale")
    private String ogLocale;
    @JsonAlias("twitter:label1")
    private String twitterLabel1;
    @JsonAlias("og:url")
    private String ogUrl;
    @JsonAlias("twitter:data1")
    private String twitterData1;
    private String moddate;
    @JsonAlias("creation date")
    private String creationdate;
    private String creator;
    private String producer;
    private String title;
    @JsonAlias("msapplication-tilecolor")
    private String msapplicationTilecolor;
    @JsonAlias("msapplication-config")
    private String msapplicationConfig;
    @JsonAlias("og:image")
    private String ogImage;
    @JsonAlias("theme-color")
    private String themeColor;
    @JsonAlias("twitter:card")
    private String twitterCard;
    @JsonAlias("tec-api-origin")
    private String tecApiOrigin;
    @JsonAlias("msapplication-tileimage")
    private String msapplicationTileimage;
    @JsonAlias("og:description")
    private String ogDescription;
    @JsonAlias("apple-mobile-web-app-capable")
    private String appleMobileWebAppCapable;
    @JsonAlias("mobile-web-app-capable")
    private String mobileWebAppCapable;
    @JsonAlias("tec-api-version")
    private String tecApiVersion;
    private String creationDate;
    @JsonAlias("count paragraphs")
    private String countParagraphs;
    private String slides;
    @JsonAlias("slide notes")
    private String slideNotes;
    @JsonAlias("links up to date")
    private String linksUpToDate;
    @JsonAlias("application version")
    private String applicationVersion;
    @JsonAlias("edit minutes")
    private String editMinutes;
    private String author;
    private String source;
    @JsonAlias("revision number")
    private String revisionNumber;
    @JsonAlias("links dirty")
    private String linksDirty;
    @JsonAlias("presentation format")
    private String presentationFormat;
    @JsonAlias("multimedia clips")
    private String multimediaClips;
    @JsonAlias("shared doc")
    private String sharedDoc;
    @JsonAlias("scale crop")
    private String scaleCrop;
    @JsonAlias("last saved date")
    private String lastSavedDate;
    @JsonAlias("last saved by")
    private String lastSavedBy;
    @JsonAlias("word count")
    private String wordCount;
    @JsonAlias("hidden slides")
    private String hiddenSlides;
    private String handheldfriendly;
    private String mobileoptimized;
    @JsonAlias("og:updated_time")
    private String ogUpdatedTime;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getOgType() {
        return ogType;
    }

    public void setOgType(String ogType) {
        this.ogType = ogType;
    }

    public String getArticlePublishedTime() {
        return articlePublishedTime;
    }

    public void setArticlePublishedTime(String articlePublishedTime) {
        this.articlePublishedTime = articlePublishedTime;
    }

    public String getOgSiteName() {
        return ogSiteName;
    }

    public void setOgSiteName(String ogSiteName) {
        this.ogSiteName = ogSiteName;
    }

    public String getArticleModifiedTime() {
        return articleModifiedTime;
    }

    public void setArticleModifiedTime(String articleModifiedTime) {
        this.articleModifiedTime = articleModifiedTime;
    }

    public String getViewport() {
        return viewport;
    }

    public void setViewport(String viewport) {
        this.viewport = viewport;
    }

    public String getOgTitle() {
        return ogTitle;
    }

    public void setOgTitle(String ogTitle) {
        this.ogTitle = ogTitle;
    }

    public String getOgLocale() {
        return ogLocale;
    }

    public void setOgLocale(String ogLocale) {
        this.ogLocale = ogLocale;
    }

    public String getTwitterLabel1() {
        return twitterLabel1;
    }

    public void setTwitterLabel1(String twitterLabel1) {
        this.twitterLabel1 = twitterLabel1;
    }

    public String getOgUrl() {
        return ogUrl;
    }

    public void setOgUrl(String ogUrl) {
        this.ogUrl = ogUrl;
    }

    public String getTwitterData1() {
        return twitterData1;
    }

    public void setTwitterData1(String twitterData1) {
        this.twitterData1 = twitterData1;
    }

    public String getModdate() {
        return moddate;
    }

    public void setModdate(String moddate) {
        this.moddate = moddate;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsapplicationTilecolor() {
        return msapplicationTilecolor;
    }

    public void setMsapplicationTilecolor(String msapplicationTilecolor) {
        this.msapplicationTilecolor = msapplicationTilecolor;
    }

    public String getMsapplicationConfig() {
        return msapplicationConfig;
    }

    public void setMsapplicationConfig(String msapplicationConfig) {
        this.msapplicationConfig = msapplicationConfig;
    }

    public String getOgImage() {
        return ogImage;
    }

    public void setOgImage(String ogImage) {
        this.ogImage = ogImage;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public String getTwitterCard() {
        return twitterCard;
    }

    public void setTwitterCard(String twitterCard) {
        this.twitterCard = twitterCard;
    }

    public String getTecApiOrigin() {
        return tecApiOrigin;
    }

    public void setTecApiOrigin(String tecApiOrigin) {
        this.tecApiOrigin = tecApiOrigin;
    }

    public String getMsapplicationTileimage() {
        return msapplicationTileimage;
    }

    public void setMsapplicationTileimage(String msapplicationTileimage) {
        this.msapplicationTileimage = msapplicationTileimage;
    }

    public String getOgDescription() {
        return ogDescription;
    }

    public void setOgDescription(String ogDescription) {
        this.ogDescription = ogDescription;
    }

    public String getAppleMobileWebAppCapable() {
        return appleMobileWebAppCapable;
    }

    public void setAppleMobileWebAppCapable(String appleMobileWebAppCapable) {
        this.appleMobileWebAppCapable = appleMobileWebAppCapable;
    }

    public String getMobileWebAppCapable() {
        return mobileWebAppCapable;
    }

    public void setMobileWebAppCapable(String mobileWebAppCapable) {
        this.mobileWebAppCapable = mobileWebAppCapable;
    }

    public String getTecApiVersion() {
        return tecApiVersion;
    }

    public void setTecApiVersion(String tecApiVersion) {
        this.tecApiVersion = tecApiVersion;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCountParagraphs() {
        return countParagraphs;
    }

    public void setCountParagraphs(String countParagraphs) {
        this.countParagraphs = countParagraphs;
    }

    public String getSlides() {
        return slides;
    }

    public void setSlides(String slides) {
        this.slides = slides;
    }

    public String getSlideNotes() {
        return slideNotes;
    }

    public void setSlideNotes(String slideNotes) {
        this.slideNotes = slideNotes;
    }

    public String getLinksUpToDate() {
        return linksUpToDate;
    }

    public void setLinksUpToDate(String linksUpToDate) {
        this.linksUpToDate = linksUpToDate;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getEditMinutes() {
        return editMinutes;
    }

    public void setEditMinutes(String editMinutes) {
        this.editMinutes = editMinutes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public String getLinksDirty() {
        return linksDirty;
    }

    public void setLinksDirty(String linksDirty) {
        this.linksDirty = linksDirty;
    }

    public String getPresentationFormat() {
        return presentationFormat;
    }

    public void setPresentationFormat(String presentationFormat) {
        this.presentationFormat = presentationFormat;
    }

    public String getMultimediaClips() {
        return multimediaClips;
    }

    public void setMultimediaClips(String multimediaClips) {
        this.multimediaClips = multimediaClips;
    }

    public String getSharedDoc() {
        return sharedDoc;
    }

    public void setSharedDoc(String sharedDoc) {
        this.sharedDoc = sharedDoc;
    }

    public String getScaleCrop() {
        return scaleCrop;
    }

    public void setScaleCrop(String scaleCrop) {
        this.scaleCrop = scaleCrop;
    }

    public String getLastSavedDate() {
        return lastSavedDate;
    }

    public void setLastSavedDate(String lastSavedDate) {
        this.lastSavedDate = lastSavedDate;
    }

    public String getLastSavedBy() {
        return lastSavedBy;
    }

    public void setLastSavedBy(String lastSavedBy) {
        this.lastSavedBy = lastSavedBy;
    }

    public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }

    public String getHiddenSlides() {
        return hiddenSlides;
    }

    public void setHiddenSlides(String hiddenSlides) {
        this.hiddenSlides = hiddenSlides;
    }

    public String getHandheldfriendly() {
        return handheldfriendly;
    }

    public void setHandheldfriendly(String handheldfriendly) {
        this.handheldfriendly = handheldfriendly;
    }

    public String getMobileoptimized() {
        return mobileoptimized;
    }

    public void setMobileoptimized(String mobileoptimized) {
        this.mobileoptimized = mobileoptimized;
    }

    public String getOgUpdatedTime() {
        return ogUpdatedTime;
    }

    public void setOgUpdatedTime(String ogUpdatedTime) {
        this.ogUpdatedTime = ogUpdatedTime;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}