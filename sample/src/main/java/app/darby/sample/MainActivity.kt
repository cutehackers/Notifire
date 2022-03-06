package app.darby.sample

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
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
import androidx.core.content.ContextCompat
import app.darby.notifire.Notifire
import app.darby.notifire.creator.extractMessagingStyleBuilderFromNotifire
import app.darby.notifire.creator.notificationAsBigTextStyle
import app.darby.notifire.creator.notificationAsMessagingStyle
import app.darby.notifire.style.MessagingStyleBuilder
import app.darby.sample.data.MockDatabase
import app.darby.sample.handler.BigTextIntentService
import app.darby.sample.handler.BigTextMainActivity
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
                    viewContainer,
                    "You need to enable notifications for this app",
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
            //BIG_PICTURE_STYLE -> newBigPictureStyleNotification()
            //INBOX_STYLE -> newInboxStyleNotification()
            //MESSAGING_STYLE -> newMessagingStyleNotification()
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

        // Set up main Intent for notification.
        val notifyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, BigTextMainActivity::class.java).apply {
                // Sets the Activity to start in a new, empty task
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Snooze Action.
        val snoozeIntent = Intent(this, BigTextIntentService::class.java).also {
            it.action = BigTextIntentService.ACTION_SNOOZE
        }
        val snoozePendingIntent =
            PendingIntent.getService(this, 0, snoozeIntent, 0)

        // Dismiss Action.
        val dismissIntent =
            Intent(this, BigTextIntentService::class.java).also {
                it.action = BigTextIntentService.ACTION_DISMISS
            }
        val dismissPendingIntent =
            PendingIntent.getService(this, 0, dismissIntent, 0)

        // Create a BigTextStyle notification with Notifire extension
        notificationAsBigTextStyle(NOTIFICATION_ID) {
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