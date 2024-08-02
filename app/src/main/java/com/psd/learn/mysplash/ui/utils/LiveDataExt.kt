package com.psd.learn.mysplash.ui.utils

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> LiveData<T>.debounce(duration: Long, coroutineScope: CoroutineScope): LiveData<T> {
    val mediatorLiveData = MediatorLiveData<T>()
    var job: Job? = null
    mediatorLiveData.addSource(this) {value ->
        job?.cancel()
        job = coroutineScope.launch {
            delay(duration)
            mediatorLiveData.value = value
        }
    }
    return mediatorLiveData
}

fun <T> MutableLiveData<T>.updateValue(value: T) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        this.value = value
    } else {
        this.postValue(value)
    }
}