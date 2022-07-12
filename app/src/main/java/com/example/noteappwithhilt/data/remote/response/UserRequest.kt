package com.example.noteappwithhilt.data.remote.response

data class UserRequest(
    val email: String,
    val password: String,
    val username: String
)