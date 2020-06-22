package com.arctouch.codechallenge.com.arctouch.codechallenge.repository.factory

import java.util.*

object PrimitiveFactory {

    fun randomInt(max: Int = 1000) = (0..max).random()

    fun randomString(): String = UUID.randomUUID().toString()
}