package app.darby.notifire.style

import android.content.Context
import androidx.core.app.NotificationCompat
import app.darby.notifire.Notifire

open class StyleBuilder<out T : NotificationCompat.Style>(
    context: Context,
    private val _builder: NotificationCompat.Builder,
    allocator: () -> T,
) : Notifire.Builder(context, _builder) {

    protected val style: T = allocator()

    init {
        applyStyle()
    }

    fun applyStyle() = apply {
        _builder.setStyle(style)
    }
}
