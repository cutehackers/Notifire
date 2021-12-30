package app.darby.notifire

import android.content.Context
import androidx.core.app.NotificationCompat

class InboxStyleBuilder(context: Context, _builder: NotificationCompat.Builder) :
    StyleBuilder<NotificationCompat.InboxStyle>(
        context,
        _builder,
        { NotificationCompat.InboxStyle() }
    ) {

    /**
     * This title is slightly different than regular title, since I know INBOX_STYLE is
     * available.
     */
    fun bigContentTitle(contentTitle: CharSequence) = apply {
        style.setBigContentTitle(contentTitle)
    }

    /**
     * Summary line after the detail section in the big form of the template.
     * Note: To improve readability, don't overload the user with info. If Summary Text
     * doesn't add critical information, you should skip it.
     */
    fun summaryText(summaryText: CharSequence) = apply {
        style.setSummaryText(summaryText)
    }

    /**
     * Append a line to the digest section of the Inbox notification.
     */
    fun addLine(message: CharSequence) = apply {
        style.addLine(message)
    }
}
