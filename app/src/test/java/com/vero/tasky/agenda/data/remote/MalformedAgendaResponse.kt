package com.vero.tasky.agenda.data.remote

val malformedAgendaResponse = """
    {
    "events: [
    {
    "id": "123",
    "title": "123",
    "description": "123",
    "from": 123456789,
    "to": 123456789,
    "remindAt": 123456789,
    "host": "123",
    "isUserEventCreator": true,
    "attendees": [],
    "photos": []
    }
    ],
    "tasks": [
    {
    "id": "123",
    "title": "123",
    "description": "123",
    "time": 123456789,
    "remindAt": 123456789,
    "isDone": true
    }
    ], 
    "reminders": [
    {
    "id": "123",
    "title": "123",
    "description": "123",
    "time": 123456789,
    "remindAt": 123456789
    }]
    }
""".trimIndent()