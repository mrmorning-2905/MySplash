package com.psd.learn.mysplash.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.psd.learn.mysplash.R
import com.psd.learn.mysplash.ui.widget.CustomTabViewHolder
import com.psd.learn.mysplash.ui.widget.TabItem

abstract class BaseFragment<VB : ViewBinding>(
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

    protected fun setupViewPager(
        viewPager: ViewPager2,
        tabLayout: TabLayout,
        pagerAdapter: FragmentStateAdapter,
        tabTitleArr: ArrayList<String>
    ) {
        viewPager.run {
            adapter = pagerAdapter

            with(tabLayout) {
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        (tab?.customView as? CustomTabViewHolder)
                            ?.run { tabItemStatus = tabItemStatus.copy(isSelected = true) }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        (tab?.customView as? CustomTabViewHolder)
                            ?.run { tabItemStatus = tabItemStatus.copy(isSelected = false) }
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                    }

                })
            }

            TabLayoutMediator(tabLayout, this) { tab, position ->
                tab.apply {
                    customView = CustomTabViewHolder(context).apply {
                        tabItemStatus = TabItem(
                            text = tabTitleArr[position],
                            isSelected = position == 0
                        )
                    }
                }
            }.attach()
        }
    }
}