package com.poorni.momatugallerychallenge.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.poorni.momatugallerychallenge.model.Photo
import com.poorni.momatugallerychallenge.network.State
import com.poorni.momatugallerychallenge.network.State.ERROR
import com.poorni.momatugallerychallenge.network.State.LOADING

class PhotoListAdapter(private val retry: () -> Unit) :
    PagedListAdapter<Photo, RecyclerView.ViewHolder>(NewsDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) NewsViewHolder.create(parent) else ListFooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as NewsViewHolder).bind(getItem(position))
        else (holder as ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == LOADING || state == ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }
}