package actual.budget.model

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Month
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class YearAndMonthTest {
  @Test
  fun `Convert strings`() = runTest {
    val string = "2024-02"
    val yearAndMonth = YearAndMonth(string)
    assertEquals(expected = 2024, actual = yearAndMonth.year)
    assertEquals(expected = Month.FEBRUARY, actual = yearAndMonth.month)
    assertEquals(expected = string, actual = yearAndMonth.toString())
  }

  @Test
  fun `Convert longs`() = runTest {
    val long = 202402L
    val yearAndMonth = YearAndMonth(long)
    assertEquals(expected = 2024, actual = yearAndMonth.year)
    assertEquals(expected = Month.FEBRUARY, actual = yearAndMonth.month)
    assertEquals(expected = long, actual = yearAndMonth.toLong())

    val converted = YearAndMonth(yearAndMonth.toLong())
    assertEquals(yearAndMonth, converted)
  }

  @Test
  fun `Compare months`() = runTest {
    val feb2024 = YearAndMonth("2024-02")
    val dec2025 = YearAndMonth("2025-12")
    assertTrue(dec2025 > feb2024)
    assertTrue(dec2025 >= feb2024)
    assertFalse(dec2025 <= feb2024)
    assertFalse(dec2025 < feb2024)
  }

  @Test
  fun `Month numbers`() = runTest {
    for (month in Month.entries) {
      val yearAndMonth = YearAndMonth(2024, month)
      val monthNumber = yearAndMonth.monthNumber()
      assertEquals(expected = yearAndMonth, actual = YearAndMonth.fromMonthNumber(monthNumber))
    }
  }
}
