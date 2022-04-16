@file:Suppress("unused")

package app.darby.notifire.creator

import android.app.Activity
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import app.darby.notifire.Notifire
import app.darby.notifire.style.BigPictureStyleBuilder
import app.darby.notifire.style.BigTextStyleBuilder
import app.darby.notifire.style.InboxStyleBuilder
import app.darby.notifire.style.MessagingStyleBuilder

/**
 * Simple notification for Activity
 */
fun Activity.notification(
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null,
    block: Notifire.Builder.() -> Unit,
) = notificationBuilder(
    applicationContext,
    notificationId,
    channelId,
    smallIconResId
).apply(block).notify()

/**
 * Big text style notification for Activity
 */
fun Activity.notificationAsBigTextStyle(
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null,
    block: BigTextStyleBuilder.() -> Unit,
) = notificationBuilderAsBigTextStyle(
    applicationContext,
    notificationId,
    channelId,
    smallIconResId
).apply(block).notify()

/**
 * Big picture style notification for Activity
 */
fun Activity.notificationAsBigPictureStyle(
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null,
    block: BigPictureStyleBuilder.() -> Unit,
) = notificationBuilderAsBigPictureStyle(
    applicationContext,
    notificationId,
    channelId,
    smallIconResId
).apply(block).notify()

/**
 * Inbox style notification for Activity
 */
fun Activity.notificationAsInboxStyle(
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null,
    block: InboxStyleBuilder.() -> Unit,
) = notificationBuilderAsInboxStyle(
    applicationContext,
    notificationId,
    channelId,
    smallIconResId
).apply(block).notify()

/**
 * Messaging notification for Activity
 */
fun Activity.notificationAsMessagingStyle(
    notificationId: Int,
    user: Person,
    channelId: String? = null,
    smallIconResId: Int? = null,
    block: MessagingStyleBuilder.() -> Unit,
) = notificationBuilderAsMessagingStyle(
    applicationContext,
    notificationId,
    user,
    channelId,
    smallIconResId
).apply(block).notify()

/**
 * Messaging notification builder with existing MessagingStyle
 */
fun Activity.notificationAsMessagingStyle(
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null,
    allocator: () -> NotificationCompat.MessagingStyle,
    block: MessagingStyleBuilder.() -> Unit
) = notificationBuilderAsMessagingStyle(
    applicationContext,
    notificationId,
    channelId,
    smallIconResId,
    allocator
).apply(block).notify()

/**
 * Messaging notification builder from existing notifire object
 */
fun Activity.extractMessagingStyleBuilderFromNotifire(
    notifire: Notifire,
    channelId: String? = null,
    smallIconResId: Int? = null
): MessagingStyleBuilder? = extractMessagingStyleBuilderFromNotifire(
    applicationContext, notifire, channelId, smallIconResId
)
