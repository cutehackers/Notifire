@file:Suppress("unused")

package app.darby.notifire.creator

import android.app.Service
import app.darby.notifire.Notifire
import app.darby.notifire.provider.NotifireBuilderProvider

fun Service.notification(
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
