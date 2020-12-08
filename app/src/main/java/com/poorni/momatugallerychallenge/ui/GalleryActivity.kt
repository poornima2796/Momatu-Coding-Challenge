package com.poorni.momatugallerychallenge.ui

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.poorni.momatugallerychallenge.R
import com.poorni.momatugallerychallenge.network.State
import kotlinx.android.synthetic.main.activity_photo_list.*


class GalleryActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_gallery)
//    }


    private lateinit var viewModel: GalleryViewModel
    private lateinit var dataListAdapter: PhotoListAdapter
    var isRotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_list)

        viewModel = ViewModelProviders.of(this)
            .get(GalleryViewModel::class.java)
        initAdapter()
        initState()


        floatingActionButton.setOnClickListener {
            fabRotate(it)

        }
        fab_one.setOnClickListener {
            changeGrid(1)
            fabRotate(it)
        }
        fab_two.setOnClickListener {
            changeGrid(2)
            fabRotate(it)
        }
        fab_three.setOnClickListener {
            changeGrid(3)
            fabRotate(it)

        }
    }

    private fun fabRotate(it: View) {
        isRotate = rotateFab(it, !isRotate)
        if (isRotate) {
            showIn(fab_one)
            showIn(fab_two)
            showIn(fab_three)

        } else {
            showOut(fab_one)
            showOut(fab_two)
            showOut(fab_three)
        }
    }

    private fun changeGrid(size: Int) {
        recycler_view.layoutManager =
            StaggeredGridLayoutManager(size, StaggeredGridLayoutManager.VERTICAL)
    }

    private fun initAdapter() {
        dataListAdapter = PhotoListAdapter { viewModel.retry() }
        recycler_view.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recycler_view.adapter = dataListAdapter
        viewModel.dataList.observe(this,
            Observer {
                dataListAdapter.submitList(it)
            })
    }

    private fun initState() {
        init(fab_one)
        init(fab_two)
        init(fab_three)
        txt_error.setOnClickListener { viewModel.retry() }
        viewModel.getState().observe(this, Observer { state ->
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && state == State.LOADING) VISIBLE else GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && state == State.ERROR) VISIBLE else GONE
            if (!viewModel.listIsEmpty()) {
                dataListAdapter.setState(state ?: State.DONE)
            }
        })
    }
}