package com.lostark.lostarkassistanthomework.checklist.rooms

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "homework")
data class Homework (
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String,
    var level: Double,
    var server: String,
    var job: String,
    var daylist: String,
    var daynows: String,
    var daymaxs: String,
    var dayicons: String,
    var dayends: String,
    var weeklist: String,
    var weeknows: String,
    var weekmaxs: String,
    var weekicons: String,
    var weekends: String,
    var dungeonrest: Int,
    var bossrest: Int,
    var questrest: Int
)