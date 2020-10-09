package dev.olaore.unittestingonandroid.fundamentals_test

import dev.olaore.unittestingonandroid.fundamentals.StringDuplicator
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class StringDuplicatorTest {

    lateinit var systemUnderTest: StringDuplicator

    @Before
    fun setup() {
        systemUnderTest = StringDuplicator()
    }

    @Test
    fun `test_duplicate string text with empty character`() {
        val result = systemUnderTest.duplicate("")
        assertThat(result, `is`(""))
    }

    @Test
    fun `test_duplicate string text with single character`() {
        val result = systemUnderTest.duplicate("b")
        assertThat(result, `is`("bb"))
    }

    @Test
    fun `test_duplicate string text with multiple characters`() {
        val result = systemUnderTest.duplicate("baby")
        assertThat(result, `is`("babybaby"))
    }

}