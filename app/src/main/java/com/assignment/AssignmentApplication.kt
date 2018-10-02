package com.assignment

import android.app.Application
import com.assignment.util.ImageUtil

class AssignmentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ImageUtil.init(this)
    }
}