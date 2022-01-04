package app.darby.notifire.style

import android.content.Context
import androidx.core.app.NotificationCompat
import app.darby.notifire.Notifire

open class StyleBuilder<out T : NotificationCompat.Style>(
    context: Context,
    _builder: NotificationCompat.Builder,
    allocator: () -> T,
) : Notifire.Builder(context, _builder) {

    protected val style: T = allocator()

    init {
        _builder.setStyle(style)
    }
}