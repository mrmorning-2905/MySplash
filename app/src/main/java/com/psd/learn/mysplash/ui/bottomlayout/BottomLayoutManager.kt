package com.psd.learn.mysplash.ui.bottomlayout

import android.view.View
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.databinding.ActivityMainBinding

class BottomLayoutManager(
    private val activityMainBinding: ActivityMainBinding
) {

    private val bottomMenuMap: HashMap<Int, BottomInfo> = HashMap()
    init {
        bottomMenuMap[DOWNLOAD_MENU] = BottomInfo(R.drawable.download_icon, "Download")
        bottomMenuMap[ADD_TO_FAVORITE_MENU] = BottomInfo(R.drawable.add_floating_icon, "Add Favorite")
        bottomMenuMap[CANCEL_MENU] = BottomInfo(R.drawable.cancle_icon, "Cancel")
    }
    fun initBottomLayoutView() {
        activityMainBinding.bottomMenuContainer.root.visibility = View.VISIBLE
        bottomMenuMap.forEach { entry ->
            //val menuItem = activityMainBinding.bottomMenuContainer.
        }
    }

    companion object {
        private const val DOWNLOAD_MENU = 1001
        private const val ADD_TO_FAVORITE_MENU = 1002
        private const val CANCEL_MENU = 1003
    }
}

data class BottomInfo(private val icon: Int, private val text: String)