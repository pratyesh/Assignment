package com.assignment.adapter

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.assignment.R
import com.assignment.R.id.description
import com.assignment.model.Row
import com.assignment.util.ImageUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MyAdapter::class, LayoutInflater::class,
        Context::class, ViewGroup::class, ImageUtil::class)
class MyAdapterTest {

    private var URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&";
    var mMyAdapter: MyAdapter? = null
    lateinit var dataModelList: ArrayList<Row>

    @Before
    fun setUp() {
        dataModelList = ArrayList();
        val imageModel = Row(URL);
        dataModelList.add(imageModel);
        mMyAdapter = MyAdapter(dataModelList);
        assertNotNull(mMyAdapter);
    }

    @Test
    fun updateList() {
        val dataList = java.util.ArrayList<Row>()
        val imageModel = Row(URL);
        dataList.add(imageModel)
        val spyMyAdapter = PowerMockito.spy(mMyAdapter)

        PowerMockito.doNothing().`when`(spyMyAdapter)?.refreshOnItemUI(Mockito.anyInt(), Mockito.anyInt())
        spyMyAdapter?.updateList(dataList)
        assertEquals(2, spyMyAdapter?.mDataModelList?.size)
        Mockito.verify(spyMyAdapter, Mockito.times(1))?.refreshOnItemUI(Mockito.anyInt(), Mockito.anyInt())
    }

    @Test
    fun getItemViewType() {
        assertEquals(R.layout.item, mMyAdapter?.getItemViewType(1))
    }

    @Test
    fun onCreateViewHolder() {
        PowerMockito.mockStatic(LayoutInflater::class.java)
        val context = PowerMockito.mock(Context::class.java)
        val parent = PowerMockito.mock(ViewGroup::class.java)
        PowerMockito.doReturn(context).`when`(parent).getContext()

        val mockLayoutInflater = PowerMockito.mock(LayoutInflater::class.java)
        PowerMockito.`when`(LayoutInflater.from(context)).thenReturn(mockLayoutInflater)

        val mockView = PowerMockito.mock(View::class.java)
        PowerMockito.doReturn(mockView).`when`(mockLayoutInflater).inflate(R.layout.item, parent, false)

        val image = PowerMockito.mock(AppCompatImageView::class.java)
        PowerMockito.doReturn(image).`when`(mockView).findViewById<AppCompatImageView>(R.id.image)

        val titleText = PowerMockito.mock(AppCompatTextView::class.java)
        PowerMockito.doReturn(titleText).`when`(mockView).findViewById<AppCompatTextView>(R.id.titleText)

        val description = PowerMockito.mock(AppCompatTextView::class.java)
        PowerMockito.doReturn(description).`when`(mockView).findViewById<AppCompatTextView>(R.id.description)

        mMyAdapter?.onCreateViewHolder(parent, R.layout.item)
        PowerMockito.verifyStatic(Mockito.times(1))
        LayoutInflater.from(context)

        Mockito.verify(mockLayoutInflater, Mockito.times(1)).inflate(R.layout.item, parent, false)
    }

    @Test
    fun getItemCount() {
        assertEquals(dataModelList.size, mMyAdapter?.getItemCount())
    }

    @Test
    fun onBindViewHolder() {
        val spyMyAdapter = PowerMockito.spy(mMyAdapter)

        val mockBaseViewHolder = PowerMockito.mock(MyAdapter.BaseViewHolder::class.java)

        val imageView = PowerMockito.mock(AppCompatImageView::class.java)
        PowerMockito.doReturn(imageView).`when`(mockBaseViewHolder).image

        val titleText = PowerMockito.mock(AppCompatTextView::class.java)
        PowerMockito.doReturn(titleText).`when`(mockBaseViewHolder).titleText

        val description = PowerMockito.mock(AppCompatTextView::class.java)
        PowerMockito.doReturn(description).`when`(mockBaseViewHolder).description

        val imageData = Row(URL)
        PowerMockito.doNothing().`when`(spyMyAdapter)?.setImage(imageData, mockBaseViewHolder)
        PowerMockito.doReturn(imageData).`when`(spyMyAdapter)?.getItem(0)

        spyMyAdapter?.onBindViewHolder(mockBaseViewHolder, 0)

        Mockito.verify(spyMyAdapter, Mockito.times(1))?.getItem(0)
        Mockito.verify(spyMyAdapter, Mockito.times(1))?.setImage(imageData, mockBaseViewHolder)
    }
}