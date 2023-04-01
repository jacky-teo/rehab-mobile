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
//    const val PASSWORD = "PASSWORD"
    const val FIRSTNAME = "FIRSTNAME"
    const val LASTNAME = "LASTNAME"
    const val DOB = "DOB"
    const val SEX = "SEX"
    const val BLOODTYPE = "BLOODTYPE"
    const val WEIGHT = "WEIGHT"
    const val HEIGHT = "HEIGHT"


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
            FIRSTNAME + " TEXT, " +
            LASTNAME + " TEXT, " +
            DOB + " TEXT, " +
            SEX + " TEXT, " +
            BLOODTYPE + " TEXT, " +
            HEIGHT + " TEXT, " +
            WEIGHT + " TEXT "
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

    const val INSERT_USER_DATA = ("INSERT INTO "+ USER_TABLE_NAME +
            "( "+ USERNAME +", "+
            FIRSTNAME+", "+ LASTNAME+", "+
            DOB+", "+ SEX+", "+ BLOODTYPE + ", "+ HEIGHT+", "+ WEIGHT +
            " ) VALUES ('jane@gmail.com','jane','doe','4/3/1997','female', 'AB', '145m', '35kg'); ")

    //Pre Populate STEP Activity Table
    const val INSERT_STEP_ACTIVITY_DATA = ("INSERT INTO "+ STEP_ACTIVITY_TABLE_NAME +
            "( "+USERNAME+", "+ ACTIVITYDATE +", " + STEPITUP +
            " ) VALUES ('jane@gmail.com', '28-2-2022',0); ")

    //Pre Populate BALL Activity Table
    const val INSERT_BALL_ACTIVITY_DATA = ("INSERT INTO "+ BALL_ACTIVITY_TABLE_NAME +
            "( "+USERNAME+", "+ ACTIVITYDATE +", " + BALLBALANCE +
            " ) VALUES ('jane@gmail.com', '28-2-2022',0); ")

    //Pre Populate Activity Table
    const val INSERT_POINT_DATA = ("INSERT INTO "+ POINTS_TABLE_NAME +
            "( "+USERNAME+", " + POINTS +
            " ) VALUES ('jane@gmail.com',0); ")
}