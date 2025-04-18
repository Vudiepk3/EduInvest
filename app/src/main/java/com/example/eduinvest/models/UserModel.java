package com.example.eduinvest.models;


import java.util.List;

public class UserModel {

    private String id;
    private String emailId;
    private String name;
    private String image;
    private String password;

    private String socialLink;
    private String cvLink;


    public UserModel() {
        this.id = "";
        this.emailId = "";
        this.name = "";
        this.image = "";

    }
    public UserModel(String name, String id, String emailId) {
        this.id = id;
        this.emailId = emailId;
        this.name = name;
    }
    public UserModel(String name, String id, String emailId,String password) {
        this.id = id;
        this.emailId = emailId;
        this.name = name;
        this.password = password;
    }
    public UserModel(String id, String emailId, String name, String image, String password,List<LoanRequestModel> loanRequestList) {
        this.id = id;
        this.emailId = emailId;
        this.name = name;
        this.image = image;
        this.password = password;
    }

    public UserModel(String id, String emailId, String name, String image, String password, String socialLink, String cvLink) {
        this.id = id;
        this.emailId = emailId;
        this.name = name;
        this.image = image;
        this.password = password;
        this.socialLink = socialLink;
        this.cvLink = cvLink;
    }

    public UserModel(String id, String emailId, String name, String image, double allTimeScore, double weeklyScore, double monthlyScore, double lastGameScore) {
        this.id = id;
        this.emailId = emailId;
        this.name = name;
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSocialLink() {
        return socialLink;
    }

    public void setSocialLink(String socialLink) {
        this.socialLink = socialLink;
    }

    public String getCvLink() {
        return cvLink;
    }

    public void setCvLink(String cvLink) {
        this.cvLink = cvLink;
    }
}

