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
        db!!.execSQL(Constants.CREATE_TABLE)
        db!!.execSQL(Constants.INSERT_DATA)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS" + Constants.TABLE_NAME)
        onCreate(db)
    }

    fun insertInfo(
        username: String?,
        password: String?,
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Constants.USERNAME, username)
        values.put(Constants.PASSWORD, password)
        val id = db.insert(Constants.TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getAllRecords(): ArrayList<ModelRecord>{
        val recordList = ArrayList<ModelRecord>()
        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME}"
        val db = this.writableDatabase
        val cursor =db.rawQuery(selectQuery,null)
        if(cursor.moveToNext()){
            do{
                val modelRecord = ModelRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.PASSWORD))
                )
                recordList.add(modelRecord)
            }while(cursor.moveToNext())
        }
        db.close()
        return recordList
    }
    fun searchRecords(query: String): ArrayList<ModelRecord>{
        val recordList = ArrayList<ModelRecord>()
        val selectQuery = "SELECT * FROM ${Constants.TABLE_NAME} WHERE ${Constants.USERNAME} LIKE $query"
        val db = this.writableDatabase
        val cursor =db.rawQuery(selectQuery,null)
        if(cursor.moveToNext()){
            do{
                val modelRecord = ModelRecord(
                    cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(Constants.PASSWORD))
                )
                recordList.add(modelRecord)
            }while(cursor.moveToNext())
        }
        db.close()
        return recordList
    }

}