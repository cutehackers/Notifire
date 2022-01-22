@file:Suppress("unused")

package app.darby.notifire

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.LocusIdCompat
import androidx.core.content.pm.ShortcutInfoCompat
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

        fun channelId(channelId: String) = apply {
            _builder.setChannelId(channelId)
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

        fun contentInfo(info: CharSequence?) = apply {
            _builder.setContentInfo(info)
        }

        fun contentIntent(intent: PendingIntent) = apply {
            _builder.setContentIntent(intent)
        }

        /**
         * Supply a {@link PendingIntent} to send when the notification is cleared by the user
         * directly from the notification panel.  For example, this intent is sent when the user
         * clicks the "Clear all" button, or the individual "X" buttons on notifications.  This
         * intent is not sent when the application calls
         * {@link android.app.NotificationManager#cancel NotificationManager.cancel(int)}.
         */
        fun deleteIntent(intent: PendingIntent) = apply {
            _builder.setDeleteIntent(intent)
        }

        fun fullScreenIntent(intent: PendingIntent?, highPriority: Boolean) = apply {
            _builder.setFullScreenIntent(intent, highPriority)
        }

        /**
         * This provides some additional information that is displayed in the notification. No
         * guarantees are given where exactly it is displayed.
         *
         * <p>This information should only be provided if it provides an essential
         * benefit to the understanding of the notification. The more text you provide the
         * less readable it becomes. For example, an email client should only provide the account
         * name here if more than one email account has been added.</p>
         *
         * <p>As of {@link android.os.Build.VERSION_CODES#N} this information is displayed in the
         * notification header area.</p>
         *
         * <p>On Android versions before {@link android.os.Build.VERSION_CODES#N}
         * this will be shown in the third line of text in the platform notification template.
         * You should not be using {@link #setProgress(int, int, boolean)} at the
         * same time on those versions; they occupy the same place.
         * </p>
         */
        fun subText(text: CharSequence?) = apply {
            _builder.setSubText(text)
        }

        /**
         * Provides text that will appear as a link to your application's settings.
         *
         * <p>This text does not appear within notification {@link Style templates} but may
         * appear when the user uses an affordance to learn more about the notification.
         * Additionally, this text will not appear unless you provide a valid link target by
         * handling {@link #INTENT_CATEGORY_NOTIFICATION_PREFERENCES}.
         *
         * <p>This text is meant to be concise description about what the user can customize
         * when they click on this link. The recommended maximum length is 40 characters.
         *
         * <p>Prior to {@link Build.VERSION_CODES#O} this field has no effect.
         */
        fun settingsText(text: CharSequence?) = apply {
            _builder.setSettingsText(text)
        }

        /**
         * Control whether the timestamp set with {@link #setWhen(long) setWhen} is shown
         * in the content view.
         *
         * <p>For apps targeting {@link android.os.Build.VERSION_CODES#N} and above, this
         * defaults to {@code false}. For earlier apps, the default is {@code true}.
         */
        fun showWhen(show: Boolean) = apply {
            _builder.setShowWhen(show)
        }

        /**
         * Set the time that the event occurred.  Notifications in the panel are
         * sorted by this time.
         *
         * <p>For apps targeting {@link android.os.Build.VERSION_CODES#N} and above, this time is
         * not shown anymore by default and must be opted into using {@link #setShowWhen(boolean)}
         */
        fun whenTimeInMillis(whenMs: Long) = apply {
            _builder.setWhen(whenMs)
        }

        /**
         * Show the {@link Notification#when} field as a stopwatch.
         *
         * Instead of presenting <code>whenTimeInMillis</code> as a timestamp, the notification will show an
         * automatically updating display of the minutes and seconds since <code>when</code>.
         *
         * Useful when showing an elapsed time (like an ongoing phone call).
         *
         * @see android.widget.Chronometer
         * @see Notification#when
         */
        fun usesChronometer(use: Boolean) = apply {
            _builder.setUsesChronometer(use)
        }

        /**
         * Sets the Chronometer to count down instead of counting up.
         *
         * This is only relevant if setUsesChronometer(boolean) has been set to true. If it
         * isn't set the chronometer will count up.
         *
         * @see android.widget.Chronometer
         */
        @RequiresApi(Build.VERSION_CODES.N)
        fun chronometerCountDown(countsDown: Boolean) = apply {
            _builder.setChronometerCountDown(countsDown)
        }

        /**
         * Set primary color (important for Wear 2.0 Notifications).
         */
        fun color(argb: Int) = apply {
            _builder.color = argb
        }

        /**
         * Set whether this notification should be colorized. When set, the color set with
         * setColor(int) will be used as the background color of this notification.
         */
        fun colorized(colorize: Boolean) = apply {
            _builder.setColorized(colorize)
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
         * Set the sound to play.  It will play on the default stream.
         *
         * <p>
         * On some platforms, a notification that is noisy is more likely to be presented
         * as a heads-up notification.
         * </p>
         *
         * <p>On platforms {@link Build.VERSION_CODES#O} and above this value is ignored in favor
         * of the value set on the {@link #setChannelId(String) notification's channel}. On older
         * platforms, this value is still used, so it is still required for apps supporting
         * those platforms.</p>
         *
         * @see NotificationChannelCompat.Builder#setSound(Uri, AudioAttributes)
         */
        fun sound(uri: Uri?) = apply {
            _builder.setSound(uri)
        }

        /**
         * Set the sound to play.  It will play on the stream you supply.
         *
         * <p>
         * On some platforms, a notification that is noisy is more likely to be presented
         * as a heads-up notification.
         * </p>
         *
         * <p>On platforms {@link Build.VERSION_CODES#O} and above this value is ignored in favor
         * of the value set on the {@link #setChannelId(String) notification's channel}. On older
         * platforms, this value is still used, so it is still required for apps supporting
         * those platforms.</p>
         *
         * @see NotificationChannelCompat.Builder#setSound(Uri, AudioAttributes)
         * @see Notification#STREAM_DEFAULT
         * @see AudioManager for the <code>STREAM_</code> constants.
         */
        fun sound(uri: Uri?, @NotificationCompat.StreamType streamType: Int) = apply {
            _builder.setSound(uri, streamType)
        }

        fun silent(silent: Boolean) = apply {
            _builder.setSilent(silent)
        }

        fun onlyAlertOnce(onlyAlertOnce: Boolean) = apply {
            _builder.setOnlyAlertOnce(onlyAlertOnce)
        }

        fun lights(@ColorInt argb: Int, onMs: Int, offMs: Int) = apply {
            _builder.setLights(argb, onMs, offMs)
        }

        fun vibrate(pattern: LongArray?) = apply {
            _builder.setVibrate(pattern)
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

        /**
         * To enable notification group summary
         *  .isGroupSummary(true, GROUP_KEY_YOUR_NAME_HERE)
         * else
         *  .isGroupSummary(false)
         */
        fun isGroupSummary(isGroupSummary: Boolean, groupKey: String? = null) = apply {
            _builder.setGroupSummary(isGroupSummary)
            _builder.setGroup(groupKey)
        }

        /**
         * Sets the group alert behavior for this notification. Use this method to mute this
         * notification if alerts for this notification's group should be handled by a different
         * notification. This is only applicable for notifications that belong to a
         * {@link #setGroup(String) group}. This must be called on all notifications you want to
         * mute. For example, if you want only the summary of your group to make noise, all
         * children in the group should have the group alert behavior {@link #GROUP_ALERT_SUMMARY}.
         *
         * <p> The default value is {@link #GROUP_ALERT_ALL}.</p>
         */
        fun groupAlertBehavior(
            @NotificationCompat.GroupAlertBehavior groupAlertBehavior: Int,
        ) = apply {
            _builder.setGroupAlertBehavior(groupAlertBehavior)
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
         * Supply a replacement Notification whose contents should be shown in insecure contexts
         * (i.e. atop the secure lockscreen). See {@link Notification#visibility} and
         * {@link #VISIBILITY_PUBLIC}.
         *
         * @param notification A replacement notification, presumably with some or all info redacted.
         * @return The same Builder.
         */
        fun publicVersion(notification: Notification?) = apply {
            _builder.setPublicVersion(notification)
        }

        /**
         * Is this notification able to be automatically cancelled?
         */
        fun autoCancel(autoCancel: Boolean) = apply {
            _builder.setAutoCancel(autoCancel)
        }

        /**
         * Sets which icon to display as a badge for this notification.
         * Must be one of BADGE_ICON_NONE, BADGE_ICON_SMALL, BADGE_ICON_LARGE.
         * Note: This value might be ignored, for launchers that don't support badge icons.
         */
        fun badgeIconType(@NotificationCompat.BadgeIconType badgeIconType: Int) = apply {
            _builder.setBadgeIconType(badgeIconType)
        }

        /**
         * Sets the number of items this notification represents.
         *
         * On the latest platforms, this may be displayed as a badge count for Launchers that
         * support badging. Prior to {@link android.os.Build.VERSION_CODES#O} it could be
         * shown in the header. And prior to {@link android.os.Build.VERSION_CODES#N} this was
         * shown in the notification on the right side.
         */
        fun number(number: Int) = apply {
            _builder.setNumber(number)
        }

        /**
         * Sets the "ticker" text which is sent to accessibility services. Prior to
         * {@link Build.VERSION_CODES#LOLLIPOP}, sets the text that is displayed in the status bar
         * when the notification first arrives.
         */
        fun ticker(tickerText: CharSequence?) = apply {
            _builder.setTicker(tickerText)
        }

        /**
         * Set whether this is an ongoing notification.
         *
         * Ongoing notifications cannot be dismissed by the user, so your application or service
         * must take care of canceling them.
         *
         * They are typically used to indicate a background task that the user is actively engaged
         * with (e.g., playing music) or is pending in some way and therefore occupying the device
         * (e.g., a file download, sync operation, active network connection).
         *
         * @see Notification#FLAG_ONGOING_EVENT
         */
        fun ongoing(ongoing: Boolean) = apply {
            _builder.setOngoing(ongoing)
        }

        /**
         * Specifies the time at which this notification should be canceled, if it is not already
         * canceled.
         *
         * No-op on versions prior to {@link android.os.Build.VERSION_CODES#O}.
         */
        fun timeoutAfter(durationMs: Long) = apply {
            _builder.setTimeoutAfter(durationMs)
        }

        /**
         * Set a sort key that orders this notification among other notifications from the
         * same package. This can be useful if an external sort was already applied and an app
         * would like to preserve this. Notifications will be sorted lexicographically using this
         * value, although providing different priorities in addition to providing sort key may
         * cause this value to be ignored.
         *
         * <p>This sort key can also be used to order members of a notification group. See
         * {@link Builder#setGroup}.
         *
         * @see String#compareTo(String)
         */
        fun sortKey(sortKey: String?) = apply {
            _builder.setSortKey(sortKey)
        }

        /**
         * Apply an extender to this notification builder. Extenders may be used to add
         * metadata or change options on this builder.
         */
        fun extend(extender: NotificationCompat.Extender) = apply {
            _builder.extend(extender)
        }

        /**
         * Apply an extender to this notification builder. Extenders may be used to add
         * metadata or change options on this builder.
         */
        fun extend(extender: (NotificationCompat.Builder) -> NotificationCompat.Builder) = apply {
            _builder.extend(extender)
        }

        fun progress(max: Int, progress: Int, indeterminate: Boolean) = apply {
            _builder.setProgress(max, progress, indeterminate)
        }

        fun remoteInputHistory(text: Array<CharSequence>?) = apply {
            _builder.setRemoteInputHistory(text)
        }

        fun customBigContentView(contentView: RemoteViews?) = apply {
            _builder.setCustomBigContentView(contentView)
        }

        fun customContentView(contentView: RemoteViews?) = apply {
            _builder.setCustomContentView(contentView)
        }

        fun customHeadsUpContentView(contentView: RemoteViews?) = apply {
            _builder.setCustomHeadsUpContentView(contentView)
        }

        /**
         * Set whether or not this notification is only relevant to the current device.
         *
         * <p>Some notifications can be bridged to other devices for remote display.
         * This hint can be set to recommend this notification not be bridged.
         */
        fun localOnly(localOnly: Boolean) = apply {
            _builder.setLocalOnly(localOnly)
        }

        fun foregroundServiceBehavior(
            @NotificationCompat.ServiceNotificationBehavior behavior: Int,
        ) = apply {
            _builder.foregroundServiceBehavior = behavior
        }

        /**
         * Sets the {@link BubbleMetadata} that will be used to display app content in a floating
         * window over the existing foreground activity.
         *
         * <p>This data will be ignored unless the notification is posted to a channel that
         * allows {@link android.app.NotificationChannel#canBubble() bubbles}.</p>
         *
         * <p>Notifications allowed to bubble that have valid bubble metadata will display in
         * collapsed state outside of the notification shade on unlocked devices. When a user
         * interacts with the collapsed state, the bubble intent will be invoked and displayed.</p>
         */
        fun bubbleMetadata(data: NotificationCompat.BubbleMetadata?) = apply {
            _builder.bubbleMetadata = data
        }

        /**
         * From Android 11, messaging notifications (those that use {@link MessagingStyle}) that
         * use this method to link to a published long-lived sharing shortcut may appear in a
         * dedicated Conversation section of the shade and may show configuration options that
         * are unique to conversations. This behavior should be reserved for person to person(s)
         * conversations where there is a likely social obligation for an individual to respond.
         * <p>
         * For example, the following are some examples of notifications that belong in the
         * conversation space:
         * <ul>
         * <li>1:1 conversations between two individuals</li>
         * <li>Group conversations between individuals where everyone can contribute</li>
         * </ul>
         * And the following are some examples of notifications that do not belong in the
         * conversation space:
         * <ul>
         * <li>Advertisements from a bot (even if personal and contextualized)</li>
         * <li>Engagement notifications from a bot</li>
         * <li>Directional conversations where there is an active speaker and many passive
         * individuals</li>
         * <li>Stream / posting updates from other individuals</li>
         * <li>Email, document comments, or other conversation types that are not real-time</li>
         * </ul>
         * </p>
         *
         * <p>
         * Additionally, this method can be used for all types of notifications to mark this
         * notification as duplicative of a Launcher shortcut. Launchers that show badges or
         * notification content may then suppress the shortcut in favor of the content of this
         * notification.
         * <p>
         * If this notification has {@link BubbleMetadata} attached that was created with
         * a shortcutId a check will be performed to ensure the shortcutId supplied to bubble
         * metadata matches the shortcutId set here, if one was set. If the shortcutId's were
         * specified but do not match, an exception is thrown.
         *
         * @param shortcutId the {@link ShortcutInfoCompat#getId() id} of the shortcut this
         *                   notification is linked to
         *
         * @see BubbleMetadata.Builder#Builder()
         */
        fun shortcutId(shortcutId: String) = apply {
            _builder.setShortcutId(shortcutId)
        }

        /**
         * Populates this notification with given {@link ShortcutInfoCompat}.
         *
         * <p>Sets {@link androidx.core.content.pm.ShortcutInfoCompat#getId() shortcutId} based on
         * the given shortcut. In addition, it also sets {@link LocusIdCompat locusId} and
         * {@link #setContentTitle(CharSequence) contentTitle} if they were empty.
         *
         */
        fun shortcutInfo(shortcutInfo: ShortcutInfoCompat) = apply {
            _builder.setShortcutInfo(shortcutInfo)
        }

        /**
         * Sets the {@link LocusIdCompat} associated with this notification.
         *
         * <p>This method should be called when the {@link LocusIdCompat} is used in other places
         * (such as {@link androidx.core.content.pm.ShortcutInfoCompat} and
         * {@link android.view.contentcapture.ContentCaptureContext}) so the device's intelligence
         * services can correlate them.
         */
        fun locusId(locusId: LocusIdCompat?) = apply {
            _builder.setLocusId(locusId)
        }

        /**
         * Add a person that is relevant to this notification.
         *
         * <P>
         * Depending on user preferences, this annotation may allow the notification to pass
         * through interruption filters, and to appear more prominently in the user interface.
         * </P>
         *
         * <P>
         * The person should be specified by the {@code String} representation of a
         * {@link android.provider.ContactsContract.Contacts#CONTENT_LOOKUP_URI}.
         * </P>
         *
         * <P>The system will also attempt to resolve {@code mailto:} and {@code tel:} schema
         * URIs.  The path part of these URIs must exist in the contacts database, in the
         * appropriate column, or the reference will be discarded as invalid. Telephone schema
         * URIs will be resolved by {@link android.provider.ContactsContract.PhoneLookup}.
         * </P>
         *
         * @param uri A URI for the person.
         * @see Notification#EXTRA_PEOPLE
         * @deprecated use {@link #addPerson(Person)}
         */
        @Deprecated("deprecated", ReplaceWith("addPerson(person: Person)"))
        fun addPerson(uri: String) = apply {
            _builder.addPerson(uri)
        }

        /**
         * Add a person that is relevant to this notification.
         *
         * <P>
         * Depending on user preferences, this annotation may allow the notification to pass
         * through interruption filters, if this notification is of category {@link #CATEGORY_CALL}
         * or {@link #CATEGORY_MESSAGE}. The addition of people may also cause this notification to
         * appear more prominently in the user interface.
         * </P>
         *
         * <P>
         * A person should usually contain a uri in order to benefit from the ranking boost.
         * However, even if no uri is provided, it's beneficial to provide other people in the
         * notification, such that listeners and voice only devices can announce and handle them
         * properly.
         * </P>
         *
         * @param person the person to add.
         * @see #EXTRA_PEOPLE_LIST
         */
        fun addPerson(person: Person) = apply {
            _builder.addPerson(person)
        }

        fun clearPeople() = apply { _builder.clearPeople() }

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

        fun clearActions() = apply {
            _builder.clearActions()
        }

        /**
         * Determines whether the platform can generate contextual actions for a notification.
         * By default this is true.
         */
        fun allowSystemGeneratedContextualActions(allowed: Boolean) = apply {
            _builder.setAllowSystemGeneratedContextualActions(allowed)
        }

        /**
         * Merge additional metadata into this notification.
         *
         * <p>Values within the Bundle will replace existing extras values in this Builder.
         *
         * @see Notification#extras
         */
        fun addExtras(extras: Bundle) = apply {
            _builder.addExtras(extras)
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
