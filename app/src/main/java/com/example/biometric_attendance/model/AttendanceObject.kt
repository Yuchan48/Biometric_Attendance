package com.example.biometric_attendance.model

class AttendanceObject {
    var userId: String? = null
    var date: String? = null
    var checkinTime: String? = null
    var checkoutTime: String? = null

    constructor(userId: String, date: String, checkinTime: String, checkoutTime: String){
        this.userId = userId
        this.date = date
        this.checkinTime = checkinTime
        this.checkoutTime = checkoutTime
    }
}