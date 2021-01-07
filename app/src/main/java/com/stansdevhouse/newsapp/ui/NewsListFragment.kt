package com.stansdevhouse.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import com.google.android.material.snackbar.Snackbar
import com.stansdevhouse.newsapp.databinding.NewsListFragmentBinding
import com.stansdevhouse.newsapp.extensions.fragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NewsListFragment : Fragment() {

    companion object {
        fun newInstance() = NewsListFragment()
    }

    private val viewModel by viewModels<NewsListViewModel>()
    private var binding by fragmentBinding<NewsListFragmentBinding>()
    private val adapter: NewsListAdapter by lazy { NewsListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newsList.adapter = adapter

        binding.refreshBtn.setOnClickListener {
            viewModel.refreshNews()
        }

        viewModel.newsListViewState.distinctUntilChanged().observe(viewLifecycleOwner){
            when (it) {
                Result.Loading -> binding.progress.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progress.visibility = View.GONE
                    adapter.submitList(it.news)
                }
                is Result.Error -> {
                    binding.progress.visibility = View.GONE
                    Snackbar.make(view, it.errorMessage, Snackbar.LENGTH_SHORT)
                }
            }
        }
    }

}