package com.lostark.lostarkassistanthomework.checklist.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework

class EditViewModel : ViewModel() {
    val homework: MutableLiveData<Homework> by lazy {
        MutableLiveData<Homework>()
    }

    init {
        homework.value = Homework(
            0,
            "",
            0.0,
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            0,
            0,
            0,
            0,
            0,
            0,
            false,
            0,
            false
        )
    }
}