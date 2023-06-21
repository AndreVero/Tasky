package com.vero.tasky.agenda.data.remote

val validAgendaResponse = """
    {
    "events": [
    {
    "id": "123",
    "title": "123",
    "description": "123",
    "from": 123456789,
    "to": 123456789,
    "remindAt": 123456789,
    "host": "123",
    "isUserEventCreator": true,
    "attendees": [ { "email": "1", 
"fullName": "1",
"userId": "1", 
"eventId": "1", 
"isGoing": true, 
"remindAt": 123456789
} ],
    "photos": [{
"key": "1", 
"url": "1" 
}]
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