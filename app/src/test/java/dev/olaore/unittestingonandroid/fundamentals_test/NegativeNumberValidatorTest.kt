package dev.olaore.unittestingonandroid.fundamentals_test

import dev.olaore.unittestingonandroid.fundamentals.NegativeNumberValidator
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class NegativeNumberValidatorTest {

    lateinit var systemUnderTest: NegativeNumberValidator

    @Before
    fun setup() {
        systemUnderTest = NegativeNumberValidator()
    }

    @Test
    fun `test with positive number should return false`() {
        val result = systemUnderTest.isNegative(1)
        Assert.assertEquals(result, false)
    }

    @Test
    fun `test with neutral value 0 should return false`() {
        val result = systemUnderTest.isNegative(0)
        Assert.assertEquals(result, false)
    }

    @Test
    fun `test with negative value should return true`() {
        val result = systemUnderTest.isNegative(-1)
        Assert.assertEquals(result, true)
    }

}