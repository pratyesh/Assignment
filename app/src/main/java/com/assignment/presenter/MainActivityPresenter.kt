package com.assignment.presenter

import com.assignment.controller.MyAsyncTaskListener
import com.assignment.controller.MyConnection
import com.assignment.model.ResponseDetails
import com.assignment.util.UrlUtils
import com.assignment.view.MainActivityView

class MainActivityPresenter constructor(mainActivityView: MainActivityView) : MyAsyncTaskListener {
    internal var mMainActivityView: MainActivityView? = null
    internal var mMyAsyncTask: MyConnection? = null

    init {
        mMainActivityView = mainActivityView
    }

    fun fetchAndLoadData() {
        mMainActivityView?.showProgressBar()
        val completeUrl: String = UrlUtils.getUrl()
        mMyAsyncTask = MyConnection(this);
        executeApiCall(completeUrl)
    }

    fun executeApiCall(completeUrl: String) {
        mMyAsyncTask?.execute(completeUrl)
    }

    fun onDestroy() {
        mMyAsyncTask?.cancelActive()
        mMyAsyncTask = null
        mMainActivityView = null
    }

    override fun onTaskCompleted(responseDetails: ResponseDetails) {
        mMainActivityView?.hideProgressBar()
        responseDetails.rows?.let {
            mMainActivityView?.setUiOnTaskCompleted(responseDetails)
        }
    }

    override fun onFailure(msg: String) {
        mMainActivityView?.onErrorResponse(msg)
    }

}