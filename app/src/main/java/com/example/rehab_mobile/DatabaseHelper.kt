package com.example.rehab_mobile

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?): SQLiteOpenHelper(context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(Constants.CREATE_USER_TABLE)
        db!!.execSQL(Constants.CREATE_ACTIVITY_TABLE)
        db!!.execSQL(Constants.INSERT_USER_DATA)
        db!!.execSQL(Constants.INSERT_ACTIVITY_DATA)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS" + Constants.USER_TABLE_NAME)
        db!!.execSQL("DROP TABLE IF EXISTS" + Constants.ACTIVITY_TABLE_NAME)
        onCreate(db)
    }

    //Create User Info
    fun insertUserInfo(
        username: String?,
        password: String?,
        firstname: String?,
        lastname: String?,
        dob: String?,
        sex: String?,
        bloodtype: String?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.USERNAME, username)
        values.put(Constants.PASSWORD, password)
        values.put(Constants.FIRSTNAME, firstname)
        values.put(Constants.LASTNAME, lastname)
        values.put(Constants.DOB, dob)
        values.put(Constants.SEX, sex)
        values.put(Constants.BLOODTYPE,bloodtype)
        val id = db.insert(Constants.USER_TABLE_NAME, null, values)
        db.close()
        return id
    }

    // Read All User Records
    fun getAllUserRecords(): ArrayList<UserModelRecord>{
        val recordList = ArrayList<UserModelRecord>()
        val selectQuery = "SELECT * FROM ${Constants.USER_TABLE_NAME}"
        val db = this.writableDatabase
        val cursor =db.rawQuery(selectQuery,null)
        if(cursor.moveToNext()){
            do{
                val userModelRecord = UserModelRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.FIRSTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.LASTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.DOB)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.SEX)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.BLOODTYPE))
                )
                recordList.add(userModelRecord)
            }while(cursor.moveToNext())
        }
        db.close()
        return recordList
    }
    // Read Single User Details
    fun searchUserRecords(query: String): ArrayList<UserModelRecord>{
        val recordList = ArrayList<UserModelRecord>()
        val selectQuery = "SELECT * FROM ${Constants.USER_TABLE_NAME} WHERE ${Constants.USERNAME} LIKE '$query' "
        val db = this.writableDatabase
        val cursor =db.rawQuery(selectQuery,null)
        if(cursor.moveToNext()){
            do{
                val userModelRecord = UserModelRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.PASSWORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.FIRSTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.LASTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.DOB)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.SEX)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.BLOODTYPE))
                )
                recordList.add(userModelRecord)
            }while(cursor.moveToNext())
        }
        db.close()
        return recordList
    }

    // Create Activity Info
    fun insertActivity (
        username: String?,
        activitydate: String?,
        points: Int?,
        stepitup: Int?,
        ballbalance: Int?,
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.USERNAME, username)
        values.put(Constants.ACTIVITYDATE, activitydate)
        values.put(Constants.POINTS, points)
        values.put(Constants.STEPITUP, stepitup)
        values.put(Constants.BALLBALANCE, ballbalance)
        val id = db.insert(Constants.ACTIVITY_TABLE_NAME, null, values)
        db.close()
        return id
    }

    // Update Activity Info
    fun updateActivityRecords(
        activityName: String,
        activitydate: String,
        username: String,
        points: Int,
        activityValue: Int
    ): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        val condition = "${Constants.USERNAME}= '$username' AND ${Constants.ACTIVITYDATE} LIKE '$activitydate' "
        values.put(activityName, activityValue)
        values.put(Constants.POINTS, points)
        val id = db.update(Constants.ACTIVITY_TABLE_NAME, values, condition, null)
        db.close()
        return id

    }

    // Read All Activity Info
    fun getAllActivityRecords():ArrayList<ActivityModelRecord>{
        val recordList = ArrayList<ActivityModelRecord>()
        val selectQuery = "SELECT * FROM ${Constants.ACTIVITY_TABLE_NAME}"
        val db = this.writableDatabase
        val cursor =db.rawQuery(selectQuery,null)
        if(cursor.moveToNext()){
            do{
                val activityModelRecord = ActivityModelRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.ACTIVITYDATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.POINTS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STEPITUP)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.BALLBALANCE))
                )
                recordList.add(activityModelRecord)
            }while(cursor.moveToNext())
        }
        db.close()
        return recordList
    }

    // Read Single User Activity Info
    fun searchActivityRecords(username: String,activitydate: String): ArrayList<ActivityModelRecord>{
        val recordList = ArrayList<ActivityModelRecord>()
        val selectQuery = "SELECT * FROM ${Constants.ACTIVITY_TABLE_NAME} WHERE ${Constants.USERNAME} LIKE '$username' AND ${Constants.ACTIVITYDATE} LIKE '$activitydate'"
        val db = this.writableDatabase
        val cursor =db.rawQuery(selectQuery,null)
        if(cursor.moveToNext()){
            do{
                val activityModelRecord = ActivityModelRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.ACTIVITYDATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.POINTS)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STEPITUP)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.BALLBALANCE))
                )
                recordList.add(activityModelRecord)
            }while(cursor.moveToNext())
        }
        db.close()
        return recordList
    }

}