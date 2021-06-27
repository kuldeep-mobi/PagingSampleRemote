package com.mobikasa.pagingsampleremote.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobikasa.pagingsampleremote.R

class LoaderAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderAdapter.FooterViewHolder>() {


    inner class FooterViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                mView.findViewById<TextView>(R.id.error_msg).text =
                    "There is something Worng Please Try again!!"
                mView.findViewById<ProgressBar>(R.id.progress_bar).visibility = View.GONE
                mView.findViewById<Button>(R.id.retry_button).visibility = View.VISIBLE
            } else {
                mView.findViewById<ProgressBar>(R.id.progress_bar).isVisible =
                    loadState is LoadState.Loading
                mView.findViewById<Button>(R.id.retry_button).isVisible =
                    loadState !is LoadState.Loading
            }
            mView.findViewById<Button>(R.id.retry_button).setOnClickListener {
                retry.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_footer, parent, false)
        return FooterViewHolder(view)
    }
}