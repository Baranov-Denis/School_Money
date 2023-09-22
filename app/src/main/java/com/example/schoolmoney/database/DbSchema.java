package com.example.schoolmoney.database;

public class DbSchema {


    public static final class ChildTable {
        public static final String NAME = "Children";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String CHILD_NAME = "child_name";
            public static final String NOTE = "note";
        }
    }

    public static final class ParentTable {
        public static final String NAME = "Parents";
        public static final class Cols {
            public static final String CHILD_UUID = "child_uuid";
            public static final String PARENT_NAME = "parent_name";
            public static final String PARENT_PHONE = "parent_phone";
        }
    }

    public static final class SettingsTable {
        public static final String NAME = "Settings";
        public static final class Cols {
            public static final String SETTINGS_ID = "settings_id";
            public static final String MONEY_TARGET = "money_target";
            public static final String DROPBOX_TOKEN = "dropbox_token";
        }
    }

    public static final class MoneyTable {
        public static final String NAME = "Money";
        public static final class Cols {

            public static final String MONEY_UUID = "money_uuid";
            public static final String CHILD_UUID = "child_uuid";
            public static final String TITLE = "title";
            public static final String NOTE = "note";
            public static final String VALUE_INCOME = "value_income";
            public static final String VALUE_EXPENSES = "value_expenses";
            public static final String DATE = "date";
        }
    }


}
