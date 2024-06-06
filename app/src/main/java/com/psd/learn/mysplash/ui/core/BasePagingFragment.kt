package com.psd.learn.mysplash.ui.core

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.R

abstract class BasePagingFragment<VB: ViewBinding>(
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : BaseFragment<VB>(inflate) {

    protected var gridLayoutManager: GridLayoutManager? = null

    protected open val mItemClickListener = object : OnItemClickListener {
        override fun coverPhotoClicked(photoId: String?) {
            showMessageToast("clicked on photo have id: $photoId")
        }

        override fun profileClicked(userId: String?) {
            showMessageToast("clicked on user profile have id: $userId")
        }
    }

    private fun showMessageToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gridLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        gridLayoutManager?.let {
            it.spanCount = resources.getInteger(R.integer.grid_column_count)
        }
    }
}