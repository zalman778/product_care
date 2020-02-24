package com.hwx.productcare.notification

import javax.inject.Inject

class NotificationsDataRepositoryImpl @Inject constructor() : NotificationsDataRepository {

    private val firstPrefsMap = HashMap<String, String>()

    override fun saveFirstPrefsState(beforePrefsMap: HashMap<String, String>) {
        beforePrefsMap.forEach {
            firstPrefsMap[it.key] = it.value
        }
    }

    override fun getFirstPrefsState(): HashMap<String, String> {
        return firstPrefsMap
    }
}