package dev.olaore.unittestingonandroid.fundamentals_test

import dev.olaore.unittestingonandroid.fundamentals.Interval
import dev.olaore.unittestingonandroid.fundamentals.IntervalsAdjacencyDetector
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class IntervalsAdjacencyDetectorTest {

    lateinit var systemUnderTest: IntervalsAdjacencyDetector

    @Before
    fun setup() {
        systemUnderTest = IntervalsAdjacencyDetector()
    }

    @Test
    fun `test_intervals with adjacent cross start and end values should return true`() {
//        arrange
        val interval1 = Interval(-1, 3)
        val interval2 = Interval(-5, -1)

//        act
        val result = systemUnderTest.isAdjacent(interval1, interval2)

//        assert
        assertThat(result, `is`(true))

    }

    @Test
    fun `test_intervals with adjacent cross end and start values should return true`() {
//        arrange
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(5, 10)

//        act
        val result = systemUnderTest.isAdjacent(interval1, interval2)

//        assert
        assertThat(result, `is`(true))

    }

    @Test
    fun `test_intervals with non-adjacent cross end and start values should return false`() {
//        arrange
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(6, 10)

//        act
        val result = systemUnderTest.isAdjacent(interval1, interval2)

//        assert
        assertThat(result, `is`(false))

    }

    @Test
    fun `test_intervals with overlapping start and end values should return false`() {
//        arrange
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(2, 6)

//        act
        val result = systemUnderTest.isAdjacent(interval1, interval2)

//        assert
        assertThat(result, `is`(false))

    }

    @Test
    fun `test_intervals with overlapping start and end values with extra step should return false`() {
//        arrange
        val interval1 = Interval(-1, 5)
        val interval2 = Interval(-1, 6)

//        act
        val result = systemUnderTest.isAdjacent(interval1, interval2)

//        assert
        assertThat(result, `is`(false))

    }

}