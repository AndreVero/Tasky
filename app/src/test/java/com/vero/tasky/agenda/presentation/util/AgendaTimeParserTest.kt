package com.vero.tasky.agenda.presentation.util

import com.google.common.truth.Truth.assertThat
import com.vero.tasky.agenda.domain.model.AgendaItem
import org.junit.Test
import java.time.LocalDateTime

class AgendaTimeParserTest {

    @Test
    fun `Get timeline for event, result success`() {
        val event = AgendaItem.Event(
            id = "",
            title = "",
            description = null,
            remindAt = LocalDateTime.of(2023, 1,1,1,1),
            time = LocalDateTime.of(2023, 1,1,1,1),
            to = LocalDateTime.of(2023, 1,1,2,1),
            host = "",
            isUserEventCreator = true,
            attendees = emptyList(),
            photos = emptyList()
        )

        val result = AgendaTimeParser.getTimeLine(event)
        assertThat(result).isEqualTo("Jan 1, 01:01 - Jan 1, 02:01")
    }

    @Test
    fun `Get timeline for task, result success`() {
        val task = AgendaItem.Task(
            id = "",
            title = "",
            description = "",
            remindAt = LocalDateTime.of(2023, 1,1,1,1),
            time = LocalDateTime.of(2023, 1,1,1,1),
            isDone = false
        )
        val result = AgendaTimeParser.getTimeLine(task)
        assertThat(result).isEqualTo("Jan 1, 01:01")
    }

    @Test
    fun `Get timeline for reminder, result success`() {
        val reminder = AgendaItem.Reminder(
            id = "",
            title = "",
            description = "",
            remindAt =  LocalDateTime.of(2023, 1,1,1,1),
            time =  LocalDateTime.of(2023, 1,1,1,1)
        )
        val result = AgendaTimeParser.getTimeLine(reminder)
        assertThat(result).isEqualTo("Jan 1, 01:01")
    }


}