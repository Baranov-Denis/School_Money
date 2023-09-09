package com.example.schoolmoney.database;

public class DbSchema {


    public static final class ChildTable {

        public static final String NAME = "Children";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String CHILD_NAME = "child_name";
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

    public static final class NoteTable {

        public static final String NAME = "Notes";

        public static final class Cols {
            public static final String CHILD_UUID = "child_uuid";
            public static final String NOTE = "note";
        }
    }


}
