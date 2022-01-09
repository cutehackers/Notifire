@file:Suppress("unused")

package app.darby.notifire

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import app.darby.notifire.style.BigPictureStyleBuilder
import app.darby.notifire.style.BigTextStyleBuilder
import app.darby.notifire.style.InboxStyleBuilder
import app.darby.notifire.style.MessagingStyleBuilder

/**
 * A notification builder.
 *
 * Usage
 *  Create a notification
 *   val notifire = Notifire.builder(context)
 *     .smallIcon(R.drawable.ic_notification_small)
 *     .contentTitle("Sender")
 *     .contentText("Subject")
 *     .largeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_avatar_white_48dp))
 *     .contentIntent(PendingIntent)
 *     .defaults(NotificationCompat.DEFAULT_ALL)
 *     .color(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
 *     .category(NotificationCompat.CATEGORY_MESSAGE)
 *     .isGroupSummary(true, GROUP_KEY_YOUR_NAME_HERE)
 *     .priority(NotificationCompat.PRIORITY_DEFAULT)
 *     .visibility(NotificationCompat.VISIBILITY_SECRET)
 *     .addAction { newAction(R.drawable.ic_cancel_white_48dp, "Dismiss", intent) }
 *     .autoCancel(true)
 *     .asBigTextStyle()
 *     .bigText("longer text...")
 *     .bigContentTitle("content title in the big form")
 *     .summaryText("content summary text")
 *     .notify()
 *
 *  Cancel notification
 *   Notifire.cancel(applicationContext, notifire)
 *   Notifire.cancel(applicationContext, notifire, "tag")
 *
 *  Cancel all notifications
 *   Notifire.cancelAll(applicationContext)
 *
 *  Cancel as target package
 *   Notifire.cancelAsPackage(applicationContext, "app.junhyounglee.ratchet", null, 0)
 *
 *  Create a notification action
 *
 *   Single action>
 *   val notifire = Notifire.builder(context)
 *     ...
 *     .addAction {
 *       newAction(R.drawable.ic_cancel_white_48dp, "Dismiss", intent)
 *     }
 *     ...
 *     .notify()
 *
 *   Multiple actions>
 *   val notifire = Notifire.builder(context)
 *     ...
 *     .addActions {
 *       listOf(
 *         newAction(R.drawable.ic_cancel_white_48dp, "Dismiss", intent),
 *         newAction(R.drawable.ic_cancel_white_48dp, "Dismiss", intent)
 *       )
 *     }
 *     ...
 *     .notify()
 *
 *  NotifireChannel
 *
 * ref: notification anatomy
 *  https://developer.android.com/guide/topics/ui/notifiers/notifications#Templates
 *
 * @param notification Android notification instance
 * @param id notification id
 */
class Notifire private constructor(
    val notification: Notification,
    val id: Int,
) {
    open class Builder(
        private val context: Context,
        private val _builder: NotificationCompat.Builder,
    ) {
        private var id: Int = Int.MAX_VALUE

        fun id(id: Int) {
            this.id = id
        }

        fun smallIcon(@DrawableRes iconResId: Int) = apply {
            _builder.setSmallIcon(iconResId)
        }

        fun smallIcon(@DrawableRes iconResId: Int, level: Int) = apply {
            _builder.setSmallIcon(iconResId, level)
        }

        /**
         * Title for API < 16 (4.0 and below) devices.
         */
        fun contentTitle(title: CharSequence) = apply {
            _builder.setContentTitle(title)
        }

        /**
         * Content for API < 24 (7.0 and below) devices.
         */
        fun contentText(text: CharSequence) = apply {
            _builder.setContentText(text)
        }

        fun largeIcon(icon: Bitmap) = apply {
            _builder.setLargeIcon(icon)
        }

        fun contentIntent(intent: PendingIntent) = apply {
            _builder.setContentIntent(intent)
        }

        /**
         * NotificationCompat.DEFAULT_ALL
         * NotificationCompat.DEFAULT_SOUND
         * NotificationCompat.DEFAULT_VIBRATE
         * NotificationCompat.DEFAULT_LIGHTS
         */
        fun defaults(defaults: Int) = apply {
            _builder.setDefaults(defaults)
        }

        /**
         * Set primary color (important for Wear 2.0 Notifications).
         */
        fun color(argb: Int) = apply {
            _builder.color = argb
        }

        /**
         * NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
         * devices and all Wear devices. If you have more than one notification and
         * you prefer a different summary notification, set a group key and create a
         * summary notification via
         *  .isGroupSummary(true, GROUP_KEY_YOUR_NAME_HERE) to enable a specific group
         * or
         *  .isGroupSummary(false)
         *
         * @param category NotificationCompat.CATEGORY_*
         */
        fun category(category: String) = apply {
            _builder.setCategory(category)
        }

        fun isGroupSummary(isGroupSummary: Boolean, groupKey: String? = null) = apply {
            _builder.setGroupSummary(isGroupSummary)
            _builder.setGroup(groupKey)
        }

        /**
         * Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
         * 'importance' which is set in the NotificationChannel. The integers representing
         * 'priority' are different from 'importance', so make sure you don't mix them.
         *
         * @param priority: NotificationCompat.PRIORITY_*
         */
        fun priority(priority: Int) = apply {
            _builder.priority = priority
        }

        /**
         * Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
         * visibility is set in the NotificationChannel.
         * @param visibility NotificationCompat.VISIBILITY_*
         */
        fun visibility(visibility: Int) = apply {
            _builder.setVisibility(visibility)
        }

        /**
         * Is this notification able to be automatically cancelled?
         */
        fun autoCancel(autoCancel: Boolean) = apply {
            _builder.setAutoCancel(autoCancel)
        }

        /**
         * Add a single action
         * ex)
         *  addAction {
         *    newAction(R.drawable.ic_launcher_foreground, "", null)
         *  }
         */
        fun addAction(isVisible: Boolean = true, builder: Builder.() -> NotificationCompat.Action) =
            apply {
                if (isVisible) {
                    _builder.addAction(builder())
                } else {
                    _builder.addInvisibleAction(builder())
                }
            }

        /**
         * Add actions
         * ex)
         *  addActions {
         *    listOf(
         *      newAction(R.drawable.ic_launcher_foreground, "", null),
         *      newAction(R.drawable.ic_launcher_foreground, "", null)
         *    )
         *  }
         */
        fun addActions(
            isVisible: Boolean = true,
            builder: Builder.() -> List<NotificationCompat.Action>,
        ) = apply {
            if (isVisible) {
                builder().forEach { _builder.addAction(it) }
            } else {
                builder().forEach { _builder.addInvisibleAction(it) }
            }
        }

        fun newAction(
            @DrawableRes iconResId: Int,
            title: CharSequence?,
            intent: PendingIntent?,
        ): NotificationCompat.Action {
            return NotificationCompat.Action.Builder(iconResId, title, intent).build()
        }

        fun notify(): Notifire {
            return notify(context, Notifire(_builder.build(), id))
        }

        fun notify(tag: String?): Notifire {
            return notify(context, Notifire(_builder.build(), id), tag)
        }

        fun asBigTextStyle() = BigTextStyleBuilder(context, _builder)

        fun asBigPictureStyle() = BigPictureStyleBuilder(context, _builder)

        fun asInboxStyle() = InboxStyleBuilder(context, _builder)

        @Deprecated(message = "deprecated, use fun asMessagingStyle(user: Person)")
        fun asMessagingStyle(userDisplayName: CharSequence) =
            MessagingStyleBuilder(context, _builder, userDisplayName)

        fun asMessagingStyle(user: Person) = MessagingStyleBuilder(context, _builder, user)
    }

    companion object {
        lateinit var configurations: NotifireConfigurations

        val isConfigurationInitialized: Boolean
            get() = ::configurations.isInitialized

        fun initialize(block: NotifireConfigurations.Builder.() -> Unit) {
            configurations = NotifireConfigurations.builder()
                .apply(block)
                .build()
        }

        fun builder(context: Context, channelId: String): Builder {
            return Builder(context, NotificationCompat.Builder(context, channelId))
        }

        internal fun notify(context: Context, notifire: Notifire): Notifire {
            val manager = NotificationManagerCompat.from(context)
            return notifire.apply {
                manager.notify(id, notification)
            }
        }

        internal fun notify(context: Context, notifire: Notifire, tag: String?): Notifire {
            val manager = NotificationManagerCompat.from(context)
            return notifire.apply {
                manager.notify(tag, id, notification)
            }
        }

        fun cancel(context: Context, notifire: Notifire) {
            val manager = NotificationManagerCompat.from(context)
            manager.cancel(notifire.id)
        }

        fun cancel(context: Context, notifire: Notifire, tag: String?) {
            val manager = NotificationManagerCompat.from(context)
            manager.cancel(tag, notifire.id)
        }

        fun cancelAll(context: Context) {
            val manager = NotificationManagerCompat.from(context)
            manager.cancelAll()
        }

        @RequiresApi(Build.VERSION_CODES.R)
        fun cancelAsPackage(context: Context, targetPackage: String, tag: String?, id: Int) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            manager.cancelAsPackage(targetPackage, tag, id)
        }
    }
}
