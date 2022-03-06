/*
Copyright 2016 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package app.darby.sample.handler

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.darby.notifire.Notifire
import app.darby.notifire.creator.notificationBuilderAsBigTextStyle
import app.darby.sample.MainActivity
import app.darby.sample.NotifireBuilderCache
import app.darby.sample.R
import app.darby.sample.data.MockDatabase
import java.util.concurrent.TimeUnit

/**
 * Asynchronously handles snooze and dismiss actions for reminder app (and active Notification).
 * Notification for for reminder app uses BigTextStyle.
 */
class BigTextIntentService : IntentService("BigTextIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent(): $intent")

        if (intent != null) {
            when (intent.action) {
                ACTION_DISMISS -> handleActionDismiss()
                ACTION_SNOOZE -> handleActionSnooze()
            }
        }
    }

    /**
     * Handles action Dismiss in the provided background thread.
     */
    private fun handleActionDismiss() {
        Log.d(TAG, "handleActionDismiss()")
        Notifire.cancel(applicationContext, MainActivity.NOTIFICATION_ID)
    }

    /**
     * Handles action Snooze in the provided background thread.
     */
    private fun handleActionSnooze() {
        Log.d(TAG, "handleActionSnooze()")

        // You could use NotificationManager.getActiveNotifications() if you are targeting SDK 23
        // and above, but we are targeting devices with lower SDK API numbers, so we saved the
        // builder globally and get the notification back to recreate it later.
        val builder: Notifire.Builder = NotifireBuilderCache.builder ?: run {
            // Recreate builder from persistent state if app process is killed
            // Note: New builder set globally in the method
            recreateNotifireBuilderWithBigTextStyle(MainActivity.NOTIFICATION_ID).apply {
                NotifireBuilderCache.builder = this
            }
        }

        Notifire.cancel(applicationContext, MainActivity.NOTIFICATION_ID)


        try {
            Thread.sleep(SNOOZE_TIME)
        } catch (ex: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        builder.notify()
    }

    /*
     * This recreates the notification from the persistent state in case the app process was killed.
     * It is basically the same code for creating the Notification from MainActivity.
     */
    private fun recreateNotifireBuilderWithBigTextStyle(
        notificationId: Int
    ): Notifire.Builder {

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

        return notificationBuilderAsBigTextStyle(applicationContext, notificationId)
            // BigTextStyle
            .bigText(bigTextStyleReminderAppData.bigText)
            .bigContentTitle(bigTextStyleReminderAppData.bigContentTitle)
            .summaryText(bigTextStyleReminderAppData.summaryText)
            // General
            .id(notificationId)
            .contentTitle(bigTextStyleReminderAppData.contentTitle)
            .contentText(bigTextStyleReminderAppData.contentText)
            .largeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_alarm_white_48dp))
            .contentIntent(notifyPendingIntent)
            .defaults(NotificationCompat.DEFAULT_ALL)
            .color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))

            // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
            // devices and all Wear devices. If you have more than one notification and
            // you prefer a different summary notification, set a group key and create a
            // summary notification via
            //
            // isGroupSummary(true, GROUP_KEY_YOUR_NAME_HERE)

            .category(NotificationCompat.CATEGORY_REMINDER)
            .priority(bigTextStyleReminderAppData.priority)
            .visibility(bigTextStyleReminderAppData.channelLockscreenVisibility)

            .addActions {
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
    }


    companion object {
        private const val TAG = "BigTextService"

        const val ACTION_DISMISS =
            "com.example.android.wearable.wear.wearnotifications.handlers.action.DISMISS"
        const val ACTION_SNOOZE =
            "com.example.android.wearable.wear.wearnotifications.handlers.action.SNOOZE"

        private val SNOOZE_TIME = TimeUnit.SECONDS.toMillis(5)
    }
}