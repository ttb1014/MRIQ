@file:Suppress("WARNINGS")

package com.vervyle.demo

import org.junit.Test

class BadPracticeTest {

    @Test
    fun badFoo() {
        var map = hashSetOf<ExamRecord>(
            ExamRecord("01.11.2024", 4),
            ExamRecord("21.11.2024", 5),
            ExamRecord("28.11.2024", 3),
            ExamRecord("12.12.2024", 5),
        )
        var size = map
            .filterIndexed { index, it ->
                it.mark != 3 && it.mark != 2
            }
            .size

        println(size)
    }

    class ExamRecord(
        var metadata: String,
        var mark: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this == other) return true
            try {
                other as ExamRecord
                if (this.mark == other.mark) {
                    return true
                } else
                    return false
            } catch (e: Exception) {
                return false
            }
        }

        override fun hashCode(): Int {
            var result = mark
            return result
        }
    }
}