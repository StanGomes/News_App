package com.stansdevhouse.newsapp

import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.stansdevhouse.newsapp.domain.NewsRepository
import com.stansdevhouse.newsapp.domain.RequestResult
import com.stansdevhouse.newsapp.domain.model.News
import com.stansdevhouse.newsapp.ui.Event
import com.stansdevhouse.newsapp.ui.NewsListViewModel
import com.stansdevhouse.newsapp.util.LiveEvent
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
class NewsListViewModelTest {

    @Rule
    @JvmField
    val coroutineTestRule = CoroutineTestRule()

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val newsRepository = mockk<NewsRepository>()
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var viewModel: NewsListViewModel


    private fun createMockNews() : List<News> =
        listOf(News(description = "News 1", id = 1, type = "video"),
            News(description = "News 2", id = 2, type = "video"),
            News(description = "News 3", id = 3, type = "story"),
            News(description = "News 4", id = 4, type = "video"),
            News(description = "News 5", id = 5, type = "video"),
            News(description = "News 6", id = 6, type = "story"),
            News(description = "News 7", id = 7, type = "content"),
            News(description = "News 8", id = 8, type = "story"))

    private val createMockNewsType = listOf("news","content","media")

    private val mockVideoTypeNews = createMockNews().filter {
        it.type == "video"
    }

    private val liveEventObserver = Observer<Event>{}

    @Before
    fun setup() {
//        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)

        coEvery {
            newsRepository.getAllNews()
        }.returns(flowOf(createMockNews()))

        coEvery {
            newsRepository.refresh()
        }.returns(flowOf(RequestResult.Success(createMockNews())))

        coEvery {
            newsRepository.getAllTypes()
        }.returns(flowOf(createMockNewsType))

        coEvery {
            newsRepository.getNewsByType("video")
        }.returns(flowOf(mockVideoTypeNews))

        viewModel = NewsListViewModel(newsRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun whenDbUpdatesThenUpdateNewsLiveData() {
        //GIVEN viewModel init

        //WHEN repository refreshed
        coVerify {
            newsRepository.refresh()
        }
        //THEN update liveData with data
        val news = viewModel.newsLiveData.getOrAwaitValue()
        assertEquals(createMockNews(), news)
    }

    @Test
    fun whenDbUpdatesThenUpdateNewsTypeLiveData() {
        //GIVEN viewModel init

        //WHEN repository refreshed
        coVerify {
            newsRepository.refresh()
        }
        //THEN update liveData with data
        val newsTypes = viewModel.newsTypeLiveData.getOrAwaitValue()
        assertEquals(createMockNewsType, newsTypes)
    }

    @Test
    fun whenFilterSelectedThenShouldFilteredNews() {
        //GIVEN

        //WHEN
        viewModel.getNews("video")

        //THEN
        val news = viewModel.newsLiveData.getOrAwaitValue()

        assertEquals(mockVideoTypeNews, news)
    }
}

@ExperimentalCoroutinesApi
class CoroutineTestRule constructor(private val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()): TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

}

/**
 * adds an observer, gets the LiveData value, and then cleans up the observer
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserve.invoke()

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

    } finally {
        this.removeObserver(observer)
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}