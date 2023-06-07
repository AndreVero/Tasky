package com.vero.tasky.agenda.domain.model
sealed class AgendaPhoto(val path: String) {
    data class LocalPhoto(val uri: String) : AgendaPhoto(uri)
    data class RemotePhoto(val url: String, val key: String) : AgendaPhoto(url)
}