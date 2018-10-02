package com.assignment.util

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttpDownloader
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import java.lang.ref.WeakReference

class ImageUtil private constructor(context: Context) {

    private val TAG: String = ImageUtil::class.java.simpleName
    private val PICASSO_DISK_CACHE_SIZE = 1024 * 1024 * 30; // Size in bytes (30 MB)

    val context: WeakReference<Context>
    var singletonBuilt: Boolean

    init {
        this.context = WeakReference<Context>(context)
        this.singletonBuilt = false;
    }

    fun getPicasso(): Picasso {
        var ctx: Context? = context.get()
        if (!singletonBuilt) {
            ctx?.let {
                val builder: Picasso.Builder = Picasso.Builder(it)
                setUpDownloader(builder, ctx)
                try {
                    Picasso.setSingletonInstance(builder.build())
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "Singleton instance already exists.");
                }
                singletonBuilt = true;
            }
        }
        return Picasso.with(ctx);
    }

    private fun setUpDownloader(builder: Picasso.Builder, ctx: Context) {
        // Use OkHttp as downloader
        val downloader = OkHttpDownloader(ctx)
        // Create memory cache
        val memoryCache = LruCache(PICASSO_DISK_CACHE_SIZE)
        builder.downloader(downloader).memoryCache(memoryCache)
    }

    fun loadImage(url: String, imageView: ImageView) {
        var ctx: Context? = context.get()
        ctx?.let {
            if (!TextUtils.isEmpty(url)) {
                val urlRequest: RequestCreator = getPicasso().load(url)
                try {
                    urlRequest.into(imageView)
                } catch (e: OutOfMemoryError) {
                    Log.e(TAG, "OutOfMemoryError", e);
                }
            }
        }
    }

    companion object {
        lateinit var instances: ImageUtil

        fun init(context: Context) {
            instances = ImageUtil(context)
        }

//        @JvmStatic
        fun getInstance(): ImageUtil {
            return instances;
        }
    }
}