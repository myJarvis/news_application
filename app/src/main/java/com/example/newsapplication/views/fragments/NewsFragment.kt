package com.example.newsapplication.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapplication.R
import com.example.newsapplication.adapter.NewsHeadlinesAdapter
import com.example.newsapplication.app.NewsApp
import com.example.newsapplication.models.Articles
import com.example.newsapplication.viewmodels.NewsViewModel
import com.example.newsapplication.viewmodels.NewsViewModelViewFactory
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject

class NewsFragment : Fragment(R.layout.fragment_news) {

    @Inject
    lateinit var newsViewModelViewFactory: NewsViewModelViewFactory

    private val newsViewModel by lazy {
        ViewModelProviders.of(this, newsViewModelViewFactory).get(NewsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        NewsApp.appComponent.inject(this)
        observeViewModelChanges()
        newsViewModel.getNewsHeadlines(activity!!)
    }

    private fun observeViewModelChanges() {
        newsViewModel.newsHeadlines.observe(this, androidx.lifecycle.Observer {
            it?.also {
                setNews(it)
            }
        })
    }

    private fun setNews(data: List<Articles>) {
        with(shimmerViewContainer) {
            stopShimmerAnimation()
            visibility = View.GONE
        }

        activity?.also {

            rvNewsHeadlines.apply {
                visibility = View.VISIBLE
                adapter = NewsHeadlinesAdapter(it, data, true)
                layoutManager = LinearLayoutManager(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shimmerViewContainer.startShimmerAnimation()
    }

    override fun onPause() {
        shimmerViewContainer.stopShimmerAnimation()
        super.onPause()
    }
}