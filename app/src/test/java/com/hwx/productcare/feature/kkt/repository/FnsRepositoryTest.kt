package com.hwx.productcare.feature.kkt.repository

import android.util.Base64
import com.hwx.productcare.NetworkError
import com.hwx.productcare.NetworkSuccess
import com.hwx.productcare.api.FnsApi
import com.hwx.productcare.api.requests.SignUpRequest
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(PowerMockRunner::class)
@PrepareForTest(Base64::class)
@PowerMockIgnore("javax.net.ssl.*")
class FnsRepositoryTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    private lateinit var fnsRepository: FnsRepository

    @Before
    fun setUp() {
        val array = ByteArray(20)
        PowerMockito.mockStatic(Base64::class.java)
        PowerMockito.`when`(
            Base64.encodeToString(
                array,
                Base64.DEFAULT
            )
        ).thenReturn("test")


        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val fnsApi = retrofit.create(FnsApi::class.java)

        fnsRepository = FnsRepository(fnsApi)
    }


    @Test
    fun test_getFnsCheckNetworkCall() {
        runBlocking {
            val json = """
            {"document":{"receipt":{"operationType":1,"properties":[],"nds18":4695,"senderAddress":"ofd@magnit.ru","fiscalDocumentNumber":1,"dateTime":"2020-01-26T16:15:00","stornoItems":[],"cashTotalSum":0,"receiptCode":3,"shiftNumber":130,"fiscalDriveNumber":"1","totalSum":81530,"modifiers":[],"requestNumber":345,"userInn":"1","ecashTotalSum":81530,"rawData":"data=","taxationType":1,"operator":"Товаровед магазина Кошкина","nds10":4852,"fiscalSign":3674755087,"message":[],"kktRegId":"0002988656028280","retailPlaceAddress":"197349, Санкт-Петербург г, Парашютная ул, дом № 27, корпус 1, лит.А","items":[{"sum":22612,"properties":[],"price":16900,"modifiers":[],"name":"КАПУСТА цветная 1кг","quantity":1.338},{"sum":4704,"properties":[],"price":1750,"modifiers":[],"name":"КАРТОФЕЛЬ фасованный цена за 1кг","quantity":2.688},{"sum":5799,"properties":[],"price":6290,"modifiers":[],"name":"БАНАНЫ 1кг","quantity":0.922},{"sum":10900,"properties":[],"price":10900,"modifiers":[],"name":"ПЕРВ. СВЕЖ. Шарики курин.ЦБ охл 0,6к","quantity":1},{"sum":5190,"properties":[],"price":5190,"modifiers":[],"name":"Хлеб Двинский нарез ржан-пшен в/с 35","quantity":1},{"sum":9960,"properties":[],"price":2490,"modifiers":[],"name":"ДАНИССИМО Твор десерт карам/орех/кр/","quantity":4},{"sum":7973,"properties":[],"price":46900,"modifiers":[],"name":"АККОНД Конфеты Отломи вафельн карам(","quantity":0.17},{"sum":14392,"properties":[],"price":28900,"modifiers":[],"name":"РОТ ФРОНТ Батончики(в)(Р-Ф) :5","quantity":0.498}],"user":"АО  Тандер"}}}
            """.trimIndent()

            mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(json))
            val headersMap = fnsRepository.buildHeadersMap("name", "password")

            val result = fnsRepository.getFnsCheckNetworkCall("url", headersMap, "fpd", "no")
            result as NetworkSuccess
            val receipt = result.data!!.document!!.receipt

            assertTrue(receipt.items.isNotEmpty())

        }

    }

    @Test
    fun test_signUpNetworkCall_fail() {
        runBlocking {
            mockWebServer.enqueue(MockResponse().setResponseCode(409))

            val request = SignUpRequest("test", "test", "test")
            val result = fnsRepository.signUpNetworkCall("url", request)
            result as NetworkError
            assertEquals("Пользователь с таким телефоном уже есть в системе.", result.message)

        }
    }
}