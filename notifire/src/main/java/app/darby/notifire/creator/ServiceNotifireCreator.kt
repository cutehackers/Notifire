@file:Suppress("unused")

package app.darby.notifire.creator

import android.app.Service
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import app.darby.notifire.Notifire
import app.darby.notifire.style.BigPictureStyleBuilder
import app.darby.notifire.style.BigTextStyleBuilder
import app.darby.notifire.style.InboxStyleBuilder
import app.darby.notifire.style.MessagingStyleBuilder

/**
 * Simple notification for Service
 */
fun Service.notification(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: Notifire.Builder.() -> Unit,
) = notificationBuilder(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId
).apply(block).notify()

/**
 * Big text style notification for Service
 */
fun Service.notificationAsBigTextStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilder.() -> Unit,
) = notificationBuilderAsBigTextStyle(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId,
).apply(block).notify()

/**
 * Big picture style notification for Service
 */
fun Service.notificationAsBigPictureStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilder.() -> Unit,
) = notificationBuilderAsBigPictureStyle(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId
).apply(block).notify()

/**
 * Inbox style notification for Service
 */
fun Service.notificationAsInboxStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilder.() -> Unit,
) = notificationBuilderAsInboxStyle(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId
).apply(block).notify()

/**
 * Messaging notification for Service
 */
fun Service.notificationAsMessagingStyle(
    notificationId: Int,
    user: Person,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: MessagingStyleBuilder.() -> Unit,
) = notificationBuilderAsMessagingStyle(
    applicationContext,
    notificationId,
    user,
    smallIconResId,
    channelId
).apply(block).notify()

/**
 * Messaging notification builder with existing MessagingStyle
 */
fun Service.notificationBuilderAsMessagingStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    allocator: () -> NotificationCompat.MessagingStyle,
    block: MessagingStyleBuilder.() -> Unit
) = notificationBuilderAsMessagingStyle(
    applicationContext,
    notificationId,
    smallIconResId,
    channelId,
    allocator
).apply(block).notify()

/**
 * Messaging notification builder from existing notifire object
 */
fun Service.extractMessagingStyleBuilderFromNotifire(
    notifire: Notifire,
    smallIconResId: Int? = null,
    channelId: String? = null
): MessagingStyleBuilder? = extractMessagingStyleBuilderFromNotifire(
    applicationContext, notifire, smallIconResId, channelId
)
