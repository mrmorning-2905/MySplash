

package com.psd.learn.mysplash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.psd.learn.mysplash.databinding.ActivityMainBinding
import com.psd.learn.mysplash.ui.feed.FeedFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<FeedFragment>(
                    containerViewId = R.id.fragment_container_view,
                    tag = FeedFragment::class.java.simpleName
                )
            }
        }
    }
}