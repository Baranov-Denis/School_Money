package com.example.schoolmoney.appLab;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Child implements Comparable<Child> {
    private UUID uuid;
    private String childName;
    private String note;
    private List<Parent> parentsList;
    private List<Money> moneyList;


    public Child() {
        moneyList = new ArrayList<>();
        parentsList = new ArrayList<>();
    }

    public List<Money> getMoneyList() {
        return moneyList;
    }
    public void addMoney(Money money) {
        moneyList.add(money);
    }
    public List<Parent> getParentsList() {
        return parentsList;
    }

    public void addParent(Parent parent) {
        parentsList.add(parent);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    @Override
    public int compareTo(Child o) {
        String thisChildName = getChildName();
        String oChildName = o.getChildName();
        if (thisChildName == null && oChildName == null) {
            return 0; // Оба объекта имеют пустые имена ресурсов
        } else if (thisChildName == null) {
            return -1; // Текущий объект имеет пустое имя ресурса
        } else if (oChildName == null) {
            return 1; // Объект 'o' имеет пустое имя ресурса
        } else {
            return thisChildName.toLowerCase(Locale.ROOT).compareTo(oChildName.toLowerCase(Locale.ROOT));
        }
    }

}
