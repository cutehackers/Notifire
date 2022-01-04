@file:Suppress("unused")

package app.darby.notifire.creator

import android.content.Context
import androidx.core.app.Person
import androidx.fragment.app.Fragment
import app.darby.notifire.provider.BigPictureStyleBuilderProvider
import app.darby.notifire.provider.BigTextStyleBuilderProvider
import app.darby.notifire.provider.InboxStyleBuilderProvider
import app.darby.notifire.provider.MessagingStyleBuilderProvider
import app.darby.notifire.provider.NotifireBuilderProvider

fun Fragment.notification(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: NotifireBuilderProvider,
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
    block: BigTextStyleBuilderProvider,
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
    block: BigPictureStyleBuilderProvider,
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
    block: InboxStyleBuilderProvider,
) = withApplicationContext {
    notificationAsInboxStyle(
        applicationContext = it,
        smallIconResId,
        channelId,
        block
    ).notify()
}


@Deprecated(message = "deprecated, use notificationAsMessagingStyle with Person",
    ReplaceWith("notificationAsMessagingStyle with Person argument")
)
fun Fragment.notificationAsMessagingStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    userDisplayName: CharSequence,
    block: MessagingStyleBuilderProvider,
) = withApplicationContext {
    notificationAsMessagingStyle(
        applicationContext = it,
        smallIconResId,
        channelId,
        userDisplayName,
        block
    ).notify()
}

fun Fragment.notificationAsMessagingStyle(
    smallIconResId: Int? = null,
    channelId: String? = null,
    user: Person,
    block: MessagingStyleBuilderProvider,
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
