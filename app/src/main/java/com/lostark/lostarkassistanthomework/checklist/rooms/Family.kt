package com.lostark.lostarkassistanthomework.checklist.rooms

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "family")
data class Family (
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String,
    var icon: String,
    var now: Int,
    var max: Int,
    var end: String,
    var position: Int,
    var type: String
)