@file:Suppress("unused")

package app.darby.notifire.creator

import android.app.Activity
import androidx.core.app.Person
import app.darby.notifire.Notifire
import app.darby.notifire.style.BigPictureStyleBuilder
import app.darby.notifire.style.BigTextStyleBuilder
import app.darby.notifire.style.InboxStyleBuilder
import app.darby.notifire.style.MessagingStyleBuilder

fun Activity.notification(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: Notifire.Builder.() -> Unit,
) = notification(
    applicationContext,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsBigTextStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilder.() -> Unit,
) = notificationAsBigTextStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsBigPictureStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilder.() -> Unit,
) = notificationAsBigPictureStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsInboxStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilder.() -> Unit,
) = notificationAsInboxStyle(
    applicationContext,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsMessagingStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    user: Person,
    block: MessagingStyleBuilder.() -> Unit,
) = notificationAsMessagingStyle(
    applicationContext,
    smallIconResId,
    channelId,
    user,
    block
).notify()
