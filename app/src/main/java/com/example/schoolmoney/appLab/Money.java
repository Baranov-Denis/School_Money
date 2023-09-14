package com.example.schoolmoney.appLab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class Money implements Comparable<Money> {

    private UUID moneyUuid;
    private String title;
    private String note;
    private String valueIncome;
    private String valueExpenses;
    private Date date;


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
    public String getValueExpenses() {return valueExpenses; }
    public void setValueExpenses(String valueExpenses) {
        this.valueExpenses = valueExpenses;
    }
    public String getDate() {
        // Установите желаемый локальный язык (например, русский)
        Locale locale = new Locale("ru", "RU");
        // Создаем объект SimpleDateFormat для форматирования даты.
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", locale);
        return dateFormat.format(this.date);
    }
    public void setDate(String dateString) {
        // Установите желаемый локальный язык (например, русский)
        Locale locale = new Locale("ru", "RU");
        // Создаем объект SimpleDateFormat для форматирования даты.
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", locale);

        try {
            this.date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
