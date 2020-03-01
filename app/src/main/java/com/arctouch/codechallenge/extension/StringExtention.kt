package com.arctouch.codechallenge.extension

// This is a really simple date formatter. Too poor to real world but enough for our purposes ;)
fun String.formatToBrazillian(): String {
    return this.split("-").reversed().joinToString("/")
}