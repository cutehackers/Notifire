@file:Suppress("unused")

package app.darby.notifire.creator

import android.app.Activity
import androidx.core.app.Person
import app.darby.notifire.provider.BigPictureStyleBuilderProvider
import app.darby.notifire.provider.BigTextStyleBuilderProvider
import app.darby.notifire.provider.InboxStyleBuilderProvider
import app.darby.notifire.provider.MessagingStyleBuilderProvider
import app.darby.notifire.provider.NotifireBuilderProvider

fun Activity.notification(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: NotifireBuilderProvider,
) = notification(
    applicationContext,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsBigTextStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilderProvider,
) = notificationAsBigTextStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsBigPictureStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilderProvider,
) = notificationAsBigPictureStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block as NotifireBuilderProvider
).notify()

fun Activity.notificationAsInboxStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilderProvider,
) = notificationAsInboxStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block as NotifireBuilderProvider
).notify()

@Deprecated(message = "deprecated, use notificationAsMessagingStyle with Person",
    ReplaceWith("notificationAsMessagingStyle with Person argument")
)
fun Activity.notificationAsMessagingStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    userDisplayName: CharSequence,
    block: MessagingStyleBuilderProvider,
) = notificationAsMessagingStyle(
    applicationContext,
    smallIconResId,
    channelId,
    userDisplayName,
    block as NotifireBuilderProvider
).notify()

fun Activity.notificationAsMessagingStyle(
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
