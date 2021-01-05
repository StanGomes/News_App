package com.stansdevhouse.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.stansdevhouse.newsapp.databinding.MainActivityBinding
import com.stansdevhouse.newsapp.ui.main.NewsListFragment
import dagger.hilt.android.AndroidEntryPoint

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