package com.example.eduinvest.models;

public class BankModel {
    String imageBank,nameBank,titleBank,rateBank,browseBank,loanPeriodBank,limitBank,moneyBank,describleBank,conditionBank,typeBank,contanctBank,key;
    public BankModel() {

    }

    public BankModel(String imageBank,String nameBank, String titleBank, String rateBank, String browseBank, String loanPeriodBank, String limitBank, String moneyBank, String describleBank, String conditionBank,String typeBank) {
        this.imageBank = imageBank;
        this.nameBank = nameBank;
        this.titleBank = titleBank;
        this.rateBank = rateBank;
        this.browseBank = browseBank;
        this.loanPeriodBank = loanPeriodBank;
        this.limitBank = limitBank;
        this.moneyBank = moneyBank;
        this.describleBank = describleBank;
        this.conditionBank = conditionBank;
        this.typeBank = typeBank;
    }
    public BankModel(String imageBank,String nameBank, String titleBank, String rateBank, String browseBank, String loanPeriodBank, String limitBank, String moneyBank, String describleBank, String conditionBank,String typeBank,String contanctBank) {
        this.imageBank = imageBank;
        this.nameBank = nameBank;
        this.titleBank = titleBank;
        this.rateBank = rateBank;
        this.browseBank = browseBank;
        this.loanPeriodBank = loanPeriodBank;
        this.limitBank = limitBank;
        this.moneyBank = moneyBank;
        this.describleBank = describleBank;
        this.conditionBank = conditionBank;
        this.typeBank = typeBank;
        this.contanctBank = contanctBank;
    }
    public String getImageBank() {
        return imageBank;
    }
    public void setImageBank(String imageBank) {
        this.imageBank = imageBank;
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }

    public String getTitleBank() {
        return titleBank;
    }

    public void setTitleBank(String titleBank) {
        this.titleBank = titleBank;
    }

    public String getRateBank() {
        return rateBank;
    }

    public void setRateBank(String rateBank) {
        this.rateBank = rateBank;
    }

    public String getBrowseBank() {
        return browseBank;
    }

    public void setBrowseBank(String browseBank) {
        this.browseBank = browseBank;
    }

    public String getLoanPeriodBank() {
        return loanPeriodBank;
    }

    public void setLoanPeriodBank(String loanPeriodBank) {
        this.loanPeriodBank = loanPeriodBank;
    }

    public String getLimitBank() {
        return limitBank;
    }

    public void setLimitBank(String limitBank) {
        this.limitBank = limitBank;
    }

    public String getMoneyBank() {
        return moneyBank;
    }

    public void setMoneyBank(String moneyBank) {
        this.moneyBank = moneyBank;
    }

    public String getDescribleBank() {
        return describleBank;
    }

    public void setDescribleBank(String describleBank) {
        this.describleBank = describleBank;
    }

    public String getConditionBank() {
        return conditionBank;
    }

    public void setConditionBank(String conditionBank) {
        this.conditionBank = conditionBank;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getTypeBank() {
        return typeBank;
    }
    public void setTypeBank(String typeBank) {
        this.typeBank = typeBank;
    }
    public String getContanctBank() {
        return contanctBank;
    }
    public void setContanctBank(String contanctBank) {
        this.contanctBank = contanctBank;
    }
}
