package com.stansdevhouse.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refreshBtn.setOnClickListener {
            viewModel.refreshNews()
        }

        viewModel.newsListViewState.distinctUntilChanged().observe(viewLifecycleOwner){
            when (it) {
                Result.Loading -> binding.message.text = "Loading"
                is Result.Success -> binding.message.text = it.news.toString()
                is Result.Error -> binding.message.text = it.errorMessage
            }
        }
    }

}