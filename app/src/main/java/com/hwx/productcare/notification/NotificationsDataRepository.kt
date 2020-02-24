package com.hwx.productcare.notification

import java.util.HashMap

interface NotificationsDataRepository {

    fun saveFirstPrefsState(beforePrefsMap: HashMap<String, String>)

    fun getFirstPrefsState(): HashMap<String, String>
}