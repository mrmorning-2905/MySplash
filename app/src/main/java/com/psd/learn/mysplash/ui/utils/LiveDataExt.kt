package com.psd.learn.mysplash.ui.utils

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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