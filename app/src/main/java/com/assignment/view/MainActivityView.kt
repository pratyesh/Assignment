package com.assignment.view

import com.assignment.model.ResponseDetails

interface MainActivityView {

    fun onErrorResponse(msg: String)

    fun setUiOnTaskCompleted(responseDetails: ResponseDetails)

    fun showProgressBar()

    fun hideProgressBar()

    fun networkFailed()
}