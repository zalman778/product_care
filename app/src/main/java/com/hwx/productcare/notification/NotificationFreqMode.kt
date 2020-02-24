package com.hwx.productcare.notification

enum class NotificationFreqMode(
    val value: Int
) {
    RARE(1),
    DEFAULT(2),
    FREQUENT(3);

    companion object {
        fun getByValue(input: String): NotificationFreqMode {
            val res = values()
            for (fm in res) {
                if (fm.value.toString() == input)
                    return fm
            }
            return DEFAULT
        }
    }
}