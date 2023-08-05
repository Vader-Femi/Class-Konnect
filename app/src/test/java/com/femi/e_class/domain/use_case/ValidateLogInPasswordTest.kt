package com.femi.e_class.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidateLogInPasswordTest {

    @Test
    fun `password is blank fails`(){
        val result = ValidateLogInPassword().execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Password cannot be empty"
            )
        )
    }

    @Test
    fun `acceptable password succeed`(){
        val result = ValidateLogInPassword().execute("p")
        assertThat(result).isEqualTo(
            ValidationResult (
                true
            )
        )
    }
}