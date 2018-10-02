package com.assignment

import android.content.Context
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.assignment.adapter.MyAdapter
import com.assignment.model.ResponseDetails
import com.assignment.presenter.MainActivityPresenter
import com.assignment.view.MainActivityView

class MainActivity : AppCompatActivity(), MainActivityView, SwipeRefreshLayout.OnRefreshListener {

    var mRecyclerView: RecyclerView? = null

    var mMyAdapter: MyAdapter? = null
    var progressBar: ProgressBar? = null
    var swipeRefresh: SwipeRefreshLayout? = null

    var mMainActivityPresenter: MainActivityPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        mMainActivityPresenter = MainActivityPresenter(this)

        mRecyclerView = findViewById(R.id.list_item)
        progressBar = findViewById(R.id.progressBar)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        swipeRefresh?.setOnRefreshListener(this)

        val mLayoutManager = LinearLayoutManager(this)
        mRecyclerView?.layoutManager = mLayoutManager

        val mDividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        mRecyclerView?.addItemDecoration(mDividerItemDecoration)

        mMainActivityPresenter?.fetchAndLoadData()
    }

    override fun onDestroy() {
        mMainActivityPresenter?.onDestroy()
        mMainActivityPresenter = null
        mRecyclerView = null
        mMyAdapter = null
        hideProgressBar()
        progressBar = null
        super.onDestroy()
    }

    override fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar?.visibility = View.GONE
    }


    fun setScreenTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setUiOnTaskCompleted(responseDetails: ResponseDetails) {
        if (isFinishing) {
            return
        }

        swipeRefresh?.isRefreshing = false
        setScreenTitle(responseDetails.title)

        responseDetails.rows?.let {
            mMyAdapter = MyAdapter(it)
        }
        mRecyclerView?.adapter = mMyAdapter
    }

    override fun networkFailed() {
        swipeRefresh?.isRefreshing = false
        hideProgressBar()
        showToast(this, "Internet Connection Failed \n Try Again later")
    }

    override fun onErrorResponse(msg: String) {
        swipeRefresh?.isRefreshing = false
        hideProgressBar()
        showToast(this, msg)
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onRefresh() {
        mMainActivityPresenter?.fetchAndLoadData()
    }

}
