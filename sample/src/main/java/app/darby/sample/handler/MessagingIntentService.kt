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
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import app.darby.notifire.Notifire
import app.darby.notifire.creator.newActionBuilder
import app.darby.notifire.creator.notificationBuilderAsMessagingStyle
import app.darby.notifire.style.MessagingStyleBuilder
import app.darby.sample.MainActivity
import app.darby.sample.NotifireBuilderCache
import app.darby.sample.R
import app.darby.sample.data.MockDatabase

/**
 * Asynchronously handles updating messaging app posts (and active Notification) with replies from
 * user in a conversation. Notification for social app use MessagingStyle.
 */
class MessagingIntentService : IntentService("MessagingIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent(): $intent")

        if (intent != null) {
            if (ACTION_REPLY == intent.action) {
                handleActionReply(getMessage(intent))
            }
        }
    }

    /** Handles action for replying to messages from the notification. */
    private fun handleActionReply(replayCharSequence: CharSequence?) {
        Log.d(TAG, "handleActionReply(): $replayCharSequence")

        if (replayCharSequence != null) {
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
             *  you only need the new/updated information. In our case, the reply from the user
             *  which we already have here.
             *
             *  IMPORTANT NOTE: You shouldn't save/modify the resulting Notification object using
             *  its member variables and/or legacy APIs. If you want to retain anything from update
             *  to update, retain the Builder as option 2 outlines.
             */

            // Retrieves NotificationCompat.Builder used to create initial Notification
            val builder: Notifire.Builder = NotifireBuilderCache.builder ?: run {
                recreateNotifireBuilderWithMessagingStyle(MainActivity.NOTIFICATION_ID).apply {
                    // Because we want this to be a new notification (not updating current notification), we
                    // create a new Builder. Later, we update this same notification, so we need to save this
                    // Builder globally (as outlined earlier).
                    NotifireBuilderCache.builder = this
                }
            }

            // Since we are adding to the MessagingStyle, we need to first retrieve the
            // current MessagingStyle from the Notification itself.
            val notifire = builder.build()

            val messagingStyleBuilder: MessagingStyleBuilder? =
                MessagingStyleBuilder.extractMessagingStyleFromNotification(notifire)
                    ?.let { messagingStyle ->
                        // Create MessagingStyleBuilder with existing messagingStyle
                        notificationBuilderAsMessagingStyle(
                            applicationContext,
                            MainActivity.NOTIFICATION_ID
                        ) {
                            messagingStyle
                        }
                    }
                    ?: run { builder as? MessagingStyleBuilder }

            // Add new message to the MessagingStyle. Set last parameter to null for responses
            // from user.
            messagingStyleBuilder?.run {
                addMessage(
                    replayCharSequence,
                    System.currentTimeMillis(),
                    null as Person?
                )

                // Updates the Notification
                applyStyle()

                // Pushes out the updated Notification
                notify()
            }
        }
    }

    /*
     * Extracts CharSequence created from the RemoteInput associated with the Notification.
     */
    private fun getMessage(intent: Intent): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)?.getCharSequence(EXTRA_REPLY)
    }

    /*
     * This recreates the notification from the persistent state in case the app process was killed.
     * It is basically the same code for creating the Notification from MainActivity.
     */
    private fun recreateNotifireBuilderWithMessagingStyle(notificationId: Int): Notifire.Builder {

        val messagingStyleCommsAppData = MockDatabase.getMessagingStyleData(applicationContext)

        // Set up main Intent for notification.
        val notifyIntent = Intent(this, MessagingMainActivity::class.java)

        // When creating your Intent, you need to take into account the back state, i.e., what
        // happens after your Activity launches and the user presses the back button.

        // There are two options:
        //      1. Regular activity - You're starting an Activity that's part of the application's
        //      normal workflow.

        //      2. Special activity - The user only sees this Activity if it's started from a
        //      notification. In a sense, the Activity extends the notification by providing
        //      information that would be hard to display in the notification itself.

        // Even though this sample's MainActivity doesn't link to the Activity this Notification
        // launches directly, i.e., it isn't part of the normal workflow, a chat app generally
        // always links to individual conversations as part of the app flow, so we will follow
        // option 1.

        // For an example of option 2, check out the BIG_TEXT_STYLE example.

        // For more information, check out our dev article:
        // https://developer.android.com/training/notify-user/navigation.html
        TaskStackBuilder.create(this)
            // Adds the back stack
            .addParentStack(MessagingMainActivity::class.java)
            // Adds the Intent to the top of the stack
            .addNextIntent(notifyIntent)

        // Gets a PendingIntent containing the entire back stack
        val mainPendingIntent =
            PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // 4. Set up RemoteInput, so users can input (keyboard and voice) from notification.

        // Note: For API <24 (M and below) we need to use an Activity, so the lock-screen present
        // the auth challenge. For API 24+ (N and above), we use a Service (could be a
        // BroadcastReceiver), so the user can input from Notification or lock-screen (they have
        // choice to allow) without leaving the notification.

        // Create the RemoteInput specifying this key.
        val replyLabel = getString(R.string.reply_label)
        val remoteInput = RemoteInput.Builder(EXTRA_REPLY)
            .setLabel(replyLabel) // Use machine learning to create responses based on previous messages.
            .setChoices(messagingStyleCommsAppData.replyChoicesBasedOnLastMessage)
            .build()

        // Pending intent =
        //      API <24 (M and below): activity so the lock-screen presents the auth challenge.
        //      API 24+ (N and above): this should be a Service or BroadcastReceiver.
        val replyActionPendingIntent: PendingIntent

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val intent = Intent(this, MessagingIntentService::class.java)
            intent.action = ACTION_REPLY
            replyActionPendingIntent = PendingIntent.getService(this, 0, intent, 0)
        } else {
            replyActionPendingIntent = mainPendingIntent
        }

        val builder = notificationBuilderAsMessagingStyle(
            applicationContext,
            notificationId,
            messagingStyleCommsAppData.me
        )
            // MessagingStyle
            /*
             * <p>This API's behavior was changed in SDK version
             * {@link Build.VERSION_CODES#P}. If your application's target version is
             * less than {@link Build.VERSION_CODES#P}, setting a conversation title to
             * a non-null value will make {@link #isGroupConversation()} return
             * {@code true} and passing {@code null} will make it return {@code false}.
             * This behavior can be overridden by calling
             * {@link #setGroupConversation(boolean)} regardless of SDK version.
             * In {@code P} and above, this method does not affect group conversation
             * settings.
             *
             * In our case, we use the same title.
             */
            .conversationTitle(messagingStyleCommsAppData.contentTitle)
            // Adds all Messages.
            // Note: Messages include the text, timestamp, and sender.
            .addMessages {
                messagingStyleCommsAppData.messages
                // Another example to create message
                //listOf(
                //    newMessage("Sample message", 1L, messagingStyleCommsAppData.me)
                //)
            }
            .isGroupConversation(messagingStyleCommsAppData.isGroupConversation)

            // General
            .contentTitle(messagingStyleCommsAppData.contentTitle)
            .contentText(messagingStyleCommsAppData.contentText)
            .largeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_person_black_48dp))
            .contentIntent(mainPendingIntent)
            .defaults(NotificationCompat.DEFAULT_ALL)
            .color(ContextCompat.getColor(applicationContext, R.color.design_default_color_primary))

            // SIDE NOTE: Auto-bundling is enabled for 4 or more notifications on API 24+ (N+)
            // devices and all Wear devices. If you have more than one notification and
            // you prefer a different summary notification, set a group key and create a
            // summary notification via
            // .isGroupSummary(true, GROUP_KEY_YOUR_NAME_HERE)

            .subText(messagingStyleCommsAppData.numberOfNewMessages.toString())
            .category(NotificationCompat.CATEGORY_MESSAGE)

            // Sets priority for 25 and below. For 26 and above, 'priority' is deprecated for
            // 'importance' which is set in the NotificationChannel. The integers representing
            // 'priority' are different from 'importance', so make sure you don't mix them.
            .priority(messagingStyleCommsAppData.priority)

            // Sets lock-screen visibility for 25 and below. For 26 and above, lock screen
            // visibility is set in the NotificationChannel.
            .visibility(messagingStyleCommsAppData.channelLockscreenVisibility)
            .addAction {
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
        for (name in messagingStyleCommsAppData.participants) {
            name.uri?.also {
                builder.addPerson(it)
            }
        }

        return builder
    }

    companion object {
        private const val TAG = "MessagingIntentService"

        const val ACTION_REPLY =
            "com.example.android.wearable.wear.wearnotifications.handlers.action.REPLY"

        const val EXTRA_REPLY =
            "com.example.android.wearable.wear.wearnotifications.handlers.extra.REPLY"
    }
}