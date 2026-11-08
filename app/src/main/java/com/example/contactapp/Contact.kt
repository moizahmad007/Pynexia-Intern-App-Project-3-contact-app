package com.example.contactapp

data class Contact(
    val id: Long = 0,
    val name: String,
    val phone: String,
    val email: String = ""
)
