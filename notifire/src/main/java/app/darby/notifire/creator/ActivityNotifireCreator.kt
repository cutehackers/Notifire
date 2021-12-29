@file:Suppress("unused")

package app.darby.notifire.creator

import android.app.Activity
import app.darby.notifire.Notifire
import app.darby.notifire.provider.BigTextStyleBuilderProvider
import app.darby.notifire.provider.NotifireBuilderProvider

fun Activity.notification(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: NotifireBuilderProvider
): Notifire {
    return notification(
        applicationContext = applicationContext,
        smallIconResId = smallIconResId,
        channelId = channelId,
        block
    ).notify()
}

fun Activity.notificationAsBigTextStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilderProvider
): Notifire {
    return notificationAsBigTextStyle(
        applicationContext = applicationContext,
        smallIconResId = smallIconResId,
        channelId = channelId,
        block
    ).notify()
}
