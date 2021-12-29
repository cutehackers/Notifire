package app.darby.notifire

import android.content.Context
import androidx.core.app.NotificationCompat

class BigTextStyleBuilder(context: Context, _builder: NotificationCompat.Builder) :
    Notifire.Builder(context, _builder) {

    private val style = NotificationCompat.BigTextStyle()

    init {
        _builder.setStyle(style)
    }

    /**
     * Overrides ContentTitle in the big form of the template.
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
     * Overrides ContentText in the big form of the template.
     */
    fun bigText(longerText: CharSequence) = apply {
        style.bigText(longerText)
    }
}
