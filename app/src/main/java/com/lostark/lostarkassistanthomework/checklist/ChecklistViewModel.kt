package com.lostark.lostarkassistanthomework.checklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChecklistViewModel : ViewModel() {
    val allGold: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val gold: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val progress: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val maxProgress: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val percent: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    init {
        allGold.value = 0
        gold.value = 0
        progress.value = 0
        maxProgress.value = 0
        percent.value = 0
    }
}