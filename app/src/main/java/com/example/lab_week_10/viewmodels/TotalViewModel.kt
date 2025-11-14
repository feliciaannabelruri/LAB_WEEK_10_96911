package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab_week_10.database.TotalObject

class TotalViewModel : ViewModel() {
    private val _total = MutableLiveData<TotalObject>()
    val total: LiveData<TotalObject> = _total

    init {
        _total.postValue(TotalObject(0, ""))
    }

    fun incrementTotal() {
        val currentValue = _total.value?.value ?: 0
        _total.postValue(TotalObject(currentValue + 1, _total.value?.date ?: ""))
    }

    fun setTotal(newTotal: TotalObject) {
        _total.postValue(newTotal)
    }

    fun updateDate(date: String) {
        val currentValue = _total.value?.value ?: 0
        _total.postValue(TotalObject(currentValue, date))
    }
}