package com.femi.e_class.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidateSignUpPasswordTest {
    @Test
    fun `password less that 8 fails`(){
        val result = ValidateSignUpPassword().execute("eight")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Password cannot be less than 8 characters"
            )
        )
    }

    @Test
    fun `password doesn't contain letters fails`(){
        val result = ValidateSignUpPassword().execute("1234567890")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Must contain at least one letter (A-Z or a-z)"
            )
        )
    }

    @Test
    fun `password doesn't contain digits fails`(){
        val result = ValidateSignUpPassword().execute("password")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Must contain at least one digit (0-9)"
            )
        )
    }

    @Test
    fun `password doesn't contain special characters fails`(){
        val result = ValidateSignUpPassword().execute("password2")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Must contain at least 1 special character"
            )
        )
    }

    @Test
    fun `password contains space fails`(){
        val result = ValidateSignUpPassword().execute("password2 .")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Must no contain spaces"
            )
        )
    }

    @Test
    fun `acceptable password succeed`(){
        val result = ValidateSignUpPassword().execute("password2.")
        assertThat(result).isEqualTo(
            ValidationResult (
                true
            )
        )
    }

}