package com.example.eduinvest.models;

public class BannerModel {
    private String urlImage,nameImage,linkWeb,noteImage;

    public BannerModel(String urlImage, String nameImage, String linkWeb, String noteImage) {
        this.urlImage = urlImage;
        this.nameImage = nameImage;
        this.linkWeb = linkWeb;
        this.noteImage = noteImage;
    }
    public BannerModel(){

    }
    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getNameImage() {
        return nameImage;
    }

    public void setNameImage(String nameImage) {
        this.nameImage = nameImage;
    }

    public String getLinkWeb() {
        return linkWeb;
    }

    public void setLinkWeb(String linkWeb) {
        this.linkWeb = linkWeb;
    }

    public String getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(String noteImage) {
        this.noteImage = noteImage;
    }

}

