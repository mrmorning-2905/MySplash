

package com.psd.learn.mysplash

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.psd.learn.mysplash.databinding.ActivityMainBinding
import com.psd.learn.mysplash.ui.bottomlayout.BottomLayoutManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    private val mainViewModel by viewModels<MainViewModel>()

    private val mBottomMenuLayout by lazy(LazyThreadSafetyMode.NONE) {
        BottomLayoutManager(this, binding)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarLayout)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appbarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbarLayout.setupWithNavController(navController, appbarConfiguration)
        observerBottomMenuLayout()
    }

    private fun observerBottomMenuLayout() {
        mainViewModel.isShowBottomMenu.observe(this) { isShow ->
            Log.d("sangpd", "observerBottomMenuLayout_isShow: $isShow")
            if (isShow) {
                mBottomMenuLayout.initBottomLayoutView()
            } else {
                mBottomMenuLayout.hideBottomLayoutMenu()
            }
        }
    }

    fun getBottomMenuLayout(): BottomLayoutManager {
        return mBottomMenuLayout
    }
}