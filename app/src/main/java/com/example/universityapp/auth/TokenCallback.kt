package com.example.universityapp.auth

interface TokenCallback {
    fun onTokenSet(flag: Boolean)
}