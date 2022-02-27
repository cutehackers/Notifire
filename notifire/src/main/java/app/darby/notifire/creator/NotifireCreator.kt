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
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: Notifire.Builder.() -> Unit,
): Notifire.Builder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .id(notificationId)
        .apply(block)
}

fun notificationAsBigTextStyle(
    applicationContext: Context,
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigTextStyleBuilder.() -> Unit,
): BigTextStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .id(notificationId)
        .asBigTextStyle()
        .apply(block)
}

fun notificationAsBigPictureStyle(
    applicationContext: Context,
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: BigPictureStyleBuilder.() -> Unit,
): BigPictureStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .id(notificationId)
        .asBigPictureStyle()
        .apply(block)
}

fun notificationAsInboxStyle(
    applicationContext: Context,
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: InboxStyleBuilder.() -> Unit,
): InboxStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .id(notificationId)
        .asInboxStyle()
        .apply(block)
}

fun notificationAsMessagingStyle(
    applicationContext: Context,
    notificationId: Int,
    smallIconResId: Int? = null,
    channelId: String? = null,
    user: Person,
    block: MessagingStyleBuilder.() -> Unit,
): MessagingStyleBuilder {
    return newNotifire(applicationContext, smallIconResId, channelId)
        .id(notificationId)
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
