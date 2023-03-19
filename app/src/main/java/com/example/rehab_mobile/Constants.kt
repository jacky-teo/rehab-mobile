package com.example.rehab_mobile

object Constants {
    const val DB_NAME = "REHAB"
    const val DB_VERSION = 1
    const val USER_TABLE_NAME = "USERS"
    const val STEP_ACTIVITY_TABLE_NAME = "STEP_ACTIVITY"
    const val BALL_ACTIVITY_TABLE_NAME = "BALL_ACTIVITY"
    const val POINTS_TABLE_NAME = "POINTS"
    const val REWARDS_TABLE_NAME = "REWARDS"

    const val ID = "ID"
    const val USERNAME = "USERNAME"
    const val PASSWORD = "PASSWORD"
    const val FIRSTNAME = "FIRSTNAME"
    const val LASTNAME = "LASTNAME"
    const val DOB = "DOB"
    const val SEX = "SEX"
    const val BLOODTYPE = "BLOODTYPE"


    const val STEPITUP = "STEPITUP"
    const val BALLBALANCE = "BALLBALANCE"
    const val ACTIVITYDATE = "ACTIVITYDATE"


    const val POINTS = "POINTS"

    const val VOUCHERVALUE = "VOUCHERVALUE"
    const val DATEOFREDEMPTION = "DATEOFREDEMPTION"



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

    //Create Step Activity Table Query
    const val CREATE_STEP_ACTIVITY_TABLE = ("CREATE TABLE " + STEP_ACTIVITY_TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT, " +
            ACTIVITYDATE + " TEXT, " +
            STEPITUP + " INTEGER "
            + "); ")

    //Create Ball Activity Table Query
    const val CREATE_BALL_ACTIVITY_TABLE = ("CREATE TABLE " + BALL_ACTIVITY_TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT, " +
            ACTIVITYDATE + " TEXT, " +
            BALLBALANCE + " INTEGER "
            + "); ")

    //Create Points Table Query
    const val CREATE_POINTS_TABLE = ("CREATE TABLE " + POINTS_TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT, " +
            POINTS + " INTEGER "
            + "); ")


    //Create Rewards Table Query
    const val CREATE_REWARDS_TABLE = ("CREATE TABLE " + REWARDS_TABLE_NAME + "(" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            USERNAME + " TEXT, " +
            VOUCHERVALUE + " INTEGER, "+
            DATEOFREDEMPTION + " TEXT "
            + "); ")

    //Pre Populate User Table
    const val INSERT_USER_DATA = ("INSERT INTO "+ USER_TABLE_NAME +
            "( "+ USERNAME +", "+ PASSWORD +", " +
            FIRSTNAME+", "+ LASTNAME+", "+
            DOB+", "+ SEX+", "+ BLOODTYPE +
            " ) VALUES ('admin', 'admin','Jacky','Teo','10/1/2002','Male', 'O+'); ")

    //Pre Populate STEP Activity Table
    const val INSERT_STEP_ACTIVITY_DATA = ("INSERT INTO "+ STEP_ACTIVITY_TABLE_NAME +
            "( "+USERNAME+", "+ ACTIVITYDATE +", " + STEPITUP +
            " ) VALUES ('admin', '28-2-2022',0); ")

    //Pre Populate BALL Activity Table
    const val INSERT_BALL_ACTIVITY_DATA = ("INSERT INTO "+ BALL_ACTIVITY_TABLE_NAME +
            "( "+USERNAME+", "+ ACTIVITYDATE +", " + BALLBALANCE +
            " ) VALUES ('admin', '28-2-2022',0); ")

    //Pre Populate Activity Table
    const val INSERT_POINT_DATA = ("INSERT INTO "+ POINTS_TABLE_NAME +
            "( "+USERNAME+", " + POINTS +
            " ) VALUES ('admin',0); ")
}