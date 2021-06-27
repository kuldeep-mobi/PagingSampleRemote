package com.mobikasa.pagingsampleremote

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.mobikasa.pagingsampleremote.api.RetrofitManager
import com.mobikasa.pagingsampleremote.db.MyDatabase
import com.mobikasa.pagingsampleremote.repositories.DataRepository
import com.mobikasa.pagingsampleremote.view.LoaderAdapter
import com.mobikasa.pagingsampleremote.view.MyAdapter
import com.mobikasa.pagingsampleremote.viewmodels.MyDataViewModel
import com.mobikasa.pagingsampleremote.viewmodels.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {
    private val myAdapter: MyAdapter by lazy {
        MyAdapter()
    }
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mBtn: Button
    private lateinit var mLinearErrorLayout: LinearLayout
    private lateinit var mErrorText: TextView

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mProgressBar = findViewById(R.id.mProgress)
        mRecyclerView = findViewById(R.id.mRecycler)
        mBtn = findViewById(R.id.btnRetry)
        mLinearErrorLayout = findViewById(R.id.mLinear)
        mErrorText = findViewById(R.id.errorMessage);
        mRecyclerView.adapter = myAdapter
        val db = MyDatabase(this)
        val api = RetrofitManager.getService()
        val repository = DataRepository(db, api)
        val viewModel =
            ViewModelProvider(this, ViewModelFactory(repository))[MyDataViewModel::class.java]
        initAdapter()
        loadData(viewModel)
    }

    @ExperimentalPagingApi
    private fun loadData(viewModel: MyDataViewModel) {
        lifecycleScope.launchWhenCreated {
            viewModel.getResults().collectLatest {
                myAdapter.submitData(it)
            }
        }

    }

    private fun initAdapter() {
        mRecyclerView.adapter = myAdapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter { myAdapter.retry() },
            footer = LoaderAdapter { myAdapter.retry() }
        )

        myAdapter.addLoadStateListener { loadState ->
            mRecyclerView.isVisible = loadState.mediator?.refresh is LoadState.NotLoading
            mProgressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            mLinearErrorLayout.isVisible = loadState.mediator?.refresh is LoadState.Error
            mBtn.setOnClickListener {
                myAdapter.retry()
            }
        }
    }
}