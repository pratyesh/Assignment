package com.assignment.presenter

import android.net.Uri
import com.assignment.model.ResponseDetails
import com.assignment.model.Row
import com.assignment.util.UrlUtils
import com.assignment.view.MainActivityView
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(Uri::class,
        MainActivityPresenter::class)
class MainActivityPresenterTest {

    internal var mMainActivityPresenter: MainActivityPresenter? = null
    internal var mockMainActivityView: MainActivityView? = null

    @Before
    fun setUp() {
        mockMainActivityView = PowerMockito.mock(MainActivityView::class.java)
        assertNotNull(mockMainActivityView)
        mMainActivityPresenter = MainActivityPresenter(mockMainActivityView!!)
        assertNotNull(mMainActivityPresenter)
    }

    @After
    fun tearDown() {
        mockMainActivityView = null
        mMainActivityPresenter = null
    }

    @SuppressWarnings("unchecked")
    @Test
    fun fetchAndLoadData() {
        PowerMockito.mockStatic(Uri::class.java)
        val spyMainActivityPresenter = PowerMockito.spy(mMainActivityPresenter)
        PowerMockito.doNothing().`when`(spyMainActivityPresenter)?.executeApiCall(Mockito.anyString())

        val mockUri = PowerMockito.mock(Uri::class.java)
        val mockUriBuilder = PowerMockito.mock(Uri.Builder::class.java)

        `when`(Uri.parse(UrlUtils.URL)).thenReturn(mockUri)
        `when`(mockUri.buildUpon()).thenReturn(mockUriBuilder)
        `when`(mockUriBuilder.clearQuery()).thenReturn(mockUriBuilder)
        `when`(mockUriBuilder.build()).thenReturn(mockUri)

        spyMainActivityPresenter?.fetchAndLoadData()
        verify(spyMainActivityPresenter?.mMainActivityView, times(1))?.showProgressBar()
        verify(spyMainActivityPresenter, times(1))?.executeApiCall(Mockito.anyString())
    }

    @Test
    fun onDestroy() {
        mMainActivityPresenter!!.onDestroy()
        assertNull(mMainActivityPresenter!!.mMyAsyncTask)
        assertNull(mMainActivityPresenter!!.mMainActivityView)
    }

    @Test
    fun onTaskCompleted() {
        val dataModelList = ArrayList<Row>()
        val imageData = Row("Image Title", "Http:://xyz.com", "Image Description")
        dataModelList.add(imageData)
        val responseDetails = ResponseDetails(dataModelList, "Test Actionbar")

        mMainActivityPresenter?.onTaskCompleted(responseDetails)
        verify(mMainActivityPresenter?.mMainActivityView, times(1))?.hideProgressBar()
        verify(mMainActivityPresenter?.mMainActivityView, times(1))?.setUiOnTaskCompleted(responseDetails)
    }
}