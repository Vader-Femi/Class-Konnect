package com.femi.e_class.use_case

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.femi.e_class.domain.use_case.ValidateCourseCode
import com.femi.e_class.domain.use_case.ValidationResult
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ValidateCourseCodeTest {

    private lateinit var validateCourseCode: ValidateCourseCode

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        validateCourseCode = ValidateCourseCode(context)
    }

    @Test
    fun courseCodeIsBlankFails() {
        val result = validateCourseCode.execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code can't be black"
            )
        )
    }

    @Test
    fun courseCodeIsLessThat6CharactersFails() {
        val result = validateCourseCode.execute("Csc10")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must be 6 characters long"
            )
        )
    }

    @Test
    fun courseCodeIsMoreThat6CharactersFails() {
        val result = validateCourseCode.execute("Csc1011")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must be 6 characters long"
            )
        )
    }

    @Test
    fun courseCodeDoesntContainNumbersOrLettersFails() {
        val result = validateCourseCode.execute("./,';*")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must only contain 3 letters and 3 numbers"
            )
        )
    }

    @Test
    fun courseCodeDoesntContain3NumbersFails() {
        val result = validateCourseCode.execute("cscc10")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must end with 3 numbers"
            )
        )
    }

    @Test
    fun courseCodeDoesntContain3LettersFails() {
        val result = validateCourseCode.execute("cs1101")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must start with 3 letters"
            )
        )
    }

    @Test
    fun courseCodeIsAcceptableSucceeds() {
        val result = validateCourseCode.execute("csc101")
        assertThat(result).isEqualTo(
            ValidationResult(
                true
            )
        )
    }
}