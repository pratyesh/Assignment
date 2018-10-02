package com.assignment

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.assignment.adapter.MyAdapter
import com.assignment.model.ResponseDetails
import com.assignment.model.Row
import com.assignment.presenter.MainActivityPresenter
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mockito.*
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(MainActivityPresenter::class,
        MainActivity::class, MyAdapter::class)

class MainActivityTest {

    private val URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json"
    lateinit var spyMainActivity: MainActivity

    @Before
    fun setUp() {
        spyMainActivity = PowerMockito.spy(MainActivity())
        assertNotNull(spyMainActivity)
        spyMainActivity.mMainActivityPresenter = PowerMockito.mock(MainActivityPresenter::class.java)
        spyMainActivity.mRecyclerView = PowerMockito.mock(RecyclerView::class.java)
        spyMainActivity.mMyAdapter = PowerMockito.mock(MyAdapter::class.java)
        spyMainActivity.progressBar = PowerMockito.mock(ProgressBar::class.java)
    }

    @After
    fun tearDown() {
        spyMainActivity.mMainActivityPresenter = null
        spyMainActivity.mRecyclerView = null
        spyMainActivity.mMyAdapter = null
        spyMainActivity.progressBar = null
    }

    @Test
    fun showProgressBar() {
        spyMainActivity.showProgressBar()
        verify(spyMainActivity.progressBar, times(1))?.visibility = View.VISIBLE
    }

    @Test
    fun hideProgressBar() {
        spyMainActivity.hideProgressBar()
        verify(spyMainActivity.progressBar, times(1))?.visibility = View.GONE
    }

    @Test
    fun setUiOnTaskCompleted() {
        val dataModelList = ArrayList<Row>()
        val imageData = Row("Image Title", "Http:://xyz.com", "Image Description")
        dataModelList.add(imageData)
        val responseDetails = ResponseDetails(dataModelList, "Test Actionbar")
        PowerMockito.doReturn(false).`when`(spyMainActivity).isFinishing
        PowerMockito.doNothing().`when`(spyMainActivity).setScreenTitle(Matchers.anyString())
        spyMainActivity.setUiOnTaskCompleted(responseDetails)
        Assert.assertNotNull(spyMainActivity.mRecyclerView)
        Assert.assertNotNull(spyMainActivity.mMyAdapter)
    }

    @Test
    fun networkFailed() {
        val msg = "Internet Connection Failed \n Try Again later";
        PowerMockito.doNothing().`when`(spyMainActivity).showToast(spyMainActivity, msg)
        PowerMockito.doNothing().`when`(spyMainActivity).hideProgressBar()
        spyMainActivity.networkFailed()
        verify(spyMainActivity, times(1)).hideProgressBar()
        verify(spyMainActivity, times(1)).showToast(spyMainActivity, msg)
    }

}