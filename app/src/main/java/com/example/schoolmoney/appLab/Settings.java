package com.example.schoolmoney.appLab;

public class Settings {
    private String moneyTarget;
    private final String settingsId;

    public Settings() {
        this.settingsId = "settingsId";
    }

    public String getMoneyTarget() {
        return moneyTarget;
    }

    public void setMoneyTarget(String moneyTarget) {
        this.moneyTarget = moneyTarget;
    }


}
