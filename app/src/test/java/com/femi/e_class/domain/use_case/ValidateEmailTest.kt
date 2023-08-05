package com.femi.e_class.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class ValidateEmailTest {

    @Test
    fun `email is blank fails`(){
        val result = ValidateEmail().execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Email can't be black"
            )
        )
    }

    @Test
    fun `email contains uppercase characters fails`(){
        val result = ValidateEmail().execute("Stealingmoviez@gmail.com")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Email must not contain upper-case letters"
            )
        )
    }

    @Test
    fun `email is invalid fails`(){
        val result = ValidateEmail().execute("stealingmoviez?@gmail.com")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "That's not a valid email"
            )
        )
    }

    @Test
    fun `email is acceptable succeeds`(){
        val result = ValidateEmail().execute("stealingmoviez.@gmail.com")
        assertThat(result).isEqualTo(
            ValidationResult(
                true
            )
        )
    }

}