package com.example.biometric_attendance.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.biometric_attendance.model.AttendanceObject
import com.example.biometric_attendance.model.User
import java.util.Calendar


class DatabaseHandler(var context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USER = "Users"
        private const val COL_ID = "userId"
        private const val COL_USERNAME = "username"
        private const val COL_EMAIL = "email"
        private const val COL_PASSWORD = "password"

        private const val TABLE_ATTENDANCE = "attendance"
        private const val COL_DATE = "date"
        private const val COL_CHECKIN_TIME = "checkinTime"
        private const val COL_CHECKOUT_TIME = "checkoutTime"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTableQuery = ("CREATE TABLE $TABLE_USER (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COL_USERNAME TEXT," +
                "$COL_EMAIL TEXT," +
                "$COL_PASSWORD TEXT)")

        val createCheckinQuery =
            ("CREATE TABLE $TABLE_ATTENDANCE ($COL_ID INTEGER, $COL_DATE TEXT, $COL_CHECKIN_TIME TEXT, $COL_CHECKOUT_TIME TEXT)")

        db?.execSQL(createUserTableQuery)
        db?.execSQL(createCheckinQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ATTENDANCE")
        onCreate(db)
    }

    fun insertUser(user: User): Long {
        val values = ContentValues().apply {
            put(COL_USERNAME, user.username)
            put(COL_EMAIL, user.email)
            put(COL_PASSWORD, user.password)
        }

        val db = this.writableDatabase
        val result = db.insert(TABLE_USER, null, values)

        if (result == (-1).toLong())
            Toast.makeText(context, "Failed to store user data in database", Toast.LENGTH_SHORT)
                .show()
        else
            Toast.makeText(context, "Success storing user data in database", Toast.LENGTH_SHORT)
                .show()

        db.close()
        return result

    }


    fun returnUserId(email: String, password: String): Int {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT $COL_ID FROM $TABLE_USER WHERE $COL_EMAIL = \'" + email + "\' AND  $COL_PASSWORD = \'" + password + "\'",
            null
        )

        var userId = -1
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(0)
        }

        cursor.close()
        db.close()
        return userId
    }

    // userid date checkinTime checkoutTime
    fun insertCheckin(userId: String, date: String): Long {

        val values = ContentValues().apply {
            put(COL_ID, userId)
            put(COL_DATE, date)
            put(COL_CHECKIN_TIME, getCurrentTime())
        }

        val db = this.writableDatabase
        val result = db.insert(TABLE_ATTENDANCE, null, values)

        if (result == (-1).toLong())
            Toast.makeText(
                context,
                "Failed to store checkin activity in database",
                Toast.LENGTH_SHORT
            )
                .show()
        else
            Toast.makeText(
                context,
                "Success storing checkin activity in database",
                Toast.LENGTH_SHORT
            )
                .show()


        // db.close()
        return result


    }

    fun insertCheckout(userId: String, date: String): Boolean {

        val currentTime: String = getCurrentTime()
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(COL_CHECKOUT_TIME, currentTime)
        }
        val selection = "$COL_ID = ? AND $COL_DATE = ?"
        val selectionArgs = arrayOf(userId, date)

        val count = db.update(TABLE_ATTENDANCE, values, selection, selectionArgs)

        if (count > 0) {
            Toast.makeText(
                context,
                "Success storing checkout activity in database",
                Toast.LENGTH_SHORT
            )
                .show()

            return true
        } else {
            Toast.makeText(
                context,
                "Failed to store checkout activity in database",
                Toast.LENGTH_SHORT
            )
                .show()


            return false
        }


    }

    fun statusToday(userId: String, date: String, operation: String): Boolean {
        val db = readableDatabase

        val colToCheck = if (operation == "checkin") {
            COL_CHECKIN_TIME
        } else {
            COL_CHECKOUT_TIME
        }


        val cursor = db.rawQuery(
            "SELECT $colToCheck FROM $TABLE_ATTENDANCE WHERE $COL_ID = \'" + userId + "\' AND $COL_DATE = \'" + date + "\'",
            null
        )


        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {


                println("Cursor Index: " + cursor.getString(cursor.getColumnIndexOrThrow(colToCheck)))
                if (cursor.getString(cursor.getColumnIndexOrThrow(colToCheck)) != null) {
                    cursor.close()
                    return true
                }
            }

        }

        cursor.close()
        return false
    }

    fun attendanceRecord(userId: String): List<AttendanceObject> {
        val db = readableDatabase

        val selection = "$COL_ID = ?"
        val selectionArgs = arrayOf(userId)

        val cursor = db.query(TABLE_ATTENDANCE, null, selection, selectionArgs, null, null, null)

        val attendanceList = mutableListOf<AttendanceObject>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE))
                val checkinTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_CHECKIN_TIME))
                val checkoutTime = cursor.getString(cursor.getColumnIndexOrThrow(COL_CHECKOUT_TIME))
                val dayRecord = AttendanceObject(userId, date, checkinTime, checkoutTime)
                attendanceList.add(dayRecord)
            }
        }

        cursor.close()
        db.close()
        return attendanceList

    }

    private fun getCurrentTime(): String {
        val calendar = Calendar.getInstance()
        val h = calendar.get(Calendar.HOUR_OF_DAY)
        val m = calendar.get(Calendar.MINUTE)
        val curTime = java.lang.String.format("%02d:%02d", h, m)
        return curTime
    }

    fun deleteAttendance() {
        val db = writableDatabase
        db.rawQuery("DELETE FROM $TABLE_ATTENDANCE", null)
    }

}