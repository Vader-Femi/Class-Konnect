package com.femi.e_class.domain.use_case

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidateNameTest {

    @Test
    fun `name is blank fails`(){
        val result = ValidateName().execute("")
        assertThat(result).isEqualTo(
            ValidationResult(
                false,
                "Name can't be black"
            )
        )
    }

    @Test
    fun `acceptable name succeed`(){
        val result = ValidateName().execute("a")
        assertThat(result).isEqualTo(
            ValidationResult (
                true
            )
        )
    }
}