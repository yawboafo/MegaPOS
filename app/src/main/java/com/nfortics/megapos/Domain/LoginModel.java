package com.nfortics.megapos.Domain;

import java.util.List;

/**
 * Created by bigifre on 7/29/2015.
 */
public class LoginModel {

    private String Id;
    private String Name;
    private List<BankModel> Banks;
    private List<TelcoModel>Telcos;
    private List<BillerModel> Billers;


    public LoginModel() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<BankModel> getBanks() {
        return Banks;
    }

    public void setBanks(List<BankModel> banks) {
        Banks = banks;
    }

    public List<TelcoModel> getTelcos() {
        return Telcos;
    }

    public void setTelcos(List<TelcoModel> telcos) {
        Telcos = telcos;
    }

    public List<BillerModel> getBillers() {
        return Billers;
    }

    public void setBillers(List<BillerModel> billers) {
        Billers = billers;
    }
}
