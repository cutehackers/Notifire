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
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: Notifire.Builder.() -> Unit,
) = notification(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsBigTextStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilder.() -> Unit,
) = notificationAsBigTextStyle(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsBigPictureStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilder.() -> Unit,
) = notificationAsBigPictureStyle(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsInboxStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilder.() -> Unit,
) = notificationAsInboxStyle(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId,
    block
).notify()

fun Activity.notificationAsMessagingStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    user: Person,
    block: MessagingStyleBuilder.() -> Unit,
) = notificationAsMessagingStyle(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId,
    user,
    block
).notify()
