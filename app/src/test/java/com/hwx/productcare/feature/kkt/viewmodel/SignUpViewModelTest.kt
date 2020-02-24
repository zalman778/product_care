package com.hwx.productcare.feature.kkt.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.hwx.productcare.feature.kkt.domain.SignUpInteractor
import com.hwx.productcare.feature.kkt.repository.FnsRepository
import com.hwx.productcare.provider.ConnectionStateProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SignUpViewModelTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var fnsRepository: FnsRepository

    @MockK
    private lateinit var signUpInteractor: SignUpInteractor

    @MockK
    private lateinit var connectionStateProvider: ConnectionStateProvider

    lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        viewModel = SignUpViewModel(fnsRepository, signUpInteractor, connectionStateProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testIsFormValid() {
        val valid = viewModel.isFormValid("test", "test@test.test", "+7999123")
        assertTrue(valid)
    }

    @Test
    fun testIdValidMediator() {
        viewModel.mlvName.postValue("test")
        viewModel.mlvEmail.postValue("test@test.test")
        viewModel.mlvPhone.postValue("+7999123")

        val validObserver = Observer<Boolean> {
            assertTrue(it)
        }

        viewModel.mlvSubmitBtnEnabled.observeForever(validObserver)
        viewModel.mlvSubmitBtnEnabled.removeObserver(validObserver)

        viewModel.mlvEmail.postValue("invalid email")
        val invalidObserver = Observer<Boolean> {
            assertFalse(it)
        }

        viewModel.mlvSubmitBtnEnabled.observeForever(invalidObserver)
        viewModel.mlvSubmitBtnEnabled.removeObserver(invalidObserver)

    }
}