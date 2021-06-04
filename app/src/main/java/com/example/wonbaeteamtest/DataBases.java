package com.example.wonbaeteamtest;

import android.provider.BaseColumns;

import java.sql.Blob;

// DataBase Table
public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String  SUBJECT= "subject";
        public static final String NAME = "name";
        public static final String IMAGE="image";
        public static final String ADDRESS = "address";
        public static final String PROVIDER = "provider";
        public static final String RECORD = "audio";
        public static final String VIDEO = "video";
        public static final String _TABLENAME = "shelter";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +IMAGE+" BLOB,"
                        +SUBJECT+" integer not null,"
                        +NAME+" text not null , "
                        +ADDRESS+" text , "
                        +PROVIDER+" text,"
                        +RECORD+" audio,"
                        +VIDEO+" video);";
    }
}