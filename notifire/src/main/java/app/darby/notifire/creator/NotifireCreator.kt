@file:Suppress("unused")

package app.darby.notifire.creator

import android.content.Context
import androidx.core.app.Person
import app.darby.notifire.Notifire
import app.darby.notifire.exception.NotInitializedYetException
import app.darby.notifire.style.BigPictureStyleBuilder
import app.darby.notifire.style.BigTextStyleBuilder
import app.darby.notifire.style.InboxStyleBuilder
import app.darby.notifire.style.MessagingStyleBuilder

@Throws(NotInitializedYetException::class)
fun notification(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: Notifire.Builder.() -> Unit,
): Notifire.Builder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .apply(block)
}

fun notificationAsBigTextStyle(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilder.() -> Unit,
): BigTextStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .asBigTextStyle()
        .apply(block)
}

fun notificationAsBigPictureStyle(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilder.() -> Unit,
): BigPictureStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .asBigPictureStyle()
        .apply(block)
}

fun notificationAsInboxStyle(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilder.() -> Unit,
): InboxStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .asInboxStyle()
        .apply(block)
}

@Deprecated(message = "deprecated, use notificationAsMessagingStyle with Person",
    ReplaceWith("notificationAsMessagingStyle with Person argument")
)
fun notificationAsMessagingStyle(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    userDisplayName: CharSequence,
    block: MessagingStyleBuilder.() -> Unit,
): MessagingStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .asMessagingStyle(userDisplayName)
        .apply(block)
}

fun notificationAsMessagingStyle(
    applicationContext: Context,
    smallIconResId: Int? = null,
    channelId: String? = null,
    user: Person,
    block: MessagingStyleBuilder.() -> Unit,
): MessagingStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .asMessagingStyle(user)
        .apply(block)
}

private fun newNotifire(
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
