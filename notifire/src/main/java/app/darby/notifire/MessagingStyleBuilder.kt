package app.darby.notifire

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.Person

/**
 * MessagingStyle notification builder
 */
class MessagingStyleBuilder : StyleBuilder<NotificationCompat.MessagingStyle> {

    @Deprecated("deprecated, use constructor with Person")
    constructor(
        context: Context,
        _builder: NotificationCompat.Builder,
        userDisplayName: CharSequence
    ) : super(
        context,
        _builder,
        { NotificationCompat.MessagingStyle(userDisplayName) }
    )

    constructor(
        context: Context,
        _builder: NotificationCompat.Builder,
        user: Person
    ) : super(
        context,
        _builder,
        { NotificationCompat.MessagingStyle(user) }
    )

    /**
     * Sets the title to be displayed on this conversation. May be set to {@code null}.
     *
     * <p>This API's behavior was changed in SDK version {@link Build.VERSION_CODES#P}. If your
     * application's target version is less than {@link Build.VERSION_CODES#P}, setting a
     * conversation title to a non-null value will make {@link #isGroupConversation()} return
     * {@code true} and passing {@code null} will make it return {@code false}. This behavior
     * can be overridden by calling {@link #setGroupConversation(boolean)} regardless of SDK
     * version. In {@code P} and above, this method does not affect group conversation settings.
     *
     * @param conversationTitle Title displayed for this conversation
     * @return this object for method chaining
     */
    fun conversationTitle(conversationTitle: CharSequence) = apply {
        style.conversationTitle = conversationTitle
        style.messages
    }

    @Deprecated(message = "deprecated, use addMessage method with Person",
        replaceWith = ReplaceWith("addMessage(text: CharSequence, timestamp: Long, sender: Person)"))
    fun addMessage(text: CharSequence?, timestamp: Long, sender: CharSequence?) = apply {
        style.addMessage(text, timestamp, sender)
    }

    fun addMessage(text: CharSequence?, timestamp: Long, sender: Person?) = apply {
        style.addMessage(text, timestamp, sender)
    }

    fun addMessage(message: NotificationCompat.MessagingStyle.Message) = apply {
        style.addMessage(message)
    }

    /**
     * Adds a {@link Message} for historic context in this notification.
     *
     * <p>Messages should be added as historic if they are not the main subject of the
     * notification but may give context to a conversation. The system may choose to present
     * them only when relevant, e.g. when replying to a message through a {@link RemoteInput}.
     *
     * <p>The messages should be added in chronologic order, i.e. the oldest first,
     * the newest last.
     *
     * @param message The historic {@link Message} to be added
     * @return this object for method chaining
     */
    fun addHistoricMessage(message: NotificationCompat.MessagingStyle.Message) = apply {
        style.addHistoricMessage(message)
    }

    fun isGroupConversation(isGroupConversation: Boolean) = apply {
        style.isGroupConversation = isGroupConversation
    }
}