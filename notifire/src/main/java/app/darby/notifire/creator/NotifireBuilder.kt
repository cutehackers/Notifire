@file:Suppress("unused")

package app.darby.notifire.creator

import android.app.PendingIntent
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import app.darby.notifire.Notifire
import app.darby.notifire.exception.NotInitializedYetException
import app.darby.notifire.style.BigPictureStyleBuilder
import app.darby.notifire.style.BigTextStyleBuilder
import app.darby.notifire.style.InboxStyleBuilder
import app.darby.notifire.style.MessagingStyleBuilder

/**
 * Simple notification builder
 */
@Throws(NotInitializedYetException::class)
fun notificationBuilder(
    applicationContext: Context,
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null
): Notifire.Builder {
    return newNotifireBuilder(applicationContext, channelId, smallIconResId)
        .id(notificationId)
}

/**
 * Big text style notification builder
 */
fun notificationBuilderAsBigTextStyle(
    applicationContext: Context,
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null
): BigTextStyleBuilder {
    return newNotifireBuilder(applicationContext, channelId, smallIconResId)
        .id(notificationId)
        .asBigTextStyle()
}

/**
 * Big picture style notification builder
 */
fun notificationBuilderAsBigPictureStyle(
    applicationContext: Context,
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null
): BigPictureStyleBuilder {
    return newNotifireBuilder(applicationContext, channelId, smallIconResId)
        .id(notificationId)
        .asBigPictureStyle()
}

/**
 * Inbox style notification builder
 */
fun notificationBuilderAsInboxStyle(
    applicationContext: Context,
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null
): InboxStyleBuilder {
    return newNotifireBuilder(applicationContext, channelId, smallIconResId)
        .id(notificationId)
        .asInboxStyle()
}

/**
 * Messaging notification builder
 */
fun notificationBuilderAsMessagingStyle(
    applicationContext: Context,
    notificationId: Int,
    user: Person,
    channelId: String? = null,
    smallIconResId: Int? = null
): MessagingStyleBuilder {
    return newNotifireBuilder(applicationContext, channelId, smallIconResId)
        .id(notificationId)
        .asMessagingStyle(user)
}

/**
 * Messaging notification builder with existing MessagingStyle
 */
fun notificationBuilderAsMessagingStyle(
    applicationContext: Context,
    notificationId: Int,
    channelId: String? = null,
    smallIconResId: Int? = null,
    allocator: () -> NotificationCompat.MessagingStyle,
): MessagingStyleBuilder {
    return newNotifireBuilder(applicationContext, channelId, smallIconResId)
        .id(notificationId)
        .asMessagingStyle(allocator)
}

/**
 * Messaging notification builder from existing notifire object
 */
fun extractMessagingStyleBuilderFromNotifire(
    applicationContext: Context,
    notifire: Notifire,
    channelId: String? = null,
    smallIconResId: Int? = null
): MessagingStyleBuilder? {
    return MessagingStyleBuilder.extractMessagingStyleFromNotifire(notifire)
        ?.let { messagingStyle ->
            newNotifireBuilder(applicationContext, channelId, smallIconResId)
                .id(notifire.id)
                .asMessagingStyle { messagingStyle }
        }
}

private fun newNotifireBuilder(
    applicationContext: Context,
    channelId: String? = null,
    smallIconResId: Int? = null,
): Notifire.Builder {
    if (!Notifire.isConfigurationInitialized) {
        throw NotInitializedYetException("Notify.configurations property is not initialized yet.")
    }
    val config = Notifire.configurations
    return Notifire.builder(applicationContext, channelId ?: config.channelId)
        .smallIcon(smallIconResId ?: config.smallIconResId)
}

fun newActionBuilder(
    @DrawableRes iconResId: Int,
    title: CharSequence?,
    intent: PendingIntent?,
): NotificationCompat.Action.Builder {
    return NotificationCompat.Action.Builder(iconResId, title, intent)
}
