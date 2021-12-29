package app.darby.notifire.provider

import app.darby.notifire.BigTextStyleBuilder

fun interface BigTextStyleBuilderProvider : (BigTextStyleBuilder) -> Unit
