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
    smallIconResId: Int? = null,
    channelId: String? = null
): Notifire.Builder {
    return newNotifireBuilder(applicationContext, smallIconResId, channelId)
        .id(notificationId)
}

/**
 * Big text style notification builder
 */
fun notificationBuilderAsBigTextStyle(
    applicationContext: Context,
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null
): BigTextStyleBuilder {
    return newNotifireBuilder(applicationContext, smallIconResId, channelId)
        .id(notificationId)
        .asBigTextStyle()
}

/**
 * Big picture style notification builder
 */
fun notificationBuilderAsBigPictureStyle(
    applicationContext: Context,
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null
): BigPictureStyleBuilder {
    return newNotifireBuilder(applicationContext, smallIconResId, channelId)
        .id(notificationId)
        .asBigPictureStyle()
}

/**
 * Inbox style notification builder
 */
fun notificationBuilderAsInboxStyle(
    applicationContext: Context,
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null
): InboxStyleBuilder {
    return newNotifireBuilder(applicationContext, smallIconResId, channelId)
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
    smallIconResId: Int? = null,
    channelId: String? = null
): MessagingStyleBuilder {
    return newNotifireBuilder(applicationContext, smallIconResId, channelId)
        .id(notificationId)
        .asMessagingStyle(user)
}

/**
 * Messaging notification builder with existing MessagingStyle
 */
fun notificationBuilderAsMessagingStyle(
    applicationContext: Context,
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    allocator: () -> NotificationCompat.MessagingStyle,
): MessagingStyleBuilder {
    return newNotifireBuilder(applicationContext, smallIconResId, channelId)
        .id(notificationId)
        .asMessagingStyle(allocator)
}

private fun newNotifireBuilder(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
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
