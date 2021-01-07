package com.stansdevhouse.newsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stansdevhouse.newsapp.databinding.NewsItemRowBinding
import com.stansdevhouse.newsapp.domain.model.News

class NewsListAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<News, NewsListViewHolder>(NewsListDiffCallback()) {

    private lateinit var binding: NewsItemRowBinding

    //region adapter implementations
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        binding = NewsItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsListViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    //endregion
}

class NewsListViewHolder(
    private val newsListItem: NewsItemRowBinding,
    private val onClick: (String) -> Unit
) : RecyclerView.ViewHolder(newsListItem.root) {

    companion object {
        private const val DEFAULT_URL = "https://www.cbc.ca"
    }

    fun bind(news: News) {
        newsListItem.title.text = news.title
        newsListItem.timeStamp.text = news.readablePublishedAt

        Glide.with(newsListItem.root.context)
            .load(news.typeAttributes?.imageLarge)
            .into(newsListItem.newsImage)

        newsListItem.newsCard.setOnClickListener {
            onClick.invoke(
                news.typeAttributes?.url ?: DEFAULT_URL
            )
        }
    }
}

class NewsListDiffCallback : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }

}