package app.darby.sample

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import app.darby.notifire.Notifire
import app.darby.notifire.creator.newActionBuilder
import app.darby.notifire.creator.notificationAsBigPictureStyle
import app.darby.notifire.creator.notificationAsBigTextStyle
import app.darby.notifire.creator.notificationAsInboxStyle
import app.darby.notifire.creator.notificationAsMessagingStyle
import app.darby.notifire.creator.notificationChannel
import app.darby.sample.data.MockDatabase
import app.darby.sample.handler.BigPictureSocialIntentService
import app.darby.sample.handler.BigPictureSocialMainActivity
import app.darby.sample.handler.BigTextIntentService
import app.darby.sample.handler.BigTextMainActivity
import app.darby.sample.handler.InboxMainActivity
import app.darby.sample.handler.MessagingIntentService
import app.darby.sample.handler.MessagingMainActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var viewContainer: View
    private lateinit var detailsView: TextView

    private lateinit var notificationMgr: NotificationManagerCompat
    private var currentStylePosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationMgr = NotificationManagerCompat.from(applicationContext)
        setUpView()
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected(): position: $position, id: $id")
        currentStylePosition = position
        detailsView.text = NOTIFICATION_STYLES_DESCRIPTION[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun setUpView() {
        viewContainer = findViewById(R.id.viewContainer)
        detailsView = findViewById(R.id.detailsView)
        findViewById<View>(R.id.launchView).setOnClickListener { onLaunchClick() }
        findViewById<Spinner>(R.id.notificationStyleSpinner).apply {
            adapter = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_item,
                NOTIFICATION_STYLES
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            onItemSelectedListener = this@MainActivity
        }
    }

    private fun onLaunchClick() {
        if (!notificationMgr.areNotificationsEnabled()) {
            Snackbar
                .make(
                    viewContainer, "You need to enable notifications for this app",
                    Snackbar.LENGTH_LONG
                )
                .setAction("ENABLE") {
                    // Links to this app's notification settings
                    openNotificationSettingsForApp()
                }
                .show()
            return
        }

        when (NOTIFICATION_STYLES[currentStylePosition]) {
            BIG_TEXT_STYLE -> notifyAsBigTextStyle()
            BIG_PICTURE_STYLE -> notifyAsBigPictureStyle()
            INBOX_STYLE -> notifyAsInboxStyle()
            MESSAGING_STYLE -> notifyAsMessagingStyle()
            else -> {}
        }

        // method 2.
//        notification {
//            contentTitle("Subject")
//            contentText("Hello people. It's Notifire!")
//        }
//
//        val user = Person.Builder().setName("Darby").build()
//        notificationAsMessagingStyle(user = user) {
//
//        }
    }

    /*
     * Generates a BIG_TEXT_STYLE Notification that supports both phone/tablet and wear. For devices
     * on API level 16 (4.1.x - Jelly Bean) and after, displays BIG_TEXT_STYLE. Otherwise, displays
     * a basic notification.
     */
    private fun notifyAsBigTextStyle() {
        Log.d(TAG, "notifying BigTextStyle notification message")

        val bigTextStyleReminderAppData = MockDatabase.getBigTextStyleData()

        // Create or retrieve notification channel
        val channelId = getOrCreateNotificationChannel(bigTextStyleReminderAppData)

        // Set up main Intent for notification.
        val notifyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, BigTextMainActivity::class.java).apply {
                // Sets the Activity to start in a new, empty task
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Snooze Action.
        val snoozeIntent = Intent(this, BigTextIntentService::class.java).also {
            it.action = BigTextIntentService.ACTION_SNOOZE
        }
        val snoozePendingIntent =
            PendingIntent.getService(this, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE)

        // Dismiss Action.
        val dismissIntent =
            Intent(this, BigTextIntentService::class.java).also {
                it.action = BigTextIntentService.ACTION_DISMISS
            }
        val dismissPendingIntent =
            PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_IMMUTABLE)

        // Create a BigTextStyle notification with Notifire extension
        notificationAsBigTextStyle(NOTIFICATION_ID, channelId) {
            // BigTextStyle
            bigText(bigTextStyleReminderAppData.bigText)
            bigContentTitle(bigTextStyleReminderAppData.bigContentTitle)
            summaryText(bigTextStyleReminderAppData.summaryText)

            // General
            contentTitle(bigTextStyleReminderAppData.contentTitle)
            contentText(bigTextStyleReminderAppData.contentText)
            largeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_alarm_white_48dp))
            contentIntent(notifyPendingIntent)
            defaults(NotificationCompat.DEFAULT_ALL)
            color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))

            // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
            // devices and all Wear devices. If you have more than one notification and
            // you prefer a different summary notification, set a group key and create a
            // summary notification via
            //
            // isGroupSummary(true, GROUP_KEY_YOUR_NAME_HERE)

            category(NotificationCompat.CATEGORY_REMINDER)
            priority(bigTextStyleReminderAppData.priority)
            visibility(bigTextStyleReminderAppData.channelLockscreenVisibility)

            addActions {
                listOf(
                    // Snooze Action.
                    newAction(
                        R.drawable.ic_alarm_white_48dp,
                        "Snooze",
                        snoozePendingIntent
                    ),
                    // Dismiss Action.
                    newAction(
                        R.drawable.ic_cancel_white_48dp,
                        "Dismiss",
                        dismissPendingIntent
                    )
                )
            }

            // Because we want this to be a new notification (not updating a previous notification), we
            // create a new Builder. Later, we use the same global builder to get back the notification
            // we built here for the snooze action, that is, canceling the notification and relaunching
            // it several seconds later.
            NotifireBuilderCache.builder = this
        }
    }

    /*
     * Generates a BIG_PICTURE_STYLE Notification that supports both phone/tablet and wear. For
     * devices on API level 16 (4.1.x - Jelly Bean) and after, displays BIG_PICTURE_STYLE.
     * Otherwise, displays a basic notification.
     *
     * This example Notification is a social post. It allows updating the notification with
     * comments/responses via RemoteInput and the BigPictureSocialIntentService on 24+ (N+) and
     * Wear devices.
     */
    private fun notifyAsBigPictureStyle() {
        Log.d(TAG, "notifying BigPictureStyle notification message")

        val bigPictureStyleSocialAppData = MockDatabase.getBigPictureStyleData()

        // Create or retrieve notification channel
        val channelId = getOrCreateNotificationChannel(bigPictureStyleSocialAppData)

        // Set up main Intent for notification
        val mainIntent = Intent(this, BigPictureSocialMainActivity::class.java)

        TaskStackBuilder.create(this)
            .addParentStack(BigPictureSocialMainActivity::class.java)
            .addNextIntent(mainIntent)

        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set up RemoteInput, so users can input (keyboard and voice) from notification
        val replyLabel = getString(R.string.reply_label)
        val remoteInput = RemoteInput.Builder(BigPictureSocialIntentService.EXTRA_COMMENT)
            .setLabel(replyLabel)
            // List of quick response choices for any wearables paired with the phone
            .setChoices(bigPictureStyleSocialAppData.possiblePostResponses)
            .build()

        val replyActionPendingIntent: PendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val intent = Intent(this, BigPictureSocialIntentService::class.java)
                    .apply {
                        action = BigPictureSocialIntentService.ACTION_COMMENT
                    }
                PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_MUTABLE)
            } else {
                mainPendingIntent
            }

        // Create a BigPictureStyle notification with Notifire extension
        notificationAsBigPictureStyle(NOTIFICATION_ID, channelId) {
            // BigPictureStyle
            bigPicture(
                BitmapFactory.decodeResource(
                    resources,
                    bigPictureStyleSocialAppData.bigImage
                )
            )
            bigContentTitle(bigPictureStyleSocialAppData.bigContentTitle)
            summaryText(bigPictureStyleSocialAppData.summaryText)

            // General
            contentTitle(bigPictureStyleSocialAppData.contentTitle)
            settingsText(bigPictureStyleSocialAppData.contentTitle)
            largeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_person_black_48dp
                )
            )
            contentIntent(mainPendingIntent)
            color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))
            subText(1.toString())

            // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
            // devices and all Wear devices. If you have more than one notification and
            // you prefer a different summary notification, set a group key and create a
            // summary notification via
            //
            // isGroupSummary(true, GROUP_KEY_YOUR_NAME_HERE)

            category(NotificationCompat.CATEGORY_SOCIAL)
            priority(NotificationCompat.PRIORITY_HIGH)
            visibility(NotificationCompat.VISIBILITY_PRIVATE)

            addAction {
                // Create action with action builder
                newActionBuilder(
                    R.drawable.ic_reply_white_18dp,
                    replyLabel,
                    replyActionPendingIntent
                ).run {
                    addRemoteInput(remoteInput)
                    build()
                }
            }

            // Because we want this to be a new notification (not updating a previous notification), we
            // create a new Builder. Later, we use the same global builder to get back the notification
            // we built here for a comment on the post.
            NotifireBuilderCache.builder = this
        }
    }

    /*
     * Generates a INBOX_STYLE Notification that supports both phone/tablet and wear. For devices
     * on API level 16 (4.1.x - Jelly Bean) and after, displays INBOX_STYLE. Otherwise, displays a
     * basic notification.
     */
    private fun notifyAsInboxStyle() {
        Log.d(TAG, "notifying InboxStyle notification message")

        val inboxStyleEmailAppData = MockDatabase.getInboxStyleData()

        // Create or retrieve notification channel
        val channelId = getOrCreateNotificationChannel(inboxStyleEmailAppData)

        // Set up main Intent for notification.
        val mainIntent = Intent(this, InboxMainActivity::class.java)
        TaskStackBuilder.create(this)
            .addParentStack(InboxMainActivity::class.java)
            .addNextIntent(mainIntent)

        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Create a InboxStyle notification with Notifire extension
        notificationAsInboxStyle(NOTIFICATION_ID, channelId) {
            // InboxStyle
            bigContentTitle(inboxStyleEmailAppData.bigContentTitle)
            summaryText(inboxStyleEmailAppData.summaryText)

            // Add each summary line of the new emails, you can add up to 5.
            for (summary in inboxStyleEmailAppData.individualEmailSummary) {
                addLine(summary)
            }

            // General
            contentTitle(inboxStyleEmailAppData.contentTitle)
            contentText(inboxStyleEmailAppData.contentText)
            largeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_person_black_48dp
                )
            )
            contentIntent(mainPendingIntent)
            defaults(NotificationCompat.DEFAULT_ALL)
            color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))
            subText(inboxStyleEmailAppData.numberOfNewEmails.toString())

            // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
            // devices and all Wear devices. If you have more than one notification and
            // you prefer a different summary notification, set a group key and create a
            // summary notification via
            //
            // isGroupSummary(true, GROUP_KEY_YOUR_NAME_HERE)

            category(NotificationCompat.CATEGORY_EMAIL)
            priority(inboxStyleEmailAppData.priority)
            visibility(inboxStyleEmailAppData.channelLockscreenVisibility)

            // If the phone is in "Do not disturb mode, the user will still be notified if
            // the sender(s) is starred as a favorite.
            addPeople(inboxStyleEmailAppData.participants)

            // Because we want this to be a new notification (not updating a previous notification), we
            // create a new Builder. However, we don't need to update this notification later, so we
            // will not need to set a global builder for access to the notification later.
            NotifireBuilderCache.builder = this
        }
    }

    /*
     * Generates a MESSAGING_STYLE Notification that supports both phone/tablet and wear. For
     * devices on API level 24 (7.0 - Nougat) and after, displays MESSAGING_STYLE. Otherwise,
     * displays a basic BIG_TEXT_STYLE.
     */
    private fun notifyAsMessagingStyle() {
        Log.d(TAG, "notifying Messaging notification message")

        val messagingStyleCommsAppData = MockDatabase.getMessagingStyleData(applicationContext)

        // Create or retrieve notification channel
        val channelId = getOrCreateNotificationChannel(messagingStyleCommsAppData)

        // Set up main Intent for notification.
        val notifyIntent = Intent(this, MessagingMainActivity::class.java)
        TaskStackBuilder.create(this)
            // Adds the back stack
            .addParentStack(MessagingMainActivity::class.java)
            // Adds the Intent to the top of the stack
            .addNextIntent(notifyIntent)

        // Gets a PendingIntent containing the entire back stack
        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set up RemoteInput, so users can input (keyboard and voice) from notification.

        // Note: For API <24 (M and below) we need to use an Activity, so the lock-screen present
        // the auth challenge. For API 24+ (N and above), we use a Service (could be a
        // BroadcastReceiver), so the user can input from Notification or lock-screen (they have
        // choice to allow) without leaving the notification.

        // Create the RemoteInput specifying this key.
        val replyLabel = getString(R.string.reply_label)
        val remoteInput = RemoteInput.Builder(MessagingIntentService.EXTRA_REPLY)
            .setLabel(replyLabel) // Use machine learning to create responses based on previous messages.
            .setChoices(messagingStyleCommsAppData.replyChoicesBasedOnLastMessage)
            .build()

        // Pending intent =
        //      API <24 (M and below): activity so the lock-screen presents the auth challenge.
        //      API 24+ (N and above): this should be a Service or BroadcastReceiver.
        val replyActionPendingIntent: PendingIntent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val intent = Intent(this, MessagingIntentService::class.java)
            intent.action = MessagingIntentService.ACTION_REPLY
            replyActionPendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        } else {
            replyActionPendingIntent = mainPendingIntent
        }

        // Create a MessagingStyle notification with Notifire extension
        notificationAsMessagingStyle(NOTIFICATION_ID, messagingStyleCommsAppData.me, channelId) {
            // MessagingStyle
            conversationTitle(messagingStyleCommsAppData.contentTitle)
            addMessages {
                messagingStyleCommsAppData.messages
                // Another example to create messages
                //listOf(
                //    newMessage("Sample message", 1L, messagingStyleCommsAppData.me)
                //)
            }
            isGroupConversation(messagingStyleCommsAppData.isGroupConversation)

            // General
            contentTitle(messagingStyleCommsAppData.contentTitle)
            contentText(messagingStyleCommsAppData.contentText)
            largeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_person_black_48dp))
            contentIntent(mainPendingIntent)
            defaults(NotificationCompat.DEFAULT_ALL)
            color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))

            subText(messagingStyleCommsAppData.numberOfNewMessages.toString())
            category(NotificationCompat.CATEGORY_MESSAGE)

            // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
            // 'importance' which is set in the NotificationChannel. The integers representing
            // 'priority' are different from 'importance', so make sure you don't mix them.
            priority(messagingStyleCommsAppData.priority)

            // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
            // visibility is set in the NotificationChannel.
            visibility(messagingStyleCommsAppData.channelLockscreenVisibility)

            addAction {
                newActionBuilder(
                    R.drawable.ic_reply_white_18dp,
                    replyLabel,
                    replyActionPendingIntent
                ).run {
                    addRemoteInput(remoteInput)
                    // Informs system we aren't bringing up our own custom UI for a reply
                    // action.
                    setShowsUserInterface(false)
                    // Allows system to generate replies by context of conversation.
                    setAllowGeneratedReplies(true)
                    setSemanticAction(NotificationCompat.Action.SEMANTIC_ACTION_REPLY)
                    build()
                }
            }

            // If the phone is in "Do not disturb" mode, the user may still be notified if the
            // sender(s) are in a group allowed through "Do not disturb" by the user.
            addPeople(messagingStyleCommsAppData.participants)

            // Because we want this to be a new notification (not updating current notification), we
            // create a new Builder. Later, we update this same notification, so we need to save this
            // Builder globally (as outlined earlier).
            NotifireBuilderCache.builder = this
        }
    }

    /**
     * Get a notification channel or Create it if not found
     * @return notification channel id
     */
    private fun getOrCreateNotificationChannel(channelData: MockDatabase.MockNotificationData): String {
        val channel = notificationChannel(channelData.channelId, channelData.channelImportance) {
            setName(channelData.channelName)
            setDescription(channelData.channelDescription)
            setVibrationEnabled(channelData.isChannelEnableVibrate)
        }
        return channel.id
    }

    private fun notifyWithBuilder() {
        val defaultChannelId = getString(R.string.notification_channel_id, "default")
        val notifire = Notifire.builder(this, defaultChannelId)
            .smallIcon(R.drawable.ic_notification_small_white_24dp)
            .contentTitle("Sender")
            .contentText("Subject")
            .largeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_people_black_48dp))
            //.contentIntent()
            .defaults(NotificationCompat.DEFAULT_ALL)
            .color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))
            .category(NotificationCompat.CATEGORY_MESSAGE)
            .isGroupSummary(true, "GROUP_KEY_YOUR_NAME_HERE")
            .priority(NotificationCompat.PRIORITY_DEFAULT)
            .visibility(NotificationCompat.VISIBILITY_SECRET)
            //.addAction {
            //    newAction(R.drawable.ic_launcher_foreground, "", null)
            //}
            // asBigTextStyle() will provide methods for BigTextStyle notification
            .asBigTextStyle()
            .bigText("longer comments")
            .bigContentTitle("content title in the big form")
            .summaryText("content summary text")
            .notify()

        Notifire.cancel(applicationContext, notifire)
    }

    /**
     * Helper method for the SnackBar action, i.e., if the user has this application's notifications
     * disabled, this opens up the dialog to turn them back on after the user requests a
     * Notification launch.
     *
     * IMPORTANT NOTE: You should not do this action unless the user takes an action to see your
     * Notifications like this sample demonstrates. Spamming users to re-enable your notifications
     * is a bad idea.
     */
    private fun openNotificationSettingsForApp() {
        // Links to this app's notification settings.
        val intent = Intent().apply {
            action = "android.settings.APP_NOTIFICATION_SETTINGS"
            putExtra("app_package", packageName)
            putExtra("app_uid", applicationInfo.uid)

            // for Android 8 and above
            putExtra("android.provider.extra.APP_PACKAGE", packageName)
        }
        startActivity(intent)
    }

    companion object {
        const val TAG = "MainActivity"

        const val NOTIFICATION_ID = 0

        // Used for Notification Style array and switch statement for Spinner selection.
        private const val BIG_TEXT_STYLE = "BIG_TEXT_STYLE"
        private const val BIG_PICTURE_STYLE = "BIG_PICTURE_STYLE"
        private const val INBOX_STYLE = "INBOX_STYLE"
        private const val MESSAGING_STYLE = "MESSAGING_STYLE"

        // Collection of notification styles to back ArrayAdapter for Spinner.
        private val NOTIFICATION_STYLES = arrayOf(
            BIG_TEXT_STYLE, BIG_PICTURE_STYLE, INBOX_STYLE, MESSAGING_STYLE
        )

        private val NOTIFICATION_STYLES_DESCRIPTION = arrayOf(
            "Demos reminder type app using BIG_TEXT_STYLE",
            "Demos social type app using BIG_PICTURE_STYLE + inline notification response",
            "Demos email type app using INBOX_STYLE",
            "Demos messaging app using MESSAGING_STYLE + inline notification responses"
        )
    }
}
