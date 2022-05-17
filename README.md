# Notifire

## Description

Notifire is an android notification builder library that makes it easy to build a notification. And it’s mostly inspired by ‣ project. Basic scheme of the `sample` module is brought from google notification example([https://github.com/android/user-interface-samples/tree/main/Notifications](https://github.com/android/user-interface-samples/tree/main/Notifications)) to test `notifire` module

### Initialize notification module

Notifire requires default notification channel and icon information. Set up the initial configuration from Application context.

```kotlin
// create a default notification channel if not found
val channel = notificationChannel("DEFAULT_NOTIFICATION_CHANNEL_ID") {
    setName(DEFAULT_NOTIFICATION_CHANNEL_NAME)
    setDescription(DEFAULT_NOTIFICATION_CHANNEL_ID_DESCRIPTION)
}

// setup notification configurations
Notifire.initialize {
    smallIconResId(R.drawable.ic_notification_small_white_24dp)
    channelId(channel.id)
}
```

## Create a notification with Kotlin DSL style extension

### A simple notification  builder

```kotlin
notification {
  contentTitle("Subject")
	contentText("Hello people. It's Notifire!")
}
```

### Cancelling a notification

```kotlin
val notifire: Notifire = notification {
  ...
}

Notifire.cancel(context, notifire)
```

### BigTextStyle notification builder

```kotlin
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
}
```

### BigPictureStyle notification builder

```kotlin
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
}
```

### InBoxStyle notification builder

```kotlin
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
}
```

### MessagingStyle notification builder

```kotlin
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
}
```
