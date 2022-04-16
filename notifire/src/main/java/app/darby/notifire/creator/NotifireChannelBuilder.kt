package app.darby.notifire.creator

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Creates a notification channel
 *
 * val channel: NotificationChannelCompat =
 *   notificationChannel("notification_channel_id") {
 *     setName("notification_channel_name")
 *     setDescription("notification_channel_description")
 *   }
 *
 * @param onlyCreateIfNotFound will create notification channel when the notification channel does
 * exit exist if it sets true, otherwise it'll create.
 */
fun Context.notificationChannel(
    channelId: String,
    importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
    onlyCreateIfNotFound: Boolean = true,
    block: NotificationChannelCompat.Builder.() -> Unit
) = notificationChannelBuilder(
    channelId,
    importance
).run {
    apply(block)

    if (onlyCreateIfNotFound) {
        createIfNotFound(applicationContext)
    } else {
        create(applicationContext)
    }
}

/**
 * val channel = notificationChannelBuilder(applicationContext)
 *   .setName()
 *   .setDescription()
 *   .create() or .createIfNotFound()
 *
 */
fun notificationChannelBuilder(
    channelId: String,
    importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT
): NotificationChannelCompat.Builder {
    return NotificationChannelCompat.Builder(channelId, importance)
}

fun NotificationChannelCompat.Builder.create(context: Context): NotificationChannelCompat {
    return build().also { channel ->
        NotificationManagerCompat.from(context).createNotificationChannel(channel)
    }
}

/**
 * createChannelIfNotFound method
 *
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 *     val channelId = getString(R.string.notification_channel_id, "default")
 *     val channelName = getString(R.string.notification_default_channel_name)
 *
 *     val mgr: NotificationManager = applicationContext.getSystemService() ?: throw IllegalStateException()
 *     if (mgr.getNotificationChannel(channelId) == null) {
 *         val channel = NotificationChannel(
 *             channelId,
 *             channelName,
 *             NotificationManager.IMPORTANCE_HIGH
 *         ).apply {
 *             description = getString(R.string.notification_default_channel_description)
 *         }
 *
 *         mgr.createNotificationChannel(channel)
 *     }
 * }
 */
fun NotificationChannelCompat.Builder.createIfNotFound(context: Context): NotificationChannelCompat {
    return build().let { channel ->
        NotificationManagerCompat.from(context).run {
            getNotificationChannelCompat(channel.id) ?: channel.also {
                createNotificationChannel(it)
            }
        }
    }
}
