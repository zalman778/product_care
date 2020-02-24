package com.hwx.productcare.api.requests

data class SignUpRequest(
    var email: String,
    var name: String,
    var phone: String
)