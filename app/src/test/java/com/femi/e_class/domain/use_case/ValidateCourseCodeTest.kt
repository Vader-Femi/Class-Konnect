package com.femi.e_class.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidateCourseCodeTest {

    @Test
    fun `course code is blank fails`(){
        val result = ValidateCourseCode().execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code can't be black"
            )
        )
    }

    @Test
    fun `course code is less that 6 characters fails`(){
        val result = ValidateCourseCode().execute("Csc10")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must be 6 characters long"
            )
        )
    }

    @Test
    fun `course code is more that 6 characters fails`(){
        val result = ValidateCourseCode().execute("Csc1011")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must be 6 characters long"
            )
        )
    }

    @Test
    fun `course code doesn't contain numbers or letters fails`(){
        val result = ValidateCourseCode().execute("./,';*")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must only contain 3 letters and 3 numbers"
            )
        )
    }

    @Test
    fun `course code doesn't contain 3 numbers fails`(){
        val result = ValidateCourseCode().execute("cscc10")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must end with 3 numbers"
            )
        )
    }

    @Test
    fun `course code doesn't contain 3 letters fails`(){
        val result = ValidateCourseCode().execute("cs1101")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Course code must start with 3 letters"
            )
        )
    }

    @Test
    fun `course code is acceptable succeeds`(){
        val result = ValidateCourseCode().execute("csc101")
        assertThat(result).isEqualTo(
            ValidationResult(
                true
            )
        )
    }
}