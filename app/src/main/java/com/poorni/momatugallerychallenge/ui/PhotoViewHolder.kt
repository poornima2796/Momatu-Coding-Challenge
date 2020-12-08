package com.poorni.momatugallerychallenge.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.poorni.momatugallerychallenge.R
import com.poorni.momatugallerychallenge.model.Photo
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo_item.view.*

class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(news: Photo?) {
        if (news != null) {
            itemView.txt_name.text = news.author
            Picasso.get().load(news.image)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .resize(200, 200)
                .placeholder(R.drawable.progress_animation)
                .into(itemView.img)

        }
    }

    companion object {
        fun create(parent: ViewGroup): NewsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_item, parent, false)
            return NewsViewHolder(view)
        }
    }
}