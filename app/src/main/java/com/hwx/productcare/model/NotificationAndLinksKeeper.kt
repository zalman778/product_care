package com.hwx.productcare.model

import java.util.HashSet

data class NotificationAndLinksKeeper(
    var notification: Notification? = null
    , var links: HashSet<Long> = HashSet()
    , var isNew: Boolean = true
)