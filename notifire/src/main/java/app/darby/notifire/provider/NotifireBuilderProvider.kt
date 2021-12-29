package app.darby.notifire.provider

import app.darby.notifire.Notifire

fun interface NotifireBuilderProvider : (Notifire.Builder) -> Unit
