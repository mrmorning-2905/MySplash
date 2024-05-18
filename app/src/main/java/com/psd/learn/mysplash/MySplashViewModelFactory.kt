package com.psd.learn.mysplash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.psd.learn.mysplash.ui.viewmodels.FeedCollectionsViewModel
import com.psd.learn.mysplash.ui.viewmodels.FeedPhotosViewModel
import com.psd.learn.mysplash.ui.viewmodels.SearchCollectionViewModel
import com.psd.learn.mysplash.ui.viewmodels.SearchPhotoViewModel

@Suppress("UNCHECKED_CAST")
val ViewModelFactory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            val unSplashApiService = ServiceLocator.unSplashService
            Log.d("sangpd", "ViewModelFactory_create_unSplashApiService: $unSplashApiService")
            when {
                isAssignableFrom(FeedPhotosViewModel::class.java) -> FeedPhotosViewModel(unSplashApiService)
                isAssignableFrom(FeedCollectionsViewModel::class.java) -> FeedCollectionsViewModel(unSplashApiService)
                isAssignableFrom(SearchPhotoViewModel::class.java) -> SearchPhotoViewModel(unSplashApiService)
                isAssignableFrom(SearchCollectionViewModel::class.java) -> SearchCollectionViewModel(unSplashApiService)
                else -> IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}