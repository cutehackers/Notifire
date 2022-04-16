@file:Suppress("unused")

package app.darby.notifire.creator

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.fragment.app.Fragment
import app.darby.notifire.Notifire
import app.darby.notifire.style.BigPictureStyleBuilder
import app.darby.notifire.style.BigTextStyleBuilder
import app.darby.notifire.style.InboxStyleBuilder
import app.darby.notifire.style.MessagingStyleBuilder

/**
 * Simple notification for Fragment
 */
fun Fragment.notification(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: Notifire.Builder.() -> Unit,
) = withApplicationContext {
    notificationBuilder(
        applicationContext = it,
        notificationId,
        channelId,
        smallIconResId
    ).apply(block).notify()
}

/**
 * Big text style notification for Fragment
 */
fun Fragment.notificationAsBigTextStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationBuilderAsBigTextStyle(
        applicationContext = it,
        notificationId,
        channelId,
        smallIconResId
    ).apply(block).notify()
}

/**
 * Big picture style notification for Fragment
 */
fun Fragment.notificationAsBigPictureStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationBuilderAsBigPictureStyle(
        applicationContext = it,
        notificationId,
        channelId,
        smallIconResId
    ).apply(block).notify()
}

/**
 * Inbox style notification for Fragment
 */
fun Fragment.notificationAsInboxStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationBuilderAsInboxStyle(
        applicationContext = it,
        notificationId,
        channelId,
        smallIconResId
    ).apply(block).notify()
}

/**
 * Messaging notification for Fragment
 */
fun Fragment.notificationAsMessagingStyle(
    notificationId: Int,
    user: Person,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: MessagingStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationBuilderAsMessagingStyle(
        applicationContext = it,
        notificationId,
        user,
        channelId,
        smallIconResId,
    ).apply(block).notify()
}

/**
 * Messaging notification builder with existing MessagingStyle
 */
fun Fragment.notificationBuilderAsMessagingStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    allocator: () -> NotificationCompat.MessagingStyle,
    block: MessagingStyleBuilder.() -> Unit
) = withApplicationContext {
    notificationBuilderAsMessagingStyle(
        applicationContext = it,
        notificationId,
        channelId,
        smallIconResId,
        allocator
    ).apply(block).notify()
}

/**
 * Messaging notification builder from existing notifire object
 */
fun Fragment.extractMessagingStyleBuilderFromNotifire(
    notifire: Notifire,
    smallIconResId: Int? = null,
    channelId: String? = null
): MessagingStyleBuilder? = withApplicationContext {
    extractMessagingStyleBuilderFromNotifire(
        it, notifire, channelId, smallIconResId
    )
}

private fun <T> Fragment.withApplicationContext(block: (Context) -> T): T? {
    return activity?.applicationContext?.let {
        block(it)
    }
}
