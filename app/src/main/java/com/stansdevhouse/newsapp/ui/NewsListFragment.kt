package com.stansdevhouse.newsapp.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.snackbar.Snackbar
import com.stansdevhouse.newsapp.R
import com.stansdevhouse.newsapp.databinding.NewsListFragmentBinding
import com.stansdevhouse.newsapp.extensions.fragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NewsListFragment : Fragment() {

    //region companion
    companion object {
        fun newInstance() = NewsListFragment()
    }
    //endregion

    //region vars
    private val viewModel by viewModels<NewsListViewModel>()
    private var binding by fragmentBinding<NewsListFragmentBinding>()
    private val adapter by lazy { NewsListAdapter(viewModel::onCardClicked) }
    //endregion

    //region lifecycle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NewsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initObservers(view)

    }
    //endregion

    //region private helpers
    private fun initViews() {
        setWindowInsets()

        binding.newsList.adapter = adapter

        binding.refreshBtn.setOnClickListener {
            viewModel.refreshFabClicked()
        }

        binding.filterChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            chip?.let {
                viewModel.getNews(it.text, checkedId)
            }
        }
    }

    private fun setWindowInsets() {
        val layoutParams = binding.refreshBtn.layoutParams as CoordinatorLayout.LayoutParams
        val refreshMargin = binding.refreshBtn.marginBottom
        ViewCompat.setOnApplyWindowInsetsListener(binding.filterChipGroup) { v, insets ->
            v.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.refreshBtn) { v, insets ->
            layoutParams.bottomMargin = insets.systemWindowInsetBottom + refreshMargin
            v.layoutParams = layoutParams
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.newsList) { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun initObservers(view: View) {
        viewModel.liveEvent.observe(viewLifecycleOwner) {
            when (it) {
                Event.Loading -> binding.progress.visibility = View.VISIBLE
                is Event.Error -> {
                    binding.progress.visibility = View.GONE
                    showSnackBar(view, it.errorMessage)
                }
                Event.Success -> binding.progress.visibility = View.GONE
                is Event.OpenUrl -> {
                    if (it.url.isNotEmpty()) {
                        openUrl(it.url, view)
                    }
                }
            }
        }

        viewModel.newsTypeLiveData.distinctUntilChanged().observe(viewLifecycleOwner) { types ->
            types.forEachIndexed { index, type ->
                val chipDrawableStyle = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    R.style.Widget_MaterialComponents_Chip_Choice
                )
                binding.filterChipGroup.addView(
                    Chip(context).apply {
                        text = type
                        id = index + 1
                        setChipDrawable(chipDrawableStyle)
                    }
                )
            }
        }

        viewModel.newsLiveData.distinctUntilChanged().observe(viewLifecycleOwner) {
            binding.progress.visibility = View.GONE
            adapter.submitList(it)
        }

        viewModel.checkedFilterId.observe(viewLifecycleOwner) {
            binding.filterChipGroup.check(it)
        }
    }

    private fun openUrl(url: String, view: View) {
        try {
            //We should also be checking for other browser apps installed but meh ¯\_(ツ)_/¯
            val webPage = Uri.parse(url)
            val customTabIntent = CustomTabsIntent
                .Builder()
                .setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams
                        .Builder()
                        .setToolbarColor(resources.getColor(R.color.red_500, null))
                        .build()
                )
                .setStartAnimations(requireContext(), R.anim.slide_in_right, R.anim.slide_out_left)
                .setExitAnimations(
                    requireContext(),
                    android.R.anim.slide_in_left,
                    android.R.anim.slide_out_right
                )
                .build()
            customTabIntent.launchUrl(requireContext(), webPage)
        } catch (e: NullPointerException) {
            showSnackBar(view, e.message ?: "Null url")
        }
    }

    private fun showSnackBar(
        view: View,
        message: String
    ) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
    //endregion
}