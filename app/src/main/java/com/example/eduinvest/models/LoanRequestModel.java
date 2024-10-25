package com.example.eduinvest.models;

public class LoanRequestModel extends LoanModel {
    // Thuộc tính mới của LoanRequestModel
    String namePerson,phoneNumber,gender,birthDate,email,note,status,key;
    // Constructor mặc định
    public LoanRequestModel() {
        super(); // Gọi constructor của BankModel
    }

    // Constructor để khởi tạo cả thuộc tính từ BankModel và các thuộc tính mới
    public LoanRequestModel(String rateBank,String loanPeriodBank,String limitBank, String namePerson, String phoneNumber, String gender, String birthDate, String email,String note,String status) {
        super(); // Gọi constructor của BankModel

        // Thiết lập các giá trị thuộc tính từ BankModel
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
        this.status = status;
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

}

