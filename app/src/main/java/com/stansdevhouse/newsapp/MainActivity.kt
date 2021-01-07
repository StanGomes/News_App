package com.stansdevhouse.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stansdevhouse.newsapp.databinding.MainActivityBinding
import com.stansdevhouse.newsapp.ui.NewsListFragment
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
    }
}