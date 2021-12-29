@file:Suppress("unused")

package app.darby.notifire.creator

import androidx.fragment.app.Fragment
import app.darby.notifire.Notifire
import app.darby.notifire.provider.NotifireBuilderProvider

fun Fragment.notification(
    smallIconResId: Int? = null,
    channelId: String? = null,
    block: NotifireBuilderProvider
): Notifire? {
    return activity?.applicationContext?.let {
        notification(
            applicationContext = it,
            smallIconResId = smallIconResId,
            channelId = channelId,
            block
        ).notify()
    }
}
