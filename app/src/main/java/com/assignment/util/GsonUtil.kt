package com.assignment.util

import android.util.Log
import com.google.gson.Gson

class GsonUtil private constructor() {

    companion object {
        private val TAG = GsonUtil::class.java.simpleName

        fun <T> fromJson(json: String, classOfT: Class<T>, requestString: String): T? {
            return fromJson(json, true, classOfT, requestString)
        }

        fun <T> fromJson(json: String, logException: Boolean, classOfT: Class<T>, requestString: String): T? {
            var retObj: T?
            val gson = Gson()

            try {
                retObj = gson.fromJson(json, classOfT)
            } catch (e: Exception) {
                if (logException) {
                    Log.e("$TAG : ERR", e.message)
                    Log.e("$TAG : REQ", requestString)
                    Log.e("$TAG : CLA", classOfT.name)
                    Log.e("$TAG : JSON", json)
                }
                retObj = null
            }
            return retObj
        }
    }
}