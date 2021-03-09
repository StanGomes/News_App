package com.stansdevhouse.newsapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.distinctUntilChanged
import com.google.android.material.snackbar.Snackbar
import com.stansdevhouse.newsapp.databinding.MainActivityBinding
import com.stansdevhouse.newsapp.ui.NewsListFragment
import com.stansdevhouse.newsapp.util.ConnectionStateLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
//        window.statusBarColor = ContextWrapper(this).getColor(android.R.color.transparent)
//        window.navigationBarColor = ContextWrapper(this).getColor(android.R.color.transparent)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewsListFragment.newInstance())
                .commitNow()
        }

        ConnectionStateLiveData(this).distinctUntilChanged().observe(this) { connected ->
            if (!connected) {
                Snackbar.make(this.window.decorView, "Connection Lost!", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
