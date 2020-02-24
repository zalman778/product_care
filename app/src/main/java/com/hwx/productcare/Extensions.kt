package com.hwx.productcare

import com.hwx.productcare.model.NotificationAndLinksKeeper
import org.joda.time.DateTime
import org.joda.time.Interval

fun Interval.toLocalDates(days: Int = 1): Sequence<DateTime> = generateSequence(start) { d ->
    d.plusDays(days).takeIf { it < end }
}

typealias NotificationsMap = HashMap<DateTime, NotificationAndLinksKeeper>
