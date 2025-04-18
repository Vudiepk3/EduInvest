package com.example.eduinvest.models;

public class LoanRequestModel extends LoanModel {
    // Thuộc tính mới của LoanRequestModel
    String namePerson, phoneNumber, gender, birthDate, email, note, status, key, idUser, linkData;
    public LoanRequestModel() {
        super();
    }
    public LoanRequestModel(String imageBank, String nameBank, String titleBank, String rateBank, String loanPeriodBank, String limitBank, String namePerson, String phoneNumber, String gender, String birthDate, String email, String note, String typeBank, String status) {
        super(); // Gọi constructor của BankModel
        // Thiết lập các giá trị thuộc tính từ BankModel
        this.imageBank = imageBank;
        this.nameBank = nameBank;
        this.titleBank = titleBank;
        this.rateBank = rateBank;
        this.loanPeriodBank = loanPeriodBank;
        this.limitBank = limitBank;
        // Thiết lập giá trị cho các thuộc tính mới
        this.namePerson = namePerson;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.note = note;
        this.typeBank = typeBank;
        this.status = status;
    }

    public LoanRequestModel(String imageBank, String nameBank, String titleBank, String rateBank, String loanPeriodBank, String limitBank, String contanctBank, String namePerson, String phoneNumber, String gender, String birthDate, String email, String note, String typeBank, String status) {
        super(); // Gọi constructor của BankModel
        // Thiết lập các giá trị thuộc tính từ BankModel
        this.imageBank = imageBank;
        this.nameBank = nameBank;
        this.titleBank = titleBank;
        this.rateBank = rateBank;
        this.loanPeriodBank = loanPeriodBank;
        this.limitBank = limitBank;
        this.contanctBank = contanctBank;
        // Thiết lập giá trị cho các thuộc tính mới
        this.namePerson = namePerson;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.note = note;
        this.typeBank = typeBank;
        this.status = status;
    }

    public LoanRequestModel(String imageBank, String nameBank, String titleBank, String rateBank, String loanPeriodBank, String limitBank, String contanctBank, String namePerson, String phoneNumber, String gender, String birthDate, String email, String note, String typeBank, String status, String idUser) {
        super(); // Gọi constructor của BankModel

        // Thiết lập các giá trị thuộc tính từ BankModel
        this.imageBank = imageBank;
        this.nameBank = nameBank;
        this.titleBank = titleBank;
        this.rateBank = rateBank;
        this.loanPeriodBank = loanPeriodBank;
        this.limitBank = limitBank;
        this.contanctBank = contanctBank;
        // Thiết lập giá trị cho các thuộc tính mới
        this.namePerson = namePerson;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.note = note;
        this.typeBank = typeBank;
        this.status = status;
        this.idUser = idUser;
    }

    public LoanRequestModel(String imageBank, String nameBank, String titleBank, String rateBank, String loanPeriodBank, String limitBank, String contanctBank, String namePerson, String phoneNumber, String gender, String birthDate, String email, String note, String typeBank, String status, String idUser, String linkData) {
        super(); // Gọi constructor của BankModel

        // Thiết lập các giá trị thuộc tính từ BankModel
        this.imageBank = imageBank;
        this.nameBank = nameBank;
        this.titleBank = titleBank;
        this.rateBank = rateBank;
        this.loanPeriodBank = loanPeriodBank;
        this.limitBank = limitBank;
        this.contanctBank = contanctBank;
        // Thiết lập giá trị cho các thuộc tính mới
        this.namePerson = namePerson;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.note = note;
        this.typeBank = typeBank;
        this.status = status;
        this.idUser = idUser;
        this.linkData = linkData;
    }


    // Getter và Setter cho các thuộc tính mới
    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    public String getLinkData() {
        return linkData;
    }
    public void setLinkData(String linkData) {
        this.linkData = linkData;
    }

}

