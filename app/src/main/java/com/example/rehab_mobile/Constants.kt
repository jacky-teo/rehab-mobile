package com.example.rehab_mobile

object Constants {
    const val DB_NAME = "REHAB"
    const val DB_VERSION = 1
    const val TABLE_NAME = "USERS"
    const val ID = "ID"
    const val USERNAME = "USERNAME"
    const val PASSWORD = "PASSWORD"

    //Create Table Query
    const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT," +
            PASSWORD + " TEXT"
            + "); ")
    //Pre Populate DB
    const val INSERT_DATA = ("INSERT INTO TABLE "+ TABLE_NAME +"( "+USERNAME+", "+ PASSWORD +
                             " ) VALUES ('admin', 'admin') ")
}