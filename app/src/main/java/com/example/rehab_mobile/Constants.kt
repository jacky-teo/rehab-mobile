package com.example.rehab_mobile

object Constants {
    const val DB_NAME = "REHAB"
    const val DB_VERSION = 1
    const val USER_TABLE_NAME = "USERS"
    const val ACTIVITY_TABLE_NAME = "ACTIVITIES"
    const val REWARDS_TABLE_NAME = "REWARDS"
    const val ID = "ID"
    const val USERNAME = "USERNAME"
    const val PASSWORD = "PASSWORD"
    const val FIRSTNAME = "FIRSTNAME"
    const val LASTNAME = "LASTNAME"
    const val DOB = "DOB"
    const val SEX = "SEX"
    const val BLOODTYPE = "BLOODTYPE"

    const val POINTS = "POINTS"
    const val STEPITUP = "STEPITUP"
    const val BALLBALANCE = "BALLBALANCE"
    const val ACTIVITYDATE = "ACTIVITYDATE"

    //Create Users Table Query
    const val CREATE_USER_TABLE = ("CREATE TABLE " + USER_TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT, " +
            PASSWORD + " TEXT, " +
            FIRSTNAME + " TEXT, " +
            LASTNAME + " TEXT, " +
            DOB + " TEXT, " +
            SEX + " TEXT, " +
            BLOODTYPE + " TEXT "
            + "); ")

    //Create Activity Table Query
    const val CREATE_ACTIVITY_TABLE = ("CREATE TABLE " + ACTIVITY_TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT, " +
            ACTIVITYDATE + " TEXT, " +
            POINTS + " INTEGER, " +
            STEPITUP + " INTEGER, " +
            BALLBALANCE + " INTEGER  "
            + "); ")

    //Pre Populate DB
    const val INSERT_USER_DATA = ("INSERT INTO "+ USER_TABLE_NAME +
            "( "+ USERNAME +", "+ PASSWORD +", " +
            FIRSTNAME+", "+ LASTNAME+", "+
            DOB+", "+ SEX+", "+ BLOODTYPE +
            " ) VALUES ('admin', 'admin','Jacky','Teo','10/1/2002','Male', 'O+'); ")

    //Pre Populate DB
    const val INSERT_ACTIVITY_DATA = ("INSERT INTO "+ ACTIVITY_TABLE_NAME +
            "( "+USERNAME+", "+ ACTIVITYDATE +", " +
            POINTS+", "+ STEPITUP +", "+
            BALLBALANCE +
            " ) VALUES ('admin', '28/2/2022',0 , 0 ,0); ")
}