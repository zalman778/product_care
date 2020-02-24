package com.hwx.productcare.feature.kkt.repository


import com.hwx.productcare.NetworkError
import com.hwx.productcare.NetworkResult
import com.hwx.productcare.NetworkSuccess
import com.hwx.productcare.api.FnsApi
import com.hwx.productcare.api.entities.FnsCheck
import com.hwx.productcare.api.requests.SignUpRequest
import com.hwx.productcare.exception.NoResponseException
import com.hwx.productcare.model.Receipt
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class FnsRepository @Inject constructor(
    private val fnsApi: FnsApi
) {
    fun getFnsCheck(
        readedReceipt: Receipt,
        kktName: String,
        kktPassword: String
    ) = flow {
        val headersMap = buildHeadersMap(kktName, kktPassword)//HashMap<String, String>()
        val url =
            "/v1/inns/*/kkts/*/fss/${readedReceipt.fiscalNumber}/tickets/${readedReceipt.fiscalDocument}"

        val result = getFnsCheckNetworkCall(url, headersMap, readedReceipt.fiscalMark.toString(), "no")
        emit(result)

    }


    internal suspend fun getFnsCheckNetworkCall(
        url: String,
        headersMap: HashMap<String, String>,
        fpd: String,
        sendToEmail: String
    ): NetworkResult<FnsCheck> {
        return fnsApi.getCheck(url, headersMap, fpd, sendToEmail).run {
            if (isSuccessful && body() != null) {
                NetworkSuccess(body()!!)
            } else {
                NetworkError(
                    NoResponseException(errorBody().toString())
                )
            }
        }
    }


    fun signUp(
        email: String,
        name: String,
        phone: String
    ) = flow {
        val url = "/v1/mobile/users/signup"
        val request = SignUpRequest(email, name, phone)
        val result = signUpNetworkCall(url, request)
        emit(result)
    }

    /*
        409 Ошибка - уже есть такой юзер
        204 - no content?!- ok?
    */
    internal suspend fun signUpNetworkCall(
        url: String,
        request: SignUpRequest
    ): NetworkResult<Nothing> {
        return fnsApi.signUp(url, request).run {
            if (code() == 204) {
                NetworkSuccess(null)
            } else {
                var textValue = errorBody().toString()
                if (code() == 409)
                    textValue = "Пользователь с таким телефоном уже есть в системе."
                NetworkError(
                    NoResponseException(textValue)
                )
            }
        }
    }


    internal fun buildHeadersMap(kktName: String, kktPassword: String): HashMap<String, String> {
        val headersMap = HashMap<String, String>()
        val authPreBase64 = "$kktName:$kktPassword"
        val authAfterBase64 = android.util.Base64.encodeToString(
            authPreBase64.toByteArray(),
            android.util.Base64.NO_WRAP
        )
        headersMap["Authorization"] = "Basic $authAfterBase64"
        headersMap["Device-OS"] = "Adnroid 6.0"
        headersMap["Device-Id"] = ""
        headersMap["Version"] = "2"
        headersMap["Accept-Encoding"] = "gzip"
        headersMap["ClientVersion"] = "1.4.4.4"
        return headersMap
    }

}