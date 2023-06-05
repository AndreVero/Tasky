package com.vero.tasky.agenda.domain.model

interface AgendaItem {
    val id: String
    val title: String
    val description: String?
    val time: Long
    val remindAt: Long
}