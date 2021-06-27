package com.mobikasa.pagingsampleremote.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobikasa.pagingsampleremote.R
import com.mobikasa.pagingsampleremote.models.Results

class MyAdapter : PagingDataAdapter<Results, MyAdapter.ViewHolder>(DIFF_COMPARE) {

    companion object {
        private val DIFF_COMPARE = object : DiffUtil.ItemCallback<Results>() {
            override fun areItemsTheSame(oldItem: Results, newItem: Results): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Results, newItem: Results): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        fun bind(mData: Results?) {
            mView.findViewById<TextView>(R.id.nameText).text = mData?.title
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_adapter, parent, false)
        )
    }
}