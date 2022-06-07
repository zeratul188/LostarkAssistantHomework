package com.lostark.lostarkassistanthomework.checklist.objects

import android.graphics.drawable.Drawable

data class ShareChecklist (
    var name: String,
    var icon: Drawable,
    var now: Int,
    var max: Int,
    var end: String
) {

}