package com.assignment.controller

import com.assignment.model.ResponseDetails

interface MyAsyncTaskListener {

    fun onTaskCompleted(responseDetails: ResponseDetails)
    fun onFailure(msg: String)
}