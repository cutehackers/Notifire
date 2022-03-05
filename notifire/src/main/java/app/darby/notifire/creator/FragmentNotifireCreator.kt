@file:Suppress("unused")

package app.darby.notifire.creator

import android.content.Context
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
        smallIconResId,
        channelId
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
        smallIconResId,
        channelId
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
        smallIconResId,
        channelId
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
        smallIconResId,
        channelId
    ).apply(block).notify()
}

/**
 * Messaging notification for Fragment
 */
fun Fragment.notificationAsMessagingStyle(
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    user: Person,
    block: MessagingStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationBuilderAsMessagingStyle(
        applicationContext = it,
        notificationId,
        smallIconResId,
        channelId,
        user,
    ).apply(block).notify()
}

private fun <T> Fragment.withApplicationContext(block: (Context) -> T): T? {
    return activity?.applicationContext?.let {
        block(it)
    }
}
