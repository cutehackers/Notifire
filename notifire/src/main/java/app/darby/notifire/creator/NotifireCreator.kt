@file:Suppress("unused")

package app.darby.notifire.creator

import android.content.Context
import app.darby.notifire.Notifire
import app.darby.notifire.exception.NotInitializedYetException
import app.darby.notifire.provider.BigPictureStyleBuilderProvider
import app.darby.notifire.provider.BigTextStyleBuilderProvider
import app.darby.notifire.provider.NotifireBuilderProvider

@Throws(NotInitializedYetException::class)
fun notification(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: NotifireBuilderProvider,
): Notifire.Builder {
    if (!Notifire.isConfigurationInitialized) {
        throw NotInitializedYetException("Notify.configurations property is not initialized yet.")
    }
    val config = Notifire.configurations

    return Notifire.builder(applicationContext, channelId ?: config.channelId)
        .smallIcon(smallIconResId ?: config.smallIconResId)
        .apply {
            block(this)
        }
}

fun notificationAsBigTextStyle(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilderProvider,
) = notification(
    applicationContext,
    smallIconResId,
    channelId,
    block as NotifireBuilderProvider
).asBigTextStyle()

fun notificationAsBigPictureStyle(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilderProvider,
) = notification(
    applicationContext,
    smallIconResId,
    channelId,
    block as NotifireBuilderProvider
).asBigPictureStyle()
