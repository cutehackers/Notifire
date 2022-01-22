@file:Suppress("unused")

package app.darby.notifire.creator

import android.app.Service
import androidx.core.app.Person
import app.darby.notifire.Notifire
import app.darby.notifire.provider.BigPictureStyleBuilderProvider
import app.darby.notifire.provider.BigTextStyleBuilderProvider
import app.darby.notifire.provider.InboxStyleBuilderProvider
import app.darby.notifire.provider.MessagingStyleBuilderProvider
import app.darby.notifire.provider.NotifireBuilderProvider

fun Service.notification(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: NotifireBuilderProvider,
): Notifire {
    return notification(
        applicationContext = applicationContext,
        smallIconResId = smallIconResId,
        channelId = channelId,
        block
    ).notify()
}

fun Service.notificationAsBigTextStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilderProvider,
) = notificationAsBigTextStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block
).notify()

fun Service.notificationAsBigPictureStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilderProvider,
) = notificationAsBigPictureStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block as NotifireBuilderProvider
).notify()

fun Service.notificationAsInboxStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilderProvider,
) = notificationAsInboxStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block as NotifireBuilderProvider
).notify()

fun Service.notificationAsMessagingStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    user: Person,
    block: MessagingStyleBuilderProvider,
) = notificationAsMessagingStyle(
    applicationContext,
    smallIconResId,
    channelId,
    user,
    block as NotifireBuilderProvider
).notify()