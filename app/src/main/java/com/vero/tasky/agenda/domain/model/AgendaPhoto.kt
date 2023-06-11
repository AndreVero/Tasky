package com.vero.tasky.agenda.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AgendaPhoto(val path: String) : Parcelable{
    @Parcelize
    data class LocalPhoto(val uri: String) : AgendaPhoto(uri)
    @Parcelize
    data class RemotePhoto(val url: String, val key: String) : AgendaPhoto(url)
}