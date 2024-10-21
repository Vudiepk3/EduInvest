package com.example.eduinvest.models;

public class NewsModel {
    String imageNew,titleNews,timeNews,linkNews,typeNews,key;
    public NewsModel(String imageNew, String titleNews, String timeNews, String linkNews,String typeNews) {
        this.imageNew = imageNew;
        this.titleNews = titleNews;
        this.timeNews = timeNews;
        this.linkNews = linkNews;
        this.typeNews = typeNews;
    }

    public NewsModel() {
    }

    public String getImageNew() {
        return imageNew;
    }

    public void setImageNew(String imageNew) {
        this.imageNew = imageNew;
    }

    public String getTitleNews() {
        return titleNews;
    }

    public void setTitleNews(String titleNews) {
        this.titleNews = titleNews;
    }

    public String getTimeNews() {
        return timeNews;
    }

    public void setTimeNews(String timeNews) {
        this.timeNews = timeNews;
    }

    public String getLinkNews() {
        return linkNews;
    }

    public void setLinkNews(String linkNews) {
        this.linkNews = linkNews;
    }
    public String getTypeNews() {
        return typeNews;
    }
    public void setTypeNews(String typeNews) {
        this.typeNews = typeNews;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
