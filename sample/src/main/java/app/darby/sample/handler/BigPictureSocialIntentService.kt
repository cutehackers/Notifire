package app.darby.sample.handler

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import app.darby.notifire.Notifire
import app.darby.notifire.creator.newActionBuilder
import app.darby.notifire.creator.notificationBuilderAsBigPictureStyle
import app.darby.sample.MainActivity
import app.darby.sample.NotifireBuilderCache
import app.darby.sample.R
import app.darby.sample.data.MockDatabase

/**
 * Asynchronously handles updating social app posts (and active Notification) with comments from
 * user. Notification for social app use BigPictureStyle.
 */
class BigPictureSocialIntentService : IntentService("BigPictureSocialIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent(): $intent")

        if (intent != null) {
            if (ACTION_COMMENT == intent.action) {
                handleActionComment(getMessage(intent))
            }
        }
    }

    /**
     * Handles action for adding a comment from the notification.
     */
    private fun handleActionComment(comment: CharSequence?) {
        Log.d(TAG, "handleActionComment(): $comment")

        if (!comment.isNullOrBlank()) {
            // TODO: Asynchronously save your message to Database and servers.

            /*
             * You have two options for updating your notification (this class uses approach #2):
             *
             *  1. Use a new NotificationCompatBuilder to create the Notification. This approach
             *  requires you to get *ALL* the information that existed in the previous
             *  Notification (and updates) and pass it to the builder. This is the approach used in
             *  the MainActivity.
             *
             *  2. Use the original NotificationCompatBuilder to create the Notification. This
             *  approach requires you to store a reference to the original builder. The benefit is
             *  you only need the new/updated information. In our case, the comment from the user
             *  regarding the post (which we already have here).
             *
             *  IMPORTANT NOTE: You shouldn't save/modify the resulting Notification object using
             *  its member variables and/or legacy APIs. If you want to retain anything from update
             *  to update, retain the Builder as option 2 outlines.
             */

            // Retrieves NotificationCompat.Builder used to create initial Notification
            val builder: Notifire.Builder = NotifireBuilderCache.builder ?: run {
                recreateNotifireBuilderWithBigPictureStyle(MainActivity.NOTIFICATION_ID).apply {
                    // Because we want this to be a new notification (not updating current notification), we
                    // create a new Builder. Later, we update this same notification, so we need to save this
                    // Builder globally (as outlined earlier).
                    NotifireBuilderCache.builder = this
                }
            }

            // Updates active Notification, Adds a line and comment below content in Notification
            builder.remoteInputHistory(arrayOf(comment))

            // Pushes out the updated Notification
            builder.notify()
        }
    }

    /*
     * Extracts CharSequence created from the RemoteInput associated with the Notification.
     */
    private fun getMessage(intent: Intent): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(EXTRA_COMMENT)
    }

    /*
     * This recreates the notification from the persistent state in case the app process was killed.
     * It is basically the same code for creating the Notification from MainActivity.
     */
    private fun recreateNotifireBuilderWithBigPictureStyle(notificationId: Int): Notifire.Builder {

        val bigPictureStyleSocialAppData = MockDatabase.getBigPictureStyleData()

        // Set up main Intent for notification
        val mainIntent = Intent(this, BigPictureSocialMainActivity::class.java)

        TaskStackBuilder.create(this)
            .addParentStack(BigPictureSocialMainActivity::class.java)
            .addNextIntent(mainIntent)

        val mainPendingIntent = PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Set up RemoteInput, so users can input (keyboard and voice) from notification
        val replyLabel = getString(R.string.reply_label)
        val remoteInput = RemoteInput.Builder(EXTRA_COMMENT)
            .setLabel(replyLabel)
            .setChoices(bigPictureStyleSocialAppData.possiblePostResponses)
            .build()

        val replyActionPendingIntent: PendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val intent = Intent(this, BigPictureSocialIntentService::class.java)
                    .apply {
                        action = ACTION_COMMENT
                    }
                PendingIntent.getService(this, 0, intent, 0)
            } else {
                mainPendingIntent
            }

        val builder = notificationBuilderAsBigPictureStyle(applicationContext, notificationId)
            // BigPictureStyle
            .bigPicture(
                BitmapFactory.decodeResource(
                    resources,
                    bigPictureStyleSocialAppData.bigImage
                )
            )
            .bigContentTitle(bigPictureStyleSocialAppData.bigContentTitle)
            .summaryText(bigPictureStyleSocialAppData.summaryText)
            // General
            .contentTitle(bigPictureStyleSocialAppData.contentTitle)
            .settingsText(bigPictureStyleSocialAppData.contentTitle)
            .largeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    R.drawable.ic_person_black_48dp
                )
            )
            .contentIntent(mainPendingIntent)
            .color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))
            .subText(1.toString())
            .category(NotificationCompat.CATEGORY_SOCIAL)
            .priority(NotificationCompat.PRIORITY_HIGH)
            .visibility(NotificationCompat.VISIBILITY_PRIVATE)
            .addAction {
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

        // If the phone is in "Do not disturb mode, the user will still be notified if
        // the sender(s) is starred as a favorite.
        for (name in bigPictureStyleSocialAppData.participants) {
            // TODO create Person.Builder() from Notifire
            builder.addPerson(Person.Builder().setName("name").build())
        }

        return builder
    }


    companion object {
        private const val TAG = "BigPictureService"

        const val ACTION_COMMENT =
            "com.example.android.wearable.wear.wearnotifications.handlers.action.COMMENT"

        const val EXTRA_COMMENT =
            "com.example.android.wearable.wear.wearnotifications.handlers.extra.COMMENT"
    }
}