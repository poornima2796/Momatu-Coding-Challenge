package com.poorni.momatugallerychallenge.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.poorni.momatugallerychallenge.R
import com.poorni.momatugallerychallenge.network.State
import kotlinx.android.synthetic.main.activity_photo_list.*


class GalleryActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_gallery)
//    }


    private lateinit var viewModel: GalleryViewModel
    private lateinit var newsListAdapter: PhotoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_list)

        viewModel = ViewModelProviders.of(this)
            .get(GalleryViewModel::class.java)
        initAdapter()
        initState()
    }

    private fun initAdapter() {
        newsListAdapter = PhotoListAdapter { viewModel.retry() }
        recycler_view.adapter = newsListAdapter
        viewModel.newsList.observe(this,
            Observer {
                newsListAdapter.submitList(it)
            })
    }

    private fun initState() {
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) VISIBLE else GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && state == State.ERROR) VISIBLE else GONE
            if (!viewModel.listIsEmpty()) {
                newsListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}