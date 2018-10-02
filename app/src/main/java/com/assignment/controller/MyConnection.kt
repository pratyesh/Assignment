package com.assignment.controller

import android.util.Log
import com.assignment.model.ResponseDetails
import com.assignment.util.GsonUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.CacheControl
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class MyConnection constructor(listener: MyAsyncTaskListener) {

    private val TAG = MyConnection::class.java.simpleName
    private val mNetworkListener: MyAsyncTaskListener = listener
    private var call: Call? = null

    fun execute(baseRequest: String) {

        Observable.just<String>(baseRequest)
                .map<ResponseDetails> { request ->
                    var baseResponse: ResponseDetails? = ResponseDetails()
                    val client = OkHttpClient()

                    val builder: Request.Builder = Request.Builder()
                            .cacheControl(CacheControl.Builder().maxStale(30, TimeUnit.MINUTES)
                                    .build()).url(baseRequest)

                    val httpRequest = builder.build()
                    Log.d("$TAG : url", httpRequest.url().toString())
                    call = client.newCall(httpRequest)
                    try {
                        val httpResponse = call?.execute()
                        val status = httpResponse?.code()
                        val responseJson = httpResponse?.body()!!.string()
                        Log.d("$TAG : status", status.toString() + "")

                        baseResponse = GsonUtil.Companion.fromJson(responseJson, ResponseDetails::class.java, request)

                    } catch (e: IOException) {
                        Log.e("$TAG : ERR", e.message)
                    }
                    baseResponse
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(object : Observer<ResponseDetails> {

                    override fun onSubscribe(d: Disposable) {
                        Log.d("$TAG : Observer", "onSubscribe")
                    }

                    override fun onNext(responseDetails: ResponseDetails) {
                        mNetworkListener.onTaskCompleted(responseDetails)
                        Log.d("$TAG : Observer", "onNext")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("$TAG : Observer", "onError")
                        mNetworkListener.onFailure(e.localizedMessage)
                    }

                    override fun onComplete() {
                        Log.d("$TAG : Observer", "onComplete")
                    }
                })
    }

    fun cancelActive() {
        call?.let {
            it.cancel()
        }
    }
}