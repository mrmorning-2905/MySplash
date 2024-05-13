package com.psd.learn.mysplash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.psd.learn.mysplash.ui.feed.photos.FeedPhotosViewModel

@Suppress("UNCHECKED_CAST")
val ViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val unSplashApiService = ServiceLocator.unSplashService
            Log.d("sangpd", "ViewModelFactory_create_unSplashApiService: $unSplashApiService")
            when {
                isAssignableFrom(FeedPhotosViewModel::class.java) -> FeedPhotosViewModel(unSplashApiService)
                else -> IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}