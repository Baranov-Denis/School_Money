package com.example.schoolmoney.appLab;

import java.util.Locale;
import java.util.UUID;

public class Money implements Comparable<Money> {

    private UUID moneyUuid;
    private String title;
    private String note;
    private String valueIncome;
    private String valueExpenses;
    private String date;


    public UUID getMoneyUuid() {return moneyUuid;}
    public void setMoneyUuid(UUID moneyUuid) {this.moneyUuid = moneyUuid;}
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public String getValueIncome() {
        return valueIncome;
    }
    public void setValueIncome(String valueIncome) {
        this.valueIncome = valueIncome;
    }
    public String getValueExpenses() {
        return valueExpenses;
    }
    public void setValueExpenses(String valueExpenses) {
        this.valueExpenses = valueExpenses;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int compareTo(Money o) {
        String thisMoneyDate = getDate();
        String oMoneyDate = o.getDate();
        if (thisMoneyDate == null && oMoneyDate == null) {
            return 0; // Оба объекта имеют пустые имена ресурсов
        } else if (thisMoneyDate == null) {
            return -1; // Текущий объект имеет пустое имя ресурса
        } else if (oMoneyDate == null) {
            return 1; // Объект 'o' имеет пустое имя ресурса
        } else {
            return thisMoneyDate.toLowerCase(Locale.ROOT).compareTo(oMoneyDate.toLowerCase(Locale.ROOT));
        }
    }
}
