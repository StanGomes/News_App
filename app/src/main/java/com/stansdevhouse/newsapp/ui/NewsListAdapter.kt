package com.stansdevhouse.newsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stansdevhouse.newsapp.databinding.NewsItemRowBinding
import com.stansdevhouse.newsapp.domain.model.News

class NewsListAdapter : ListAdapter<News, NewsListAdapter.NewsListViewHolder>(NewsListDiffCallback()) {

    private lateinit var binding: NewsItemRowBinding

    inner class NewsListViewHolder(private val newsListItem: NewsItemRowBinding) : RecyclerView.ViewHolder(newsListItem.root) {
        fun bind(news: News) {
            newsListItem.title.text = news.title
            newsListItem.timeStamp.text = news.readablePublishedAt
            Glide.with(newsListItem.root.context)
                .load(news.typeAttributes?.imageLarge)
                .into(newsListItem.newsImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        binding = NewsItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        holder.bind(getItem(position))
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