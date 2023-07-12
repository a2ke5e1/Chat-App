package com.a2k.chatapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun sameGeneratedString() {
        val r1 = generateChatId("test1", "test2")
        val r2 = generateChatId("test2", "test1")
        assertEquals(r1, r2)
    }
}