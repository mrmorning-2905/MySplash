package com.psd.learn.mysplash.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.data.local.entity.PhotoItem
import com.psd.learn.mysplash.ui.feed.photos.favorite.FavoritePhotoHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    protected open val TAG = BaseFragment::class.java.simpleName

    private var _binding: VB? = null

    protected val binding get() = _binding!!

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflate(inflater, container, false).also { _binding = it }.root

    @CallSuper
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected fun setupToolbar(needShowActionBar: Boolean, title: String, needHomeAsUp: Boolean) {
        val actionbar = (activity as? AppCompatActivity)?.supportActionBar
        if (!needShowActionBar) {
            actionbar?.hide()
            return
        }
        actionbar?.apply {
            show()
            setDisplayHomeAsUpEnabled(needHomeAsUp)
            setHomeButtonEnabled(needHomeAsUp)
            setDisplayShowTitleEnabled(needHomeAsUp)
            setTitle(title)
            setDisplayShowCustomEnabled(false)
            customView = null
            setBackgroundDrawable(null)
            setHomeAsUpIndicator(R.drawable.navigation_back_icon)

        }
    }

    protected fun executeFavorite(currentState: Boolean, photoItem: PhotoItem) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                FavoritePhotoHelper.executeAddOrRemoveFavorite(requireContext(), photoItem, currentState)
            }
        }
    }
}