package com.psd.learn.mysplash.ui.bottomlayout

import android.content.Context
import android.view.View
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.databinding.ActivityMainBinding
import com.psd.learn.mysplash.databinding.BottomMenuItemBinding

class BottomLayoutManager(
    private val context: Context,
    private val activityMainBinding: ActivityMainBinding
) {

    private val bottomMenuMap: HashMap<Int, BottomInfo> = HashMap()

    private var bottomMenuListener: BottomMenuClickListener? = null

    fun addBottomMenuListener(listener: BottomMenuClickListener) {
        bottomMenuListener = listener
    }

    fun removeBottomMenuListener() {
        bottomMenuListener = null
    }

    private fun notifyBottomMenuClick(menuId: Int) {
        bottomMenuListener?.onBottomMenuClicked(menuId)
    }

    init {
        bottomMenuMap[DOWNLOAD_MENU] = BottomInfo(R.drawable.download_icon, "Download")
        bottomMenuMap[ADD_TO_FAVORITE_MENU] = BottomInfo(R.drawable.add_floating_icon, "Add Favorite")
        bottomMenuMap[CANCEL_MENU] = BottomInfo(R.drawable.cancle_icon, "Cancel")
    }

    fun initBottomLayoutView() {
        activityMainBinding.bottomMenuContainer.root.visibility = View.VISIBLE
        bottomMenuMap.forEach { entry ->
            val menuItemBinding = getMenuItemBinding(entry.key)
            menuItemBinding.run {
                bottomMenuIcon.setImageResource(entry.value.icon)
                bottomMenuText.text = entry.value.text
            }
            menuItemBinding.root.setOnClickListener {
                notifyBottomMenuClick(it.id)
            }
        }
    }

    fun hideBottomLayoutMenu() {
        activityMainBinding.bottomMenuContainer.root.visibility = View.GONE
    }

    fun isBottomLayoutMenuShow(): Boolean = activityMainBinding.bottomMenuContainer.root.isShown

    private fun getMenuItemBinding(menuType: Int): BottomMenuItemBinding {
        return when(menuType) {
            DOWNLOAD_MENU -> activityMainBinding.bottomMenuContainer.menuDownload
            ADD_TO_FAVORITE_MENU -> activityMainBinding.bottomMenuContainer.menuAddFavorite
            CANCEL_MENU -> activityMainBinding.bottomMenuContainer.menuCancel
            else -> {
                error("invalid menu")
            }
        }
    }

    companion object {
        private const val DOWNLOAD_MENU = 1001
        private const val ADD_TO_FAVORITE_MENU = 1002
        private const val CANCEL_MENU = 1003
    }
}

data class BottomInfo(val icon: Int, val text: String)

interface BottomMenuClickListener {
    fun onBottomMenuClicked(menuId: Int)
}