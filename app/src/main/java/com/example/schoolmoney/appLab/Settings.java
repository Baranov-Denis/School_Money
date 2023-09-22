package com.example.schoolmoney.appLab;

public class Settings {
    private String moneyTarget;
    private String token;
    private final String settingsId;


    public Settings() {
        this.settingsId = "settingsId";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMoneyTarget() {
        return moneyTarget;
    }

    public void setMoneyTarget(String moneyTarget) {
        this.moneyTarget = moneyTarget;
    }


}
