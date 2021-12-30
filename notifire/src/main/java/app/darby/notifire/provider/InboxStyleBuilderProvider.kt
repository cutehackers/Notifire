package app.darby.notifire.provider

import app.darby.notifire.InboxStyleBuilder

fun interface InboxStyleBuilderProvider : (InboxStyleBuilder) -> Unit
