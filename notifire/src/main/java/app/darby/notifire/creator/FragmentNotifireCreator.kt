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

fun Fragment.notification(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: Notifire.Builder.() -> Unit,
) = withApplicationContext {
    notification(
        applicationContext = it,
        smallIconResId,
        channelId,
        block
    ).notify()
}

fun Fragment.notificationAsBigTextStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationAsBigTextStyle(
        applicationContext = it,
        smallIconResId,
        channelId,
        block
    ).notify()
}

fun Fragment.notificationAsBigPictureStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationAsBigPictureStyle(
        applicationContext = it,
        smallIconResId,
        channelId,
        block
    ).notify()
}

fun Fragment.notificationAsInboxStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationAsInboxStyle(
        applicationContext = it,
        smallIconResId,
        channelId,
        block
    ).notify()
}

fun Fragment.notificationAsMessagingStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    user: Person,
    block: MessagingStyleBuilder.() -> Unit,
) = withApplicationContext {
    notificationAsMessagingStyle(
        applicationContext = it,
        smallIconResId,
        channelId,
        user,
        block
    ).notify()
}

private fun <T> Fragment.withApplicationContext(block: (Context) -> T): T? {
    return activity?.applicationContext?.let {
        block(it)
    }
}
