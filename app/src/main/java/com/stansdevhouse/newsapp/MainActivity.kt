package com.stansdevhouse.newsapp

import android.os.Bundle
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
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewsListFragment.newInstance())
                .commitNow()
        }

        ConnectionStateLiveData(this).distinctUntilChanged().observe(this){ connected ->
            if (!connected) {
                Snackbar.make(this.window.decorView, "Connection Lost!", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
